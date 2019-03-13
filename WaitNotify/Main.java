package cn.singleqi;

import org.springframework.util.CollectionUtils;

import java.util.ArrayDeque;
import java.util.Queue;

import static cn.singleqi.Main.SIZE;

/**
 * wait、notify、notifyAll的生产消费者
 * wait释放当前对象锁（Object的方法），线程等待对象的Notify唤醒，sleep不释放对象锁（Thread的方法），线程等待时间
 * 在synchronized的代码块中执行，和wait的对象和synchronized的对象锁是同一个
 */
public class Main {
    public static int SIZE = 10;

    public static void main(String[] args) {
        Queue queue = new ArrayDeque();
        new Thread(new Producer(queue)).start();
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

class Producer implements Runnable {
    Queue queue;

    public Producer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            product(queue);
        }
    }

    public void product(Queue goods) {
        synchronized (goods) {
            if (goods.size() == SIZE) {
                try {
                    System.out.println(Thread.currentThread().getName() + ",good full," + goods.size() + ",wait consume");
                    goods.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {//必须要为else，否则在多生产者的情况下，会造成多个wait product连续执行而大于比较值，需要重新比较
                if (CollectionUtils.isEmpty(queue)) {
                    goods.notifyAll();
                }
                goods.add(1);
                System.out.println(Thread.currentThread().getName() + ",good ++," + goods.size());
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    Queue queue;

    public Consumer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true)
            consume(queue);
    }

    public void consume(Queue goods) {
        synchronized (goods) {
            if (CollectionUtils.isEmpty(queue)) {
                try {
                    System.out.println(Thread.currentThread().getName() + ",no good," + goods.size() + ",wait product");
                    goods.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {//必须要为else，否则在多消费者的情况下，会造成多个wait consume连续执行而低于比较值，需要重新比较
                if (goods.size() == SIZE) {
                    goods.notifyAll();
                }
                goods.poll();
                System.out.println(Thread.currentThread().getName() + ",good --," + goods.size());
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
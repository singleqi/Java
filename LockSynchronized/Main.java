package cn.singleqi;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock和Synchronized比较
 * 1）Lock是一个接口，而synchronized是Java中的关键字，synchronized是内置的语言实现；
 * 2）synchronized在发生异常时，会自动释放线程占有的锁，因此不会导致死锁现象发生；而Lock在发生异常时，如果没有主动通过unLock()去释放锁，则很可能造成死锁现象，因此使用Lock时需要在finally块中释放锁；
 * 3）Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断；
 * 4）通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。
 * 5）Lock可以提高多个线程进行读操作的效率。
 * 6）在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。所以说，在具体使用时要根据适当情况选择。
 * 7）lock和synchronized都是非公平锁，但lock可以设置喂公平锁
 * Lock使用文档：https://www.cnblogs.com/dolphin0520/p/3923167.html
 * Synchronized使用：https://www.jianshu.com/p/d53bf830fa09 
 */
public class Main {

    private static ArrayList<Integer> list = new ArrayList<Integer>();
    private static Lock lock = new ReentrantLock();    //注意这个地方
    public static void main(String[] args)  {

        new Thread(() ->
                insert(Thread.currentThread())
        ).start();

        new Thread(() ->
                insert(Thread.currentThread())
        ).start();
    }

    public static void insert(Thread thread) {
        try {
            if(lock.tryLock(1, TimeUnit.SECONDS)) {
                System.out.println(thread.getName()+"得到了锁");
                try {
                    for(int i=0;i<5;i++) {
                        list.add(i);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }finally {
                    System.out.println(thread.getName()+"释放了锁");
                    lock.unlock();
                }
            } else {
                System.out.println(thread.getName()+"获取锁失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class SyncDemo{
    //修饰实例方法，锁对象是类的实例new Sync()
    synchronized void method1(){}
    //修饰静态方法，锁的对象是该类对象SyncDemo.class
    static synchronized void method2(){}
    void method3(){
        //修饰同步代码块，锁对象是该类实例new Sync()
        synchronized (this){}
        //修饰同步代码块，锁对象是该类对象，String.class
        synchronized (String.class){}
        String lock="";
        //修饰同步代码块，锁对象是该实例，lock
        synchronized (lock){}
    }
}

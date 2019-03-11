package cn.singleqi;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * ThreadLocal的使用、弱引用、内存泄漏
 * https://www.jianshu.com/p/98b68c97df9b
 * https://www.jianshu.com/p/a1cd61fa22da
 */
public class Main {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        //在main线程中设置threadLocal值
        threadLocal.set("threadLocal main thread set");
        System.out.println(Thread.currentThread().getName() + "\t" + threadLocal.get());
        new Thread(() ->
        {
            //在Thread-0中设置threadLocal值
            threadLocal.set("threadLocal other thread set");
            System.out.println(Thread.currentThread().getName() + "\t" + threadLocal.get());
        }
        ).start();
        //等待Thread-0中的值设置threadLocal，并输出
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //再次获取main线程中的threadLocal值
        System.out.println(Thread.currentThread().getName() + "\t" + threadLocal.get());
        System.out.println();


        //ThreadLocalMap的key弱引用ThreadLocal在gc时被回收，value没有被回收，导致内存泄漏
        //但如果是强引用的话，线程不死，ThreadLocalMap的key-value一直在涨
        ThreadLocal tl = new MyThreadLocal();
        tl.set(new My50MB());
        tl = null;
        System.out.println("Full GC");
        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("After Full GC");
    }

    public static class MyThreadLocal extends ThreadLocal {
        private byte[] a = new byte[1024 * 1024 * 1];

        @Override
        public void finalize() {
            System.out.println("My threadlocal 1 MB finalized.");
        }
    }

    public static class My50MB {
        private byte[] a = new byte[1024 * 1024 * 50];

        @Override
        public void finalize() {
            System.out.println("My 50 MB finalized.");
        }
    }
}

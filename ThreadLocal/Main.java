package cn.singleqi;

/**
 * ThreadLocal的使用、弱引用、内存泄漏
 * https://www.jianshu.com/p/98b68c97df9b
 * https://www.jianshu.com/p/a1cd61fa22da
 */
public class Main {
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        //在main线程中设置threadLocal值
        threadLocal.set(111);
        Integer i = threadLocal.get();
        System.out.println(Thread.currentThread().getName() + "\t" + i);
        new Thread(() ->
        {
            //在Thread-0中设置threadLocal值
            threadLocal.set(222);
            Integer t = threadLocal.get();
            System.out.println(Thread.currentThread().getName() + "\t" + t);
        }
        ).start();
        //等待Thread-0中的值设置threadLocal，并输出
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //再次获取main线程中的threadLocal值
        i = threadLocal.get();
        System.out.println(Thread.currentThread().getName() + "\t" + i);
        new Thread(() ->
        {
            //清除threadLocal的在虚拟机中的强引用
            threadLocal = null;
            //强制GC
            System.gc();
            //此时ThreadLocalMap中的Entry的key的弱应用已被清除
            Integer t = threadLocal.get();
            System.out.println(Thread.currentThread().getName() + "\t" + t);
        }
        ).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Main线程中的ThreadLocalMap中的Entry的key的弱应用也已被清除
        i = threadLocal.get();
        System.out.println(Thread.currentThread().getName() + "\t" + i);
    }
}

package cn.singleqi;

import java.util.concurrent.*;

/**
 * 线程池的几种创建方式
 * 单个、缓存、固定、工作窃取、定时
 * 三种队列：https://www.cnblogs.com/feiyun126/p/7686302.html
 */
public class Main {

    public static void main(String[] args) {
        Task task=new Task();
        /**
         * 1.特点在于工作线程数目被限制为 1
         * 单个线程的线程池，有一个无界任务队列，最多一个任务在活动状态，不允许使用者修改线程实例
         */
        ExecutorService singleThreadExecutor=Executors.newSingleThreadExecutor();
        singleThreadExecutor.submit(task);

        /**
         * 2.特点在用于处理大量短时间工作的线程池
         * 试图缓存线程，超过60s闲置的线程会被终止并移除，长时间闲置该线程池不会又什么资源消耗
         * SynchronousQueue
         */
        ExecutorService cachedThreadPool=Executors.newCachedThreadPool();
        cachedThreadPool.submit(task);

        /**
         * 3.指定运行线程数目 n
         * 任务放入到一个无界队列中，同时有n个线程处于活动状态,有线程从池中退出会新建线程
         * LinkedBlockingQueue
         */
        ExecutorService fixedThreadPool=Executors.newFixedThreadPool(2);
        fixedThreadPool.submit(task);

        /**
         * 4.和newSingleThreadExecutor 1类似，但可以进行定时或周期性的工作调度
         */
        ExecutorService singleThreadScheduledExecutor= Executors.newSingleThreadScheduledExecutor();
        ((ScheduledExecutorService) singleThreadScheduledExecutor).schedule(new Task(),1L, TimeUnit.SECONDS);

        /**
         * 6.和fixedThreadPool 3类似，但可以进行定时或周期性的工作调度
         */
        ExecutorService scheduledThreadPool=Executors.newScheduledThreadPool(2);
        ((ScheduledExecutorService) scheduledThreadPool).schedule(task,1L,TimeUnit.SECONDS);

        /**
         * 6 工作窃取，设置n个线程和队列，每个线程从各自的队列上取得任务，完成该队列的任务则选择从其他队列的末尾取task
         * LinkedBlockingQueue
         */
        ExecutorService workStealingPool=Executors.newWorkStealingPool(2);
        workStealingPool.submit(task);

        /**
         * 7.最基本的线程池创建方式
         * corePoolSize 核心池的大小.默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
         * maximumPoolSize：线程池最大线程数，这个参数也是一个非常重要的参数，它表示在线程池中最多能创建多少个线程；
         * keepAliveTime：表示线程没有任务执行时最多保持多久时间会终止。默认情况下，只有当线程池中的线程数大于corePoolSize时，keepAliveTime才会起作用（allowCoreThreadTimeOut(boolean)除外）
         * unit：参数keepAliveTime的时间单位，有7种取值，在TimeUnit类中有7种静态属性
         * workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
         * ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue(生产1后必须要被消费才能继续生产)
         */
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.submit(task);
    }
    static class Task implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    }
}
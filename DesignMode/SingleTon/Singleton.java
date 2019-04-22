package cn.singleqi;

/**
 * 懒汉双重检验模式，保证多线程唯一
 * 缺点复杂,乱序执行问题
 * 1.在堆内存开辟内存空间。
 * 2.在堆内存中实例化SingleTon里面的各个参数。
 * 3.把对象指向堆内存空间。
 * 由于jvm存在乱序执行功能，所以可能在2还没执行时就先执行了3，如果此时再被切换到线程B上，由于执行了3，INSTANCE 已经非空了，会被直接拿出来用，这样的话，就会出现异常。这个就是著名的DCL失效问题。
 * 可以用volatile解决
 */
public class Singleton {
    private Singleton() {
    }

    private volatile static Singleton singleton = null;

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
///**
// * 恶汉模式
// * 缺点加载时初始化，不使用会导致内存浪费
// */
//public class Singleton {
//    private Singleton() {
//    }
//
//    private static Singleton singleton = new Singleton();
//
//    public static Singleton getInstance() {
//        return singleton;
//    }
//}
///**
// * 静态内部类方式
// * 外部类加载时并不需要立即加载内部类，内部类不被加载则不去初始化INSTANCE，故而不占内存
// * 缺点是传参的问题，由于是静态内部类的形式去创建单例的，故外部无法传递参数进去，例如Context这种参数，所以，我们创建单例时，可以在静态内部类与DCL模式里自己斟酌。
// */
//public class Singleton {
//    private Singleton() {
//    }
//
//    public static Singleton getInstance() {
//        return SingletonHolder.INSTANCE;
//    }
//
//    private static class SingletonHolder{
//        private static Singleton INSTANCE = new Singleton();
//    }
//}
//
///**
// * 枚举单例
// */
//public enum Singleton{
//    INSTANCE;
//    private Singleton(){
//
//    }
//}
package cn.singleqi;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Java Lambda和Stream
 * https://www.cnblogs.com/aoeiuv/p/5911692.html
 */
public class Main {
    public static void main(String[] args) {
        //Runnable的Lambda实现
        new Thread(() -> System.out.println(Thread.currentThread() + "--- Hello,Lambda"));
        List<Integer> numList = Arrays.asList(1, 3, null, 2, null, 5, 4);
        numList.forEach(System.out::print);
        System.out.println();

        //过滤并计数
        long count = numList.stream().filter(Objects::nonNull).count();
        System.out.println("过滤null并计数：" + count);

        //去重并求和
        int sum = numList.stream().distinct().mapToInt(num -> num == null ? 0 : num).sum();
        System.out.println("去重并求和：" + sum);

        //进行过滤掉null，再去重，再每个元素乘以2，再每个元素被消费的时候打印自身，在跳过前两个元素，最后去前四个元素进行加和运算
        System.out.println("  sum is:" + numList.stream().filter(Objects::nonNull).distinct().mapToInt(num -> num * 2).peek(System.out::print).skip(2).limit(4).sum());

        /**
         * – reduce方法: 将后面a,b的计算结果作为下一个a的值，Stream中会再传一个b
         * – count方法：获取Stream中元素的个数
         * – allMatch：是不是Stream中的所有元素都满足给定的匹配条件
         * – anyMatch：Stream中是否存在任何一个元素满足匹配条件
         * – findFirst: 返回Stream中的第一个元素，如果Stream为空，返回空Optional
         * – noneMatch：是不是Stream中的所有元素都不满足给定的匹配条件
         * – max和min：使用给定的比较器（Operator），返回Stream中的最大|最小值
         */
        System.out.println("ints sum is:" + numList.stream().filter(Objects::nonNull).reduce((a, b) -> a + b).get());
        System.out.println(numList.stream().filter(Objects::nonNull).allMatch(item -> item < 100));
        numList.stream().filter(Objects::nonNull).max(Integer::compareTo).ifPresent(System.out::println);
    }
}

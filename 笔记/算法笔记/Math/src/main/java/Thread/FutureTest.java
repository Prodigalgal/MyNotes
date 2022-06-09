package Thread;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class FutureTest {
    public static void main(String[] args) {
        final Object objectLockA = new Object();
        final Object objectLockB = new Object();

        new Thread(() -> {
            synchronized (objectLockA) {
                System.out.println(Thread.currentThread().getName() + "\t" + "自己持有A，希望获得B");
                //暂停几秒钟线程
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (objectLockB) {
                    System.out.println(Thread.currentThread().getName() + "\t" + "A-------已经获得B");
                }
            }
        }, "A").start();

        new Thread(() -> {
            synchronized (objectLockB) {
                System.out.println(Thread.currentThread().getName() + "\t" + "自己持有B，希望获得A");
                //暂停几秒钟线程
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (objectLockA) {
                    System.out.println(Thread.currentThread().getName() + "\t" + "B-------已经获得A");
                }
            }
        }, "B").start();

    }


}

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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class FutureTest {
    public static void main(String[] args) {
        // 正常使用+不需要锁块
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " " + "1111111111111");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " " + "2222222222222------end被唤醒");
        }, "t1");
        t1.start();

        // 暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LockSupport.unpark(t1);
        System.out.println(Thread.currentThread().getName() + "   -----LockSupport.unparrk() invoked over");
    }


}

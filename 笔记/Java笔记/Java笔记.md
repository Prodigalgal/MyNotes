# JUC

## 1、基本概念

JUC 就是 java.util .concurrent 工具包的简称。这是一个处理线程的工具包，JDK  1.5 开始出现的。

### 1.1、进程与线程

- **进程（Process）**：是计算机中的程序关于某数据集合上的一次运行活动，是**系统进行资源分配和调度的基本单位**，是操作系统结构的基础。 在当代面向线程设计的计算机结构中，**进程是线程的容器**。程序是指令、数据及其组织形式的描述。进程是程序的实体，是系统进行资源分配和调度的基本单位，是操作系统结构的基础。
- **线程（thread）**：是**操作系统能够进行运算调度的最小单位**。它被包含在进程之中，是进程中的实际运作单位。一条线程指的是进程中一个单一顺序的控制流， 一个进程中可以并发多个线程，每条线程并行执行不同的任务。

进程就是指在系统中正在运行的一个应用程序，程序一旦运行就是进程。

线程就是系统分配处理器时间资源的基本单元，或者说进程之内独立执行的一个单元执行流，也即应用程序的操作，线程——程序执行的最小单位。

### 1.2、线程状态

线程状态枚举类**Thread.State**

~~~java
public enum State {

NEW,(新建)

RUNNABLE,（准备就绪）

BLOCKED,（阻塞）

WAITING,（不见不散）

TIMED_WAITING,（过时不候）

TERMINATED;(终结)
}
~~~

### 1.3、wait/sleep

- sleep 是 Thread 的静态方法，wait 是 Object 的方法，任何对象实例都能调用。
- sleep 不会释放锁，它也不需要占用锁。wait 会释放锁，但调用它的前提是当前线程占有锁(即代码要在 synchronized 中)。
- 它们都可以被 interrupted 方法中断。

### 1.4、并发与并行

#### 1.4.1、串行模式

串行表示所有任务都一一按先后顺序进行。串行意味着必须先装完一车柴才能运送这车柴，只有运送到了，才能卸下这车柴，并且只有完成了这整个三个步骤，才能进行下一个步骤。

串行是一次只能取得一个任务，并执行这个任务。

#### 1.4.2、并行模式

并行意味着可以同时取得多个任务，并同时去执行所取得的这些任务。并行模式相当于将长长的一条队列，划分成了多条短队列，所以并行缩短了任务队列的长度。

并行的效率从代码层次上强依赖于多进程/多线程代码，从硬件角度上则依赖于多核 CPU。

#### 1.4.3、并发

**并发(concurrent)**：指的是多个程序可以同时运行的现象，更细化的是多进程可以同时运行或者多指令可以同时运行。但这不是重点，并发的重点在于它是一种现象，并发描述的是多进程同时运行的现象。

但实际上，对于单核心 CPU 来说，同一时刻只能运行一个线程。所以，这里的"同时运行"表示的不是真的同一时刻有多个线程运行的现象，这是并行的概念，而是提供一种功能让用户看来多个程序同时运行起来了，但实际上这些程序中的进程不是一直霸占 CPU 的，而是执行一会停一会。 

要解决大并发问题，通常是将大任务分解成多个小任务, 由于操作系统对进程的调度是随机的，所以切分成多个小任务后，可能会从任一小任务处执行。

这可能会出现一些现象：

- 可能出现一个小任务执行了多次，还没开始下个任务的情况。这时一般会采用队列或类似的数据结构来存放各个小任务的成果。
- 可能出现还没准备好第一步就执行第二步的可能。这时，一般采用多路复用或异步的方式，比如只有准备好产生了事件通知才执行某个任务。
- 可以多进程/多线程的方式并行执行这些小任务。也可以单进程/单线程执行这些小任务，这时很可能要配合多路复用才能达到较高的效率。

并发就是多个线程对应一个资源点。

并行就是多个任务同时执行，最后汇总。

### 1.5、管程

**管程(monitor)**：是保证了同一时刻只有一个进程在管程内活动，即管程内定义的操作在同一时刻只被一个进程调用(由编译器实现)。

但是这样并不能保证进程以设计的顺序执行 JVM 中同步是基于进入和退出管程(monitor)对象实现的，每个对象都会有一个管程 (monitor)对象，管程(monitor)会随着 java 对象一同创建和销毁。

执行线程首先要持有管程对象，然后才能执行方法，当方法完成之后会释放管程，方法在执行时候会持有管程，其他线程无法再获取同一个管程。

### 1.6、用户/守护线程 

**用户线程**：平时用到的普通线程，自定义线程，直接new的线程。

**守护线程**：运行在后台，是一种特殊的线程，比如垃圾回收，使用setDeamon设置的守护线程。

当主线程结束后，用户线程还在运行，JVM 存活

如果没有用户线程，都是守护线程，JVM 结束

## 2、Lock 接口

### 2.1、Synchronized

synchronized 是 Java 中的关键字，是一种同步锁。

它修饰的对象有以下几种：

- 修饰一个**代码块**，被修饰的代码块称为同步语句块，其作用的范围是大括号 { } 括起来的代码，作用的对象是调用这个代码块的对象。
- 修饰一个**方法**，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象。

- 修改一个**静态的方法**，其作用的范围是整个静态方法，作用的对象是这个类的所有对象。
- 修改一个**类**，其作用的范围是 synchronized 后面括号括起来的部分，作用的对象是这个类的所有对象。

虽然可以使用 synchronized 来修饰方法，但 synchronized 并不属于方法定义的一部分，因此 **synchronized 关键字不能被继承**。

如果在父类中的某个方法使用了 synchronized 关键字，而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，而必须显式地在子类的这个方法中加上 synchronized 关键字才可以。

当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，但子类调用了父类的同步方法，因此， 子类的方法也就相当于同步了。

### 2.2、基本概念

Lock 锁实现提供了比使用同步方法和语句可以获得的更广泛的锁操作。

其允许更灵活的结构，可能具有非常不同的属性，并且可能支持多个关联的条件对象。

Lock 提供了比 synchronized 更多的功能。

### 2.3、Lock/Synchronized

- synchronized 是 Java 语言的关键字，因此是内置特性。Lock 不是 Java 语言内置的，是一个类，通过这个类可以实现同步访问。
- 采用 synchronized 不需要用户去手动释放锁，当 synchronized 方法或者 synchronized 代码块执行完之后， 系统会自动让线程释放对锁的占用，或者在发生异常时，也会自动释放线程占用的锁。采用 Lock 必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象，即使发生异常也要手动解锁。
- Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断
- 通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。
- Lock可以提高多个线程进行读操作的效率。在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。

### 2.4、Lock 接口

~~~java
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}
~~~

#### 2.4.1、lock()

lock()方法是平常使用得最多的一个方法，就是用来获取锁。

如果锁已被其他线程获取，则进行等待。 

采用 Lock，必须主动去释放锁，而且在发生异常时，不会自动释放锁。

因此一般来说，使用 Lock 必须在try{}catch{}块中进行，并且将释放锁的操作放在 finally 块中进行，以保证锁一定被被释放，防止死锁的发生。

通常使用 Lock 来进行同步的话，是以下面这种形式去使用的：

~~~java
Lock lock = ...;
lock.lock();
try{
    //处理任务
}catch(Exception ex){
}finally{
    lock.unlock(); //释放锁
}
~~~

#### 2.4.2、newCondition()

关键字 synchronized 与 wait()/notify() 这两个方法一起使用可以实现等待/通知模式。

Lock 锁的 newContition() 方法返回 Condition 对象，Condition 类也可以实现等待/通知模式。 

用 notify()通知时，JVM 会随机唤醒某个等待的线程， 使用 Condition 类可以进行选择性通知。

Condition 比较常用的两个方法：

- await()会使当前线程等待，同时会释放锁，当其他线程调用 signal()时，线程会重新获得锁并继续执行。
- signal()用于唤醒一个等待的线程。

**注意**：在调用Condition的await()/signal()方法前，也需要线程持有相关的Lock锁，调用await()后线程会释放这个锁，在singal()调用后会从当前Condition对象的等待队列中，唤醒 一个线程，唤醒的线程尝试获得锁， 一旦获得锁成功就继续执行。

#### 2.4.3、ReentrantLock

ReentrantLock，意思是“可重入锁”。

ReentrantLock是唯一实现了Lock接口的类，并且ReentrantLock提供了更

多的方法。下面通过一些实例看具体看一下如何使用。

```java
public class Test {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    
    public static void main(String[] args) {
        final Test test = new Test();
        
        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();
        
        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();
        
    } 
    
    public void insert(Thread thread) {
        
        Lock lock = new ReentrantLock(); //注意这个地方
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i=0;i<5;i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
        }finally {
            System.out.println(thread.getName()+"释放了锁");
            lock.unlock();
        }
    }
    
}
```


#### 2.4.4、ReadWriteLock

ReadWriteLock也是一个接口，在它里面只定义了两个方法：

~~~java
public interface ReadWriteLock {

    Lock readLock();

    Lock writeLock();
}
~~~

一个用来获取读锁，一个用来获取写锁。也就是说将文件的读写操作分开，分成 2 个锁来分配给线程，从而使得多个线程可以同时进行读操作。

下面的 **ReentrantReadWriteLock** 实现了ReadWriteLock接口。ReentrantReadWriteLock里面提供了很多丰富的方法，不过最主要的有两个方法：readLock()和writeLock()用来获取读锁和写锁。

下面通过几个例子来看一下ReentrantReadWriteLock具体用法。

- 假如有多个线程要同时进行读操作的话，先看一下 synchronized 达到的效果：

~~~java
public class Test {

    public static void main(String[] args) {
        final Test test = new Test();

        new Thread(){
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

    } 

    public synchronized void get(Thread thread) {
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start <= 1) {
            System.out.println(thread.getName()+"正在进行读操作");
        }
        System.out.println(thread.getName()+"读操作完毕");
    }
}

~~~

- 改成用读写锁

~~~java
public class Test {
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final Test test = new Test();

        new Thread(){
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.get(Thread.currentThread());
            };
        }.start();

    } 

    public void get(Thread thread) {
        rwl.readLock().lock();
        try {
            long start = System.currentTimeMillis();

            while(System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName()+"正在进行读操作");
            }
            System.out.println(thread.getName()+"读操作完毕");
        } finally {
            rwl.readLock().unlock();
        }
    }
}
~~~

说明thread1和thread2在同时进行读操作。这样就大大提升了读操作的效率。

**注意**：

- 如果有一个线程已经占用了读锁，则此时其他线程如果要申请写锁，则申请写锁的线程会一直等待释放读锁。

- 如果有一个线程已经占用了写锁，则此时其他线程如果申请写锁或者读锁，则申请的线程会一直等待释放写锁。



## 3、线程间通信

### 3.1、基本方法

线程间通信的模型有两种：**共享内存**和**消息传递**。

以下方式都是基于这两种模型来实现的。

### 3.2、案例

两个线程，一个线程对当前数值加 1，另一个线程对当前数值减 1，要求用线程间通信。

#### synchronized方案

~~~java
public class TestVolatile {
    
    public static void main(String[] args){
        DemoClass demos = new DemoClass();
        
        new Thread(() ->{
            for (int i = 0; i < 5; i++) {
                demo.increment();
            }
        }, "线程 A").start();
        
        new Thread(() ->{
            for (int i = 0; i < 5; i++) {
                demo.decrement();
            }
        }, "线程 B").start();
    }

}

class DemoClass{
    //加减对象
    private int number = 0;

    public synchronized void increment() {
        try {
            while (number != 0){
                this.wait();
            }
            number++;
            System.out.println(Thread.currentThread().getName() + "加一成功----------,值为:" + number);
            notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void decrement(){
        try {
            while (number == 0){
                this.wait();
            }
            number--;
            System.out.println(Thread.currentThread().getName() + "减一成功----------,值为:" + number);
            notifyAll();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
~~~

#### Lock 方案



## 4、集合的线程安全

## 5、多线程锁

## 6、Callable 接口

## 7、三大辅助类:

CountDownLatch CyclicBarrier Semaphore

## 8、读写锁:

ReentrantReadWriteLock

## 9、阻塞队列

## 10、ThreadPool 线程池

## 11、Fork/Join 框架

## 12、CompletableFuture









# 问题

## Integer.valueOf()和Integer.parseInt()的区别

- Integer.parseInt(String s)将会返回int常量。
- Integer.valueOf(String s)将会返回Integer类型，如果存在缓存将会返回缓存中已有的对象。

Integer会缓存 -128 ~ 127 范围的整型数字

## Integer i1 = 100 与 Integer i2 = 200 有何不一样

首先要知道 Integer i1 = 100 在做这样的操作时，实际就是基本数据类型与引用类型之间的拆箱和装箱操作，Integer i1 = 100是一个装箱操作，本质就是Integer i1 = Integer.valueOf(100)，源码如下：

~~~java
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}    
~~~

在valueOf方法，对赋的值进行一个判断操作，如果值在-128~127之间，就会在内部类IntegerCache的cache[]数组中获取一个Integer对象，如果不是就new一个新的Integer对象。

从IntegerCache中的一段源码中可以发现cache[]中循环放入了值在-128~127之间的Integer对象，根据内部类加载机制，当类第一次调用时会初始化这个数组，并且在JVM中只初始化一次。

~~~java
cache = new Integer[(high - low) + 1];
int j = low;
for(int k = 0; k < cache.length; k++)
    cache[k] = new Integer(j++);
~~~

因为 == 比较的是内存地址，i1 和 i2 都赋100时，在这个范围内都引用了从cache取出的同一个对象，对象内存地址一样，所以是相等的，在超出这个范围之后，每次创建会new一个新的Integer对象，引用的是不同的对象,所以不相等。

再看看Integer对equals方法进行的重写，从比较两个对象的内存地址变成了比较两个Integer对象的的值，这与String类相似，同时重写的还有hashCode()方法，hashcode返回了对象的值。

~~~java
public boolean equals(Object obj) {
        if (obj instanceof Integer) {
            return value == ((Integer)obj).intValue();
        }
        return false;
}
~~~

设计IntegerCache类来缓存-128~127是为了节省内存消耗，提高程序性能，Integer是一个经常使用到的类，并且一般创建的对象值范围都在-128~127之间，并且创建这样相似值的对象并没有太大意义，所以使用IntegerCache类，与此类似的ByteCache、ShortCache等。






















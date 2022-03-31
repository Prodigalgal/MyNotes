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

## 2、Synchronized 关键字

synchronized 是 Java 中的关键字，是一种同步锁。

synchronized 实现同步的基础：Java 中的每一个对象都可以作为锁。

它修饰的对象有以下几种：

- 修饰一个**代码块**，被修饰的代码块称为同步语句块，其作用的范围是大括号 { } 括起来的代码，作用的对象是调用这个代码块的对象。
- 修饰一个**方法**，被修饰的方法称为同步方法，其作用的范围是整个方法，作用的对象是调用这个方法的对象。

- 修改一个**静态的方法**，其作用的范围是整个静态方法，作用的对象是这个类的所有对象。
- 修改一个**类**，其作用的范围是 synchronized 后面括号括起来的部分，作用的对象是这个类的所有对象。

>对于普通同步方法，锁是当前实例对象this，被锁定后，其它的线程都不能进入到当前对象的其它的 synchronized 方法。
>
>对于静态同步方法，锁是当前类的 Class 对象。 
>
>对于同步方法块，锁是 Synchonized 括号里配置的对象
>
>静态同步方法与非静态同步方法之间是不会有竞态条件的。

**注意**：

虽然可以使用 synchronized 来修饰方法，但 synchronized 并不属于方法定义的一部分，因此 **synchronized 关键字不能被继承**。

如果在父类中的某个方法使用了 synchronized 关键字，而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，而必须显式地在子类的这个方法中加上 synchronized 关键字才可以。

当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，但子类调用了父类的同步方法，因此， 子类的方法也就相当于同步了。

## 3、Lock 接口

### 3.1、基本概念

Lock 锁实现提供了比使用同步方法和语句可以获得的更广泛的锁操作。

其允许更灵活的结构，可能具有非常不同的属性，并且可能支持多个关联的条件对象。

Lock 提供了比 synchronized 更多的功能。

### 3.2、Lock/Synchronized

- synchronized 是 Java 语言的关键字，因此是内置特性。Lock 不是 Java 语言内置的，是一个类，通过这个类可以实现同步访问。
- 采用 synchronized 不需要用户去手动释放锁，当 synchronized 方法或者 synchronized 代码块执行完之后， 系统会自动让线程释放对锁的占用，或者在发生异常时，也会自动释放线程占用的锁。采用 Lock 必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象，即使发生异常也要手动解锁。
- Lock可以让等待锁的线程响应中断，而synchronized却不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断
- 通过Lock可以知道有没有成功获取锁，而synchronized却无法办到。
- Lock可以提高多个线程进行读操作的效率。在性能上来说，如果竞争资源不激烈，两者的性能是差不多的，而当竞争资源非常激烈时（即有大量线程同时竞争），此时Lock的性能要远远优于synchronized。

### 3.3、Lock 接口

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

#### 3.3.1、lock()

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

#### 3.3.2、newCondition()

关键字 synchronized 与 wait()/notify() 这两个方法一起使用可以实现等待/通知模式。

Lock 锁的 newContition() 方法返回 Condition 对象，Condition 类也可以实现等待/通知模式。 

用 notify()通知时，JVM 会随机唤醒某个等待的线程， 使用 Condition 类可以进行选择性通知。

Condition 比较常用的两个方法：

- await()会使当前线程等待，同时会释放锁，当其他线程调用 signal()时，线程会重新获得锁并继续执行。
- signal()用于唤醒一个等待的线程。

**注意**：在调用Condition的await()/signal()方法前，也需要线程持有相关的Lock锁，调用await()后线程会释放这个锁，在singal()调用后会从当前Condition对象的等待队列中，唤醒 一个线程，唤醒的线程尝试获得锁， 一旦获得锁成功就继续执行。

#### 3.3.3、ReentrantLock

ReentrantLock，意思是“可重入锁”。

ReentrantLock 是唯一实现了Lock接口的类，并且ReentrantLock提供了更多的方法。下面通过一些实例看具体看一下如何使用。

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


#### 3.3.4、ReadWriteLock

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



## 4、线程间通信

### 4.1、基本方法

线程间通信的模型有两种：**共享内存**和**消息传递**。

以下方式都是基于这两种模型来实现的。

### 4.2、案例

#### 案例一

两个线程，一个线程对当前数值加 1，另一个线程对当前数值减 1，要求用线程间通信。

##### synchronized方案

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

##### Lock 方案

~~~java
class DemoClass{
    //加减对象
    private int number = 0;
    //声明锁
    private Lock lock = new ReentrantLock();
    //声明钥匙
    private Condition condition = lock.newCondition();

    public void increment() {
        try {
            lock.lock();
            while (number != 0){
                condition.await();
            }
            number++;
            System.out.println(Thread.currentThread().getName() + "加一成功,值为:" + number);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void decrement(){
        try {
            lock.lock();
            while (number == 0){
                condition.await();
            }
            number--;
            System.out.println(Thread.currentThread().getName() + "减一成功,值为:" + number);
            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
~~~

#### 案例二

 A 线程打印 5 次 A，B 线程打印 10 次 B，C 线程打印 15 次 C，按照此顺序循环 10 轮

~~~java
class DemoClass{
    //通信对象:0--打印 A 1---打印 B 2----打印 C
    private int number = 0;
    //声明锁
    private Lock lock = new ReentrantLock();
    //声明钥匙 A
    private Condition conditionA = lock.newCondition();
    //声明钥匙 B
    private Condition conditionB = lock.newCondition();
    //声明钥匙 C
    private Condition conditionC = lock.newCondition();

    public void printA(int j){
        try {
            lock.lock();
            while (number != 0){
                conditionA.await();
            }
            System.out.println(Thread.currentThread().getName() + "输出 A,第" + j + "轮开始");
            //输出 5 次 A
            for (int i = 0; i < 5; i++) {
                System.out.println("A");
            }
            //开始打印 B
            number = 1;
            //唤醒 B
            conditionB.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    
    public void printB(int j){
        try {
            lock.lock();
            while (number != 1){
                conditionB.await();
            }
            System.out.println(Thread.currentThread().getName() + "输出 B,第" + j + "轮开始");
            //输出 10 次 B
            for (int i = 0; i < 10; i++) {
                System.out.println("B");
            }
            //开始打印 C
            number = 2;
            //唤醒 C
            conditionC.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void printC(int j){
        try {
            lock.lock();
            while (number != 2){
                conditionC.await();
            }
            System.out.println(Thread.currentThread().getName() + "输出 C,第" + j + "轮开始");
            //输出 15 次 C
            for (int i = 0; i < 15; i++) {
                System.out.println("C");
            }
            System.out.println("-----------------------------------------");
            //开始打印 A
            number = 0;
            //唤醒 A
            conditionA.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

~~~

~~~java
public static void main(String[] args){
    DemoClass demoClass = new DemoClass();
    new Thread(() ->{
        for (int i = 1; i <= 10; i++) {
            demoClass.printA(i);
        }
    }, "A 线程").start();

    new Thread(() ->{
        for (int i = 1; i <= 10; i++) {
            demoClass.printB(i);
        }
    }, "B 线程").start();

    new Thread(() ->{
        for (int i = 1; i <= 10; i++) {
            demoClass.printC(i);
        }
    }, "C 线程").start();
}
~~~



## 5、集合的线程安全

### 1、基本原因

多个线程对集合进行操作，会报错java.util.ConcurrentModificationException

查看 ArrayList 的 add 方法源码

~~~java
public boolean add(E e) {
    ensureCapacityInternal(size + 1); // Increments modCount!!
    elementData[size++] = e;
    return true;
}
~~~

### 2、Vector

- Vector 是矢量队列，它是 JDK1.0 版本添加的类，继承于 AbstractList，实现 了 List，RandomAccess，Cloneable 这些接口。 
- Vector 继承了 AbstractList， 实现了 List，所以，它是一个队列，支持相关的添加、删除、修改、遍历等功能。
- Vector 实现了 RandmoAccess 接口，即提供了随机访问功能。 RandmoAccess 是 java 中用来被 List 实现，为 List 提供快速访问功能的。在 Vector 中，我们即可以通过元素的序号快速获取元素对象，这就是快速随机访问。 
- Vector 实现了 Cloneable 接口，即实现 clone()函数。它能被克隆。
- 和 ArrayList 不同，Vector 中的操作是线程安全的。

查看 Vector 的 add 方法

add 方法被 synchronized 同步修饰，线程安全，因此没有并发异常

~~~java
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}
~~~

### 3、Collections

Collections 提供了方法 synchronizedList 保证 list 是同步线程安全的 NotSafeDemo 代码修改

查看源码

~~~java
public static <T> List<T> synchronizedList(List<T> list) {
    return (list instanceof RandomAccess ?
            new SynchronizedRandomAccessList<>(list) :
            new SynchronizedList<>(list));
}
~~~

### 4、CopyOnWriteArrayList

#### 1、概述

它相当于线程安全的 ArrayList，是个可变数组，但是和 ArrayList 不同的是

独占锁效率低：采用读写分离思想解决

写线程获取到锁，其他写线程阻塞

它具有以下特性：

- List 大小通常保持很小，只读操作远多于可变操作，需要在遍历期间防止线程间的冲突。
- 它是线程安全的。
- 因为通常需要复制整个基础数组，所以可变操作（add()、set() 和 remove()  等等）的开销很大。
- 迭代器支持 hasNext(), next()等不可变操作，但不支持可变 remove()等操作。
- 使用迭代器进行遍历的速度很快，并且不会与其他线程发生冲突。在构造迭代器时，迭代器依赖于不变的数组快照。

#### 2、复制思想

当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行 Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。 

这时候会抛出来一个新的问题，也就是数据不一致的问题。如果写线程还没来得及写进内存，其他的线程就会读到了脏数据。

#### 3、原理说明

**动态数组机制** 

它内部有个volatile 数组(array)来保持数据。在添加/修改/删除数据时，都会新建一个数组，并将更新后的数据拷贝到新建的数组中，最后再将该数组赋值给volatile 数组吗，这就是它叫做 CopyOnWriteArrayList 的原因。

由于它在添加/修改/删除数据时，都会新建数组，所以涉及到修改数据的操作，CopyOnWriteArrayList 效率很低，但是单单只是进行遍历查找的话，效率比较高。

**线程安全机制** 

通过 volatile 和互斥锁来实现的。

通过volatile 数组来保存数据的，一个线程读取 volatile 数组时，总能看到其它线程对该 volatile 变量最后的写入，通过 volatile 提供了读取到的数据总是最新的这个机制的保证。

通过互斥锁来保护数据。在添加/修改/删除数据时，会先获取互斥锁，再修改完毕之后，先将数据更新到volatile 数组中，然后再释放互斥锁，就达到了保护数据的目的。

### 总结

集合类型中存在线程安全与线程不安全的两种

常见例如：

ArrayList ----- Vector

HashMap -----HashTable 

但是以上都是通过 synchronized 关键字实现，效率较低

Collections 构建的线程安全集合

java.util.concurrent 并发包下

CopyOnWriteArrayList CopyOnWriteArraySet 类型，通过动态数组与线程安全个方面保证线程安全



## 6、Callable & Future 接口

目前有两种创建线程的方法一种是通过创建 Thread 类，另一种是通过使用 Runnable 创建线程。

但是，Runnable 缺少的一项功能是，当线程终止时（即 run()完成时），我们无法使线程返回结果。为了支持此功能， Java 中提供了 Callable 接口。

### 1、Callable 接口

#### 1、特点

- 为了实现 Runnable，需要实现不返回任何内容的 run()方法，而对于 Callable，需要实现在完成时返回结果的 call()方法。
- call()方法可以引发异常，而 run()则不能。
- 为实现 Callable 而必须重写 call 方法
- 不能直接替换 runnable，因为 Thread 类的构造方法根本没有 Callable

~~~java
// 创建新类 MyThread 实现 runnable 接口
class MyThread implements Runnable{
    @Override
    public void run() {}
}
// 新类 MyThread2 实现 callable 接口
class MyThread2 implements Callable<Integer>{
    @Override
    public Integer call() throws Exception {
        return 200;
    }
}
~~~

### 2、Future 接口

当 call 方法完成时，结果必须存储在主线程已知的对象中，以便主线程可以知道该线程返回的结果。为此，可以使用 Future 对象。

将 Future 视为保存结果的对象–它可能暂时不保存结果，但将来会保存（一旦 Callable 返回）。

Future 是主线程可以跟踪进度以及其他线程的结果的一种方式，并且要实现此接口，必须重写 5 种方法。

~~~java
public boolean cancel(boolean mayInterrupt)
    // 用于停止任务。
    // 如果尚未启动，它将停止任务。如果已启动，则仅在 mayInterrupt 为 true 时才会中断任务。
public Object get() throws InterruptedException，ExecutionException：
    // 用于获取任务的结果。
    // 如果任务完成，它将立即返回结果，否则将等待任务完成，然后返回结果。
public boolean isDone()
    // 如果任务完成，则返回 true，否则返回 false
~~~

可以看到 Callable 和 Future 做了两件事：

- Callable 与 Runnable 类似，因为它封装了要在另一个线程上运行的任务。
- 而 Future 用于存储从另一个线程获得的结果。

实际上，future 也可以与 Runnable 一起使用。

要创建线程，需要 Runnable。为了获得结果，需要 future。

### 3、FutureTask

FutureTask 类型实现 Runnable 和 Future，并方便地将两种功能组合在一起。 可以通过为其构造函数提供 Callable 来创建，将 FutureTask 对象提供给 Thread 的构造函数以创建 Thread 对象，间接地使用 Callable 创建线程。

在主线程中需要执行比较耗时的操作时，但又不想阻塞主线程时，可以把这些作业交给 Future 对象在后台完成。

FutureTask 仅在计算完成才能检索结果，如果计算尚未完成，则阻塞 get 方法，一旦计算完成，就不能再重新开始或取消计算（只计算一次）。

~~~java
class MyThread implements Callable{
    @Override
    public Long call() throws Exception {
        try {
            System.out.println(Thread.currentThread().getName() + "线程进入了call,开始准备睡觉");
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName() + "睡醒了");
        }catch (Exception e){
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
}

public static void main(String[] args) throws Exception{

    // callable
    Callable callable = new MyThread();
    // future-callable
    FutureTask<Long> futureTask = new FutureTask(callable);
    // Thread-futureTask
    new Thread(futureTask, "线程1").start();
    for (int i = 0; i < 10; i++) {
        Long result1 = futureTask.get();
        // 只计算一次
        System.out.println(result1);
    }
}
~~~



## 7、三大辅助类

### 1、CountDownLatch 

CountDownLatch 这个类使一个线程等待其他线程各自执行完毕后再执行。

CountDownLatch 类可以设置一个计数器代表参与线程数量，然后通过 countDown 方法来进行减 1 的操作，使用 await 方法等待计数器不大于 0，当计数器不大于0时继续执行 await 方法之后的语句。

- 当一个或多个线程调用 await 方法时，这些线程会阻塞。
- 其它线程调用 countDown 方法会将计数器减 1(调用 countDown 方法的线程不会阻塞)。
- 当计数器的值变为 0 时，因 await 方法阻塞的线程会被唤醒，继续执行。

~~~java
// 六个同学走后，值班同学才可关门（结束程序）
public static void main(String[] args) throws Exception{
    // 定义一个数值为 6 的计数器
    CountDownLatch countDownLatch = new CountDownLatch(6);
    // 创建 6 个同学 0，1，2，3，4，5，6(值班)
    for (int i = 1; i <= 6; i++) {
        new Thread(() ->{
            try{
                if(Thread.currentThread().getName().equals("同学 6")){
                    Thread.sleep(2000);
                }
                System.out.println(Thread.currentThread().getName() + "离开了");
                //计数器减一,不会阻塞
                countDownLatch.countDown();
            }catch (Exception e){
                e.printStackTrace();
            }
        }, "同学" + i).start();
    }
    // 主线程 await 休息
    System.out.println("主线程睡觉");
    countDownLatch.await();
    // 全部离开后自动唤醒主线程
    System.out.println("全部离开了,现在的计数器为" + countDownLatch.getCount());
}

~~~



### 2、CyclicBarrier 

CyclicBarrier 循环阻塞

该类的构造方法有俩个参数：

- 第一个参数是参与任务的线程个数。
- 第二个参数代表最后一个到达的线程要做的事。

每次执行 CyclicBarrier 一次，计数会加一，如果达到了目标数，才会执行 cyclicBarrier.await() 之后的语句，也即一个线程组的线程需要等待所有线程完成任务后再继续执行下一次任务。

~~~java
// 定义神龙召唤需要的龙珠总数
private final static int NUMBER = 7;

public static void main(String[] args) {
    // 定义循环栅栏
    CyclicBarrier cyclicBarrier = 
        new CyclicBarrier(NUMBER, ()->{System.out.println("集齐"+NUMBER+"颗龙珠,现在召唤神龙");});

    // 定义 7 个线程分别去收集龙珠
    for (int i = 1; i <= 7; i++) {
        new Thread(()->{
            try {
                if(Thread.currentThread().getName().equals("龙珠 3 号")){
                    System.out.println("龙珠 3 号抢夺战开始,孙悟空开启超级赛亚人模式!");
                    Thread.sleep(5000);
                    System.out.println("龙珠 3 号抢夺战结束,孙悟空打赢了,拿到了龙珠3号!");
                }else{
                    System.out.println(Thread.currentThread().getName() + "收集到了!!!!");
                }
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + "神龙召唤完毕");
            }catch (Exception e){
                e.printStackTrace();
            }
        }, "龙珠"+i+"号").start();
    }
}
~~~

**CountDownLatch和CyclicBarrier区别**：

- CountDownLatch 是一个计数器，线程完成一个记录一个，计数器递减，只能只用一次。
- CyclicBarrier 的计数器更像一个阀门，需要所有线程都到达，然后继续执行，计数器递增，提供reset功能，可以多次使用。



### 3、Semaphore

Semaphore 可以用来控制同时访问特定资源的线程数量。

Semaphore 的构造方法中传入的第一个参数是最大信号量（可以看成最大线程池），每个信号量初始化为一个最多只能分发一个许可证。

- 使用 acquire 方法获得许可证
- release 方法释放许可

~~~java
public static void main(String[] args) throws Exception{
    // 定义 3 个停车位
    Semaphore semaphore = new Semaphore(1);
    // 模拟 6 辆汽车停车
    for (int i = 1; i <= 10; i++) {
        Thread.sleep(100);
        // 停车
        new Thread(() ->{
            try {
                System.out.println(Thread.currentThread().getName() + "找车位 ing");
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + "汽车停车成功!");
                Thread.sleep(10000);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                System.out.println(Thread.currentThread().getName() + "溜了溜了");
                semaphore.release();
            }
        }, "汽车" + i).start();
    }
}
~~~



## 8、读写锁

### 1、基本概念

**场景**：对共享资源有读和写的操作，且写操作没有读操作那么频繁。在没有写操作的时候，多个线程允许同时读同一个资源，但是如果一个线程想去写这个共享资源， 就不允许其他线程对该资源进行读和写的操作了。

针对这个场景，JDK提供了 **ReentrantReadWriteLock** 类，它表示两个锁，一个是读操作相关的锁，称为**共享锁**，一个是写相关的锁，称为**排他锁**。

线程进入读锁的前提条件：

- 没有其他线程的写锁。
- 没有写请求，或者有写请求，但调用线程和持有写锁的线程是同一个(可重入锁)。

线程进入写锁的前提条件：

- 没有其他线程的读锁，包括自己也不能持有读锁。
- 没有其他线程的写锁。

读写锁有以下三个重要的特性： 

- 公平选择性：支持非公平（默认）和公平的锁获取方式，吞吐量还是非公平优于公平。 
- 重进入：读锁和写锁都支持线程重进入。 
- 锁降级：遵循获取写锁、获取读锁再释放写锁的次序，写锁能够降级成为读锁。

### 2、ReentrantReadWriteLock

~~~java
public class ReentrantReadWriteLock implements ReadWriteLock, java.io.Serializable {
    /** 读锁 */
    private final ReentrantReadWriteLock.ReadLock readerLock;
    /** 写锁 */
    private final ReentrantReadWriteLock.WriteLock writerLock;
    
    final Sync sync;

    /** 使用默认（非公平）的排序属性创建一个新的 ReentrantReadWriteLock */
    public ReentrantReadWriteLock() {
        this(false);
    }
    /** 使用给定的公平策略创建一个新的 ReentrantReadWriteLock */
    public ReentrantReadWriteLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
        readerLock = new ReadLock(this);
        writerLock = new WriteLock(this);
    }
    /** 返回用于写入操作的锁 */
    public ReentrantReadWriteLock.WriteLock writeLock() { return 
        writerLock; }

    /** 返回用于读取操作的锁 */
    public ReentrantReadWriteLock.ReadLock readLock() { return readerLock; }
    abstract static class Sync extends AbstractQueuedSynchronizer {}
    static final class NonfairSync extends Sync {}
    static final class FairSync extends Sync {}
    public static class ReadLock implements Lock, java.io.Serializable {}
    public static class WriteLock implements Lock, java.io.Serializable {}
}
~~~

ReentrantReadWriteLock 实现了 ReadWriteLock 接口， ReadWriteLock 接口定义了获取读锁和写锁的规范，具体需要实现类去实现。

同时其还实现了 Serializable 接口，表示可以进行序列化，在源代码中可以看到 ReentrantReadWriteLock 实现了自己的序列化逻辑。

~~~java
// 资源类
class MyCache {
    // 创建 map 集合
    private volatile Map<String,Object> map = new HashMap<>();
    
    // 创建读写锁对象
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();
    
    // 放数据
    public void put(String key,Object value) {
        // 添加写锁
        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+""+key);
            // 暂停一会
            TimeUnit.MICROSECONDS.sleep(300);
            // 放数据
            map.put(key,value);
            System.out.println(Thread.currentThread().getName()+""+key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放写锁
            rwLock.writeLock().unlock();
        }
    }
    
    // 取数据
    public Object get(String key) {
        // 添加读锁
        rwLock.readLock().lock();
        Object result = null;
        try {
            System.out.println(Thread.currentThread().getName()+""+key);
            // 暂停一会
            TimeUnit.MICROSECONDS.sleep(300);
            result = map.get(key);
            System.out.println(Thread.currentThread().getName()+""+key);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放读锁
            rwLock.readLock().unlock();
        }
        return result;
    }
}
~~~



## 9、阻塞队列

### 1、基本概念

阻塞队列，首先它是一个队列，通过一个共享的队列，可以使得数据由队列的一端输入，从另外一端输出。

![image-20220330113427933](images/image-20220330113427933.png)

- 当队列是空的，从队列中获取元素的操作将会被阻塞。
- 当队列是满的，从队列中添加元素的操作将会被阻塞。

常用的队列主要有以下两种：

- 先进先出（FIFO）：先插入的队列的元素也最先出队列，类似于排队的功能。 从某种程度上来说这种队列也体现了一种公平性。
- 后进先出（LIFO）：后插入队列的元素最先出队列，这种队列优先处理最近发生的事件(栈)。

适用场景：消息对垒、生产消费模型。

### 2、核心方法

![image-20220330113805035](images/image-20220330113805035.png)

#### 1、放入数据

**offer(anObject)**

- 表示如果可能的话，将 anObject 加到 BlockingQueue 里，即如果 BlockingQueue 可以容纳，则返回 true，否则返回 false。
- （本方法不阻塞当前执行方法的线程）

**offer(E o, long timeout, TimeUnit unit)**

- 可以设定等待的时间，如果在指定的时间内，还不能往队列中加入 BlockingQueue，则返回失败。

**put(anObject)**

- 把 anObject 加到 BlockingQueue 里，如果 BlockQueue 没有空间，则调用此方法的线程被阻塞直到 BlockingQueue 里面有空间再继续。

#### 2、获取数据

**poll(time)**

- 取走 BlockingQueue 里排在首位的对象，若不能立即取出，则可以等 time 参数规定的时间，取不到时返回 null

**poll(long timeout, TimeUnit unit)**

- 从 BlockingQueue 取出一个队首的对象， 如果在指定时间内，队列一旦有数据可取，则立即返回队列中的数据。否则直到时间超时还没有数据可取，返回失败。

**take()**

- 取走 BlockingQueue 里排在首位的对象，若 BlockingQueue 为空，阻塞进入等待状态直到 BlockingQueue 有新的数据被加入。

**drainTo()**

- 一次性从 BlockingQueue 获取所有可用的数据对象（还可以指定获取数据的个数），通过该方法，可以提升获取数据效率。
- 不需要多次分批加锁或释放锁。

~~~java
public class BlockingQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        
        // List list = new ArrayList();
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);
        
        //第一组
        // System.out.println(blockingQueue.add("a"));
        // System.out.println(blockingQueue.add("b"));
        // System.out.println(blockingQueue.add("c"));
        // System.out.println(blockingQueue.element());
        // System.out.println(blockingQueue.add("x"));
        // System.out.println(blockingQueue.remove());
        // System.out.println(blockingQueue.remove());
        // System.out.println(blockingQueue.remove());
        // System.out.println(blockingQueue.remove());
        
        // 第二组
        // System.out.println(blockingQueue.offer("a"));
        // System.out.println(blockingQueue.offer("b"));
        // System.out.println(blockingQueue.offer("c"));
        // System.out.println(blockingQueue.offer("x"));
        // System.out.println(blockingQueue.poll());
        // System.out.println(blockingQueue.poll());
        // System.out.println(blockingQueue.poll());
        // System.out.println(blockingQueue.poll());
        
        // 第三组
        // blockingQueue.put("a");
        // blockingQueue.put("b");
        // blockingQueue.put("c");
        // //blockingQueue.put("x");
        // System.out.println(blockingQueue.take());
        // System.out.println(blockingQueue.take());
        // System.out.println(blockingQueue.take());
        // System.out.println(blockingQueue.take());
        
        // 第四组
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("a",3L, TimeUnit.SECONDS));
    }
}

~~~

### 3、常见子类

#### 1、ArrayBlockingQueue

**特点**：**由数组结构组成的有界阻塞队列 **

基于**数组**的阻塞队列实现，在 ArrayBlockingQueue 内部，维护了一个**定长数组**，以便缓存队列中的数据对象，这是一个常用的阻塞队列，除了一个定长数组外，ArrayBlockingQueue 内部还保存着**两个整型变量**，分别标识着队列的**头部**和**尾部**在数组中的位置。

ArrayBlockingQueue 在生产者放入数据和消费者获取数据，都是共用同一个锁对象，由此也意味着两者无法真正并行运行，这点尤其不同于 LinkedBlockingQueue。

按照实现原理来分析，ArrayBlockingQueue 完全可以采用分离锁，从而实现生产者和消费者操作的完全并行运行。Doug Lea 之所以没这样去做，也许是因为 ArrayBlockingQueue 的数据写入和获取操作已经足够轻巧，以至于引入独立的锁机制，除了给代码带来额外的复杂性外，其在性能上完全占不到任何便宜。 

ArrayBlockingQueue 和 LinkedBlockingQueue 间还有一个明显的不同之处在于，前者在插入或删除元素时不会产生或销毁任何额外的对象实例，而后者则会生成一个额外的 Node 对象。这在长时间内需要高效并发地处理大批量数据的系统中，其对于 GC 的影响还是存在一定的区别。而在创建 ArrayBlockingQueue 时，还可以控制对象的内部锁是否采用公平锁，默认采用非公平锁。

#### 2、LinkedBlockingQueue

**特点**：**由链表结构组成的有界阻塞队列** （大小默认值为 integer.MAX_VALUE）

基于**链表**的阻塞队列，同 ArrayListBlockingQueue 类似，其内部也维持着一 个**数据缓冲队列**（该队列由一个链表构成）。

当生产者往队列中放入一个数据时，队列会从生产者手中获取数据，并缓存在队列内部，而生产者立即返回，只有当队列缓冲区达到最大值缓存容量时（LinkedBlockingQueue 可以通过构造函数指定该值），才会阻塞生产者队列，直到消费者从队列中消费掉一份数据，生产者线程会被唤醒，反之对于消费者这端的处理也基于同样的原理。

LinkedBlockingQueue 之所以能够高效的处理并发数据，还因为其对于生产者端和消费者端分别采用了独立的锁来控制数据同步，这也意味着在高并发的情况下生产者和消费者可以并行地操作队列中的数据，以此来提高整个队列 的并发性能。

#### 3、DelayQueue

**特点**：**使用优先级队列实现的延迟无界阻塞队列**

DelayQueue 中的元素只有当其指定的延迟时间到了，才能够从队列中获取到该元素。

DelayQueue 是一个**没有大小限制的队列**，因此往队列中插入数据的操作（生产者）永远不会被阻塞，而只有获取数据的操作（消费者）才会被阻塞。

#### 4、PriorityBlockingQueue

**特点**：**支持优先级排序的无界阻塞队列**

基于**优先级的阻塞队列**（优先级的判断通过构造函数传入的 Compator 对象来决定）

**注意**：PriorityBlockingQueue 并不会阻塞生产者，而只会在没有可消费的数据时，阻塞消费者。 因此使用的时候要特别注意，生产者生产数据的速度绝对不能快于消费者消费数据的速度，否则时间一长，会最终耗尽所有的可用堆内存空间。 

在实现 PriorityBlockingQueue 时，内部控制线程同步的锁采用的是**公平锁**。

#### 5、SynchronousQueue

**特点**：**不存储元素的阻塞队列，也即单个元素的队列**

一种**无缓冲的等待队列**，类似于无中介的直接交易，有点像原始社会中的生产者和消费者。

生产者拿着产品去集市销售给产品的最终消费者，而消费者必须亲自去集市找到所要商品的直接生产者，如果一方没有找到合适的目标，那么对不起，大家都在集市等待。相对于有缓冲的 BlockingQueue 来说，少了一个中间经销商的环节（缓冲区），如果有经销商，生产者直接把产品批发给经销商，而无需在意经销商最终会将这些产品卖给那些消费者，由于经销商可以库存一部分商品，因此相对于直接交易模式，总体来说采用中间经销商的模式会吞吐量高一些（可以批量买卖）。但另一方面，又因为经销商的引入，使得产品从生产者到消费者中间增加了额外的交易环节，单个产品的及时响应性能可能会降低。 

声明一个 SynchronousQueue 有两种不同的方式，它们之间有着不太一样的行为：

- **公平模式**：SynchronousQueue 会采用**公平锁**，并配合一个 **FIFO 队列**来阻塞多余的生产者和消费者，从而体系整体的公平策略。

- **非公平模式**（SynchronousQueue 默认）：SynchronousQueue 采用**非公平锁**，同时配合一个 **LIFO 队列**来管理多余的生产者和消费者，而此种模式， 如果生产者和消费者的处理速度有差距，则很容易出现饥渴的情况，即可能有某些生产者或者是消费者的数据永远都得不到处理。

#### 6、LinkedTransferQueue

**特点**：**由链表组成的无界阻塞队列**

由**链表**结构组成的**无界**阻塞 TransferQueue 队列。

相对于其他阻塞队列，LinkedTransferQueue 多了 tryTransfer 和 transfer 方法。 

LinkedTransferQueue 采用一种**预占模式**，意思就是消费者线程取元素时，如果队列不为空，则直接取走数据，若队列为空，那就生成一个节点（节点元素 为 null）入队，然后消费者线程被等待在这个节点上，后面生产者线程入队时发现有一个元素为 null 的节点，生产者线程就不入队了，直接就将元素填充到该节点，并唤醒该节点等待的线程，被唤醒的消费者线程取走元素，从调用的方法返回。

#### 7、LinkedBlockingDeque

**特点**：**由链表组成的双向阻塞队列**

由**链表**结构组成的**双向**阻塞队列，即可以从队列的两端插入和移除元素。 

对于一些指定的操作，在插入或者获取队列元素时如果队列状态不允许该操作，可能会阻塞住该线程直到队列状态变更为允许操作，这里的阻塞一般有两种情况：

- **插入元素时**：如果当前队列已满将会进入阻塞状态，一直等到队列有空的位置时，再将该元素插入，该操作可以通过设置超时参数，超时后返回 false 表示操作失败，也可以不设置超时参数一直阻塞，中断后抛出 InterruptedException 异常。
- **读取元素时**：如果当前队列为空会阻塞住直到队列不为空然后返回元素，同样可以通过设置超时参数。

## 10、ThreadPool 线程池

### 1、基本概念

一种线程使用模式。

线程过多会带来调度开销， 进而影响缓存局部性和整体性能。

线程池维护着多个线程，等待着监督管理者分配可并发执行的任务，可以避免了在处理短时间任务时创建与销毁线程的代价。

线程池不仅能够保证内核的充分利用，还能防止过分调度。

**优势**： 线程池做的工作只是控制运行的线程数量，处理过程中将任务放入队列，然后在线程创建后消费这些任务，如果任务数量超过了最大数量，超出数量的任务排队等候，等其他线程执行完毕，再从队列中取出任务来执行。

**特点**：

- 降低资源消耗：通过重复利用已创建的线程降低线程创建和销毁造成的销耗。
- 提高响应速度：当任务到达时，任务可以不需要等待线程创建就能立即执行。
- 提高线程的可管理性：线程是稀缺资源，如果无限制的创建，不仅会销耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。
- Java 中的线程池是通过 Executor 框架实现的，该框架中用到了 Executor，Executors，ExecutorService，ThreadPoolExecutor

![image-20220330161703074](images/image-20220330161703074.png)

### 2、参数介绍

| 参数            | 作用                                         |
| --------------- | -------------------------------------------- |
| corePoolSize    | 线程池的核心线程数，也即最小的线程数（重要） |
| maximumPoolSize | 能容纳的最大线程数，最大线程数（重要）       |
| keepAliveTime   | 空闲线程存活时间                             |
| unit            | 存活的时间单位                               |
| workQueue       | 存放提交但未执行任务的队列，阻塞队列（重要） |
| threadFactory   | 创建线程的工厂类                             |
| handler         | 等待队列满后的拒绝策略                       |

当提交任务数大于 corePoolSize 的时候，会优先将任务放到 workQueue 阻塞队列中。当阻塞队列饱和后，会扩充线程池中线程数，直到达到 maximumPoolSize 最大线程数配置。此时，再多余的任务，则会触发线程池的拒绝策略了，也即此三者参数影响拒绝策略。

> 当提交的任务数 >（workQueue.size() +  maximumPoolSize ），就会触发线程池的拒绝策略。

### 3、拒绝策略

#### CallerRunsPolicy

- 当触发拒绝策略，只要线程池没有关闭的话，则使用调用线程池的线程(上层线程)直接运行任务。
- 一般并发比较小，性能要求不高，不允许失败。（如果数据处理时间顺序上有要求，可能建议使用）
- 由于调用者自己运行任务，如果任务提交速度过快，可能导致程序阻塞，性能效率上必然的损失较大 。

#### AbortPolicy

- 丢弃任务，并抛出拒绝执行。

#### RejectedExecutionException

- 异常信息。线程池默认的拒绝策略。必须处理好抛出的异常，否则会打断当前的执行流程，影响后续的任务执行。

#### DiscardPolicy

- 直接丢弃，其他啥都没有。

#### DiscardOldestPolicy

- 当触发拒绝策略，只要线程池没有关闭的话，丢弃阻塞队列 workQueue 中最老的一个任务，并将新任务加入。

### 4、线程池种类/创建

#### newCachedThreadPool(常用)

- **作用**：创建一个**可缓存**线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
- **特点**：
  - 线程池中数量没有固定，可达到最大值（Interger. MAX_VALUE）。
  - 线程池中的线程可进行缓存重复利用和回收（回收默认时间为 1 分钟）。
  - 当线程池中，没有可用线程，会重新创建一个线程。
- **场景**：适用于创建一个可无限扩大的线程池，服务器负载压力较轻，执行时间较短，任务多的场景

~~~java
public static ExecutorService newCachedThreadPool(){
    return new ThreadPoolExecutor(0,
                                  Integer.MAX_VALUE,
                                  60L,
                                  TimeUnit.SECONDS,
                                  new SynchronousQueue<>(),
                                  Executors.defaultThreadFactory(),
                                  new ThreadPoolExecutor.AbortPolicy());
}
~~~

#### newFixedThreadPool(常用)

- **作用**：创建一个**可重用固定线程数**的线程池，以**共享的无界队列**方式来运行这些线程。在任意点，在大多数线程会处于处理任务的活动状态。如果在所有线程处于活动状态时提交附加任务，则在有可用线程之前，附加任务将在队列中等待。如果在关闭前的执行期间由于失败而导致任何线程终止，那么一个新线程将代替它执行后续的任务（如果需要）。在某个线程被显式地关闭之前，池中的线程将一直存在。
- **特点**：
  - 线程池中的线程处于一定的量，可以很好的控制线程的并发量。
  - 线程可以重复被使用，在显示关闭之前，都将一直存在。
  - 超出一定量的任务被提交时候需在队列中等待。
- **场景**：适用于可以预测线程数量的业务中，或者服务器负载较重，对线程数有严格限制的场景

~~~java
public static ExecutorService newFixedThreadPool(){
    return new ThreadPoolExecutor(10,
                                  10,
                                  0L,
                                  TimeUnit.SECONDS,
                                  new LinkedBlockingQueue<>(),
                                  Executors.defaultThreadFactory(),
                                  new ThreadPoolExecutor.AbortPolicy());
}
~~~

#### newSingleThreadExecutor(常用)

- **作用**：创建一个使用单个 **worker** 线程的 Executor，以**无界队列**方式来运行该线程。（注意，如果因为在关闭前的执行期间出现失败而终止了此单个线程， 那么如果需要，一个新线程将代替它执行后续的任务）。可保证顺序地执行各个任务，并且在任意给定的时间不会有多个线程是活动的。与其他等效的 newFixedThreadPool 不同，可保证无需重新配置此方法所返回的执行程序，即可使用其他的线程。
- **特点**： 线程池中最多执行 1 个线程，之后提交的线程活动将会排在队列中以此执行
- **场景**：适用于需要保证顺序执行各个任务，并且在任意时间点，不会同时有多个线程的场景

~~~java
public static ExecutorService newSingleThreadExecutor(){
    return new ThreadPoolExecutor(1,
                                  1,
                                  0L,
                                  TimeUnit.SECONDS,
                                  new LinkedBlockingQueue<>(),
                                  Executors.defaultThreadFactory(),
                                  new ThreadPoolExecutor.AbortPolicy());
}

~~~

#### newScheduleThreadPool

- **作用**：线程池支持定时以及周期性执行任务，创建一个 corePoolSize 为传入参数，最大线程数为整形的最大数的线程池。
- **特点**：
  - 线程池中具有指定数量的线程，即便是空线程也将保留。
  - 可定时 或者 延迟执行线程活动。
- **场景**：适用于需要多个后台线程执行周期任务的场景。

~~~java
public static ScheduledExecutorService newScheduledThreadPool(int
                                                              corePoolSize, 
                                                              ThreadFactory threadFactory) {
    return new ScheduledThreadPoolExecutor(corePoolSize, 
                                           threadFactory);
}
~~~

#### newWorkStealingPool

- **作用**：jdk1.8 提供的线程池，底层使用的是 **ForkJoinPool** 实现，创建一个拥有**多个任务队列**的线程池，可以减少连接数，创建当前可用 cpu 核数的线程来并行执行任务。
- **场景**：适用于大耗时，可并行执行的场景。

~~~java
public static ExecutorService newWorkStealingPool(int parallelism) {
    return new ForkJoinPool(parallelism,
                            ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                            null,
                            true);
}
~~~



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






















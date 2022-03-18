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






















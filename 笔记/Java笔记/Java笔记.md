

# JDK8新特性

## Lambda表达式

### 1、基本概念

JDK8新特性，取代大部分匿名内部类。

需要函数式接口支持也就是，Lambda规定接口中只能有一个需要被实现的方法，不是规定接口中只能有一个方法。

@Functionallinterface 修饰函数式接口，可以检查该接口是否只有一个抽象方法，要求接口中抽象方法只有一个，此注解往往和lambda表达式一起出现。

### 2、语法形式

> （）-> { }；
>
> - 其中（）用来描述参数列表
> - { }用来表述方法体也叫lambda体
> - -> 为lambda运算符，读作goes to

- 语法一：无参数，无返回值（）-> { }；
- 语法二：一个参数，无返回值（x）-> { }；
- 语法三：两个以上的参数，lambda体具有多条语句（x, y）-> { xxx; zzzzzz;}；



### 3、四大核心函数式接口 

| 函数式接口 | 参数类型 | 返回类型 | 用途 |
| ---- | ---- | ---- | ---- |
|Consumer 消费型接口| T| void| 对类型为T的对象应用操作，<br />包含方法： void accept(T t) |
|Supplier 供给型接口 |无| T | 返回类型为T的对象，<br />包含方法：T get() |
|Function 函数型接口| T |R| 对类型为T的对象应用操作，并返回结果。结果是R类型的对象。<br />包含方法：R apply(T t) |
|Predicate 断定型接口| T |boolean |确定类型为T的对象是否满足某约束，并返回<br />包含方法：boolean test(T t)|
|BiFunction |T,U |R |对类型为 T, U 参数应用操作，返回 R 类型的结果。<br />包含方法为： R apply(T t, U u); |
|UnaryOperator (Function子接口) |T |T| 对类型为T的对象进行一元运算，并返回T类型的结果。<br />包含方法为：T apply(T t); |
|BinaryOperator (BiFunction 子接口) |T,T| T| 对类型为T的对象进行二元运算，并返回T类型的结果。<br />包含方法为： T apply(T t1, T t2); |
|BiConsumer |T,U |void |对类型为T, U 参数应用操作。 <br />包含方法为： void accept(T t, U u) |
|BiPredicate |T,U| boolean |包含方法为： boolean test(T t,U u)|

![image-20220403093333848](images/image-20220403093333848.png)

### 4、简化Lambda

#### 1、简化参数类型

- （）中可以不写参数类型，但必须所有的都不写，因为JVM具有类型推断，通过上下文类型推断。

- 只有一个参数，可以不写（） 

> x -> { }；

- 方法体内只有一条语句或者只有一条return语句，可以不写{ } 

> x -> xxxx；

![image-20220403093344635](images/image-20220403093344635.png)

#### 2、方法引用

**语法**：方法归属者 **::** 方法名 

- 实例对象::实例方法名
- 类::静态方法名
- 类::实例方法名

**注意**：

- 静态方法的归属者为类对象，普通方法归属者为实例对象
- Lambda体中调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数列表和返回值类型保持一致。
- Lambda参数列表中第一个参数是实例方法的调用者，而第二个参数是实例方法的时，可以用类::实例方法名

~~~java
// 方法引用-对象::实例方法
Consumer<Integer> con2 = System.out::println;
con2.accept(200);

// 方法引用-类名::静态方法名
BiFunction<Integer, Integer, Integer> biFun = (x, y) -> Integer.compare(x, y);
BiFunction<Integer, Integer, Integer> biFun2 = Integer::compare;
Integer result = biFun2.apply(100, 200);

// 方法引用-类名::实例方法名
BiFunction<String, String, Boolean> fun1 = (str1, str2) -> str1.equals(str2);
BiFunction<String, String, Boolean> fun2 = String::equals;
Boolean result2 = fun2.apply("hello", "world");
System.out.println(result2);
~~~



#### 3、构造器引用

声明接口，该接口作为对象的生成器，通过 **类名::new** 的方式来实例化对象，通过调用方法返回对象。

**注意**：需要调用的构造器的参数列表要与函数式接口中的抽象方法的参数列表保持一致

~~~java
// 构造方法引用  类名::new
Supplier<Employee> sup = () -> new Employee();
System.out.println(sup.get());
Supplier<Employee> sup2 = Employee::new;
System.out.println(sup2.get());

// 构造方法引用 类名::new （带一个参数）
Function<Integer, Employee> fun = (x) -> new Employee(x);
Function<Integer, Employee> fun2 = Employee::new;
System.out.println(fun2.apply(100));
~~~

![image-20220403094039820](images/image-20220403094039820.png)



## Stream流

### 1、基本概念

对指定集合进行复杂的查找、过滤、映射数据等操作，可以串行也可以并行。

**注意**：

- Stream本身不会存储元素。
- Stream不会改变源对象，会返回一个持有结果的新Stream。
- Stream操作具有延迟，也就是需要结果的时候才执行。

### 2、三个步骤

#### 1、创建Stream

获取一个数据源（集合、数组），获取一个流

使用**Collection接口**中的方法

~~~java
- default Stream<E> stream() : 返回一个顺序流
    
- default Stream<E> parallelStream() : 返回一个并行流
~~~



使用**数组**创建流，具有多个重载形式

~~~java
- static <T> Stream<T> stream(T[] array): 返回一个流

- public static IntStream stream(int[] array)

- public static LongStream stream(long[] array)

- public static DoubleStream stream(double[] array)
~~~



使用**值**创建流，使用静态方法Stream.of()，可以接受任意数量的参数

~~~java
- public static<T> Stream<T> of(T... values) : 返回一个流。
~~~



使用**函数**创建流，无限流

~~~java
- public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f) 迭代

- public static<T> Stream<T> generate(Supplier<T> s) 生成
~~~



#### 2、中间操作Stream

一个中间操作链，对数据源的数据处理，该操作的返回值仍然是流

##### 1、筛选与切片 

~~~java
- filter(Predicate p) 接收 Lambda ， 从流中排除某些元素。

- distinct() 筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素

- limit(long maxSize) 截断流，使其元素不超过给定数量。
    
- peek(Consumer<? super T> action) 此方法的存在主要是为了支持调试，希望在元素流过管道中的某个点时查看它们

- skip(long n) 跳过元素，返回一个扔掉了前 n 个元素的流。若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补
~~~



##### 2、映射  

~~~java
- map(Function f) 接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。

- mapToDouble(ToDoubleFunction f) 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的 DoubleStream。

- mapToInt(ToIntFunction f) 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的 IntStream。

- mapToLong(ToLongFunction f) 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的 LongStream。

- flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) 
    返回一个流，其中包含将此流的每个元素替换为通过将提供的mapper映射函数应用于每个元素而生成的映射流的内容的结果。
    
- flatMapToInt(Function<? super T, ? extends IntStream> mapper) 
    根据给定的mapper作用于当前流的每个元素，将结果组成新的Int流来返回
    同理有flatMapToLong flatMapToDouble
~~~



##### 3、排序 

~~~java
- sorted() 产生一个新流，其中按自然顺序排序

- sorted(Comparator comp) 产生一个新流，其中按比较器顺序排序
~~~





#### 3、终止操作

终止操作会执行中间操作链，并产生结果

##### 1、查找与匹配 

~~~java
- allMatch(Predicate p) 检查是否匹配所有元素

- anyMatch(Predicate p) 检查是否至少匹配一个元素，也即Stream中是否存在任何一个元素满足匹配条件

- noneMatch(Predicate p) 检查是否没有匹配所有元素，也即是不是Stream中的所有元素都不满足给定的匹配条件
   
- findFirst() 返回第一个元素

- findAny() 返回当前流中的任意元素，多个挑一个

- count() 返回流中元素总数

- max(Comparator c) 返回流中最大值

- min(Comparator c) 返回流中最小值
    
- toArray() 有俩个重载，一个有参数指定返回的数组类型，一个无参数默认Object[]，作用将结果返回为一个数组

- forEach(Consumer c) 
    内部迭代(使用 Collection 接口需要用户去做迭代，称为外部迭代。相反，Stream API 使用内部迭代——它帮你把迭代做了)
    在并行的情况下不保证顺序，而forEachOrdered保证顺序
~~~



##### 2、归约 （重要）

简介：

归约操作（也称为折叠）接受一个元素序列为输入，反复使用某个合并操作，把序列中的元素合并成一个汇总的结果。

比如查找一个数字列表的总和或者最大值，或者把这些数字累积成一个List对象。

Stream接口有一些通用的归约操作，比如reduce()和collect()；也有一些特定用途的归约操作，比如sum(),max()和count()。

注意：sum()方法不是所有的Stream对象都有的，只有IntStream、LongStream和DoubleStream是实例才有。



~~~java
- reduce(T iden, BinaryOperator b) 可以将流中元素反复结合起来，得到一个值。返回 T

- reduce(BinaryOperator b) 可以将流中元素反复结合起来，得到一个值。返回 Optional<T>
~~~



##### 3、收集 （重要）

~~~java
collect(Collector c) 将流转换为其他形式。接收一个 Collector 接口的实现，用于给Stream中元素做汇总的方法
~~~

Collector 接口中方法的实现决定了如何对流执行收集操作(如收集到 List、Set、Map)。<a href="#Collector 接口">详见</a> 

Collectors 实现类是JDK Collector 接口的预实现类，提供了很多静态方法，可以方便地创建常见收集器实例。<a href="#Collectors 实用类">详见</a> 

 ![image-20220402222628755](images/image-20220402222628755.png)









### 3、串行流与并行流

并行流：把一个内容分成多个数据块，并用不同的线程分别处理每个数据块的流，Stream API通过parallel()与sequential()进行并串切换，其底层使用Fork/Join框架。

### 4、<a name="Collector 接口">Collector 接口</a> 

#### 1、参数简介

Collector 有五个主要参数，也即一些函数式接口

~~~java
public interface Collector<T, A, R> {
    // supplier参数用于生成结果容器，容器类型为A
    Supplier<A> supplier();
    // accumulator用于消费元素，也就是归纳元素，这里的T就是元素，它会将流中的元素一个一个与结果容器A发生操作
    BiConsumer<A, T> accumulator();
    // combiner用于两个两个合并并行执行的线程的执行结果，将其合并为一个最终结果A
    BinaryOperator<A> combiner();
    // finisher用于将之前整合完的结果R转换成为A
    Function<A, R> finisher();
    // characteristics表示当前Collector的特征值，这是个不可变Set
    Set<Characteristics> characteristics();
}
~~~

Collector拥有两个of方法用于生成Collector实例，其中一个拥有上面所有五个参数，另一个四个参数，不包括finisher。

~~~java
public interface Collector<T, A, R> {
    // 四参方法，用于生成一个Collector，T代表流中的一个一个元素，R代表最终的结果
    public static<T, R> Collector<T, R, R> of(Supplier<R> supplier,
                                              BiConsumer<R, T> accumulator,
                                              BinaryOperator<R> combiner,
                                              Characteristics... characteristics) {/*...*/}
    
    // 五参方法，用于生成一个Collector，T代表流中的一个一个元素，A代表中间结果，R代表最终结果，finisher用于将A转换为R      
    public static<T, A, R> Collector<T, A, R> of(Supplier<A> supplier,
                                                 BiConsumer<A, T> accumulator,
                                                 BinaryOperator<A> combiner,
                                                 Function<A, R> finisher,
                                                 Characteristics... characteristics) {/*...*/}                                              
}
~~~

>Characteristics：这个特征值是一个枚举，拥有三个值：CONCURRENT（多线程并行），UNORDERED（无序），IDENTITY_FINISH（无需转换结果）。
>
>其中四参of方法中没有finisher参数，所以必有IDENTITY_FINISH特征值。

#### 2、<a name="Collectors 实用类">Collectors 实现类</a> 

Collectors是一个工具类，是JDK预实现Collector的工具类，它内部提供了多种Collector，可直接使用。

以下为各个方法的用例

~~~java
// toCollection 将流中的元素全部放置到一个集合中返回，这里使用Collection，泛指多种集合。
List<String> ll = list.stream().collect(Collectors.toCollection(LinkedList::new));

------------------------------------------------------------------------------------------------------------
    
// toList 将流中的元素放置到一个列表集合中去。这个列表默认为ArrayList。
List<String> ll = list.stream().collect(Collectors.toList());

------------------------------------------------------------------------------------------------------------
    
// toSet 将流中的元素放置到一个无序集set中去。默认为HashSet。
Set<String> ss = list.stream().collect(Collectors.toSet());

------------------------------------------------------------------------------------------------------------
    
// joining 目的是将流中的元素全部以字符序列的方式连接到一起，可以指定连接符，甚至是结果的前后缀。
// 无参方法
String s = list.stream().collect(Collectors.joining());
// 指定连接符
String ss = list.stream().collect(Collectors.joining("-"));
// 指定连接符和前后缀
String sss = list.stream().collect(Collectors.joining("-","S","E"));

------------------------------------------------------------------------------------------------------------
    
// mapping 这个映射是首先对流中的每个元素进行映射，即类型转换，然后再将新元素以给定的Collector进行归纳。
// mapping 收集器在用于多级归约时最有用，例如groupingBy或partitioningBy的下游
// mapping 方法有俩个参数
// 第一个参数是Function类型的函数，应用于输入元素的函数
// 第二个参数是Collector类型，将接受映射值的收集器
List<Integer> ll = list.stream().limit(5).collect(Collectors.mapping(Integer::valueOf,Collectors.toList()));
Map<City, Set<String>> lastNamesByCity = 
    people.stream()
    .collect(groupingBy(Person::getCity, mapping(Person::getLastName, toSet())));

------------------------------------------------------------------------------------------------------------
    
// collectingAndThen 该方法是在收集动作结束之后，对收集的结果进行再处理，也即调整Collector以执行额外的整理转换
// collectingAndThen 有两个参数
// 第一个参数是下游的Collectors
// 第二个参数是对Collectors产生的结果进行处理的函数，是Function类型
int length = list.stream().collect(Collectors.collectingAndThen(Collectors.toList(), e -> e.size()));
List<String> list = people.stream().collect(collectingAndThen(toList(), Collections::unmodifiableList));

------------------------------------------------------------------------------------------------------------
    
// counting 用于计数
long size = list.stream().collect(Collectors.counting());

------------------------------------------------------------------------------------------------------------
    
// minBy/maxBy 生成一个用于获取最小/最大值的Optional结果。
// minBy/maxBy 只有一个Comparator类型的参数
// 等效于reducing(BinaryOperator.minBy(comparator))
list.stream().collect(Collectors.maxBy((a,b) -> a.length()-b.length()));
list.stream().collect(Collectors.minBy((a,b) -> a.length()-b.length()));

------------------------------------------------------------------------------------------------------------
    
// summingInt/summingLong/summingDouble 生成一个用于求元素和的Collector。
// 首先通过给定的mapper将元素转换类型，然后再求和，最后结果与转换后类型一致。
int i = list.stream().limit(3).collect(Collectors.summingInt(Integer::valueOf));
long l = list.stream().limit(3).collect(Collectors.summingLong(Long::valueOf));
double d = list.stream().limit(3).collect(Collectors.summingDouble(Double::valueOf));

------------------------------------------------------------------------------------------------------------
    
// averagingInt/averagingLong/averagingDouble 生成一个用于求元素平均值的Collector。
// 参数的作用就是将元素转换为指定的类型，求平均值涉及到除法操作，结果一律为Double类型。
double i = list.stream().limit(3).collect(Collectors.averagingInt(Integer::valueOf));
double l = list.stream().limit(3).collect(Collectors.averagingLong(Long::valueOf));
double d = list.stream().limit(3).collect(Collectors.averagingDouble(Double::valueOf));

------------------------------------------------------------------------------------------------------------
    
// reducing 方法有三个重载方法，其实是和Stream里的三个reduce方法对应的。
// 二者是可以替换使用的，作用完全一致，也是对流中的元素做统计归纳作用。

// 无初始值的情况，返回一个可以生成Optional结果的Collector
public static <T> Collector<T, ?, Optional<T>> reducing(BinaryOperator<T> op) {/*...*/}
Map<City, Optional<Person>> tallestByCity = people.stream()
    .collect(groupingBy(Person::getCity, reducing(BinaryOperator.maxBy(byHeight))));

// 有初始值的情况，返回一个可以直接产生结果的Collector
public static <T> Collector<T, ?, T> reducing(T identity, BinaryOperator<T> op) {/*...*/}

// 有初始值，还有针对元素的处理方案mapper，生成一个可以直接产生结果的Collector。
// 元素在执行结果操作op之前需要先执行mapper进行元素转换操作
public static <T, U> Collector<T, ?, U> reducing(U identity,
                                                 Function<? super T, ? extends U> mapper,
                                                 BinaryOperator<U> op) {/*...*/}
 Map<City, String> longestLastNameByCity = people.stream()
     .collect(groupingBy(Person::getCity, reducing("",Person::getLastName,BinaryOperator.maxBy(byLength))));

list.stream().limit(4).map(String::length).collect(Collectors.reducing(Integer::sum));
list.stream().limit(3).map(String::length).collect(Collectors.reducing(0, Integer::sum));
list.stream().limit(4).collect(Collectors.reducing(0, String::length, Integer::sum));

------------------------------------------------------------------------------------------------------------
    
// groupingBy 返回一个Collector ，对T类型的输入元素执行“分组依据”操作，根据分类函数对元素进行分组，并在Map中返回结果。
// groupingBy 有三个重载方法

// 第一个参数分类器，是Function类型，内部自动将结果保存到一个map中
// 每个map的键为?类型（即classifier的结果类型），值为一个list，这个list中保存在属于这个组的元素。
public static <T, K> Collector<T, ?, Map<K, List<T>>> 
    groupingBy(Function<? super T, ? extends K> classifier) {/*...*/}
Map<Integer,List<String>> s = list.stream()
    .collect(Collectors.groupingBy(String::length));

// 第一个参数分类器，对T类型的输入元素实现级联“分组依据”操作，根据分类函数对元素进行分组
// 第二个参数下游收集器，使用指定的下游Collector对与给定键关联的值执行归约操作。
// 也即在上面方法的基础上增加了对流中元素的处理方式的Collector，比如上面的默认的处理方法就是Collectors.toList()
public static <T, K, A, D>Collector<T, ?, Map<K, D>> 
    groupingBy(Function<? super T, ? extends K> classifier,Collector<? super T, A, D> downstream) {/*...*/}
 Map<City, Set<String>> namesByCity = people.stream()
     .collect(groupingBy(Person::getCity, mapping(Person::getLastName, toSet())));
Map<Integer,List<String>> ss = list.stream()
    .collect(Collectors.groupingBy(String::length, Collectors.toList()));

// 第一个参数分类器，对T类型的输入元素实现级联“分组依据”操作，根据分类函数对元素进行分组
// 第二个参数Map工厂，用于提供一个空的map，保存此次分组的结果
// 第三个参数下游收集器，使用指定的下游Collector对与给定键关联的值执行归约操作。
// 也即在第二个方法的基础上再添加了结果Map的生成方法。
public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> 
    groupingBy(Function<? super T, ? extends K> classifier,
                                  Supplier<M> mapFactory,
                                  Collector<? super T, A, D> downstream) {/*...*/}
Map<City, Set<String>> namesByCity = people.stream()
    .collect(groupingBy(Person::getCity, TreeMap::new, mapping(Person::getLastName, toSet())));
Map<Integer,Set<String>> sss = list.stream()
    .collect(Collectors.groupingBy(String::length,HashMap::new, Collectors.toSet()));

// groupingByConcurrent 并发版groupingBy，功能效果一致

------------------------------------------------------------------------------------------------------------
    
// partitioningBy 根据Predicate对输入元素进行分区，并将它们组织成一个Map
// partitioningBy 方法将流中的元素按照给定的校验规则的结果分为两个部分
// 其中一份结果放到一个map中返回，map的键是Boolean类型，值为元素的列表List。
// 该方法有俩个重载

// 只需一个校验参数predicate
public static <T> Collector<T, ?, Map<Boolean, List<T>>> 
    partitioningBy(Predicate<? super T> predicate) {/*...*/}
Map<Boolean,List<String>> map = list.stream().collect(Collectors.partitioningBy(e -> e.length()>5));

// 在上面方法的基础上增加了对流中元素的处理方式的Collector，比如上面的默认的处理方法就是Collectors.toList()
public static <T, D, A> Collector<T, ?, Map<Boolean, D>> 
    partitioningBy(Predicate<? super T> predicate, Collector<? super T, A, D> downstream) {/*...*/}
Map<Boolean,Set<String>> map2 = list.stream()
    .collect(Collectors.partitioningBy(e -> e.length()>6, Collectors.toSet()));

------------------------------------------------------------------------------------------------------------
  
// toMap 方法是根据给定的键生成器和值生成器生成的键和值保存到一个map中返回
// 键和值的生成都依赖于元素，可以指定出现重复键时的处理方案和保存结果的map。
// 该方法有三个重载
    
// 指定键和值的生成方式keyMapper和valueMapper
// 如果映射的键可能有重复项，请改用toMap(Function, Function, BinaryOperator) 
public static <T, K, U> Collector<T, ?, Map<K,U>> 
    toMap(Function<? super T, ? extends K> keyMapper,Function<? super T, ? extends U> valueMapper) {/*...*/}
Map<Student, Double> studentToGPA = students.stream()
    .collect(toMap(Function.identity(), student -> computeGPA(student)));
Map<String, Student> studentIdToStudent = students.stream()
    .collect(toMap(Student::getId, Function.identity()));
Map<String,String> map = list.stream().limit(3).collect(Collectors.toMap(e -> e.substring(0,1), e -> e));
// 在上面方法的基础上增加了对键发生重复时处理方式的mergeFunction，比如上面的默认的处理方法就是抛出异常
// 注意是处理键冲突
public static <T, K, U> Collector<T, ?, Map<K,U>> 
    toMap(Function<? super T, ? extends K> keyMapper
          ,Function<? super T, ? extends U> valueMapper
          ,BinaryOperator<U> mergeFunction) {/*...*/}
Map<String, String> phoneBook = people.stream()
    .collect(toMap(Person::getName, Person::getAddress, (s, a) -> s + ", " + a));
Map<String,String> map1 = list.stream().collect(Collectors.toMap(e -> e.substring(0,1), e->e, (a,b)-> b));
// 在第二个方法的基础上再添加了Map工厂，可以指定结果Map的生成方法。
public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> 
    toMap(Function<? super T, ? extends K> keyMapper
          ,Function<? super T, ? extends U> valueMapper
          ,BinaryOperator<U> mergeFunction
          ,Supplier<M> mapSupplier) {/*...*/}
Map<String,String> map2 = list.stream()
    .collect(Collectors.toMap(e -> e.substring(0,1), e->e,(a,b)->b, HashMap::new));

// toConcurrentMap 和toMap 功能用法效果一致

------------------------------------------------------------------------------------------------------------
  ;
// summarizingInt/summarizingLong/summarizingDouble
// 适用于汇总的，返回值分别是IntSummaryStatistics，LongSummaryStatistics，DoubleSummaryStatistics。
// 在这些返回值中包含有流中元素的指定结果的数量、和、最大值、最小值、平均值。所有仅仅针对数值结果。
// summarizingInt ----> 有一个参数mapper，作用域每一个元素，产生一个int映射，剩下俩方法同样道理。
IntSummaryStatistics intSummary = list.stream()
    .collect(Collectors.summarizingInt(String::length));

LongSummaryStatistics longSummary = list.stream().limit(4)
    .collect(Collectors.summarizingLong(Long::valueOf));

DoubleSummaryStatistics doubleSummary = list.stream().limit(3)
    .collect(Collectors.summarizingDouble(Double::valueOf));
        
~~~

扩展：

- StringJoiner：这是一个字符串连接器，可以定义连接符和前后缀，正好适用于实现joining第三种joining方法。





# 集合框架

## 1、基本概念

Java 集合可分为 **Collection** 和 **Map** 两种体系

- Collection接口：单列数据，定义了存取一组对象的方法的集合
  - List：元素有序、可重复的集合
  - Set：元素无序、不可重复的集合
- Map接口：双列数据，保存具有映射关系“key-value对”的集合

![image-20220401091830467](images/image-20220401091830467.png)

![image-20220401091919923](images/image-20220401091919923.png)

## 2、Collection 接口

### 1、基本概念

- Collection 接口是 **List**、**Set** 和 **Queue** 接口的父接口，该接口里定义的方法既可用于操作 Set 集合，也可用于操作 List 和 Queue 集合。 
- JDK不提供此接口的任何直接实现，而是提供更具体的子接口(如：Set和List) 实现。 
- 在 Java5 之前，Java 集合会丢失容器中所有对象的数据类型，把所有对象都当成 Object 类型处理，从 JDK 5.0 增加了泛型以后，Java 集合可以记住容器中对象的数据类型。

### 2、常用方法

| 用途 | 方法 |
| ---- | ---- |
| 添加  | add(Object obj)、addAll(Collection coll) |
|  获取有效元素的个数    |  int size()    |
|   清空集合   |   void clear()   |
|	是否是空集合	|	boolean isEmpty()	|
|	是否包含某个元素	|	boolean contains(Object obj)：是通过元素的equals方法来判断是否是同一个对象 <br>boolean containsAll(Collection c)：也是调用元素的equals方法来比较的。拿两个集合的元素挨个比较。	|
|	删除		|	boolean remove(Object obj) ：通过元素的equals方法判断是否是要删除的那个元素。只会删除找到的第一个元素<br>boolean removeAll(Collection coll)：取当前集合的差集	|
|取两个集合的交集	|	boolean retainAll(Collection c)：把交集的结果存在当前集合中，不影响c	|
|集合是否相等	|	boolean equals(Object obj)	|
|	转成对象数组	|	Object[] toArray()	|
|	获取集合对象的哈希值	|hashCode()	|
|	遍历	|	iterator()：返回迭代器对象，用于集合遍历	|

### 3、List 子接口

#### 1、基本概念

- List集合类中元素有序、且可重复，集合中的每个元素都有其对应的顺序索引。
- List容器中的元素都对应一个整数型的序号记载其在容器中的位置，可以根据序号存取容器中的元素。
- JDK API中List接口的实现类常用的有：ArrayList、LinkedList 和 Vector。

**常用方法**：

| 方法 | 用途 |
| ---- | ---- |
|void add(int index, Object ele) |在index位置插入ele元素 |
|boolean addAll(int index, Collection eles) |从index位置开始将eles中 的所有元素添加进来 |
|Object get(int index) |获取指定index位置的元素 |
|int indexOf(Object obj) |返回obj在集合中首次出现的位置 |
|int lastIndexOf(Object obj) |返回obj在当前集合中末次出现的位置 |
|Object remove(int index) |移除指定index位置的元素，并返回此元素 |
|Object set(int index, Object ele) |设置指定index位置的元素为ele |
|List subList(int fromIndex, int toIndex) |返回从fromIndex到 toIndex 位置的子集合|

#### 2、常用子类

##### ArrayList

ArrayList本质上是对象引用的一个**”变长”数组**。

~~~java
transient Object[] elementData;
~~~

##### LinkedList

**双向链表**，内部没有声明数组，而是定义了Node类型的first和last， 用于记录首末元素。同时，定义内部类Node，作为LinkedList中保存数据的基本结构。

Node除了保存数据，还定义了两个变量：prev变量记录前一个元素的位置，next变量记录下一个元素的位置

新增方法

~~~java
void addFirst(Object obj)

void addLast(Object obj)

Object getFirst() 

Object getLast() 

Object removeFirst()

Object removeLast()
~~~

##### Vector

大多数操作与ArrayList 相同，区别之处在于Vector是线程安全的。

新增方法

~~~java
void addElement(Object obj)
    
void insertElementAt(Object obj,int index)
    
void setElementAt(Object obj,int index)
    
void removeElement(Object obj)
    
void removeAllElements()
~~~



#### 特殊方法

##### Arrays.asList

该方法是将数组转化成List集合的方法。

**注意**：

- 该方法适用于对象型数据的数组（String、Integer...）

- 该方法不建议使用于基本数据类型的数组（byte,short,int,long,float,double,boolean）

- 该方法将数组与List列表链接起来：当更新其一个时，另一个自动更新

- 不支持add()、remove()、clear()等方法
- 方法返回的 List 集合，既不是 ArrayList 实例，也不是 Vector 实例。 
- 其返回值是一个固定长度的 List 集合，用此方法得到的List的长度是不可改变的。

**特别注意**：

当向这个List添加或删除一个元素时（例如 list.add("d");）程序就会抛出异常（java.lang.UnsupportedOperationException）。

先看该方法源码： 

~~~java
public static <T> List<T> asList(T... a) {return new ArrayList<>(a);}
~~~

这个ArrayList不是java.util包下的，而是java.util.Arrays.ArrayList，它是Arrays类自己定义的一个静态内部类，这个内部类没有实现add()、remove()方法，而是直接使用它的父类AbstractList的相应方法。

而AbstractList中的add()和remove()是直接抛出java.lang.UnsupportedOperationException异常的。

~~~java
public void add(int index, E element) { throw new UnsupportedOperationException();}

public E remove(int index) {throw new UnsupportedOperationException();}
~~~

### 4、Set 子接口

#### 1、基本概念

- Set接口是Collection的子接口，set接口没有提供额外的方法
- Set 集合不允许包含相同的元素，如果试把两个相同的元素加入同一个 Set 集合中，则添加操作失败。
- Set 判断两个对象是否相同不是使用 == 运算符，而是根据 equals() 方法

#### 2、常用子类

##### HashSet

HashSet 是 Set 接口的典型实现，大多数时候使用 Set 集合时都使用这个实现类。

HashSet 按 Hash 算法来存储集合中的元素，因此具有很好的存取、查找、删除 性能。

判断两个元素相等的标准：

- 两个对象通过 hashCode() 方法比较相等
- 并且两个对象的 equals() 方法返回值也相等。
- 任意一个不等都不行！

**注意**：

- 对于存放在Set容器中的对象，对应的类一定要**重写equals()和hashCode(Object  obj)方法**，以实现对象相等规则。即：“相等的对象必须具有相等的散列码”。

**特点**： 

- 不能保证元素的排列顺序 
- HashSet 不是线程安全的
- 集合元素可以是 null

**添加元素的过程**：

1. 当向 HashSet 集合中存入一个元素时，HashSet 会调用该对象的 **hashCode**() 方法来得到该对象的 hashCode 值，然后根据 hashCode 值，通过某种**散列函数**决定该对象 在 HashSet **底层数组**中的**存储位置**。（这个散列函数会与底层数组的长度相计算得到在数组中的下标，并且这种散列函数计算还**尽可能保证能均匀存储元素**，越是散列分布， 该散列函数设计的越好）
2. 如果两个元素的**hashCode**()值相等，会再继续调用**equals**方法，如果equals方法结果为true，添加失败，如果为false，那么会保存该元素，但是该数组的位置已经有元素了，那么会通过链表的方式继续链接。
3. 如果两个元素的 **equals**() 方法返回 true，但它们的 **hashCode**() 返回值不相 等，hashSet 将会把它们存储在不同的位置，依然可以添加成功。

![image-20220401115015307](images/image-20220401115015307-16487850165343.png)

底层也是**数组**，初始容量为16，当如果使用率超过0.75，（16*0.75=12） 就会扩大容量为原来的**2倍**。（16扩容为32，依次为64,128....等）

##### LinkedHashSet

- LinkedHashSet 是 **HashSet** 的子类 
- LinkedHashSet 根据元素的 hashCode 值来决定元素的存储位置， 但它同时使用**双向链表**维护元素的次序，这使得元素看起来是以插入顺序保存的。 
- LinkedHashSet插入性能略低于 HashSet，但在迭代访问 Set 里的全部元素时有很好的性能。 
- LinkedHashSet 不允许集合元素重复。

##### TreeSet

- TreeSet 是 **SortedSet** 接口的实现类，TreeSet 可以确保集合元素处于排序状态。
- TreeSet底层使用**红黑树**结构存储数据。
- TreeSet 两种排序方法：**自然排序**和**定制排序**。默认情况下，TreeSet 采用自然排序。

**注意**：

- TreeSet只能添加同类对象，不然抛出ClassCastException异常。

新增方法：

~~~java
Comparator comparator()
    
Object first()
    
Object last()
    
Object lower(Object e)
    
Object higher(Object e)
    
SortedSet subSet(fromElement, toElement)
    
SortedSet headSet(toElement)
    
SortedSet tailSet(fromElement)
~~~

**自然排序注意事项**：

- TreeSet 会调用集合元素的 **compareTo**(Object obj) 方法来比较元素之间的大小关系，然后将集合元素按升序(默认情况)排列。也就是说，如果试图把一个对象添加到 TreeSet 时，则该对象的类必须实现 **Comparable**  接口。
- 向 TreeSet 中添加元素时，只有**第一个**元素无须比较compareTo()方法，后面添 加的所有元素都会调用compareTo()方法进行比较。
- **建议**：对象如果有重写equals方法，那么如果equals方法于compareTo方法应该一致。equals---->ture，compareTo---->0

**定制排序注意事项**：

- 通过Comparator接口来 实现。需要重写**compare(T o1,T o2)**方法，比较o1和o2的大小：如果方法返回正整数，则表示o1大于o2，如果返回0，表示相等，返回负整数，表示o1小于o2。

## 3、Map接口

### 1、基本概念

- Map 与 Collection 并列存在。用于**保存具有映射关系**的数据：key - value。
- Map 中的 key 和 value 可以是**任何引用类型的数据**。
- Map 中的 key 用 **Set** 来存放，**不允许重复**，即同一个 Map 对象所对应的类，因此key所在类重写hashCode()和equals()方法。
- 常用String类作为Map的键。
- key 和 value 之间存在单向一对一关系，即通过指定的 key 总能找到唯一的、确定的 value。
- Map接口的常用实现类：**HashMap**、**TreeMap**、**LinkedHashMap**和 **Properties**。其中，HashMap是 Map 接口使用频率最高的实现类

### 2、常用方法

| 方法 | 用途 |
| ---- | ---- |
|Object put(Object key,Object value)|将指定key-value添加到(或修改)当前map对象中 |
|void putAll(Map m)|将m中的所有key-value对存放到当前map中 |
|Object remove(Object key)|移除指定key的key-value对，并返回value |
|void clear()|清空当前map中的所有数据 |
|Object get(Object key)|获取指定key对应的value |
|boolean containsKey(Object key)|是否包含指定的key |
|boolean containsValue(Object value)|是否包含指定的value |
|int size()|返回map中key-value对的个数 |
|boolean isEmpty()|判断当前map是否为空 |
|boolean equals(Object obj)|判断当前map和参数对象obj是否相等 |
|Set keySet()|返回所有key构成的Set集合 |
|Collection values()|返回所有value构成的Collection集合 |
|Set entrySet()|返回所有key-value对构成的Set集合|

### 3、常用子类

#### HashMap

允许使用null键和null值，与HashSet一样，不保证映射的顺序。

所有的key构成的集合是Set无序的、不可重复的，所以，key所在的类要重写equals()和hashCode()。

所有的value构成的集合是Collection无序的、可以重复的，所以，value所在的类要重写equals()。

一个key-value构成一个entry ，所有的entry构成的集合是Set无序的、不可重复的。

**注意**：

-  判断两个 key 相等的标准是：两个 key 通过 equals() 方法返回 true， hashCode 值也相等。
- 判断两个 value 相等的标准是：两个 value 通过 equals() 方法返回 true。

**JDK7/JDK8区别**：

- JDK 7及以前版本：HashMap是**数组**+**链表结构**(即为链地址法) 。
- JDK 8版本发布以后：HashMap是**数组**+**链表**+**红黑树实现**。并新增操作，桶的树形化

![image-20220401155541027](images/image-20220401155541027.png)

![image-20220401155559300](images/image-20220401155559300.png)

**源码**：

| 常量 | 作用 |
| ---- | ---- |
|DEFAULT_INITIAL_CAPACITY |HashMap的默认容量，16|
|MAXIMUM_CAPACITY|HashMap的最大支持容量，2^30|
|DEFAULT_LOAD_FACTOR|HashMap的默认加载因子|
|TREEIFY_THRESHOLD|Bucket中链表长度大于该默认值，转化为红黑树|
|UNTREEIFY_THRESHOLD|Bucket中红黑树存储的Node小于该默认值，转化为链表|
|MIN_TREEIFY_CAPACITY|桶中的Node被树化时最小的hash表容量。（当桶中Node的数量大到需要变红黑树时，若hash表容量小于MIN_TREEIFY_CAPACITY时，此时应执行resize扩容操作这个|
|table|存储元素的数组，总是2的n次幂|
|entrySet|存储具体元素的集|
|size|HashMap中存储的键值对的数量|
|modCount|HashMap扩容和结构改变的次数|
|threshold|扩容的临界值 = 数组容量*填充因子|
|loadFactor|填充因子|

#### LinkedHashMap

- LinkedHashMap 是 HashMap 的子类 。
- 在HashMap存储结构的基础上，使用了一对**双向链表**来记录添加元素的顺序。
- 与LinkedHashSet类似，LinkedHashMap 可以维护 Map 的迭代顺序：迭代顺序与 Key-Value 对的插入顺序一致。

#### TreeMap

- TreeMap 存储 Key-Value 对时，需要根据 key-value 对进行排序。 TreeMap 可以保证所有的 Key-Value 对处于有序状态。
- TreeSet底层使用**红黑树**结构存储数据。

**排序**：

- 自然排序：TreeMap 的所有的 Key 必须实现 **Comparable** 接口，而且所有 的 Key 应该是**同一个类**的对象，否则将会抛出 ClasssCastException
- 定制排序：创建 TreeMap 时，传入一个 Comparator 对象，该对象负责对 TreeMap 中的所有 key 进行排序。此时不需要 Map 的 Key 实现 Comparable 接口

判断两个key相等的标准：两个key通过**compareTo**()方法或者**compare**()方法返回0。

#### Hashtable

- Hashtable是线程安全的。
- Hashtable实现原理和HashMap相同，功能相同。底层都使用哈希表结构，查询速度快，很多情况下可以互用。
- 与HashMap一样，Hashtable 也不能保证其中 Key-Value 对的顺序
- 与HashMap一样，Hashtable 判断两个key相等、两个value相等的标准。
- 与HashMap不同，Hashtable 不允许使用 null 作为 key 和 value

#### Properties

- Properties 类是 Hashtable 的子类，该对象用于**处理属性文件**。
- 由于属性文件里的 key、value 都是字符串类型，所以 Properties 里的 **key**  和 **value** 都是字符串类型。
- 存取数据时，建议使用setProperty(String key,String value)方法和getProperty(String key)方法。




## 4、Iterator迭代器接口

### 1、基本概念

-  Iterator对象称为迭代器(设计模式的一种)，主要用于遍历 Collection 集合中的元素。
- Collection接口继承了java.lang.**Iterable**接口，该接口有一个**iterator**()方法，那么所有实现了Collection接口的集合类都有一个iterator()方法，用以返回一个实现了Iterator接口的对象。
- Iterator 仅用于遍历集合，Iterator 本身并不提供承装对象的能力。如果需要创建Iterator 对象，则必须有一个被迭代的集合。
- 集合对象每次调用iterator()方法都得到一个**全新**的迭代器对象，默认游标都在集合的**第一个**元素之前。

### 2、常用方法

![image-20220401093056277](images/image-20220401093056277-16487766572261.png)



**注意**：

- 在调用**it.next()**方法之前必须要调用**it.hasNext()**进行检测。若不调用，且下一条记录无效，直接调用it.next()会抛出NoSuchElementException异常。

- Iterator可以删除集合的元素，但是是遍历过程中通过迭代器对象的remove方法，不是集合对象的remove方法。如果还未调用next()或在上一次调用 next 方法之后已经调用了 remove 方法，再调用remove都会报IllegalStateException。

- 使用 foreach 循环遍历集合元素，底层是调用了Iterator完成操作。

## 5、Comparable 排序接口

### 1、基本概念

**典型实现**： 

- BigDecimal、BigInteger 以及所有的数值型对应的包装类：按它们对应的数值大小进行比较 
- Character：按字符的 unicode值来进行比较 
- Boolean：true 对应的包装类实例大于 false 对应的包装类实例 
- String：按字符串中字符的 unicode 值进行比较 
- Date、Time：后边的时间、日期比前面的时间、日期大

## 6、Collections 工具类

### 1、基本概念

- Collections 是一个操作 Set、List 和 Map 等集合的工具类
- Collections 中提供了一系列静态的方法对集合元素进行排序、查询和修改等操作， 还提供了对集合对象设置不可变、对集合对象实现同步控制等方法

### 2、常用方法

| 方法 | 用途 |
| ---- | ---- |
|reverse(List)|反转 List 中元素的顺序 |
|shuffle(List)|对 List 集合元素进行随机排序 |
|sort(List)|根据元素的自然顺序对指定 List 集合元素按升序排序 |
|sort(List，Comparator)|根据指定的 Comparator 产生的顺序对 List 集合元素进行排序 |
|swap(List，int， int)|将指定 list 集合中的 i 处元素和 j 处元素进行交换<br>操作数组的工具类：Arrays 查找、替换 |
|Object max(Collection)|根据元素的自然顺序，返回给定集合中的最大元素 |
|Object max(Collection，Comparator)|根据 Comparator 指定的顺序，返回 给定集合中的最大元素 |
|Object min(Collection) | |
|Object min(Collection，Comparator) | |
|int frequency(Collection，Object)|返回指定集合中指定元素的出现次数 |
|void copy(List dest, List src)|将src中的内容复制到dest中 |
|boolean replaceAll(List list，Object oldVal，Object newVal)|使用新值替换 List 对象的所有旧值|

Collections 类中提供了多个 synchronizedXxx() 方法，该方法可使将指定集合包装成线程同步的集合，从而可以解决多线程并发访问集合时的线程安全问题。

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

### 1.7、Thread类

#### 1、构造器

~~~java
// 创建新的Thread对象
Thread()
    
// 创建线程并指定线程实例名
Thread(String threadname)
    
// 指定创建线程的目标对象，它实现了Runnable接中的run方法
Thread(Runnable target)
    
// 创建新的Thread对象
Thread(Runnable target, String name)
~~~

**注意**：

- run()方法由JVM调用，什么时候调用，执行的过程控制都有操作系统的CPU调度决定。
- 启动线程必须调用start方法。
- 一个线程对象只能调用一次start()方法启动，如果重复调用了，则将抛出以上的异常“IllegalThreadStateException”

#### 2、常用方法

~~~java
// 启动线程，并执行对象的run()方法
void start();
    
// 线程在被调度时执行的操作
run();

// 返回线程的名称
String getName();

// 设置该线程名称
void setName(String name);

// 返回当前线程。在Thread子类中就是this，通常用于主线程和Runnable实现类
static Thread currentThread();

// 线程让步
// 暂停当前正在执行的线程，把执行机会让给优先级相同或更高的线程
// 若队列中没有同优先级的线程，忽略此方法
static void yield();

// 当某个程序执行流中调用其他线程的 join() 方法时，调用线程将被阻塞，直到 join() 方法加入的 join 线程执行完为止
// 低优先级的线程也可以获得执行
join();

// (指定时间 毫秒)
// 令当前活动线程在指定时间段内放弃对CPU控制,使其他线程有机会被执行,时间到后重排队。
// 抛出InterruptedException异常
sleep(long millis);

// 返回boolean，判断线程是否还活着    
isAlive();

// 返回线程优先值
getPriority();

// 改变线程的优先级
setPriority(int newPriority)

~~~

### 1.8、线程调度

![image-20220331190717200](images/image-20220331190717200.png)

 Java的调度方法

- 同优先级线程组成先进先出队列（先到先服务），使用时间片策略
- 对高优先级，使用优先调度的抢占式策略

线程优先级：

- MAX_PRIORITY：10 
- MIN _PRIORITY：1 
- NORM_PRIORITY：5

**注意**：

- 线程创建时继承父线程的优先级
- 低优先级只是获得调度的概率低，并非一定是在高优先级线程之后才被调用

![image-20220331191258153](images/image-20220331191258153.png)

![image-20220331191305466](images/image-20220331191305466.png)



## 2、Synchronized 关键字

### 1、基本概念

synchronized 是 Java 中的关键字，是一种同步锁。

synchronized 实现同步的基础：Java 中的每一个对象都可以作为锁。

每个对象都有一个锁，并且是唯一的，锁是针对对象的，所以也叫对象锁。

它修饰的对象有以下几种：

- 修饰一个**代码块**，被修饰的代码块称为同步语句块，其作用的范围是大括号 { } 括起来的代码，作用的对象是该实例
- 修饰一个**方法**，被修饰的方法称为同步方法，其作用的范围是该实例对象，作用的对象是该实例。

- 修改一个**静态的方法**，其作用的范围是整个静态方法，作用的对象是这个类的所有对象。针对类，也叫类锁
- 修改一个**类**，其作用的范围是 synchronized 后面括号括起来的部分，作用的对象是这个类的所有对象。针对类，也叫类锁

>1、对于普通同步方法，锁是当前实例对象this，被锁定后，其它的线程都不能进入到当前对象的其它的 synchronized 方法。因为对象的锁唯一。只有解锁后再由JVM去分配。
>
>2、对于静态同步方法，锁是当前类的 Class 对象。 
>
>3、对于同步方法块，锁是 Synchonized 括号里配置的对象。
>
>4、静态同步方法与非静态同步方法之间是不会有竞态条件的。

**注意**：

虽然可以使用 synchronized 来修饰方法，但 synchronized 并不属于方法定义的一部分，因此 **synchronized 关键字不能被继承**。

如果在父类中的某个方法使用了 synchronized 关键字，而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，而必须显式地在子类的这个方法中加上 synchronized 关键字才可以。

当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，但子类调用了父类的同步方法，因此， 子类的方法也就相当于同步了。

**场景**：

1. 同一个对象在两个线程中分别访问该对象的两个非静态同步方法

   - 会产生互斥

   - 因为锁针对的是对象，当对象调用一个synchronized方法时，其他同步方法需要等待其执行结束并释放锁后才能执行。

2. 不同对象在两个线程中调用同一个非静态同步方法

   - 不会产生互斥
   - 因为是两个对象，锁针对的是对象，并不是方法，所以可以并发执行，不会互斥。

3. 两个线程中调用类的两个不同的静态同步方法

   - 会产生互斥
   - 类对象只有一个，可以理解为只有一把锁

4. 一个对象在两个线程中分别调用一个静态同步方法和一个非静态同步方法

   - 不会产生互斥
   - 锁类型不一样，产生的不是同一个对象锁，一个是类的，一个是实例的

### 2、Synchronized(){}代码块

- 括号内不写或者括号内写this，效果是一样的，只是后者更精确，有时显得更高效

- 括号内写非this对象
  - 这个"非this对象"大多数是实例变量及方法的参数，可以是任意的。
  - 锁住的不是当前实例对象，而是此非this对象，即对该非this对象进行加锁。
  - 代码块中的程序与同步方法是异步的，不与其他锁this同步方法争抢this锁，大大提高了运行效率。
  - 非this对象，这个对象如果是实例变量的话，指的是对象的引用，只要对象的引用不变，即使改变了对象的属性，运行结果依然是同步的。





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

> wait() 与 notify() 和 notifyAll(）
>
> 注意：
>
> - 这三个方法只有在synchronized方法或synchronized代码块中才能使用，否则会报java.lang.IllegalMonitorStateException异常。
> - 因为这三个方法必须有锁对象调用，而任意对象都可以作为synchronized的同步锁，因此这三个方法只能在Object类中声明。

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
            System.out.println(Thread.currentThread().getName() + "加一成功----------,值为 " + number);
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
            System.out.println(Thread.currentThread().getName() + "减一成功----------,值为 " + number);
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
            System.out.println(Thread.currentThread().getName() + "加一成功,值为 " + number);
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
            System.out.println(Thread.currentThread().getName() + "减一成功,值为 " + number);
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
    //通信对象 0--打印 A 1---打印 B 2----打印 C
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
            new SynchronizedRandomAccessList<>(list)  
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

#### 1、基本概念

当 call 方法完成时，结果必须存储在主线程已知的对象中，以便主线程可以知道该线程返回的结果，为此，可以使用 Future 对象，将 Future 视为保存结果的对象---它可能暂时不保存结果，但将来会保存（一旦 Callable 返回）。

Futrue 在 Java 里面，通常用来表示一个异步任务的引用，比如将任务提交到线程池里面，然后会得到一个 Futrue，在 Future 里面有 isDone 方法来判断任务是否处理结束，还有 get 方法可以一直阻塞直到任务结束然后获取结果，但整体来说这种方式，还是同步的，因为需要客户端不断阻塞等待或者不断轮询才能知道任务是否完成。



#### 2、基本使用

Future 是主线程可以跟踪进度以及其他线程的结果的一种方式，并且要实现此接口，必须重写 5 种方法。

~~~java
// 用于停止任务。
// 如果尚未启动，它将停止任务。如果已启动，则仅在 mayInterrupt 为 true 时才会中断任务。
public boolean cancel(boolean mayInterrupt)
    
// 用于获取任务的结果。
// 如果任务完成，它将立即返回结果，否则将等待任务完成，然后返回结果。阻塞
public Object get() throws InterruptedException，ExecutionException：
    
// 如果任务完成，则返回 true，否则返回 false
public boolean isDone()
~~~

可以看到 Callable 和 Future 做了两件事：

- Callable 与 Runnable 类似，因为它封装了要在另一个线程上运行的任务。
- 而 Future 用于存储从另一个线程获得的结果。

实际上，future 也可以与 Runnable 一起使用。

要创建线程，需要 Runnable。为了获得结果，需要 future。



#### 3、缺点

- 不支持手动完成
  - 提交了一个任务，但是执行太慢了，并且通过其他路径已经获取到了任务结果， 现在没法把这个任务结果通知到正在执行的线程，所以必须主动取消或者一直等待它执行完成。
- 不支持进一步的非阻塞调用
  - 通过 Future 的 get 方法会一直阻塞到任务完成，但是想在获取任务之后执行额外的任务，因为 Future 不支持回调函数，所以无法实现这个功能。
- 不支持链式调用
  - 对于 Future 的执行结果，我们想继续传到下一个 Future 处理使用，从而形成 一个链式的 pipline 调用，这在 Future 中是没法实现的。
- 不支持多个 Future 合并
  - 有 10 个 Future 并行执行，我们想在所有的 Future 运行完毕之后， 执行某些函数，是没法通过 Future 实现的。
- 不支持异常处理
  - Future 的 API 没有任何的异常处理的 api，所以在异步运行时，如果出了问题是不好定位的。



### 3、FutureTask

FutureTask 类型实现 **Runnable** 和 **Future**，并方便地将两种功能组合在一起。 可以通过为其构造函数提供 **Callable** 来创建，将 FutureTask 对象提供给 Thread 的构造函数以创建 Thread 对象，间接地使用 Callable 创建线程。

在主线程中需要执行比较耗时的操作时，但又不想阻塞主线程时，可以把这些作业交给 Future 对象在后台完成。

FutureTask 仅在计算完成才能检索结果，如果计算尚未完成，则**阻塞 get 方法**，一旦计算完成，就不能再重新开始或取消计算（只计算一次）。

~~~java
class MyThread implements Callable<Long> {
    @Override
    public Long call() throws Exception {
        try {
            System.out.println(Thread.currentThread().getName() + "线程进入了call,开始准备睡觉");
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + "睡醒了");
        }catch (Exception e){
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
}

public static void main(String[] args) throws Exception{

    // callable
    Callable<Long> callable = new MyThread();
    // future-callable
    FutureTask<Long> futureTask = new FutureTask<>(callable);
    // Thread-futureTask
    new Thread(futureTask, "线程1").start();
    for (int i = 0; i < 10; i++) {
        System.out.println("第" + i + "次");
        // 阻塞
        // 只计算一次
        Long result1 = futureTask.get();
        System.out.println(result1);
    }
}


线程1线程进入了call,开始准备睡觉 // 任务开始
第0次    // 调用 get 进入阻塞，因为线程还未完成任务
线程1睡醒了 // 任务完成
1648712008121 // 得到的值都是同样的
第1次
1648712008121 
第2次
1648712008121
第3次
1648712008121
第4次
1648712008121
第5次
1648712008121
第6次
1648712008121
第7次
1648712008121
第8次
1648712008121
第9次
1648712008121
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
        sync = fair ? new FairSync()   new NonfairSync();
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

<img src="images/image-20220331175240485.png" alt="image-20220331175240485" style="zoom  80%;" />

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

### 5、基本工作原理

![image-20220331142339187](images/image-20220331142339187.png)

1. 在创建了线程池后，线程池中的线程数为零。
2. 当调用 execute()方法添加一个请求任务时，线程池会做出如下判断：
   1. 如果正在运行的线程数量小于 corePoolSize，那么马上创建线程运行这个任务。
   2. 如果正在运行的线程数量大于或等于 corePoolSize，那么将这个任务放入队列。
   3. 如果这个时候队列满了且正在运行的线程数量还小于 maximumPoolSize，那么还是要创建非核心线程立刻运行这个任务。
   4. 如果队列满了且正在运行的线程数量大于或等于 maximumPoolSize，那么线程池会启动饱和拒绝策略来执行。
3. 当一个线程完成任务时，它会从队列中取下一个任务来执行。
4. 当一个线程无事可做超过一定的时间（keepAliveTime）时，线程会判断：
   1. 如果当前运行的线程数大于 corePoolSize，那么这个线程就被停掉。
   2. 所以线程池的所有任务完成后，它最终会收缩到 corePoolSize 的大小。

### 6、注意事项

创建多线程时，使用常见的三种线程池创建方式，单一、可变、定长都有一定问题，原因是 FixedThreadPool 和 SingleThreadExecutor 底层都是用 LinkedBlockingQueue 实现的，这个队列最大长度为 Integer.MAX_VALUE， 容易导致 OOM。所以实际生产一般自己通过 ThreadPoolExecutor 的 7 个参数，自定义线程池。

<img src="images/image-20220331175350301.png" alt="image-20220331175350301" style="zoom 80%;" />

## 11、Fork/Join 框架

### 1、基本概念

Fork/Join 它可以将一个大的任务拆分成多个子任务进行并行处理，最后将子任务结果合并成最后的计算结果，并进行输出。

- Fork：把一个复杂任务进行分拆，大事化小 
- Join：把分拆任务的结果进行合并

首先 Fork/Join 框架需要把大的任务分割成足够小的子任务，如果子任务比较大的话还要对子任务进行继续分割，分割的子任务分别放到**双端队列**里，然后几个启动线程分别从双端队列里获取任务执行。子任务执行完的结果都放在另外一个队列里， 启动一个线程从队列里取数据，然后合并这些数据。



### 2、常用类

![image-20220331143732635](images/image-20220331143732635.png)

**ForkJoinTask**

- 我们要使用 Fork/Join 框架，首先需要创建一个 ForkJoin 任务。 该类提供了在任务中执行 fork 和 join 的机制。通常情况下我们不需要直接集成 ForkJoinTask 类，只需要继承它的子类，Fork/Join 框架提供了两个子类：
  - **RecursiveAction**：用于没有返回结果的任务 
  - **RecursiveTask**：用于有返回结果的任务，继承后可以实现递归(自己调自己)调用的任务

**ForkJoinPool**

- ForkJoinTask 需要通过 ForkJoinPool 来执行

- **实现原理**：ForkJoinPool 由 **ForkJoinTask** **数组**和 **ForkJoinWorkerThread** **数组**组成， ForkJoinTask 数组负责存放以及将任务提交给 ForkJoinPool，而 ForkJoinWorkerThread 负责执行这些任务。

### 3、fork/join方法

#### Fork 方法

当我们调用 ForkJoinTask 的 fork 方法时，程序会把任务放在 ForkJoinWorkerThread 的 pushTask 的 workQueue 中，异步地执行这个任务，然后立即返回结果。

~~~java
public final ForkJoinTask<V> fork() {
    Thread t;
    if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread)
        ((ForkJoinWorkerThread)t).workQueue.push(this);
    else
        ForkJoinPool.common.externalPush(this);
    return this;
}
~~~

而 pushTask 方法把当前任务存放在 ForkJoinTask 数组队列里。然后再调用 ForkJoinPool 的 signalWork()方法唤醒或创建一个工作线程来执行任务。代码如下：

~~~java
final void push(ForkJoinTask<?> task) {
    ForkJoinTask<?>[] a; ForkJoinPool p;
    int b = base, s = top, n;
    if ((a = array) != null) { // ignore if queue removed
        int m = a.length - 1; // fenced write for task visibility
        U.putOrderedObject(a, ((m & s) << ASHIFT) + ABASE, task);
        U.putOrderedInt(this, QTOP, s + 1);
        if ((n = s - b) <= 1) {
            if ((p = pool) != null)
                p.signalWork(p.workQueues, this); // 执行
        }
        else if (n >= m)
            growArray();
    }
}
~~~

####  Join 方法

Join 方法的主要作用是阻塞当前线程并等待获取结果。让我们一起看看 ForkJoinTask 的 join 方法的实现，代码如下：

~~~java
public final V join() {
    int s;
    if ((s = doJoin() & DONE_MASK) != NORMAL)
        reportException(s);
    return getRawResult();
}
~~~

它首先调用 doJoin 方法，通过 doJoin()方法得到当前任务的状态来判断返回什么结果，任务状态有 4 种：

- 已完成（NORMAL），如果任务状态是已完成，则直接返回任务结果
- 被取消（CANCELLED），如果任务状态是被取消，则直接抛出 CancellationException
- 信号（SIGNAL）
- 出现异常（EXCEPTIONAL），如果任务状态是抛出异常，则直接抛出对应的异常

doJoin 方法的实现

~~~java
private int doJoin() {
    int s; 
    Thread t; 
    ForkJoinWorkerThread wt; 
    ForkJoinPool.WorkQueue w;
    
    return (s = status) < 0 ? s   
    ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) ?
        (w = (wt = (ForkJoinWorkerThread)t).workQueue).
        tryUnpush(this) && (s = doExec()) < 0 ? s  
    wt.pool.awaitJoin(w, this, 0L)  
    externalAwaitDone();
}
final int doExec() {
    int s; boolean completed;
    if ((s = status) >= 0) {
        try {
            completed = exec();
        } catch (Throwable rex) {
            return setExceptionalCompletion(rex);
        }
        if (completed)
            s = setCompletion(NORMAL);
    }
    return s;
}
~~~

在 doJoin()方法流程如下：

1. 首先通过查看任务的状态，看任务是否已经执行完成，如果执行完成，则直接返回任务状态。
2. 如果没有执行完，则从任务数组里取出任务并执行。 
3. 如果任务顺利执行完成，则设置任务状态为 NORMAL，如果出现异常，则记录异常，并将任务状态设置为 EXCEPTIONAL。

### 4、异常处理

ForkJoinTask 在执行的时候可能会抛出异常，但是没办法在主线程里直接捕获异常。

所以 ForkJoinTask 提供了 **isCompletedAbnormally**()方法来检查任务是否已经抛出异常或已经被取消了。

并且可以通过 ForkJoinTask 的 **getException** 方法获取异常。

getException 方法返回 **Throwable** 对象

- 如果任务被取消了则返回 CancellationException。
- 如果任务没有完成或者没有抛出异常则返回 null。

### 5、案例

~~~java
public class TaskExample extends RecursiveTask<Long> {
    private int start;
    private int end;
    private long sum;

    public TaskExample(int start, int end){
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        System.out.println("任务" + start + "=========" + end + "累加开始");
        // 大于 100 个数相加切分,小于直接加
        if(end - start <= 100){
            for (int i = start; i <= end; i++) {
                // 累加
                sum += i;
            }
        }else {
            // 切分为 2 块
            int middle = start + 100;
            // 递归调用,切分为 2 个小任务
            TaskExample taskExample1 = new TaskExample(start, middle);
            TaskExample taskExample2 = new TaskExample(middle + 1, end);
            // 执行---->异步
            taskExample1.fork();
            taskExample2.fork();
            // 获取执行结果---->同步阻塞
            sum = taskExample1.join() + taskExample2.join();
        }
        // 加完返回
        return sum;
    }
}


public static void main(String[] args) {
    // 定义任务
    TaskExample taskExample = new TaskExample(1, 1000);
    // 定义执行对象
    ForkJoinPool forkJoinPool = new ForkJoinPool();
    // 加入任务执行
    ForkJoinTask<Long> result = forkJoinPool.submit(taskExample);
    // 输出结果
    try {
        System.out.println(result.get());
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        forkJoinPool.shutdown();
    }
}
~~~



## 12、CompletableFuture

### 1、基本概念

CompletableFuture 被用于异步编程，异步通常意味着非阻塞， 可以使得任务单独运行在与主线程分离的其他线程中，并且通过**回调**可以在主线程中得到异步任务的执行状态，是否完成，和是否异常等信息。 

CompletableFuture 实现了 Future, CompletionStage 接口，实现了 Future 接口就可以兼容现在有线程池框架，而 CompletionStage 接口才是异步编程的接口抽象，里面定义多种异步方法。

一个 CompletableFuture 就代表了一个任务。

**注意**：

- CompletableFuture的大部分方法都有带和不带Async后缀的，带Async代表异步方法，可以指定一个线程池，作为任务的运行环境。
- 如果没有指定就会使用默认ForkJoinPool线程池来执行。
- 如果机器是单核的，则默认使用ThreadPerTaskExecutor，该类是一个内部类，每次执行execute都会创建一个新线程。

**注意**：

CompletableFuture 所创建的线程都是守护线程，也就是创建完后，如果没有用户线程，此时全部都是守护线程，等main线程结束，程序退出，也就会导致CompletableFuture线程中断，避免的方法可以调用get或者使用回调

### 2、基本使用

主线程里面创建一个 CompletableFuture，然后主线程**调用 get 方法会阻塞**，最后我们在一个子线程中使其终止。

~~~java
public static void main(String[] args) throws Exception {
    
    CompletableFuture<String> future = new CompletableFuture<>();
    
    new Thread(() -> {
        try {
            System.out.println(Thread.currentThread().getName() + "子线程开始干活");
            // 子线程睡 5 秒
            Thread.sleep(5000);
            // 在子线程中完成主线程
            future.complete("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }, "A").start();
    
    // 主线程调用 get 方法阻塞
    System.out.println("主线程调用 get 方法获取结果为  " + future.get());
    System.out.println("主线程完成,阻塞结束!!!!!!");
}
~~~

#### runAsync

没有返回值的异步任务

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    // 运行一个没有返回值的异步任务
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        try {
            System.out.println("子线程启动干活");
            Thread.sleep(5000);
            System.out.println("子线程完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
    
    // 主线程阻塞
    future.get();
    System.out.println("主线程结束");
}
~~~

#### supplyAsync

有返回值的异步任务

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    // 运行一个有返回值的异步任务
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("子线程开始任务");
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "子线程完成了!";
        });
    
    // 主线程阻塞
    String s = future.get();
    System.out.println("主线程结束, 子线程的结果为 " + s);
    
    // 创建线程池
    ExecutorService executorService = Executors.newCachedThreadPool();
    // 指定线程池运行任务
    CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("this is task with executor");
        return "result2";
    }, executorService);
}
~~~





### 3、线程依赖

#### thenApply

当一个线程依赖另一个线程时，可以使用 **thenApply** 方法来把这两个线程**串行化**。

~~~java
private static Integer num = 10;

public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("加 10 任务开始");
                num += 10;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return num;
        // 串行
        }).thenApply(integer -> {
        return num * num;
    });
    
    Integer integer = future.get();
    System.out.println("主线程结束, 子线程的结果为 " + integer);
}
~~~

### 4、消费处理结果

#### thenAccept

**thenAccept** 消费处理结果，接收任务的处理结果，并消费处理，无返回结果。

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");

    CompletableFuture.supplyAsync(() -> {
        try {
            System.out.println("加 10 任务开始");
            num += 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
        // 串行
    }).thenApply(integer -> {
        return num * num;
        // 消费结果
    }).thenAccept(new Consumer<Integer>() {
        @Override
        public void accept(Integer integer) {
            System.out.println("子线程全部处理完成,最后调用了 accept,结果为 " + integer);
        }
    });
}
~~~

#### whenComplete

当某个任务执行完成后执行的回调方法，会将执行结果或者执行期间抛出的异常传递给回调方法。

如果是正常执行则异常为null，回调方法对应的CompletableFuture的result和该任务一致。

如果该任务正常执行，则get方法返回执行结果，如果是执行异常，则get方法抛出异常。

~~~java
// 创建异步执行任务 
CompletableFuture<Double> cf = CompletableFuture.supplyAsync(()->{
    System.out.println(Thread.currentThread()+"job1 start,time->"+System.currentTimeMillis());
    try {
        Thread.sleep(600);
    } catch (InterruptedException e) {
    }
    if(false){
        throw new RuntimeException("test");
    }else{
        System.out.println(Thread.currentThread()+"job1 exit,time->"+System.currentTimeMillis());
        return 1.2;
    }
});

        // System.out.println("主线程睡眠");
        // Thread.sleep(400);

// cf执行完成后会将执行结果和执行过程中抛出的异常传入回调方法，如果是正常执行的则传入的异常为null
CompletableFuture<Double> cf2=cf.whenComplete((a,b)->{
    System.out.println(Thread.currentThread()+"job2 start,time->"+System.currentTimeMillis());
    try {
        Thread.sleep(600);
    } catch (InterruptedException e) {
    }
    if(b!=null){
        System.out.println("error stack trace->");
        b.printStackTrace();
    }else{
        System.out.println("run succ,result->"+a);
    }
    System.out.println(Thread.currentThread()+"job2 exit,time->"+System.currentTimeMillis());
});

//等待子任务执行完成
System.out.println("main thread start wait,time->"+System.currentTimeMillis());
//如果cf是正常执行的，cf2.get的结果就是cf执行的结果
//如果cf是执行异常，则cf2.get会抛出异常
System.out.println("run result->"+cf2.get());
System.out.println("main thread exit,time->"+System.currentTimeMillis());
~~~

**题外话**：

当whenComplete之前如果出现了其他事情，阻塞了，例如main线程sleep，分俩种情况：

- 如果上一个CompletableFuture执行完毕了，那么就使用主线程调用whenComplete方法。
- 如果上一个CompletableFuture还没执行完毕，使用的线程还是上一个CompletableFuture的。

（可以打开，上图的sleep代码测试）

**注意**：此种情况在then开头的方法中，不会出现

### 5、异常处理

**exceptionally** 异常处理，出现异常时触发。

**handle** 类似于 thenAccept/thenRun 方法，是最后一步的处理调用，但是同时可以处理异常。

#### exceptionally

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        int i= 1/0;
        System.out.println("加 10 任务开始");
        num += 10;
        return num;
        // 处理异常
    }).exceptionally(ex -> {
        System.out.println(ex.getMessage());
        return -1;
    });
    System.out.println(future.get());
}
~~~

#### handle

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
        System.out.println("加 10 任务开始");
        num += 10;
        return num;
        // 最后一步
    }).handle((i,ex) ->{
        System.out.println("进入 handle 方法");
        if(ex != null){
            System.out.println("发生了异常,内容为 " + ex.getMessage());
            return -1;
        }else{
            System.out.println("正常完成,内容为  " + i);
            return i;
        }
    });
    System.out.println(future.get());
}

~~~

### 6、结果合并

**thenCompose** 合并两个有依赖关系的 CompletableFutures 的执行结果。

**thenCombine** 合并两个没有依赖关系的 CompletableFutures 任务。

**allOf** 与 **anyOf** 合并多个任务的结果，

#### thenCompose

在某个任务执行完成后，将该任务的执行结果作为方法入参然后执行指定的方法，该方法会返回一个新的CompletableFuture实例。

如果上一个CompletableFuture实例的result不为null，则返回一个基于该result的新CompletableFuture实例。

如果上一个CompletableFuture实例的result为null，则执行任务时抛出异常

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    // 第一个CompletableFuture
    // 加10
    CompletableFuture<Integer> future0 = CompletableFuture.supplyAsync(() -> {
        System.out.println("加 10 任务开始");
        num += 10;
        return num;
    });
    
    // 合并第一个CompletableFuture
    // 再来一个CompletableFuture
    CompletableFuture<Integer> future1 = future0.thenCompose(result -> CompletableFuture.supplyAsync(() -> {
        return result+ 1;
    }));
    System.out.println(future0.get());
    System.out.println(future1.get());
}
~~~

#### thenCombine/thenAcceptBoth/runAfterBoth

类似还有

这三个方法都是将两个CompletableFuture组合起来，只有这**两个都正常执行完了才会执行某个任务**。

区别在于：

- thenCombine 会将两个任务的执行结果作为方法入参传递到指定方法中，且该方法有返回值。

- thenAcceptBoth 同样将两个任务的执行结果作为方法入参，但是无返回值。
- runAfterBoth 没有入参，也没有返回值。

注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    // 任务一
    CompletableFuture<Integer> job1 = CompletableFuture.supplyAsync(() -> {
        System.out.println("加 10 任务开始");
        num += 10;
        return num;
    });
    
    // 任务二
    CompletableFuture<Integer> job2 = CompletableFuture.supplyAsync(() -> {
        System.out.println("乘以 10 任务开始");
        num = num * 10;
        return num;
    });
    
    // 合并两个结果
    CompletableFuture<Object> future = job1.thenCombine(job2, 
                                                        new BiFunction<Integer, Integer, List<Integer>>() {
        @Override
        public List<Integer> apply(Integer a, Integer b) {
            List<Integer> list = new ArrayList<>();
            list.add(a);
            list.add(b);
            return list;
        }
    });
    System.out.println("合并结果为 " + future.get());
    
    CompletableFuture cf4=cf.thenAcceptBoth(cf2, (a,b)->{
        System.out.println(Thread.currentThread()+" start job4,time->"+System.currentTimeMillis());
        System.out.println("job4 param a->"+a+",b->"+b);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
        }
        System.out.println(Thread.currentThread()+" exit job4,time->"+System.currentTimeMillis());
    });

    CompletableFuture cf4=cf.runAfterBoth(cf2, ()->{
        System.out.println(Thread.currentThread()+" start job5,time->"+System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        System.out.println("cf5 do something");
        System.out.println(Thread.currentThread()+" exit job5,time->"+System.currentTimeMillis());
    });

}
~~~

#### applyToEither/acceptEither/runAfterEither

这三个方法都是将两个CompletableFuture组合起来，只要**其中一个执行完了就会执行某个任务**。

其区别在于：

- applyToEither 会将已经执行完成的任务的执行结果作为方法入参，并有返回值。
- acceptEither 同样将已经执行完成的任务的执行结果作为方法入参，但是没有返回值。
- runAfterEither没有方法入参，也没有返回值。

注意两个任务中只要有一个执行异常，则将该异常信息作为指定任务的执行结果。

~~~java
// 创建异步执行任务 
CompletableFuture<Double> cf1 = CompletableFuture.supplyAsync(()->{
    System.out.println(Thread.currentThread()+" start job1,time->"+System.currentTimeMillis());
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
    }
    System.out.println(Thread.currentThread()+" exit job1,time->"+System.currentTimeMillis());
    return 1.2;
});

CompletableFuture<Double> cf2 = CompletableFuture.supplyAsync(()->{
    System.out.println(Thread.currentThread()+" start job2,time->"+System.currentTimeMillis());
    try {
        Thread.sleep(1500);
    } catch (InterruptedException e) {
    }
    System.out.println(Thread.currentThread()+" exit job2,time->"+System.currentTimeMillis());
    return 3.2;
});

//cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf3, 且有返回值
CompletableFuture<Double> cf3=cf1.applyToEither(cf2, (result)->{
    System.out.println(Thread.currentThread()+" start job3,time->"+System.currentTimeMillis());
    System.out.println("job3 param result->"+result);
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
    }
    System.out.println(Thread.currentThread()+" exit job3,time->"+System.currentTimeMillis());
    return result;
});

//cf1和cf2的异步任务都执行完成后，会将其执行结果作为方法入参传递给cf4,无返回值
CompletableFuture cf4=cf1.acceptEither(cf2,(result)->{
    System.out.println(Thread.currentThread()+" start job4,time->"+System.currentTimeMillis());
    System.out.println("job4 param result->"+result);
    try {
        Thread.sleep(1500);
    } catch (InterruptedException e) {
    }
    System.out.println(Thread.currentThread()+" exit job4,time->"+System.currentTimeMillis());
});

//cf4和cf3都执行完成后，执行cf5，无入参，无返回值
CompletableFuture cf5=cf4.runAfterEither(cf3,()->{
    System.out.println(Thread.currentThread()+" start job5,time->"+System.currentTimeMillis());
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    System.out.println("cf5 do something");
    System.out.println(Thread.currentThread()+" exit job5,time->"+System.currentTimeMillis());
});

System.out.println("main thread start cf.get(),time->"+System.currentTimeMillis());
//等待子任务执行完成
System.out.println("cf run result->"+cf.get());
System.out.println("main thread start cf5.get(),time->"+System.currentTimeMillis());
System.out.println("cf5 run result->"+cf5.get());
System.out.println("main thread exit,time->"+System.currentTimeMillis());
~~~



#### allOf

返回的CompletableFuture是**多个任务都执行完成后才会执行**，只要有一个任务执行异常，则返回的CompletableFuture执行get方法时会抛出异常，如果都是正常执行，则get返回null。

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    
    List<CompletableFuture> list = new ArrayList<>();
    
    CompletableFuture<Integer> job1 = CompletableFuture.supplyAsync(() -> {
        System.out.println("加 10 任务开始");
        num += 10;
        return num;
    });
    list.add(job1);
    
    CompletableFuture<Integer> job2 = CompletableFuture.supplyAsync(() -> {
        System.out.println("乘以 10 任务开始");
        num = num * 10;
        return num;
    });
    list.add(job2);
    
    CompletableFuture<Integer> job3 = CompletableFuture.supplyAsync(() -> {
        System.out.println("减以 10 任务开始");
        num = num * 10;
        return num;
    });
    list.add(job3);
    
    CompletableFuture<Integer> job4 = CompletableFuture.supplyAsync(() -> {
        System.out.println("除以 10 任务开始");
        num = num * 10;
        return num;
    });
    list.add(job4);
    
    // 多任务合并
    CompletableFuture<Void> cf4 = CompletableFuture.allOf(
        list.toArray(new CompletableFuture[0])).whenComplete((a, b)->{
        if(b != null){
            System.out.println("error stack trace->");
            b.printStackTrace();
        }else{
            System.out.println("run succ,result->"+a);
        }
    });

    // 多任务合并
    List<Integer> collect = list.stream()
        .map(CompletableFuture  join)
        .collect(Collectors.toList());

    System.out.println(collect);
    System.out.println("cf4 run result->"+cf4.get());
}
~~~

#### anyOf

返回的CompletableFuture是**多个任务只要其中一个执行完成就会执行**，其get返回的是已经执行完成的任务的执行结果，如果该任务执行异常，则抛出异常。

~~~java
public static void main(String[] args) throws Exception{
    System.out.println("主线程开始");
    CompletableFuture<Integer>[] futures = new CompletableFuture[4];
    
    CompletableFuture<Integer> job1 = CompletableFuture.supplyAsync(() -> {
        try{
            Thread.sleep(5000);
            System.out.println("加 10 任务开始");
            num += 10;
            return num;
        }catch (Exception e){
            return 0;
        }
    });
    futures[0] = job1;
    
    CompletableFuture<Integer> job2 = CompletableFuture.supplyAsync(() -> {
        try{
            Thread.sleep(2000);
            System.out.println("乘以 10 任务开始");
            num = num * 10;
            return num;
        }catch (Exception e){
            return 1;
        }
    });
    futures[1] = job2;
    
    CompletableFuture<Integer> job3 = CompletableFuture.supplyAsync(() -> {
        try{
            Thread.sleep(3000);
            System.out.println("减以 10 任务开始");
            num = num * 10;
            return num;
        }catch (Exception e){
            return 2;
        }
    });
    futures[2] = job3;
    
    CompletableFuture<Integer> job4 = CompletableFuture.supplyAsync(() -> {
        try{
            Thread.sleep(4000);
            System.out.println("除以 10 任务开始");
            num = num * 10;
            return num;
        }catch (Exception e){
            return 3;
        }
    });
    futures[3] = job4;
    
    // 多任务合并
    CompletableFuture<Object> cf4 = CompletableFuture.anyOf(futures).whenComplete((a, b)->{
        if(b != null){
            System.out.println("error stack trace->");
            b.printStackTrace();
        }else{
            System.out.println("run succ,result->"+a);
        }
    });
    
    CompletableFuture<Object> future = CompletableFuture.anyOf(futures);
    System.out.println(future.get());
    System.out.println("cf4 run result->"+cf4.get());
}
~~~



# 问题

## 1、Integer.valueOf()和Integer.parseInt()的区别

- Integer.parseInt(String s)将会返回int常量。
- Integer.valueOf(String s)将会返回Integer类型，如果存在缓存将会返回缓存中已有的对象。

Integer会缓存 -128 ~ 127 范围的整型数字

## 2、Integer i1 = 100 与 Integer i2 = 200 有何不一样

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

## 3、何为OOM

OOM，全称“Out Of Memory”

按照JVM规范，JAVA虚拟机在运行时会管理以下的内存区域：

- 程序计数器：当前线程执行的字节码的行号指示器，线程私有
- JAVA虚拟机栈：Java方法执行的内存模型，每个Java方法的执行对应着一个栈帧的进栈和出栈的操作。
- 本地方法栈：类似“ JAVA虚拟机栈 ”，但是为native方法的运行提供内存环境。
- JAVA堆：对象内存分配的地方，内存垃圾回收的主要区域，所有线程共享。可分为新生代，老生代。
- 方法区：用于存储已经被JVM加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。Hotspot中的“永久代”。
- 运行时常量池：方法区的一部分，存储常量信息，如各种字面量、符号引用等。
- 直接内存：并不是JVM运行时数据区的一部分， 可直接访问的内存， 比如NIO会用到这部分。

按照JVM规范，除了程序计数器不会抛出OOM外，其他各个内存区域都可能会抛出OOM。

## 4、wait()为什么要处于while循环中

当多个线程并发访问同一个资源的时候，若消费者同时被唤醒，但是只有一个资源可用，那么如果用 if 去判断竞态条件，会导致在资源被用完后，还有线程直接去获取资源(发生越界异常等)，而while则会让每个消费者获取之前再去判断一下资源是否可用，可用则获取，不可用则继续wait。

程序应该循环检测线程被唤醒的条件，并在不满住条件时通知继续等待，防止虚假唤醒。

## 5、instanceof, isinstance,isAssignableFrom的区别

1、instanceof

instanceof运算符 只被用于**对象引用变量**，检查左边的被测试对象 是不是 右边类或接口的 实例化。

如果被测对象是null值，则测试结果总是false。

形象地：自身实例或子类实例 instanceof 自身类 返回true

```java
String s=new String("javaisland");

System.out.println(s instanceof String); //true
```

2、isInstance(Object obj)

Class类的isInstance(Object obj)方法，obj是被测试的对象如果obj是调用这个方法的class或接口 的实例，则返回true。这个方法是instanceof运算符的动态等价。

形象地：自身类.class.isInstance(自身实例或子类实例) 返回true

```java
String s=new String("javaisland");
System.out.println(String.class.isInstance(s)); //true
```

3、isAssignableFrom(Class cls)

Class类的isAssignableFrom(Class cls)方法，如果调用这个方法的class 或接口 与 参数cls表示的类或接口相同，或者是参数cls表示的类或接口的父类，则返回true。

形象地：自身类.class.isAssignableFrom(自身类或子类.class) 返回true

```java
System.out.println(ArrayList.class.isAssignableFrom(Object.class)); //false
System.out.println(Object.class.isAssignableFrom(ArrayList.class)); //true
```

## 6、ArrayList的JDK1.8之前与之后的实现区别

JDK1.7：ArrayList像饿汉式，直接创建一个初始容量为10的数组。

JDK1.8：ArrayList像懒汉式，一开始创建一个长度为0的数组，当添加第一个元 素时再创建一个始容量为10的数组。

## 7、ArrayList/LinkedList/Vector的异同

首先是ArrayList和LinkedList的异同：

- 二者都线程不安全，相对线程安全的Vector，执行效率高。 此外，ArrayList是实现了基于动态数组的数据结构，LinkedList基于链表的数据结构。
- 对于随机访问get和set，ArrayList觉得优于LinkedList，因为LinkedList要移动指针。
- 对于新增和删除操作add(特指插入)和remove，LinkedList比较占优势，因为ArrayList要移动数据。

然后是ArrayList和Vector的区别：

- Vector和ArrayList几乎是完全相同的，唯一的区别在于Vector是同步类(synchronized)，属于强同步类。因此开销就比ArrayList要大，访问要慢。
- Vector每次扩容请求其大小的2倍空间，而ArrayList是1.5倍。Vector还有一个子类Stack。

## 8、ArrayList扩容机制

先看ArrayList的两个重要的成员变量：

- ```java
  // 用于空实例的共享空数组实例。
  private static final Object[] EMPTY_ELEMENTDATA = {};
  ```

- ```java
  // 是用来使用默认构造方法时候返回的空数组，如果第一次添加数据的话那么数组扩容长度为DEFAULT_CAPACITY=10
  // 或者有指定长度的画，就是用指定长度创建
  private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
  ```

~~~java
public ArrayList(int initialCapacity) {
    // 如果初始化长度大于0，就使用初始化长度创建数组
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        // 否者使用默认 空实例共享对象
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}
~~~

从这个构造方法可以看出，如果有设置默认大小，则数组长度设置为默认大小，如果没有，则返回一个空的共享空数组实例

而另一个设置容量的方法ensureCapacity，可以在创建数组后设置大小，调用扩容方法grow进行扩容

~~~java
public void ensureCapacity(int minCapacity) {
    if (minCapacity > elementData.length
        && !(elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
             && minCapacity <= DEFAULT_CAPACITY)) {
        modCount++;
        grow(minCapacity);
    }
}
~~~

核心的grow方法，解析如下：

```java
private Object[] grow(int minCapacity) {
    // 先获取老数组长度
    int oldCapacity = elementData.length;
    // 老数组长度大于0 或者 数组不是默认空共享实例
    if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        // 计算新数组长度需要多少
        // 关于newLength
        int newCapacity = ArraysSupport.newLength(oldCapacity,
                                                  // 最小增长量，一般是当前siez + 1
                                                  minCapacity - oldCapacity, /* minimum growth */
                                                  // 位移运算，计算首选增长量，等同于 oldCapacity/2
                                                  oldCapacity >> 1           /* preferred growth */);
        // 将老数组复制到新数组
        return elementData = Arrays.copyOf(elementData, newCapacity);
    } else {
        return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
    }
}


// 软最大增长量
public static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
    // preconditions not checked because of inlining
    // assert oldLength >= 0
    // assert minGrowth > 0
    // 将老数组长度加上最小增长量于首选增长量中较大值
    int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
    // 一般来说首选增长量 > 最小增长量，那么新组数长度就会等于 老数组长度 * 1.5 ----》 有溢出可能，看下方判断
    // 如果新数组长度没有溢出并且小于软最大增长量，则扩容1.5倍
    if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
        return prefLength;
    } else {
        // 如果超出的上方的条件，则使用最小增长量
        // put code cold in a separate method
        return hugeLength(oldLength, minGrowth);
    }
}

private static int hugeLength(int oldLength, int minGrowth) {
    // 有溢出可能，看下方判断
    int minLength = oldLength + minGrowth;
    // 如果溢出了，直接抛出异常
    if (minLength < 0) { // overflow
        throw new OutOfMemoryError(
            "Required array length " + oldLength + " + " + minGrowth + " is too large");
        // 没有溢出，并且小于等于软最大增长量，则直接使用软最大增长量，因为考虑未来可能会要再次添加
    } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
        return SOFT_MAX_ARRAY_LENGTH;
    } else {
        // 如果最小增长量大于软最大增长量，并且没有溢出，则直接使用
        return minLength;
    }
}
```

由此看出ArrayList为什么是增长1.5倍。

## 9、用Eclipse/IDEA复写hashCode方法，有31这个数字

- 选择系数的时候要选择尽量大的系数，因为如果计算出来的hash地址越大，所谓的 “冲突”就越少，查找起来效率也会提高。（减少冲突）

- 31只占用5bits，相乘造成数据溢出的概率较小。

- 31可以 由 i*31== (i<<5)-1来表示，现在很多虚拟机里面都有做相关优化。（提高算法效率）

- 31是一个素数，素数作用就是如果我用一个数字来乘以这个素数，那么最终出来的结果只能被素数本身和被乘数还有1来整除(减少冲突)

## 10、HashMap在JDK1.8之前与之后的实现区别

**JDK 1.8之前**

**储存结构**：

HashMap的内部存储结构其实是**数组**和**链表**的结合。当实例化一个HashMap时， 系统会创建一个长度为**Capacity**的**Entry数组**，这个长度在哈希表中被称为**容量** (Capacity)，在这个数组中可以**存放元素的位置**我们称之为桶(bucket)，**每个bucket都有自己的索引**，系统可以根据索引快速的查找bucket中的元素。

每个bucket中存储一个元素，即一个Entry对象，但每一个Entry对象可以带一个引用变量，用于指向下一个元素，因此，在一个桶中，就有可能生成一个Entry链，而且新添加的元素作为链表的head。

**添加元素**：

向HashMap中添加entry(key，value)，需要首先**计算entry中key的哈希值**(根据key所在类的hashCode()计算得到)，此哈希值经过处理以后，**得到在底层Entry[]数组中要存储的位置i**。------》 也即，通过哈希函数计算key要放在哪个桶里。

如果位置i上没有元素，则entry直接添加成功。如果位置i上已经存在其他entry(或有entry链表存在的)，则需要通过循环的方法，依次比较要添加的entry中key和其他的entry的key的hashCode。------》找到桶后，需要比较是否已经存在这个entry。

如果彼此hash值不同，则直接添加成功。如果hash值不同，继续比较二者的equals()方法。如果返回值为true，则使用新添加entry的value去替换equals()为true的entry的value。-----》如果桶中没有相同的entry，直接放入，相同的entry，替换value。

如果遍历一遍以后，发现所有的equals返回都为false，则entry仍可添加成功。

新添加的entry指向原有的entry元素。------》放在头部，头插。

**扩容**：

> 当HashMap中的元素越来越多的时候，hash冲突的几率也就越来越高，因为数组的长度是固定的。所以为了提高查询的效率，就要对HashMap的数组进行扩容，而在HashMap数组扩容之后，最消耗性能的点就出现了：原数组中的数据必须重新计算其在新数组中的位置，并放进去，这就是resize。

当HashMap中的 **元素个数** 超过 **数组大小 * loadFactor** 时就会进行数组扩容。注意：(数组大小指的是length，不是数组中个数 size)  

loadFactor 的默认值 (DEFAULT_LOAD_FACTOR)为**0.75**，这是一个折中的取值。

数组大小 的默认值(DEFAULT_INITIAL_CAPACITY)为**16**。

那么当HashMap中元素个数超过16*0.75=12（这个值就是代码中的threshold值，也叫做临界值）的时候，就把数组的大小扩展为 2*16=32，即扩大一倍，然后重新计算每个元素在数组中的位置， 而这是一个非常消耗性能的操作，所以如果我们已经预知HashMap中元素的个数， 那么预设元素的个数能够有效的提高HashMap的性能。

**JDK 1.8**

**储存结构**：

HashMap的内部存储结构其实是**数组**+**链表**+**树**的结合。当实例化一个HashMap时，会初始化**initialCapacity**和loadFactor，**在put第一对映射关系时**，系统会创建一个长度为initialCapacity的**Node数组**，（也就是说不会一开始就创建一个16长度的数组了），这个长度在哈希表中被称为**容量**(Capacity)，在这个数组中可以存放元素的位置我们称之为桶(bucket)，每个bucket都有自己的索引，系统可以根据索引快速的查找bucket中的元素。

每个bucket中存储一个元素，可能是一个**Node对象**，并且每一个Node对象可以带一个引用变量next，用于指向下一个元素，因此，在一个桶中，就有可能生成一个Node链表。

每个bucket中存储的可能是一个一个**TreeNode对象**，每一个TreeNode对象有两个叶子结点left和right，因此，在一个桶中，就有可能生成一个TreeNode树。而新添加的元素作为链表的last，或树的叶子结点。尾插。

**扩容**：

当HashMap中的其中一个链的对象个数如果达到了**8**个，此时如果**Capacity**没有达到**64**，那么HashMap会先扩容解决，如果已经达到了64，那么这个链会变成树，结点类型由Node变成TreeNode类型。当然，如果当映射关系被移除后， 下次resize时判断树的结点个数低于**6**个，也会把树再转为链表。调用的方法为**treeifyBin**

~~~java
// 将桶内所有的 链表节点 替换成 红黑树节点
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
    // 如果当前哈希表为空，或者哈希表中元素的个数小于 进行树形化的阈值(默认为 64)，就去新建/扩容
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
        resize();
    else if ((e = tab[index = (n - 1) & hash]) != null) {
        // 如果哈希表中的元素个数超过了 树形化阈值，进行树形化
        // e 是哈希表中指定位置桶里的链表节点，从第一个开始
        TreeNode<K,V> hd = null, tl = null; // 红黑树的头、尾节点
        do {
            // 新建一个树形节点，内容和当前链表节点 e 一致
            TreeNode<K,V> p = replacementTreeNode(e, null);
            if (tl == null) // 确定树头节点
                hd = p;
            else {
                p.prev = tl;
                tl.next = p;
            }
            tl = p;
        } while ((e = e.next) != null);
        // 让桶的第一个元素指向新建的红黑树头结点，以后这个桶里的元素就是红黑树而不是链表了
        if ((tab[index] = hd) != null)
            hd.treeify(tab);
    }
}
TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next) {
    return new TreeNode<>(p.hash, p.key, p.value, next);
}
~~~

1. 根据哈希表中元素个数确定是扩容还是树形化
2. 如果是树形化遍历桶中的元素，创建相同个数的树形节点，复制内容，建立起联系
3. 然后让桶第一个元素指向新建的树头结点，替换桶的链表内容为树形内容

## 11、HashMap映射关系的key是否可修改

**不要修改**，如果修改了key，会导致hashCode变化，最后匹配不上。

映射关系存储到HashMap中会存储key的hash值，这样就不用在每次查找时重新计算每一个Entry或Node（TreeNode）的hash值了，因此如果已经put到Map中的映射关系，再修改key的属性，而这个属性又参与hashcode值的计算，那么会导致匹配不上。

## 12、负载因子值的大小，对HashMap有什么影响

负载因子的大小决定了HashMap的数据密度。

负载因子越大，密度越大，发生碰撞的几率越高，数组中的链表容易变长，造成查询或插入时的比较次数增多，性能会下降。

负载因子越小，就越容易触发扩容，数据密度也越小，意味着发生碰撞的几率越小，数组中的链表也就越短，查询和插入时比较的次数也越小，性能会更高。但是会浪费一定的内容空间。而且经常扩容也会影响性能，建议初始化预设大一点的空间。

按照其他语言的参考及研究经验，会考虑将负载因子设置为0.7~0.75，此时平均检索长度接近于常数。

## 13、HashMap的put方法

![image-20220401173631154](images/image-20220401173631154.png)

1. 判断键值对数组 table[i]是否为空或为 null，否则执行 resize() 进行扩容。
2. 根据键值 key 计算 hash 值得到插入的数组索引 i，如果 table[i]==null，直接新建节点添加， 转向6，如果 table[i]不为空，转向3。
3. 判断 table[i] 的首个元素是否和 key 一样，如果相同直接覆盖 value，否则转向4，这里的相同指的是 hashCode 以及 equals。 
4. 判断 table[i] 是否为 treeNode，即 table[i] 是否是红黑树，如果是红黑树，则直接在树中插入键值对，否则转向5。
5. 遍历 table[i]，判断链表长度是否大于 8，大于 8 的话把链表转换为红黑树，在红黑树中执行插入操作，否则进行链表的插入操作，遍历过程中若发现 key 已经存在直接覆盖 value 即可。
6. 插入成功后，判断实际存在的键值对数量 size 是否超多了最大容量 threshold，如果超过， 进行扩容。

## 14、HashMap的get方法

1. HashMap 的查找方法是 get()，它通过计算指定 key 的哈希值后，调用内部方法 getNode()。
2.  这个 getNode() 方法就是根据哈希表元素个数与哈希值求模（使用的公式是 (n - 1)  &hash）得到 key 所在的桶的头结点，如果头节点恰好是红黑树节点， 就调用红黑树节点的 getTreeNode() 方法，否则就遍历链表节点。
3.  getTreeNode 方法使通过调用树形节点的 find()方法进行查找：
   1. final TreeNode getTreeNode(int h, Object k) { return ((parent != null) ? root() : this).find(h, k, null); } 
   2. 由于之前添加时已经保证这个树是有序的，因此查找时基本就是折半查找，效率很高。
4. 这里和插入时一样，如果对比节点的哈希值和要查找的哈希值相等，就会判断 key 是否相 等，相等就直接返回，不相等就从子树中递归查找。














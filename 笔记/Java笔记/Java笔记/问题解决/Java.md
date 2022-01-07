# instanceof, isinstance,isAssignableFrom的区别

//获得监听器集合，遍历监听器，可支持同步和异步的广播事件

## 1、instanceof

instanceof运算符 只被用于**对象引用变量**

检查左边的被测试对象 是不是 右边类或接口的 实例化。

如果被测对象是null值，则测试结果总是false。

形象地：自身实例或子类实例 instanceof 自身类 返回true

```java
String s=new String("javaisland");

System.out.println(s instanceof String); //true
```

## 2、isInstance(Object obj)

Class类的isInstance(Object obj)方法，obj是被测试的对象

如果obj是调用这个方法的class或接口 的实例，则返回true。

这个方法是instanceof运算符的动态等价。

形象地：自身类.class.isInstance(自身实例或子类实例) 返回true

```java
String s=new String("javaisland");
System.out.println(String.class.isInstance(s)); //true
```

## 3、isAssignableFrom(Class cls)

Class类的isAssignableFrom(Class cls)方法，如果调用这个方法的class

或接口 与 参数cls表示的类或接口相同，或者是参数cls表示的类或接口的

父类，则返回true。

形象地：自身类.class.isAssignableFrom(自身类或子类.class) 返回true

```java
System.out.println(ArrayList.class.isAssignableFrom(Object.class)); //false
System.out.println(Object.class.isAssignableFrom(ArrayList.class)); //true
```


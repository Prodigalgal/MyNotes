# ApplicationEvent事件机制源码分析

## 1、三点重要概念

### 1、事件源：

​	事件对象的产生者，任何一个EventObject都有一个来源



### 2、事件监听器注册表：

​	当事件框架或组件收到一个事件后，需要通知所有相关的事件监听器来进行处理，这个时候就需要有个存储监听器的地方，也就是事件监听器注册表。事件源与事件监听器关联关系的存储。



### 3、事件广播器：

​	事件广播器在整个事件机制中扮演一个中介的角色，当事件发布者发布一个事件后，就需要通过广播器来通知所有相关的监听器对该事件进行处理。

![image-20210830232136946](images\ApplicationEvent事件机制源码分析.assets\image-20210830232136946.png)



## 2、Spring中的监听器模式

### 1、三个主要角色

Spring在事件处理机制中使用了监听器模式，其中有三个主要角色



#### 1、事件，ApplicationEvent

- 该抽象类继承了EventObject

- EventObject是JDK中的类，并建议所有的事件都应该继承自EventObject



#### 2、事件监听器，ApplicationListener

- 一个接口，该接口继承了EventListener接口。

- EventListener接口是JDK中的，并建议所有的事件监听器都应该继承EventListener。

- 监听器是用于接收事件，并触发事件的操作，简单的说就是，Listener是监听ApplicationContext.publishEvent()方法的调用，一旦调用publishEvent，就会执行ApplicaitonListener中的方法，下面这个是ApplicationContext的源码。

- ```java
  public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
  
      /**
       * publishEvent触发该方方法
       * 可以在该方法中写各种业务逻辑
       */
      void onApplicationEvent(E event);
  
  }
  ```



#### 3、事件发布，ApplicationEventPublisher

- ApplicationContext继承了该接口，在ApplicationContext的抽象实现类AbstractApplicationContext中做了实现。



## 3、Spring事件发布机制

事件机制如下图，具体的实现采用观察者模式

<img src="images\ApplicationEvent事件机制源码分析.assets\image-20210830233029587.png" alt="image-20210830233029587" style="zoom:200%;" />



## 4、Spring事件异步机制流程



### 1、ApplicationEventPublisher

**ApplicationEventPublisher**是Spring的事件发布接口，事件源通过该接口的pulishEvent方法发布事件。



### 2、ApplicationEventMulticaster

**ApplicationEventMulticaster**就是Spring事件机制中的事件广播器，它默认提供一个SimpleApplicationEventMulticaster实现，如果用户没有自定义广播器，则使用默认的。

它通过父类AbstractApplicationEventMulticaster的getApplicationListeners方法从事件注册表（事件-监听器关系保存）中获取事件监听器，并且通过invokeListener方法执行监听器的具体逻辑。



### 3、ApplicationListener

**ApplicationListener**就是Spring的事件监听器接口，所有的监听器都实现该接口，本图中列出了典型的几个子类。其中RestartApplicationListnener在SpringBoot的启动框架中就有使用。



### 4、ApplicationContext

在Spring中通常是**ApplicationContext**本身担任监听器注册表的角色，在其子类AbstractApplicationContext中就聚合了**事件广播器**ApplicationEventMulticaster和**事件监听器**ApplicationListnener，并且提供注册监听器的addApplicationListener方法。



## 5、Spring中观察者模式的四个角色

### 1、事件

ApplicationEvent 是所有事件对象的父类。ApplicationEvent 继承自 jdk 的 EventObject, 所有的事件都需要继承 ApplicationEvent, 并且通过 source 得到事件源。

Spring 也为我们提供了很多内置事件，`ContextRefreshedEvent`、`ContextStartedEvent`、`ContextStoppedEvent`、`ContextClosedEvent`、`RequestHandledEvent`。



### 2、事件监听器

ApplicationListener，也就是观察者，继承自 jdk 的 EventListener，该类中只有一个方法 onApplicationEvent。当监听的事件发生后该方法会被执行。



### 3、事件源

ApplicationContext，`ApplicationContext` 是 Spring 中的核心容器，在事件监听中 ApplicationContext 可以作为事件的发布者，也就是事件源。因为 ApplicationContext 继承自 ApplicationEventPublisher。在 `ApplicationEventPublisher` 中定义了事件发布的方法：`publishEvent(Object event)`



### 4、事件管理

ApplicationEventMulticaster，用于事件监听器的注册和事件的广播。监听器的注册就是通过它来实现的，它的作用是把 ApplicationContext 发布的 Event 广播给它的监听器列表。 



## 6、如何根据事件找到对应的监听器

在Spring容器初始化的时候

也就是在**AbstractApplicationContext**.java在**refresh()**中

```java
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        ......
        try {
            ......
            // Initialize event multicaster for this context.
            //初始化一个事件注册表
            initApplicationEventMulticaster();
            ......
            // Check for listener beans and register them.
            //注册事件监听器
            registerListeners();

            ......
        }
    }
}
```

也就是在**AbstractApplicationContext**.java的**initApplicationEventMulticaster()**方法初始化事件注册表

```java
protected void initApplicationEventMulticaster() {
    //获得beanFactory
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    //先查找BeanFactory中是否有ApplicationEventMulticaster
    if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
        this.applicationEventMulticaster =
                beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
    }
    else {
        //如果BeanFactory中不存在，就创建一个SimpleApplicationEventMulticaster
        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
    }
}
```

在**SimpleApplicationEventMulticaster**的父类**AbstractApplicationEventMulticaster**类中有如下属性：

```java
//注册表
private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);
//注册表的缓存
private final Map<ListenerCacheKey, ListenerRetriever> retrieverCache = new ConcurrentHashMap<ListenerCacheKey, ListenerRetriever>(64);

private BeanFactory beanFactory;
```

初始化注册表之后，就会把事件注册到注册表中，**AbstractApplicationContext.registerListeners()**：

```java
protected void registerListeners() {
    //获取所有的Listener，把事件的bean放到ApplicationEventMulticaster中
    for (ApplicationListener<?> listener : getApplicationListeners()) {
        getApplicationEventMulticaster().addApplicationListener(listener);
    }
    // Do not initialize FactoryBeans here: We need to leave all regular beans
    // uninitialized to let post-processors apply to them!
    String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
    //把事件的名称放到ApplicationListenerBean里去。
    for (String lisName : listenerBeanNames) {
        getApplicationEventMulticaster().addApplicationListenerBean(lisName);
    }
}
```

Spring使用**反射机制**，通过方法getBeansOfType()获取所有继承了**ApplicationListener接口**的监听器，然后把监听器放到注册表中，所以我们可以在Spring配置文件中配置自定义监听器，在Spring初始化的时候，会把监听器自动注册到注册表中去。



### 1、getBeansOfType(Class<T> type)

**ApplicationContext**.java中的**getBeansOfType(Class<T> type)**的实现：

```java
@Override
@SuppressWarnings("unchecked")
public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
    throws BeansException {

    boolean isFactoryType = (type != null && FactoryBean.class.isAssignableFrom(type));
    Map<String, T> matches = new LinkedHashMap<String, T>();

    for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
        String beanName = entry.getKey();
        Object beanInstance = entry.getValue();
        // Is bean a FactoryBean?
        if (beanInstance instanceof FactoryBean && !isFactoryType) {
            // Match object created by FactoryBean.
            FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
            Class<?> objectType = factory.getObjectType();
            if ((includeNonSingletons || factory.isSingleton()) &&
                objectType != null && (type == null || type.isAssignableFrom(objectType))) {
                matches.put(beanName, getBean(beanName, type));
            }
        }
        else {
            if (type == null || type.isInstance(beanInstance)) {
                // If type to match is FactoryBean, return FactoryBean itself.
                // Else, return bean instance.
                if (isFactoryType) {
                    beanName = FACTORY_BEAN_PREFIX + beanName;
                }
                matches.put(beanName, (T) beanInstance);
            }
        }
    }
    return matches;
}
```



### 2、getApplicationListeners()

**ApplicationContext**发布事件可以参考上面的内容。发布事件的时候的一个方法，**getApplicationListeners()**：

```java
protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
    //获取事件类型
    Class<? extends ApplicationEvent> eventType = event.getClass();
    //或去事件源类型
    Class sourceType = event.getSource().getClass();
    ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
    //从缓存中查找ListenerRetriever
    ListenerRetriever retriever = this.retrieverCache.get(cacheKey);
    //缓存中存在，直接返回对应的Listener
    if (retriever != null) {
        return retriever.getApplicationListeners();
    }
    else {
        //缓存中不存在，就获取相应的Listener
        retriever = new ListenerRetriever(true);
        LinkedList<ApplicationListener> allListeners = new LinkedList<ApplicationListener>();
        Set<ApplicationListener> listeners;
        Set<String> listenerBeans;
        synchronized (this.defaultRetriever) {
            listeners = new LinkedHashSet<ApplicationListener>(this.defaultRetriever.applicationListeners);
            listenerBeans = new LinkedHashSet<String>(this.defaultRetriever.applicationListenerBeans);
        }
        //根据事件类型，事件源类型，获取所需要的监听事件
        for (ApplicationListener listener : listeners) {
            if (supportsEvent(listener, eventType, sourceType)) {
                retriever.applicationListeners.add(listener);
                allListeners.add(listener);
            }
        }
        if (!listenerBeans.isEmpty()) {
            BeanFactory beanFactory = getBeanFactory();
            for (String listenerBeanName : listenerBeans) {
                ApplicationListener listener = beanFactory.getBean(listenerBeanName, ApplicationListener.class);
                if (!allListeners.contains(listener) && supportsEvent(listener, eventType, sourceType)) {
                    retriever.applicationListenerBeans.add(listenerBeanName);
                    allListeners.add(listener);
                }
            }
        }
        OrderComparator.sort(allListeners);
        this.retrieverCache.put(cacheKey, retriever);
        return allListeners;
    }
}
```



### 3、supportsEvent(listener, eventType, sourceType)

根据事件类型，事件源类型获取所需要的监听器**supportsEvent(listener, eventType, sourceType)**：

```java
protected boolean supportsEvent(ApplicationListener<?> listener, ResolvableType eventType, Class<?> sourceType) {
        GenericApplicationListener smartListener = (listener instanceof GenericApplicationListener ?
                (GenericApplicationListener) listener : new GenericApplicationListenerAdapter(listener));
        return (smartListener.supportsEventType(eventType) && smartListener.supportsSourceType(sourceType));
    }
```



### 4、supportsEventType(eventType)

这里没有进行实际的处理，实际处理在**smartListener.supportsEventType(eventType)**和**smartListener.supportsSourceType(sourceType)**方法中。

smartListener.supportsEventType(eventType)：

```java
public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
    Class typeArg = GenericTypeResolver.resolveTypeArgument(this.delegate.getClass(), ApplicationListener.class);
    if (typeArg == null || typeArg.equals(ApplicationEvent.class)) {
        Class targetClass = AopUtils.getTargetClass(this.delegate);
        if (targetClass != this.delegate.getClass()) {
            typeArg = GenericTypeResolver.resolveTypeArgument(targetClass, ApplicationListener.class);
        }
    }
    return (typeArg == null || typeArg.isAssignableFrom(eventType));
}
```

该方法主要的逻辑就是根据事件类型判断是否和监听器参数泛型的类型是否一致。

**smartListener.supportsSourceType(sourceType)**方法的实现为：

```java
public boolean supportsSourceType(Class<?> sourceType) {
    return true;
}
```

定义自己的监听器要明确指定参数泛型，表明该监听器支持的事件，如果不指明具体的泛型，则没有监听器监听事件。



## 7、示例

### 1、Spring 事件发布

实现非常简单，只需要三个步骤：

1. 定义一个继承**ApplicationEvent**事件
2. 定义一个实现**ApplicationListener**的监听器或者使用 **@EventListener** 来监听事件
3. 定义一个发送者，调用**ApplicationContext**直接发布或者使用 **ApplicationEventPublisher** 来发布自定义事件（@Autowired注入即可）

```java
//1. 建立事件类,继承applicationEvent
public class MyEvent extends ApplicationEvent {

    public MyEvent(Object source) {
        super(source);
        System.out.println("my Event");
    }
    public void print(){
        System.out.println("hello spring event[MyEvent]");
    }
}
//2.建立监听类,实现ApplicationListener接口
public class MyListener  implements ApplicationListener{
    
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof MyEvent){
            System.out.println("into My Listener");
            MyEvent myEvent=(MyEvent)event;
            myEvent.print();
        }
    }
}
//3.创建一个发布事件的类,该类实现ApplicationContextAware接口,得到ApplicationContext对象,使用该对象的publishEvent方法发布事件.
public class MyPubisher implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    public void publishEvent(ApplicationEvent event){
        System.out.println("into My Publisher's method");
        applicationContext.publishEvent(event);
    }
}
//4.测试
public class MyTest {
    public static void main(String[] args) {
        ApplicationContext context=new ClassPathXmlApplicationContext("classpath:spring/application-database.xml");
        MyPubisher myPubisher=(MyPubisher) context.getBean("myPublisher");
        myPubisher.publishEvent(new MyEvent("1"));
    }
}
```




























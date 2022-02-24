# Spring-MVC入门

配置web.xml

- 配置**DispatcherServlet**：**默认加载 /WEB-INF/servletName-servlet.xml**的Spring配置文件，启动WEB层的Spring容器，可以通过属性**contextConfigLocation**初始化参数，自定义配置文件的位置和名称

```xml
<servlet>
    <!-- 设置servlet名字 -->
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <!-- 设置p -->
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:xxx.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern></url-pattern>
</servlet-mapping>
```

- 创建Spring-MVC配置文件：配置自动扫描、视图解析器

```xml
<context:component-scan base-package="xxx"/>
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <!-- 前缀 -->
    <property name="prefix" value="/WEB-INF/views/"/>
    <!-- 后缀 -->
    <property name="suffix" value=".jsp"/>
</bean>
```

- 创建请求处理类：使用@Controller注解修饰类，@RequestMapping注解修饰方法

# 常用注解

## @Controller注解

### 简介

在SpringBoot中@Controller注解及其相关注解，主要分三个层面，请求前、处理中、返回。

| **应用场景** | **注解**        | **注解说明**                         |
| ------------ | --------------- | ------------------------------------ |
| 处理请求     | @Controller     | 处理 Http 请求                       |
| 处理请求     | @RestController | @Controller 的衍生注解               |
| 路由请求     | @RequestMapping | 路由请求 可以设置各种操作方法        |
| 路由请求     | @GetMapping     | GET 方法的路由                       |
| 路由请求     | @PostMapping    | POST 方法的路由                      |
| 路由请求     | @PutMapping     | PUT 方法的路由                       |
| 路由请求     | @DeleteMapping  | DELETE 方法的路由                    |
| 请求参数     | @PathVariable   | 处理请求 url 路径中的参数 /user/{id} |
| 请求参数     | @RequestParam   | 处理问号后面的参数                   |
| 请求参数     | @RequestBody    | 请求参数以json格式提交               |
| 返回参数     | @ResponseBody   | 返回 json 格式                       |

注意：

- @RestController是@Controller的子集，该注解是@Controller和@ResponseBody的集合
- @GetMapping、@PostMapping、@PutMapping、@DeleteMapping 是 @RequestMapping 的子集

@Controller 与 @RestController应用场景：

- @Controller 一般应用在有返回界面的应用场景下
- @RestController 如果只是接口，那么就用 @RestController 来注解

## @RequestMapping注解

### 简介

1、@RequestMapping注解为**控制器@Controller**指定可以处理哪些URL请求

2、在控制器@Controller的**类定义**以及**方法定义**处都可以用该注解修饰

- 类定义处：提供初步的请求映射信息，相对于WEB应用的根目录

- 方法处：提供细分映射信息，相对于类定义处的URL
  - **注意**：若类定义处无该注解，则此URL相对于WEB应用的根目录

3、DispatcherServlet拦截请求后，通过控制器上的@RequestMapping提供的映射信息确定请求对应的处理方法

4、@RequestMapping还可以设定**请求方法**、**请求参数**、**请求头的映射**等让映射更加精确

### 属性

- **Value**：请求URL

- **Method**：请求方法

- **Params**：请求参数，支持简单的表达式

- **Heads**：请求头，支持简单表达式

```java
//表明映射URL为/helloThree，请求方法必须为GET，必须要含有参数userName与age且age的值不能为18，请求头必须为指定的
@RequestMapping(

    value = "/helloThree", 

    method = RequestMethod.GET,

    params = {"userName", "age!=18"}, 

    headers = {"Accept-Language=zh-CN,zh;q=0.9"}
)
```

### 支持Ant风格的URL

- 支持三种匹配符
  - ？：匹配文件名中的一个字符
  - *：匹配文件名中的任意字符
  - **：匹配多层路径

```text
1. 例子：/user/*/creatUser

	匹配：/user/aaa/creatUser

2. 例子：/user/**/creatUser

	匹配：/user/creatUser，/uer/xxx/yyy/creatUser

3. 例子：/user/creatUser？？

	匹配：/user/creatUserxxx
```

##  @PathVariable注解

### 简介

1、@PathVariable注解**映射URL绑定的占位符**，通过该注解可以将URL中**占位符参数绑定**到控制器@Controller的处理方法的**入参**中，即URL中的{xxx}占位符可以通过@PathVariable（“xxx“）绑定到处理方法的入参中。

```java
@RequestMapping(value = "/helloFive/{name}/{age}")
public String testFive(@PathVariable("name") String name,@PathVariable("age") String age)
```

2、只支持简单的数据类型，**只支持GET请求**，**不支持复杂的数据类型以及自定义的数据类型**。

3、@PathVariable**只有一个参数**，不填写**默认绑定**到与URL占位符参数**同名的入参**中

## @RequestParam注解

### 简介

1、Spring-MVC通过分析处理方法的签名，将HTTP的请求信息，绑定到处理方法的相应入参中。

2、可以对方法以及方法的入参标注相应的注解例如**@PathVariable**、**@RequestParam**、**@RequestHeader**。

3、在处理方法**入参处使用**@RequestParam可以**把请求参数传递给处理方法的入参**。

4、支持GET、POST、PUT、DELETE请求方法。

### 属性

- **Value**：参数名

- **Required**：该请求参数是否必须？，默认为true，表示必须存在，不存在将抛出异常。

```java
@RequestParam(value = "age", defaultValue = "0", required = false) int age
```

## @RequestHeader注解

### 简介

1、注解绑定请求报头的属性值。

2、**请求报头包含了若干个属性**，服务器可据此获知客户端的信息，通过该注解即可**将请求头中的属性值绑定到处理方法的入参**中。

```java
@RequestHeader(value = "Accept-Language") String val
```

## @CookieValue注解

### 简介

1、让处理方法绑定请求中的某个Cookie值。

### 属性

- **value**：参数名称

- **required**：是否必须

- **defaultValue**：默认值

```java
@CookieValue(value = "JSESSIONID") String val
```

## @SessionAttributes注解

### 简介

默认情况下Spring MVC将模型中的数据存储到request域中。当一个请求结束后，数据就失效了。如果要跨页面使用。那么需要使用到session。而@SessionAttributes注解就可以使得模型中的数据存储一份到session域中。

### 参数

- **names**：这是一个字符串数组。里面应写需要存储到session中数据的名称。

- **types**：根据指定参数的类型，将模型中对应类型的参数存储到session中

- **value**：其实和names是一样的。

##  @ResponseBody注解

### 简介

1、**作用于方法上**，表示该方法的返回值结果直接写入HTTPResponseBody中，一般在**异步获取数据**时使用。

2、在使用@RequestMapping后再使用此注解，返回值不会被解析为跳转路径，而是直接写入HTTP响应正文中。

### 使用时机

返回的数据不是页面

## @RequestBody注解

### 简介

1、将**HTTP请求正文插入方法**中，使用适合的HttpMessageConverter**将请求体写入某个对象**。

2、**作用于形参列表上**，用于将前台发送过来的固定格式的数据（xml或者json等）封装为对应的Bean，封装时使用到的一个对象是系统默认配置的HttpMessageConverter进行解析，然后封装到形参上。

### 使用时机

1、GET、POST，根据request header Content-Type的值判断：

- application/x-www-form-urlencoded， 可选（即非必须，因为这种情况的数据@RequestParam, @ModelAttribute也可以处理，当然@RequestBody也能处理）
- 其他格式， 必须（其他格式包括application/json, application/xml等。这些格式的数据，必须使用@RequestBody来处理）

2、PUT方式提交时，根据request header Content-Type的值来判断：

- application/x-www-form-urlencoded， 必须
- multipart/form-data, 不能处理
- 其他格式， 必须

### 说明

request的body部分的数据编码格式由header部分的Content-Type指定

## @ExceptionHandler注解

### 简介

1、统一处理某一异常类。

2、属性**value**指定异常类

3、被该注解修饰的方法的返回值可以为ModleAndView、Modle、Map、View、String、void、@ResponseBody、HttpEntity\<T>、ResponseEntity\<T>。

### 优先级问题

- 例如发生的是NullPointerException，但声明的异常有RuntimeException、Exception，此时会根据异常的继承关系找到**继承深度最浅**的那个@ExceptionHandler注解方法，即标记了RuntimeException的方法。
- ExceptionHandlerMethodResolver内部若找不到@ExceptionHandler注解的话，会找**@ControllerAdvice注解**中的@ExceptionHandler注解方法

## @ResponseStatus注解

### 简介

1、两种用法：修饰**自定义异常类**，修饰**目标处理方法**。

2、属性**value**设置异常状态，属性**reason**设置异常描述。

3、修饰自定义异常类，先声明一个自定义异常类，再加上该注解。

### 例子

定义一个@ResponseStatus 注解修饰的异常类

```java
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "用户名错误")
public class UserNameNOT extends RuntimeException
```

当处理器抛出上述自定义异常，若**ExceptionHandlerExceptionResolver**不解析异常，由于触发的异常UserNameNOT带有@ResponseStatus注解。因此会被**ResponseStatusExceptionResolver**解析到。最后响应预先设置的HTTP状态码与错误信息给客户端。

## @ControllerAdvice

### 简介

这是一个增强的 Controller。使用这个 Controller ，可以实现三个方面的功能：

1. 全局异常处理
2. 全局数据绑定
3. 全局数据预处理

### 全局异常处理

只需要定义类，添加该注解即可定义方式如下：

```java
@ControllerAdvice
public class MyGlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ModelAndView customException(Exception e) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", e.getMessage());
        mv.setViewName("myerror");
        return mv;
    }
}
```

### 全局数据绑定

用来做一些初始化的数据操作，我们可以将一些公共的数据定义在添加了 @ControllerAdvice 注解的类中，这样，在每一个 Controller 的接口中，就都能够访问导致这些数据。

使用 @ModelAttribute 注解标记该方法的返回数据是一个全局数据，默认情况下，这个全局数据的 key 就是返回的变量名，value 就是方法返回值，当然开发者可以通过 @ModelAttribute 注解的 name 属性去重新指定 key。

```java
@ControllerAdvice
public class MyGlobalExceptionHandler {
    @ModelAttribute(name = "md")
    public Map<String,Object> mydata() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("age", 99);
        map.put("gender", "男");
        return map;
    }
}
```

### 全局数据预处理

两个类具有同名属性，mvc无法分辨时，先用@ModelAttribute取别名

```java
@PostMapping("/book")
public void addBook(@ModelAttribute("b") Book book, @ModelAttribute("a") Author author) {
    System.out.println(book);
    System.out.println(author);
}
```

@InitBinder("b") 注解表示该方法用来处理和Book和相关的参数,在方法中,给参数添加一个 b 前缀,即请求参数要有b前缀.

```java
@InitBinder("b")
public void b(WebDataBinder binder) {
    binder.setFieldDefaultPrefix("b.");
}
@InitBinder("a")
public void a(WebDataBinder binder) {
    binder.setFieldDefaultPrefix("a.");
}
```

发送请求时

![image-20211106170820089](images/image-20211106170820089.png)



# REST

1、配置**HiddenHttpMethodFilter**拦截器，支持将POST转为PUT、DELETE

```xml
<filter>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>hiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

SpringMVC 提供了 **HiddenHttpMethodFilter** 帮助我们**将 POST 请求转换为 DELETE 或 PUT 请求**。

**HiddenHttpMethodFilter** 处理put和delete请求的条件：

- 当前请求的请求方式必须为**post**


- 当前请求必须传输请求参数**_method**


满足以上条件，**HiddenHttpMethodFilter** 过滤器就会将当前请求的请求方式转换为请求参数_method的值，因此请求参数\_method的值才是最终的请求方式。

```text
目前为止，SpringMVC中提供了两个过滤器：CharacterEncodingFilter 和 HiddenHttpMethodFilter

在web.xml中注册时，必须先注册 CharacterEncodingFilter，再注册 HiddenHttpMethodFilter

原因：

- 在 CharacterEncodingFilter 中通过 request.setCharacterEncoding(encoding) 方法设置字符集的

- request.setCharacterEncoding(encoding) 方法要求前面不能有任何获取请求参数的操作

- 而 HiddenHttpMethodFilter 恰恰有一个获取请求方式的操作：
		String paramValue = request.getParameter(this.methodParam);
```





# POJO对象绑定请求参数

1. Spring-MVC会按照**请求参数名**和**POJO的属性名**进行自动的匹配，自动为该对象填充属性值，**支持级联属性**如xxx.a，xxx.b。
2. 其实是调用了POJO的**无参构造**，然后使用**set方法**填充属性
3. 使用该方法POJO无**需使用注解修饰**，POJO的级联POJO也是

# 使用Servlet API作为入参

```java
public String testServletAPI(HttpServletResponse response, HttpServletRequest request)
```

- 可以接受这些参数

```java
1、HttpServletRequest
2、HttpServletResponse
3、HttpSession
4、java.security.Principal
5、Locale
6、InputStream
7、OutputStream
8、Reader
9、Writer
```

# 处理模型数据

## 简介

Spring-MVC提供了四种途径输出模型数据

- **ModelAndView**
  - 处理方法返回值类型为ModelAndView时，方法体既可通过该对象添加模型数据。
- **Map以及Model**
  - 入参为**org.springframework.ui.Model**、**org.springframework.ui.ModelMap** 或 **java.uti.Map** 时，处理方法返回时，Map中的数据会自动添加到模型中。
- **@SessionAttributes**注解
  - 将模型中的某个属性暂存到HttpSession中，以便多个请求之间可以共享这个属性
- **@ModelAttribute**注解
  - 方法入参标注该注解后，入参的对象就会放到数据模型中

## ModelAndView

**控制器处理方法**的**返回值**如果为ModelAndView，则既包含视图信息，也包含模型数据信息。

ModelAndView有**Model**和**View**的功能

- **Model**：主要用于向请求域共享数据
- **View**：主要用于设置视图，实现页面跳转

- 添加模型数据
  - **addObject**(String attributeName, Object attributeValue)
  - **addAllObject**(Map<String, ?> modelMap)

- 设置视图
  - **setView**(View view)
  - **setViewName**(String viewName)

## Map以及Model

Spring-MVC在内部使用了一个**org.springframework.ui.Model** 接口存储模型数据。

- 具体步骤
  - Spring-MVC在调用方法前会创建一个**隐含的模型对象**作为模型数据的存储容器。
  - 如果**方法入参**为**Map**或**Model**类型，Spring-MVC会将隐含模型的引用传递给这些入参，在方法体内，开发者可以通过这个入参对象访问到模型中的所有数据，也可以添加新的属性数据。

## @SessionAttributes注解

在**控制器**上使用该注解修饰，即可在多个请求之间共用某个模型属性的数据

- 使用方法

  - 可以通过属性名指定需要放到Session中的数据

  - 可以通过模型属性的对象类型指定需要放到Session中的数据

    ```java
    @SessionAttributes(value={“user1”,“user2”},types={Dept.class})
    ```

    - 所有Dept类型以及属性名为user1与user2的数据放入Session中

## @ModelAttribute注解

在**方法的定义**上使用该注解，Spring-MVC在调用目标处理方法前，会先逐个调用在方法级上标注了该注解的方法。

在**方法入参**中使用该注解，Spring-MVC先从模型数据中获取对象，之后将请求参数绑定到对象中，再传入形参，如果不存在这个对象，则会自动实例化一个新的对象，并且模型数据中的对象的属性会被覆盖。（对象是同一个，只不过数据被覆盖）。模型属性还覆盖了来自 HTTP Servlet 请求参数的名称与字段名称匹配的值，也就是请求参数如果和模型类中的域变量一致，则会自动将这些请求参数绑定到这个模型对象，这被称为数据绑定，从而避免了解析和转换每个请求参数和表单字段这样的代码。

- 写入模型数据有俩种方式：**存入implicitModel**

  - 通过入参处使用**Model类型或者Map类型**（不推荐，会产生一个key = void 值为null的数据）在调用最终的目标处理方法前，“student”会被放入到Model或Map中，Map放入数据时，key由map决定，如果注解没有使用value或者name。

    ```java
    @ModelAttribute
    public void getStudent(@RequestParam(value = "id", required = false) String idStr, Map<String, Object> map) {
        .........
        //key由map决定
        map.put("student", student);
    }
    //隐含模型内会似乎有俩个对象，一个是map指定key，一个是类型首字母小写
    ```

  - 通过@ModelAttribute的value或name属性以及注解需要的方法返回值，如果不用value或name属性，默认为**返回值首字母小写**。

    ```java
    @ModelAttribute("student")
    public Student getStudent(@RequestParam(value = "id", required = false) String idStr)
    ```

    - **注意**：
      - 如果同时指定了返回值以及@ModelAttribute的name属性，则隐含模型内会出现两个相同属性相同，key不同的对象。两个key分别为注解的name属性与返回值类型首字母小写。
      - 在没有返回值时，注解的属性似乎无用。

- 读取由该注解写入Model的数据：

  - 在**方法入参前**使用该注解，并写明value或name值，Spring-MVC会自动从模型数据中获取。且只获取一个，否则似乎按照类型全部获取。
  - 在方法入参前无该注解，key默认为POJO类名第一个字母小写。
  - 最终目标处理方法处的实体**形参名**与注解标注**的value或name无关**，只与**类型有关**。
  - 在implicitModel中查找key对应的对象，若存在，作为入参传入，在@ModelAttribute注解修饰过的方法中的Map存入过该key相同对象，则可以获取到。

## Model、Map、ModelMap的关系

Model、Map、ModelMap类型的参数其实本质上都是 **BindingAwareModelMap** 类型的。

```java
public interface Model{}
public class ModelMap extends LinkedHashMap<String, Object> {}
public class ExtendedModelMap extends ModelMap implements Model {}
public class BindingAwareModelMap extends ExtendedModelMap {}
```

## 域对象共享数据

以下方法会直接将数据存入request域中

1、直接传入ServletAPI

```java
@RequestMapping("/testServletAPI")
public String testServletAPI(HttpServletRequest request){
    request.setAttribute("testScope", "hello,servletAPI");
    return "success";
}
```

2、使用ModelAndView

```java
@RequestMapping("/testModelAndView")
public ModelAndView testModelAndView(){
    ModelAndView mav = new ModelAndView();
    // 向请求域共享数据
    mav.addObject("testScope", "hello,ModelAndView");
    // 设置视图，实现页面跳转
    mav.setViewName("success");
    return mav;
}
```

3、使用Model

```java
@RequestMapping("/testModel")
// 在入参处使用
public String testModel(Model model){
    model.addAttribute("testScope", "hello,Model");
    return "success";
}
```

4、使用map

在Controller上使用Map来共享数据，其实就是存放在Model

```java
@RequestMapping("/testMap")
// 在入参处使用
public String testMap(Map<String, Object> map){
    map.put("testScope", "hello,Map");
    return "success";
}
```

5、使用ModelMap

```java
@RequestMapping("/testModelMap")
// 在入参处使用
public String testModelMap(ModelMap modelMap){
    modelMap.addAttribute("testScope", "hello,ModelMap");
    return "success";
}
```

7、向session域共享数据

直接传入ServletAPI

```java
@RequestMapping("/testSession")
public String testSession(HttpSession session){
    session.setAttribute("testSessionScope", "hello,session");
    return "success";
}
```

8、向application域共享数据

直接传入ServletAPI

```java
@RequestMapping("/testApplication")
public String testApplication(HttpSession session){
	ServletContext application = session.getServletContext();
    application.setAttribute("testApplicationScope", "hello,application");
    return "success";
}
```

# 视图和视图解析器

## 简介

1、Spring-MVC根据返回值类型String、ModelAndView、View =》**ModelAndView** =》ViewResolver=》视图对象JSP/JSTL/PDF

2、请求处理方法执行完毕，**最终会返回一个ModelAndView对象**，返回其他类型的Spring-MVC会在内部装配成一个ModelAndView对象。

3、**视图**：渲染模型数据，为了实现视图模型和具体实现技术的解耦，Spring 在 org.springframework.web.servlet 包中定义了一个高度抽象的 **View 接口**。视图对象由视图解析器负责实例化，视图无状态，所以不会有线程安全问题。

4、**视图解析器**：在SpringWEB上下文中配置一种或多种解析策略，并指定他们之间的先后顺序。每一种映射策略对应一个具体的视图解析器实现类，将逻辑视图解析为一个具体的视图对象。所有的视图解析器都必须实现**ViewResolver接口**。

5、每个视图解析器都实现了**Ordered接口**并开放出一个**order属性**，可以通过order属性指定解析器的优先顺序，order越小优先级越高。Spring-MVC会按视图解析器顺序对逻辑视图名进行解析，直到成功，否则抛出ServletException异常。

## 常用的视图实现类

- URL资源视图：
  - **InternalResourceView**：将JSP或其他资源封装成一个视图，是InternalResourceViewResolver默认使用的视图实现类
  - JstlView：如果JSP文件中使用了JSTL国际化标签功能，则需要使用

- 文档视图：
  - **AbstractExcelView：Excel**文档视图的抽象类，该类基于POI构造Excel文档

- JSON视图：
  - **MappingJacksonJsonView**：将模型数据通过Jackson开源框架的ObjectMapper以JSON方式输出

## 常用的视图解析器实现类

- 解析为Bean的名字：
  - **BeanNameViewResolver**：将逻辑视图名解析为一个Bean，Bean的id等于逻辑视图名
- 解析为URL文件：
  - **InternalResourceViewResolver**：将视图名解析为一个URL文件，一般使用该解析器将视图名映射为一个保存在WEN-INF目录下的程序文件

## InternalResourceViewResolver解析器

用于解析JSP

```xml
<!-- 解析 /WEB-INF/views/xxxx.jsp -->
<bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```

可以通过\<property name="viewNames" value="html*"/>指定处理规则，此规则表示，只处理html开头的视图名，如html/aa。

## 补充

1、如果要使用JSTL的fmt标签需要在Spring-MVC的配置文件中配置国际化资源文件

```xml
<bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
    <property name="basename" value="i18n"/>
</bean>
```

2、若希望直接响应通过Spring-MVC渲染的页面，可以使用mvc：view-controller标签实现

```xml
<mvc:view-controller path="/success" view-name="success"/>
```

3、视图对象需要配置IOC容器中的一个Bean，使用**BeanNameViewResolver**作为视图解析器即可

```xml
<bean class="org.springframework.web.servlet.view.BeanNameViewResolver">
    <property name="order" value="10"/>
</bean>
```

```java
@Component(value = "helloView")
public class HelloView implements View
```

# 重定向

如果返回的视图名中带**forward：**或者**redirect：**前缀

# Spring-MVC表单标签

## 简介

Spring-MVC表单标签可以将模型数据中的属性和HTML表单的元素相绑定，实现快速表单回显和编辑

## 标签

1、**form标签**：可以通过 @modelAttribute 属性指定绑定的模型属性，若没有指定该属性，则默认从 request 域对象中读取 command 的表单 Bean，如果该属性值也不存在，则会发生错误。

```html
<form:form action="${pageContext.request.contextPath}/emp" method="post" modelAttribute="employee">
```

2、**form:radiobutton标签**：单选框组件标签，当表单 bean 对应的属性值和 value 值相等时，单选框被选中。

3、**form:radiobuttons标签**：单选框组标签，用于构造多个单选框

- **items**：可以是一个 List、String[] 或 Map

- **itemValue**：指定 radio 的 value 值。可以是集合中 bean 的一个属性值 

- **itemLabel**：指定 radio 的 label 值 

- **delimiter**：多个单选框可以通过 delimiter 指定分隔符

4、**form:checkbox标签**：复选框组件。用于构造单个复选框

5、**form:checkboxs标签**：用于构造多个复选框。使用方式同 form:radiobuttons 标签

6、**form:select标签**：用于构造下拉框组件。使用方式同 form:radiobuttons 标签

7、**form:option标签**：下拉框选项组件标签。使用方式同 form:radiobuttons 标签

8、**form:errors标签**：显示表单组件或数据校验所对应的错误 

- <form:errors path =”* ”/> ：显示表单所有的错误 
- <form:errors path = “user*”/> ：显示所有以 user 为前缀的属性对应 的错误 
- <form:errors path =”username”/> ：显示特定表单对象属性的错误

9、form:input、form:password、form:hidden、form:textarea ：对应 HTML 表单的 text、password、hidden、textarea 标签

Spring-MVC 提供了多个表单组件标签，如\<form:input>\<form:select>等，用以绑定表单字段的属性值，它们的共有属性如下：

- **Path**：表单字段，对应html元素的name属性，支持级联属性

- **HtmlEscape**：是否对表单值的html特殊字符进行转换，默认为true

- **cssClass**：表单组件对应的CSS样式类名

- **cssErrorClass**：表单组件的数据存在错误时，采取的CSS样式

# 处理静态资源

1、若将 **DispatcherServlet** 请求映射配置为 **/**，则 Spring MVC 将捕获 WEB 容器的**所有请求**，包括静态资源的请求， SpringMVC 会将他们当成一个普通请求处理，因找不到对应处理器将导致错误。

2、可以在 SpringMVC 的配置文件中配置 **\<mvc:default-servlet-handler/>**的方式解决静态资源的问题：

- 将在 SpringMVC 上下文中定义一个 **DefaultServletHttpRequestHandler**，它会对进入 DispatcherServlet 的请求进行筛查，如果发现是没有经过映射的请求，就将该请求交由 WEB 应用服务器默认的 Servlet 处理，如果不是静态资源的请求，才由 DispatcherServlet 继续处理。

- 一般 WEB 应用服务器默认的 Servlet 的名称都是 default。若所使用的 WEB 服务器的默认 Servlet 名称不是 default，则需要通过 default-servlet-name 属性显式指定。

# 数据绑定流程

1、Spring-MVC框架将**ServletRequest对象及目标方法的入参实例**传递给**WebDataBinderFactory**实例，创建**DataBinder**对象。

2、DataBinder调用装配在Spring-MVC上下文中的**ConversionService**组件进行数据类型转换、数据格式化。将Servlet中的**请求信息填充到入参对象**中。

3、调用**Validator**组件对已经绑定了请求消息的入参对象进行**数据合法性校验**，最终生成数据绑定结果**BindingData**对象。

4、Spring-MVC抽取BindingResult中的**入参对象和校验错误对象**，将他们赋给**处理方法的响应入参**。

5、由 **@InitBinder** 标识的方法，可以对 WebDataBinder 对象进行初始化。WebDataBinder 是 DataBinder 的子类，用于完成由表单字段到 JavaBean 属性的绑定。

- @InitBinder方法不能有返回值，它必须声明为void。

- @InitBinder方法的参数通常是是 WebDataBinder。

```java
@InitBinder
public void initBinder(WebDataBinder dataBinder){
    dataBinder.setDisallowedFields("roleset");
}
```

![image-20210923104634810](Spring-MVC笔记\images\Spring-MVC.assets\image-20210923104634810.png)

# 数据转换

## Spring-MVC上下文中内建的转换器

![image-20210923104741375](images\Spring-MVC.assets\image-20210923104741375.png)

## 自定义类型转换器

1. **ConversionService** 是 Spring 类型转换体系的核心接口。
2. 可以利用**ConversionServiceFactoryBean**在Spring 的 IOC 容器中定义一个ConversionService。Spring将自动识别出IOC容器中的ConversionService，并在Bean属性配置及Spring-MVC处理方法入参绑定等场合使用它进行数据的转换。
3. 可通过 ConversionServiceFactoryBean 的 **converters** 属性注册自定义的类型转换器。

```xml
<mvc:annotation-driven conversion-service="conversionService" />
<bean id="conversionService" class="org.springframework.format.support.ConversionServiceFactoryBean">
    <property name="converters">
        <list>
            <bean class="xxx.xxxx.xxx.employeeC"/>
        </list>
    </property>
</bean>
需要实现Converter接口
```

## Spring-MVC支持的转换器

**Spring-MVC定义了三种类型的转换器接口，实现任意一个转换器接口**都可以作为自定义的转换器注册到ConversionServiceFactroyBean。

1. **Converter**：将 S 类型对象转为 T 类型对象。
2. **ConverterFactory**：将相同系列多个“同质”Converter 封装在一起。如果希望将一种类型的对象转换为另一种类型及其子类的对象（例如将 String 转换为 Number 及 Number 子类 （Integer、Long、Double 等）对象）可使用该转换器工厂类。
3. **GenericConverter**：会根据源类对象及目标类对象所在的宿主类中的上下文信息进行类型转换。

```xml
<!-- <mvc:annotation-driven conversion-service="conversionService" />会将自定义的 ConversionService 注册到 Spring MVC 的上下文中 -->
<bean id="conversionService" class="org.springframework.format.support.ConversionServiceFactoryBean">
    <property name="converters">
        <list>
            <bean class="xxx.xxxx.xxx.employeeC"/>
        </list>
    </property>
</bean>
```

# 数据格式化

## 简介

1、Spring在格式化模块中定义了一个实现**ConversionService接口**的**FormattiongConversionService**实现类，该类扩展了**GenericConversionService**，因此它兼具类型转换的功能，又具有格式化的功能。

2、FormattiongConversionService拥有一个**FactroyBean**工厂类，后者用于在Spring上下文构造前者。

## FormattiongConversionServiceFactroyBean

**FormattiongConversionServiceFactroyBean**内部已经注册了：

1. **NumberFormatAnnotationFormatterFactory**：支持对数字类型的属性使用**@NumberFormat注解**。
2. **JodaDateTimeFormatAnnotationFormatterFactory**：支持对日期类型的属性使用**@DateTimeFormat注解**。

装配了FormattiongConversionServiceFactroyBean后，就可以在Spring-MVC入参绑定及模型数据输出时使用注解驱动。

**注意**：\<mvc:annotation-driven/>默认创建的是ConversionService

## 日期格式化

### 简介

**@DateTimeFormat注解**可对**java.util.Date**、**java.util.Calendar**、**java.long.Long** 时间类型进行标注。

### 属性

- **属性pattern**：类型为字符串，指定解析/格式化字段数据的模式。
- **属性iso**：类型为DateTimeFormat.ISO，指定解析/格式化字段数据的ISO模式，包括四种：ISO.NONE（不使用，默认），ISO.DATE（yyyy-MM-dd），ISO.TIME（hh：mm：ss.SSSZ），ISO.DATE_TIME（yyyy-MM-dd hh：mm：ss：SSSZ）。
- **属性style**：字符串类型，通过样式指定日期时间的格式，由两位字符组成，第一位表示日期的格式，第二位表示时间的格式，S：短日 期/时间格式、M：中日期/时间格式、L：长日期/时间格式、F：完整 日期/时间格式、-：忽略日期或时间格式。

## 数值格式化

### 简介

**@NumberFormat注解**可对类似数字类型的属性进行标注，包含俩个互斥的属性

### 属性

- **属性Style**：类型为NumberFormat.Style。用于指定样式类型，包括三种：Style.NUMBER（正常数字类型），Style.CURRENCY（货币类型）、 Style.PERCENT（ 百分数类型）。

- **属性pattern**：类型为 String，自定义样式， 如patter="#,###"。

# 数据校验

## 简介

1、**JSR 303** 是 Java 为 Bean 数据合法性校验提供的标准框架，通过在Bean属性上标注注解，并通过标准的验证接口对Bean进行校验。

![image-20210923110343960](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923110343960.png)

2、**Hibernate Validator** 是 JSR 303 的一个参考实现，除支持所有标准的校验注解外，它还支持以下的扩展注解。

![image-20210923110409811](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923110409811.png)

3、Spring 在进行数据绑定时，可同时调用校验框架完成数据校验工作。在 Spring MVC 中，可直接通过注解驱动的方式进行数据校验。

## LocalValidatorFactroyBean 

1、Spring 的 **LocalValidatorFactroyBean** 既实现了 Spring 的 Validator 接口，也实现了 JSR 303 的 Validator 接口。只要在 Spring 容器中定义一个 LocalValidatorFactoryBean，即可将其注入到需要数据校验的 Bean 中。

- Spring 本身并没有提供 JSR 303 的实现，所以必须将 JSR 303 的实现者的 jar 包放到类路径下
- \<mvc:annotation-driven/>会**默认**装配好一个 LocalValidatorFactoryBean，通过在处理方法的入参上标注 @valid 注解即可让 Spring-MVC 在**完成数据绑定后执行数据校验**的工作。

## 注意

Spring-MVC 是通过对**处理方法签名的规约来保存校验结果的**：前一个表单/命令对象的校验结果保存到随后的入参中，这个保存校验结果的入参必须是 **BindingResult 或 Errors** 类型，这两个类都位于 **org.springframework.validation** 包中。

- 需校验的 Bean 对象和其绑定结果对象或错误对象时成对出现的，它**们之间不允许声明其他的入参**。
- **Errors 接口**提供了获取错误信息的方法，如 getErrorCount() 或 getFieldErrors(String field)。
- **BindingResult** 扩展了 Errors 接口

## 在目标方法中获取校验结果

常用方法：

- FieldError getFieldError(String field) 
- List getFieldErrors() 
- Object getFieldValue(String field) 
- Int getErrorCount()

## 在页面上显示错误

- Spring-MVC除了将校验结果保存在BindingResult 或 Errors还会将所有校验结果保存到 “隐含模型”。
- 即使处理方法的签名中没有用于保存校验结果的入参，校验结果依旧会保存到隐含模型中。
- 隐含模型的所有数据最终都将通过HttpServletRequest的属性列表暴露给JSP视图对象，因此可以在页面获取到错误信息。**<form:errors path=“userName”>**

# HttpMessageConverter

## 简介

**HttpMessageConverter\<T>**是Spring 3.0 新添加的一个接口，负责将请求信息转为一个对象（类型T），将对象输出为响应信息。

## 接口定义的方法

- **Boolean canRead(Class clazz,MediaType mediaType)**：指定转换器可以读取的对象类型，即转换器是否可将请求信息转换为 clazz 类型的对象，同时指定支持 MIME 类型(text/html,applaiction/json等) 
- **Boolean canWrite(Class clazz,MediaType mediaType)**：指定转换器是否可将 clazz 类型的对象写到响应流中，响应流支持的媒体类型在MediaType 中定义。 
- **List getSupportMediaTypes()**：该转换器支持的媒体类型。
- **T read(Class clazz,HttpInputMessage inputMessage)**： 将请求信息流转换为 T 类型的对象。
- **void write(T t,MediaType contnetType,HttpOutputMessgae outputMessage)**：将T类型的对象写到响应流中，同时指定相应的媒体类 型为 contentType。

![image-20210923111942270](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923111942270.png)

## 接口实现类

| 实现类                               | 功能说明                                                     |
| ------------------------------------ | ------------------------------------------------------------ |
| StringHtpMessageConverter            | 将请求信息转换为字符串                                       |
| FormHttpMessageConverter             | 将表单数据读取到 MultiValueMap中                             |
| XmIAwareFormHittpMessageConverter    | 扩展于FormHitpMessageConverter,如果部分表单属性是XML数据，可用该转换器进行读取 |
| ResourceHittpMessageConverter        | 读写org.springframework.core.io.Resource对象                 |
| BufferedlmageHttpMessageConverter    | 读写 Bufferedllmage对象                                      |
| ByteArrayHttpMessageConverter        | 读写二进制数据                                               |
| SourcelittpMessageConverter          | 读写javax.xml.transform.Source类型的数据                     |
| MarshallingHittpMessageConverter     | 通过 Spring 的org.springframework..xmL.Marshaller和Unmarshaller 读写XML消息 |
| Jaxb2RootElemengHttpMessageConverter | 通过JAXB2读写XML消息,将请求消息转换到标注XmIRootElement和 XxmlTy直接的类中 |
| MappingJacksonHttpMessageConverter   | 利用Jackson开源包的ObjectMapper读写JSON数据                  |
| RssChannellHittpMessageConverter     | 能够读写RSS种子消息                                          |
| AtomFeedHttpMessageConverter         | 和RssChannellHittpMessageConverter能够读写RSS种子消息        |

## HttpMessageConverter的默认装配

DispatcherServlet 默认装配 **RequestMappingHandlerAdapter** ，而RequestMappingHandlerAdapter 默认装配如下 **HttpMessageConverter**的实现。

![image-20210923112826854](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923112826854.png)

加入 jackson.jar 包后

![image-20210923112844941](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923112844941.png)

## 请求信息转化并绑定到处理方法

使用HttpMessageConverter\<T>将请求信息转化并绑定到处理方法的入参中、或将响应结果转为对应类型的响应信息。Spring提供了两种途径：

- 使用 **@RequestBody / @ResponseBody** 对处理方法进行标注。

- 使用 **HttpEntity\<T> / ResponseEntity\<T>** 作为处理方法的入参或返回值。

3、当控制器处理方法使用到 @RequestBody/@ResponseBody 或 HttpEntity\<T>/ResponseEntity\<T> 时，Spring首先根据请求头或响应头的 Accept 属性选择匹配的 HttpMessageConverter, 进而根据参数类型或泛型类型的过滤得到匹配的 HttpMessageConverter, 若找不到可用的HttpMessageConverter 将报错。

- **注意**：@RequestBody 和 @ResponseBody 不需要成对出现

```java
@ResponBody
@RequestMapping("/handle15")
//由ByteArrayHttpMessageConverter处理
public byte[] handle15() throws IOException {
    Resource resource = new ClassPathResource("/lighthouse.jpg");
    byte[] fileData = FileCopyUtils.copyToByteArray(resource.getIputStream());
    return fileData;
}

@RequestMapping(value="handle14", method=RequestMethod.POST)
//由StringHtpMessageConverter处理
public String handle14(@RequestBody String requestBody) {
    System.out.println(requestBody);
    return "success";
}
```

![image-20210923115326343](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923115326343.png)

# 文件上传

1、Spring-MVC为文件上传提供了直接支持，使用**MultipartResolver**实现。Spring用**Jakarta Commons FileUpload** 技术实现了一个MultipartResolver的实现类：**CommonsMultipartResovler**。

2、Spring-MVC上下文**默认没有装配MultipartResovler**，因此默认情况下不能处理文件的上传工作，若需使用要在上下文中配置MultipartResolver

```xml
<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
    <property name="defaultEncoding" value="UTF-8"/>
    <property name="maxUploadSize" value="-1"/>
</bean>
```

**注意**：

- Bean的ID必须为multipartResolver否则Spring找不到
- 属性 defaultEncoding：必须和用户JSP的pageEncoding相同
- 需要添加Jakarta Commons FileUpload 及 Jakarta Commons io 的类包添加到类路径下。

# 拦截器

## 简介

Spring-MVC可以使用拦截器对请求进行拦截处理，开发者可以自定义拦截器来实现特定的功能，自定义拦截器必须实现**HandlerInterceptor接口**，内含三个抽象方法：

- **preHandle()**：这个方法在业务处理器处理请求之前被调用，在该方法中对用户请求 request 进行处理。如果程序员决定该拦截器对请求进行拦截处理后还要调用其他的拦截器，或者是业务处理器去进行处理，则返回true；如果程序员决定不需要再调用其他的组件去处理请求，则返回false。
- **postHandle()**：这个方法在业务处理器处理完请求后，但是DispatcherServlet 向客户端返回响应前被调用，在该方法中对用户请求request进行处理。 
- **afterCompletion()**：这个方法在 DispatcherServlet 完全处理完请求后被调用，可以在该方法中进行一些资源清理的操作。

## 拦截器方法执行顺序

单个拦截器：

First#preHandle --> HandlerAdapter#handle --> First#postHandle --> DispatcherServlet#render --> First#afterCompletion

多个拦截器：

First#preHandle --> Second#preHandle --> HandlerAdapter#handle --> Second#postHandle --> First#postHandle --> DispatcherServlet#render --> Second# afterCompletion --> First#afterCompletion

## 配置自定义拦截器

```xml
<mvc:interceptors>
    <mvc:interceptor>
        <mvc:mapping path="/拦截路径"/>
        <bean class="拦截器类路径"/>
    </mvc:interceptor>
</mvc:interceptors>
```

## 注意

如果再**preHandle**阶段任意一个拦截器返回false直接跳到最后。

# 异常处理

## 简介

Spring-MVC通过HandlerExceptionResolver处理程序的异常，包括Handler映射、数据绑定、目标处理方法执行异常。

## 默认装配的异常处理器

DispatcherServlet默认装配的**HandlerExceptionResolver**。

- 没有使用\<mvc:annotation-driven>配置

![image-20210923163456940](images\Spring-MVC.assets\image-20210923163456940.png)

- 使用了\<mvc:annotation-driven>配置

![image-20210923163511094](images\Spring-MVC.assets\image-20210923163511094.png)

## ExceptionHandlerExceptionResolver

**作用**：主要处理Handler中使用**@ExceptionHandler注解**定义的方法。

## ResponseStatusExceptionResolver

**作用**：在异常及异常父类中找到**@ResponseStatus注解**，然后使用这个注解的属性进行处理。

## DefaultHandlerExceptionResolver

**作用**：对一些特殊的异常进行处理：**NoSuchRequestHandlingMethodException**、**HttpReques** **tMethodNotSupportedException**、**HttpMediaTypeNotSuppo** **rtedException**、**HttpMediaTypeNotAcceptableException** 等

## SimpleMappingExceptionResolver

**作用**：如果希望对所有异常进行统一处理，可以使用**SimpleMappingExceptionResolver**，它将异常类名映射为视图名，即发生异常时使用对应的视图报告异常。

**配置**：

```xml
<bean class = "org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
    <property name="exceptionMappings">
        <props>
            <prop key="异常全类名 ">视图名</prop>
        </props>
    </property>
</bean>
```

**属性**：

- **defaultErrorView**：为所有异常定义默认的异常处理页面exceptionMappings未定义的使用此配置。
- **exceptionAttribute**：定义存入的异常名默认为exception，将出现的异常信息在请求域中进行共享
- **exceptionMappings**：定义需要处理的特殊异常，用类名全路径作为key，异常视图为值。

# Servlet3.0

## 1、新规

框架必须提供**ServletContainerInitializer**的实现，在jar文件的**META-INF/services**目录中绑定一个名为**javax.servlet.ServletContainerInitializer.根据jar服务API**，它指向ServletContainerInitializer的实现类。

```java
//容器启动的时候会将@HandlesTypes指定的这个类型下面的子类（实现类，子接口等）传递过来
//@HandlesTypes传入感兴趣的类型
@HandlesTypes(value={HelloService.class})
public class MyServletContainerInitializer implements ServletContainerInitializer {
    @Override
	public void onStartup(Set<Class<?>> arg0, ServletContext sc) throws ServletException {
		System.out.println("感兴趣的类型：");
		for (Class<?> claz : arg0) {
			System.out.println(claz);
		}
		//注册组件ServletRegistration  
		ServletRegistration.Dynamic servlet = sc.addServlet("userServlet", new UserServlet());
		//配置servlet的映射信息
		servlet.addMapping("/user");
		//注册Listener
		sc.addListener(UserListener.class);
		//注册Filter  FilterRegistration
		FilterRegistration.Dynamic filter = sc.addFilter("userFilter", UserFilter.class);
		//配置Filter的映射信息
		filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
	}
}
```

实现了ServletContainerInitializer接口的应用启动的时候，会运行**onStartup(Set<Class<?>> arg0, ServletContext sc)**方法。

- 参数**Set<Class<?>> arg0**：感兴趣的类型的所有子类型

- 参数**ServletContext sc**：代表当前Web应用的ServletContext，一个Web应用一个ServletContext

使用**ServletContext**注册三大组件（Servlet，Filter，Listener）

## 2、异步

使用Servlet3.0的异步模式

- @WebServlet注解需要开启支持异步**asyncSupported=true**
- 方法内开启异步模式**AsyncContext startAsync = req.startAsync();**
- 创建一个副线程执行异步处理**startAsync.start(new Runnable() {..........})；**

```java
@WebServlet(value="/async",asyncSupported=true)
public class HelloAsyncServlet extends HttpServlet {
   
   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      //1、支持异步处理asyncSupported=true
      //2、开启异步模式
      System.out.println("主线程开始。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
      AsyncContext startAsync = req.startAsync();
      
      //3、业务逻辑进行异步处理;开始异步处理
      startAsync.start(new Runnable() {
         @Override
         public void run() {
            try {
               System.out.println("副线程开始。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
               sayHello();
                //异步任务完成
               startAsync.complete();
               //获取到异步上下文
               AsyncContext asyncContext = req.getAsyncContext();
               //获取响应
               ServletResponse response = asyncContext.getResponse();
               response.getWriter().write("hello async...");
               System.out.println("副线程结束。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
            } catch (Exception e) {
            }
         }
      });       
      System.out.println("主线程结束。。。"+Thread.currentThread()+"==>"+System.currentTimeMillis());
   }

   public void sayHello() throws Exception{
      System.out.println(Thread.currentThread()+" processing...");
      Thread.sleep(3000);
   }
}
```

# 完全注解Spring-MVC

1、WEB容器在启动的时候，会扫描每个jar包下的**META-INF/services/javax.servlet.ServletContainerInitializer**。

2、加载这个文件指定的类**SpringServletContainerInitializer**。

3、Spring的应用一启动会加载感兴趣的**WebApplicationInitializer**接口的下的所有组件

4、并且为WebApplicationInitializer组件创建对象（组件不是接口，不是抽象类）

1. **AbstractContextLoaderInitializer**：创建根容器，**createRootApplicationContext()；** **实现了WebApplicationInitializer接口**

2. **AbstractDispatcherServletInitializer**：**继承了了AbstractContextLoaderInitializer**

   1. 创建一个web的IOC容器：**createServletApplicationContext();**
   2. 创建了**DispatcherServlet**：**createDispatcherServlet()；** 
   3. 将创建的DispatcherServlet添加到ServletContext中：**getServletMappings();**

3. **AbstractAnnotationConfigDispatcherServletInitializer**：注解方式配置的**DispatcherServlet初始化器** 

   **继承了AbstractDispatcherServletInitializer**

   1. 创建根容器：**createRootApplicationContext()；** 调用下面方法
   2. 传入一个配置类：**getRootConfigClasses()；** 
   3. 创建WEB的IOC容器： **createServletApplicationContext()；** 调用下面的方法 
   4. 获取配置类：**getServletConfigClasses()；** 

也就是说，在Servlet3.0环境中，容器会在类路径中查找实现javax.servlet.ServletContainerInitializer接口的类，如果找到的话就用它来配置Servlet容器。
Spring提供了这个接口的实现，名为SpringServletContainerInitializer，这个类反过来又会查找实现WebApplicationInitializer的类并将配置的任务交给它们来完成。Spring3.2引入了一个便利的WebApplicationInitializer基础实现，名为AbstractAnnotationConfigDispatcherServletInitializer，当我们的类扩展了AbstractAnnotationConfigDispatcherServletInitializer并将其部署到Servlet3.0容器的时候，容器会自动发现它，并用它来配置Servlet上下文。

```java
public abstract class AbstractAnnotationConfigDispatcherServletInitializer extends AbstractDispatcherServletInitializer{.......}
```

```java
//web容器启动的时候创建对象；调用方法来初始化容器以前前端控制器
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	//获取根容器的配置类，（Spring的配置文件）父容器，下文中的RootConfig
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{RootConfig.class};
	}
	//获取web容器的配置类（SpringMVC配置文件）子容器，下文中的AppConfig
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{AppConfig.class};
	}
	//获取DispatcherServlet的映射信息
	//  /：拦截所有请求（包括静态资源（xx.js,xx.png）），但是不包括*.jsp；
	//  /*：拦截所有请求；连*.jsp页面都拦截；jsp页面是tomcat的jsp引擎解析的；
	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}
}
```

通过继承**WebMvcConfigurerAdapter**，实现方法来定制MVC

```java
//SpringMVC只扫描Controller，子容器
//useDefaultFilters=false 禁用默认的过滤规则
@ComponentScan(value="com.atguigu",includeFilters={
		@Filter(type=FilterType.ANNOTATION,classes={Controller.class})
},useDefaultFilters=false)
//开启MVC定制配置功能
@EnableWebMvc
@Configuration
public class AppConfig  extends WebMvcConfigurerAdapter  {
	//定制
	//视图解析器
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		//默认所有的页面都从 /WEB-INF/ xxx .jsp
		//registry.jsp();
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	//静态资源访问
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	//拦截器
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new MyFirstInterceptor()).addPathPatterns("/**");
	}
}
```

```java

//Spring的容器不扫描controller，父容器
@ComponentScan(value="com.atguigu",excludeFilters={
		@Filter(type=FilterType.ANNOTATION,classes={Controller.class})
})
@Configuration
public class RootConfig {

}
```

**总结**：
以注解方式来启动SpringMVC，首先要继承**AbstractAnnotationConfigDispatcherServletInitializer**，实现抽象方法指定DispatcherServlet的配置信息。

**定制Spring-MVC**：
1、**@EnableWebMvc**：开启SpringMVC定制配置功能相当于**\<mvc:annotation-driven/>**

2、配置组件（视图解析器、视图映射、静态资源映射、拦截器。。。）通过继承实现方法**extends WebMvcConfigurerAdapter** 

# 异步Spring-MVC

## Callable

1、首先方法需要返回的是**Callable**

2、Spring异步处理，将Callable 提交到 **TaskExecutor** 使用一个隔离的线程进行执行

3、**DispatcherServlet**和所有的**Filter**退出WEB容器的线程，但是**response** 保持打开状态

4、Callable返回结果，SpringMVC将请求**重新派发给容器**，恢复之前的处理

5、根据Callable返回的结果。SpringMVC继续进行视图渲染流程等（从收请求--->视图渲染）

6、流程：

```text
preHandle... url=》/springmvc-annotation/async01
主线程开始...Thread[http-bio-8081-exec-3,5,main]==>1513932494700
主线程结束...Thread[http-bio-8081-exec-3,5,main]==>1513932494700
=========DispatcherServlet及所有的Filter退出线程============================

================等待Callable执行==========
副线程开始...Thread[MvcAsync1,5,main]==>1513932494707
副线程开始...Thread[MvcAsync1,5,main]==>1513932496708
================Callable执行完成==========

================再次收到之前重发过来的请求========
preHandle...url=》/springmvc-annotation/async01
postHandle...（Callable的之前的返回值就是目标方法的返回值）
afterCompletion...
```

```java
@ResponseBody
@RequestMapping("/async01")
public Callable<String> async01(){
    System.out.println("主线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
	//创建一个Callable，执行异步处理
    Callable<String> callable = new Callable<String>() {
        @Override
        //异步处理
        public String call() throws Exception {
            System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
            Thread.sleep(2000);
            System.out.println("副线程开始..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
            return "Callable<String> async01()";
        }
    };
    System.out.println("主线程结束..."+Thread.currentThread()+"==>"+System.currentTimeMillis());
    //返回该Callable
    return callable;
}
```

## **DeferredResult**

**需求**：

- API接口需要在指定时间内将**异步操作的结果**同步返回给前端时

- Controller处理耗时任务，并且需要耗时任务的返回结果时

**过程**：当一个请求到达API接口，如果该API接口的return返回值是DeferredResult，在没有超时或者DeferredResult对象调用setResult()方法时，接口不会返回，但是Servlet容器线程会结束，DeferredResult另起线程来进行结果处理(即这种操作提升了**服务短时间的吞吐能力**)，并setResult，如此以来这个请求不会占用服务连接池太久，如果超时或设置setResult，接口会立即返回。

**流程**：

1. 定义一个DeferredResult
2. 然后在主线程中直接返回deferredResult结果，**此时servlet容器线程被释放，继续服务其他请求，以此提高吞吐量，后台任务线程执行耗时长的任务** 
3. 将任务放入队列中，后台定义一个专门执行任务的线程，循环执行队列中的任务
4. 执行完的任务，直接调用deferredResult.setResult()方法，即可将结果返回给客户端，和Callable、Future性质一样

异步的拦截器:

1. 原生API的**AsyncListener**
2. SpringMVC：实现**AsyncHandlerInterceptor**

****

# 扩展

## Spring-MVC运行流程

![image-20210923165259667](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923165259667.png)

### SpringMVC常用组件

- DispatcherServlet：**前端控制器**，由框架提供

作用：统一处理请求和响应，整个流程控制的中心，由它调用其它组件处理用户的请求

- HandlerMapping：**处理器映射器**，由框架提供

作用：根据请求的url、method等信息查找Handler，即控制器方法

- Handler：**处理器**，需要工程师开发

作用：在DispatcherServlet的控制下Handler对具体的用户请求进行处理

- HandlerAdapter：**处理器适配器**，由框架提供

作用：通过HandlerAdapter对处理器（控制器方法）进行执行

- ViewResolver：**视图解析器**，由框架提供

作用：进行视图解析，得到相应的视图，例如：ThymeleafView、InternalResourceView、RedirectView

- View：**视图**

作用：将模型数据通过页面展示给用户

### DispatcherServlet初始化过程

DispatcherServlet 本质上是一个 Servlet，所以天然的遵循 Servlet 的生命周期。所以宏观上是 Servlet 生命周期来进行调度。

![images](C:\Users\zzp84\Desktop\Spring笔记\Spring-MVC笔记\images\Spring-MVC.assets\img005.png)

##### 1、初始化WebApplicationContext

所在类：org.springframework.web.servlet.FrameworkServlet

```java
protected WebApplicationContext initWebApplicationContext() {
    WebApplicationContext rootContext =
        WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    WebApplicationContext wac = null;

    if (this.webApplicationContext != null) {
        // A context instance was injected at construction time -> use it
        wac = this.webApplicationContext;
        if (wac instanceof ConfigurableWebApplicationContext) {
            ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
            if (!cwac.isActive()) {
                // The context has not yet been refreshed -> provide services such as
                // setting the parent context, setting the application context id, etc
                if (cwac.getParent() == null) {
                    // The context instance was injected without an explicit parent -> set
                    // the root application context (if any; may be null) as the parent
                    cwac.setParent(rootContext);
                }
                configureAndRefreshWebApplicationContext(cwac);
            }
        }
    }
    if (wac == null) {
        // No context instance was injected at construction time -> see if one
        // has been registered in the servlet context. If one exists, it is assumed
        // that the parent context (if any) has already been set and that the
        // user has performed any initialization such as setting the context id
        wac = findWebApplicationContext();
    }
    if (wac == null) {
        // No context instance is defined for this servlet -> create a local one
        // 创建WebApplicationContext
        wac = createWebApplicationContext(rootContext);
    }

    if (!this.refreshEventReceived) {
        // Either the context is not a ConfigurableApplicationContext with refresh
        // support or the context injected at construction time had already been
        // refreshed -> trigger initial onRefresh manually here.
        synchronized (this.onRefreshMonitor) {
            // 刷新WebApplicationContext
            onRefresh(wac);
        }
    }

    if (this.publishContext) {
        // Publish the context as a servlet context attribute.
        // 将IOC容器在应用域共享
        String attrName = getServletContextAttributeName();
        getServletContext().setAttribute(attrName, wac);
    }

    return wac;
}
```

##### 2、创建WebApplicationContext

所在类：org.springframework.web.servlet.FrameworkServlet

```java
protected WebApplicationContext createWebApplicationContext(@Nullable ApplicationContext parent) {
    Class<?> contextClass = getContextClass();
    if (!ConfigurableWebApplicationContext.class.isAssignableFrom(contextClass)) {
        throw new ApplicationContextException(
            "Fatal initialization error in servlet with name '" + getServletName() +
            "': custom WebApplicationContext class [" + contextClass.getName() +
            "] is not of type ConfigurableWebApplicationContext");
    }
    // 通过反射创建 IOC 容器对象
    ConfigurableWebApplicationContext wac =
        (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

    wac.setEnvironment(getEnvironment());
    // 设置父容器
    wac.setParent(parent);
    String configLocation = getContextConfigLocation();
    if (configLocation != null) {
        wac.setConfigLocation(configLocation);
    }
    configureAndRefreshWebApplicationContext(wac);

    return wac;
}
```

##### 3、DispatcherServlet初始化策略

FrameworkServlet创建WebApplicationContext后，刷新容器，调用onRefresh(wac)，此方法在DispatcherServlet中进行了重写，调用了initStrategies(context)方法，初始化策略，即初始化DispatcherServlet的各个组件

所在类：org.springframework.web.servlet.DispatcherServlet

```java
protected void initStrategies(ApplicationContext context) {
   initMultipartResolver(context);
   initLocaleResolver(context);
   initThemeResolver(context);
   initHandlerMappings(context);
   initHandlerAdapters(context);
   initHandlerExceptionResolvers(context);
   initRequestToViewNameTranslator(context);
   initViewResolvers(context);
   initFlashMapManager(context);
}
```

### DispatcherServlet调用组件处理请求

##### 1、processRequest()

FrameworkServlet重写HttpServlet中的service()和doXxx()，这些方法中调用了processRequest(request, response)

所在类：org.springframework.web.servlet.FrameworkServlet

```java
protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    long startTime = System.currentTimeMillis();
    Throwable failureCause = null;

    LocaleContext previousLocaleContext = LocaleContextHolder.getLocaleContext();
    LocaleContext localeContext = buildLocaleContext(request);

    RequestAttributes previousAttributes = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes requestAttributes = buildRequestAttributes(request, response, previousAttributes);

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
    asyncManager.registerCallableInterceptor(FrameworkServlet.class.getName(), new RequestBindingInterceptor());

    initContextHolders(request, localeContext, requestAttributes);

    try {
		// 执行服务，doService()是一个抽象方法，在DispatcherServlet中进行了重写
        doService(request, response);
    }
    catch (ServletException | IOException ex) {
        failureCause = ex;
        throw ex;
    }
    catch (Throwable ex) {
        failureCause = ex;
        throw new NestedServletException("Request processing failed", ex);
    }

    finally {
        resetContextHolders(request, previousLocaleContext, previousAttributes);
        if (requestAttributes != null) {
            requestAttributes.requestCompleted();
        }
        logResult(request, response, failureCause, asyncManager);
        publishRequestHandledEvent(request, response, startTime, failureCause);
    }
}
```

##### 2、doService()

所在类：org.springframework.web.servlet.DispatcherServlet

```java
@Override
protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
    logRequest(request);

    // Keep a snapshot of the request attributes in case of an include,
    // to be able to restore the original attributes after the include.
    Map<String, Object> attributesSnapshot = null;
    if (WebUtils.isIncludeRequest(request)) {
        attributesSnapshot = new HashMap<>();
        Enumeration<?> attrNames = request.getAttributeNames();
        while (attrNames.hasMoreElements()) {
            String attrName = (String) attrNames.nextElement();
            if (this.cleanupAfterInclude || attrName.startsWith(DEFAULT_STRATEGIES_PREFIX)) {
                attributesSnapshot.put(attrName, request.getAttribute(attrName));
            }
        }
    }

    // Make framework objects available to handlers and view objects.
    request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
    request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
    request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
    request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());

    if (this.flashMapManager != null) {
        FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
        if (inputFlashMap != null) {
            request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
        }
        request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
        request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
    }

    RequestPath requestPath = null;
    if (this.parseRequestPath && !ServletRequestPathUtils.hasParsedRequestPath(request)) {
        requestPath = ServletRequestPathUtils.parseAndCache(request);
    }

    try {
        // 处理请求和响应
        doDispatch(request, response);
    }
    finally {
        if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
            // Restore the original attribute snapshot, in case of an include.
            if (attributesSnapshot != null) {
                restoreAttributesAfterInclude(request, attributesSnapshot);
            }
        }
        if (requestPath != null) {
            ServletRequestPathUtils.clearParsedRequestPath(request);
        }
    }
}
```

##### 3、doDispatch()

所在类：org.springframework.web.servlet.DispatcherServlet

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request;
    HandlerExecutionChain mappedHandler = null;
    boolean multipartRequestParsed = false;

    WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

    try {
        ModelAndView mv = null;
        Exception dispatchException = null;

        try {
            processedRequest = checkMultipart(request);
            multipartRequestParsed = (processedRequest != request);

            // Determine handler for the current request.
            /*
            	mappedHandler：调用链
                包含handler、interceptorList、interceptorIndex
            	handler：浏览器发送的请求所匹配的控制器方法
            	interceptorList：处理控制器方法的所有拦截器集合
            	interceptorIndex：拦截器索引，控制拦截器afterCompletion()的执行
            */
            mappedHandler = getHandler(processedRequest);
            if (mappedHandler == null) {
                noHandlerFound(processedRequest, response);
                return;
            }

            // Determine handler adapter for the current request.
           	// 通过控制器方法创建相应的处理器适配器，调用所对应的控制器方法
            HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());

            // Process last-modified header, if supported by the handler.
            String method = request.getMethod();
            boolean isGet = "GET".equals(method);
            if (isGet || "HEAD".equals(method)) {
                long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
                if (new ServletWebRequest(request, response).checkNotModified(lastModified) && isGet) {
                    return;
                }
            }
			
            // 调用拦截器的preHandle()
            if (!mappedHandler.applyPreHandle(processedRequest, response)) {
                return;
            }

            // Actually invoke the handler.
            // 由处理器适配器调用具体的控制器方法，最终获得ModelAndView对象
            mv = ha.handle(processedRequest, response, mappedHandler.getHandler());

            if (asyncManager.isConcurrentHandlingStarted()) {
                return;
            }

            applyDefaultViewName(processedRequest, mv);
            // 调用拦截器的postHandle()
            mappedHandler.applyPostHandle(processedRequest, response, mv);
        }
        catch (Exception ex) {
            dispatchException = ex;
        }
        catch (Throwable err) {
            // As of 4.3, we're processing Errors thrown from handler methods as well,
            // making them available for @ExceptionHandler methods and other scenarios.
            dispatchException = new NestedServletException("Handler dispatch failed", err);
        }
        // 后续处理：处理模型数据和渲染视图
        processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
    }
    catch (Exception ex) {
        triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
    }
    catch (Throwable err) {
        triggerAfterCompletion(processedRequest, response, mappedHandler,
                               new NestedServletException("Handler processing failed", err));
    }
    finally {
        if (asyncManager.isConcurrentHandlingStarted()) {
            // Instead of postHandle and afterCompletion
            if (mappedHandler != null) {
                mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
            }
        }
        else {
            // Clean up any resources used by a multipart request.
            if (multipartRequestParsed) {
                cleanupMultipart(processedRequest);
            }
        }
    }
}
```

##### 4、processDispatchResult()

```java
private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
                                   @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
                                   @Nullable Exception exception) throws Exception {

    boolean errorView = false;

    if (exception != null) {
        if (exception instanceof ModelAndViewDefiningException) {
            logger.debug("ModelAndViewDefiningException encountered", exception);
            mv = ((ModelAndViewDefiningException) exception).getModelAndView();
        }
        else {
            Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
            mv = processHandlerException(request, response, handler, exception);
            errorView = (mv != null);
        }
    }

    // Did the handler return a view to render?
    if (mv != null && !mv.wasCleared()) {
        // 处理模型数据和渲染视图
        render(mv, request, response);
        if (errorView) {
            WebUtils.clearErrorRequestAttributes(request);
        }
    }
    else {
        if (logger.isTraceEnabled()) {
            logger.trace("No view rendering, null ModelAndView returned.");
        }
    }

    if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted()) {
        // Concurrent handling started during a forward
        return;
    }

    if (mappedHandler != null) {
        // Exception (if any) is already handled..
        // 调用拦截器的afterCompletion()
        mappedHandler.triggerAfterCompletion(request, response, null);
    }
}
```

### SpringMVC的执行流程

1) 用户向服务器发送请求，请求被SpringMVC 前端控制器 DispatcherServlet捕获。

2) DispatcherServlet对请求URL进行解析，得到请求资源标识符（URI），判断请求URI对应的映射：

a) 不存在

i. 再判断是否配置了mvc:default-servlet-handler

ii. 如果没配置，则控制台报映射查找不到，客户端展示404错误

![image-20210709214911404](C:\Users\zzp84\Desktop\Spring笔记\Spring-MVC笔记\images\Spring-MVC.assets\img006.png)

![image-20210709214947432](C:\Users\zzp84\Desktop\Spring笔记\Spring-MVC笔记\images\Spring-MVC.assets\img007.png)

iii. 如果有配置，则访问目标资源（一般为静态资源，如：JS,CSS,HTML），找不到客户端也会展示404错误

![image-20210709215255693](C:\Users\zzp84\Desktop\Spring笔记\Spring-MVC笔记\images\Spring-MVC.assets\img008.png)

![image-20210709215336097](C:\Users\zzp84\Desktop\Spring笔记\Spring-MVC笔记\images\Spring-MVC.assets\img009.png)

b) 存在则执行下面的流程

3) 根据该URI，调用HandlerMapping获得该Handler配置的所有相关的对象（包括Handler对象以及Handler对象对应的拦截器），最后以HandlerExecutionChain执行链对象的形式返回。

4) DispatcherServlet 根据获得的Handler，选择一个合适的HandlerAdapter。

5) 如果成功获得HandlerAdapter，此时将开始执行拦截器的preHandler(…)方法【正向】

6) 提取Request中的模型数据，填充Handler入参，开始执行Handler（Controller)方法，处理请求。在填充Handler的入参过程中，根据你的配置，Spring将帮你做一些额外的工作：

a) HttpMessageConveter： 将请求消息（如Json、xml等数据）转换成一个对象，将对象转换为指定的响应信息

b) 数据转换：对请求消息进行数据转换。如String转换成Integer、Double等

c) 数据格式化：对请求消息进行数据格式化。 如将字符串转换成格式化数字或格式化日期等

d) 数据验证： 验证数据的有效性（长度、格式等），验证结果存储到BindingResult或Error中

7) Handler执行完成后，向DispatcherServlet 返回一个ModelAndView对象。

8) 此时将开始执行拦截器的postHandle(...)方法【逆向】。

9) 根据返回的ModelAndView（此时会判断是否存在异常：如果存在异常，则执行HandlerExceptionResolver进行异常处理）选择一个适合的ViewResolver进行视图解析，根据Model和View，来渲染视图。

10) 渲染视图完毕执行拦截器的afterCompletion(…)方法【逆向】。

11) 将渲染结果返回给客户端。

## @ModelAttribute源码流程分析

1. 首先调用@ModelAttribute注解修饰的方法，若采用通过入参处使用**Model类型或者Map类型**写入数据，会把Map中的数据放入implicitModel中。
2. 解析请求处理器的目标参数，该目标参数来自于**WebDataBinder**对象的target属性。
   1. 创建WebDataBinder对象：
      1. 确定objectName属性值：若传入的attrName属性值为“ ”，则该值为类名第一个字母小写，若目标方法的POJO属性使用了@ModelAttribute修饰，则attrName值为@ModelAttribute的value属性值
      2. 确定target属性：在implicitModel中查找attrName对应的属性值。若存在，取出。若不存在，检查当前处理类Handler是否使用了@SessionAttributes注解修饰。若使用了，尝试从Session中取出attrName对应的属性值。若没有对应属性值，抛出异常。若没有使用@SessionAttributes注解修饰，或@SessionAttributes中没有value值的key和attrName匹配，通过反射创建一个新的POJO对象
   2. Spring-MVC把表单的请求参数赋给WebDataBinder的target对应的属性。
   3. Spring-MVC把WebDataBinder的attrName和target给到implicitModel，进而传到request域对象中。
   4. 把WebDataBinder的target作为参数传递给目标方法的入参。

## 关于\<mvc:annotation-driven/>

- 会自动注册**RequestMappingHandlerMapping**、**RequestMappingHandlerAdapter**、**ExceptionHandlerExceptionResolver**三个bean

- 还将提供一下支持：
  - 支持使用 ConversionService 实例对表单参数进行类型转换
  - 支持使用 @NumberFormat annotation、@DateTimeFormat注解完成数据类型的格式化
  - 支持使用 @Valid 注解对 JavaBean 实例进行 JSR 303 验证
  - 支持使用 @RequestBody 和 @ResponseBody 注解

![image-20210923105518249](C:\Users\zzp84\Desktop\Spring-MVC笔记\images\Spring-MVC.assets\image-20210923105518249.png)

## 隐含模型

1、首先解释**Model**、**ModelMap**、**Map**、**ModelAndView**的关系

- Model是一个**接口**。

![image-20210923164640345](images\Spring-MVC.assets\image-20210923164640345.png)

![image-20210923164643964](images\Spring-MVC.assets\image-20210923164643964.png)

- ModelMap是一个**LinkedHashMap**实现类。

![image-20210923164703156](images\Spring-MVC.assets\image-20210923164703156.png)

- ModelAndView：**有一个属性为Model**，在本身初始化后，该属性为空，当调用它增加数据模型的方法时，会自动创建一个ModelMap实例，用于保存数据。

2、隐含模型对象作为模型数据的存储容器：在控制器方法中，可以将**ModelAndView**、**Model**、**ModelMap**、**Map**作为参数，在Spring-MVC运行时，会自动初始化，新建一个**BindAwareModelMap**实例。

![image-20210923164852109](images\Spring-MVC.assets\image-20210923164852109.png)

3、Spring-MVC在内部使用了一个**org.springframework.ui.Model**接口存储模型

![image-20210923164915345](images\Spring-MVC.assets\image-20210923164915345.png)

- 如果方法入参为**Map**或者**Model**类型，Sping-MVC会将隐含模型的引用传递给入参，之后在方法体内开发者可以通过这个入参对象访问/修改到模型中的数据。

## HandlerExceptionResolver异常处理器使用详解

### 古老的异常处理方式

在web.xml中配置

```xml
<!-- 根据状态码 -->
<error-page>
    <error-code>500</error-code>
    <location>/500.jsp</location>
</error-page>

<!-- 根据异常类型 -->
<error-page>
	<exception-type>java.lang.RuntimeException</exception-type>
	<location>/500.jsp</location>
</error-page>

```

### Spring MVC处理异常

Spring MVC提供处理异常的方式主要分为两种：

- 实现HandlerExceptionResolver方式（不推荐使用它）
- @ExceptionHandler注解方式。注解方式也有两种用法：

1. 使用在Controller内部
2. 配置@ControllerAdvice一起使用实现全局处理（推荐）

本次仅讲解HandlerExceptionResolver

```java
// @since 22.11.2003
public interface HandlerExceptionResolver {
	// 注意：handler是有可能为null的，比如404
	@Nullable
	ModelAndView resolveException(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  @Nullable Object handler, 
                                  Exception ex);
}

```

处理方法返回一个`ModelAndView`视图：既可以是json，也可以是页面。从接口参数上可以发现的是：它只能处理`Exception`，因为`Error`是程序处理不了的（**注意：`Error`也是可以捕获的**），因此入参类型若写成`Throwable`是不合适的。

![image-20211106174304511](images/image-20211106174304511.png)

#### AbstractHandlerExceptionResolver

所有其它子类的实现都是此抽象类的子类，所以若我们自定义异常处理器，推荐从此处去继承，它是`Spring3.0`后才有的。它主要是提供了对异常更细粒度的控制：此`Resolver`可只处理指定类型的异常。

```java
// @since 3.0
public abstract class AbstractHandlerExceptionResolver implements HandlerExceptionResolver, Ordered {
	...
	private int order = Ordered.LOWEST_PRECEDENCE;
	
	// 可以设置任何的handler，表示只作用于这些Handler们
	@Nullable
	private Set<?> mappedHandlers;
	// 表示只作用域这些Class类型的Handler们~~~
	@Nullable
	private Class<?>[] mappedHandlerClasses;
	// 以上两者若都为null，那就是匹配素有。但凡有一个有值，那就需要精确匹配（并集的关系）
	
	... // 省略所有的get/set方法

	@Override
	@Nullable
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {

		// 这个作用匹配逻辑很简答
		// 若mappedHandlers和mappedHandlerClasses都为null永远返回true
		// 但凡配置了一个就需要精确匹配（并集关系）
		// 需要注意的是：shouldApplyTo方法，子类AbstractHandlerMethodExceptionResolver是有复写的
		if (shouldApplyTo(request, handler)) {
			// 是否执行；response.addHeader(HEADER_CACHE_CONTROL, "no-store")  默认是不执行的
			prepareResponse(ex, response);
			// 此抽象方法留给子类去完成~~~~~
			ModelAndView result = doResolveException(request, response, handler, ex);
			return result;
		} else { // 若此处理器不处理，就返回null呗
			return null;
		}
	}
}

```

此抽象类主要是提供setMappedHandlers和setMappedHandlerClasses让此处理器可以作用在指定类型/处理器上，因此子类只要继承了它都将会有这种能力，这也是为何推荐自定义实现也继承于它的原因。它提供了shouldApplyTo()方法用于匹配逻辑，子类若想定制化匹配规则，亦可复写此方法。

#### SimpleMappingExceptionResolver

顾名思义就是通过简单映射关系来决定由哪个错误视图来处理当前的异常信息。它提供了多种映射关系可以使用：

- 通过异常类型Properties exceptionMappings;映射。它的key可以是全类名、短名称。
  - 同时还有继承效果：比如key是Exception那将匹配所有的异常。value是view name视图名称
  - 若有需要，可以配合Class<?>[] excludedExceptions来一起使用
- 通过状态码Map<String, Integer> statusCodes匹配。key是view name，value是http状态码

部分源码

```java
SimpleMappingExceptionResolver：
	@Override
	@Nullable
	protected ModelAndView doResolveException(
			HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {

		// 根据异常类型去exceptionMappings匹配到一个viewName
		// 实在木有匹配到，就用的defaultErrorView（当然defaultErrorView也可能为null没配置，不过建议配置）
		String viewName = determineViewName(ex, request);
		if (viewName != null) {
			// 如果匹配上了一个视图后，再去使用视图匹配出一个statusCode
			// 若没匹配上就用defaultStatusCode（当然它也有可能为null）
			Integer statusCode = determineStatusCode(request, viewName);
			if (statusCode != null) {
				//	执行response.setStatus(statusCode)
				applyStatusCodeIfPossible(request, response, statusCode);
			}
			// new ModelAndView(viewName) 设置好viewName
			// 并且，并且，并且：mv.addObject(this.exceptionAttribute, ex)把异常信息放进去。exceptionAttribute的值默认为：exception
			return getModelAndView(viewName, ex, request);
		} else {
			return null;
		}
	}

```

#### ResponseStatusExceptionResolver

若抛出的**异常类型**上有`@ResponseStatus`注解，那么此处理器就会处理，并且状态码会返给response。`Spring5.0`还能处理`ResponseStatusException`这个异常（此异常是5.0新增）。

```java
// 实现了接口MessageSourceAware，方便拿到国际化资源，方便错误消息的国际化
// @since 3.0
public class ResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver implements MessageSourceAware {

	@Nullable
	private MessageSource messageSource;
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	@Override
	@Nullable
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
		try {
			// 若异常类型是，那就处理这个异常
			// 处理很简单：response.sendError(statusCode, resolvedReason)
			// 当然会有国际化消息的处理。最终new一个空的new ModelAndView()供以返回
			if (ex instanceof ResponseStatusException) {
				return resolveResponseStatusException((ResponseStatusException) ex, request, response, handler);
			}

			// 若异常类型所在的类上标注了ResponseStatus注解，就处理这个状态码
			//（可见：异常类型优先于ResponseStatus）
			// 处理方式同上~~~~
			ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
			if (status != null) {
				return resolveResponseStatus(status, request, response, handler, ex);
			}

			// 这里有个递归：如果异常类型是Course里面的，也会继续处理，所以需要注意这里的递归处理
			if (ex.getCause() instanceof Exception) {
				return doResolveException(request, response, handler, (Exception) ex.getCause());
			}
		} catch (Exception resolveEx) { // 处理失败，就记录warn日志（非info哦~）
			if (logger.isWarnEnabled()) {
				logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", resolveEx);
			}
		}
		return null;
	}
}

```

这里有个处理的小细节：递归调用了doResolveException()方法，也就是说若有coouse原因也是异常，那就继续会尝试处理的。
另外请注意：@ResponseStatus标注在异常类上此处理器才会处理，而不是标注在处理方法上，或者所在类上哦，所以一般用于自定义异常时使用。

#### DefaultHandlerExceptionResolver

默认的异常处理器。它能够处理标准的`Spring MVC`异常们，**并且把它转换为对应的HTTP status codes**，一般作为兜底处理，`Spring MVC`默认也注册了此处理器。它能处理的异常非常之多，简单列出来如下：

![image-20211106175447063](images/image-20211106175447063.png)

```java
// @since 3.0
public class DefaultHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
	public DefaultHandlerExceptionResolver() {
		setOrder(Ordered.LOWEST_PRECEDENCE);
		setWarnLogCategory(getClass().getName()); // 不同的日志采用不同的记录器是个很好的习惯
	}

	@Override
	@Nullable
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
		try {
			if (ex instanceof HttpRequestMethodNotSupportedException) {
				return handleHttpRequestMethodNotSupported(
						(HttpRequestMethodNotSupportedException) ex, request, response, handler);
			} else if (ex instanceof HttpMediaTypeNotSupportedException) {
				return handleHttpMediaTypeNotSupported(
						(HttpMediaTypeNotSupportedException) ex, request, response, handler);
			} ... // 省略其它的else if
			// 多有的handle方法几乎一样的，都是response.sendError（）
			// 有的还会response.setHeader("Accept", MediaType.toString(mediaTypes));等等
	}
}

```

它对这些异常的处理，亦可参考内置的`ResponseEntityExceptionHandler`实现，它提供了基于`@ExceptionHandler`的很多异常类型的处理。

#### 自定义`HandlerExceptionResolver`处理异常

```java
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        // 自定义异常处理器一般请放在首位
        exceptionResolvers.add(0, new AbstractHandlerExceptionResolver() {
            @Override
            protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                // 若是自定义的业务异常，那就返回到单页面异常页面
                if (ex instanceof BusinessException) {
                    return new ModelAndView("/business.jsp");
                } else { // 否则统一到统一的错误页面
                    return new ModelAndView("/error.jsp");
                }
            }
        });
    }
}

```



# 问题

## Spring环境下使用Spring-MVC

1、 Bean被创建两次？

- Spring的IOC容器不应该扫描Spring-MVC中的Bean，对应的Spring-MVC的IOC容器不应该扫描Spring中的Bean

2、 Spring-MVC 配置文件中引用业务层的 Bean？

- 多个Spring IOC容器之间可以设置为父子关系，实现良好的解耦
- Spring-MVC WEB层容器可作为业务层，Spring容器的子容器：即 WEB 层容器可以引用业务层容器的Bean，而业务层容器却访问不到 WEB 层容器的 Bean。

## 由@SessionAttributes引发的异常

如果在处理类定义处标注了@SessionAttributes(“xxx”)，则尝试从会话中获取该属性，并将其赋给该入参，然后再用请求消息填充该入参对象。如果在会话中找不到对应的属性，则抛出 HttpSessionRequiredException 异常。

**解决办法**：

```java
@ModelAttribute("user") 
public User getUser( ) {
    User user = new User( ); 
    return user; 
}
```

```java
//参数 
@ModelAttribute("xxx") User user
```

## @ModelAttribute为什么放在返回值是空的方法上要以Map作为参数

@ModelAttribute主要作用是将数据存入模型对象中，等价于model.addAttribute（“xx”，“yy”），所以如果被该注解修饰的方法的入参含有map或者model，可以void返回，因为map已经put了，如果没有map或者model，则需要返回。






































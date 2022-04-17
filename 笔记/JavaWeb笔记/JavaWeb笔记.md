# 总体系结构

***

![image-20220123204818284](images/image-20220123204818284.png)

![image-20220123204801885](images/image-20220123204801885.png)

# XML

***

## 第一章 XML简介

> “当 XML（扩展标记语言）于 1998 年 2 月被引入软件工业界时，它给整个行业带来了一场风暴。有史以来第一次，这个世界拥有了一种用来结构化文档和数据的通用且适应性强的格式，它不仅仅可以用于 Web，而且可以被用于任何地方。”
>
> ​                                                           --《Designing With Web Standards Second Edition》, Jeffrey Zeldman 

我们不同的平台有他自己的数据格式，但是不同平台之间如果相互想传递数据，那么就应该用同一种数据格式，这样大家都能读懂。就像加入WTO组织的各个国家一样。每个国家都有自己的语言和货币，但是如果大家都用自己的东西就很难沟通和衡量。那么我们就使用统一的方式，使用英语作为交流语言，使用美元作为货币标准。

### 1.1 什么是XML

- XML：e**X**tensible **M**arkup **L**anguage  （可扩展标记语言）。

- 由W3C组织发布，目前推荐遵守的是W3C组织于2000年发布的XML1.0规范。

- XML 是一种标记语言，很类似 HTML。

- XML 的设计宗旨是传输数据，而非显示数据。

- XML的使命，就是以一个统一的格式，组织有关系的数据。为不同平台下的应用程序服务，独立于软件和硬件的信息传输工具。

- XML 仅仅是纯文本。有能力处理纯文本的软件都可以处理 XML。不过，能够读懂 XML 的应用程序可以有针对性地处理 XML 的标签。

  ```xml
  <!--下面是 John 写给 George 的便签，存储为 XML： -->
  
  <note>
  	<to>George</to>
  	<from>John</from>
  	<heading>Reminder</heading>
  	<body>Don't forget the meeting!</body>
  </note>
  ```

### 1.2 XML  vs  HTML

- XML 不是 HTML 的替代。
- XML 和 HTML 为不同的目的而设计：HTML 旨在显示信息，而 XML 旨在传输信息
  - 传统中，XML 被设计为传输和存储数据，其焦点是数据的内容。
  - HTML 被设计用来显示数据，其焦点是数据的外观。
- 关于内部标签：
  - **XML没有任何预定义标签，均为自定义标签**。
  - HTML 文档只使用在 HTML 标准中定义过的标签。
- 树形结构：
  - XML 文档形成一种树形结构，这点上与HTML是相同的。
- 语法要求：
  - XML相较于HTML，语法更加严格。
  - HTML不区分大小写，XML严格区分大小写。

## 第2章 XML用途

### 2.1 配置文件

- JavaWeb中的web.xml，以及后面涉及到的框架都会有很多的配置文件
- C3P0中的c3p0-config.xml
- JDK安装后的目录文件

### 2.2 数据交换格式

- Ajax

- WebService

![1556726712329](images/1556726712329.png)

### 2.3 数据存储

- 保存关系型数据

  举例1：

![1557487578710](images/1557487578710.png)

```xml
<?xml version="1.0" encoding="utf-8"?>
<中国>
	<北京>
		<海淀></海淀>
		<丰台></丰台>
	</北京>
	<湖南>
		<长沙></长沙>
		<岳阳></岳阳>
	</湖南>
	<湖北>
		<武汉></武汉>
		<荆州></荆州>
	</湖北>
</中国>
```

举例2：

![1557492855132](images/1557492855132.png)

> ：为什么我们要使用xml保存数据呢？比如c3p0的配置文件。因为有些数据是不会经常变化的数据，我们需要固定的保存起来，然而这些数据又是有某些关系的。我们希望也把他们直接的关系用简明易懂的格式保存起来，方便后来查看这些数据的时候一下就能看懂他们直接的关系。



### 2.4 移动端android页面

如下是android某应用的登录界面代码：【activity_login.xml】

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <include layout="@layout/common_title"/>

    <RelativeLayout
        android:id="@+id/rl_login"
        android:layout_width="match_parent"
        android:layout_height="44dp"
        android:background="@color/white"
        android:paddingLeft="17dp"
        android:paddingRight="17dp">

        <TextView
            android:id="@+id/tv_login_number"
            android:layout_width="95dp"
            android:layout_height="wrap_content"
            android:layout_centerVertical="true"
            android:text="手机号:"
            android:textColor="#333333"
            android:textSize="14sp"/>

        <EditText
            android:id="@+id/et_login_number"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_centerVertical="true"
            android:layout_toRightOf="@id/tv_login_number"
            android:background="@null"
            android:gravity="center_vertical"
            android:hint="请输入手机号"
            android:inputType="phone"
            android:maxLines="1"
            android:singleLine="true"
            android:textColor="#5c5c5c"
            android:textSize="14sp"/>
    </RelativeLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="?android:listDivider"/>

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="44dp"
        android:background="@color/white"
        android:paddingLeft="17dp"
        android:paddingRight="17dp">

        <TextView
            android:id="@+id/tv_login_pwd"
            android:layout_width="95dp"
            android:layout_height="wrap_content"
            android:layout_alignParentLeft="true"
            android:layout_centerVertical="true"
            android:text="密码:"
            android:textColor="#333333"
            android:textSize="14sp"/>

        <EditText
            android:id="@+id/et_login_pwd"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_centerVertical="true"
            android:layout_toRightOf="@id/tv_login_pwd"
            android:hint="请输入密码"
            android:background="@null"
            android:inputType="textPassword"
            android:maxLines="1"
            android:singleLine="true"
            android:textColor="#5c5c5c"
            android:textSize="14sp"/>
    </RelativeLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="?android:listDivider"/>

    <Button
        android:id="@+id/btn_login"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginLeft="20dp"
        android:layout_marginRight="20dp"
        android:layout_marginTop="15dp"
        android:background="@drawable/btn_01"
        android:text="登　录"
        android:textColor="#ffffff"
        android:textSize="18sp"/>
</LinearLayout>
```



## 第3章 XML基本语法

### 3.1 XML文档结构

- 举例

  ![1556728924678](images/1556728924678.png)

#### 3.1.1 XML文档声明

- version属性指定XML版本，固定值是**1.0。**
  - 必须在文件的第一行书写文档声明。
  - 最简单的声明语法：\<?xml version="1.0" ?>
- encoding指定的字符集，是**告诉解析器使用什么字符集进行解码**，而编码是由文本编辑器决定的
  - 通常的写法：**\<?xml version="1.0" encoding="utf-8" ?>** 
  - 这样就要求保存文件时，必须用utf-8编码保存。此时要求XML文档的作者确认当前编辑器保存文档的编码方式。
  - eclipse会自动按照解码字符集进行编码保存。记事本需要另存为指定的字符集。
- **xml文档声明如果声明，必须严格遵循语法。当然，也可以不声明。**

#### 3.1.2 CDATA区

- 当XML文档中需要写一些程序代码、SQL语句或其他**不希望XML解析器进行解析的内容**时，就可以写在CDATA区中

- XML解析器会将CDATA区中的内容原封不动的输出

- CDATA区的定义格式：\<![CDATA[…]]>

- 例如：

  ![1557489132945](images/1557489132945.png)

#### 3.1.3 注 释

XML文件中的注释采用：<!--注释-->

#### 3.1.4 处理指令

① 处理指令，简称PI （processing instruction）。处理指令用来指挥解析引擎如何解析XML文档内容。

② 处理指令必须以 \<? 作为开头，以 ?> 作为结尾，XML声明语句就是最常见的一种处理指令。 

例如，在XML文档中可以使用xml-stylesheet指令，通知XML解析引擎，应用xsl文件显示xml文档内容。

```xml
<?xml-stylesheet type="text/css" href="p.xsl"?>
```

③ XSL对于XML来说就相当于CSS对HTML一样，XSL是XML的文件样式。

XSL 指扩展样式表语言（*E*Xtensible *S*tylesheet *L*anguage）。

| 文件 | 样式文件 |
| ---- | -------- |
| XML  | XSL      |
| HTML | CSS      |

#### 3.1.5 元素

- 详情见下

### 3.2 语法规则

① XML声明要么不写，要写就写在第一行，并且前面没有任何其他字符

② 只能有一个根标签，所有的其他标签都必须声明在根标签内部

③ 标签必须正确结束，不能交叉嵌套

⑤ 严格区分大小写

⑥ 开始标签中，可以自定义添加属性。但，如果有属性就必须有值，且必须加引号

⑦ 标签不能以数字开头

⑧ 注释不能嵌套

⑨ XML中可以使用HTML中的转义字符

- 举例：

![1556728908971](images/1556728908971.png)

### 3.3 转义字符

| 特殊字符 | 替代符号 |
| -------- | -------- |
| <        | \&lt;    |
| >        | \&gt;    |
| &        | &amp ;   |
| "        | \&quot;  |
| ‘        | \&aops;  |

XML实体中不允许出现"&","<",">"等特殊字符,否则XML语法检查时将出错,如果编写的XML文件必须包含这些字符,则必须分别写成"&amp;","&lt;","&gt;"再写入文件中。例如，如果在XML文档中使用类似"<" 的字符, 那么解析器将会出现错误，因为解析器会认为这是一个新元素的开始。所以不应该像下面那样书写代码: 

```xml
<age> age < 30 </age>   
<!--这种写法会解析出现错误，因为<,>等等都是关键字符，除过标签以外，不允许占用的。-->
<age> age &lt; 30 </age> 

```

练习：

显示如下一对标签：\<tcode>\</tcode>。标签内的文本内容为：\<script>我喜欢写js代码\<script>

```
<tcode>&lt;script&gt;我喜欢写js代码&lt;script&gt;</tcode>
```



## 第4章 XML解析

- XML解析是指通过解析器读取XML文档，解释语法，并将文档转化成对象
- 对XML的一切操作都是由解析开始的，所以解析非常重要。
- Java 平台同时提供了 DOM（Document Object Model）和 SAX（Simple API for XML）。
  - DOM是 W3C 组织推荐的处理 XML 的一种方式。
  - SAX不是官方标准，但它是 XML 社区实际上的标准，几乎所有的 XML 解析器都支持它。

### 4.1 XML解析技术体系

![1556727035805](images/1556727035805.png)

> DOM解析需要重点掌握，相对简单，且效率高。后台多采用此种方式解析。
>
> SAX解析了解，移动平台使用较多。

### 4.2 DOM和SAX对比

| 对比项   | DOM (Document Object Model)                                  | SAX （Simple API for XML）                                   |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 速度     | 需要一次性加载整个XML文档，然后将文档转换为DOM树，速度较差   | 顺序解析XML文档，无需将整个XML都加载到内存中，速度快         |
| 重复访问 | 将XML转换为DOM树之后，在解析时，DOM树将常驻内存，可以重复访问。 | 顺序解析XML文档，已解析过的数据，如果没有保存，将不能获得，除非重新解析。 |
| 内存要求 | 内存占用较大                                                 | 内存占用率低                                                 |
| 增删改   | 可以对xml文件进行增删改查的操作                              | 只能进行解析（查询操作）                                     |
| 复杂度   | 完全面向对象的解析方式，容易使用                             | 采用事件回调机制，通过事件的回调函数来解析XML文档，略复杂。  |

### 4.3 dom4j

- dom4j是一个简单、灵活的开放源代码的库。

- Dom4j是由早期开发JDOM的人分离出来而后独立开发的。与JDOM不同的是，dom4j使用接口和抽象基类，虽然Dom4j的API相对要复杂一些，但它提供了比JDOM更好的灵活性。

- dom4j是一个非常优秀的Java XML API，具有性能优异、功能强大和极易使用的特点。现在很多软件都采用dom4j，例如Hibernate。

- 使用dom4j开发，需导入dom4j相应的jar包dom4j-1.6.1.jar。所以使用第一步：导包！

- **Dom4j解析关键步骤**

  - 解析并遍历

  ```java
  //1、创建解析器对象
  SAXReader reader = new SAXReader();
  //2、使用解析器将xml文件转换为内存中的document对象
  Document document = reader.read("teachers.xml"); //相对在项目根路径下查找xml文件
  //3、通过文档对象可以获取文档的根标签
  Element rootElement = document.getRootElement();
  //4、根据根标签获取所有的根标签的子标签集合
  List<Element> elements = rootElement.elements();
  //5、遍历集合中的标签，并将所有的数据解析出来
  for (Element element : elements) {
  	//每次遍历就代表一个teacher信息
  	System.out.println("正在遍历的标签名："+element.getName());
  	System.out.println("正在遍历标签的id属性值："+element.attributeValue("id"));
  	//获取teacher的子标签的内容
  	String tname = element.elementText("tname");
  	System.out.println("tname："+tname);
  	String age = element.elementText("tage");
  	System.out.println("tage:"+age);
  	System.out.println("=====================================");
  }
  ```

  其中，xml文件声明如下：【teachers.xml】

  ```xml
   <teachers>
   	<teacher id="1">
   		<tname>佟刚</tname>
   		<tage>49</tage>
   	</teacher>
   	<teacher id="2">
   		<tname>苍老师</tname>
   		<tage>18</tage>
   	</teacher>
   </teachers>
  ```

  - 修改

  ```java
  //添加一个新的student节点
  Element newEle = rootElement.addElement("teacher");
  //创建一个良好的xml格式
  OutputFormat format = OutputFormat.createPrettyPrint();
  //写入文件
  XMLWriter xmlWriter = new XMLWriter(new FileWriter("teachers.xml"),format);
  xmlWriter.write(document);
  xmlWriter.close();
  ```

  - 新建

  ```java
  //1.创建文档
  Document document = DocumentHelper.createDocument();
  //2.添加根元素
  Element root = document.addElement("teachers");
  //3.添加元素节点
  Element tcEle = root.addElement("teacher");
  Element tcEle2 = root.addElement("teacher");
  ```

### 4.4 pull

- Pull解析和Sax解析很相似，都是轻量级的解析，它是一个第三方开源的Java项目，但在Android的内核中已经嵌入了Pull 。

- 只能进行解析（查询）

## 第5章 XPath

### 5.1 简介

- XPath 是在 XML 文档中查找信息的语言，类似于jQuery选择器。

- XPath通过元素和属性进行查找，**简化了Dom4j查找节点的过程**，是W3C组织发布的标准。

- 使用XPath必须在导入dom4j.jar包之外，**再导入jaxen-1.1-beta-6.jar包**

- 具体语法：**见 《XPathTutorial (菜鸟必备)》**


### 5.2 语法

XPath语法示例

| 语法               | 说明                                   |
| ------------------ | -------------------------------------- |
| /AAA               | 选择根元素AAA                          |
| /AAA/CCC           | 选择AAA下的所有CCC子元素               |
| /AAA/DDD/BBB       | 选择AAA的子元素DDD的所有BBB子元素      |
| //BBB              | 选择所有BBB元素                        |
| //DDD/BBB          | 选择所有父元素是DDD的BBB元素           |
| /AAA/CCC/DDD/*     | 选择所有路径依附于/AAA/CCC/DDD的元素   |
| /\*/\*/*/BBB       | 选择所有的有3个祖先元素的BBB元素       |
| //*                | 选择所有元素                           |
| /AAA/BBB[1]        | 选择AAA的第一个BBB子元素               |
| /AAA/BBB[last()]   | 选择AAA的最后一个BBB子元素             |
| //@id              | 选择所有元素的id属性                   |
| //BBB[@id]         | 选择有id属性的BBB元素                  |
| //BBB[@name]       | 选择有name属性的BBB元素                |
| //BBB[@*]          | 选择有任意属性的BBB元素                |
| //BBB[not(@*)]     | 选择没有属性的BBB元素                  |
| //BBB[@id='b1']    | 选择含有属性id且其值为'b1'的BBB元素    |
| //BBB[@name='bbb'] | 选择含有属性name且其值为'bbb'的BBB元素 |

> 其他情况，参见《 XPathTutorial (菜鸟必备)》

### 5.3 使用

① 导包：**jaxen-1.1-beta-6.jar**

② dom4j怎么做就怎么做。只是在查找元素的时候可以使用xpath了

③ 获取所有符合条件的节点

- **document.selectNodes(String xpathExpression) ：返回List集合**
  - 举例：document.selectNodes("/students/student")

- **document.selectSingleNode(String xpathExpression)** ：获取符合条件的单个节点。即一个Node对象。如果符合条件的节点有多个，那么返回第一个。
  - 举例：document.selectSingleNode("/students/student[@id='1']")

### 5.4 代码示例

```java
public void testXpath() throws Exception{
		
	SAXReader reader = new SAXReader();
	Document document = reader.read("src/students.xml");
	//我们使用selectNodes 或 selectSingleNode
	//查询id为2的学生
	Element stuEle = (Element) document.selectSingleNode("/students/student[@id='2']");
	//获取学生的信息
	String idStr = stuEle.attributeValue("id");
	String name = stuEle.elementText("name");
	String ageStr = stuEle.elementText("age");
	String gender = stuEle.elementText("gender");
	String address = stuEle.elementText("address");
	//封装为一个学生对象
	Student stu = new Student(Integer.parseInt(idStr), name, Integer.parseInt(ageStr), gender, address);
	System.out.println(stu);
}

```

其中，xml文件声明在【src/students.xml】中：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<students>
	<student id="1">
		<name>孙悟空</name>
		<gender>男</gender>
		<age>520</age>
		<address>花果山</address>
	</student>
	<student id="2">
		<name>猪八戒</name>
		<gender>男</gender>
		<age>1520</age>
		<address>高老庄</address>
	</student>
	<student id="3">
		<name>沙悟净</name>
		<gender>男</gender>
		<age>1000</age>
		<address>流沙河</address>
	</student>
	<student id="4">
		<name>小白龙</name>
		<gender>男</gender>
		<age>1800</age>
		<address>鹰愁涧</address>
	</student>
</students>
```

# Web环境搭建：Tomcat

![1557670023691](images/1557670023691.png)

***

## 第1章 Web服务器

- Web服务器通常由硬件和软件共同构成。

  - 硬件：电脑，提供服务供其它客户电脑访问

    ![1561995738943](images/1561995738943.png)

  - 软件：电脑上安装的服务器软件，安装后能提供服务给网络中的其他计算机，**将本地文件映射成一个虚拟的url地址供网络中的其他人访问。**

- Web服务器主要用来接收客户端发送的请求和响应客户端请求。

- 常见的JavaWeb服务器：

  - **Tomcat（Apache）**：当前应用最广的JavaWeb服务器
  - JBoss（Redhat红帽）：支持JavaEE，应用比较广EJB容器 –> SSH轻量级的框架代替
  - GlassFish（Orcale）：Oracle开发JavaWeb服务器，应用不是很广
  - Resin（Caucho）：支持JavaEE，应用越来越广
  - Weblogic（Orcale）：要钱的！支持JavaEE，适合大型项目
  - Websphere（IBM）：要钱的！支持JavaEE，适合大型项目

## 第2章 Tomcat服务器

### 2.1 Tomcat简介

Tomcat是Apache 软件基金会（Apache Software Foundation）的Jakarta 项目中的一个核心项目，由Apache、Sun 和其他一些公司及个人共同开发而成。由于有了Sun 的参与和支持，最新的Servlet 和JSP 规范总是能在Tomcat 中得到体现，因为Tomcat 技术先进、性能稳定，而且免费，因而深受Java 爱好者的喜爱并得到了部分软件开发商的认可，成为目前比较流行的Web 应用服务器。

### 2.2 Tomcat下载

- Tomcat官方网站：<http://tomcat.apache.org/>
- 安装版：需要安装，一般不考虑使用。
- 解压版: 直接解压缩使用，我们使用的版本。
- 因为tomcat服务器软件需要使用java环境，所以需要正确配置JAVA_HOME。

### 2.3 Tomcat的版本

- 版本：目前最新版本是10.0，企业用的比较广泛的是7.0和8.0的。授课我们暂时用的是7.0。
- tomcat6以下都不用了，所以我们从tomcat6开始比较：

  - tomcat6 	支持servlet2.5、jsp2.1、el
  - tomcat7 	支持servlet3.0、jsp2.2、el2.2、websocket1.1
  - tomcat8	 支持servlet3.1、jsp2.3、el3.0、websocket1.1
  - tomcat9	 支持servlet4.0、jsp2.3、el3.0、websocket1.1

### 2.3 安装

解压apache-tomcat-7.0.79-windows-x64.zip到**非中文无空格**目录中

![1557499687108](images/1557499687108.png)

D:\developer_tools\apache-tomcat-7.0.78，这个目录下直接包含Tomcat的bin目录，conf目录等，我们称之为**Tomcat的安装目录或根目录**。

- bin：该目录下存放的是二进制可执行文件，如果是安装版，那么这个目录下会有两个exe文件：tomcat6.exe、tomcat6w.exe，前者是在控制台下启动Tomcat，后者是弹出GUI窗口启动Tomcat；如果是解压版，那么会有startup.bat和shutdown.bat文件，startup.bat用来启动Tomcat，但需要先配置JAVA_HOME环境变量才能启动，shutdawn.bat用来停止Tomcat；

- conf：这是一个非常非常重要的目录，这个目录下有四个最为重要的文件：
  - **server.xml：配置整个服务器信息。例如修改端口号。默认HTTP请求的端口号是：8080**
  - tomcat-users.xml：存储tomcat用户的文件，这里保存的是tomcat的用户名及密码，以及用户的角色信息。可以按着该文件中的注释信息添加tomcat用户，然后就可以在Tomcat主页中进入Tomcat Manager页面了；
  - web.xml：部署描述符文件，这个文件中注册了很多MIME类型，即文档类型。这些MIME类型是客户端与服务器之间说明文档类型的，如用户请求一个html网页，那么服务器还会告诉客户端浏览器响应的文档是text/html类型的，这就是一个MIME类型。客户端浏览器通过这个MIME类型就知道如何处理它了。当然是在浏览器中显示这个html文件了。但如果服务器响应的是一个exe文件，那么浏览器就不可能显示它，而是应该弹出下载窗口才对。MIME就是用来说明文档的内容是什么类型的！
  - context.xml：对所有应用的统一配置，通常我们不会去配置它。

- lib：Tomcat的类库，里面是一大堆jar文件。如果需要添加Tomcat依赖的jar文件，可以把它放到这个目录中，当然也可以把应用依赖的jar文件放到这个目录中，这个目录中的jar所有项目都可以共享之，但这样你的应用放到其他Tomcat下时就不能再共享这个目录下的jar包了，所以建议只把Tomcat需要的jar包放到这个目录下；

- logs：这个目录中都是日志文件，记录了Tomcat启动和关闭的信息，如果启动Tomcat时有错误，那么异常也会记录在日志文件中。

- temp：存放Tomcat的临时文件，这个目录下的东西可以在停止Tomcat后删除！

- **webapps：存放web项目的目录，其中每个文件夹都是一个项目**；如果这个目录下已经存在了目录，那么都是tomcat自带的项目。其中ROOT是一个特殊的项目，在地址栏中访问：http://127.0.0.1:8080，没有给出项目目录时，对应的就是ROOT项目。<http://localhost:8080/examples，进入示例项目。其中examples>就是项目名，即文件夹的名字。

- work：运行时生成的文件，最终运行的文件都在这里。通过webapps中的项目生成的！可以把这个目录下的内容删除，再次运行时会生再次生成work目录。当客户端用户访问一个JSP文件时，Tomcat会通过JSP生成Java文件，然后再编译Java文件生成class文件，生成的java和class文件都会存放到这个目录下。

- LICENSE：许可证。

- NOTICE：说明文件。

### 2.4 配置

启动Tomcat前，需要配置如下的环境变量

① 配置JAVA_HOME环境变量

![1557501069603](images/1557501069603.png)

② 在Path环境变量中加入JAVA_HOME\bin目录

![1561975042021](images/1561975042021.png)

③ 新建环境变量CATALINA_HOME=解压目录 

![1557501109051](images/1557501109051.png)

④ 在Path环境变量中加入Tomcat解压目录\bin目录

![1557501164603](images/1557501164603.png)

### 2.5 启动 

在命令行中运行**catalina run**或者 Tomcat解压目录下**双击startup.bat** 启动Tomcat服务器，在浏览器地址栏访问如下地址进行测试

**http://localhost:8080**

![1557501275330](images/1557501275330.png)



如果启动失败，查看如下的情况：

情况一：如果双击startup.bat后窗口一闪而过，请查看JAVA_HOME是否配置正确。

> startup.bat会调用catalina.bat，而catalina.bat会调用setclasspath.bat，setclasspath.bat会使用JAVA_HOME环境变量，所以我们必须在启动Tomcat之前把JAVA_HOME配置正确。

情况二：如果启动失败，提示端口号被占用，则将默认的8080端口修改为其他未使用的值，例如8989等。

【方法】 打开：解压目录\conf\server.xml，找到第一个Connector标签，修改port属性

![1557505660785](images/1557505660785.png)

> web服务器在启动时，实际上是监听了本机上的一个端口，当有客户端向该端口发送请求时，web服务器就会处理请求。但是如果不是向其所监听的端口发送请求，web服务器不会做任何响应。例如：Tomcat启动监听了8989端口，而访问的地址是<http://localhost:8080>，将不能正常访问。

### 2.6 在Eclipse中创建Tomcat镜像

在Eclipse中配置好Tomcat后，可以直接通过Eclipse控制Tomcat的启动和停止，而不用再去操作startup.bat和shutdown.bat。

① 在Servers视窗里点击超链接或在空白处右键→New

![1557501416892](images/1557501416892.png)

接着：

![1557501489531](images/1557501489531.png)

接着：

![1557501625107](images/1557501625107.png)

接着：

![1557506000732](images/1557506000732.png)



> 注意1：关联Tomcat镜像时，Eclipse会从本地Tomcat中复制信息及文件（如下图），之后二者的配置信息就没有关系了，其中任何一个的配置信息发生变化都不会自动同步到另外一个。所以，如果修改端口号的话，需要Tomcat目录中和Eclipse的镜像文件中都要修改端口号才可以。
>
> ![1557506463395](images/1557506463395.png)



> 注意2：Tomcat服务器在Eclipse中启动以后，在浏览器中访问：http://localhost:8080会发现访问失败。因为此时在Eclipse中启动的webapps目录与之前方式访问使用的webapps目录不一致。
>
> ![1560414216815](images/1560414216815.png)
>
> 得到：
>
> ![1560414312096](images/1560414312096.png)

### 2.7 在IDEA中创建Web项目

![3](images/3.png) 

![1](images/1.png)

![2](images/2.png)

![4](images/4.png)

![5](images/5.png)

## 第3章 动态Web工程部署与测试

### 3.1 创建**动态**Web工程

![1557506289562](images/1557506289562.png)

接着：

![1557501901441](images/1557501901441.png)

接着，在创建好的工程的WebContent目录下创建index.jsp。如下：

![1560417195187](images/1560417195187.png)

index.jsp中声明如下代码：

```jsp
<%@page import="java.util.Date"%>
<%=new Date() %>
```

接着，在index.jsp上点右键：Run as→Run on Server查看运行结果。

### 3.2 开发项目目录结构说明

- **src：存放Java源代码的目录。**

- Libraries：存放的是Tomcat及JRE中的jar包。

- build：自动编译.java文件的目录。

- WebContent：存放的是需要部署到服务器的文件

  - META-INF：是存放工程自身相关的一些信息，元文件信息，通常由开发工具和环境自动生成。
    - MANIFEST.MF：配置清单文件
  - WEB-INF：**这个目录下的文件，是不能被客户端直接访问的。**
    - **lib：用于存放该工程用到的库。粘贴过来以后，不再需要build path**
    - **web.xml：web工程的配置文件，完成用户请求的逻辑名称到真正的servlet类的映射。**
    - classes：存放Java字节码文件的目录。

  > 凡是客户端能访问的资源(*.html或 *.jpg)必须跟WEB-INF在同一目录，即放在Web根目录下的资源，从客户端是可以通过URL地址直接访问的。

![开发的web工程与部署以后的web工程目录结构的对应关系](images/开发的web工程与部署以后的web工程目录结构的对应关系.png)

### 3.3 部署/移除Web项目

右键点击刚创建好的Tomcat服务器，选择Add and Remove …

![1557665489280](images/1557665489280.png)

将左边的项目 add 到右边，即为部署：

![1557665884313](images/1557665884313.png)

将右边的项目remove到左边，即为移除：

![1557665929617](images/1557665929617.png)

部署或移除web项目以后，需要重启服务器：

![1557665994974](images/1557665994974.png)

如果是部署，在部署完成且重新启动Tomcat服务器以后，可以通过浏览器访问：

![1560417585069](images/1560417585069.png)

或

![1560417623200](images/1560417623200.png)

### 3.4 Tomcat中部署Web项目的原理

- 当我们使用Eclipse将项目部署到Tomcat中时，我们发现，在Tomcat下的webapps目录下并没有我们创建好的项目，但是当通过Eclipse启动服务器后，项目却真的可以访问到，这是为什么呢？

- 实际上，Eclipse在使用Tomcat时，并没有将项目部署到Tomcat的目录下，而是在其工作空间中，创建了一个Tomcat的镜像，Eclipse启动的Tomcat是他自己配置的镜像。

- Eclipse镜像所在目录：你的工作空间目录\.metadata\.plugins\org.eclipse.wst.server.core\tmp0

  （重启Tomcat以后，双击Servers-TOmcat v7.0即会看到部署好的web工程存在的默认位置。)

![1557666100393](images/1557666100393.png)

或者通过如下的方式，也可以直接找到部署的位置：

![1560417779516](images/1560417779516.png)



![1557669357254](images/1557669357254.png)

- 在这个目录下，我们会发现一个和Tomcat安装目录几乎一模一样的目录结构。

- 在这里Eclipse中Tomcat镜像配置文件也是独立的，而我们所创建的项目是部署到wtpwebapps目录下的，这样做的好处是Eclipse中Tomcat和我们本机的Tomcat可以保持相互独立，不会影响彼此。但是千万注意，此时千万不要试图删除原有的Tomcat目录结构或移动位置。

![1557666148925](images/1557666148925.png)

总结：启动服务器以后，我们的web项目会部署到服务器目录的wtpwebapps文件夹的目录下。index.jsp也是直接声明在此项目目录下的。

# HTTP协议

## 第1章 HTTP协议简介

![img](images/timg.jpg)

- **HTTP 超文本传输协议** (HTTP-Hypertext transfer protocol)，是一个属于应用层的面向对象的协议，由于其简捷、快速的方式，适用于分布式超媒体信息系统。它于1990年提出，经过十几年的使用与发展，得到不断地完善和扩展。**它是一种详细规定了浏览器和万维网服务器之间互相通信的规则**，通过因特网传送万维网文档的数据传送协议。

- 客户端与服务端通信时传输的内容我们称之为**报文**。**HTTP协议就是规定报文的格式。**

- HTTP就是一个通信规则，这个规则规定了客户端发送给服务器的报文格式，也规定了服务器发送给客户端的报文格式。实际我们要学习的就是这两种报文。客户端发送给服务器的称为”**请求报文**“，服务器发送给客户端的称为”**响应报文**“。

- 类比于生活中案例：① 毕业租房，签署租房协议，规范多方需遵守的规则。②与远方的朋友的写信。信封的规范。

  实际互联网：

  - 客户端 与 服务端进行通信。比如：用户 ---> 访问京东（就是一个数据传输的过程），数据传输需要按照一种协议去传输。就如，用户给服务器写信；服务器给用户回信。有格式：协议。HTTP协议规定通信规则。规定互联网之间如何传输数据。
    - 信：报文。
    - 写信：用户给服务器写信，用户给服务器发请求。把发的请求所有数据，请求报文
    - 回信：服务器回信给用户，回给浏览器。把服务器响应浏览器的所有数据，响应报文



## 第2章 HTTP协议的发展历程

- 超文本传输协议的前身是世外桃源(Xanadu)项目，超文本的概念是泰德·纳尔森(Ted Nelson)在1960年代提出的。进入哈佛大学后，纳尔森一直致力于超文本协议和该项目的研究，但他从未公开发表过资料。1989年，**蒂姆·伯纳斯·李**(Tim Berners Lee)在CERN(欧洲原子核研究委员会 = European Organization for Nuclear Research)担任软件咨询师的时候，开发了一套程序，**奠定了万维网(WWW = World Wide Web)**的基础。1990年12月，超文本在CERN首次上线。1991年夏天，继Telnet等协议之后，超文本转移协议成为互联网诸多协议的一分子。

- 当时，**Telnet协议**解决了一台计算机和另外一台计算机之间一对一的控制型通信的要求。邮件协议解决了一个发件人向少量人员发送信息的通信要求。**文件传输协议**解决一台计算机从另外一台计算机批量获取文件的通信要求，但是它不具备一边获取文件一边显示文件或对文件进行某种处理的功能。**新闻传输协议**解决了一对多新闻广播的通信要求。而**超文本要解决的通信要求是**：在一台计算机上获取并显示存放在多台计算机里的文本、数据、图片和其他类型的文件；它包含两大部分：超文本转移协议和超文本标记语言(HTML)。HTTP、HTML以及浏览器的诞生给互联网的普及带来了飞跃。

## 第3章 HTTP协议的会话方式

- 浏览器与服务器之间的通信过程要经历四个步骤

![1557672342250](images/1557672342250.png)

- 浏览器与WEB服务器的连接过程是短暂的，每次连接只处理一个请求和响应。对每一个页面的访问，浏览器与WEB服务器都要建立一次单独的连接。

- 浏览器到WEB服务器之间的所有通讯都是完全独立分开的请求和响应对。



## 第4章 HTTP1.0和HTTP1.1的区别

在HTTP1.0版本中，浏览器请求一个带有图片的网页，会由于下载图片而与服务器之间开启一个新的连接；但在HTTP1.1版本中，允许浏览器在拿到当前请求对应的全部资源后再断开连接，提高了效率。

![1557672415271](images/1557672415271.png)



## 第5章 不同浏览器监听HTTP操作

### 5.1 IE8浏览器：HttpWatch插件

- 由于IE8以下的IE浏览器没有提供监听HTTP的功能，所以如果要使用IE8以下的浏览器查看HTTP请求的内容需要安装一个工具HttpWatch。
- Firefox和chrome都有内置的开发者工具，可以直接查看Http请求。
- HttpWatch的使用非常简单，直接安装，然后一直下一步，直到安装完成。

- 安装完成后，打开IE浏览器，工具下拉列表可以看到HttpWatch Professional选项

![1557672511595](images/1557672511595.png)

- 打开后点击Record按钮开始监听Http请求。

![1557672538558](images/1557672538558.png)

### 5.2 IE9浏览器中F12可查看

![1561904433377](images/1561904433377.png)

### 5.3 Chrome中F12可查看

![1561904121561](images/1561904121561.png)

### 5.4 FireFox中F12可查看

![1561904179928](images/1561904179928.png)



## 第6章 报文

### 6.1 报文格式

![1557672592385](images/1557672592385.png)

- 报文：
  - 请求报文：浏览器发给服务器

  - 响应报文：服务器发回给浏览器

### 6.2 请求报文

#### 6.2.1 报文格式 (4部分)

- 请求首行（**请求行**）；

- 请求头信息（**请求头**）；

- 空行；

- 请求体；

#### 6.2.2 GET请求

1、由于请求参数在请求首行中已经携带了，所以没有请求体，也没有请求空行
2、请求参数拼接在url地址中，地址栏可见[url?name1=value1&name2=value2]，不安全
3、由于参数在地址栏中携带，所以由大小限制[地址栏数据大小一般限制为4k]，只能携带纯文本
4、get请求参数只能上传文本数据
5、没有请求体。所以封装和解析都快，效率高， 浏览器默认提交的请求都是get请求[比如：① 地址栏输入url地址回车，②点击超链接a ， ③ form表单默认方式...]

- **请求首行**

```http
GET /05_web_tomcat/login_success.html?username=admin&password=123213 HTTP/1.1

请求方式 	访问的服务器中的资源路径？get请求参数	协议版本
```

- **请求头**

```http
Host: localhost:8080   主机虚拟地址
Connection: keep-alive 长连接
Upgrade-Insecure-Requests: 1  请求协议的自动升级[http的请求，服务器却是https的，浏览器自动会将请求协议升级为https的]
User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36
- 用户系统信息
Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
- 浏览器支持的文件类型
Referer: http://localhost:8080/05_web_tomcat/login.html
- 当前页面的上一个页面的路径[当前页面通过哪个页面跳转过来的]：   可以通过此路径跳转回上一个页面， 广告计费，防止盗链
Accept-Encoding: gzip, deflate, br
- 浏览器支持的压缩格式
Accept-Language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7
- 浏览器支持的语言
```



#### 6.2.3 POST请求

- POST请求要求将form标签的method的属性设置为post

![1557672877007](images/1557672877007.png)

1、post请求有请求体，而GET请求没有请求体。
2、post请求数据在请求体中携带，请求体数据大小没有限制，可以用来上传所有内容[文件、文本]
3、只能使用post请求上传文件
4、post请求报文多了和请求体相关的配置[请求头]
5、地址栏参数不可见，相对安全
6、post效率比get低

- 请求首行

```http
POST /05_web_tomcat/login_success.html HTTP/1.1
```

- 请求头

```http
Host: localhost:8080
Connection: keep-alive
Content-Length: 31 		-请求体内容的长度
Cache-Control: max-age=0  -无缓存
Origin: http://localhost:8080
Upgrade-Insecure-Requests: 1  -协议的自动升级
Content-Type: application/x-www-form-urlencoded   -请求体内容类型[服务器根据类型解析请求体参数]
User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.75 Safari/537.36
Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8
Referer: http://localhost:8080/05_web_tomcat/login.html
Accept-Encoding: gzip, deflate, br
Accept-Language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7
Cookie:JSESSIONID-
```

- 请求空行
- 请求体：浏览器提交给服务器的内容

```http
username=admin&password=1232131
```

#### 6.2.4 HTTP Request Header 请求头

| **Header**          | **解释**                                                     | **示例**                                                 |
| ------------------- | ------------------------------------------------------------ | -------------------------------------------------------- |
| Accept              | 指定客户端能够接收的内容类型                                 | Accept: text/plain, text/html                            |
| Accept-Charset      | 浏览器可以接受的字符编码集。                                 | Accept-Charset: iso-8859-5                               |
| Accept-Encoding     | 指定浏览器可以支持的web服务器返回内容压缩编码类型。          | Accept-Encoding: compress, gzip                          |
| Accept-Language     | 浏览器可接受的语言                                           | Accept-Language: en,zh                                   |
| Accept-Ranges       | 可以请求网页实体的一个或者多个子范围字段                     | Accept-Ranges: bytes                                     |
| Authorization       | HTTP授权的授权证书                                           | Authorization: Basic  QWxhZGRpbjpvcGVuIHNlc2FtZQ==       |
| Cache-Control       | 指定请求和响应遵循的缓存机制                                 | Cache-Control: no-cache                                  |
| Connection          | 表示是否需要持久连接。（HTTP 1.1默认进行持久连接）           | Connection: close                                        |
| Cookie              | HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器。 | Cookie: $Version=1; Skin=new;                            |
| Content-Length      | 请求的内容长度                                               | Content-Length: 348                                      |
| Content-Type        | 请求的与实体对应的MIME信息                                   | Content-Type:  application/x-www-form-urlencoded         |
| Date                | 请求发送的日期和时间                                         | Date: Tue, 15 Nov 2010 08:12:31  GMT                     |
| Expect              | 请求的特定的服务器行为                                       | Expect: 100-continue                                     |
| From                | 发出请求的用户的Email                                        | From: user@email.com                                     |
| Host                | 指定请求的服务器的域名和端口号                               | Host: www.zcmhi.com                                      |
| If-Match            | 只有请求内容与实体相匹配才有效                               | If-Match:  “737060cd8c284d8af7ad3082f209582d”            |
| If-Modified-Since   | 如果请求的部分在指定时间之后被修改则请求成功，未被修改则返回304代码 | If-Modified-Since: Sat, 29 Oct  2010 19:43:31 GMT        |
| If-None-Match       | 如果内容未改变返回304代码，参数为服务器先前发送的Etag，与服务器回应的Etag比较判断是否改变 | If-None-Match:  “737060cd8c284d8af7ad3082f209582d”       |
| If-Range            | 如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体。参数也为Etag | If-Range:  “737060cd8c284d8af7ad3082f209582d”            |
| If-Unmodified-Since | 只在实体在指定时间之后未被修改才请求成功                     | If-Unmodified-Since: Sat, 29 Oct  2010 19:43:31 GMT      |
| Max-Forwards        | 限制信息通过代理和网关传送的时间                             | Max-Forwards: 10                                         |
| Pragma              | 用来包含实现特定的指令                                       | Pragma: no-cache                                         |
| Proxy-Authorization | 连接到代理的授权证书                                         | Proxy-Authorization: Basic  QWxhZGRpbjpvcGVuIHNlc2FtZQ== |
| Range               | 只请求实体的一部分，指定范围                                 | Range: bytes=500-999                                     |
| Referer             | 先前网页的地址，当前请求网页紧随其后,即来路                  | Referer:  http://www.zcmhi.com/archives/71.html          |
| TE                  | 客户端愿意接受的传输编码，并通知服务器接受接受尾加头信息     | TE: trailers,deflate;q=0.5                               |
| Upgrade             | 向服务器指定某种传输协议以便服务器进行转换（如果支持）       | Upgrade: HTTP/2.0, SHTTP/1.3,  IRC/6.9, RTA/x11          |
| User-Agent          | User-Agent的内容包含发出请求的用户信息                       | User-Agent: Mozilla/5.0 (Linux;  X11)                    |
| Via                 | 通知中间网关或代理服务器地址，通信协议                       | Via: 1.0 fred, 1.1 nowhere.com  (Apache/1.1)             |
| Warning             | 关于消息实体的警告信息                                       | Warn: 199 Miscellaneous warning                          |



### 6.3 响应报文

#### 6.3.1 报文格式(4部分)

- 响应首行（**响应行**）；

- 响应头信息（**响应头**）；

- 空行；

- **响应体；**

#### 6.3.2 具体情况

- **响应首行：**

  ```http
  HTTP/1.1 200 OK
  
  说明：响应协议为HTTP1.1，响应状态码为200，表示请求成功； 
  ```

- **响应头：**

  ```http
  Server: Apache-Coyote/1.1   服务器的版本信息
  Accept-Ranges: bytes
  ETag: W/"157-1534126125811"
  Last-Modified: Mon, 13 Aug 2018 02:08:45 GMT
  Content-Type: text/html    响应体数据的类型[浏览器根据类型解析响应体数据]
  Content-Length: 157   响应体内容的字节数
  Date: Mon, 13 Aug 2018 02:47:57 GMT  响应的时间，这可能会有8小时的时区差
  ```

- **响应空行**

- **响应体**

  ```html
  <!--需要浏览器解析使用的内容[如果响应的是html页面，最终响应体内容会被浏览器显示到页面中]-->
  
  <!DOCTYPE html>
  <html>
  	<head>
  		<meta charset="UTF-8">
  		<title>Insert title here</title>
  	</head>
  	<body>
  		恭喜你，登录成功了...
  	</body>
  </html>
  ```

#### 6.3.3 响应码

响应码对浏览器来说很重要，它告诉浏览器响应的结果。比较有代表性的响应码如下：

- **200：**请求成功，浏览器会把响应体内容（通常是html）显示在浏览器中；

- **404：**请求的资源没有找到，说明客户端错误的请求了不存在的资源；

  ![1561905288592](images/1561905288592.png)

  ![1561905338054](images/1561905338054.png)

  ![1561905371728](images/1561905371728.png)

- **500：**请求资源找到了，但服务器内部出现了错误；

  ![1561905801690](images/1561905801690.png)

  ![1561905834066](images/1561905834066.png)

- **302：**重定向，当响应码为302时，表示服务器要求浏览器重新再发一个请求，服务器会发送一个响应头Location，它指定了新请求的URL地址；

除此之外，其它一些响应码如下：

```
200 - 服务器成功返回网页 
404 - 请求的网页不存在 
503 - 服务不可用 
详细分解：

1xx（临时响应） 
表示临时响应并需要请求者继续执行操作的状态代码。

代码 说明 
100 （继续） 请求者应当继续提出请求。服务器返回此代码表示已收到请求的第一部分，正在等待其余部分。 
101 （切换协议） 请求者已要求服务器切换协议，服务器已确认并准备切换。

2xx （成功） 
表示成功处理了请求的状态代码。

代码 说明 
200 （成功） 服务器已成功处理了请求。通常，这表示服务器提供了请求的网页。 
201 （已创建） 请求成功并且服务器创建了新的资源。 
202 （已接受） 服务器已接受请求，但尚未处理。 
203 （非授权信息） 服务器已成功处理了请求，但返回的信息可能来自另一来源。 
204 （无内容） 服务器成功处理了请求，但没有返回任何内容。 
205 （重置内容） 服务器成功处理了请求，但没有返回任何内容。 
206 （部分内容） 服务器成功处理了部分 GET 请求。

3xx （重定向） 
表示要完成请求，需要进一步操作。 通常，这些状态代码用来重定向。

代码 说明 
300 （多种选择） 针对请求，服务器可执行多种操作。服务器可根据请求者 (user agent) 选择一项操作，或提供操作列表供请求者选择。 
301 （永久移动） 请求的网页已永久移动到新位置。服务器返回此响应（对 GET 或 HEAD 请求的响应）时，会自动将请求者转到新位置。 
302 （临时移动） 服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。 
303 （查看其他位置） 请求者应当对不同的位置使用单独的 GET 请求来检索响应时，服务器返回此代码。 
304 （未修改） 自从上次请求后，请求的网页未修改过。服务器返回此响应时，不会返回网页内容。 
305 （使用代理） 请求者只能使用代理访问请求的网页。如果服务器返回此响应，还表示请求者应使用代理。 
307 （临时重定向） 服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。

4xx（请求错误） 
这些状态代码表示请求可能出错，妨碍了服务器的处理。

代码 说明 
400 （错误请求） 服务器不理解请求的语法。 
401 （未授权） 请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。 
403 （禁止） 服务器拒绝请求。 
404 （未找到） 服务器找不到请求的网页。 
405 （方法禁用） 禁用请求中指定的方法。 
406 （不接受） 无法使用请求的内容特性响应请求的网页。 
407 （需要代理授权） 此状态代码与 401（未授权）类似，但指定请求者应当授权使用代理。 
408 （请求超时） 服务器等候请求时发生超时。 
409 （冲突） 服务器在完成请求时发生冲突。服务器必须在响应中包含有关冲突的信息。 
410 （已删除） 如果请求的资源已永久删除，服务器就会返回此响应。 
411 （需要有效长度） 服务器不接受不含有效内容长度标头字段的请求。 
412 （未满足前提条件） 服务器未满足请求者在请求中设置的其中一个前提条件。 
413 （请求实体过大） 服务器无法处理请求，因为请求实体过大，超出服务器的处理能力。 
414 （请求的 URI 过长） 请求的 URI（通常为网址）过长，服务器无法处理。 
415 （不支持的媒体类型） 请求的格式不受请求页面的支持。 
416 （请求范围不符合要求） 如果页面无法提供请求的范围，则服务器会返回此状态代码。 
417 （未满足期望值） 服务器未满足”期望”请求标头字段的要求。

5xx（服务器错误） 
这些状态代码表示服务器在尝试处理请求时发生内部错误。 这些错误可能是服务器本身的错误，而不是请求出错。

代码 说明 
500 （服务器内部错误） 服务器遇到错误，无法完成请求。 
501 （尚未实施） 服务器不具备完成请求的功能。例如，服务器无法识别请求方法时可能会返回此代码。 
502 （错误网关） 服务器作为网关或代理，从上游服务器收到无效响应。 
503 （服务不可用） 服务器目前无法使用（由于超载或停机维护）。通常，这只是暂时状态。 
504 （网关超时） 服务器作为网关或代理，但是没有及时从上游服务器收到请求。 
505 （HTTP 版本不受支持） 服务器不支持请求中所用的 HTTP 协议版本。

HttpWatch状态码Result is

200 - 服务器成功返回网页，客户端请求已成功。 
302 - 对象临时移动。服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。 
304 - 属于重定向。自上次请求后，请求的网页未修改过。服务器返回此响应时，不会返回网页内容。 
401 - 未授权。请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应。 
404 - 未找到。服务器找不到请求的网页。 
2xx - 成功。表示服务器成功地接受了客户端请求。 
3xx - 重定向。表示要完成请求，需要进一步操作。客户端浏览器必须采取更多操作来实现请求。例如，浏览器可能不得不请求服务器上的不同的页面，或通过代理服务器重复该请求。 
4xx - 请求错误。这些状态代码表示请求可能出错，妨碍了服务器的处理。 
5xx - 服务器错误。表示服务器在尝试处理请求时发生内部错误。 这些错误可能是服务器本身的错误，而不是请求出错。
```



#### 6.3.4 MIME类型（了解）

HTTP内容类型：当前响应体的数据类型。

① 需要指出的是在浏览器和服务器之间传输的数据类型并非都是文本类型，还包括图片、视频、音频等多媒体类型。这些多媒体类型是使用MIME类型定义的。

② MIME的英文全称是"Multipurpose Internet Mail Extensions" 多功能Internet 邮件扩充服务。MIME类型的格式是“大类型/小类型”，并与某一种文件的扩展名相对应。

③ 常见的MIME类型

| 文件               | MIME类型                            |
| ------------------ | ----------------------------------- |
| 超文本标记语言文本 | .html,.htm  text/html               |
| 普通文本           | .txt text/plain                     |
| RTF文本            | .rtf   application/rtf              |
| GIF图形            | .gif image/gif                      |
| JPEG图形           | .jpeg,.jpg   image/jpeg             |
| au声音文件         | .au audio/basic                     |
| MIDI音乐文件       | mid,.midi   audio/midi,audio/x-midi |
| RealAudio音乐文件  | .ra, .ram   audio/x-pn-realaudio    |
| MPEG文件           | .mpg,.mpeg   video/mpeg             |
| AVI文件            | .avi   video/x-msvideo              |
| GZIP文件           | .gz   application/x-gzip            |
| TAR文件            | .tar   application/x-tar            |

#### 6.3.4 HTTP Responses Header 响应头

| **Header**         | **解释**                                                     | **示例**                                                |
| ------------------ | ------------------------------------------------------------ | ------------------------------------------------------- |
| Accept-Ranges      | 表明服务器是否支持指定范围请求及哪种类型的分段请求           | Accept-Ranges: bytes                                    |
| Age                | 从原始服务器到代理缓存形成的估算时间（以秒计，非负）         | Age: 12                                                 |
| Allow              | 对某网络资源的有效的请求行为，不允许则返回405                | Allow: GET, HEAD                                        |
| Cache-Control      | 告诉所有的缓存机制是否可以缓存及哪种类型                     | Cache-Control: no-cache                                 |
| Content-Encoding   | web服务器支持的返回内容压缩编码类型。                        | Content-Encoding: gzip                                  |
| Content-Language   | 响应体的语言                                                 | Content-Language: en,zh                                 |
| Content-Length     | 响应体的长度                                                 | Content-Length: 348                                     |
| Content-Location   | 请求资源可替代的备用的另一地址                               | Content-Location: /index.htm                            |
| Content-MD5        | 返回资源的MD5校验值                                          | Content-MD5:  Q2hlY2sgSW50ZWdyaXR5IQ==                  |
| Content-Range      | 在整个返回体中本部分的字节位置                               | Content-Range: bytes  21010-47021/47022                 |
| Content-Type       | 返回内容的MIME类型                                           | Content-Type: text/html;  charset=utf-8                 |
| Date               | 原始服务器消息发出的时间                                     | Date: Tue, 15 Nov 2010 08:12:31  GMT                    |
| ETag               | 请求变量的实体标签的当前值                                   | ETag:  “737060cd8c284d8af7ad3082f209582d”               |
| Expires            | 响应过期的日期和时间                                         | Expires: Thu, 01 Dec 2010 16:00:00  GMT                 |
| Last-Modified      | 请求资源的最后修改时间                                       | Last-Modified: Tue, 15 Nov 2010  12:45:26 GMT           |
| Location           | 用来重定向接收方到非请求URL的位置来完成请求或标识新的资源    | Location:  http://www.zcmhi.com/archives/94.html        |
| Pragma             | 包括实现特定的指令，它可应用到响应链上的任何接收方           | Pragma: no-cache                                        |
| Proxy-Authenticate | 它指出认证方案和可应用到代理的该URL上的参数                  | Proxy-Authenticate: Basic                               |
| refresh            | 应用于重定向或一个新的资源被创造，在5秒之后重定向（由网景提出，被大部分浏览器支持） | Refresh: 5; url=  http://www.zcmhi.com/archives/94.html |
| Retry-After        | 如果实体暂时不可取，通知客户端在指定时间之后再次尝试         | Retry-After: 120                                        |
| Server             | web服务器软件名称                                            | Server: Apache/1.3.27 (Unix)  (Red-Hat/Linux)           |
| Set-Cookie         | 设置Http Cookie                                              | Set-Cookie: UserID=JohnDoe;  Max-Age=3600; Version=1    |
| Trailer            | 指出头域在分块传输编码的尾部存在                             | Trailer: Max-Forwards                                   |
| Transfer-Encoding  | 文件传输编码                                                 | Transfer-Encoding:chunked                               |
| Vary               | 告诉下游代理是使用缓存响应还是从原始服务器请求               | Vary: *                                                 |
| Via                | 告知代理客户端响应是通过哪里发送的                           | Via: 1.0 fred, 1.1 nowhere.com  (Apache/1.1)            |
| Warning            | 警告实体可能存在的问题                                       | Warning: 199 Miscellaneous warning                      |
| WWW-Authenticate   | 表明客户端请求实体应该使用的授权方案                         | WWW-Authenticate: Basic                                 |

#  Servlet组件

## 第1章 我们为什么需要Servlet？

### 1.1 Web应用基本运行模式

- 生活中的例子

![1557676745259](images/1557676745259.png)

- Web应用运行模式

![1557676763604](images/1557676763604.png)

- Servlet的执行过程

![Servlet的执行过程](images/Servlet的执行过程.png)

### 1.2 Web服务器中Servlet作用举例

- 举例一：插入数据

![1557751488465](images/1557751488465.png)

- 举例二：查询数据

通过网页驱动服务器端的Java程序。在网页上显示Java程序返回的数据。

![1557676863875](images/1557676863875.png)



## 第2章 什么是Servlet？

**如果把Web应用比作一个餐厅，Servlet就是餐厅中的服务员**——负责接待顾客、上菜、结账。

![1557753017639](images/1557753017639.png)

- 从广义上来讲，Servlet规范是Sun公司制定的一套技术标准，包含与Web应用相关的一系列接口，是Web应用实现方式的宏观解决方案。而具体的Servlet容器负责提供标准的实现。
- 从狭义上来讲，Servlet指的是javax.servlet.Servlet接口及其子接口，也可以指实现了Servlet接口的实现类。
- Servlet（**Server Applet**）作为服务器端的一个组件，它的本意是“服务器端的小程序”。
  - **Servlet的实例对象由Servlet容器负责创建；**
  - **Servlet的方法由容器在特定情况下调用；**
  - **Servlet容器会在Web应用卸载时销毁Servlet对象的实例。**

## 第3章 如何使用Servlet？

### 3.1 操作步骤

- 复习：使用一个接口的传统方式：

  - 创建一个类实现接口
  - new 实现类的对象
  - 调用类的方法等

  

- 使用Servlet接口的方式：

  ① 搭建Web开发环境

  ② 创建动态Web工程

  ③ 创建javax.servlet.Servlet接口的实现类：com.atguigu.servlet.MyFirstServlet

  ④ 在service(ServletRequest, ServletResponse)方法中编写如下代码，输出响应信息：

```java
@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		//1.编写输出语句，证明当前方法被调用
		System.out.println("Servlet worked...");
		//2.通过PrintWriter对象向浏览器端发送响应信息
		PrintWriter writer = res.getWriter();
		writer.write("Servlet response");
		writer.close();
	}

```

​	⑤ 在web.xml配置文件中**注册**MyFirstServlet

```xml
<!-- 声明一个Servlet，配置的是Servlet的类信息 -->
<servlet>
	<!-- 这是Servlet的别名，一个名字对应一个Servlet。相当于变量名 -->
	<servlet-name>MyFirstServlet</servlet-name>
	<!-- Servlet的全类名，服务器会根据全类名找到这个Servlet -->
	<servlet-class>com.atguigu.servlet.MyFirstServlet</servlet-class>
</servlet>

<!-- 建立Servlet的请求映射信息 -->
<servlet-mapping>
	<!-- Servlet的别名，说明这个Servlet将会响应下面url-pattern的请求 -->
	<servlet-name>MyFirstServlet</servlet-name>
	<!-- Servlet响应的请求路径。如果访问这个路径，这个Servlet就会响应 -->
	<url-pattern>/MyFirstServlet</url-pattern>
</servlet-mapping>

```

> 说明：
>
> - <url-pattern>：这个url-pattern可以配置多个，这时表示的就是访问这些url都会触发这个Servlet进行响应，运行浏览器，访问刚才配置的url路径，Servlet的service方法就会被调用。
>
> - <url-pattern>中的文本内容必须以 / 或 *. 开始书写路径。相当于将资源映射到项目根目录下形成虚拟的资源文件。
> - <servlet-mapping>中的<url-pattern>可以声明多个，可以通过任意一个都可以访问。但是开发中一般只会配置一个。

​	⑥ 在WebContent目录下创建index.html

​	⑦ 在index.html中加入超链接 \<a href="MyFirstServlet">To Servlet</a>

​	⑧ 点击超链接测试Servlet

### 3.2 运行分析(执行原理)

- index.html

![1557677216444](images/1557677216444.png)

- web.xml

![1557677235443](images/1557677235443.png)

- 如果配置文件一旦修改，需要重启服务器来重新部署web项目。


### 3.3 Servlet作用总结

- 接收请求 【解析请求报文中的数据：请求参数】

- 处理请求 【DAO和数据库交互】

- 完成响应 【设置响应报文】

![1557755002982](images/1557755002982.png)



## 第4章 Servlet生命周期(重要)

### 4.1 Servlet生命周期概述

- 应用程序中的对象不仅在空间上有层次结构的关系，在时间上也会因为处于程序运行过程中的不同阶段而表现出不同状态和不同行为——这就是对象的生命周期。
- 简单的叙述生命周期，就是对象在容器中从开始创建到销毁的过程。

### 4.2 Servlet容器

Servlet对象是Servlet容器创建的，生命周期方法都是由容器调用的。这一点和我们之前所编写的代码有很大不同。在今后的学习中我们会看到，越来越多的对象交给容器或框架来创建，越来越多的方法由容器或框架来调用，开发人员要尽可能多的将精力放在业务逻辑的实现上。

### 4.3 Servlet生命周期的主要过程

![1560591043883](images/1560591043883.png)

#### ① Servlet对象的创建：构造器

- 默认情况下，**Servlet容器第一次收到HTTP请求时创建对应Servlet对象。**
- 容器之所以能做到这一点是由于我们在注册Servlet时提供了全类名，容器使用反射技术创建了Servlet的对象。

#### ② Servlet对象初始化：init()

- Servlet容器**创建Servlet对象之后，会调用init(ServletConfig config)**方法。
- 作用：是在Servlet对象创建后，执行一些初始化操作。例如，读取一些资源文件、配置文件，或建立某种连接（比如：数据库连接）
- init()方法只在创建对象时执行一次，以后再接到请求时，就不执行了
- 在javax.servlet.Servlet接口中，public void init(ServletConfig config)方法要求容器将ServletConfig的实例对象传入，这也是我们获取ServletConfig的实例对象的根本方法。

#### ③ 处理请求：service()

- 在javax.servlet.Servlet接口中，定义了**service(ServletRequest req, ServletResponse res)**方法处理HTTP请求。
- 在每次接到请求后都会执行。
- 上一节提到的Servlet的作用，主要在此方法中体现。
- 同时要求容器将ServletRequest对象和ServletResponse对象传入。

#### ④ Servlet对象销毁：destroy()

- 服务器重启、服务器停止执行或Web应用卸载时会销毁Servlet对象，会调用public void destroy()方法。
- 此方法用于销毁之前执行一些诸如释放缓存、关闭连接、保存内存数据持久化等操作。

### 4.4 Servlet请求过程

- 第一次请求
  - 调用构造器，创建对象
  - 执行init()方法
  - 执行service()方法
- 后面请求
  - 执行service()方法
- 对象销毁前
  - 执行destroy()方法

## 第5章 Servlet的两个重要接口

官方API中声明如下：

![1560591446414](images/1560591446414.png)

### 5.1 ServletConfig接口

![1557750087153](images/1557750087153.png)

- **ServletConfig接口封装了Servlet配置信息**，这一点从接口的名称上就能够看出来。

- **每一个Servlet都有一个唯一对应的ServletConfig对象**，代表当前Servlet的配置信息。

- 对象由Servlet容器创建，并传入生命周期方法init(ServletConfig config)中。可以直接获取使用。

- 代表当前Web应用的ServletContext对象也封装到了ServletConfig对象中，使ServletConfig对象成为了获取ServletContext对象的一座桥梁。

- ServletConfig对象的主要功能

  - **获取Servlet名称：getServletName()**

  - **获取全局上下文ServletContext对象：getServletContext()**

  - **获取Servlet初始化参数：getInitParameter(String) / getInitParameterNames()。**

  - 使用如下：

    ![1560581763744](images/1560581763744.png)

    ```java
    通过String info = config.getInitParameter("url");的方式获取value值
    ```

    

### 5.2 ServletContext接口

![1557750386422](images/1557750386422.png)

- Web容器在启动时，它会为**每个Web应用程序都创建一个唯一对应的ServletContext对象**，意思是Servlet上下文，**代表当前Web应用。**

- 由于**一个Web应用程序中的所有Servlet都共享同一个ServletContext对象**，所以ServletContext对象也被称为 application 对象（Web应用程序对象）。

- **对象由Servlet容器在项目启动时创建**，通过ServletConfig对象的getServletContext()方法获取。在项目卸载时销毁。

- ServletContext对象的主要功能

  ① 获取项目的上下文路径(带/的项目名):  **getContextPath()**

  ```java
  @Override
  public void init(ServletConfig config) throws ServletException {
  	ServletContext application = config.getServletContext();
  	System.out.println("全局上下文对象："+application);
  	String path = application.getContextPath();
  	System.out.println("全局上下文路径："+path);// /06_Web_Servlet
  }
  ```

  ② 获取虚拟路径所映射的本地真实路径：**getRealPath(String path)**

  - 虚拟路径：浏览器访问Web应用中资源时所使用的路径。

  - 本地路径：资源在文件系统中的实际保存路径。

  - 作用：将用户上传的文件通过流写入到服务器硬盘中。

    ```java
    @Override
    public void init(ServletConfig config) throws ServletException {
    	//1.获取ServletContext对象
    	ServletContext context = config.getServletContext();
    	//2.获取index.html的本地路径
    	//index.html的虚拟路径是“/index.html”,其中“/”表示当前Web应用的根目录，
    	//即WebContent目录
    	String realPath = context.getRealPath("/index.html");
    	//realPath=D:\DevWorkSpace\MyWorkSpace\.metadata\.plugins\
    	//org.eclipse.wst.server.core\tmp0\wtpwebapps\MyServlet\index.html
    	System.out.println("realPath="+realPath);
    }
    
    ```

  ③ 获取WEB应用程序的全局初始化参数（基本不用）

  - 设置Web应用初始化参数的方式是在web.xml的根标签下加入如下代码

    ```xml
    <web-app>
    	<!-- Web应用初始化参数 -->
    	<context-param>
    		<param-name>ParamName</param-name>
    		<param-value>ParamValue</param-value>
    	</context-param>
    </web-app>
    ```

  - 获取Web应用初始化参数

    ```java
    @Override
    public void init(ServletConfig config) throws ServletException {
    	//1.获取ServletContext对象
    	ServletContext application = config.getServletContext();
    	//2.获取Web应用初始化参数
    	String paramValue = application.getInitParameter("ParamName");
    	System.out.println("全局初始化参数paramValue="+paramValue);
    }
    ```

  ④ 作为域对象共享数据

  - 作为最大的域对象在整个项目的不同web资源内共享数据。

  ![1557992888615](images/1557992888615.png)

  其中，

  - setAttribute(key,value)：以后可以在任意位置取出并使用
  - getAttribute(key)：取出设置的value值

## 第6章 Servlet技术体系

### 6.1 Servlet接口

![1557677292072](images/1557677292072.png)

### 6.2 Servlet接口的常用实现类

![1560591074608](images/1560591074608.png)

- 为什么要扩展Servlet接口？
  - 封装不常用方法
- 实现类体系
  - GenericServlet实现Servlet接口
  - HttpServlet继承GenericServlet
- 创建Servlet的最终方式
  - 继承HttpServlet

![1557677275555](images/1557677275555.png)

#### 6.2.1 GenericServlet抽象类

![1557677305075](images/1557677305075.png)

- GenericServlet对Servlet功能进行了封装和完善，重写了init(ServletConfig config)方法，用来获取ServletConfig对象。此时如果GenericServlet的子类（通常是自定义Servlet）又重写了init(ServletConfig config)方法有可能导致ServletConfig对象获取不到，所以子类不应该重写带参数的这个init()方法。
- 如果想要进行初始化操作，可以重写GenericServlet提供的无参的init()方法，这样就不会影响ServletConfig对象的获取。
- 将service(ServletRequest req,ServletResponse res)保留为抽象方法，让使用者仅关心业务实现即可。


![1557677325708](images/1557677325708.png)

#### 6.2.2 HttpServlet抽象类

![1557677338617](images/1557677338617.png)

![1557677406592](images/1557677406592.png)

- 专门用来处理Http请求的Servlet。

- 对GenericServlet进行进一步的封装和扩展，在service(ServletRequest req, ServletResponse res)方法中，将ServletRequest和ServletResponse转换为HttpServletRequest和HttpServletResponse，根据不同HTTP请求类型调用专门的方法进行处理。

- **今后在实际使用中继承HttpServlet抽象类创建自己的Servlet实现类即可。**重写doGet(HttpServletRequest req, HttpServletResponse resp)和doPost(HttpServletRequest req, HttpServletResponse resp)方法实现请求处理，不再需要重写service(ServletRequest req, ServletResponse res)方法了。

- 又因为我们业务中get，post的处理方式又都是一样的，所以我们只需要写一种方法即可，使用另外一种方法调用我们写好的doXXX方法。web.xml配置与之前相同。

  ```java
  //处理浏览器的get请求
  doGet(HttpServletRequest request, HttpServletResponse response){
  	//业务代码
  }
  //处理浏览器的post请求
  doPost(HttpServletRequest request, HttpServletResponse response){
      doGet(request, response);
  }
  ```




## 第7章 其它几个重要的接口

### 7.1 HttpServletRequest接口

- 该接口是ServletRequest接口的子接口，封装了HTTP请求的相关信息。

- 浏览器请求服务器时会封装请求报文交给服务器，服务器接受到请求会将请求报文解析生成request对象。
- 由Servlet容器创建其实现类对象并传入service(HttpServletRequest req, HttpServletResponse res)方法中。

- 以下我们所说的HttpServletRequest对象指的是容器提供的HttpServletRequest实现类对象。

HttpServletRequest对象的主要功能有：

#### 7.1.1 获取请求参数

- 什么是请求参数？

  - 请求参数就是浏览器向服务器提交的数据。

- 浏览器向服务器如何发送数据？

  ① 附在url后面，如：

  [http](http://localhost:8989/MyServlet/MyHttpServlet?userId=20)[://](http://localhost:8989/MyServlet/MyHttpServlet?userId=20)[localhost:8080/MyServlet/MyHttpServlet?userId=20](http://localhost:8080/MyServlet/MyHttpServlet?userId=20)

  ② 通过表单提交

  ```html
  <form action="MyHttpServlet" method="post">
  	你喜欢的足球队<br /><br />
  	巴西<input type="checkbox" name="soccerTeam" value="Brazil" />
  	德国<input type="checkbox" name="soccerTeam" value="German" />
  	荷兰<input type="checkbox" name="soccerTeam" value="Holland" />
  	<input type="submit" value="提交" />
  </form>
  
  ```

- 使用HttpServletRequest对象获取请求参数

  ```java
  //一个name对应一个值
  String userId = request.getParameter("userId");
  ```

  ```java
  //一个name对应一组值
  String[] soccerTeams = request.getParameterValues("soccerTeam");
  for(int i = 0; i < soccerTeams.length; i++){
  	System.out.println("team "+i+"="+soccerTeams[i]);
  }
  ```

#### 7.1.2 获取url地址参数

```java
String path = request.getContextPath();//重要
System.out.println("上下文路径："+path);
System.out.println("端口号："+request.getServerPort());
System.out.println("主机名："+request.getServerName());
System.out.println("协议："+request.getScheme());
```

#### 7.1.3 获取请求头信息

![1557996350220](images/1557996350220.png)

```java
String header = request.getHeader("User-Agent");
System.out.println("user-agent:"+header);
String referer = request.getHeader("Referer");
System.out.println("上个页面的地址："+referer);//登录失败，返回登录页面让用户继续登录
```

#### 7.1.4 请求的转发

将请求转发给另外一个URL地址，参见第7章-请求的转发与重定向。

```java
//获取请求转发对象
RequestDispatcher dispatcher = request.getRequestDispatcher("success.html");
dispatcher.forward(request, response);//发起转发
```

#### 7.1.5 向请求域中保存数据

```java
//将数据保存到request对象的属性域中
request.setAttribute("attrName", "attrValueInRequest");
//两个Servlet要想共享request对象中的数据，必须是转发的关系
request.getRequestDispatcher("/ReceiveServlet").forward(request, response);

```

```java
//从request属性域中获取数据
Object attribute = request.getAttribute("attrName");
System.out.println("attrValue="+attribute);

```



### 7.2 HttpServletResponse接口

- 该接口是ServletResponse接口的子接口，封装了服务器针对于HTTP响应的相关信息。(暂时只有服务器的配置信息，没有具体的和响应体相关的内容)

- 由Servlet容器创建其实现类对象，并传入service(HttpServletRequest req, HttpServletResponse res)方法中。

- 后面我们所说的HttpServletResponse对象指的是容器提供的HttpServletResponse实现类对象。

HttpServletResponse对象的主要功能有：

#### 7.2.1 使用PrintWriter对象向浏览器输出数据

```java
//通过PrintWriter对象向浏览器端发送响应信息
PrintWriter writer = res.getWriter();
writer.write("Servlet response");
writer.close();
```

- 写出的数据可以是页面、页面片段、字符串等

- 当写出的数据包含中文时，浏览器接收到的响应数据就可能有乱码。为了避免乱码，可以使用Response对象在向浏览器输出数据前设置响应头。

#### 7.2.2 设置响应头

- 响应头就是浏览器解析页面的配置。比如：告诉浏览器使用哪种编码和文件格式解析响应体内容

```java
response.setHeader("Content-Type", "text/html;charset=UTF-8");
```

- 设置好以后，会在浏览器的响应报文中看到设置的响应头中的信息。

#### 7.2.2 重定向请求

- 实现请求重定向，参见第8章-请求的转发与重定向。
- 举例：用户从login.html页面提交登录请求数据给LoginServlet处理。如果账号密码正确，需要让用户跳转到成功页面，通过servlet向响应体中写入成功页面过于复杂，通过重定向将成功页面的地址交给浏览器并设置响应状态码为302，浏览器会自动进行跳转。

```java
//注意路径问题，加上/会失败，会以主机地址为起始，重定向一般需要加上项目名
response.sendRedirect(“success.html”);
```



## 第8章 请求的转发与重定向

请求的转发与重定向是web应用页面跳转的主要手段，在Web应用中使用非常广泛。所以我们一定要搞清楚他们的区别。

![1562000421414](images/1562000421414.png)

### 8.1 请求的转发

![1557754164834](images/1557754164834.png)

- 第一个Servlet接收到了浏览器端的请求，进行了一定的处理，然后没有立即对请求进行响应，而是将请求“交给下一个Servlet”继续处理，下一个Servlet处理完成之后对浏览器进行了响应。**在服务器内部将请求“交给”其它组件继续处理就是请求的转发。**对浏览器来说，一共只发了一次请求，服务器内部进行的“转发”浏览器感觉不到，同时浏览器地址栏中的地址不会变成“下一个Servlet”的虚拟路径。
- HttpServletRequest代表HTTP请求，对象由Servlet容器创建。转发的情况下，两个Servlet可以共享同一个Request对象中保存的数据。
- 当需要将后台获取的数据传送到JSP上显示的时候，就可以先将数据存放到Request对象中，再转发到JSP从属性域中获取。此时由于是“转发”，所以它们二者共享Request对象中的数据。
- 转发的情况下，可以访问WEB-INF下的资源。
- **转发以“/”开始表示项目根路径，重定向以”/”开始表示主机地址。**
- 功能：
  - 获取请求参数
  - 获取请求路径即URL地址相关信息
  - 在请求域中保存数据
  - 转发请求
- 代码举例

```java
protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
	//1.使用RequestDispatcher对象封装目标资源的虚拟路径
	RequestDispatcher dispatcher = request.getRequestDispatcher("/index.html");
	//2.调用RequestDispatcher对象的forward()方法“前往”目标资源
	//[注意：传入的参数必须是传递给当前Servlet的service方法的
	//那两个ServletRequest和ServletResponse对象]
	dispatcher.forward(request, response);
}

```

### 8.2 请求的重定向

![1557754122187](images/1557754122187.png)

- 第一个Servlet接收到了浏览器端的请求，进行了一定的处理，然后给浏览器一个特殊的响应消息，这个特殊的响应消息会通知浏览器去访问另外一个资源，这个动作是服务器和浏览器自动完成的。**整个过程中浏览器端会发出两次请求**，且在**浏览器地址栏里面能够看到地址的改变**，改变为下一个资源的地址。

- 重定向的情况下，原Servlet和目标资源之间就不能共享请求域数据了。

- HttpServletResponse代表HTTP响应，对象由Servlet容器创建。

- 功能：

  - 向浏览器输出数据
  - 重定向请求

- 重定向的响应报文的头

  ```
  HTTP/1.1 302 Found
  Location: success.html
  ```

- 应用：

  - 用户从login.html页面提交登录请求数据给LoginServlet处理。

    如果账号密码正确，需要让用户跳转到成功页面，通过servlet向响应体中写入成功页面过于复杂，通过重定向将成功页面的地址交给浏览器并设置响应状态码为302，浏览器会自动进行跳转

- 代码举例：

```java
protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
	//1.调用HttpServletResponse对象的sendRedirect()方法
	//2.传入的参数是目标资源的虚拟路径
	response.sendRedirect("index.html");
}

```

### 8.3 对比请求的转发与重定向

|                         | 转发                             | 重定向                                              |
| ----------------------- | -------------------------------- | --------------------------------------------------- |
| 浏览器感知              | 在服务器内部完成，浏览器感知不到 | 服务器以302状态码通知浏览器访问新地址，浏览器有感知 |
| 浏览器地址栏            | 不改变                           | 改变                                                |
| 整个过程发送请求次数    | 一次                             | 两次                                                |
| 执行效率                | 效率高                           | 效率低                                              |
| API（或发起者）         | Request对象                      | Response对象                                        |
| 能否共享request对象数据 | 能                               | 否                                                  |
| WEB-INF下的资源         | 能访问                           | 不能访问                                            |
| 目标资源                | 必须是当前web应用中的资源        | 不局限于当前web应用                                 |

> 说明1：默认情况下，浏览器是不能访问服务器web-inf下的资源的，而服务器是可以访问的。
>
> 说明2：浏览器默认的绝对路径：http://localhost:8080/
>
> ​              服务器项目的代码中的绝对路径：http://localhost:8080/项目名/

## 第9章 请求与响应中的字符编码设置

### 9.1 字符编码问题

- 我们web程序在接收请求并处理过程中，如果不注意编码格式及解码格式，很容易导致中文乱码，引起这个问题的原因到底在哪里？如何解决？我们这个小节将会讨论此问题。
- 说到这个问题我们先来说一说字符集。
  - 什么是字符集，就是各种字符的集合，包括汉字，英文，标点符号等等。各国都有不同的文字、符号。这些文字符号的集合就叫字符集。
  - 现有的字符集ASCII、GB2312、BIG5、GB18030、Unicode、UTF-8、ISO-8859-1等
- 这些字符集，集合了很多的字符，然而，字符要以二进制的形式存储在计算机中，我们就需要对其进行编码，将编码后的二进制存入。取出时我们就要对其解码，将二进制解码成我们之前的字符。这个时候我们就需要制定一套编码解码标准。否则就会导致出现混乱，也就是我们的乱码。

### 9.2 编码与解码

- 编码：将字符转换为二进制数

| 汉字 | 编码方式   | 编码       | 二进制                                         |
| ---- | ---------- | ---------- | ---------------------------------------------- |
| ‘中' | **GB2312** | **D6D0**   | **1101 0110-1101 0000**                        |
| ‘中' | **UTF-16** | **4E2D**   | **0100 1110-0010 1101**                        |
| ‘中' | **UTF-8**  | **E4B8AD** | **1110** **0100-** **1011** **1000-1010 1101** |

- 解码：将二进制数转换为字符

1110 0100-1011 1000-1010 1101 → E4B8AD → ’中’

- 乱码：一段文本，使用A字符集编码，使用B字符集解码，就会产生乱码。所以解决乱码问题的根本方法就是统一编码和解码的字符集。

![1558009252673](images/1558009252673.png)

### 9.3 解决请求乱码问题

解决乱码的方法：就是统一字符编码。

![1558009756944](images/1558009756944.png)

#### 9.3.1 GET请求

- GET请求参数是在地址后面的。我们需要修改tomcat的配置文件。需要在server.xml文件修改Connector标签，添加URIEncoding="utf-8"属性。

![1561220531242](images/1561220531242.png)

- 一旦配置好以后，可以解决当前工作空间中所有的GET请求的乱码问题。

#### 9.3.2 POST请求

- post请求提交了中文的请求体，服务器解析出现问题。

- 解决方法：在获取参数值之前，设置请求的解码格式，使其和页面保持一致。

  ```java
  request.setCharacterEncoding("utf-8");
  ```

- POST请求乱码问题的解决，只适用于当前的操作所在的类中。不能类似于GET请求一样统一解决。因为请求体有可能会上传文件。不一定都是中文字符。

### 9.4 解决响应乱码问题

- 向浏览器发送响应的时候，要告诉浏览器，我使用的字符集是哪个，浏览器就会按照这种方式来解码。如何告诉浏览器响应内容的字符编码方案。很简单。

- 解决方法一：

  ```java
  response.setHeader("Content-Type", "text/html;charset=utf-8");
  ```

- 解决方法二

  ```java
  response.setContentType("text/html;charset=utf-8");
  ```

  > 说明：有的人可能会想到使用response.setCharacterEncoding(“utf-8”)，设置reponse对象将UTF-8字符串写入到响应报文的编码为UTF-8。只这样做是不行的，还必须手动在浏览器中设置浏览器的解析用到的字符集。

## 第10章 Web应用路径设置

**问题再现：**

① 创建Web应用Path，目录结构如图所示

![1558010305136](images/1558010305136.png)

② 在a.html中有超链接\<a href="b.html">To b.html</a>

③ 如果先直接访问a.html，再通过超链接转到b.html没有问题。

④ 如果先通过TestServlet转发到a.html，则浏览器地址栏会变成 http://localhost:8989/Path/TestServlet

此时再点击超链接\<a href="b.html">To b.html</a>就会发生问题，找不到b.html。

**如何解决？**

原因是超链接\<a href="b.html">To b.html</a>使用的是相对路径，浏览器进行解析时，只能以当前浏览器地址栏里的路径为基准。例如，当前浏览器地址栏里的内容是：http://localhost:8989/Path/TestServlet

那么经过浏览器解析后b.html的访问地址就成了：http://localhost:8989/Path/TestServletb.html这显然无法访问到b.html。

完整的url构成如下图：

![1558010582082](images/1558010582082.png)

**相对路径和绝对路径**

**相对路径：虚拟路径如果不以“/”开始，就是相对路径**，浏览器会以当前资源所在的虚拟路径为基准对相对路径进行解析，从而生成最终的访问路径。此时如果通过转发进入其他目录，再使用相对路径访问资源就会出错。所以为了防止路径出错，我们经常将相对路径转化为绝对路径的形式进行请求。

**绝对路径：虚拟路径以“/”开始，就是绝对路径。**
**① 在服务器端**：虚拟路径最开始的“/”表示当前Web应用的根目录。只要是服务端解析的绝对路径，都是以web根目录为起始的。由服务器解析的路径包括：<1> web.xml的配置路径、<2>request转发的路径。
**② 在浏览器端**：虚拟路径最开始的“/”表示当前主机地址。
例如：链接地址“/Path/dir/b.html”经过浏览器解析后为：
相当于http://localhost:8989/Path/dir/b.html
由浏览器解析的路径包括：
<1>重定向操作：response.sendRedirect("/xxx")
<2>所有HTML标签：\<a href="/xxx"> 、\<form action="/xxx"> 、link、img、script等
这些最后的访问路径都是 http://localhost:8989/xxx

所以我们可以看出，如果是浏览器解析的路径，我们必须加上项目名称才可以正确的指向资源。[http://localhost:8989](http://localhost:8989/Path/xxx)[/Path](http://localhost:8989/Path/xxx)[/xxx](http://localhost:8989/Path/xxx)

<1>重定向操作：

```java
response.sendRedirect(request.getContextPath()+"/xxx");
```

<2>所有HTML标签：\<a href="/项目名/xxx">；  \<form action=“/项目名/xxx">



- 在浏览器端，除了使用绝对路径之外，我们还可以使用base标签+相对路径的方式来确定资源的访问有效。
- base标签影响当前页面中的所有相对路径，不会影响绝对路径。相当于给相对路径设置了一个基准地址。
- 习惯上在html的<head>标签内，声明：

```html
<!-- 给页面中的相对路径设置基准地址 -->
<base href="http://localhost:8080/Test_Path/"/>
```

接着html中的路径就可以使用相对路径的方式来访问。比如：

```html
<h4> base+相对路径</h4>
<!-- <base href="http://localhost:8080/Test_Path/"/> -->
<a href="1.html">1.html</a><br/>
<a href="a/3.html">a/3.html</a><br/>
<!-- servlet映射到了项目根目录下，可以直接访问 -->
<a href="PathServlet">PathServlet</a><br/>
```



## 第11章 Web应用中的常见问题

我们经常需要把我们的项目导入到另外的地方，或者把别的工程导入到我们自己的eclipse里面进行开发。但是导入工程后很可能会发生问题，如何解决这些问题，我们下面做统一的分析解决。

### 问题一：如何导入外部的工程

![1558008615831](images/1558008615831.png)

![1558008624987](images/1558008624987.png)

![1558008631864](images/1558008631864.png)

### 问题二：导入后可能存在的问题

1、如果项目出现叹号，说明环境有问题

![1562137419498](images/1562137419498.png)

解决办法：

项目上点击右键 -->buildpath -->查看未绑定的包以及环境，并绑定

2、绑定完成环境如果出现叉号，特别是关于servlet之类。我们需要选择一下当前的服务器环境，还在buildpath里面。

![1562137450633](images/1562137450633.png)

![1558008983884](images/1558008983884.png)

![1558009013481](images/1558009013481.png)

同时，如果要导入的项目与自己电脑的JDK或Tomcat版本不同，需要修改为自己的版本：

![1561220378575](images/1561220378575.png)

# JSP

## 第一章 为什么学习Jsp

### 1.1 现有技术不足

Servlet可以通过转发或重定向跳转到某个HTML文档。但HTML文档中的内容不受Servlet的控制。比如登录失败时，跳转回登录表单页面无法显示诸如“用户名或密码不正确”的错误消息，所以我们目前采用的办法是跳转到一个错误信息页面。如果通过Servlet逐行输出响应信息则会非常繁琐。

**Servlet输入html页面的程序代码：**

```java
package com.atguigu.servlet;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HtmlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 设置返回的数据内容的数据类型和编码
		response.setContentType("text/html; charset=utf-8");
		// 获取字符输出流
		Writer writer = response.getWriter();
		//输出页面内容！
		writer.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		writer.write("<html>");
		writer.write("<head>");
		writer.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		writer.write("<title>Insert title here</title>");
		writer.write("</head>");
		writer.write("<body>");
		writer.write("这是由Servlet程序输出的html页面内容！");
		writer.write("</body></html>");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}

```

**接着，在浏览器中输入访问Servlet的程序地址得到以下结果：**

![1558024339505](images/1558024339505.png)

上面的代码我们不难发现。通过Servlet输出简单的html页面信息都非常不方便。那我们要输出一个复杂页面的时候，就更加的困难，而且不利于页面的维护和调试。

### 1.2 Servlet与HTML

|      | Servlet                            | HTML               |
| ---- | ---------------------------------- | ------------------ |
| 长处 | 接收请求参数，访问域对象，转发页面 | 以友好方式显示数据 |
| 短处 | 以友好方式显示数据                 | 动态显示数据       |

### 1.3 总结

那能否将Servlet和HTML二者的长处结合起来呢？

能！sun公司推出一种叫做JSP的动态页面技术帮助我们实现对页面输出繁锁工作。



## 第二章 Jsp简介

### 2.1 Jsp全称

- Jsp全称Java Server Pages，顾名思义就是运行在java服务器中的页面。由Sun 公司专门为了解决动态生成HTML文档的技术，也就是在我们JavaWeb中的动态页面。
- Jsp能够以HTML页面的方式呈现数据，是一个可以嵌入Java代码的HTML。
- Jsp其本质就是一个Servlet。Servlet能做的事情JSP都能做。
- Jsp必须运行在服务器中，不能直接使用浏览器打开。
- Jsp是Web网页的技术标准,主要语法组成包括：指令，html模板元素，脚本片段（小脚本），表达式，声明，注释，后缀是*.jsp。

### 2.2 Jsp与Html的区别

- Jsp是动态页面，html是静态页面。

  |          | 动态页面                                     | 静态页面                           |
  | -------- | -------------------------------------------- | ---------------------------------- |
  | 运行原理 | 通过服务器解析后，将数据在浏览器中显示       | 直接在浏览器中解析运行             |
  | 维护成本 | 较低，可以修改后台数据，进而影响页面中的数据 | 较高，必须将修改后的页面覆盖原页面 |
  | 数据库   | 可以连接数据库                               | 不可连接数据库                     |
  | 访问速度 | 较慢                                         | 较快                               |
  | 书写代码 | 可以书写java代码                             | 不能书写java代码                   |

### 2.3 Jsp与Servlet分工

- Jsp本质是一个Servlet ，翻译后的文件结构为：class helloworld_jsp : HttpJspBase : HttpServlet。
- Jsp主要负责显示及获取数据，从表面上看，Jsp相对于在html中嵌入java代码：jsp=html+java。
- Servlet主要负责处理业务，从表面上看，Servlet相当于在java中嵌入html代码：Servlet=java+html。
- 总结：相比于Servlet，Jsp更加善于处理显示页面，而Servlet更善于处理业务逻辑，两种技术各有专长，所以一般我们会将Servlet和Jsp结合使用，Servlet负责业务，Jsp负责显示。

### 2.4 Jsp基本格式

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	这是我的第一个jsp页面。
</body>
</html>
```



## 第三章 Jsp的初体验

### 3.1 创建一个JSP动态页面程序：HelloWorld

**① 选中WebContent目录，右键创建一个jsp文件**

![1558024530231](images/1558024530231.png)

**②  修改jsp页面的文件名**

![1558024571910](images/1558024571910.png)

**③ 选择生成jsp文件的模板，我们选择默认的New JSP File(html)**

![1558024609595](images/1558024609595.png)

**④ 在body标签中添加你想要显示的文本内容**

![1558024688103](images/1558024688103.png)

**⑤ 然后在浏览器中输入jsp页面的访问地址**

- jsp页面的访问地址和html页面的访问路径一样http://ip:端口号/工程名/文件名。也就是http://127.0.0.1:8080/day08/index.jsp

  ![1558024743613](images/1558024743613.png)



### 3.2 修改jsp文件的默认编码

![1558024995955](images/1558024995955.png)

**注意事项：**

1、jsp页面是一个类似于html的一个页面。 jsp直接存放到WebContent目录下，和html一样访问jsp的时候，也和访问html一样

2、jsp的默认编码集是iso-8859-1，修改jsp的默认编码为UTF-8

### 3.3 JSP运行原理

- jsp的本质其实是一个Servlet程序。

  - 实际上Tomcat在运行JSP时，并不是直接显示的我们所编写的JSP页面，而是将JSP页面转换成了一个Java类，这个Java类是什么，我想大家也能猜到了，它实际上就是一个Servlet。

- 这个Servlet在哪呢？还记得我们说过的Tomcat的work目录吗？在那个目录下保存着Tomcat自动生成的一些内容，下面让我们来找到那个目录。

  - 对于Eclipse来说是在：工作空间下的.metadata\.plugins\org.eclipse.wst.server.core\tmp0
  - 对于MyEclipse来说就可以直接去Tomcat的安装目录去查找
  - 对于IDEA来说，目录：C:\Users\admin\.IntelliJIdea2019.2\system\tomcat\Tomcat_7_0_79_web_0621_4\work\Catalina\localhost\day07_jsp\org\apache\jsp

- 在work目录下的...work\Catalina\localhost\day07_jsp\org\apache\jsp文件夹中我们可以发现两个文件index_jsp.java和index_jsp.class，前者就是Tomcat自动生成的Servlet的源码，后者是编译后的.class文件。

  - ![1558366745836](images/1558366745836.png)

- 打开index_jsp.java文件部分内容如下：

  - 图一：

    ![jsp翻译servlet后部分源码1](images/jsp翻译servlet后部分源码1.png)

    我们打开index_jsp.java文件查看里面的内容：发现，生成的类继承于HttpJspBase类。这是一个jsp文件生成Servlet程序要继承的基类！于是，我们关联源代码。去查看一下HttpJspBase类的内容。从源码的类注释说明中，我们发现。HttpJspBase这个类就是所有JSP文件生成Servlet程序需要去继承的基类。并且这个HttpJspBase类继承于HttpServlet类。我们访问JSP时服务器就是调用了该Servlet来响应请求。所以JSP也是一个Servlet小程序。

    ![1558367072400](images/1558367072400.png)

    我们分别在工程的WebContent目录下创建多个jsp文件。然后依次访问。它们都被翻译为.java文件并编译成为.class字节码文件。

    ![1558367125616](images/1558367125616.png)

    我们顺着代码向下看，会发现_jspService()方法。会发现有九个对象（实际上默认会看到八个），这是后面要讲到的重点。

  - 图二：

    ![jsp翻译servlet后部分源码2](images/jsp翻译servlet后部分源码2.png)

**小结：**			

从生成的文件我们不难发现一个规则：

a.jsp 翻译成 java文件后的全名是    a_jsp.java文件

b.jsp 翻译成 java文件后的全名是    b_jsp.java文件

**那么当我们访问 一个xxx.jsp文件后翻译成java文件的全名是  xxx_jsp.java文件。**

**xxx_jsp.java文件是一个Servlet程序。原来jsp中的html内容都被翻译到Servlet类的service方法中原样输出。**

![1558023396492](images/1558023396492.png)

下一个问题：**Servlet是需要在web.xml中配置的**，而我们并没有配置JSP的serlvet映射，那他是如何访问的呢？实际在tomcat下的conf目录中的**web.xm**l早已配置好了JSP的映射信息，具体内容如下：

```xml
<servlet>
	<servlet-name>jsp</servlet-name>
    <servlet-class>org.apache.jasper.servlet.JspServlet</servlet-class>
    <init-param>
		<param-name>fork</param-name>
		<param-value>false</param-value>
	</init-param>
	<init-param>
		<param-name>xpoweredBy</param-name>
		<param-value>false</param-value>
	</init-param>
	<load-on-startup>3</load-on-startup>
</servlet>

<servlet-mapping>
	<servlet-name>jsp</servlet-name>
	<url-pattern>*.jsp</url-pattern>
</servlet-mapping>

<servlet-mapping>
	<servlet-name>jsp</servlet-name>
	<url-pattern>*.jspx</url-pattern>
</servlet-mapping>
```

#### 总结Jsp运行原理

1. **第一次访问jsp页面时，服务器会将xxx.jsp文件翻译成xxx_jsp.java文件，再编译成xxx_jsp.class文件。** 
2. **以后在访问同一个jsp文件**
   - **如果文件未改变，不会被翻译和编译**
   - **如果文件改变，会翻译和编译**

## 第四章 Jsp基本语法

### 4.1指令

- 语法格式：<%@ %>  

- 实例

  ```
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
  ```

- 三大指令：<%@ page %> ，<%@ include %> ，| <%@ taglib %> 

### 4.2模板元素

- html&css&js&jQuery等...

### 4.3代码脚本片段（重点）

- 格式 ：<%%>

- 作用：在_jspService()方法中，书写java代码。

- 实例

  ```jsp
  <% int i = 0;%>
  ```

### 4.4表达式（重点）

- 格式： <%=%>

- 作用：将数据显示到页面，与out.print()或out.write()作用相同。

- 实例

  ```jsp
   <%=i%>
  ```

### 4.5声明（了解）

- 格式：<%!%> 
- 作用：在翻译后的class helloworld_jsp这个Servlet类中，书写java代码。

### 4.6注释：Jsp支持三种注释

- java：单行注释：//，多行注释：/**/ 
- html：<!-- -->
- jsp：<%-- --%>
- jsp中三种注释的比较，如下所示：

|          | JSP注释 | Java注释 | HTML注释 |
| -------- | ------- | -------- | -------- |
| JSP页面  | 可见    | 可见     | 可见     |
| Java代码 | 不可见  | 可见     | 可见     |
| 浏览器   | 不可见  | 不可见   | 可见     |



## 第五章 Jsp常用指令

### 5.1 语法格式

- <%@ 指令名   属性=属性值	属性2=属性值2  ... %> 

### 5.2 Jsp常用指令

#### 5.2.1 page指令

- 语法

  ```jsp
  <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
  ```

- 属性

  - language：语言，值为java且仅java。
  - contentType：与response.setContentType()作用一致，设置浏览器字符集。
  - pageEncoding：设置Jsp页面的编码字符集。
  - import：导包
  - isErrorPage：设置当前页面是否为错误页面，默认值"false"。
    - ​    true：设置当前页面为错误页面，可以使用exception内置对象，捕获异常 。
    - ​    false：设置当前页面不是错误页面，不可以使用exception内置对象，捕获异常 。
  - errorPage：设置当前页面错误时的跳转目标页面。错误需要在_jspService()中才可以捕获。

#### 5.2.2 include指令:静态包含

- 语法

  ```jsp
  <%@include file="被包含文件的路径" %> 	
  ```

- 作用：将目标文件包含到当前文件中。

- 特点：被包含的文件不会被翻译&编译。（先包含，再翻译)

#### 5.2.3 taglib指令（略）

- 语法

  ```jsp
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
  ```

- 属性

  - prefix用来指定前缀名，我们通过该名来使用JSTL。
  - uri相当于库的唯一标识，因为JSTL由多个不同的库组成，使用该属性指定要导入哪个库。

- 作用：引入标签库。



## 第六章 Jsp常用动作标签

### 6.1 概述

- JSP动作标签与HTML标签不同，HTML标签由浏览器来解析，而JSP动作标签需要服务器（Tomcat）来运行。

### 6.2 常用的JSP动作标签

#### 6.2.1  转发动作标签

- 语法：\<jsp:forward>\</jsp:forward>

- 作用：在页面中用于转发操作

- 实例

  ```jsp
  <jsp:forward page="target.jsp"></jsp:forward>
  ```

- 转发子标签

  - 语法：<jsp:param value="paramValue" name="paramName"/>

  - 作用：在转发时设置请求参数，通过request.getParameter()在目标页面获取请求参数。

  - 实例

    ```jsp
    <jsp:forward page="target.jsp">
    	<jsp:param value="paramValue" name="paramName"/>
    </jsp:forward>
    ```

  - 注意：如果转发动作标签不需要设置请求参数，该标签开始与结束标签内部，不允许书写任何内容，（包括空格）

#### 6.2.2 动态包含动作标签

- 语法：<jsp:include page=*"target.jsp"*>

- 作用：动态包含，将其他页面包含到当前页面中。

- 实例

  ```jsp
  <jsp:include page="target.jsp"></jsp:include>
  ```

- 特点：被包含的文件同时会被翻译&编译。（先翻译，再包含）

  - 本质原理：当使用动态包含时，Tomcat会在生成的Servlet中加入如下代码：

    ```java
    org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "target.jsp", out, false);
    ```

### 6.3 动态包含与静态包含的区别

|                    | @include指令                                                 | <jsp:include>标签                                          |
| ------------------ | ------------------------------------------------------------ | ---------------------------------------------------------- |
| 特点               | 静态包含                                                     | 动态包含                                                   |
| 语法的基本形式     | <%@ include   file=”…”%>                                     | <jsp:include   page=”…”/>                                  |
| 包含动作发生的时机 | 翻译期间                                                     | 请求期间                                                   |
| 是否生成java文件   | 不生成                                                       | 生成                                                       |
| 合并方式           | 代码复制                                                     | 合并运行结果                                               |
| 包含的内容         | 文件实际内容                                                 | 页面输出结果                                               |
| 代码冲突           | 有可能                                                       | 不可能                                                     |
| 编译次数           | 1                                                            | 包含的文件 + 1                                             |
| 适用范围           | 适用包含纯静态内容(CSS,HTML,JS)，或没有非常耗时操作。或少量java代码的jsp | 包含需要传递参数。含有大量java代码，运算，耗时很长的操作。 |



## 第七章 Jsp九大隐式对象

**（隐含对象|内置对象）**

### 7.1 概述

> Jsp共有九大隐式对象，也叫隐含对象或内置对象。JSP隐式对象是JSP容器为每个页面提供的Java对象，开发者可以直接使用它们而不用显式声明。JSP隐式对象也被称为预定义变量。

- 对象详情见：工作原理图二。

### 7.2 对象详情

#### 7.2.1 pageContext

- 类型：PageContext
- 定义：代表页面域对象，用来代表整个JSP页面。
- 作用：
  1. 页面域对象，具体详见：下方四大域对象。
  2. 九大隐式对象的“大哥”，可以直接调用其他八大隐式对象。
- 在Servlet中获取方式：无。

#### 7.2.2 request

- 类型：HttpServletRequest

- 定义：代表浏览器向服务器发送的请求报文，该对象由服务器创建，最终以参数的形式发送到doGet()和doPost()方法中。

  > 每当客户端请求一个JSP页面时，JSP引擎就会制造一个新的request对象来代表这个请求。request对象提供了一系列方法来获取HTTP头信息，cookies，HTTP方法等等。

- 作用（详见Servlet中request对象）

  1.  获取请求参数
  2.  获取url地址参数
  3.  请求转发
  4.  向请求域中保存数据（获取数据&移除数据）
  5.  获取请求头信息

- 在Servlet中获取方式：doGet()或doPost()中直接使用。

#### 7.2.3 session

- 类型：HttpSession

- 定义：代表浏览器与服务器之间的会话。

- 作用

  - 会话域对象，具体详见：下方四大域对象。

  > session对象用来跟踪在各个客户端请求间的会话。

- 在Servlet中获取方式 ：request.getSession();

#### 7.2.4 application

- 类型：ServletContext

- 定义：Servlet上下文，代表当前web应用。

  > Web容器在启动时，它会为**每个Web应用程序都创建一个唯一对应的ServletContext对象**，意思是Servlet上下文，**代表当前Web应用。**

- 作用

  1.  获取项目的上下文路径(带/的项目名)：**getContextPath()**

  2.  获取虚拟路径所映射的本地真实路径：**getRealPath(String path)**

  3.  获取WEB应用程序的全局初始化参数（基本不用）

      - 设置Web应用初始化参数的方式是在web.xml的根标签下加入如下代码

        ```xml
        <web-app>
        	<!-- Web应用初始化参数 -->
        	<context-param>
        		<param-name>ParamName</param-name>
        		<param-value>ParamValue</param-value>
        	</context-param>
        </web-app>
        ```

        

      - 获取Web应用初始化参数

        ```java
        @Override
        public void init(ServletConfig config) throws ServletException {
        	//1.获取ServletContext对象
        	ServletContext application = config.getServletContext();
        	//2.获取Web应用初始化参数
        	String paramValue = application.getInitParameter("ParamName");
        	System.out.println("全局初始化参数paramValue="+paramValue);
        }
        ```

  4. 作为域对象共享数据:具体详见：下方四大域对象。

- 在Servlet中获取方式：使用this.getServletContext()方法获取。

#### 7.2.5 page

- 类型：Object
- 作用：this，当前类对象。

#### 7.2.6 response

- 类型：HttpServletResponse
- 定义：代表服务器向浏览器发送的响应报文，该对象由服务器创建，最终以参数的形式发送到doGet()和doPost()方法中。
- 作用：
  1. 向页面（响应体）中响应数据，数据包括文本、Html等。
  2. 重定向
  3. 设置响应头信息
- 在Servlet中获取方式：doGet()或doPost()中直接使用

#### 7.2.7 config

- 类型：ServletConfig
- 定义：代表当前Servlet的配置信息，每一个Servlet都有一个唯一对应的ServletConfig对象。
- 作用：
  1. 获取Servlet名称：getServletName()
  2. 获取全局上下文ServletContext对象：getServletContext()
  3. 获取Servlet初始化参数：getInitParameter(String) / getInitParameterNames()。
- 在Servlet中获取方式：this.getServletConfig()

#### 7.2.8 out

- 类型：JspWriter
- 定义：代表当前页面的输出流。
- 作用：与Servlet中的PrintWriter功能类似，将数据响应到页面，响应的数据可以是页面、页面片段、字符串等。
- 在Servlet中获取方式：无

#### 7.2.9 exception

- 类型：Throwable
- 定义：代表当前页面的异常对象。
- 作用：捕获处理页面中的异常信息。
- 在Servlet中获取方式：new Throwable()

**九大内置对象，都是我们可以在【代码脚本】中或【表达式脚本】中直接使用的对象。**



## 第八章 Jsp四大域对象

### 8.1 域对象概述

> ​	生活中使用“域对象”比较经典的行业，是快递行业。现如今快递行业大体分为，全球快递，全国快递，同城快递和同区快递。需求不同，使用不同“域对象”。
>
> ​	如：外卖一般使用同区快递，给北京朝阳区的朋友邮寄贺卡，一般使用同城快递。在某宝某东上购买外地商品，一般使用全国快递或全球快递。

### 8.2 程序中的域对象

#### 8.2.1 域对象概述

> 程序中的域对象，主要负责在不同web资源之间进行数据交换，（如:servlet和jsp之间的数据交换）。由于不同的web资源之间需要共享数据，所以就有了域对象。
>
> 在Jsp中一共有四个域对象，分别是pageContext 、request、session、application。主要作用是能够在一定范围内共享数据。

#### 8.2.2 域对象分析

**每个域对象内部都维护了一个Map<String , Object>，域对象的共同方法。**

- 设置属性到域中：void setAttribute(String key , Object value);
- 从域中获取指定的属性：Object  getAttribute(String key);
- 移除域中指定属性：void removeAttribute(String key);

#### 8.2.3 域对象有效性

- pageContext: 当前页面中共享数据有效，离开当前页面失效。
  - 每个页面都有自己唯一的一个pageContext对象。
  - 注意servlet中没有该对象。
- request： 当前请求中共享数据有效。
  - 当前请求：转发、直接访问一个页面为当前请求。
  - 不在当前请求：重定向、 打开页面再点击页面中的超链接不在当前请求 。
- session： 一次会话范围中共享数据有效。
  - 当前会话：当前浏览器不关闭&不更换浏览器即为当前会话。
  - 只关心浏览器是否关闭，不关心服务器关闭重启。
  - 不同浏览器不共享会话。
- application： 在服务器运行的一次过程中共享数据有效。
  - 服务器关闭销毁

**小结**：

| 域对象      | 作用范围    | 起始时间    | 结束时间    |
| ----------- | ----------- | ----------- | ----------- |
| pageContext | 当前JSP页面 | 页面加载    | 离开页面    |
| request     | 同一个请求  | 收到请求    | 响应        |
| session     | 同一个会话  | 开始会话    | 结束会话    |
| application | 当前Web应用 | Web应用加载 | Web应用卸载 |

#### 8.2.4 四个作用域的测试代码

- 新建两个jsp页面。分别取名叫：context1.jsp，context2.jsp

  - context1.jsp的页面代码如下：

    ```jsp
    <%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    </head>
    <body>
    	这是context1页面<br/>
    	<%
    		//设置page域的数据
    		pageContext.setAttribute("key", "pageContext-value");
    		//设置request域的数据
    		request.setAttribute("key", "request-value");
    		//设置session域的数据
    		session.setAttribute("key", "session-value");
    		//设置application域的数据
    		application.setAttribute("key", "application-value");
    	%>
    	<%-- 测试当前页面作用域 --%>
    	<%=pageContext.getAttribute("key") %><br/>
    	<%=request.getAttribute("key") %><br/>
    	<%=session.getAttribute("key") %><br/>
    	<%=application.getAttribute("key") %><br/>
    	<%	
    		// 测试request作用域
    // 		request.getRequestDispatcher("/context2.jsp").forward(request, response);
    	%>
    </body>
    </html>
    ```

  - context2.jsp的页面代码如下：

    ```jsp
    <%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    </head>
    <body>
    	这是context2页面 <br/>
    	<%=pageContext.getAttribute("key") %><br/>
    	<%=request.getAttribute("key") %><br/>
    	<%=session.getAttribute("key") %><br/>
    	<%=application.getAttribute("key") %><br/>
    </body>
    </html>
    ```

  - 测试操作：

    ```txt
    测试pageContext作用域步骤：
    直接访问context1.jsp文件
    
    
    测试request作用域步骤：
    1.在context1.jsp文件中添加转发到context2.jsp（有数据）
    2.直接访问context2.jsp文件 （没有数据）
    
    
    测试session作用域步骤：
    1.访问完context1.jsp文件
    2.关闭浏览器。但是要保持服务器一直开着
    3.打开浏览器，直接访问context2.jsp文件
    
    
    测试application作用域步骤：
    1.访问完context1.jsp文件，然后关闭浏览器
    2.停止服务器。再启动服务器。
    3.打开浏览器访问context2.jsp文件
    ```

# EL表达式

**(Expression Language)**

***

## 第1章 为什么需要EL

在JSP页面上获取域对象中保存的数据和获取请求参数数据是非常常用的操作。

获取请求域中的数据

```jsp
<%=request.getAttribute("message") == null ? "" : request.getAttribute("message") %>
```

获取请求参数

```jsp
<%=request.getParameter("userName")==null? "": request.getParameter("userName")%>
```

有没有什么办法能够让上述代码简洁一些呢？有！

获取请求域中的数据

```jsp
${requestScope.message }
```

获取请求参数

```jsp
${param.userName }
```

这就是EL表达式，它能够极大的简化JSP页面上数据的显示。他就是被用来优化数据读取操作的。

再比如：

​	![1558537244287](images/1558537244287.png)



## 第2章 EL的基本使用

### 2.1 什么是EL表达式

- EL（Expression Language）是JSP**内置的表达式语言**，用以访问页面的上下文以及不同作用域中的对象 ，取得对象属性的值，或执行简单的运算或判断操作。EL在得到某个数据时，会自动进行数据类型的转换。

- **EL表达式用于代替JSP表达式(<%= %>)**在页面中做输出操作。
- EL表达式出现的目的是为了使JSP写起来更加简单，让jsp的代码更佳简化。

- EL表达式**仅仅用来读取数据，而不能对数据进行修改。**

- 使用EL表达式输出数据时，如果有则输出数据，如果为null则什么也不输出。

- 我们先来看一下EL表达式的一个Hello world 程序，看看它是如何简化jsp代码。


```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	//首先我们需要在request域对象中设置一个属性
	request.setAttribute("hello", "这是内容");
%>
<%-- 获取请求域中的属性hello输出 --%>
jsp的输出：<%=request.getAttribute("hello") == null ? "" : request.getAttribute("hello")%><br/><br/>
<%-- 输出在域中查找输出hello的值 --%>
EL表达式的输出：${hello}<br/><br/>
</body>
</html>

```

页面访问后的输出内容：

![1558517484366](images/1558517484366.png)

从上面的程序，我们不难看出：我们要输出域中的属性，方便多了。

所以EL表达式使得jsp页面的代码变得更加简洁。主要用于替换 jsp中表达式脚本。

**EL表达式的最主要功能就是从域对象中获取数据，并且输出**

### 2.2 EL表达式基本语法

- 使用EL表达式获取数据的语法：(EL表达式总是放在{}中，而且前边有一个$作为前缀。)

```jsp
 “${标识符}”
```

- 第一点：当EL表达式输出的key不存在的时候，输出的是空串””

- 第二点：EL表达式在域对象中搜索属性的顺序是pageContext，request，session，application

- 获取域中的对象可以直接使用对象名，这时按照从小到大的顺序依次向上查找。如获取域中名字为user的对象
  -  ${user}

- 获取对象的属性值可以直接通过“对象.属性名”
  -  ${user.name}
  -  ${user.age}

- 注意：这里的属性名是**get和set方法对应的属性值**，并不是对象中的变量名。
  - 比如：如果获取name属性值，那么该对象中必定要存在一个getName()方法。

- 获取对象的属性也可以通过“对象[“属性名”]”
  -  ${user[“name”]}
  -  ${user[“age”]}

- 获取Map中属性时可以直接通过属性的key
  -  ${map.key}
  -  ${map[key]}

- **在指定域中获取属性**
  - 在EL表达式中如果我们直接使用属性名如：${user}，它将会在四个域中**由小到大**依次查找。顺序：pageScope、requestScope、sessionScope、applicationScope。
  - 也可以指定从哪个域中获取：
    - ${pageScope .user}：当前页面
    - ${requestScope.user}：当前请求
    - ${sessionScope.user}：当前会话
    - ${applicationScope.user}：当前应用



## 第3章 EL表达式运算符

语法：${ 运算表达式 } ， EL 表达式支持如下运算符：

### 3.1 算术运算符

| **算数运算符** | **说**    **明** |         **范**    **例**         | **结果** |
| :------------: | :--------------: | :------------------------------: | :------: |
|       +        |       加法       |           ${ 12 + 18 }           |    30    |
|       -        |       减法       |           ${ 18 - 8 }            |    10    |
|       *        |       乘法       |           ${ 12 * 12 }           |   144    |
|    / 或div     |       除法       | ${ 144 / 12 } 或 ${ 144 div 12 } |    12    |
|    % 或mod     |       取模       | ${ 144 % 10 } 或 ${ 144 mod 10 } |    4     |



### 3.2 关系运算符

| **关系运算符** | **说**    **明** |          **范**    **例**           | **结果** |
| :------------: | :--------------: | :---------------------------------: | :------: |
|   ==   或 eq   |       等于       | ${  5 == 5    } 或 ${  5 eq 5    }  |   true   |
|   !=   或 ne   |      不等于      |  ${  5 !=5    } 或 ${  5 ne 5    }  |  false   |
|   <   或 lt    |       小于       |  ${  3 < 5    } 或 ${  3 lt 5    }  |   true   |
|   >   或 gt    |       大于       | ${  2 > 10    } 或 ${  2 gt 10    } |  false   |
|   <=   或 le   |     小于等于     |   ${   5 <= 12 } 或 ${ 5 le 12 }    |   true   |
|   >=   或 ge   |     大于等于     |    ${   3 >= 5 }  或 ${ 3 ge 5 }    |  false   |



### 3.3 逻辑运算符

| **逻辑运算符** | **说**    **明** |                      **范**    **例**                      | **结果** |
| :------------: | :--------------: | :--------------------------------------------------------: | :------: |
|  &&   或 and   |      与运算      |  ${   12 == 12 && 12 < 11 } 或 ${ 12 == 12 and 12 < 11 }   |  false   |
|  \|\|   或 or  |      或运算      | ${   12 == 12 \|\| 12 < 11 } 或 ${ 12 == 12   or 12 < 11 } |   true   |
|    ! 或 not    |     取反运算     |                ${   !true } 或 ${not true }                |  false   |



### 3.4 empty运算符 (常用)

- empty运算是判断一个对象或者集合是否为空。返回boolean值。

  - 对象为null时，返回**true**
  - 如果是空的字符串，返回**true**
  - 如果是空的Object数组(Object数组.length == 0)，返回**true**
  - 空的集合，返回**true**
  - 空的map，返回**true**

- empty运算符测试代码：

  ```jsp
  <%@page import="java.util.Map"%>
  <%@page import="java.util.List"%>
  <%@page import="java.util.HashMap"%>
  <%@page import="java.util.ArrayList"%>
  <%@ page language="java" contentType="text/html; charset=UTF-8"
      pageEncoding="UTF-8"%>
  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
  <html>
  <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  </head>
  <body>
  <%
  	// 对象为null时，返回true
  	request.setAttribute("nullObject", null);
  	//如果是空的字符串，返回true
  	request.setAttribute("emptyStr", "");
  	//如果是空的数组，返回true
  	request.setAttribute("emptyArr", new Object[]{});
  	//空的集合，返回true
  	List list = new ArrayList();
  	request.setAttribute("emptyList", list);
  	//空的map，返回true
  	Map map = new HashMap();
  	request.setAttribute("emptyMap", map);
  %>
  <h1>EL empty 运算</h1>
  对象为null，empty为真 ---->>>>${ empty nullObject }<br/>
  空的字符串，empty为真------>>>>${ empty emptyStr }<br/>
  空的数组，empty为真------>>>>${ empty emptyArr }<br/>
  空的list集合，empty为真---->>>>${ empty emptyList }<br/>
  空的map集合，empty为真----->>>>${ empty emptyMap }<br/>
  </body>
  </html>
  
  ```

  浏览器运行结果：

![1558518559400](images/1558518559400.png)

### 3.5 三元运算符

我们可以在EL 表达式中方便的使用三元运算输出。

- 语法：

```
${ 表达式1 ? 表达式2 : 表达式3 }
```

- 示例：

```
${ 12 == 12 ? "12 等于 12" : "12 ！= 12" }
```

 

我们可以很方便的在EL 表达式中使用三元运算符进行运算。

### 3.6 “.”和[]运算符

- “.” 运算符，可以取JavaBean对象的属性值，也可以取map中某个key的值。

- [] 中括号，可以获取顺序集合中指定索引的元素，也可以获取特殊key的值。

- 当我们在map对象中存放一些特殊的key的时候，比如说，key字符串中含有 “.” 、“+” 、“-” 、“*” 、“/” 、 “%” 等 这些运算符的时候，会让el解析器产生歧义的时候。我们可以使用[‘key’]中括号加引号包含key的形式取值。

- []中括号，不仅可以获取有序集合（数组和List集合）中的给定索引的元素，还可以获取key中含有特殊意义字符的key对应的值。比如key中含有 “.” ，  “+”   , “_”    ,  “*”  ,   “/”  等的运算字符。

- **示例：**

```jsp
<body>
	<%
		//设置
		Map map = new HashMap();
		map.put("a-b-c","a-b-c-Value");
		map.put("a.b.c", "a.b.c.Value");
		map.put("aaa","aaa-Value");
		request.setAttribute("map", map);
	%>
	<%-- 下面我们可以通过中括号方式获取对应key的值  --%>
	${ map['a.b.c'] }<br/>
	${ map['a-b-c'] }<br/>
	${ map.aaa }<br/>
</body>

```

**输出为：**

a.b.c.Value

a-b-c-Value

aaa-Value

## 第4章 EL表达式中11个隐含对象

EL中包含11个隐含对象，这些对象可以在EL表达式中直接使用。

|     **隐含对象**     | **类型**                         | **说明**                                |
| :------------------: | :------------------------------- | :-------------------------------------- |
|   **pageContext**    | javax.servlet.jsp.PageContext    | 就是JSP页面中的pageContext对象          |
|    **pageScope**     | java.util.Map<String,Object>     | Page范围属性名和属性值                  |
|   **requestScope**   | java.util.Map<String,Object>     | Request范围属性名和属性值               |
|   **sessionScope**   | java.util.Map<String,Object>     | Session范围属性名和属性值               |
| **applicationScope** | java.util.Map<String,Object>     | Web应用范围属性名和属性值               |
|      **param**       | **java.util.Map<String,String>** | **对应一个请求参数**                    |
|     paramValues      | java.util.Map<String,String[]>   | 对应一组请求参数                        |
|      **header**      | java.util.Map<String,String>     | 请求头                                  |
|     headerValues     | java.util.Map<String,String[]>   | 请求头返回字符数组                      |
|      **cookie**      | java.util.Map<String, Cookie>    | 获取某个cookie对象                      |
|      initParam       | java.util.Map<String, String>    | 获取web.xml中\<context-param>初始化参数 |

- 请求域
  - 请求域主要用于获取域对象中的属性，四个对象分别对应四个请求域。
    - pageScope
    - requestScope
    - sessionScope
    - applicationScope

- 请求参数
  - 参数对象主要用于获取get或post请求中的参数。
    - param：获取指定的请求参数， 如：${param.username},与jsp中request.getParameter()作用一致。
    - paramValues：获取请求参数数组，如：${paramValues.hobby}会返回一个string类型的数组



## 第5章 EL表达式获取域对象中的数据

称呼			el对象			     <=== 对应 ===>		jsp对象

page域		pageScope                       <=== 对应 ===>              pageContext         域中的属性

request域	   requestScope                  <=== 对应 ===>              request                  域中的属性

session域	    sessionScope                   <=== 对应 ===>             session                  域中的属性

application域     applicationScope               <=== 对应 ===>           application 	   域中的属性

### 5.1 如何从四个域对象中获取各自的属性

需求：分别往四个域对象中存储数据，然后使用pageScope，requestScope，sessionScope，applicationScope中取出数据

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<% 	
	// 在四个域中存放数据，进行获取
	pageContext.setAttribute("key", "pageContext-value");
	request.setAttribute("key", "request-Value");
	session.setAttribute("key", "session-value");
	application.setAttribute("key", "application-value");
%>
<%-- 从不同的域中获取数据 --%>
page域中key的值：${ pageScope.key }<br/>
request域中key的值：${ requestScope.key }<br/>
session域中key的值：${ sessionScope.key }<br/>
application域中key的值：${ applicationScope.key }<br/>
</body>
</html>

```

运行的结果：

![1558536775437](images/1558536775437.png)



### 5.2 EL表达式获取域数据的顺序

EL 表达式语句在执行时，会用标识符为关键字分别从page、request、session、application四个域中查找对应key的对象。找到则返回相应数据。找不到则返回空串。（注意，不是null，而是空字符串）

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	//我们在四个域对象中设置同一个属性
	pageContext.setAttribute("hello", "page这是内容");
	request.setAttribute("hello", "request这是内容");
	session.setAttribute("hello", "session这是内容");
	application.setAttribute("hello", "application这是内容");
%>
<%-- 从page，request，session，application四个作用域中顺序查找对应的key的值输出，如果没有找到就输出空串 --%>
${hello}<br/>
</body>
</html>

```

**测试步骤：**

1. 写好页面代码，直接访问页面，输出pageContext中的内容。

2. 注掉pageContext.setAttribute 代码。刷新页面，输出request域范围的hello属性值。

3. 注掉 request.setAttribute  代码。刷新页面，输出Session域范围的hello属性值。

4. 注掉 session.setAttribute 代码，并且关闭浏览器后重新打开浏览器访问页面。输出 application（ServletContext）域范围的属性值

5. 注掉application.setAttribute代码，关闭服务器。然后再启动服务器。再打开浏览器，再访问页面。application中也没有数据了

### 5.3 获取javaBean普通属性、数组属性、List集合属性，map属性中的数据

**例如：** 

${  user.username  }           // 获取user对象中。username属性值 

${  list[下标]  }                       // 访问有序集合（或数组）中给定索引的元素

${  map.key  }                       // 访问map集合中指定key的属性值

${  map[“key”] }          // 访问特殊字符串的key的属性值

**注意：[] 中括号除了可以访问带有顺序的集合和数组的元素之外。还可以访问特殊的key值**

**需求：**创建一个User类对象，添加字符串属性，数组属性，List集合属性。map属性。然后创建一个对象实例添加到request域对象中测试获取。 

一定要记住一点，EL表达式获取数据的时候，是通过对应的get方法获取的BeanUtils 是通过set方法设置值

① **先定义一个JavaBean对象------User类**

```java
package com.atguigu.servlet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class User {

	private String username;
	private String[] phones;
	private Map<String, Object> map;
	private List<String> strList;

	public User(String username, String[] phones, Map<String, Object> map, List<String> strList) {
		super();
		this.username = username;
		this.phones = phones;
		this.map = map;
		this.strList = strList;
	}

	public User() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String[] getPhones() {
		return phones;
	}

	public void setPhones(String[] phones) {
		this.phones = phones;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<String> getStrList() {
		return strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", phones=" + Arrays.toString(phones) + ", map="
				+ map + ", strList=" + strList + "]";
	}

}

```

② 在jsp页面中添加一些对象到四个域对象中，使用el表达式访问测试。

```jsp
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.atguigu.servlet.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	// 创建一个map对象，并添加数据
	HashMap<String,Object> map = new HashMap<String,Object>();
	map.put("aaa", "AAAValue");
	map.put("bbb", "bbbValue"); 
	map.put("ccc", "cccValue");
	map.put("ddd", "dddValue");
	
	// 创建一个list集合对象
	List<String> strList = new ArrayList<String>();
	strList.add("aaa");
	strList.add("bbb");
	strList.add("ccc");
	
	// 创建一个User对象
	User user = new User("用户名",new String[]{"第一个电话","第二个电话","第三个电话"},map,strList);
	
	// 把用户对象添加到请求request的属性中
	request.setAttribute("user", user);
%>
<%--访问 对象的username 属性--%>
user对象的username属性值---->>>>${ user.username }<br/><br/>

<%--访问user对象中数组的第二个元素--%>
user对象中phones数组的第二个元素-------->>>>${ user.phones[1] }<br/><br/>

<%--访问 list集合 中第一个元素--%>
list集合中第一个元素-------->>>>>${ user.strList[0] }<br/> <br/>

<%--访问 user对象中map集合的aaa的属性值 --%>
user对象中map集合中aaa属性的值----->>>>>${ user.map.aaa }<br/><br/>

</body>
</html>

```

页面输出如下：

![1558518059962](images/1558518059962.png)

### 5.4 pageContext访问Jsp中内置对象

- 通过pageContext对象我们可以直接获取jsp中的一些内置对象，比如：request对象，session对象，Servletconfig对象，ServletContext对象，然后获取一些我们需要的信息。

- **常用的功能获取**
  - 协议
  - 服务器ip
  - 服务器端口
  - **获取工程路径**
  - 获取请求方法
  - 获取客户端ip地址
  - 获取会话的id编号

- pageContext使用示例代码

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
协议：${ pageContext.request.scheme }<br/>
服务器ip：${ pageContext.request.serverName }<br/>
服务器端口：${ pageContext.request.serverPort }<br/>
获取工程路径：${ pageContext.request.contextPath }<br/>
获取请求方法：${ pageContext.request.method }<br/>
获取客户端ip地址：${ pageContext.request.remoteHost }<br/>
获取会话的id编号：${ pageContext.session.id }<br/>
</body>
</html>

```

- **pageContext对象最常用的功能就是获取上下文路径（也就是工程路径名）**

- 工程名(上下文路径):${ pageContext.request.contextPath }

- **但是在实际项目中为了缩短代码量，会把request对象放在pageContext域对象中。然后再使用**。比如说

```jsp
<% 
// 先把request对象放到pageContext域对象中
pageContext.setAttribute(“req”,request);

%>
```

然后EL表达式代码改为

```jsp
工程名(上下文路径):${ req.contextPath }
```

### 5.5 EL表达式其他隐含对象的使用

web.xml文件中的配置内容：

```
	<context-param>
		<param-name>username</param-name>
		<param-value>root</param-value>
	</context-param>

```

使用的示例代码：

```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
参数username的值：${ param.username }<br/>
参数hobby的值：${ paramValues.hobby[0] }<br/>
请求头Accept-Language的值：${ header["Accept-Language"] }<br/>
请求头Accept的值：${ headerValues["Accept"][0] }<br/>
cookie的key = ${ cookie.JSESSIONID.name } : value = ${ cookie.JSESSIONID.value } <br/>
上下文参数：${ initParam.username }<br/>
</body>
</html>

```

访问的显示结果：

![1558537139590](images/1558537139590.png)

# JSTL 

**(JSP Standard Tag Library)**

***

## 第1章 JSTL简介

- JSP虽然为我们提供了EL表达式用来替代JSP表达式，但是由于EL表达式仅仅具有输出功能，用于替代JSP中的表达式脚本，而不能替代页面中的JSP代码脚本。

- 为了解决这个问题，JSP为我们提供了可以自定义标签库(Tag Library)的功能，用来替代代码脚本。这样使得整个jsp页面变得更佳简洁。

- 所谓自定义标签库就是指可以在JSP页面中以类似于HTML标签的形式调用Java中的方法。使用方法和我们JSP动作标签类似。

- 而为了方便开发使用，Sun公司又定义了一套通用的标签库名为JSTL(JSP Standard Tag Library)，里面定义很多我们开发中常用的方法，方便我们使用。

- JSTL的标准由Sun公司定制，Apache的Jakarta小组负责实现，是一个不断完善的开放源代码的JSP标签库。

- JSTL由5个不同功能的标签库组成。如下：

  |       功能范围       | 前缀  |                  URI                   |
  | :------------------: | :---: | :------------------------------------: |
  | **核心标签库--重点** | **c** | **http://java.sun.com/jsp/jstl/core**  |
  |     格式化标签库     |  fmt  |    http://java.sun.com/jsp/jstl/fmt    |
  |      函数标签库      |  fn   | http://java.sun.com/jsp/jstl/functions |
  |    数据库(不使用)    |  sql  |    http://java.sun.com/jsp/jstl/sql    |
  |     XML(不使用)      |   x   |    http://java.sun.com/jsp/jstl/xml    |

  > 说明：
  >
  > 函数标签库，需要结合EL表达式使用，里面定义了一些对字符串的操作（对字符串的截取、替换等）。
  >
  > 格式化标签库，主要用来对日期、时间、数字进行国际化的操作。

- 在jsp标签库中使用taglib指令引入标签库

  ```jsp
  CORE 标签库
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  
  XML 标签库
  <%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
  
  FMT 标签库 
  <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
  
  SQL 标签库
  <%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
  
  FUNCTIONS 标签库
   <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
  
  ```

  

## 第2章 使用JSTL标签库步骤

**第一步：先引入JSTL标签库的jar包类库到WEB-INF/lib目录下**

​	![1558539063529](images/1558539063529.png)

这两个jar包，和笔记在同一个目录下。

**第二步：在使用的jsp页面中使用taglib指令导入需要的标签库**

- 以core核心标签库为例，需要在jsp页面中导入标签库的引用。

  ![1558539099390](images/1558539099390.png)

  - prefix用来指定前缀名，我们通过该名来使用JSTL
  - uri属性为标签库的唯一uri地址。因为JSTL由多个不同的库组成，使用该属性指定要导入哪个库。http://java.sun.com/jsp/jstl/core 

**第三步：使用JSTL**

然后我们就可以在jsp页面中愉快的使用标签库了。

<c:out value="hello">\</c:out>

这个例子标识，调用前缀为c的标签的out方法，向页面中输出value属性中的字符串。

JSTL的使用非常像html标签。

## 第3章 核心标签

- Core标签库，包括了我们最常用的标签。

- 要使用Core标签库需要在JSP页面中加入CORE 标签库：

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

### 3.1 \<c:out>标签

**作用：**\<c:out>用于计算一个表达式并将结果输出到当前页面。功能类似于JSP表达式<%= %>和EL表达式${}

**属性：**

| 属性    | 作用                        | 参数类型 |
| ------- | --------------------------- | -------- |
| value   | 要输出的值                  | Object   |
| default | 当value为null时显示的默认值 | Object   |
| escaXml | 是否对特殊字符进行转义      | boolean  |

**示例：**

```jsp
<c:out value="${user.name}" default="" escapeXml="true"></c:out>
```



### 3.2 \<c:set>标签

**作用：**\<c:set>标签 用于 添加 或 修改 域中的属性。

**属性：**

| **属性** | **描述**                                                     | **是否必要** | **默认值** |
| :------: | :----------------------------------------------------------- | :----------- | ---------- |
|  value   | 要存储或修改的值                                             | 否           | 主体的内容 |
|  target  | 要修改的域对象的属性名（必须是JavaBean或者Map）              | 否           | 无         |
| property | 指定要修改的对象的属性名                                     | 否           | 无         |
|   var    | 表示域中存放的属性名                                         | 否           | 无         |
|  scope   | var属性的作用域 (page , request , session ,application) ，若不指定则为page | 否           | page       |

如果指定了target属性，那么property属性也需要被指定。

**1）在作用域中添加新属性**

```jsp
<c:set scope="" var="" value=""></c:set>
```

- scope是哪个作用域

- var 是属性名

- value 是属性值

**2）修改已存中的作用域的属性**

```jsp
<c:set target="" property="" value=""></c:set>
```

- target 是要修改的目标对象

- property 是要修改或者添加的属性名

- value 是要修改的属性值

**示例：**

```jsp
<body>
<%
	User user = new User("username姓名",null,null,null);
	session.setAttribute("user", user);
	
	Map<String,String> map = new HashMap<String,String>();
	map.put("abc", "abcValue");
	pageContext.setAttribute("map", map);
%>
	<%-- 在作用域里添加新的属性 --%>
	<c:set scope="page" var="two" value="twovalue"></c:set>
	<%-- 修改在作用域里的对象 --%>
	<c:set target="${ sessionScope.user }" property="username" value="twovalue"></c:set>
	<%-- 给原作用域中的map添加一个key --%>
	<c:set target="${ pageScope.map }" property="bbb" value="twovalue"></c:set>
	${ pageScope.two }<br/> <hr/>
	${ pageScope.map }<br/> <hr/>
	${ sessionScope.user }<br/> <hr/>
</body>

```

**运行结果：**

![1558604309995](images/1558604309995.png)

### 3.3 \<c:if>标签

**作用：**\<c:if>标签 判断表达式的值，如果表达式的值为 true 则执行其主体内容。

**属性：**

| **属性** | **描述**               | **是否必要** | **默认值** |
| -------- | ---------------------- | ------------ | ---------- |
| test     | 条件                   | 是           | 无         |
| var      | 用于存储条件结果的变量 | 否           | 无         |
| scope    | var属性的作用域        | 否           | page       |

**示例：**

```jsp
<body>
	<!-- 判断 12 == 12 -->
	<c:if test="${ 12 == 12 }">
		<h1>12 == 12 为真</h1>
	</c:if>
	<!-- 把判断的结果保存到作用域中 -->
	<c:if test="${ 12 == 13 }" scope="page" var="a"></c:if>
	<%-- 输出page作用域中的a对象 --%>
	<c:out value="${ pageScope.a }"></c:out>
</body>

```

**运行结果：**

![1558611135271](images/1558611135271.png)

### 3.4 \<c:choose>、\<c:when>、\<c:otherwise>标签(常用)

**作用：**

\<c:choose>标签与Java switch语句的功能一样，用于在众多选项中做出选择。

switch语句中有case，而\<c:choose>标签中对应有\<c:when>，switch语句中有default，而\<c:choose>标签中有\<c:otherwise>。

 

**属性：**

\<c:choose>标签没有属性。

\<c:when>标签只有一个属性，如下：

| **属性** | **描述** | **是否必要** | **默认值** |
| -------- | -------- | ------------ | ---------- |
| test     | 条件     | 是           | 无         |

\<c:otherwise>标签没有属性。

**示例：**

```jsp
<body>
	<%
		// 保存一个分数
		pageContext.setAttribute("score", 90);
	%>
	<%-- 开始判断 --%>
	<c:choose>
		<%-- 如果成绩大于等于 90分 --%>
		<c:when test="${ pageScope.score >= 90 }">
			成绩为A
		</c:when>
		<%-- 如果成绩大于等于 80分 --%>
		<c:when test="${ pageScope.score >= 80 }">
			成绩为B
		</c:when>
		<%-- 如果成绩大于等于 70分 --%>
		<c:when test="${ pageScope.score >= 70 }">
			成绩为C
		</c:when>
		<%-- 如果成绩大于等于 60分 --%>
		<c:when test="${ pageScope.score >= 60 }">
			成绩为D
		</c:when>
		<%-- 其他情况 --%>
		<c:otherwise>
			成绩为E，不及格
		</c:otherwise>
	</c:choose>
</body>

```

**运行结果：**

![1558611318535](images/1558611318535.png)

### 3.5 \<c:forEach>标签

**作用：**

\<c:forEach>标签是迭代一个集合中的对象-可以是数组，也可以是list，也可以是map对象。

**属性**：

| **属性**  | **描述**                                   | **是否必要** | **默认值**   |
| --------- | ------------------------------------------ | ------------ | ------------ |
| items     | 要被循环的数据集合-可以使用el表达式        | 否           | 无           |
| begin     | 开始的索引（0=第一个元素，1=第二个元素）   | 否           | 0            |
| end       | 最后一个索引（0=第一个元素，1=第二个元素） | 否           | Last element |
| step      | 每一次迭代的步长                           | 否           | 1            |
| var       | 代表当前条目的变量名称                     | 否           | 无           |
| varStatus | 代表循环状态的变量名称                     | 否           | 无           |

**varStatus状态：**

- 作用：指定保存迭代状态的对象的名字，该变量引用的是一个LoopTagStatus类型的对象

- 通过该对象可以获得一些遍历的状态，如下：
  - begin       获取begin属性里的值
  - end           获取end属性里的值
  - count        获取当前遍历的个数
  - index         获取当前索引值
  - first           获取是否是第一个元素
  - last            获取是否是最后一个元素
  - current     获取当前遍历的元素对象



**需求1：遍历一个List集合，遍历一个Map**

**需求2：使用 map 模拟用户信息。包含编号，用户名，密码，年龄，电话信息。创建5个放到list集合中，再把list集合存放到pageContext域中，然后遍历输出**

表格的格式：

```css
<style type="text/css">
	table{
		width: 700px;
		border: 1px solid red;
		border-collapse: collapse;
	}
	th , td{
		border: 1px solid red;
	}
</style>

```

**示例：**

```jsp
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
	table{
		width: 700px;
		border: 1px solid red;
		border-collapse: collapse;
	}
	th , td{
		border: 1px solid red;
	}
</style>
</head>
<body>
<%
	// 模拟创建5个学生的信息
	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
	for (int i = 0; i < 5; i++) {
		Map<String,Object> item = new HashMap<String,Object>();
		item.put("id", (i + 1));
		item.put("username", "用户名" + i);
		item.put("password", "密码" + i);
		item.put("age", (12 + i) );
		item.put("phone", "18615041888");
		list.add(item);
	}
	// 保存学生的信息到pageContext作用域中
	pageContext.setAttribute("stus", list);
%>
<table>
	<tr>
		<th>编号</th>
		<th>姓名</th>
		<th>密码</th>
		<th>年龄</th>
		<th>电话</th>
		<th>遍历的个数</th>
		<th>当前索引</th>
		<th>是否第一个</th>
		<th>是否最后一个</th>
	</tr>
	<%-- 
		begin表示从索引0开始，
		end表示到索引10结束，
		step表示迭代的步长
		items是遍历的集合
		var是当前遍历到的对象名
		varStatus是当前遍历的状态对象，是LoopTagStatus对象实例
	 --%>
	<c:forEach begin="0" end="10" step="1" items="${ pageScope.stus }" var="item" varStatus="status">
		<tr>
			<td>${ item.id }</td>
			<td>${ item.username }</td>
			<td>${ item.password }</td>
			<td>${ item.age }</td>
			<td>${ item.phone }</td>
			<td>${ status.count }</td>
			<td>${ status.index }</td>
			<td>${ status.first }</td>
			<td>${ status.last }</td>
		</tr>
	</c:forEach>
</table>
</body>
</html>

```

**运行结果：**

![1558611541959](images/1558611541959.png)

### 3.6 \<c:remove>标签

**作用：**用于移除域中的属性



**属性：**

| 属性  | 作用                                                     | 参数类型 |
| ----- | -------------------------------------------------------- | -------- |
| var   | 设置要移除的属性的名字                                   | String   |
| scope | 设置要移除属性所在的域，若不指定则删除所有域中的对应属性 | String   |

**示例：**

```
移除所有域中key属性：
<c:remove var="key"/>

移除request中的key属性: 
<c:remove var="key" scope="request"/>

```



### 3.7 \<c:url>标签

**作用：**主要用来重写URL地址

**属性：**

| 属性  | 作用                                   | 参数类型 |
| ----- | -------------------------------------- | -------- |
| value | 设置要处理的URI地址，注意这里要以/开头 | String   |
| var   | 修改后存储到域对象中的uri属性名        | String   |
| scope | l  设置修改后uri存放的域               | String   |

**示例：**

```jsp
使用相对路径：
<c:url value="index.jsp" var="uri" scope="request">
	<c:param name="name" value="张三"></c:param>
</c:url>

会生成如下地址：index.jsp?name=%E5%BC%A0%E4%B8%89

使用绝对路径会自动在路径前加上项目名：
<c:url value="/index.jsp" var="uri" scope="request">
	<c:param name="name" value="张三"></c:param>
</c:url>

会生成如下地址：/Test_JSTL/index.jsp?name=%E5%BC%A0%E4%B8%89
```



### 3.8 \<c:redirect>标签

**作用：**用于将请求重定向到另一个资源地址

**属性：**

| 属性 | 作用                                                         | 参数类型 |
| ---- | ------------------------------------------------------------ | -------- |
| url  | 指定要重定向到的目标地址，注意这里指定绝对路径会自动加上项目名 | String   |

**示例：**

```jsp
<c:redirect url="/target.jsp"></c:redirect>
```



## 第4章 JSTL函数

- 函数标签库是在JSTL中定义的标准的EL函数集。

- 函数标签库中定义的函数基本上都是对字符串的操作。

- 引入：

  ```
  <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
  ```



### 4.1 fn:contains和fn:containsIgnoreCase

作用：用于判断字符串中是否包含指定字符串，containsIgnoreCase忽略大小写。

语法：

```
fn:contains(string,subString) --> boolean
```

参数和返回值：

| 参数      | 类型    | 作用                                             |
| --------- | ------- | ------------------------------------------------ |
| string    | String  | 源字符串                                         |
| subString | String  | 要查找的字符串                                   |
| 返回值    | boolean | 若String中包含subString则返回true，否则返回false |

例：

```
${fn:contains("hello","HE")}  --> false

${fn:containsIgnoreCase("hello","HE")} --> true

```



### 4.2 fn:startsWith和fn:endsWith

作用：判断一个字符串是否以指定字符开头（startsWith）或结尾（endsWith）

语法：

```
fn:startsWith(string , prefix) --> boolean

fn:endsWith(string,suffix) --> boolean

```

参数和返回值：

| 参数             | 类型    | 作用                            |
| ---------------- | ------- | ------------------------------- |
| string           | String  | 源字符串                        |
| prefix 或 suffix | String  | 要查找的前缀或后缀字符串        |
| 返回值           | boolean | 符合要求返回true，否则返回false |

例：

```
${fn:startsWith("hello","he") } --> true
${fn:endsWith("hello","he") } --> false

```



### 4.3 fn:indexOf

作用：在一个字符串中查找指定字符串，并返回第一个符合的字符串的第一个字符的索引。

语法：

```
fn:indexOf(string,subString) --> int
```

参数和返回值：

| 参数      | 类型   | 作用                                                         |
| --------- | ------ | ------------------------------------------------------------ |
| string    | String | 源字符串                                                     |
| subString | String | 要查找的字符串                                               |
| 返回值    | int    | 若在string中找到subString则返回第一个符合的索引，若没有符合的则返回-1 |

例：

```
${fn:indexOf("hello",'e') } --> 1
```



### 4.4 fn:replace

作用：将一个字符串替换为另外一个字符串，并返回替换结果

语法：

```
fn:replace(str , beforeSubString , afterSubString) --> String
```

参数和返回值：

| 参数            | 类型   | 作用             |
| --------------- | ------ | ---------------- |
| str             | String | 源字符串         |
| beforeSubString | String | 被替换的字符串   |
| afterSubString  | String | 要替换的新字符串 |
| 返回值          | String | 替换后的字符串   |

例：

```
${fn:replace("hello","llo",'e') } --> hee
```

### 4.5 fn:substring

作用：截取字符串

语法：

```
fn: substring (str , beginIndex , endIndex) --> String
```

参数和返回值：

| 参数       | 类型   | 作用                     |
| ---------- | ------ | ------------------------ |
| str        | String | 源字符串                 |
| beginIndex | int    | 开始位置索引(包含该位置) |
| endIndex   | int    | 结束位置索引(不包含自身) |
| 返回值     | String | 返回截取的字符串         |

例：

```
${fn:substring("hello",1,3) } --> el
```



### 4.6 fn:substringBefore和fn:substringAfter

作用：返回一个字符串指定子串之前（substringBefore）之后（substringAfter）的字符串

语法：

```
fn: substringBefore(string,subString) --> String
fn: substringAfter (string,subString) --> String

```

参数和返回值：

| 参数      | 类型   | 作用                                                  |
| --------- | ------ | ----------------------------------------------------- |
| str       | String | 源字符串                                              |
| subString | int    | 指定str中的一个子串，该串之前或之后的字符串将被返回。 |
| 返回值    | String | 返回截取的字符串                                      |

例：

```
${fn:substringBefore("hello","l") } --> he
	${fn:substringAfter("hello","l") } --> lo

```



### 4.7 fn:split

作用：将一个字符串拆分成字符串数组。

语法：

```
fn:split(string,delimiters)--> String
```

参数和返回值：

| 参数       | 类型     | 作用                       |
| ---------- | -------- | -------------------------- |
| str        | String   | 要被拆分的字符串           |
| delimiters | String   | 指定根据什么内容拆分字符串 |
| 返回值     | String[] | 返回拆分后的字符串数组     |

例：

```
${fn:split("a-b-c-d-e-f-g","-")} --> 返回一个数组对象[a,b,c,d,e,f,g]
```



### 4.8 fn:join

作用：将数组中所有元素连接成一个字符串

语法：

```
fn:join(array,sparator) --> String
```

参数和返回值：

| 参数     | 类型     | 作用                         |
| -------- | -------- | ---------------------------- |
| str      | String   | 要被拆分的字符串             |
| sparator | String   | 在结果中每个元素之间的分隔符 |
| 返回值   | String[] | 拼接之后的结果               |

例：

```
<%
String[] strs = new String[]{"a","b","c","d","e","f"};
pageContext.setAttribute("strs", strs);
%>
${fn:join(strs,'-') }

```

返回：a-b-c-d-e-f


### 4.9 fn:toLowerCase和fn:toUpperCase

作用：将字符串都转换成大写（toUpperCase）或小写（toLowerCase）字符

语法：

```
fn: toLowerCase (str) --> String
fn: toUpperCase(str) --> String

```

参数和返回值：

| 参数   | 类型   | 作用                     |
| ------ | ------ | ------------------------ |
| str    | String | 源字符串                 |
| 返回值 | String | 转换为大写或小写的字符串 |

 

例：

```
${fn:toLowerCase("ABCDEFG") } --> abcdefg
${fn:toUpperCase("abcdefg") } --> ABCDEFG

```



### 4.10 fn:trim

作用：去掉字符串的前后空格

用法：

```
fn:trim(str) --> String
```

参数和返回值：

| 参数   | 类型   | 作用               |
| ------ | ------ | ------------------ |
| str    | String | 源字符串           |
| 返回值 | String | 去掉前后空格的结果 |

例：

```
${fn:trim("     hello  ") } --> hello
```



### 4.11 fn:length

作用：返回集合或者字符串的长度

用法：

```
fn:trim(input) --> int
```

参数和返回值：

| 参数   | 类型               | 作用               |
| ------ | ------------------ | ------------------ |
| input  | String、集合、数组 | 要计算长度的目标   |
| 返回值 | int                | 集合或字符串的长度 |

例：

```
${fn:length("hello") } --> 5
```

# 会话控制

***

## 第1章 Cookie的介绍

### 1.1 为什么需要Cookie

HTTP是无状态协议，服务器不能记录浏览器的访问状态，也就是说服务器不能区分两次请求是否由一个客户端发出。这样的设计严重阻碍了Web程序的设计。如：在我们进行网购时，买了一条裤子，又买了一个手机。由于HTTP协议是无状态的，如果不通过其他手段，服务器是不能知道用户到底买了什么。而Cookie就是解决方案之一。

### 1.2 Cookie是什么

- Cookie，翻译是小饼的意思。实际上就是服务器保存在浏览器上的一段信息。浏览器有了Cookie之后，每次向服务器发送请求时都会同时将该信息发送给服务器，服务器收到请求后，就可以根据该信息处理请求。

- 例如：我们上文说的网上商城，当用户向购物车中添加一个商品时，服务器会将这个条信息封装成一个Cookie发送给浏览器，浏览器收到Cookie，会将它保存在内存中（注意这里的内存是本机内存，而不是服务器内存），那之后每次向服务器发送请求，浏览器都会携带该Cookie，而服务器就可以通过读取Cookie来判断用户到底买了哪些商品。当用户进行结账操作时，服务器就可以根据Cookie的信息来做结算。

- Cookie的用途：
  - 网上商城的购物车
  - 保持用户登录状态

- 总结一句话：Cookie，是一种服务器告诉浏览器以键值对形式存储小量信息的技术。

### 1.3 Cookie的工作原理

- 总的来看Cookie像是服务器发给浏览器的一张“会员卡”，浏览器每次向服务器发送请求时都会带着这张“会员卡”，当服务器看到这张“会员卡”时就可以识别浏览器的身份。

- 实际上这个所谓的“会员卡”就是服务器发送的一个响应头：

  ![1558667076701](images/1558667076701.png)

  如图Set-Cookie这个响应头就是服务器在向浏览器发“会员卡”，这个响应头的名字是Set-Cookie，后边JSESSIONID=95A92EC1D7CCB4ADFC24584CB316382E和 Path=/Test_cookie，是两组键值对的结构就是服务器为这个“会员卡”设置的信息。浏览器收到该信息后就会将它保存到内存或硬盘中。

- 当浏览器再次向服务器发送请求时就会携带这个Cookie信息：

  ![1558667127766](images/1558667127766.png)

  这是浏览器发送的请求报文，中间画红框的就是Cookie信息，这里可以理解为浏览器这次带着“会员卡”再次访问服务器。

- 于是服务器就可以根据Cookie信息来判断浏览器的状态。

- Cookie的缺点

  - Cookie因为请求或响应报文发送，无形中增加了网络流量。

  - Cookie是明文传送的安全性差。

  - 各个浏览器对Cookie有限制，使用上有局限。

## 第2章 Cookie的使用

### 2.1 Cookie的创建与设置

1. 在Servlet中创建Cookie对象，并添加到Response中。
2. 然后打开浏览器访问Servlet程序，服务器将Cookie信息发送给浏览器。
3. 浏览器收到Cookie后会自动保存，然后我们可以在下次浏览器发送请求时读取Cookie信息。

> 说明：按下F12查看Cookie内容

**图解Cookie的创建过程：**

![1558626326421](images/1558626326421.png)

**Cookie的创建代码：**

```java
/**
 * Cookie的代码
 */
public class CookieServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	protected void createCookie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Cookie的创建
		Cookie cookie = new Cookie("cookie-name", "cookie-Value");
		Cookie cookie2 = new Cookie("cookie-name2", "cookie-Value2");

		// 告诉浏览器保存
		response.addCookie(cookie);
		response.addCookie(cookie2);
		response.getWriter().write("已创建Cookie……");
	}
}

```

**web.xml文件中的配置**

```xml
<servlet>
	<servlet-name>CookieServlet</servlet-name>
	<servlet-class>com.atguigu.servlet.CookieServlet</servlet-class>
</servlet>

<servlet-mapping>
	<servlet-name>CookieServlet</servlet-name>
	<url-pattern>/cookieServlet</url-pattern>
</servlet-mapping>

```

**修改html页面中连接的访问地址为：**

```html
<li><a href="cookieServlet?action=createCookie" target="target">Cookie的创建</a></li>
```

然后点击访问。记住，访问的时候，一定不是把html的页面托到浏览器中访问，而是在浏览器里输出地址，通过访问Tomcat服务器访问页面。

**浏览器工具--查看结果：**

谷歌浏览器，直接按下F12功能键，会弹出调试工具，选择Resource-----Cookies----localhost查看localhost域名下的cookie。

![1558626500878](images/1558626500878.png)

如果是火狐浏览器。同样按下F12功能键，弹出调试工具（一定要记住启用所有窗口）。选择Cookies选择卡

![1558626536006](images/1558626536006.png)

### 2.2 Cookie的读取

读取Cookie主要指读取浏览器中携带的Cookie。

1. 服务器端获取浏览器传过来的Cookie代码：request.getCookies()
2. 遍历Cookie数组，获取所有Cookie信息
3. 修改html连接，点击访问
4. 打开浏览器工具查看HTTP协议内容
5. 查看服务器代码获取Cookie后的输出

**图解Cookie的获取过程**

![1558626636876](images/1558626636876.png)

**获取Cookie的代码：**

```java
protected void getCookie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取所有cookie对象
		Cookie[] cookies = request.getCookies();
		// 如果没有cookie，则返回null。
		if (cookies != null) {
			// 有cookie则遍历
			for (Cookie cookie : cookies) {
				response.getWriter().write("Cookie名：" + cookie.getName() 
						+ "<br/>Cookie值：" + cookie.getValue() + "<br/><br/>");
			}
		} else {
			response.getWriter().write("没有Cookie");
		}
		
	}

```

**修改html页面中的连接访问地址为：**

```html
<li><a href="cookieServlet?action=getCookie" target="target">Cookie的获取</a></li>
```

修改完之后，点击连接访问服务器。

**通过浏览器查看请求头：**

![1558626743021](images/1558626743021.png)

**页面输出：**

![1558626783286](images/1558626783286.png)

### 2.3 Cookie值的修改

1. 在Servlet中添加修改Cookie值的代码

2. 修改html页面中修改cookie的连接，并访问

3. 打开浏览器的调试工具查看，请求头和响应头中Cookie的信息

**图解修改Cookie值的过程：**

![1558626892380](images/1558626892380.png)

**修改Cookie值的代码：**

```java
protected void updateCookie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 创建一个已存在key的Cookie对象
		Cookie cookie = new Cookie("cookie-name", null);
		// 修改Cookie的值
		cookie.setValue("this is new value");
		// 通知浏览器保存修改
		response.addCookie(cookie);
		response.getWriter().write("Cookie…已修改值");
	}

```

**修改html页面中update修改cookie的访问地址为：**

```html
<li><a href="cookieServlet?action=updateCookie" target="target">Cookie值的修改</a></li>
```

修改完Cookie更新的连接访问之后，点击访问。

**打开浏览器调试工具查看请求头和响应头中Cookie的信息：**

![1558626979002](images/1558626979002.png)

**在Resource页签中，查看修改后Cookie的内容：**

![1558627009024](images/1558627009024.png)

### 2.4 Cookie的有效时间

- 经过上边的介绍我们已经知道Cookie是存储在浏览器中的，但是可想而知一般情况下浏览器不可能永远保存一个Cookie，一来是占用硬盘空间，再来一个Cookie可能只在某一时刻有用没必要长久保存。所以我们还需要为Cookie设置一个有效时间。

- Cookie的实例方法setMaxAge(  ) 控制Cookie存活的时间，接收一个int型参数，单位：秒。
  - 参数设置为0，即：setMaxAge(0)：立即失效，表示浏览器一收到响应后，就马上删除Cookie，下次浏览器发送请求时，将不会再携带该Cookie。
  - 参数设置大于0：比如setMaxAge(60)，表示有效的秒数60秒后，Cookie失效。
  - 参数设置小于0：比如setMaxAge(-1)，表示当前会话有效。也就是关闭浏览器后Cookie失效，被删除。
  - 如果不设置失效时间，默认为当前会话有效，一旦关闭浏览器，Cookie就失效，被删除。

**图解Cookie过期时间被修改的过程：**

![1558627416248](images/1558627416248.png)

**修改Cookie过期时间的代码：**

```java
protected void deleteCookie(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取Cookie
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		if (cookies != null) {
			// 查找出我们需要修改的Cookie对象
			for (Cookie c : cookies) {
				// 获取键为cookie-name的cookie对象
				if ("cookie-name".equals(c.getName())) {
					cookie = c;
					break;
				}
			}
		}
		if (cookie != null) {
//          负数表示浏览器关闭后删除，正数表示多少秒后删除
//			 设置为零，表示立即删除Cookie
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			response.getWriter().write("删除Cookie……");
		}
		
	}

```

**修改html页面中的连接地址：**

```html
<li><a href="cookieServlet?action=deleteCookie" target="target">Cookie立即删除</a></li>
```

修改之后，点击访问。

**通过浏览器调试工具查看请求响应信息。和Cookie信息：**

![1558627434014](images/1558627434014.png)

![1558627444677](images/1558627444677.png)

### 2.5 Cookie的路径Path设置

- Cookie的路径指告诉浏览器访问哪些地址时应该携带该Cookie，我们知道浏览器会保存很多不同网站的Cookie，比如百度的Cookie，新浪的Cookie，腾讯的Cookie等等。那我们不可能访问百度的时候携带新浪的Cookie，也不可能访问每个网站时都带上所有的Cookie，这是不现实的。所以往往我们还需要为Cookie设置一个Path属性，来告诉浏览器何时携带该Cookie。

- 我们通过调用Cookie的实例方法setPath()，来设置Cookie的Path路径。这个路径由浏览器来解析。
  - / ：代表服务器的根目录
  - 如果设置有效路径为：`/day14/abc`，则：下面几个路径能访问到Cookie
    - /day14/abc                             能获取Cookie
    - /day14/xxxx.xxx                     不能获取Cookie
    - /day14/abc/xxx.xxx               能获取Cookie
    - /day14/abc/a/b/c                   能获取Cookie
  - 如果不设置，默认会在访问“`/项目名`”下的资源时携带
    - 如：“/项目名/index.jsp” 、 “/项目名/hello/index.jsp”

**设置Cookie对象Path属性的代码**

```java
protected void setPath(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 创建一个Cookie对象
		Cookie cookie = new Cookie("cookie-path", "test");
		// 设置Cookie的有效访问路径为/day14/abc/下所有资源
		cookie.setPath(request.getContextPath() + "/abc");
		// 通知浏览器保存修改
		response.addCookie(cookie);
		response.getWriter().write("设置Cookie…的path路径");
	}

```

**当我们通过浏览器访问上面的代码，响应头中会有下如下信息：**

![1558627560065](images/1558627560065.png)

**分别通过不同的路径去访问，查看浏览器的请求头，是否会携带/day14/abc 路径下Cookie**

访问/day14/cookie.html请求头信息

![1558627601165](images/1558627601165.png)

访问/day14/abc/a/b/c.html请求头信息

![1558627633503](images/1558627633503.png)



## 第3章 Cookie练习：用户免输入

**需求：第一次登录之后，一个星期内免输入用户名登录。**

**1 - 服务器Servlet的代码**

```java
protected void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获取请求参数
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if ("admin".equals(username) && "admin".equals(password)) {
			// 创建Cookie
			Cookie cookie = new Cookie("username", username);
			// 设置过期时间为一个星期
			cookie.setMaxAge(60 * 60 * 24 * 7);
			// 通知浏览器保存
			response.addCookie(cookie);
			response.getWriter().write("登录成功！");
		} else {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		}
	}

```

**2 - WebContent/login.jsp页面**

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
    <form action="cookieServlet?action=login" method="post">
	用户名：<input name="username" type="text" value="${ cookie.username.value }" /><br /> 
	密&emsp;码：<input name="password" type="password" value="" /><br /> 
	7天免输入:<input type="checkbox" name="ck" value="ck"><br /> 
		<input type="submit" value="提 交" />
</form>
</body>
</html>

```



## 第4章 Session的介绍

### 4.1 为什么需要Session

- 使用Cookie有一个非常大的局限，就是如果Cookie很多，则无形的增加了客户端与服务端的数据传输量。而且由于浏览器对Cookie数量的限制，注定我们不能再Cookie中保存过多的信息，于是Session出现。

- Session的作用就是在服务器端保存一些用户的数据，然后传递给用户一个名字为JSESSIONID的Cookie，这个JESSIONID对应这个服务器中的一个Session对象，通过它就可以获取到保存用户信息的Session。

### 4.2 Session是什么

- 首先Session是jsp中九大内置对象之一

- 其次Session是一个域对象


- 然后Session是在服务器端用来保存用户数据的一种技术。并且Session会话技术是基于Cookie实现的。




## 第5章 Session的使用

### 5.1 Session的创建和获取

- Session的创建时机是在request.getSession()方法第一次被调用时。
  - 补充：request.getSession()之后的调用都是获取已经创建了的Session对象

- Session被创建后，同时还会有一个名为JSESSIONID的Cookie被创建。

- 这个Cookie的默认时效就是当前会话。

![1558627840672](images/1558627840672.png)

**下面是创建Session和获取Session。以及获取Session的 ID编号，获取Session是否是新创建的示例代码：**

```java
package com.atguigu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	public SessionServlet() {
	}

	protected void getSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getHeader("Cookie"));
		
		// 第一次调用就是创建一个新的Session。如果Session已经创建过。就获取原来的会话。
		HttpSession session = request.getSession();
		// 输出会话id号，和是否是新创建
		// session.getId()返回Session的唯一编号
		// session.isNew()返回当前Session是否是刚创建的
		response.getWriter().write("session ID:" + session.getId() + "<br/>是否是新的：" + session.isNew());
	}
}

```

**在web.xml文件中的配置：**

```xml
<servlet>
		<servlet-name>SessionServlet</servlet-name>
		<servlet-class>com.atguigu.servlet.SessionServlet</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>SessionServlet</servlet-name>
		<url-pattern>/sessionServlet</url-pattern>
	</servlet-mapping>

```

**第一次访问的结果：**

![1558627941430](images/1558627941430.png)

**之后每次访问的结果：**

![1558627964822](images/1558627964822.png)

### 5.2 Session的工作原理

- Session被创建后，对应的Cookie被保存到浏览器中，之后浏览器每次访问项目时都会携带该Cookie。

- 当我们再次调用时会根据该JSESSIONID获取已经存在的Cookie，而不是再创建一个新的Cookie。

- 如果Cookie中有JSESSIONID，但是JSESSIONID没有对应的Session存在，则会重新创建一个HttpSession对象，并重新设置JSESSIONID。

![1558748151080](images/1558748151080.png)



### 5.3 Session数据的存取

- Session域对象数据的存取和其他三个域对象PageContext、Request、ServletContext是一样的。只需要调用下面两个方法：
  - setAttribute 设置属性
  - getAttribute 获取属性

- 编写下面的java代码去访问，就可以在Session域中设置属性，和获取属性。

```java
protected void setAttribute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
		HttpSession session = request.getSession();
		// 设置数据
		session.setAttribute("abc", "abc value");
		response.getWriter().write("设置属性值成功！");
	}
	
	protected void getAttribute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
		HttpSession session = request.getSession();
		// 设置数据
		String value = (String) session.getAttribute("abc");
		response.getWriter().write("获取abc的属性值：" + value);
	}

```

修改session.html 中访问的连接地址，然后点击访问。

```html
<li>
	<a href="sessionServlet?action=setAttribute" target="target">Session域数据的存储</a></li>

<li>
	<a href="sessionServlet?action=getAttribute" target="target">Session域数据的获取</a></li>

```

访问后效果图：

![1558628097144](images/1558628097144.png)

![1558628140230](images/1558628140230.png)



### 5.4 Session 的有效时间

- **基本原则**

  - Session对象在服务器端不能长期保存，它是有时间限制的，超过一定时间没有被访问过的Session对象就应该释放掉，以节约内存。所以Session的有效时间并不是从创建对象开始计时，到指定时间后释放。而是**从最后一次被访问开始计时，统计其“空闲”的时间。**

- **默认时效**

  - 在tomcat的conf目录下web.xml配置文件中能够找到如下配置：

    ```xml
    <!-- ==================== Default Session Configuration ================= -->
      <!-- You can set the default session timeout (in minutes) for all newly   -->
      <!-- created sessions by modifying the value below.                       -->
    
        <session-config>
            <session-timeout>30</session-timeout>
        </session-config>
    
    ```

    > 说明：Session对象默认的最长有效时间为30分钟。

- **手动设置1：全局**

  - 我们也可以在自己工程的web.xml文件中配置Session会话的超时时间为10分钟。

  - 记住一点，我们在web.xml文件中配置的Session会话超时时间是对所有Session都生效的。

    ```xml
    <!-- 设置Session默认的过期时间  -->
    <session-config>
    	<!-- 以分钟为单位。10分钟超时  -->
        <session-timeout>10</session-timeout>
    </session-config>
    ```

- **手动设置2：局部**

  - int getMaxInactiveInterval()    获取超时时间。以秒为单位。
  - setMaxInactiveInterval (int seconds)  设置用户多长时间没有操作之后就会Session过期。以秒为单位。
    - 如果是正数。表示用户在给定的时间内没有任意操作，Session会话就会过期。
    - 如果是非正数（零&负数）。表示Session永不过期。

- **强制失效**

  - invalidate()

- **示例代码**

```java
Session在3秒之后超时
// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
HttpSession session = request.getSession();
// 设置过期时间为3秒 
session.setMaxInactiveInterval(3);

Session在1分钟之后超时
// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。 
HttpSession session = request.getSession();
// 设置过期时间为1分钟
session.setMaxInactiveInterval(60);

Session在1小时之后超时
// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
HttpSession session = request.getSession();
// 设置过期时间为1小时
session.setMaxInactiveInterval(60 * 60);

Session在1天之后超时
// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
HttpSession session = request.getSession();
// 设置过期时间为1天
session.setMaxInactiveInterval(60 * 60 * 24);

Session在1周之后超时
// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
HttpSession session = request.getSession();
// 设置过期时间为1周
session.setMaxInactiveInterval(60 * 60 * 24 * 7);

Session永远不超时
// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
HttpSession session = request.getSession();
// 设置永远不超时
session.setMaxInactiveInterval(-1);

Session马上超时（失效）
// 第一个调用就是获取一个新的Session。如果Session已经创建过。就获取原来的会话。
HttpSession session = request.getSession();
// 让Session对象立即过期
session.invalidate();

```

### 5.4 Session对象的释放

- Session对象空闲时间达到了目标设置的最大值，自动释放

- Session对象被强制失效

- Web应用卸载

- 服务器进程停止

### 5.5 Session的活化和钝化

- Session机制很好的解决了Cookie的不足，但是当访问应用的用户很多时，服务器上就会创建非常多的Session对象，如果不对这些Session对象进行处理，那么在Session失效之前，这些Session一直都会在服务器的内存中存在。那么，就出现了Session活化和钝化的机制。

- **Session钝化：**Session在一段时间内没有被使用或关闭服务器时，会将当前存在的Session对象及Session对象中的数据从内存序列化到磁盘的过程，称之为钝化。

- **Session活化：**Session被钝化后，服务器再次调用Session对象或重启服务器时，将Session对象及Session对象中的数据从磁盘反序列化到内存的过程，称之为活化。

- 如果希望Session域中的对象也能够随Session钝化过程一起序列化到磁盘上，则对象的实现类也必须实现java.io.Serializable接口。不仅如此，如果对象中还包含其他对象的引用，则被关联的对象也必须支持序列化，否则会抛出异常：java.io.NotSerializableException

### 5.6 浏览器和Session关联的技术内幕

在前面的演示中我们发现一旦浏览器关闭之后，我们再去获取Session对象就会创建一个新的Session对象。这是怎么回事呢。现在让我们来看一下。这一系列操作过程中的内幕细节。

![1558628349795](images/1558628349795.png)

通过上图的分析，我们不难发现。当浏览器关闭之后。只是因为浏览器无法再通知服务器，之前创建的Session的会话id是多少了。所以服务器没办法找到对应的Session对象之后，就以为这是第一次访问服务器。就创建了新的Session对象返回。

## 第6章 URL重写（了解）

- 在整个会话控制技术体系中，保持JSESSIONID的值主要通过Cookie实现。但Cookie在浏览器端可能会被禁用，所以我们还需要一些备用的技术手段，例如：URL重写。

- URL重写其实就是将JSESSIONID的值以固定格式附着在URL地址后面，以实现保持JSESSIONID，进而保持会话状态。这个固定格式是：URL;jsessionid=xxxxxxxxx

- 例如：

  ```java
  targetServlet;jsessionid=97120112D5538009334F1C6DEADB1BE7
  ```

- 实现方式：

  - response.encodeURL(String)

  - response.encodeRedirectURL(String)

- 示例代码

  ```java
  //1.获取Session对象
  HttpSession session = request.getSession();
  		
  //2.创建目标URL地址字符串
  String url = "targetServlet";
  		
  //3.在目标URL地址字符串后面附加JSESSIONID的值
  url = response.encodeURL(url+";jsessionid=97120112D5538009334F1C6DEADB1BE7");
  		
  //4.重定向到目标资源
  response.sendRedirect(url);
  
  ```

## 第7章 处理表单重复提交问题

- 表单重复提交的危害
  - 可重复注册，对数据库进行批处理攻击。（验证码已解决该问题）
  - 可重复提交已付款表单，用户支付一次订单费用，下了多个订单
  - 等待...
- 解决表单重复提交的步骤
  - 生成一个不可重复（全球唯一）的随机数(uuid)
  - 在提交表单前，将随机数(uuid)分别存放到表单内的隐藏域，和session域对象中
  - 发送“提交表单”请求
  - 判断是否提交表单，具体操作如下：
    - 分别获取隐藏域和session域中的uuid
    - 判断两个域中的数据是否相等
      - 相等：提交表单，并将session域中的uuid移除
      - 不等：不提交表单
- UUID
  - 定义：是一个32位16进制的随机数
  - 特点：全球唯一
  - 使用：java.util.UUID.randomUUID()

# Filter(过滤器)

***

## 第1章 什么是Filter

### 1.1 Filter简介

- Filter中文意思为过滤器。顾名思义，过滤器可在浏览器以及目标资源之间起到一个过滤的作用。例如：水净化器，可以看成是生活中的一个过滤器，他可以将污水中的杂质过滤，从而使进入的污水变成净水。

- 对于WEB应用来说，过滤器是一个驻留在服务器中的WEB组件，他可以截取客户端和WEB资源之间的请求和响应信息。
  - WEB资源可能包括Servlet、JSP、HTML页面等。

![1558767049205](images/1558767049205.png)

- 当服务器收到特定的请求后，会先将请求交给过滤器，程序员可以在过滤器中对请求信息进行读取修改等操作，然后将请求信息再发送给目标资源。目标资源作出响应后，服务器会再次将响应转交给过滤器，在过滤器中同样可以对响应信息做一些操作，然后再将响应发送给服务器。

- 也就是说过滤器可以在WEB资源收到请求之前，浏览器收到响应之前，对请求和响应信息做一些相应的操作。

- 在一个WEB应用中可以部署多个过滤器，多个过滤器就组成了一个过滤器链，请求和响应必须在经过多个过滤器后才能到达目标；

![1558774993622](images/1558774993622.png)

- 过滤器不是必须将请求传送到下一个过滤器（或WEB资源），也可以自己来处理请求，发送响应。
- 当配置多个Filter以后就有一个执行顺序的问题，实际执行顺序是按照在web.xml文件中servlet-mapping的顺序决定的，如果顺序越靠前越先被调用。

### 1.2 总结

- Filter是一个接口。
- Filter是Java Web三大组件之一。（JavaWeb三大组件分别是：Servlet小程序、Filter过滤器、Listener监听器）
- Filter是服务器专门用来过滤请求，拦截响应的。 
- **Filter的常见作用：**
  - 检查用户访问权限
  - 设置请求响应编码，解决乱码问题

### 1.3 主要API

- 编写Filter和编写Servlet类似，都需要实现接口。

#### 1.3.1 Filter接口

- 编写Filter需要实现Filter接口，我们来看一下Filter接口的主要方法：

  ![1558775226983](images/1558775226983.png)

  - init()方法用于初始化Filter
  - doFilter()作用和service()方法类似，是过滤请求和响应的主要方法。
  - destroy()用于在Filter对象被销毁前做一些收尾工作。如：释放资源等。

#### 1.3.2 FilterConfig接口

![1558775582961](images/1558775582961.png)

- FilterConfig对象在服务器调用init()方法时传递进来。
  - getFilterName() 获取Filter的名字
  - getServletContext() 获取ServletContext对象（即application）
  - getInitParameter() 获取Filter的初始化参数
  - getInitParameterNames() 获取所有初始化参数的名字

#### 1.3.3 FilterChain接口

![1558775624400](images/1558775624400.png)

- FilterChain对象是在doFilter()方法被调用时作为参数传递进来的。
  - doFilter()方法用于调用Filter链上的下一个过滤器，如果当前过滤器为最后一个过滤器则将请求发送到目标资源。

## 第2章 Filter初体验

### 需求：

现在在WebContent目录下有一个目录admin。这个目录是管理员操作的目录。这个目录里有jsp文件，有html文件，还有图片资源文件。现在我们要让这些资源都在用户登录才能被访问。那么我们要怎么实现这样的需求。

### 思路：

前面我们讲过Session。有同学可能会想，我们可以在用户登录之后把用户的信息保存在Session域对象中。然后在jsp页面里通过Session域对象获取用户的信息。如果用户信息存在，说明用户已登录。否则就重定向到登录页面。这个方案可行。可是html页面呢? html页面是没有Session域对象的。

### 解决方案：

这就需要我们使用Filter过滤器来进行请求的拦截。然后判断Session域对象中是否包含用户的信息。

现在我们以admin目录下user.jsp为例进行讲解。

1、首先，我们需要创建一个类来实现Filter接口，用来检查Session中是否包含用户信息。

2、实现Filter中的doFilter方法

3、然后到web.xml文件中去配置Filter的过滤信息。

4、然后重启服务器访问测试



**操作步骤1：Filter1的类代码**

```java
package com.atguigu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Filter1 implements Filter {

	/**
	 * Filter初始化方法
	 */
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * Filter的过滤方法
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 强转
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// 获取用户登录信息
		String username = (String) httpRequest.getSession().getAttribute("username");
		if (username != null) {
			// 过滤器中，只要允许用户访问资源，一定要调用chain.doFilter方法，否则用户永远访问不到资源
			chain.doFilter(request, response);
		} else {
			// 如果用户未登录。返回登录页面
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
		}
	}

	/**
	 * Filter销毁的方法
	 */
	public void destroy() {
	}

}

```

**操作步骤2：web.xml文件中的Filter配置**

```xml
<!-- 配置Filter1 -->
<filter>
	<!-- 给Filter1起一个名字 -->
	<filter-name>Filter1</filter-name>
	<!-- 是哪一个Filter类，即全类名 -->
	<filter-class>com.atguigu.filter.Filter1</filter-class>
</filter>

<filter-mapping>
	<!-- Filter的名字 -->
	<filter-name>Filter1</filter-name>
	<!-- Filter1的过滤地址
		表示过滤http://127.0.0.1:8080/day17/admin/user.jsp
	-->
	<url-pattern>/admin/user.jsp</url-pattern>
</filter-mapping>

```

- 除此之外在filter-mapping还有一个子标签dispatcher，该标签用来指定需要Filter处理的请求类型，该标签可以配置四个值：

  ```xml
  <!-- 用户直接访问资源时，会调用Filter -->
  <dispatcher>REQUEST</dispatcher>
  
  <!-- 通过转发访问时，会调用Filter -->
  <dispatcher>FORWARD</dispatcher>
  
  <!-- 通过动态包含获取时，会调用Filter -->
  <dispatcher>INCLUDE</dispatcher>
  
  <!-- 当通过异常处理访问页面时，会调用Filter -->
  <dispatcher>ERROR</dispatcher>
  ```

  - 这四种情况可以设置一个，也可以同时设置多个，如果不设置那么默认为REQUEST。

## 第3章 Filter的生命周期

**Servlet的生命周期**

1. 先执行构造方法

2. 执行init方法做初始化操作

3. 执行service方法

4. 销毁的时候调用destory方法

 

**Filter生命周期：**

1. 先执行Filter的构造方法

2. 然后执行Filter的init()方法，对象创建后，马上就被调用，对Filter做一些初始化操作

3. 执行Filter的doFilter()方法，每次访问目标资源，只要匹配过滤的地址，就会调用。

4. 执行Filter的destroy()方法，服务器停止时调用，用来释放资源。

***

**创建一个Filter2类。代码如下：**

```java
package com.atguigu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class Filter2 implements Filter {

	public Filter2() {
		System.out.println("Filter2 构造 方法 被调用");
	}
	
	/**
	 * Filter初始化方法
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Filter2 init 方法被调用。初始化……");
	}

	/**
	 * Filter的过滤方法
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("Filter2 doFilter 方法被调用  ");
		// 一定要调用此方法，否则用户访问的资源会访问不到。
		chain.doFilter(request, response);
	}

	/**
	 * Filter销毁的方法
	 */
	public void destroy() {
		System.out.println("Filter2 的destroy方法被调用……");
	}

}

```

**web.xml文件中的配置：**

```xml
<!-- 配置Filter2 -->
	<filter>
		<!-- 给Filter2起一个名字 -->
		<filter-name>Filter2</filter-name>
		<!-- 是哪一个Filter类 -->
		<filter-class>com.atguigu.filter.Filter2</filter-class>
	</filter>
	<filter-mapping>
		<!-- Filter的名字 -->
		<filter-name>Filter2</filter-name>
		<!-- Filter1的过滤地址
			表示过滤http://127.0.0.1:8080/day17/login.jsp
		 -->
		<url-pattern>/login.jsp</url-pattern>
	</filter-mapping>

```

然后打开浏览器访问 http://127.0.0.1:8080/day17/login.jsp

**查看整个控制台的打印如下：**

1. Filter在工程启动的时候初始化。 

2. 在访问过滤的时候调用doFilter()方法

3. Tomcat关闭Filter被销毁的时候调用destory()方法

![1558764250206](images/1558764250206.png)





## 第4章 FilterConfig类

- 作用：FilterConfig类和ServletConfig类是一样的。可以获取Filter在web.xml文件中的配置信息，做初始化之用。

- 我们可以在web.xml文件中给Filter添加初始化参数。然后在init初始化方法中使用FilterConfig类获取到初始化的参数。

- FilterConfig类，一般有三个作用：
  - 获取Filter在web.xml文件中配置的名称
  - 获取Filter在web.xml文件中配置的初始化参数
  - 通过FilterConfig类获取ServletContext对象实例



**第一步：修改Filter2在web.xml中的配置信息**

```xml
<!-- 配置Filter2 -->
	<filter>
		<!-- 给Filter2起一个名字 -->
		<filter-name>Filter2</filter-name>
		<!-- 是哪一个Filter类 -->
		<filter-class>com.atguigu.filter.Filter2</filter-class>
		<!-- 配置初始化参数 -->
		<init-param>
			<!-- 初始化参数的名称 -->
			<param-name>username</param-name>
			<!-- 初始化参数的值 -->
			<param-value>root</param-value>
		</init-param>
	</filter>

```

**第二步：修改Filter2中init方法的代码**

```java
/**
	 * Filter初始化方法
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Filter2 init 方法被调用。初始化……");
		// 获取Filter的名称
		String filterName = filterConfig.getFilterName();
		System.out.println("Filter name ==>>> " + filterName);
		// 获取初始化参数。username的值
		String username = filterConfig.getInitParameter("username");
		System.out.println("username ==>> " + username);
		// 获取ServletContext的对象实例 
		ServletContext ctx = filterConfig.getServletContext();
		System.out.println(ctx);
	}

```

**第三步：重启Tomcat服务器，控制台打印如下：**

![1558764469028](images/1558764469028.png)



## 第5章 FilterChain过滤器链（重点）

- FilterChain是整个Filter过滤器的调用者。Filter与Filter之间的传递，或者Filter与请求资源之间的传递都靠FilterChain.doFilter方法。

- 一般Filter.doFilter中的代码分为三段：
  - 第一段是FilterChain.doFilter之前的代码。一般用来做请求的拦截，检查用户访问的权限，访问日记的记录。参数编码的设置等等操作。
  - 第二段是FilterChain.doFilter方法。此方法可以将代码的执行传递到下一个Filter中。或者是传递到用户最终访问的资源中。
  - 第三段是FilterChain.doFilter之后的代码。主要用过做一些日志操作。我们很少会在第三段中做太多复杂的操作。

- 在每一个Filter类的doFilter方法中，一定要调用chain.doFilter方法，除非你想要阻止用户继续往下面访问。否则一定要调用FilterChain的doFilter方法。



**5.1 图解：多个Filter过滤器的代码流转**

​	![1558764647060](images/1558764647060.png)

**5.2 现在我们添加两个Filter类，对同一个资源进行过滤**

第一个Filter类ChainFilter1 代码：

```java
package com.atguigu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ChainFilter1 implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("资源访问前---ChainFilter1 -- 开始执行");
		// 转发下一个Filter或者请求的资源
		chain.doFilter(request, response);
		System.out.println("资源访问后---ChainFilter1 -- 执行结束");
	}

	public void destroy() {
	}

}

```

第二个Filter类ChainFilter2 代码：

```java
package com.atguigu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ChainFilter2 implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("资源访问前---ChainFilter2 -- 开始执行");
		// 转发下一个Filter或者请求的资源
		chain.doFilter(request, response);
		System.out.println("资源访问后---ChainFilter2 -- 执行结束");
	}

	public void destroy() {
	}

}

```

**5.3 在web.xml文件中的配置如下：**

```xml
<filter>
		<filter-name>ChainFilter1</filter-name>
		<filter-class>com.atguigu.filter.ChainFilter1</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>ChainFilter1</filter-name>
		<url-pattern>/chainFilter.jsp</url-pattern>
	</filter-mapping>
	<filter>
		<filter-name>ChainFilter2</filter-name>
		<filter-class>com.atguigu.filter.ChainFilter2</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>ChainFilter2</filter-name>
		<url-pattern>/chainFilter.jsp</url-pattern>
	</filter-mapping>

```

**5.4 WebContent/chainFilter.jsp文件的内容如下：**

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<%
			System.out.println("这是请求资源的代码");
		%>
		这是ChainFilter.jsp
	</body>
</html>

```

**5.5 打开浏览器输入http://127.0.0.1:8080/day17/chainFilter.jsp回车访问：**

![1558764839164](images/1558764839164.png)

> **千万要注意：**在Filter类的doFilter方法中，除非你要拦截请求的资源，否则一定要调用FilterChain参数的doFilter方法让代码的执行传递到下一个Filter或访问的资源中。



## 第6章 Filter的拦截路径（目标资源的配置）

- Filter的目标资源指的是需要调用Filter来进行过滤处理的资源，例如上文我们配置的/index.html就是我们的目标资源，当我们访问项目根目录下的index.html时就会调用HelloFilter来进行过滤。

- 目标资源的配置方式主要有以下两大种：

  - 第一种：通过filter-mapping的url-pattern来配置（与Servlet的url-pattern的规则相同）

    - **精确匹配：/路径/资源名**

      比如：/index.html、/hello/index.jsp 、 /client/LoginServlet 等，只要在请求地址完全一样时才会调用Filter

    - **目录匹配：/路径名/*** 

      比如1：/abc/* 表示可以拦截abc目录下的所有资源，甚至是abc目录下的其他目录。其中：/* 表示访问 当前工程下所有资源

      比如2：/* 表示只要访问项目根目录下的资源就会调用Filter

    - **后缀名匹配：*.后缀名** 

      比如：*.jsp 表示拦截所有后缀为jsp文件资源

  -  第二种：通过filter-mapping中的servlet-name来指定要过滤的Servlet

     - 如：以下是一个项目中的web.xml配置文件，在项目中有一个Filter加做HelloFilter，一个Servlet叫做HelloServlet。在Filter的filter-mapping中增加了一个servlet-name标签，将该标签的值设置成Servlet的名字，在访问Servlet时就会调用该过滤器过滤请求。

       ```xml
       <filter>
           <filter-name>HelloFilter</filter-name>
           <filter-class>com.atguigu.web.filter.HelloFilter</filter-class>
         </filter>
         <filter-mapping>
           <filter-name>HelloFilter</filter-name>
           <servlet-name>HelloServlet</servlet-name>
         </filter-mapping>
         <servlet>
           <servlet-name>HelloServlet</servlet-name>
           <servlet-class>com.atguigu.web.servlet.HelloServlet</servlet-class>
         </servlet>
         <servlet-mapping>
           <servlet-name>HelloServlet</servlet-name>
           <url-pattern>/HelloServlet</url-pattern>
         </servlet-mapping>
       
       ```

针对于第一种情况：

精确匹配前面 ，我们已经演示过了。

下面我们以目录匹配为示例展示代码。大家可以在此基础上修改web.xml文件中的\<url-pattern>标签来测试自己想要的路径。

**1. Filter的代码如下**

```java
package com.atguigu.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FilterPath implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("filter path 执行了");
		// 将代码执行传递到下一个Filter或者是请求资源
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}

```

**2. web.xml文件中的配置内容：**

```xml
<filter>
	<filter-name>FilterPath</filter-name>
	<filter-class>com.atguigu.filter.FilterPath</filter-class>
</filter>

<filter-mapping>
	<filter-name>FilterPath</filter-name>
	<url-pattern>/admin/*</url-pattern>
</filter-mapping>

```



## 第7章 HttpFilter

- 回想Servlet中学习，我们发现：实现Servlet接口，不如继承HttpServlet应用方便。所以，我们想到需要继承HttpFilter。

- 如果tomcat类库没有提供HttpFilter,就需要我们自己设计一个HttpFilter。

  - 类比最终创建Servlet的方式，我们发现设计HttpFilter大体分为以下几个步骤：

    1. 提供getFilterConfig()和getServletContext()

       ![1558764839164](images/1558775624401.png)

    2. 将doFilter()重载并抽象化处理

    3. 原有doFilter()方法需要将参数转换类型后，调用重载doFilter()方法

       ![1558764839164](images/1558775624402.png)

# Listener(监听器)

***

## 第1章 Listener简介

- Listener用于监听JavaWeb程序中的事件。

- 例如：ServletContext、HttpSession、ServletRequest的创建、修改和删除。

- Listener和我们之前学习的JS中的事件处理机制类似。在JS中，当我们触发某个事件后（例如：点击一个按钮）程序会调用一个响应函数来处理事件。同样的，在JavaWeb中，我们可以为某些事件来设置监听器，当事件被触发时，监听器中的指定方法将会被调用。

## 第2章 观察者模式

- Listener的原理是基于观察者模式的，所谓观察者模式简单来说，就是当被观察者的特定事件被触发（一般这某些方法被调用）后，会通知观察者（调用观察者的方法），观察者可以在自己的方法中来对事件做一些处理。

- 在我们的JavaWeb中，观察者就是Listener，而被观察者可能有三个ServletContext、HttpSession、ServletRequest。而事件指的就是这些对象的创建、修改和删除等。



## 第3章 Listener的分类

### 3.1 监听对象的创建与销毁

- **ServletContextListener**

  - 作用：监听ServletContext对象的创建与销毁

  - 方法：

    public void contextInitialized ( ServletContextEvent sce )：ServletContext创建时调用

    public void contextDestroyed ( ServletContextEvent sce )：ServletContext销毁时调用

  - ServletContextEvent对象

    - 作用：public ServletContext getServletContext ()：获取ServletContext对象

- **HttpSessionListener**

  - 作用：监听HttpSession对象的创建与销毁

  - 方法：
    - public void sessionCreated ( HttpSessionEvent se )：HttpSession对象创建时调用
    - public void sessionDestroyed ( HttpSessionEvent se )：HttpSession对象销毁时调用
  - HttpSessionEvent对象
    - 作用：public HttpSession getSession ()：获取当前HttpSession对象

- **ServletRequestListener**

  - 作用：监听ServletRequest对象的创建与销毁

  - 方法：

    - public void requestInitialized ( ServletRequestEvent sre )：ServletRequest对象创建时调用
    - public void requestDestroyed ( ServletRequestEvent sre )：ServletRequest对象销毁时调用

  - ServletRequestEvent对象

    - 作用：

      public ServletRequest getServletRequest ()：获取当前的ServletRequest对象。

      public ServletContext getServletContext ()：获取当前项目的ServletContext对象。

#### 3.1.1 创建与销毁监听器的使用

- 三种创建与销毁的监听器使用起来基本一致。

- 下边来编写一个ServletContext的监听器：

  - 步骤一：创建一个类实现ServletContextListener

    ```java
    public class MyServletContextListener implements ServletContextListener {
    	@Override
    	public void contextInitialized(ServletContextEvent sce) {
    		System.out.println("哈哈，我是ServletContext，我出生了");
    	}
    	@Override
    	public void contextDestroyed(ServletContextEvent sce) {
    		System.out.println("~~~~(>_<)~~~~，我是ServletContext，我要死了");	
    	}
    }
    
    ```

  - 步骤二：在web.xml文件中注册监听器

    ```xml
    <listener>
    <listener-class>com.atguigu.web.listener.MyServletContextListener</listener-class>
    </listener>
    
    ```

    > 说明1：由于ServletContext对象在服务器启动时创建，停止时销毁。所以启动服务器时我们会发现contextInitialized()方法被调用，服务器停止时contextDestroyed()方法被调用。
    >
    > 说明2：其他两个监听器和该监听器使用方法一样，不再多说。

### 3.2 监听对象的属性变化

- **ServletContextAttributeListener**

  - 作用：监听ServletContext中属性的创建、修改和销毁

  - 方法：

    public void attributeAdded(ServletContextAttributeEvent scab)：向ServletContext中添加属性时调用

    public void attributeRemoved(ServletContextAttributeEvent scab)：从ServletContext中移除属性时调用

    public void attributeReplaced(ServletContextAttributeEvent scab)：当ServletContext中的属性被修改时调用

  - ServletContextAttributeEvent对象

    - 作用：

      public String getName() ：获取修改或添加的属性名

      public Object getValue()：获取被修改或添加的属性值

      public ServletContext getServletContext ()：获取当前WEB应用的ServletContext对象

- **HttpSessionAttributeListener**

  - 作用：监听HttpSession中属性的创建、修改和销毁

  - 方法：

    public void attributeAdded ( HttpSessionBindingEvent se )：向HttpSession中添加属性时调用

    public void attributeRemoved(HttpSessionBindingEvent se)：从HttpSession中移除属性时调用

    public void attributeReplaced(HttpSessionBindingEvent se)：当HttpSession中的属性被修改时调用

  - HttpSessionBindingEvent对象

    - 作用：

      public String getName() ：获取修改或添加的属性名

      public Object getValue()：获取被修改或添加的属性值

      public HttpSession getSession ()：获取当前的HttpSession对象

- **ServletRequestAttributeListener**

  - 作用：监听ServletRequest中属性的创建、修改和销毁

  - 方法：

    public void attributeAdded (ServletRequestAttributeEvent srae ):向ServletRequest中添加属性时调用

    public void attributeRemoved(ServletRequestAttributeEvent srae):从ServletRequest中移除属性时调用

    public void attributeReplaced(ServletRequestAttributeEvent srae)：当ServletRequest中的属性被修改时调用

  - ServletRequestAttributeEvent对象

    - 作用：

      public String getName()：获取修改或添加的属性名

      public Object getValue()：获取被修改或添加的属性值

      public ServletRequest getServletRequest () ：获取当前的ServletRequest对象

#### 3.2.1 对象属性变化监听器的使用

同样三种对象属性变化监听器使用方式类似，下边以request属性监听器为例。

- 创建一个类实现ServletRequestAttributeListener接口

```java
public class ReqAttrListener implements ServletRequestAttributeListener {
	@Override
	public void attributeAdded(ServletRequestAttributeEvent srae) {
		System.out.println("request域中添加一个属性"+srae.getName()+"="+srae.getValue());
	}
	@Override
	public void attributeRemoved(ServletRequestAttributeEvent srae) {
		System.out.println("request域中移除一个属性"+srae.getName()+"="+srae.getValue());
	}
	@Override
	public void attributeReplaced(ServletRequestAttributeEvent srae) {
		System.out.println("request域中一个属性被修改了"+srae.getName()+"="+srae.getValue());
	}

}

```

- 在web.xml文件中注册监听器

```xml
<listener>
		<listener-class>com.atguigu.web.listener.ReqAttrListener</listener-class>
</listener>

```

如此当我们操作request域中的属性时，对应方法将会被调用。

### 3.3 监听Session内的对象

- **HttpSessionBindingListener**

  - 作用：监听某个对象在session域中的创建与移除。

  - 方法：

    public void valueBound(HttpSessionBindingEvent event)：该类的实例被放到Session域中时调用

    public void valueUnbound(HttpSessionBindingEvent event)：该类的实例从Session中移除时调用

  - HttpSessionBindingEvent对象

    - 作用：

      public HttpSession getSession ()：获取HttpSession对象

      public String getName()：获取操作的属性名

      public Object getValue()：获取操作的属性值

- 使用：要监听哪一个类，直接使该类实现HttpSessionBindingListener接口即可。

```java
public class Student implements HttpSessionBindingListener {
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		//doSomeThing
	}
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		//doSomeThing
	}
}

```

- **HttpSessionActivationListener**

  - 作用：监听某个对象在session中的序列化与反序列化。

  - 方法：

    public void sessionWillPassivate(HttpSessionEvent se)：该类实例和Session一起钝化到硬盘时调用

    public void sessionDidActivate(HttpSessionEvent se)：该类实例和Session一起活化到内存时调用

  - HttpSessionEvent对象

    - 作用：

      public HttpSession getSession ()：获取HttpSession对象

- 使用：要监听哪一个类，直接使该类实现HttpSessionActivationListener接口即可。

```java
public class Student implements HttpSessionActivationListener , Serializable {
	private static final long serialVersionUID = 1L;
	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		// TODO Auto-generated method stub
	}
	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		// TODO Auto-generated method stub
	}
}

```

注意：这里为了是Student对象可以正常序列化到硬盘上，还需要让类实现java.io.Serializable接口

# 文件的上传和下载

***

- 文件的上传和下载在web应用中是非常常用，也是非常有用的功能。
  - 例如：发送电子邮件时可以同过上传附件发送文件，OA系统中可以通过上传文件来提交公文，社交网站通过上传图片来自定义头像等等。
  - 例如：下载实际上只要资源放在用户可访问的目录中用户就可以直接通过地址下载，但是一些资源是存放到数据库中的，还有一些资源需要一定权限才能下载，这里就需要我们通过Servlet来完成下载的功能。

- 可以说上传和下载是每一个web应用都需要具有的一个功能，所以需要我们掌握。

## 第1章 文件的上传

### 1.1 文件上传的步骤

文件的上传主要分成两个步骤：

1. 用户在页面中选择要上传的文件，然后将请求提交到Servlet

2. Servlet收到请求，解析用户上传的文件，然后将文件存储到服务器

### 1.2 创建上传文件的表单

1. 创建一个form表单

```html
<form action="" method="post" enctype="multipart/form-data">
	<input type="file" name="file" /><br /><br />
	<input type="submit" value="上传" />
</form>

```

- 文件上传的表单和之前的表单类似，但有以下内容需要注意：

  - 表单的method属性必须为post
  - 表单的enctype属性必须为multipart/form-data
  - 上传文件的控件是input，type属性为file

- 该表单打开后是如下效果：

  - IE

    ![1558975331009](images/1558975331009.png)

  - Chrome

    ![1558975309963](images/1558975309963.png)

  - 火狐

    ![1558975370024](images/1558975370024.png)

2. 编写Servelet

   - 页面的表单控件创建好以后，选中文件点击上传按钮请求将会提交到指定的Servlet来处理。

   - 注意：这里不能再像以前的Servlet中那样，通过request.getParamter()来获取请求参数了，当enctype="multipart/form-data" 时，再使用getParamter()获取到内容永远为空。因为浏览器发送请求的方式已经改变。

   - 既然以前的方法不能使用了，这里我们必须要引入一个新的工具来解析请求中的参数和文件，这个工具就是commons-fileupload。

### 1.3 commons-fileupload

- commons-fileupload是Apache开发的一款专门用来处理上传的工具，它的作用就是可以从request对象中解析出，用户发送的请求参数和上传文件的流。

- **commons-fileupload包依赖commons-io，两个包需要同时导入。**

- 核心类：

  1. **DiskFileItemFactory**

     1. 工厂类，用于创建ServletFileUpload，设置缓存等

     2. 该类一般直接使用构造器直接创建实例

     3. 方法：

        - public void setSizeThreshold(int sizeThreshold)：用于设置缓存文件的大小（默认值10kb）

        - public void setRepository(File repository)：用于设置缓存文件位置（默认系统缓存目录）

  2. **ServletFileUpload**

     1. 该类用于解析request对象从而获取用户发送的请求参数（包括普通参数和文件参数）

     2. 该类需要调用有参构造器创建实例，构造器中需要一个DiskFileItemFactory作为参数

     3. 方法：

        - public List<FileItem> parseRequest(HttpServletRequest request)：解析request对象，获取请求参数，返回的是一个List，List中保存的是一个FileItem对象，一个对象代表一个请求参数。

        - public void setFileSizeMax(long fileSizeMax)：设置单个文件的大小限制，单位为B。如果上传文件超出限制，会在parseRequest()抛出异常FileSizeLimitExceededException。

        - public void setSizeMax(long sizeMax)：限制请求内容的总大小，单位为B。如果上传文件超出限制，会在parseRequest()抛出异常SizeLimitExceededException。

  3. **FileItem**

     1. 该类用于封装用户发送的参数和文件，也就是用户发送来的信息将会被封装成一个FileItem对象，我们通过该对象获取请求参数或上传文件的信息。

     2. 该类不用我们手动创建，由ServletFileItem解析request后返回。

     3. 方法：

        - String getFieldName()：获取表单项的名字，也就是input当中的name属性的值。

        - String getName()：获取上传的文件名，普通的请求参数为null。

        - String getString(String encoding)：获取内容，encoding参数需要指定一个字符集。

          ​	① 若为文件，将文件的流转换为字符串。

          ​	② 若为请求参数，则获取请求参数的value。

        - boolean isFormField()：判断当前的FileItem封装的是普通请求参数，还是一个文件。

          ​	① 如果为普通参数返回：true

          ​	② 如果为文件参数返回：false

        - String getContentType()：获取上传文件的MIME类型

        - long getSize()：获取内容的大小

        - write():将文件上传到服务器

- 示例代码：创建一个Servlet并在doPost()方法中编写如下代码

  ```java
  //创建工厂类
  DiskFileItemFactory factory = new DiskFileItemFactory();
  //创建请求解析器
  ServletFileUpload fileUpload = new ServletFileUpload(factory);
  //设置上传单个文件的的大小
  fileUpload.setFileSizeMax(1024*1024*3);
  //设置上传总文件的大小
  fileUpload.setSizeMax(1024*1024*3*10);
  //设置响应内容的编码
  response.setContentType("text/html;charset=utf-8");
  try {
  	//解析请求信息，获取FileItem的集合
  	List<FileItem> items = fileUpload.parseRequest(request);
  	//遍历集合
  	for (FileItem fileItem : items) {
  		//如果是普通的表单项
  		if(fileItem.isFormField()){
  		    //获取参数名
  		    String fieldName = fileItem.getFieldName();
  		    //获取参数值
  		    String value = fileItem.getString("utf-8");
  		    System.out.println(fieldName+" = "+value);
  	        //如果是文件表单项
  	    }else{
  		    //获取文件名
  		    String fileName = fileItem.getName();
  		    //获取上传路径
  		    String realPath = getServletContext().getRealPath("/WEB-INF/upload");
  		    //检查upload文件夹是否存在，如果不存在则创建
  		    File f = new File(realPath);
  		    if(!f.exists()){
  			    f.mkdir();
  		    };
  		    //为避免重名生成一个uuid作为文件名的前缀
  		    String prefix = UUID.randomUUID().toString().replace("-", "");
  		    //将文件写入到服务器中
  		    fileItem.write(new File(realPath+"/"+prefix+"_"+fileName));
  		    //清楚文件缓存
  		    fileItem.delete();
  	    }
  }
  } catch (Exception e) {
  	if(e instanceof SizeLimitExceededException){
  		//文件总大小超出限制
  		response.getWriter().print("上传文件的总大小不能超过30M");
  	}else if(e instanceof FileSizeLimitExceededException){
  		//单个文件大小超出限制
  		response.getWriter().print("上传单个文件的大小不能超过3M");
  	}
  } 
  response.getWriter().print("上传成功");
  
  ```

## 第2章 文件的下载

### 2.1 使用说明

- 文件下载最直接的方法就是把文件直接放到服务器的目录中，用户直接访问该文件就可以直接下载。

- 但是实际上这种方式并不一定好用，比如我们在服务器上直接放置一个MP3文件，然后通过浏览器访问该文件的地址，如果是IE浏览器可能就会弹出下载窗口，而如果是FireFox和Chrome则有可能直接播放。再有就是有一些文件我们是不希望用户可以直接访问到的，这是我们就要通过Servlet来完成下载功能。

- 下载文件的关键是几点：

  1. 服务器以一个流的形式将文件发送给浏览器。

  2. 发送流的同时还需要设置几个响应头，来告诉浏览器下载的信息。

     - 具体响应头如下：
       - Content-Type
         - 下载文件的MIME类型
         - 可以通过servletContext. getMimeType(String file)获取
         - 也可以直接手动指定
         - 使用response.setContentType(String type);
         - 响应头样式：Content-Type: audio/mpeg
       - Content-Disposition
         - 下载文件的名字，主要作用是提供一个默认的用户名
         - 通过response.setHeader("Content-Disposition", disposition)设置
         - 响应头样式：Content-Disposition: attachment; filename=xxx.mp3
       - Content-Length
         - 下载文件的长度，用于设置文件的长处（不必须）
         - 通过response. setContentLength(int len)设置。
         - 设置后样式：Content-Length: 3140995

  3. 接下来需要以输入流的形式读入硬盘上的文件

     - FileInputStream is = new FileInputStream(file);
     - 这个流就是我们一会要发送给浏览器的内容

  4. 通过response获取一个输出流，并将文件（输入流）通过该流发送给浏览器

     - 获取输出流：ServletOutputStream out = response.getOutputStream();

     - 通过输出流向浏览器发送文件（不要忘了关闭输入流）

       ```java
       byte[] b = new byte[1024];
       int len = 0;
       while((len=is.read(b))> 0){
       	out.write(b, 0, len);
       }
       is.close();
       
       ```

### 2.2 步骤演示

- 一下步骤都是在同一个Servlet的doGet()方法中编写的

- 我所下载的文件是放在WEB-INF下mp3文件夹中的文件

- 具体步骤

  1. 获取文件的流：

     ```java
     String realPath = getServletContext().getRealPath("/WEB-INF/mp3/中国话.mp3");
     //获取文件的File对象
     File file = new File(realPath);
     //获取文件的输入流
     FileInputStream is = new FileInputStream(file);
     
     ```

  2. 获取头信息：

     ```java
     //获取文件的MIME信息
     String contentType = getServletContext().getMimeType(realPath);
     //设置下载文件的名字
     String filename = "zhongguohua.mp3";
     //创建Content-Disposition信息
     String disposition = "attachment; filename="+ filename ;
     //获取文件长度
     long size = file.length();
     
     ```

  3. 设置头信息

     ```java
     //设置Content-Type
     response.setContentType(contentType);
     //设置Content-Disposition
     response.setHeader("Content-Disposition", disposition);
     //设置文件长度
     response.setContentLength((int)size);
     
     ```

  4. 发送文件

     ```java
     //通过response获取输出流，用于向浏览器输出内容
     ServletOutputStream out = response.getOutputStream();
     //将文件输入流通过输出流输出
     byte[] b = new byte[1024];
     int len = 0;
     while((len=is.read(b))> 0){
     	out.write(b, 0, len);
     }
     //最后不要忘记关闭输入流，输出流由Tomcat自己处理，我们不用手动关闭
     is.close();
     
     ```

### 2.3 乱码

- 至此实际上文件下载的主要功能都已经完成。但是还有一个问题我们这里没有体现出来，因为目前我们的文件名使用的是纯英文的，没有乱码问题。这里如果我们要使用中文文件名的话，毫无疑问会出现乱码问题。

- 解决此问题的方法很简单，在获取文件名之后为文件名进行编码：

```java
filename = java.net.URLEncoder.encode(filename,"utf-8");
```

- 但是注意这里火狐浏览器比较特殊，因为他默认是以BASE64解码的，所以这块如果需要考虑火狐的问题的话还需要特殊处理一下。

  1. 先要获取客户端信息（通过获取请求头中的User-Agent信息）

  ```java
  //获取客户端信息
  String ua = request.getHeader("User-Agent");
  
  ```

  2. 然后判断浏览器版本，做不同的处理（通过判断头信息中是否包含Firefox字符串来判断浏览器版本）

  ```java
  //判断客户端是否为火狐
  if(ua.contains("Firefox")){
  	//若为火狐使用BASE64编码
  	filename = "=?utf-8?B?"+new BASE64Encoder()
  .encode(filename.getBytes("utf-8"))+"?=";
  }else{
  	//否则使用UTF-8
  	filename = URLEncoder.encode(filename,"utf-8");
  }
  
  ```

  3. 谁问跟谁急

  ```java
  String string = new String("你好.jpg".getBytes("gbk"), "iso8859-1");
  ```

















































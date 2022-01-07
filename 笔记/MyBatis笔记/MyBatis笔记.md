# MyBatis开发步骤

## 1、POM文件添加MyBatis的坐标

```xml
<!--导入MyBatis的坐标和其他相关坐标-->
<!--mybatis坐标-->
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.4.5</version>
</dependency>
<!--mysql驱动坐标-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.6</version>
    <scope>runtime</scope>
</dependency>
<!--单元测试坐标-->
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
<!--日志坐标-->
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.12</version>
</dependency>
```

## 2、创建数据表

## 3、编写对应实体类

## 4、编写映射文件实体类的Mapper.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="userMapper">
    <!-- resultType属性：返回值类型，如果是List，则写List内的类型 -->
    <select id="findAll" resultType="xxxx.xxxx.xxxx.User">
        select * from xxxx
    </select>
</mapper>
```

## 5、编写核心文件MyBatisConfig.xml

```xml
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN“ "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/><property name="url" value="jdbc:mysql:///test"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers> 
        <mapper resource="com/itheima/mapper/UserMapper.xml"/> 
    </mappers>
    <settings>
        <!-- 打印sql日志 -->
        <setting name="logImpl" value="STDOUT_LOGGING" />
    </settings>
</configuration>
```

## 6、编写测试类

```java
//加载核心配置文件
InputStream resourceAsStream = Resources.getResourceAsStream("Config.xml");
//获得sqlSession工厂对象
SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
//获得sqlSession对象
SqlSession sqlSession = sqlSessionFactory.openSession();
//执行sql语句
List<User> userList = sqlSession.selectList("userMapper.findAll");
//打印结果
System.out.println(userList);
//释放资源
sqlSession.close();
```

# MyBatis的映射文件概述

## 1、概述

映射文件指导着MyBatis如何进行数据库增删改查

```text
cache –命名空间的二级缓存配置
cache-ref – 其他命名空间缓存配置的引用。
resultMap – 自定义结果集映射
parameterMap – 已废弃！老式风格的参数映射
sql –抽取可重用语句块。
insert – 映射插入语句
update – 映射更新语句
delete – 映射删除语句
select – 映射查询语句
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!-- 映射文件DTD约束头，支持mybatis配置提示 -->
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 根标签 -->
<!-- namespace命名空间，与下面的查询操作标签的id，一起组成查询标识 -->
<mapper namespace="userMapper">
    <!-- 查询操作标签，可用的还有insert、update、delete -->
    <!-- id属性与上面的命名空间组成查询标识 -->
    <!-- resultType属性标识查询结果对应的实体类型 -->
    <select id="findAll" resultType="xxxx.xxxx.xxxx.User">
        <!-- 查询标签内写sql语句 -->
        select * from xxxx
    </select>
</mapper>
```

## 2、insert、update、delete元素属性

### 属性简介

| 属性                 | 说明                                                         |
| -------------------- | ------------------------------------------------------------ |
| **id**               | **命名空间中的唯一标识符**                                   |
| parameterType        | 将要传入的语句中的参数的完全限定类名或别名，这个属性是**可选**的，因为**MyBatis可以通过TypeHandler推断出具体的传入语句中的参数类型，默认值为unset** |
| flushCache           | 将其设置为true，任何时候只要语句被调用，都会导致本地缓存和二级缓存被清空，默认值为true（**对应插入、更新和删除语句**） |
| **timeout**          | 这个设置实在抛出异常之前，驱动程序**等待数据库返回请求结果的秒数**，默认值为unset（依赖驱动） |
| statementType        | STATEMENT，PREPARED或CALLABLE的一个。这会让MyBatis分别使用Statement，PreparedStatement或CallableStatement，**默认值PREPARED** |
| **userGenerateKeys** | （**仅对insert和update有用**）**这会令MyBatis使用JDBC的getGeneratedKeys()方法来取出由数据库内部生成的主键**，（比如Mysql的自动递增字段），默认值：false |
| **keyProperty**      | （**仅对insert和update有用**）唯一标记一个属性，**MyBatis会通过getGeneratedKeys()的返回值或者通过insert语句的selectKey子元素设置他的键值**，默认：unset |
| keyColumn            | （**仅对insert和update有用**）通过生成的键值设置表中的列名，这个设置仅在某些数据库（例如PostgreSQL）是必须的，当主键列不是表中的第一列的时候需要设置，如果希望得到多个生成列，也可以是逗号分隔的属性列表 |
| **databaseId**       | 如果配置了databaseIdProvider，MyBatis会加载所有的不带databaseId或匹配当前databaseId的语句，如果带或者不带的语句都有，则不带的会被忽略 |

### 主键生成方式

- 若数据库支持自动生成主键的字段（比如MySQL、SQL Server），则可以设置**userGenerateKeys=“true”**，然后再把用**keyProperty**设置到目标属性上

```xml
<insert id="insertCustomer" databaseId="mysql" userGenerateKeys="true" keyProperty="id">
    INSERT INTO customers2 (last_name, email, age) VALUES(#{lastName}, #{email}, #{age}) 
</insert>
```

- 而对于不支持自增型主键的数据库（例如：Oracle），则可以使用selectKey子元素：selectKey元素将会首先运行，id会被设置，然后插入语句会被调用。

```xml
<insert id="insertCustomer" databaseId="oracle" parameterTyper="customer">
    <selectKey order="BEFORE" keyProperty="id", resultType="_int">
        SELECT crm_seq.nextval
        FROM dual
    </selectKey>
	INSERT INTO customer2(id, last_name, email, age)
    VALUES(#{id}, #{lastName}, #{email}, #{age})
</insert>
```

## 3、selectKey元素

| 属性          | 解释                                                         |
| ------------- | ------------------------------------------------------------ |
| keyProperty   | seletKey语句结果应该被设置的目标属性，也就是把查出的主键封装给JavaBean |
| keyColumn     | 匹配属性的返回结果集中的列名称                               |
| resultType    | 查出结果的类型，MyBatis通常可以推算出来，但是为了更加确定写上也不会有问题，MyBatis允许任何简单类型用作主键的类型，包括字符串 |
| order         | 可以被设置为BEFORE或AFTER。如果设置为BEFORE，那么他会首先选择主键，设置keyProperty然后执行插入语句。如果设置为AFTER，那么先执行插入语句，然后是selectKey元素，也就是查询主键语句先于还是后于主要查询语句 |
| statementType | 与前面相同，MyBatis支持STATEMENT，PREPARED和CALLABLE语句的映射类型，分别代表PreParedStatement和CallableStatement类型 |

## 4、Parameters元素

### 参数的传递

- **单个参数**：可以接受**基本类型**，**对象类型**，**集合类型**的值。这种情况 MyBatis可直接使用这个参数，不需要经过任何处理。
- **多个参数**：任意多个参数，都会被MyBatis重新包装成一个**Map**传入。 Map的key是param1，param2，.......，或者索引0，1，..…，值就是参数的值。
- **命名参数**：为参数处使用**注解@Param**起一个名字，MyBatis就会将这些参数封装进map中，key就是我们自己指定的，也就可以#{}取出参数。**推荐**。
- **POJO**：当这些参数属于自己的POJO时，可以直接传递POJO，直接#{}可以直接取出
- **Map**：可以封装多个参数为map，直接传递，设置自定义的key，也可以直接#{}取出

如果一系列数据使用频率较高，但又不是一个POJO，可以自定义一个TO（Transfer Object），用作数据传输。

- **特别注意**：如果是Collection（List、Set）类型或者是数组，也会被特殊处理，也就是把传入的List或者数组存入Map中。
  - key则有三种：
    - 如果是Collection，则key为collection
    - 如果是List，则key为list
    - 如果是数组，则key为array

### 参数的处理

- 参数也可以指定一个特殊的数据类型：

```xml
#{property, javaType=int, jdbcType=NUMERIC}
#{Height, javaType=double, jdbcType=NUMERIC, numericScale=2}
```

1. **javaType**：通常可以从参数对象中来去确定
   - 如果null被当做值来传递，对于所有可能为空的列，jdbcType需要被设置。
   - 对于数值类型，还可以设置小数点后保留的位置
   - **mode**属性允许指定IN、OUT、INOUT参数。如果参数为OUT或INOUT，参数对象属性的真实值将会被改变，就像在获取输出参数时所期望的那样。
   
2. **参数位置支持的属性**：javaType、jdbcType、mode(存储过程)、numericScale、 resultMap、typeHandler、jdbcTypeName、~~expression~~(未来准备支持)

3. **实际上通常被设置的是**：可能为空的列名指定 jdbcType

   - jdbcType通常需要在某种特定的条件下被设置：

     - 在我们数据为null的时候，有些数据库可能不能识别mybatis对null的默认处理。比如Oracle（报错）

     - JdbcType OTHER：无效的类型；因为mybatis对所有的null都映射的是原生Jdbc的OTHER类型，Oracle不能正确处理

       - 两种解决办法：

         1. ```
            #{email,jdbcType=OTHER};
            ```

         2. ```
            jdbcTypeForNull=NULL
            全局配置
            <setting name="jdbcTypeForNull" value="NULL"/>
            ```

4. **\#{key}**：获取参数的值，预编译到SQL中。**安全**。

5. **${key}**：获取参数的值，拼接到SQL中。**有SQL注入问题**。ORDER BY ${name}。场景：对于原生SQL不支持占位符的地方，可以使用。

## 5、select元素

**作用**：Select元素来定义查询操作

**参数**：

- **Id**：唯一标识符。用来引用这条语句，需要和接口的方法名一致
- **parameterType**：参数类型。可以不传，MyBatis会根据TypeHandler自动推断
- **resultType**：返回值类型。别名或者全类名，如果返回的是集合，定义集合中元素的类型。不能和**resultMap**同时使用

**扩展**：

1. 返回一个Map封装

```java
//返回一条记录的map，key就是列名，值就是对应的值
public Map<String, Object> getEmpByIdReturnMap(Integer id);

<select id="getEmpByIdReturnMap" resultType="map">
    select * from tbl_employee where id=#{id}
</select>
```

```java
//多条记录封装一个map：Map<Integer,Employee>:键是这条记录的主键，值是记录封装后的javaBean
//@MapKey:告诉mybatis封装这个map的时候使用哪个属性作为map的key
@MapKey("lastName")
public Map<String, Employee> getEmpByLastNameLikeReturnMap(String lastName);

<select id="getEmpByLastNameLikeReturnMap" resultType="com.atguigu.mybatis.bean.Employee">
    select * from tbl_employee where last_name like #{lastName}
</select>
```



# MyBatis的增删改查操作

## 1、插入数据操作

### 1、编写Mapper映射文件

```xml
<mapper namespace="userMapper">
    <insert id="add" parameterType="com.itheima.domain.User">
        insert into user values(#{id},#{username},#{password})
    </insert>
</mapper>

```

### 2、编写插入实体的代码

```java
//读取配置文件、获取SqlSession
.........
//执行插入
int insert = sqlSession.insert("userMapper.add", user);
//提交事务
sqlSession.commit();
sqlSession.close();
```

###  3、插入操作注意问题

- 插入语句使用**insert**标签 
- 在映射文件中使用**parameterType**属性指定要插入的数据类型
- Sql语句中使用**#{实体属性名}**方式**引用实体中的属性值**
- 插入操作使用的API是**sqlSession.insert(“命名空间.id”,实体对象);** 
- 插入操作涉及数据库数据变化，所以要使用sqlSession对象显示的提交事务， 即**sqlSession.commit()；** 

## 2、修改数据操作

### 1、编写映射文件

```xml
<mapper namespace="userMapper">
    <update id="update" parameterType="com.itheima.domain.User">
        update user set username=#{username},password=#{password} where id=#{id}
    </update>
</mapper>
```

### 2、编写修改实体的代码

```java
//读取配置文件、获取SqlSession
.........
//执行修改
int update = sqlSession.update("userMapper.update", user);
//提交事务
sqlSession.commit();
sqlSession.close();
```

### 3、修改操作注意问题

- 修改语句使用**update**标签
- 修改操作使用的API是**sqlSession.update(“命名空间.id”,实体对象);**
- 修改操作涉及数据库数据变化，所以要使用sqlSession对象显示的提交事务， 即**sqlSession.commit()；**

## 3、删除数据操作

### 1、编写Mapper映射文件

```xml
<mapper namespace="userMapper">
    <delete id="delete" parameterType="java.lang.Integer">
        delete from user where id=#{id}
    </delete>
</mapper>
```

### 2、编写删除数据的代码

```java
//读取配置文件、获取SqlSession
.........
//执行删除
int delete = sqlSession.delete("userMapper.delete",3);
//提交事务
sqlSession.commit();
sqlSession.close();
```

### 3、删除操作注意问题

- 删除语句使用**delete**标签
- Sql语句中使用**#{任意字符串}**方式引用**传递的单个参数**
- 删除操作使用的API是**sqlSession.delete(“命名空间.id”,Object);**

#  MyBatis核心配置文件概述

## 1、MyBatis核心配置文件层级关系

- **configuration**：配置
  - **properties**：属性
  - **settings**：设置
  - **typeAliases**：类型别名
  - **typeHandlers**：类型处理器
  - **objectFactory**：对象工厂
  - **plugins**：插件
  - **environments**：环境
    - **environment**：环境变量
    - **transactionManager**：事务管理器
    - **dataSource**：数据源
  - **databaseIdProvider**：数据库厂商标识
  - **mappers**：映射器

## 2、MyBatis常用配置解析

标签具有一定顺序，不能打乱

### 1、**environments**标签

其内可以写多个environment标签，每个environment标签代表了一个具体环境信息

1、数据库环境的配置，支持多环境配

2、MyBatis可以配置多种环境，比如开发、测试和生 产环境需要有不同的配置。

3、每种环境使用一个environment标签进行配置并指定唯一标识符

4、可以通过environments标签中的**default属性**指定一个环境的标识符来**快速的切换环境**

- **事务管理器**（transactionManager）**类型**(type)有三种：
  1. **JDBC**：这个配置就是直接使用了JDBC 的提交和回滚设置，它依赖于从数据源得到的连接来管理事务作用域。
  2. **MANAGED**：这个配置几乎没做什么。它从来不提交或回滚一个连接，而是让**容器**来管理事务的整个生命周期。 默认情况下它会关闭连接，然而一些容器并不希望这样，因此需要将 **closeConnection** 属性设置 为 false 来阻止它默认的关闭行为。
  3. **自定义**：实现TransactionFactory接口，type=全类名/ 别名
- **数据源**（dataSource）类型（type）有四种：
  1. **UNPOOLED**：这个数据源的实现只是每次被请求时打开和关闭连接。
  2. **POOLED**：这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来。
  3. **JNDI**：这个数据源的实现是为了能在如 EJB 或应用服务器这类容器中使用，容器可以集中或在外部配置数据源，然后放置 一个 JNDI 上下文的引用。
  4. **自定义**：实现DataSourceFactory接口，定义数据源的获取方式。
- 实际开发中我们使用**Spring管理数据源**，并进行事务控制的配置来覆盖上述配置

```xml
<!-- 配置外部的jdbc文件，在此文件中引用 -->
<properties resource="xxxx.properties"/>
<!-- default属性指定默认环境的名称 -->
<environments default="development">
    <!-- environment指定具体环境，id属性指定当前环境名称 -->
    <environment id="development">
        <!-- type属性指定事务管理类型 -->
        <transactionManager type="JDBC"/>
        <!-- type属性指定当前数据源类型是连接池 -->
        <dataSource type="POOLED">
            <!-- 数据源配置的基本参数 -->
            <property name="driver" value="${jdbc.driver}"/>
            <property name="url" value="${jdbc.url}"/>
            <property name="username" value="${jdbc.username}"/>
            <property name="password" value="${jdbc.password}"/>
        </dataSource>
    </environment>
</environments>
```

### 2、mappers标签

作用：该标签的作用是加载映射的，加载方式有如下几种，在其内使用mapper标签加载

1. 使用相对于类路径的资源引用，一般用于引用类路径下的资源

   - ```xml
     <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     ```

2. 使用完全限定资源定位符（URL），一般用于引用网络以及磁盘上的资源

   - ```xml
     <mapper url="file:///var/mappers/AuthorMapper.xml"/>
     ```

3. 使用映射器接口实现类的完全限定类名，引用接口

   - ​	**注意**：接口与对应的mapper.xml文件必须放在**同一个目录**下且**同名**，否则会报错
     - 或者不使用sql映射文件，使用注解
     - **推荐**：重要sql语句使用sql映射文件，反之为了开发快速，使用注解

   ```xml
   <mapper class="org.mybatis.builder.AuthorMapper"/>
   ```

4. 将包内的映射器接口实现全部注册为映射器，批量映射

   1. 标志一个包的权限路径，该包标识的是mapper的接口文件
      - **注意**：对应的映射文件与接口必须同名同包，而注解可以自动识别

   - ```xml
     <package name="org.mybatis.builder"/>
     ```

扩展：最终上面的方式都会被解析到mybatis的configuration类中，供用户使用。

### 3、Properties标签

作用：实际开发中，习惯将数据源的配置信息单独抽取成一个properties文件，该标签可以加载额外配置的properties文件。

如果属性在不只一个地方进行了配置，那么 MyBatis 将按 照下面的顺序来加载：

- 在 properties **元素体内**指定的属性首先被读取。
- 然后根据 properties 元素中的 resource 属性读取类路径下属性文件或根据 url 属性指定的路径读取属性文件，并**覆盖**已读取的同名属性。
  - resource属性一般引入类路径下的文件，url一般引入网络或磁盘上的文件，二者不能同时出现在properties里面
- 最后读取作为方法参数传递的属性，并覆盖已读取的同名属性。

```xml
<!-- 配置外部的jdbc文件，在此文件中引用 -->
<properties resource="xxxx.properties"/>

<dataSource type="POOLED">
    <!-- 数据源配置的基本参数 -->
    <property name="driver" value="${jdbc.driver}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</dataSource>
```

### 4、typeAliases标签

作用：类型别名是为Java 类型设置一个短的名字。在其内使用**typeAlias**标签为单个类型起别名或者**package**标签为包内类型批量起别名

属性**alias**：设置新的别名，如果不设置默认为类名第一个字母小写。

```xml
								<!-- 使用了全类名 -->
<select id="findAll" resultType="com.itheima.domain.User">
select * from User
</select>
```

```xml
<!-- 配置别名 -->
<typeAliases>
    <typeAlias type="com.itheima.domain.User“ alias="user"/>
	<!-- 或者 -->
    <package name="com.xxx.xxxx.pojo" />
</typeAliases>
                                                          
```

```xml
					<!-- 使用了别名 -->
<select id="findAll" resultType=“user">
    select * from User
</select>
```

**注意**：使用package标签容易造成别名冲突，可以使用@Alias注解解决。

也可以只用注解@Alias指定别名。

```java
@Alias("user")
public class User{.....}
```

**建议**：最后开发最好使用全类名。

### 5、settings标签

这是 MyBatis 中**极为重要**的调整设置，它们会改变 MyBatis 的运行时行为。

| 设置参数                 | 描述                                                         | 有效值               | 默认值  |
| ------------------------ | ------------------------------------------------------------ | -------------------- | ------- |
| cacheEnabled             | 该配置影响的所有映射器中配置的**缓存**的全局开关             | true/false           | ture    |
| lazyLoadinfEnabled       | 延迟加载的全局开关。**当开启时，所有的关联对象都会延迟加载**。特定关联关系中可通过设置fetchType属性来覆盖该项的开关状态。 | true/false           | true    |
| useColumnLabel           | **使用列标签代替列名**，不同的驱动在这方面会有不同的表现。   | ture/false           | ture    |
| defaultStatementTimeout  | **设置超时时间**，他决定驱动等待数据库响应的秒数             | Any positive integer | Not Set |
| mapUnderscoreToCamelCase | **是否自动开启驼峰命名规则映射**，即从数据库列名A_B到Java属性名aB | true/false           | false   |

```xml
<settings>
	<setting name="mapUnderscoreToCamelCase" value="true" />
</settings>
```

### 6、databaseIdProvider标签

**作用**：指示数据库环境

配置后支持多数据库厂商。

MyBatis 可以根据不同的数据库厂商执行不同的语句。

```xml
<databaseIdProvider type="DB_VENDOR">
    <!-- 为不同的数据库厂商起别名 -->
    <property name="MySQL" value="mysql" />
    <property name="Oracle" value="oracle" />
    <property name="SQL Server" value="sqlserver" />
</databaseIdProvider>
```

**属性**：

- **Type**： DB_VENDOR
  - 使用MyBatis提供的**VendorDatabaseIdProvider**解析数据库厂商标识。也可以自定义实现**DatabaseIdProvider**接口来自定义。
- Property.**name**：数据库厂商标识
- Property.**value**：为标识起一个别名，方便SQL语句使用databaseId属性引用

```xml
<select id="getSome" resultType="some" parameterType="Integer" databaseId="mysql">
    SELECT * FROM SOME WHERE id=#{id}
</select>
```

- **DB_VENDOR**：会通过 **DatabaseMetaData#getDatabaseProductName()** 返回的字符串进行设置。由于通常情况下这个字符串都非常长而且相同产品的不同版本会返回不同的值，所以最好通过设置属性别名来使其变短。

**MyBatis匹配规则如下**：

1. 如果没有配置databaseIdProvider标签，那么**databaseId=null**。
2. 如果配置了databaseIdProvider标签，使用标签配置的name去匹配数据库信息，**匹配上设置databaseId=配置指定的值，否则依旧为null**。
3. 如果databaseId不为null，他只会找到配置databaseId的sql语句
4. MyBatis 会加载不带 databaseId 属性和带有匹配当前数据库 databaseId 属性的所有语句。如果同时找到带有 databaseId 和不带 databaseId 的相同语句，则**后者会被舍弃**。也就是匹配更精确的会被加载

# MyBatis相应API

## 1、SqlSessionFactoryBuilder

SqlSession工厂构建器

常用API：SqlSessionFactory build(InputStream inputStream)

通过加载mybatis的核心文件的输入流的形式构建一个SqlSessionFactory对象

```java
String resource = "org/mybatis/builder/mybatis-config.xml";
InputStream inputStream = Resources.getResourceAsStream(resource);
SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
SqlSessionFactory factory = builder.build(inputStream);
```

其中， **Resources** 工具类，这个类在 **org.apache.ibatis.io** 包中。Resources 类帮助你从类路径下、文件系统或 一个 web URL 中加载资源文件。

## 2、SqlSessionFactory

SqlSessionFactory 有多个个方法创建 SqlSession 实例。常用的有如下两个：

| 方法                            | 解释                                                         |
| ------------------------------- | ------------------------------------------------------------ |
| openSession()                   | 会默认开启一个事务，但事务不会自动提交，也就意味着需要手动提 交该事务，更新操作数据才会持久化到数据库中 |
| openSession(boolean autoCommit) | 参数为是否自动提交，如果设置为true，那么不需要手动提交事务   |

## 3、SqlSession

会话对象，和connection一样**不是线程安全**的，因此**不能被共享**，每次使用完成**必须正确的关闭**，关闭操纵是必须的

SqlSession 实例在 MyBatis 中是非常强大的一个类。在这里你会看到所有执行语句、提交或回滚事务和获取映射器实例的方法。 

1、执行语句的方法主要有：

```java
<T> T selectOne(String statement, Object parameter) 
<E> List<E> selectList(String statement, Object parameter) 
int insert(String statement, Object parameter) 
int update(String statement, Object parameter) 
int delete(String statement, Object parameter)
```

2、操作事务的方法主要有：

```java
void commit()
void rollback()
```

# Mybatis的Dao层实现

## 1、代理开发方式

### 1、介绍

Mapper 接口开发方法只需要程序员编写Mapper **接口**（相当于Dao 接口），由Mybatis 框架根据接口定义创建接口的**动态代理对象**，代理对象的方法体同上边Dao接口实现类方法。

Mapper 接口开发需要遵循以下规范：

1、 Mapper.xml文件中的namespace与mapper接口的全限定名相同 

2、 Mapper接口方法名和Mapper.xml中定义的每个statement的id相同 

3、 Mapper接口方法的输入参数类型和mapper.xml中定义的每个sql的parameterType的类型相同 

4、 Mapper接口方法的输出参数类型和mapper.xml中定义的每个sql的resultType的类型相同

### 2、开发方式

```xml
<!-- 命名空间的全类名要是接口的全限定名 -->
<mapper namespace="com.itheima.mapper.UserMapper">
    <!-- 操作标签的id必须要与方法名相同 -->
    <!-- 操作标签的resultType必须要与方法返回值相同 -->
 	<!-- 操作标签的parameterType必须要与方法参数类型相同 --> 
    <select id="findById" parameterType="int" resultType="user">
        select * from User where id=#{id}
    </select>
</mapper>
```

```java
public interface UserMapper {
    User findById(int id);
}
```

### 3、测试

```java
@Test
public void testProxyDao() throws IOException {
    InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
    SqlSession sqlSession = sqlSessionFactory.openSession();
    //获得MyBatis框架生成的UserMapper接口的实现类
    UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
    User user = userMapper.findById(1);
    System.out.println(user);
    sqlSession.close();
}
```

**扩展**：MyBatis允许直接定义Intger、Long、Boolean、void及其子类的返回值，无需在标签处设置返回值（也没有位置设置），MyBatis会自动封装。

# MyBatis映射文件深入

MyBatis映射文件配置：    
<select>：查询    
    <insert>：插入    
        <update>：修改    
            <delete>：删除    
                <where>：where条件，内嵌条件判断    
                    <if>：if判断    
                        <foreach>：循环   
                            <sql>：sql片段抽取

## 1、动态sql语句

### 标签if

我们根据实体类的不同取值，使用不同的 SQL语句来进行查询。

通常用于where标签中

test属性：表示判断表达式

```xml
<select id="findByCondition" parameterType="user" resultType="user">
    select * from User
    <where>
        <if test="id!=0">
            and id=#{id}
        </if>
        <if test="username!=null">
            and username=#{username}
        </if>
    </where>
</select>
```

**注意**：当if标签较多时，会容易出现冗余and，导致sql语法错误

- 解决方法：
  1. sql语句的where后加一个1=1
  2. 或者使用where标签

### 标签where

**作用**：如果该标签包含的标签中有返回值的话，它就为sql语句插入一个‘where’。此外，如果标签返回的内容是以 AND 或 OR 开头的，则它会剔除掉。

**注意**：无法剔除末尾的and或者or

### 标签foreach

循环执行sql的拼接操作

动态 SQL 的另外一个常用的必要操作是需要对一个集合进行遍历，**通常是在构建 IN 条件语句的时候**。

- 当迭代**列表**、**集合**等可迭代对象或者**数组**时，**index**是当前迭代的次数，**item**的值是本次迭代获取的元素
- 当使用**字典**（或者Map.Entry对象的集合）时，**index**是键，**item**是值

例如：SELECT * FROM USER WHERE id IN (1,2,5)。

```xml
<select id="findByIds" parameterType="list" resultType="user">
    select * from User
    <where>
        <foreach collection="array" open="id in(" close=")" item="id" separator=",">
            #{id}
        </foreach>
    </where>
</select>
```

**属性**：

1. **collection**：代表要遍历的集合元素，注意编写时不要写#{}
2. **open**：代表语句的开始部分
3. **close**：代表结束部分
4. **item**：代表遍历集合的每个元素，生成的变量名
5. **sperator**：代表分隔符

**批量保存**：

MySQL

```xml
<!-- 批量保存 -->
<!--public void addEmps(@Param("emps")List<Employee> emps);  -->
<!--MySQL下批量保存：可以foreach遍历，mysql支持values(),(),()语法-->
<insert id="addEmps">
    insert into tbl_employee(
    <include refid="insertColumn"></include>
    ) 
    values
    <foreach collection="emps" item="emp" separator=",">
        (#{emp.lastName},#{emp.email},#{emp.gender},#{emp.dept.id})
    </foreach>
</insert>
<!-- 
	 <insert id="addEmps">
	 	<foreach collection="emps" item="emp" separator=";">
	 		insert into tbl_employee(last_name,email,gender,d_id)
	 		values(#{emp.lastName},#{emp.email},#{emp.gender},#{emp.dept.id})
	 	</foreach>
	 </insert> 
-->
```

**注意**：

- 这种方式需要数据库连接属性**allowMultiQueries=true**。
- 这种分号分隔多个sql可以用于其他的批量操作（删除，修改）

Oracle

**注意**：

- Oracle不支持values(),(),()

-  Oracle支持的批量方式：

  1. 多个insert放在begin - end里面

     ```xml
     begin
            insert into employees(employee_id,last_name,email) 
            values(employees_seq.nextval,'test_001','test_001@atguigu.com');
            insert into employees(employee_id,last_name,email) 
            values(employees_seq.nextval,'test_002','test_002@atguigu.com');
     end;
     ```

  2. 利用中间表

     ```xml
     insert into employees(employee_id,last_name,email)
            select employees_seq.nextval,lastName,email from(
                   select 'test_a_01' lastName,'test_a_e01' email from dual
                   union
                   select 'test_a_02' lastName,'test_a_e02' email from dual
                   union
                   select 'test_a_03' lastName,'test_a_e03' email from dual
            )  
     ```

     

### 标签choose

按顺序判断 when 中的条件出否成立，如果有一个成立，则 choose 结束。当 choose 中所有 when的条件都不满则时，则执行 otherwise 中的 sql。类似于 Java 的 switch 语句，choose 为 switch，when 为 case，otherwise 则为 default。

```xml
<select id="getStudentListChoose" parameterType="Student" resultMap="BaseResultMap">
    SELECT * from STUDENT WHERE 1=1
    <where>
        <choose>
            <when test="Name!=null and student!='' ">
                AND name LIKE CONCAT(CONCAT('%', #{student}),'%')
            </when>
            <when test="hobby!= null and hobby!= '' ">
                AND hobby = #{hobby}
            </when>
            <otherwise>
                AND AGE = 15
            </otherwise>
        </choose>
    </where>
</select>
```

### 标签set

没有使用 if 标签时，如果有一个参数为 null，都会导致错误。

当在 update 语句中使用 if 标签时，如果最后的 if 没有执行，则或导致逗号多余错误。

使用 set 标签可以将动态的配置 set关键字，和剔除追加到条件末尾的任何不相关的逗号。

```xml
不使用set标签
<update id="updateStudent" parameterType="Object">
    UPDATE STUDENT
    SET NAME = #{name},
    MAJOR = #{major},
    HOBBY = #{hobby}
    WHERE ID = #{id};
</update>

<update id="updateStudent" parameterType="Object">
    UPDATE STUDENT SET
    <if test="name!=null and name!='' ">
        NAME = #{name},
    </if>
    <if test="hobby!=null and hobby!='' ">
        MAJOR = #{major},
    </if>
    <if test="hobby!=null and hobby!='' ">
        HOBBY = #{hobby}
    </if>
    WHERE ID = #{id};
</update>
```

```xml
使用set标签
<update id="updateStudent" parameterType="Object">
    UPDATE STUDENT
    <set>
        <if test="name!=null and name!='' ">
            NAME = #{name},
        </if>
        <if test="hobby!=null and hobby!='' ">
            MAJOR = #{major},
        </if>
        <if test="hobby!=null and hobby!='' ">
            HOBBY = #{hobby}
        </if>
    </set>
    WHERE ID = #id};
</update>
```

### 标签trim

**作用**：trim标记是一个格式化的标记，主要用于拼接sql的条件语句（前缀或后缀的添加或忽略），可以完成set或者是where标记的功能。

trim属性主要有以下四个

- **prefix**：在trim标签内sql语句加上前缀
- **suffix**：在trim标签内sql语句加上后缀
- **prefixOverrides**：指定去除多余的前缀内容，如：prefixOverrides=“AND | OR”，去除trim标签内sql语句多余的前缀"and"或者"or"。
- **suffixOverrides**：指定去除多余的后缀内容。

```xml
<update id="updateByPrimaryKey" parameterType="Object">
	update student set 
	<trim  suffixOverrides=",">
		<if test="name != null">
		    NAME=#{name},
		</if>
		<if test="hobby != null">
		    HOBBY=#{hobby},
		</if>
	</trim> 
	where id=#{id}
</update>
如果那name和hobby都不为空的话，会省略最后一个“，”
update student set NAME='XX',HOBBY='XX' /*,*/ where id='XX'
```

会为片段添加 “WHERE” 前缀，并忽略第一个 “and” 。

### 标签include

SQL片段抽取

Sql 中可将重复的 sql 提取出来，使用时用 **include** 引用即可，最终达到 sql 重用的目的

```xml
<!--抽取sql片段简化编写-->
<sql id="selectUser" select * from User</sql>
<select id="findById" parameterType="int" resultType="user">    
    <include refid="selectUser"></include> 
    where id=#{id}
</select>
<select id="findByIds" parameterType="list" resultType="user">    
    <include refid="selectUser"></include>    
    <where>        
        <foreach collection="array" open="id in(" close=")" item="id" separator=",">  
            #{id}        
        </foreach>    
    </where>
</select>
```

自定义一些property，使得sql语句内部可以使用，取值方法**${}** 

### 标签bind

bind 元素可以从 OGNL 表达式中创建一个变量并 将其绑定到上下文。比如：

```xml
<select id="getEmpByLastName" resultType="xxx.xxx.xxx.Employ">	
    <bind name="myLastName" value="'%'+_LastName+'%'"/>    
    select * from employee where last_name like #{myLastName}
</select>
```

### Multi-db vendor support

内置函数

**\_parameter**：代表整个参数

- 单个参数：\_parameter就是这个参数
- 多个参数：参数会被封装为一个map，_parameter就是代表这个map

**_databaseId**：如果配置了databaseIdProvider标签。

- _databaseId就是代表当前数据库的别名oracle

若在 mybatis 配置文件中配置了 databaseIdProvider , 则可以使用 “**_databaseId**”变量，这样就可以根据不同的数据库 厂商构建特定的语句

```xml
<select id="getEmpByLastName" resultType="xxx.xxx.xxx.Employ">	
    <if test="_databaseId == 'mysql'">       
        select * from employee where last_name like #{myLastName}    
    </if>
</select>
```

## 2、自动映射

### 1、全局setting设置

- **autoMappingBehavior**默认是**PARTIAL**，开启自动映射的功能。唯一的要求是**列名和javaBean属性名一致**。
- 如果autoMappingBehavior设置为null则会取消自动映射
- 数据库字段命名规范，POJO属性符合驼峰命名法，如 A_COLUMN---》aColumn，我们可以开启自动驼峰命名规则映射功能，mapUnderscoreToCamelCase=true。

### 2、实现高级结果集映射

通过自定义resultMap元素，实现高级结果集自定义映射规则

如果有的列没有指定，将会自动封装，不过建议使用了resultMap就全部映射一遍便于检查。

#### resultMap元素属性

- **type**：自定义规则的Java类型

- **constructor**：类在实例化时, 用来注入结果到构造方法中 
  - **idArg**：ID 参数，标记结果作为 ID 可以帮助提高整体效能
  - **arg**：注入到构造方法的一个普通结果
- **id**：一个 ID 结果，标记结果作为 ID 可以帮助提高整体效能，方便引用
- **result**：注入到字段或 JavaBean 属性的普通结果
- **association**：一个复杂的类型关联，许多结果将包成这种类型
  - 嵌入结果映射：结果映射自身的关联,或者参考一个
- **collection**：复杂类型的集
  - 嵌入结果映射：结果映射自身的集,或者参考一个
- **discriminator**：使用结果值来决定使用哪个结果映射
  - **case**：基于某些值的结果映射
  - 嵌入结果映射：这种情形结果也映射它本身,因此可以包含很多相同的元素，或者它可以参照一个外部的结果映射。

#### 子元素id & result的属性

id和result是resultMap内的子元素。

id 和 result 映射一个单独列的值到**简单数据类型** (字符串，整型，双精度浮点数，日期等)的属性或字段。

id定义主键会有底层优化

result定义普通封装规则

| 属性        | 解释                                                         |
| ----------- | ------------------------------------------------------------ |
| property    | 映射到列结果的字段或属性。也就是指定对应的JavaBean属性<br>例如："username"或“address.street.number” |
| column      | 数据表的列名。通常和resultSet.getString（columnName）的返回值一致，也就是指定哪一列 |
| javaType    | 一个Java类的完全限定名，或一个类型的别名。如果映射到一个JavaBean，MyBatis通常可以断定类型 |
| jdbcType    | JDBC类型是仅仅需要对插入、更新和删除操作可能为空的列进行处理。 |
| typeHandler | 类型处理器。使用这个属性，可以覆盖默认的类型处理器。这个属性值是类的完全限定名或者是一个类型处理器的实现，或者是类型别名 |

#### 子元素association的属性

指定联合的JavaBean对象

- 复杂对象映射

- POJO中的属性可能会是一个对象

- 我们可以使用联合查询，并以级联属性的方式封
  装对象。

- **属性**：
  
  - property：指定哪个属性是需要联合的对象，例如员工.部门
  - javaType：指定这个属性对象的类型
  - select：调用目标的方法查询当前属性的值 
  - column：将指定列的值传入目标方法与select搭配使用

  ```xml
  <resultMap type="xxx.xxx.lock" id="mylock">	
      <id column="id" property="id"/>   
      <result column="lockName" property="lockName"/>   	  
      <result column="key_id" property="key.id"/>   	 	 
      <result column="keyName" property="key.keyName"/>
  </resultMap>
  ```
  
  使用association标签定义对象的封装规则
  
  - **association-嵌套结果集**
  
  ```xml
  <resultMap type="xxx.xxx.lock" id="mylock">	
      <id column="id" property="id"/>    
      <result column="lockName" property="lockName"/>    
      <asspcoation property="key" javaType="xxx.xxx.key">        
      	<id column="key_id" property="id"/>        
      	<result column="keyName" property="keyName"/>    
      </asspcoation>
  </resultMap>
  ```
  
  - **association-分段查询**
  
  ```xml
  <resultMap type="xxx.xxx.lock" id="mylock">
      <id column="id" property="id"/>    
      <result column="lockName" property="lockName"/>    
      <!--                                                       指定查询方法           -->
      <asspcoation property="key" javaType="xxx.xxx.key" select="xxx.xxx.dao.mapper.getKwyById" column="key_id"/>
  </resultMap>
  ```
  
  - **association-分段查询&延迟加载**
  
  开启延迟加载和属性按需加载
  
  在查询的时候需要才查
  
  ```xml
  <settings>	
      <setting name="LazyLoadingEnabled" value="true"/>    
      <setting name="aggressiveLazyLoading" value="false"/>
  </settings>
  ```

#### 子元素Collection

定义关联集合类型的属性的封装规则

**属性**：

- **property**：集合的名字
- **ofType**：指定集合里面的元素类型

- Collection-集合类型&嵌套结果集

![image-20210915204058183](C:\Users\zzp84\Desktop\MyBatis笔记\images\MyBatis笔记.assets\image-20210915204058183.png)

- Collection-分步查询&延迟加载

![image-20210915204127759](C:\Users\zzp84\Desktop\MyBatis笔记\images\MyBatis笔记.assets\image-20210915204127759.png)

扩展-多列值封装map传递

- 分步查询的时候通过column指定，将对应的列的数据 传递过去，我们有时需要传递多列数据。
- 使用**{key1=column1,key2=column2…}**的形式

![image-20210915204411112](C:\Users\zzp84\Desktop\MyBatis笔记\images\MyBatis笔记.assets\image-20210915204411112.png)

association或者collection标签的 **fetchType=eager/lazy**可以**覆盖全局的延迟加载策略**， 指定立即加载（eager）或者延迟加载（lazy）

# MyBatis核心配置文件深入

## 1、typeHandlers标签

**简介**：无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，还是从结果集中取出一个值时， 都会用**类型处理器将获取的值以合适的方式转换成 Java 类型**。

**自定义**：可以重写类型处理器或创建你自己的类型处理器来处理不支持的或非标准的类型。具体做法为：**实现 org.apache.ibatis.type.TypeHandler 接口**， 或**继承 org.apache.ibatis.type.BaseTypeHandler**， 然 后可以选择性地将它**映射到一个JDBC类型**。

**开发步骤**：

- 定义转换类继承类**BaseTypeHandler**
- 覆盖4个未实现的方法，其中**setNonNullParameter**为java程序设置**数据到数据库**的回调方法，**getNullableResult**为查询时**mysql的字符串类型转换成 java的Type类型**的方法
- 在MyBatis核心配置文件中进行注册
- 测试转换是否正确

```java
//继承类，实现方法
public class MyDateTypeHandler extends BaseTypeHandler<Date> {    
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType type) {
        preparedStatement.setString(i,date.getTime()+"");    
    }    
    public Date getNullableResult(ResultSet resultSet, String s) throws SQLException {        
        return new Date(resultSet.getLong(s));    
    }    
    public Date getNullableResult(ResultSet resultSet, int i) throws SQLException {        
        return new Date(resultSet.getLong(i));    
    }    
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {        
        return callableStatement.getDate(i);    
    }
}
```

```xml
<!--注册类型自定义转换器-->
<typeHandlers>    
    <typeHandler handler="全限定名"></typeHandler>
</typeHandlers>
```

## 2、plugins标签

**简介**：MyBatis可以使用第三方的插件来对功能进行扩展，分页助手PageHelper是将分页的复杂操作进行封装，使用简单的方式即 可获得分页的相关数据，**插件通过动态代理机制**，可以介入四大对象的任何 一个方法的执行。

**开发步骤**：

- 导入通用PageHelper的坐标
- 在mybatis核心配置文件中配置PageHelper插件
- 测试分页数据获取

```xml
<!-- 导入分页助手依赖 -->
<dependency>    
    <groupId>com.github.pagehelper</groupId>    
    <artifactId>pagehelper</artifactId>    
    <version>3.7.5</version>
</dependency>
<dependency>    
    <groupId>com.github.jsqlparser</groupId>    
    <artifactId>jsqlparser</artifactId>    
    <version>0.9.1</version>
</dependency>
```

```xml
<!-- 注意：分页助手的插件 配置在通用mapper之前 -->
<plugin interceptor="com.github.pagehelper.PageHelper">    
    <!-- 指定方言 -->    
    <property name="dialect" value="mysql"/>
</plugin>
```

```java
//简单分页的实现
public void testPageHelper(){    
    //设置分页参数    
    PageHelper.startPage(1,2);    
    List<User> select = userMapper2.select(null);    
    for(User user : select){        
        System.out.println(user);    
    }
}
//获得分页相关的其他参数
PageInfo<User> pageInfo = new PageInfo<User>(select);
System.out.println("总条数："+pageInfo.getTotal());
System.out.println("总页数："+pageInfo.getPages());
System.out.println("当前页："+pageInfo.getPageNum());
System.out.println("每页显示长度："+pageInfo.getPageSize());
System.out.println("是否第一页："+pageInfo.isIsFirstPage());
System.out.println("是否最后一页："+pageInfo.isIsLastPage());
```

```text
MyBatis核心配置文件常用标签：
1、properties标签：该标签可以加载外部的properties文件
2、typeAliases标签：设置类型别名
3、environments标签：数据源环境配置标签
4、typeHandlers标签：配置自定义类型处理器
5、plugins标签：配置MyBatis的插件
```

# Mybatis多表查询

## 1、一对一查询

一对一查询的需求：查询一个订单，与此同时查询出该订单所属的用户

- 一对一查询的语句：

  ```sql
  select * from orders o,user u where o.uid=u.id;
  ```

**步骤**：

1. 创建对应实体

   - ```java
     public class Order {    ......    //代表当前订单从属于哪一个客户    private User user;}public class User {    .......}
     ```

2. 创建Mapper接口

   - ```java
     public interface OrderMapper {	List<Order> findAll();}
     ```

3. 配置Mapper.xml

   - ```xml
     <mapper namespace="com.itheima.mapper.OrderMapper">    <!-- 第一种resultMap -->    <resultMap id="orderMap" type="com.itheima.domain.Order">        <result column="uid" property="user.id"></result>        <result column="username" property="user.username"></result>        <result column="password" property="user.password"></result>        <result column="birthday" property="user.birthday"></result>    </resultMap>    <select id="findAll" resultMap="orderMap">        select * from orders o,user u where o.uid=u.id    </select></mapper>
     ```

   - ```xml
     <!-- 第二种resultMap --><resultMap id="orderMap" type="com.itheima.domain.Order">    <result property="id" column="id"></result>    <result property="ordertime" column="ordertime"></result>    <result property="total" column="total"></result>    <association property="user" javaType="com.itheima.domain.User">        <result column="uid" property="id"></result>        <result column="username" property="username"></result>        <result column="password" property="password"></result>        <result column="birthday" property="birthday"></result>    </association></resultMap>
     ```

## 2、一对多查询

一对多查询的需求：查询一个用户，与此同时查询出该用户具有的订单

- 一对多查询的语句：

```sql
select *,o.id oid from user u left join orders o on u.id=o.uid;
```

1. 编写对应实体

   - ```java
     public class Order {    
         ......    
         //代表当前订单从属于哪一个客户    
         private User user;
     }
     public class User {    
         ........    
             //代表当前用户具备哪些订单    
             private List<Order> orderList;
     }
     ```

2. 创建Mapper接口

   - ```java
     public interface UserMapper {    
         List<User> findAll();
     }
     ```

3. 配置Mapper.xml

   - ```xml
     <mapper namespace="com.itheima.mapper.UserMapper">    
         <resultMap id="userMap" type="com.itheima.domain.User">        
             <result column="id" property="id"></result>        
             <result column="username" property="username"></result>        
             <result column="password" property="password"></result>        
             <result column="birthday" property="birthday"></result>        
             <collection property="orderList" ofType="com.itheima.domain.Order">            
                 <result column="oid" property="id"></result>            
                 <result column="ordertime" property="ordertime"></result>            
                 <result column="total" property="total"></result>        
             </collection>    
         </resultMap>    
         <select id="findAll" resultMap="userMap">  select *,o.id oid from user u left join orders o on u.id=o.uid   </select>
     </mapper>
     ```

## 3、多对多查询

多对多查询的需求：查询用户同时查询出该用户的所有角色

- 多对多查询的语句：

```sql
select u.*,r.*,r.id rid from user u left join user_role ur on u.id=ur.user_idinner join role r on ur.role_id=r.id;
```

1. 创建实体：

   - ```java
     public class User {    ........    //代表当前用户具备哪些订单    private List<Order> orderList;    //代表当前用户具备哪些角色    private List<Role> roleList;}public class Role {    ........}
     ```

2. 添加Mapper接口方法

   - ```java
     List<User> findAllUserAndRole();
     ```

3. 配置Mapper.xml

   - ```xml
     <resultMap id="userRoleMap" type="com.itheima.domain.User">    <result column="id" property="id"></result>    <result column="username" property="username"></result>    <result column="password" property="password"></result>    <result column="birthday" property="birthday"></result>    <collection property="roleList" ofType="com.itheima.domain.Role">        <result column="rid" property="id"></result>        <result column="rolename" property="rolename"></result>    </collection></resultMap><select id="findAllUserAndRole" resultMap="userRoleMap">    select u.*,r.*,r.id rid from user u left join user_role ur on     u.id=ur.user_id    inner join role r on ur.role_id=r.id</select>
     ```

```text
MyBatis多表配置方式：一对一配置：使用<resultMap>做配置一对多配置：使用<resultMap>+<collection>做配置多对多配置：使用<resultMap>+<collection>做配置
```

# Mybatis的注解开发

```text
@Insert：实现新增@Update：实现更新@Delete：实现删除@Select：实现查询@Result：实现结果集封装@Results：可以与@Result 一起使用，封装多个结果集@One：实现一对一结果集封装@Many：实现一对多结果集封
```

## 1、修改核心配置文件

- 我们使用了注解替代的映射文件，所以我们只需要加载使用了注解的Mapper接口即可。

```xml
<mappers>    <!--扫描使用注解的类-->    <mapper class="com.itheima.mapper.UserMapper"></mapper></mappers>
```

- 或者指定扫描包含映射关系的接口所在的包也可以

```xml
<mappers>    <!--扫描使用注解的类所在的包-->    <package name="com.itheima.mapper"></package></mappers>
```

##  2、注解实现复杂映射开发

实现复杂关系映射之前我们可以在映射文件中通过配置**\<resultMap>**来实现，使用注解开发后，我们可以使用**@Results**注解 ，**@Result**注解，**@One**注解，**@Many**注解组合完成复杂关系的配置。

| 注解             | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| @Results         | 代替的是**\<resultMap>**标签，该注解中可以使用单个@Result注解，也可以使用@Result集合。<br>使用格式：<br>@Results（{@Result（），@Result（）}）或@Results（@Result（）） |
| @Resut           | 代替了**\<id>**标签和**\<result>**标签 <br>@Result中属性介绍： <br>column：数据库的列名 <br>property：需要装配的属性名 <br>one：需要使用的@One 注解（@Result（one=@One）（））） <br>many：需要使用的@Many 注解（@Result（many=@many）（））） |
| @One （一对一）  | 代替了**\<assocation>** 标签，是多表查询的关键，在注解中用来指定子查询返回单一对象。<br>@One注解属性介绍：<br> select: 指定用来多表查询的 sqlmapper<br>使用格式：<br>@Result(column=" ",property="",one=@One(select="")) |
| @Many （多对一） | 代替了**\<collection>**标签, 是是多表查询的关键，在注解中用来指定子查询返回对象集合。 <br>使用格式：<br>@Result(property="",column="",many=@Many(select="")) |

## 3、复杂查询

#### 1、一对一查询

使用注解配置Mapper

```java
public interface OrderMapper {    
    @Select("select * from orders")    
    @Results({        
        @Result(id=true, property = "id", column = "id"),        
        @Result(property = "ordertime", column = "ordertime"),        
        @Result(property = "total", column = "total"),        
        @Result(property = "user", column = "uid", javaType = User.class,                
        //指定多表查询的mapper                
        one = @One(select = "com.itheima.mapper.UserMapper.findById"))    })    
    List<Order> findAll();
}
public interface UserMapper {    
    @Select("select * from user where id=#{id}")    
    User findById(int id);
}
```

#### 2、一对多查询

```java
public interface UserMapper {    @Select("select * from user")    @Results({        @Result(id = true, property = "id", column = "id"),        @Result(property = "username", column = "username"),        @Result(property = "password", column = "password"),        @Result(property = "birthday", column = "birthday"),        @Result(property = "orderList", column = "id", javaType = List.class,                many = @Many(select = "com.itheima.mapper.OrderMapper.findByUid"))    })    List<User> findAllUserAndOrder();}public interface OrderMapper {    @Select("select * from orders where uid=#{uid}")    List<Order> findByUid(int uid);}
```

#### 3、多对多查询

```java
public interface UserMapper {    @Select("select * from user")    @Results({        @Result(id = true, property = "id", column = "id"),        @Result(property = "username", column = "username"),        @Result(property = "password", column = "password"),        @Result(property = "birthday", column = "birthday"),        @Result(property = "roleList", column = "id", javaType = List.class,                many = @Many(select = "com.itheima.mapper.RoleMapper.findByUid"))    })    List<User> findAllUserAndRole();}public interface RoleMapper {    @Select("select * from role r,user_role ur where r.id=ur.role_id and ur.user_id=#{uid}")    List<Role> findByUid(int uid);}
```

# MyBatis缓存机制

## 简介

MyBatis系统中默认定义了两级缓存。

**一级缓存和二级缓存**：

- 默认情况下，只有一级缓存（SqlSession级别的缓存， 也称为本地缓存）开启。
- 二级缓存需要手动开启和配置，他是基于namespace级别的缓存。
- 为了提高扩展性。MyBatis定义了缓存接口Cache。我们
  可以通过实现Cache接口来自定义二级缓存。

## 一级缓存

### 简介

- 一级缓存(local cache), 即本地缓存, 作用域默认为sqlSession。当 Session flush 或 close 后, 该 Session 中的所有 Cache 将被清空。

- **本地缓存不能被关闭**, 但可以调用 **clearCache()**  来清空本地缓存, 或者改变缓存的作用域
- 在mybatis3.1之后, 可以配置本地缓存的作用域.  在 mybatis.xml 中配置。
  - 属性：**localCacheScope**
  - 介绍：MyBatis利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。默认值为SESSINO，这种情况下会缓存一个会话中执行的所有查询，若设置值为STATEMENT，本地会话仅用在语句执行上，对相同SqlSession的不同调用将不会共享数据。
  - 可选值：SESSINO|STATEMENT
  - 默认值：SESSINO

### 演示与失效情况

- 同一次会话期间只要查询过的数据都会保存在当 前SqlSession的一个Map中。
  - key：hashCode+查询的SqlId+编写的sql查询语句+参数
- 一级缓存失效的四种情况：
  1. 不同的SqlSession对应不同的一级缓存
  2. 同一个SqlSession但是查询条件不同
  3. 同一个SqlSession两次查询期间执行了任何一次增 删改操作
  4. 同一个SqlSession两次查询期间手动清空了缓存

## 二级缓存

### 简介

- 二级缓存(second level cache)，全局作用域缓存
- 二级缓存默认不开启，需要手动配置
- MyBatis提供二级缓存的接口以及实现，缓存实现要求 POJO实现Serializable接口
- 二级缓存在 SqlSession 关闭或提交之后才会生效

### 使用步骤

1. 在配置文件中开启二级缓存

   - ```xml
     <setting name="cacheEnabled" value="true"/>
     ```

2. 需要使用二级缓存的映射文件处处使用cache配置缓存

   - ```xml
     <cache/>
     ```

3. 注意：POJO需要实现Serializable接口

## 缓存相关属性

- **eviction=“FIFO”**：缓存回收策略
  - LRU – 最近最少使用的：移除最长时间不被使用的对象。
  - FIFO – 先进先出：按对象进入缓存的顺序来移除它们。
  - SOFT – 软引用：移除基于垃圾回收器状态和软引用规则的对象。
  - WEAK – 弱引用：更积极地移除基于垃圾收集器状态和弱引用规则的对象。
  - 默认的是 LRU。
- **flushInterval**：刷新间隔，单位毫秒
  - 默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句时刷新
- **size**：引用数目，正整数
  - 代表缓存最多可以存储多少个对象，太大容易导致内存溢出
- **readOnly**：只读，true/false
  - true：只读缓存，会给所有调用者返回缓存对象的相同实例。因此这些对象不能被修改。这提供了很重要的性能优势。
  - false：读写缓存，会返回缓存对象的拷贝（通过序列化）。这会慢一些， 但是安全，因此默认是 false。

## 缓存有关设置

1. 全局setting的**cacheEnable**：
   - 配置二级缓存的开关。一级缓存一直是打开的。
2. select标签的**useCache**属性：
   - 配置这个select是否使用二级缓存。一级缓存一直是使用的
3. sql标签的**flushCache**属性：
   - 增删改默认flushCache=true。sql执行以后，会同时清空一级和二级缓存。 查询默认flushCache=false。
4. **sqlSession.clearCache()**：
   - 只是用来清除一级缓存。
5. 当在某一个作用域 (一级缓存 Session/二级缓存 Namespaces) 进行了 C/U/D 操作后，默认该作用域下所 有 select 中的缓存将被clear

## 第三方缓存整合

整合EhCche

步骤：

1. 导入ehcache-core，mybatis-ehcache，slf4j-api，slf4j-log4j

2. 编写ehcache.xml

3. 配置cache标签

   ```xml
   <cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
   ```

4. 参照缓存：若想在命名空间中共享相同的缓存配置和实例。可以使用cache-ref元素来引用另外一个缓存

   ```xml
   <cache-ref namespace="com.xxxx.xxxx.CustomerMapper"
   ```

# MyBatis-Spring整合

1. 下载整合适配包
2. 整合关键配置

```xml
<!-- 向IOC容器中注入SQLSessionFactory，以便于获取sqlSession -->
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <!-- 指定mybatis全局配置文件位置 -->    
    <property name="configLocation" value="classpath:mybatis/mybatis-config.xml" />   
    <!-- 指定数据源 -->    
    <property name="dataSource" ref="dataSource" />  
    <!-- mapperLocations：所有sql映射文件所在的位置 -->    
    <property name="mapperLocations" value="classpath:mybatis/mapper/*.xml" />   
    <!-- typeAliasesPackage：批量别名处理 -->    
    <property name="typeAliasesPackage" value="com.atguigu.bean" />
</bean>

<!-- 第一种方法 -->
<mybatis-spring:scan base-package="xxxx.xxx.xxxx"/>

<!-- 第二种方法 -->
<!-- 自动的扫描所有的mapper的实现并加入到ioc容器中 -->
<bean id="configure" class="org.mybatis.spring.mapper.MapperScannerConfigurer">   
    <!-– basePackage:指定包下所有的mapper接口实现自动扫描并加入到ioc容器中 -->   
    <property name="basePackage" value="com.atguigu.dao" />
</bean>
```

# MyBati逆向工程

## **MyBatis Generator**

### 简介

- 可以快速的根据表生成对应的 **映射文件**，**接口**，以及**bean类**。支持基本的增删 改查，以及QBC风格的条件查询
- 但是表连接、 存储过程等这些复杂sql的定义需要我们手工编写

### 使用步骤

1、编写MBG的配置文件

1. **jdbcConnection**配置数据库连接信息
2. **javaModelGenerator**配置javaBean的生成策略
3. **sqlMapGenerator** 配置sql映射文件生成策略
4. **javaClientGenerator**配置Mapper接口的生成策略
5. **table** 配置要逆向解析的数据表 
   - **tableName**：表名 
   - **domainObjectName**：对应的javaBean名

2、运行代码生成器生成代码

**注意**：

- Context标签
  - targetRuntime=“MyBatis3“可以生成带条件的增删改查 
  - targetRuntime=“MyBatis3Simple“可以生成基本的增删改查
  - 如果再次生成，建议将之前生成的数据删除，避免xml向后追加内容出现的问题

### MBG配置文件

```xml
<generatorConfiguration>    
    <context id="DB2Tables" targetRuntime="MyBatis3">        
        //数据库连接信息配置        
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"                
                        connectionURL="jdbc:mysql://localhost:3306/bookstore0629"                        
                        userId="root" 
                        password="123456" />              
        //javaBean的生成策略        
        <javaModelGenerator targetPackage="com.atguigu.bean" targetProject=".\src">            
            <property name="enableSubPackages" value="true" />           
            <property name="trimStrings" value="true" />        
        </javaModelGenerator>        
        //映射文件的生成策略        
        <sqlMapGenerator targetPackage="mybatis.mapper" targetProject=".\conf">            
            <property name="enableSubPackages" value="true" />        
        </sqlMapGenerator>       
        //dao接口java文件的生成策略       
        <javaClientGenerator type="XMLMAPPER" 
                             targetPackage="com.atguigu.dao"                             
                             targetProject=".\src">            
            <property name="enableSubPackages" value="true" />       
        </javaClientGenerator>        
        //数据表与javaBean的映射        
        <table tableName="books" domainObjectName="Book" />    
    </context>
</generatorConfiguration>
```

### 生成器代码

```java
public static void main(String[] args) throws Exception {   
    List<String> warnings = new ArrayList<String>();    
    boolean overwrite = true;    
    File configFile = new File("mbg.xml");    
    ConfigurationParser cp = new ConfigurationParser(warnings);    
    Configuration config = cp.parseConfiguration(configFile);    
    DefaultShellCallback callback = new DefaultShellCallback(overwrite);    
    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings); 
    myBatisGenerator.generate(null);
}
```

### QBC风格的带条件查询

```java
@Test
public void test01(){
    SqlSession openSession = build.openSession();
    DeptMapper mapper = openSession.getMapper(DeptMapper.class);
    DeptExample example = new DeptExample();
    //所有的条件都在example中封装
    Criteria criteria = example.createCriteria();
    //select id, deptName, locAdd from tbl_dept WHERE 
    //( deptName like ? and id > ? ) 
    criteria.andDeptnameLike("%部%");
    criteria.andIdGreaterThan(2);
    List<Dept> list = mapper.selectByExample(example);
    for (Dept dept : list) {
        System.out.println(dept);
    }
}
```

# MyBatis插件开发

## 简介

- MyBatis在**四大对象的创建过程中，都会有插件进行介入**。插件可以利用**动态代理**机制一层层的包装目标对象，而实现在目标对象执行目标方法之前进行拦截的效果。

- MyBatis 允许在已映射语句执行过程中的某一点进行拦截调用。

- 默认情况下，MyBatis 允许使用插件来拦截的方法调用包括：

  - ```java
    Executor (update, query, flushStatements, commit, rollback, 
    getTransaction, close, isClosed) 
    ParameterHandler (getParameterObject, setParameters) 
    ResultSetHandler (handleResultSets, handleOutputParameters) 
    StatementHandler (prepare, parameterize, batch, update, query)
    ```

## 插件开发步骤

1、编写插件实现**Interceptoe**接口，并使用**@Intercepts**注解完成插件签名

```java
@Intercepts( {
    @Signature(type=StatementHandler.class, method="prepare", args={Connection.class})
} )
public class MyFirstPlugin implements Interceptor{
    .....
}
```

2、在全局配置文件中注册插件

```xml
<plugins>
	<plugin interceptor="xxxxx.xxxx.xxxxx">
    	<property name="username" value="tomcat" />
    </plugin>
</plugins>
```

## 插件原理

1. 按照插件注解声明，**按照插件配置顺序**调用插件plugin方法，生成被拦截对象的动态代理。
2. 多个插件依次生成目标对象的代理对象，层层包裹，先声明的先包裹，形成代理链
3. 目标方法执行时依次从外到内执行插件的**intercept**方法。
4. 多个插件情况下，我们往往需要在某个插件中分离出目标对象。可以借助MyBatis提供的**SystemMetaObject**类来进行获 取最后一层的**h**以及**target**属性的值

## Interceptor接口

- **Intercept**：拦截目标方法执行
- **plugin**：生成动态代理对象，可以使用MyBatis提 供的Plugin类的wrap方法
- **setProperties**：注入插件配置时设置的属性

## 常用代码

从代理链中分离真实被代理对象

```java
//1、分离代理对象。由于会形成多次代理，所以需要通过一个 while 循环分离出最终被代理对象，从而方便提取信息
MetaObject metaObject = SystemMetaObject.forObject(target);
while (metaObject.hasGetter("h")) {
Object h = metaObject.getValue("h");
metaObject = SystemMetaObject.forObject(h);
}
//2、获取到代理对象中包含的被代理的真实对象
Object obj = metaObject.getValue("target");
//3、获取被代理对象的MetaObject方便进行信息提取
MetaObject forObject = SystemMetaObject.forObject(obj);
```

# MyBatis使用场景

## 1、PageHelper插件

**使用步骤**：

1. 导入相关依赖pagehelper-xxxx.jar和jsqlparser.0.9.5.jar

2. 在MyBatis全局配置文件中配置分页插件

   - ```xml
     <plugins>
         <!-- com.github.pagehelper为PageHelper类所在包名 -->
     	<plugin interceptor="com.github.pagehelper.PageInterceptor">
             <!-- 使用下面的方式配置参数，后面会有所有的参数介绍 -->
             <property name="param1" value="value1"/>
         </plugin>
     </plugins>
     ```

3. 使用PageHelper提供的方法进行分页

4. 可以使用更强大的PageInfo封装返回结果

## 2、批量操作

1、默认的 openSession() 方法没有参数,它会创建有如下特性的：

- 会开启一个事务(也就是**不自动提交**)
- 连接对象会从由活动环境配置的数据源实例得到。
- 事务隔离级别将会使用驱动或数据源的默认设置。
- 预处理语句不会被复用，也不会批量处理更新。

2、openSession 方法的 **ExecutorType** 类型的参数，枚举类型：

- ExecutorType.SIMPLE: 这个执行器类型不做特殊的事情（这是默认装配 的）。它为每个语句的执行创建一个新的预处理语句。
- ExecutorType.REUSE: 这个执行器类型会复用预处理语句。
- ExecutorType.BATCH: 这个执行器会批量执行所有更新语句

3、批量操作我们是使用MyBatis提供的**BatchExecutor**进行的， 他的底层就是通过jdbc攒sql的方式进行的。我们可以让他攒够一定数量后发给数据库一次。

```java
public void test01() {
    SqlSession openSession = build.openSession(ExecutorType.BATCH);
    UserDao mapper = openSession.getMapper(UserDao.class);
    for (int i = 0; i < 1000000; i++) {
        String name = UUID.randomUUID().toString().substring(0, 5);
        mapper.addUser(new User(null, name, 13));
    }
    openSession.commit();
    openSession.close();
}
```

4、与Spring整合中，我们推荐，额外的配置一个可以专 门用来执行批量操作的sqlSession，需要用到批量操作的时候，我们可以注入配置的这个批量 SqlSession。通过他获取到mapper映射器进行操作。

**注意**：

1、批量操作是在**session.commit()**以后才发送sql语句给数 据库进行执行的

2、如果我们想让其提前执行，以方便后续可能的查询操作 获取数据，我们可以使用**sqlSession.flushStatements()**方 法，让其直接冲刷到数据库进行执行。

## 3、存储过程

**使用步骤**：

一个最简单的存储过程

```sql
delimiter $$
create procedure test()
begin
	select 'hello';
end $$
delimiter ;
```

存储过程的调用

1、select标签中statementType=“CALLABLE”

2、标签体中调用语法： {call procedure_name(#{param1_info},#{param2_info})}

**存储过程的-游标处理**：

MyBatis对存储过程的游标提供了一个**JdbcType=CURSOR**的支持， 可以智能的把游标读取到的数据，映射到我们声明的结果集中。

## 4、typeHandler处理枚举

通过自定义TypeHandler的形式来在设置参数或 者取出结果集的时候自定义参数封装策略。

**步骤**：

1. 实现**TypeHandler**接口或者继承**BaseTypeHandler**

2. 使用**@MappedTypes**定义处理的java类型

   使用**@MappedJdbcTypes**定义jdbcType类型

3. 在自定义结果集标签或者参数处理的时候声明使用自定义 TypeHandler进行处理，或者在全局配置TypeHandler要处理的javaType。

# 问题

## 1、MyBatis注解与XML混用

### 解答

MyBatis可以同时使用XML和注解的方式配置。

1、方法一：只写明XML的resource路径或者URL路径

- 原因：在**SqlSessionFactory**创建的过程中，会先创建**Configuration**对象，会先解析**SqlMapConfig.xml**中的节点，最后解析的就是节点，其中会调用**XMLMapperBuilder**的**parse()**方法解析，当解析了XML的方式节点时，会在解析XML文件配置到Configuration中之后进行一个命名空间绑定的操作： **bindMapperForNamespace();**该操作首先会判断Configuration有没有事先解析过Mapper对象，如果事先解析过则不做处理直接退出，如果没有解析过他则会通过XML文件中配置的命名空间反射到对应的Mapper类，然后通过一系列的反射操作解析注解。所以，只写明XML文件路径依然是可以解析到Mapper注解。

2、方法二：只写明注解Mapper的类全路径名（这种方式只适合于只包含注解的配置）

- 这种方式会直接向Configuration的MapperRegistry注册Mapper,但是由于Mapper对象不知道XML的位置所欲不会解析XML中的配置。故这种方式是**不安全的**。

3、方法三：同时注明，但是类全路径名必须写在XML前面

- 方式三一定要把类的配置写在xml的配置之前，先解析完mapper之后，可以继续解析xml，解析xml时如果判断mapper解析过之后则不会重复解析也不会抛错，但是如果先解析xml，会向Configuration中注册Mapper，当之后解析Mapper时如果检测到有加载过则会抛出异常并终止程序创建SqlSessionFactory。

### 注意

虽然可以同时采用XML和注解两种方式配置，但是不能同时对同一个方法既注解又XML配置，不然会报错。

# 扩展

## 1、OGNL

OGNL（ Object Graph Navigation Language ）对象图导航语言，这是一种强大的 表达式语言，通过它可以非常方便的来操作对象属性。 类似于我们的EL，SpEL等

| 使用              | 例子                                                |
| ----------------- | --------------------------------------------------- |
| 访问对象属性      | person.name                                         |
| 调用方法          | person.getName()                                    |
| 调用静态属性/方法 | @java.lang.Math@PI<br/>@java.util.UUID@randomUUID() |
| 调用构造方法      | new com.atguigu.bean.Person(‘admin’).name           |
| 运算符            | +，-，*，/，%                                       |
| 逻辑运算符        | in，not in，>，>=，<，<=，==，!=                    |

注意：xml中特殊符号如”，>，<等这些都需要使用转义字符

访问集合伪属性：

| 类型           | 伪属性        | 伪属性对应的Java方法                          |
| -------------- | ------------- | --------------------------------------------- |
| List、Set、Map | Size、isEmpty | List/Set/Map.size()<br>List/Set/Map.isEmpty() |
| List、Set      | iterator      | List.iterator()、Set.iterator()               |
| Map            | keys、values  | Map.keySet()、Map.values()                    |
| iterator       | mext、hasNext | iterator.next()、iterator.hasNext()           |

## 2、MyBatis处理与封装参数

(@Param("id")Integer id,@Param("lastName")String lastName)。
ParamNameResolver解析参数封装map的。

1、names：{0=id, 1=lastName}；构造器的时候就确定好了

确定流程：

1. 获取每个标了@Param注解的参数的@Param的值：id，lastName，然后赋值给name

2. 每次解析一个参数到map中保存信息：（key：参数索引，value：name的值）

   - name的值：

     - 标注了@Param注解：注解的值

     - 没有标注：

       1. 全局配置：useActualParamName（jdk1.8）：name=参数名

       2. name=map.size()；相当于当前元素的索引

          {0=id, 1=lastName,2=2}

          args【1，"Tom",'hello'】:

```java
public Object getNamedParams(Object[] args) {
    final int paramCount = names.size();
    //1、参数为null直接返回
    if (args == null || paramCount == 0) {
      return null;
     
    //2、如果只有一个元素，并且没有Param注解；args[0]：单个参数直接返回
    } else if (!hasParamAnnotation && paramCount == 1) {
      return args[names.firstKey()];
      
    //3、多个元素或者有Param标注
    } else {
      final Map<String, Object> param = new ParamMap<Object>();
      int i = 0;
      
      //4、遍历names集合；{0=id, 1=lastName,2=2}
      for (Map.Entry<Integer, String> entry : names.entrySet()) {
      
      	//names集合的value作为key;  names集合的key又作为取值的参考args[0]:args【1，"Tom"】:
      	//eg:{id=args[0]:1,lastName=args[1]:Tom,2=args[2]}
        param.put(entry.getValue(), args[entry.getKey()]);
        
        
        // add generic param names (param1, param2, ...)param
        //额外的将每一个参数也保存到map中，使用新的key：param1...paramN
        //效果：有@Param注解可以#{指定的key}，或者#{param1}
        final String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
        // ensure not to overwrite parameter named with @Param
        if (!names.containsValue(genericParamName)) {
          param.put(genericParamName, args[entry.getKey()]);
        }
        i++;
      }
      return param;
    }
  }
}
```

## 3、解析properties标签流程

首先解析properties标签

在XMLConfigBuilder类中的parseConfiguration()方法中有关于该标签的解析

查看**parseConfiguration()**方法的源代码：

```java
private void parseConfiguration(XNode root) {
    try {
        //issue #117 read properties first
        // 解析properties标签    
        propertiesElement(root.evalNode("properties"));
        // 解析settings标签
        Properties settings = settingsAsProperties(root.evalNode("settings"));
        loadCustomVfs(settings);
        // 解析别名标签
        // 例<typeAlias alias="user" type="cn.com.bean.User"/>
        typeAliasesElement(root.evalNode("typeAliases"));
        // 解析插件标签
        pluginElement(root.evalNode("plugins"));
        // 解析objectFactory标签，此标签的作用是mybatis每次创建结果对象的新实例时都会使用ObjectFactory
        // 如果不设置，则默认使用DefaultObjectFactory来创建，设置之后使用设置的
        objectFactoryElement(root.evalNode("objectFactory"));
        // 解析objectWrapperFactory标签
        objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
        // 解析reflectorFactory标签
        reflectorFactoryElement(root.evalNode("reflectorFactory"));
        settingsElement(settings);
        // read it after objectFactory and objectWrapperFactory issue #631
        // 解析environments标签
        environmentsElement(root.evalNode("environments"));
        databaseIdProviderElement(root.evalNode("databaseIdProvider"));
        typeHandlerElement(root.evalNode("typeHandlers"));
        // 解析<mappers>标签
        mapperElement(root.evalNode("mappers"));
    } catch (Exception e) {
        throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
    }
}
```

再解析子标签和属性，源代码如下：

```java
/**
 *解析步骤：
 *1、解析配置的property标签，放到defaults中；
 *2、解析resource或url属性，放到defaults中；
 *3、获取configuration中的variables变量值，放到defaults中
 * @param context
 * @throws Exception
 */
private void propertiesElement(XNode context) throws Exception {
    if (context != null) {
        // 1、读取properties标签中的property标签<property name="" value=""/>
        // 源码如下文
        Properties defaults = context.getChildrenAsProperties();
        // 2、读取properties标签中的resource、url属性
        String resource = context.getStringAttribute("resource");
        String url = context.getStringAttribute("url");
        // resource和url属性不能同时出现在properties标签中
        if (resource != null && url != null) {
            throw new BuilderException("The properties element cannot specify both a URL and a resource based property file reference.  Please specify one or the other.");
        }
        // 如果resource不为空，则解析转换为properties，置入defaults中，由于defaults是key-value结构，所以会覆盖相同key的值
        if (resource != null) {
            // 处理resource源码如下
            defaults.putAll(Resources.getResourceAsProperties(resource));
        } else if (url != null) {
            // 如果url不为空，则解析转换为properties，置入defaults中，由于defaults是key-value结构，所以会覆盖相同key的值
            defaults.putAll(Resources.getUrlAsProperties(url));
        }
        
        // 3、获得configuration中的variables变量的值，此变量可以通过SqlSessionFactoryBuilder.build()传入properties属性值
        Properties vars = configuration.getVariables();
        // 如果调用build的时候传入了properties属性，放到defaults中
        if (vars != null) {
            defaults.putAll(vars);
        }
        // 放到parser和configuration对象中
        parser.setVariables(defaults);
        configuration.setVariables(defaults);
    }
}
```

首先解析Properties的子标签，源码如下：

通过循环遍历Propertie获取其中的name值和value值。

```java
public Properties getChildrenAsProperties() {
    Properties properties = new Properties();
    for (XNode child : getChildren()) {
        String name = child.getStringAttribute("name");
        String value = child.getStringAttribute("value");
        if (name != null && value != null) {
            properties.setProperty(name, value);
        }
    }
    return properties;
}
```

接着处理resource属性，源码如下：

转化为InputStream，最后放到Properties对象中，处理url同理

```java
public static Properties getResourceAsProperties(String resource) throws IOException {
    Properties props = new Properties();
    InputStream in = getResourceAsStream(resource);
    props.load(in);
    in.close();
    return props;
}
```

之后，处理已添加的Properties，即从configuration获取Properties，如果configuration中已经存在properties信息，则取出来，放到defaults中。

最后放入configuration对象中。

把defaults放到了configuration的variables属性中，代表的是整个mybatis环境中所有的properties信息。这个信息可以在mybatis的配置文件中使用${key}使用，比如，${username}，则会从configuration的variables中寻找key为username的属性值，并完成自动属性值替换。

**总结**：

先解析property标签，然后是resource、url属性，最后是生成SqlSessionFactory调用SqlSessionFactoryBuilder的build()方法时，传入的properties。

从上面的解析过程，可以知道如果存在重复的键，那么最先解析的会被后面解析的覆盖掉，也就是解析过程是：property子标签-->resource-->url-->开发者设置的，那么覆盖过程为：开发者设置的-->url-->resource-->property子标签，优先级最高的为开发者自己设置的properties属性。

## 4、解析settings标签流程

在XMLConfigBuilder类中的parseConfiguration方法中有关于该标签的解析

```java
private void parseConfiguration(XNode root) {
    // .......
    // 解析settings标签，1、把<setting>标签解析为 Properties对象，源码如下文
    Properties settings = settingsAsProperties(root.evalNode("settings"));
     /*2、对<settings>标签中的<setting>标签中的内容进行解析，这里解析的是<setting name="vfsImpl" value=",">
      * VFS是mybatis中用来表示虚拟文件系统的一个抽象类，用来查找指定路径下的资源。
      * 上面的key为vfsImpl的value可以是VFS的具体实现，必须是全限类名，多个使用逗号隔开
      * 如果存在则设置到configuration中的vfsImpl属性中，如果存在多个，则设置到configuration中的仅是最后一个
      * */
    loadCustomVfs(settings);
    // .......
}
```

settingsAsProperties(XNode context)方法，源码入下：

```java
private Properties settingsAsProperties(XNode context) {
    if (context == null) {
        return new Properties();
    }
    // 解析子标签
    // 把<setting name="" value="">标签解析为Properties对象
    Properties props = context.getChildrenAsProperties();
    // Check that all settings are known to the configuration class
    // 校验setting标签中的name值是否存在
    // localReflectorFactory源码如下
    MetaClass metaConfig = MetaClass.forClass(Configuration.class, localReflectorFactory);
    // 如果获取的配置的<setting name="" value="">信息，name不在metaConfig中，则会抛出异常
    // 这里metaConfig中的信息是从Configuration类中解析出来的，包含set方法的属性
    // 所以在配置<setting>标签的时候，其name值可以参考configuration类中的属性，配置为小写
    // 校验属性，源码如下
    for (Object key : props.keySet()) {
        // 从metaConfig的relector中的setMethods中判断是否存在该属性，setMethods中存储的是可写的属性。
        // 所以这里要到setMethods中进行判断
        if (!metaConfig.hasSetter(String.valueOf(key))) {
            throw new BuilderException("The setting " + key + " is not known.  Make sure you spelled it correctly (case sensitive).");
        }
    }
    return props;
}
```

查看localReflectorFactory源码：

```java
// 在XMLConfigBuilder类中的属性
private final ReflectorFactory localReflectorFactory = new DefaultReflectorFactory();
```

使用了DefaultReflectorFactory，看其默认构造方法

```java
// 默认构造方法仅初始化了classCacheEnabled和relectorMap两个属性。
public class DefaultReflectorFactory implements ReflectorFactory {
    private boolean classCacheEnabled = true;
    private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentMap<Class<?>, Reflector>;

}
```

回过来继续看MetaClass.forClass方法

```java
public static MetaClass forClass(Class<?> type, ReflectorFactory reflectorFactory) {
    return new MetaClass(type, reflectorFactory);
}
```

方法返回的是一个MetaClass的对象。

```java
private MetaClass(Class<?> type, ReflectorFactory reflectorFactory) {
    this.reflectorFactory = reflectorFactory;
    // 源码如下
    this.reflector = reflectorFactory.findForClass(type);
}
```

重点看reflectorFactory.findForClass()方法，这里reflectorFactory是DefaultReflectorFactory的一个实例。

下面是DefaultReflectorFactory的findForClass()方法。

```java
@Override
public Reflector findForClass(Class<?> type) {
    if (classCacheEnabled) {
        // synchronized (type) removed see issue #461
        Reflector cached = reflectorMap.get(type);
        if (cached == null) {
            cached = new Reflector(type);
            reflectorMap.put(type, cached);
        }
        return cached;
    } else {
        // 重点，源码如下
        return new Reflector(type);
    }
}
```

```java
public Reflector(Class<?> clazz) {
    type = clazz;
    // 解析默认的构造方法，及无参构造方法，源码如下
    addDefaultConstructor(clazz);
    // 解析clazz中的get方法，这里的clazz指的是Configuration.class，源码如下
    addGetMethods(clazz);
    // 解析clazz中的set方法，这里的clazz指的是Configuration.class，源码如下
    addSetMethods(clazz);
    
    addFields(clazz);
    readablePropertyNames = getMethods.keySet().toArray(new String[getMethods.keySet().size()]);
    writeablePropertyNames = setMethods.keySet().toArray(new String[setMethods.keySet().size()]);
    for (String propName : readablePropertyNames) {
        caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
    }
    for (String propName : writeablePropertyNames) {
        caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
    }
}
```

此方法完成的功能是解析clazz（包含其父类）的构造方法、getXX方法、setXX方法、字段，通过一个类的Class对象获取。

addDefaultConstructor(clazz)源码如下：

```java
private void addDefaultConstructor(Class<?> clazz) {
    // 获得该类的声明的构造方法
    Constructor<?>[] consts = clazz.getDeclaredConstructors();
    // 对构造方法进行循环
    for (Constructor<?> constructor : consts) {
        // 判断构造方法的参数是否为0，为0代表为默认的无参构造方法
        if (constructor.getParameterTypes().length == 0) {
            // 如果是私有的（修饰符为private），这里需要设置可见。
            if (canAccessPrivateMethods()) {
                try {
                    constructor.setAccessible(true);
                } catch (Exception e) {
                    // Ignored. This is only a final precaution, nothing we can do.
                }
            }
            if (constructor.isAccessible()) {
                this.defaultConstructor = constructor;
            }
        }
    }
}
```

addGetMethods(clazz)源码如下：

```java
private void addGetMethods(Class<?> cls) {
    Map<String, List<Method>> conflictingGetters = new HashMap<String, List<Method>>();
    // 使用反射的放上获得cls的所有方法
    Method[] methods = getClassMethods(cls);
    // 把所有的方法放入conflictingGetters中，key为属性名，value为List<Method>
    for (Method method : methods) {
        // 方法的参数大于0，则结束本次循环，因为这里解析的是get方法，get方法默认不应该有参数
        if (method.getParameterTypes().length > 0) {
            continue;
        }
        String name = method.getName();
        // 如果以get或is开头，且方法名称分别大于3和2，则说明是get方法
        if ((name.startsWith("get") && name.length() > 3)
            || (name.startsWith("is") && name.length() > 2)) {
            // 通过方法名转化为属性名，如，getUserName-->userName
            name = PropertyNamer.methodToProperty(name);
			// 添加冲突的方法， 源码如下
            addMethodConflict(conflictingGetters, name, method);
        }
    }
    /**
    *处理一个属性多个get方法的情况。
    *即conflictingGetter方法中一个key对应的value的长度大于1的情况，
    *如下：         
    *key propertyName         
    *value list<Method> 其长度大于1          
    */ 
    // 源码如下
    resolveGetterConflicts(conflictingGetters);
}
```

查看addGetMethods(clazz)中的addMethodConflict()源码如下：

```java
private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
    // 根据字段名取方法
    List<Method> list = conflictingMethods.get(name);
    if (list == null) {
        list = new ArrayList<Method>();
        conflictingMethods.put(name, list);
    }
    list.add(method);
}
```

这里是根据get和is开头的方法获取属性名作为键值，并且使用list作为value进行存储，为什么使用list，看下面的方法：

```java
public void getUser(){}
public User getuser(){}
public List<User> getUser(){}
public void getUser(String id){}
```

上面三个方法都会以user为键进行存储，但是其方法名是一样的，所以这里要存储为list，即存储多个Method对象。

由于一个字段的属性的get或set方法，不可能出现上面的情况，所以针对上面的情况需要做处理，在addGetMethods(clazz)最后调用resolveGetterConflicts(conflicttingGetters)方法。

```java
private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
    // 遍历conflictingGetters
    for (Entry<String, List<Method>> entry : conflictingGetters.entrySet()) {
        Method winner = null;
        String propName = entry.getKey();
        // 循环value这里value是一个List<Method>类型
        for (Method candidate : entry.getValue()) {
            if (winner == null) {
                winner = candidate;
                continue;
            }
            // 获得get方法的返回值类型
            Class<?> winnerType = winner.getReturnType();
            Class<?> candidateType = candidate.getReturnType();
            // 如果winnerType和candidateType相等，
            if (candidateType.equals(winnerType)) {
                if (!boolean.class.equals(candidateType)) {
                    throw new ReflectionException(
                        "Illegal overloaded getter method with ambiguous type for property "
                        + propName + " in class " + winner.getDeclaringClass()
                        + ". This breaks the JavaBeans specification and can cause unpredictable results.");
                } else if (candidate.getName().startsWith("is")) {
                    winner = candidate;
                }
            } else if (candidateType.isAssignableFrom(winnerType)) {
                // OK getter type is descendant
            } else if (winnerType.isAssignableFrom(candidateType)) {
                winner = candidate;
            } else {
                throw new ReflectionException(
                    "Illegal overloaded getter method with ambiguous type for property "
                    + propName + " in class " + winner.getDeclaringClass()
                    + ". This breaks the JavaBeans specification and can cause unpredictable results.");
            }
        }
        // 源码如下
        addGetMethod(propName, winner);
    }
}
```

上面的方法处理了上面提到的一个属性存在多个get方法的情况，所以resolveGetterConflicts(conflicttingGetters)方法最后调用addGetMethod()方法

```java
private void addGetMethod(String name, Method method) {
    if (isValidPropertyName(name)) {
        getMethods.put(name, new MethodInvoker(method));
        Type returnType = TypeParameterResolver.resolveReturnType(method, type);
        getTypes.put(name, typeToClass(returnType));
    }
}
```

上面的方法把信息放到了getMethods和getTyps中，分别存储了get方法和返回值。

上面分析了Reflector中的addGetMethods()方法，addSetMethods()方法和其处理过程类似，最终把set方法和返回值放到了setMethods和setTypes中。

回过来看Reflector(Class<?> clazz)的addFileds(clazz)方法，即是处理clazz中的属性：

```java
private void addFields(Class<?> clazz) {
    
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
        if (canAccessPrivateMethods()) {
            try {
                field.setAccessible(true);
            } catch (Exception e) {
                // Ignored. This is only a final precaution, nothing we can do.
            }
        }
        if (field.isAccessible()) {
            // 检查是否存在set方法，如果不存在添加该field
            if (!setMethods.containsKey(field.getName())) {
                // issue #379 - removed the check for final because JDK 1.5 allows
                // modification of final fields through reflection (JSR-133). (JGB)
                // pr #16 - final static can only be set by the classloader
                int modifiers = field.getModifiers();
                if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                    addSetField(field);
                }
            }
            // 检查是否存在get方法，如果不存在添加该field
            if (!getMethods.containsKey(field.getName())) {
                addGetField(field);
            }
        }
    }
    // 添加父类的field
    if (clazz.getSuperclass() != null) {
        // 源码如下
        addFields(clazz.getSuperclass());
    }
}
```

获得field之后，判断是否在getMethods和setMethods中，如果不在则进行添加，查看其内的addSetField()方法

```java
private void addSetField(Field field) {
    if (isValidPropertyName(field.getName())) {
        setMethods.put(field.getName(), new SetFieldInvoker(field));
        Type fieldType = TypeParameterResolver.resolveFieldType(field, type);
        setTypes.put(field.getName(), typeToClass(fieldType));
    }
}
```

从上面看到如果一个field不存在set方法，则生成一个SetFieldInvoker把该对象放入setMethods，从这里可以看出一个setting配置的name值在configuration中可以没有set方法。同理也可以没有get方法。

上面分析完了settingsAsProperties()方法中的MetaClass.forClass(Configuration.class, localReflectorFactory);代码

把Configuration中的构造方法、get方法、set方法、field放入了metaConfig中的reflector对象中的下列属性：

```java
private final String[] readablePropertyNames;
private final String[] writeablePropertyNames;
private final Map<String, Invoker> setMethods = new HashMap<String, Invoker>();
private final Map<String, Invoker> getMethods = new HashMap<String, Invoker>();
private final Map<String, Class<?>> setTypes = new HashMap<String, Class<?>>();
private final Map<String, Class<?>> getTypes = new HashMap<String, Class<?>>();
private Constructor<?> defaultConstructor;
```

最后回到settingsAsProperties()里，下一步，校验配置的setting标签中的name是否存在。

遍历从setting标签解析出来的Properties对象，调用metaConfig.hasSetter方法，源码如下：

```java
public boolean hasSetter(String name) {
    PropertyTokenizer prop = new PropertyTokenizer(name);
    if (prop.hasNext()) {
        // 源码如下
      if (reflector.hasSetter(prop.getName())) {
        MetaClass metaProp = metaClassForProperty(prop.getName());
        return metaProp.hasSetter(prop.getChildren());
      } else {
        return false;
      }
    } else {
      return reflector.hasSetter(prop.getName());
    }
  }
```

查看reflector.hasSetter(prop.getName())源码：

```java
public boolean hasSetter(String propertyName) {
    return setMethods.keySet().contains(propertyName);
}
```

判断setMethods是否存在该key，也就是已set方法为表标准，只要在setMethods中，便可以在\<setting>标签的name中配置，具体配置值还需要看其类型。

## 5、解析typeAliases标签流程






























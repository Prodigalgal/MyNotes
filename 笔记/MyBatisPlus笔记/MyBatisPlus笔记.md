# MyBatisPlus快速开始

## 1、创建一张数据表

```sql
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
	id BIGINT(20) NOT NULL COMMENT '主键ID'，
	name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名'，
	age INT(11) NULL DEFAULT NULL COMMENT '年龄'，
	email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱'，
	PRIMARY KEY (id)
);

DELETE FROM user;

INSERT INTO user (id， name， age， email) VALUES
(1， 'Jone'， 18， 'test1@baomidou.com')，
(2， 'Jack'， 20， 'test2@baomidou.com')，
(3， 'Tom'， 28， 'test3@baomidou.com')，
(4， 'Sandy'， 21， 'test4@baomidou.com')，
(5， 'Billie'， 24， 'test5@baomidou.com');
```

## 2、SpringBoot初始化过程

### 1、添加依赖

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>spring-latest-version</version>
    <relativePath/>
</parent>
```

引入 `spring-boot-starter`、`spring-boot-starter-test`、`mybatis-plus-boot-starter`、`h2` 依赖：

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>Latest Version</version>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### 2、编辑配置文件

在 `application.yml` 配置文件中添加 H2 数据库的相关配置：

```yaml
# DataSource Config
spring:
  datasource:
    driver-class-name: org.h2.Driver
    schema: classpath:db/schema-h2.sql
    data: classpath:db/data-h2.sql
    url: jdbc:h2:mem:test
    username: root
    password: test
```

properties版本

```properties
# 配置连接数据库的四大参数
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

spring.datasource.url=jdbc:mysql://192.168.133.139/db_mybatisplus?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true

spring.datasource.username=root

spring.datasource.password=root

# 指定连接池的类型
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource

# 显示SQL语句
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```



#### SpringBoot

在 Spring Boot 启动类中添加 `@MapperScan` 注解，扫描 Mapper 文件夹：

```java
@SpringBootApplication
@MapperScan("com.baomidou.mybatisplus.samples.quickstart.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(QuickStartApplication.class， args);
    }

}
```

#### Spring

配置 MapperScan

```xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.baomidou.mybatisplus.samples.quickstart.mapper"/>
</bean>
```

调整 SqlSessionFactory 为 MyBatis-Plus 的 SqlSessionFactory

```xml
<bean id="sqlSessionFactory" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

## 3、编码

编写实体类 `User.java`，使用了Lombok简化开发

```java
@NoArgsConstructor// 创建无参的构造方法
@AllArgsConstructor// 创建满参的构造方法
@Accessors(chain = true)// 使用链式方法
@Data// 重写toString方法等方法
@TableName("User") // 对应表名
public class User {
    
    @TableId(value = "id", type = IdType.AUTO) // 主键必须有TableId注解
    private Long id;
    @TableField("name")
    private String name;
    @TableField("age")
    private Integer age;
    @TableField("email")
    private String email;
}
```

编写Mapper类 `UserMapper.java`，继承BaseMapper\<User>，在启动类标注@MapperScan扫描继承了BaseMapper的类

```java
@Repository // 添加入IOC容器中
public interface UserMapper extends BaseMapper<User> {

}
```

## 4、开始使用

```java
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        // UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper，所以不填写就是无任何条件
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5， userList.size());
        userList.forEach(System.out::println);
    }
}
```

# MyBatisPlus相关注解

## @TableName

- 描述：表名注解
- 把实体类和数据库进行绑定，好处是当数据库名与MyBatis-Plus默认名字不一样时，也可以进行操作。

|       属性       |   类型   | 必须指定 | 默认值 | 描述                                                         |
| :--------------: | :------: | :------: | :----: | ------------------------------------------------------------ |
|      value       |  String  |    否    |   ""   | 表名                                                         |
|      schema      |  String  |    否    |   ""   | schema                                                       |
| keepGlobalPrefix | boolean  |    否    | false  | 是否保持使用全局的 tablePrefix 的值(如果设置了全局 tablePrefix 且自行设置了 value 的值) |
|    resultMap     |  String  |    否    |   ""   | xml 中 resultMap 的 id                                       |
|  autoResultMap   | boolean  |    否    | false  | 是否自动构建 resultMap 并使用(如果设置 resultMap 则不会进行 resultMap 的自动构建并注入) |
| excludeProperty  | String[] |    否    |   {}   | 需要排除的属性名(@since 3.3.1)                               |

关于 autoResultMap 的说明：

mp会自动构建一个 ResultMap 并注入到mybatis里(一般用不上)。

 因为mp底层是mybatis，mp只是帮你注入了常用crud到mybatis里，注入之前可以说是动态的（根据你entity的字段以及注解变化而变化），但是注入之后是静态的（等于你写在xml的东西） 而对于直接指定 typeHandler，mybatis只支持你写在2个地方：

1. 定义在resultMap里，只作用于select查询的返回结果封装。
2. 定义在 insert 和 update sql的 #{property} 里的 property 后面（例：#{property，typehandler=xxx.xxx.xxx}），只作用于设置值。而除了这两种直接指定typeHandler，mybatis有一个全局的扫描你自己的 typeHandler 包的配置，这是根据你的 property 的类型去找 typeHandler 并使用。

## @TableId

- 描述：主键注解
- 主键必须有TableId注解

| 属性  |  类型  | 必须指定 |   默认值    |    描述    |
| :---: | :----: | :------: | :---------: | :--------: |
| value | String |    否    |     ""      | 主键字段名 |
| type  |  Enum  |    否    | IdType.NONE |  主键类型  |

IdType可选值：

| 值            | 描述                                                         |
| :------------ | :----------------------------------------------------------- |
| AUTO          | 数据库ID自增                                                 |
| NONE          | 无状态，该类型为未设置主键类型(注解里等于跟随全局，全局里约等于 INPUT) |
| INPUT         | insert 前自行 set 主键值                                     |
| ASSIGN_ID     | 分配ID（主键类型为Number（Long和Integer）或String）（since 3.3.0），使用**接口 IdentifierGenerator** 的方法 **nextId** （默认实现类为 DefaultIdentifierGenerator 雪花算法） |
| ASSIGN_UUID   | 分配UUID，主键类型为String（since 3.3.0），使用**接口 IdentifierGenerator** 的方法 **nextUUID** （默认default方法） |
| ID_WORKER     | 分布式全局唯一ID 长整型类型（please use  ASSIGN_ID）         |
| UUID          | 32位UUID字符串（please use ASSIGN_UUID）                     |
| ID_WORKER_STR | 分布式全局唯一ID 字符串类型（please use ASSIGN_ID）          |

## @TableField

- 描述：字段注解(非主键)

|       属性       |             类型             | 必须指定 |          默认值          |                             描述                             |
| :--------------: | :--------------------------: | :------: | :----------------------: | :----------------------------------------------------------: |
|      value       |            String            |    否    |            ""            |                         数据库字段名                         |
|        el        |            String            |    否    |            ""            | 映射为原生 `#{ ... }` 逻辑，相当于写在 xml 里的 `#{ ... }` 部分 |
|      exist       |           boolean            |    否    |           true           |                      是否为数据库表字段                      |
|    condition     |            String            |    否    |            ""            | 字段 where 实体查询比较条件，有值设置则按设置的值为准，没有则为默认全局的 `%s=#{%s}`，[参考(opens new window)](https://github.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-annotation/src/main/java/com/baomidou/mybatisplus/annotation/SqlCondition.java) |
|      update      |            String            |    否    |            ""            | 字段 update set 部分注入， 例如：update="%s+1"：表示更新时会set version=version+1(该属性优先级高于 `el` 属性) |
|  insertStrategy  |             Enum             |    N     |         DEFAULT          | 举例：NOT_NULL: `insert into table_a(<if test="columnProperty != null">column</if>) values (<if test="columnProperty != null">#{columnProperty}</if>)` |
|  updateStrategy  |             Enum             |    N     |         DEFAULT          | 举例：IGNORED: `update table_a set column=#{columnProperty}` |
|  whereStrategy   |             Enum             |    N     |         DEFAULT          | 举例：NOT_EMPTY: `where <if test="columnProperty != null and columnProperty!=''">column=#{columnProperty}</if>` |
|       fill       |             Enum             |    否    |    FieldFill.DEFAULT     |                       字段自动填充策略                       |
|      select      |           boolean            |    否    |           true           |                     是否进行 select 查询                     |
| keepGlobalFormat |           boolean            |    否    |          false           |              是否保持使用全局的 format 进行处理              |
|     jdbcType     |           JdbcType           |    否    |    JdbcType.UNDEFINED    |           JDBC类型 (该默认值不代表会按照该值生效)            |
|   typeHandler    | Class<? extends TypeHandler> |    否    | UnknownTypeHandler.class |          类型处理器 (该默认值不代表会按照该值生效)           |
|   numericScale   |            String            |    否    |            ""            |                    指定小数点后保留的位数                    |

关于 jdbcType 和 typeHandler 以及 numericScale 的说明：

`numericScale`只生效于 update 的sql. `jdbcType`和`typeHandler`如果不配合`@TableName#autoResultMap = true`一起使用，也只生效于 update 的sql. 对于`typeHandler`如果你的字段类型和set进去的类型为`equals`关系，则只需要让你的`typeHandler`让Mybatis加载到即可，不需要使用注解

FieldStrategy属性值：

|    值     |                            描述                            |
| :-------: | :--------------------------------------------------------: |
|  IGNORED  |                          忽略判断                          |
| NOT_NULL  |                         非NULL判断                         |
| NOT_EMPTY | 非空判断(只对字符串类型字段，其他类型字段依然为非NULL判断) |
|  DEFAULT  |                        追随全局配置                        |

FieldFill属性值：

|      值       |         描述         |
| :-----------: | :------------------: |
|    DEFAULT    |      默认不处理      |
|    INSERT     |    插入时填充字段    |
|    UPDATE     |    更新时填充字段    |
| INSERT_UPDATE | 插入和更新时填充字段 |

## @Version

- 描述：乐观锁注解、标记 `@Verison` 在字段上

## @EnumValue

- 描述：通枚举类注解(注解在枚举字段上)

## @TableLogic

- 描述：表字段逻辑处理注解（逻辑删除）

|  属性  |  类型  | 必须指定 | 默认值 |     描述     |
| :----: | :----: | :------: | :----: | :----------: |
| value  | String |    否    |   ""   | 逻辑未删除值 |
| delval | String |    否    |   ""   |  逻辑删除值  |

## @KeySequence

- 描述：序列主键策略 `oracle`
- 属性：value、resultMap

| 属性  |  类型  | 必须指定 |   默认值   |                             描述                             |
| :---: | :----: | :------: | :--------: | :----------------------------------------------------------: |
| value | String |    否    |     ""     |                            序列名                            |
| clazz | Class  |    否    | Long.class | id的类型， 可以指定String.class，这样返回的Sequence值是字符串"1" |

## @OrderBy

- 描述：内置 SQL 默认指定排序，优先级低于 wrapper 条件查询

|  属性  |  类型   | 必须指定 |     默认值      |      描述      |
| :----: | :-----: | :------: | :-------------: | :------------: |
| isDesc | boolean |    否    |       是        |  是否倒序查询  |
|  sort  |  short  |    否    | Short.MAX_VALUE | 数字越小越靠前 |

# MyBatisPlus代码生成器

## 1、使用

### 快速生成

```java
FastAutoGenerator.create("url"， "username"， "password")
    .globalConfig(builder -> {
        // 设置作者
        builder.author("baomidou") 
            // 开启 swagger 模式
            .enableSwagger() 
            // 覆盖已生成文件
            .fileOverride() 
            // 指定输出目录
            .outputDir("D://"); 
    })
    .packageConfig(builder -> {
        // 设置父包名
        builder.parent("com.baomidou.mybatisplus.samples.generator") 
            // 设置父包模块名
            .moduleName("system") 
            // 设置mapperXml生成路径
            .pathInfo(Collections.singletonMap(OutputFile.mapperXml， "D://")); 
    })
    .strategyConfig(builder -> {
        // 设置需要生成的表名
        builder.addInclude("t_simple") 
            // 设置过滤表前缀
            .addTablePrefix("t_"， "c_"); 
    })
    // 使用Freemarker引擎模板，默认的是Velocity引擎模板
    .templateEngine(new FreemarkerTemplateEngine())
    .execute();
```

### 交互式生成

```java
FastAutoGenerator.create(DATA_SOURCE_CONFIG)
    // 全局配置
    .globalConfig((scanner， builder) -> builder.author(scanner.apply("请输入作者名称？")).fileOverride())
    // 包配置
    .packageConfig((scanner， builder) -> builder.parent(scanner.apply("请输入包名？")))
    // 策略配置
    .strategyConfig(builder -> builder.addInclude(Arrays.asList(scanner.apply("请输入表名，多个英文逗号分隔？").split("，")))
                    .controllerBuilder().enableRestStyle().enableHyphenStyle()
                    .entityBuilder().enableLombok().addTableFills(new Column("create_time"， FieldFill.INSERT))
                    .build())
    /*
        模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
       .templateEngine(new BeetlTemplateEngine())
       .templateEngine(new FreemarkerTemplateEngine())
     */
    .execute();
```

## 2、说明

### 数据库配置(DataSourceConfig)

#### 基础配置

| 属性     | 说明       | 示例                                     |
| -------- | ---------- | ---------------------------------------- |
| url      | jdbc路径   | jdbc:mysql://127.0.0.1:3306/mybatis-plus |
| username | 数据库账号 | root                                     |
| password | 数据库密码 | 123456                                   |

```java
new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/mybatis-plus"， "root"， "123456").build();
```

#### 可选配置

| 方法                              | 说明                         | 示例                       |
| --------------------------------- | ---------------------------- | -------------------------- |
| dbQuery(IDbQuery)                 | 数据库查询                   | new MySqlQuery()           |
| schema(String)                    | 数据库schema(部分数据库适用) | mybatis-plus               |
| typeConvert(ITypeConvert)         | 数据库类型转换器             | new MySqlTypeConvert()     |
| keyWordsHandler(IKeyWordsHandler) | 数据库关键字处理器           | new MySqlKeyWordsHandler() |

```java
new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/mybatis-plus"，"root"，"123456")
    .dbQuery(new MySqlQuery())
    .schema("mybatis-plus")
    .typeConvert(new MySqlTypeConvert())
    .keyWordsHandler(new MySqlKeyWordsHandler())
    .build();
```

### 全局配置(GlobalConfig)

| 方法                | 说明              | 示例                                                      |
| ------------------- | ----------------- | --------------------------------------------------------- |
| fileOverride        | 覆盖已生成文件    | 默认值:false                                              |
| disableOpenDir      | 禁止打开输出目录  | 默认值:true                                               |
| outputDir(String)   | 指定输出目录      | /opt/baomidou/ 默认值： windows:D:// linux or mac ： /tmp |
| author(String)      | 作者名            | baomidou 默认值：作者                                     |
| enableKotlin        | 开启 kotlin 模式  | 默认值:false                                              |
| enableSwagger       | 开启 swagger 模式 | 默认值:false                                              |
| dateType(DateType)  | 时间策略          | DateType.ONLY_DATE 默认值： DateType.TIME_PACK            |
| commentDate(String) | 注释日期          | 默认值： yyyy-MM-dd                                       |

```java
new GlobalConfig.Builder()
    .fileOverride()
    .outputDir("/opt/baomidou")
    .author("baomidou")
    .enableKotlin()
    .enableSwagger()
    .dateType(DateType.TIME_PACK)
    .commentDate("yyyy-MM-dd")
    .build();
```

### 包配置(PackageConfig)

| 方法                               | 说明              | 示例                                                    |
| ---------------------------------- | ----------------- | ------------------------------------------------------- |
| parent(String)                     | 父包名            | 默认值:com.baomidou                                     |
| moduleName(String)                 | 父包模块名        | 默认值：无                                              |
| entity(String)                     | Entity 包名       | 默认值:entity                                           |
| service(String)                    | Service 包名      | 默认值:service                                          |
| serviceImpl(String)                | Service Impl 包名 | 默认值:service.impl                                     |
| mapper(String)                     | Mapper 包名       | 默认值:mapper                                           |
| mapperXml(String)                  | Mapper XML 包名   | 默认值:mapper.xml                                       |
| controller(String)                 | Controller 包名   | 默认值:controller                                       |
| other(String)                      | 自定义文件包名    | 输出自定义文件时所用到的包名                            |
| pathInfo(Map<OutputFile， String>) | 路径配置信息      | Collections.singletonMap(OutputFile.mapperXml， "D://") |

```java
new PackageConfig.Builder()
    .parent("com.baomidou.mybatisplus.samples.generator")
    .moduleName("sys")
    .entity("po")
    .service("service")
    .serviceImpl("service.impl")
    .mapper("mapper")
    .mapperXml("mapper.xml")
    .controller("controller")
    .other("other")
    .pathInfo(Collections.singletonMap(OutputFile.mapperXml， "D://")
    .build();
```

### 模板配置(TemplateConfig)

| 方法                     | 说明                      | 示例                        |
| ------------------------ | ------------------------- | --------------------------- |
| disable                  | 禁用所有模板              |                             |
| disable(TemplateType...) | 禁用模板                  | TemplateType.ENTITY         |
| entity(String)           | 设置实体模板路径(JAVA)    | /templates/entity.java      |
| entityKt(String)         | 设置实体模板路径(kotlin)  | /templates/entity.java      |
| service(String)          | 设置 service 模板路径     | /templates/service.java     |
| serviceImpl(String)      | 设置 serviceImpl 模板路径 | /templates/serviceImpl.java |
| mapper(String)           | 设置 mapper 模板路径      | /templates/mapper.java      |
| mapperXml(String)        | 设置 mapperXml 模板路径   | /templates/mapper.xml       |
| controller(String)       | 设置 controller 模板路径  | /templates/controller.java  |

```java
new TemplateConfig.Builder()
    .disable(TemplateType.ENTITY)
    .entity("/templates/entity.java")
    .service("/templates/service.java")
    .serviceImpl("/templates/serviceImpl.java")
    .mapper("/templates/mapper.java")
    .mapperXml("/templates/mapper.xml")
    .controller("/templates/controller.java")
    .build();
```

### 注入配置(InjectionConfig)

| 方法                                                         | 说明                | 示例                                                        |
| ------------------------------------------------------------ | ------------------- | ----------------------------------------------------------- |
| beforeOutputFile(BiConsumer<TableInfo， Map<String， Object>>) | 输出文件之前消费者  |                                                             |
| customMap(Map<String， Object>)                              | 自定义配置 Map 对象 | Collections.singletonMap("test"， "baomidou")               |
| customFile(Map<String， String>)                             | 自定义配置模板文件  | Collections.singletonMap("test.txt"， "/templates/test.vm") |

```java
new InjectionConfig.Builder()
    .beforeOutputFile((tableInfo， objectMap) -> {
    System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
    })
    .customMap(Collections.singletonMap("test"， "baomidou"))
    .customFile(Collections.singletonMap("test.txt"， "/templates/test.vm"))
    .build();
```

### 策略配置(StrategyConfig)

|                           |                          |                                                              |
| ------------------------- | ------------------------ | ------------------------------------------------------------ |
| 方法                      | 说明                     | 示例                                                         |
| enableCapitalMode         | 开启大写命名             | 默认值:false                                                 |
| enableSkipView            | 开启跳过视图             | 默认值:false                                                 |
| disableSqlFilter          | 禁用 sql 过滤            | 默认值:true，语法不能支持使用 sql 过滤表的话，可以考虑关闭此开关 |
| enableSchema              | 启用 schema              | 默认值:false，多 schema 场景的时候打开                       |
| likeTable(LikeTable)      | 模糊表匹配(sql 过滤)     | likeTable 与 notLikeTable 只能配置一项                       |
| notLikeTable(LikeTable)   | 模糊表排除(sql 过滤)     | likeTable 与 notLikeTable 只能配置一项                       |
| addInclude(String...)     | 增加表匹配(内存过滤)     | include 与 exclude 只能配置一项                              |
| addExclude(String...)     | 增加表排除匹配(内存过滤) | include 与 exclude 只能配置一项                              |
| addTablePrefix(String...) | 增加过滤表前缀           |                                                              |
| addTableSuffix(String...) | 增加过滤表后缀           |                                                              |
| addFieldPrefix(String...) | 增加过滤字段前缀         |                                                              |
| addFieldSuffix(String...) | 增加过滤字段后缀         |                                                              |
| entityBuilder             | 实体策略配置             |                                                              |
| controllerBuilder         | controller 策略配置      |                                                              |
| mapperBuilder             | mapper 策略配置          |                                                              |
| serviceBuilder            | service 策略配置         |                                                              |

```java
new StrategyConfig.Builder()
    .enableCapitalMode()
    .enableSkipView()
    .disableSqlFilter()
    .likeTable(new LikeTable("USER"))
    .addInclude("t_simple")
    .addTablePrefix("t_"， "c_")
    .addFieldSuffix("_flag")
    .build();
```

#### Entity策略配置

|                                    |                                   |                                                        |
| ---------------------------------- | --------------------------------- | ------------------------------------------------------ |
| 方法                               | 说明                              | 示例                                                   |
| nameConvert(INameConvert)          | 名称转换实现                      |                                                        |
| superClass(Class<?>)               | 设置父类                          | BaseEntity.class                                       |
| superClass(String)                 | 设置父类                          | com.baomidou.global.BaseEntity                         |
| disableSerialVersionUID            | 禁用生成 serialVersionUID         | 默认值:true                                            |
| enableColumnConstant               | 开启生成字段常量                  | 默认值:false                                           |
| enableChainModel                   | 开启链式模型                      | 默认值:false                                           |
| enableLombok                       | 开启 lombok 模型                  | 默认值:false                                           |
| enableRemoveIsPrefix               | 开启 Boolean 类型字段移除 is 前缀 | 默认值:false                                           |
| enableTableFieldAnnotationEnable   | 开启生成实体时生成字段注解        | 默认值:false                                           |
| enableActiveRecord                 | 开启 ActiveRecord 模型            | 默认值:false                                           |
| versionColumnName(String)          | 乐观锁字段名(数据库)              |                                                        |
| versionPropertyName(String)        | 乐观锁属性名(实体)                |                                                        |
| logicDeleteColumnName(String)      | 逻辑删除字段名(数据库)            |                                                        |
| logicDeletePropertyName(String)    | 逻辑删除属性名(实体)              |                                                        |
| naming                             | 数据库表映射到实体的命名策略      | 默认下划线转驼峰命名:NamingStrategy.underline_to_camel |
| columnNaming                       | 数据库表字段映射到实体的命名策略  | 默认为 null，未指定按照 naming 执行                    |
| addSuperEntityColumns(String...)   | 添加父类公共字段                  |                                                        |
| addIgnoreColumns(String...)        | 添加忽略字段                      |                                                        |
| addTableFills(IFill...)            | 添加表字段填充                    |                                                        |
| addTableFills(List)                | 添加表字段填充                    |                                                        |
| idType(IdType)                     | 全局主键类型                      |                                                        |
| convertFileName(ConverterFileName) | 转换文件名称                      |                                                        |
| formatFileName(String)             | 格式化文件名称                    |                                                        |

```java
new StrategyConfig.Builder()
    .entityBuilder()
    .superClass(BaseEntity.class)
    .disableSerialVersionUID()
    .enableChainModel()
    .enableLombok()
    .enableRemoveIsPrefix()
    .enableTableFieldAnnotation()
    .enableActiveRecord()
    .versionColumnName("version")
    .versionPropertyName("version")
    .logicDeleteColumnName("deleted")
    .logicDeletePropertyName("deleteFlag")
    .naming(NamingStrategy.no_change)
    .columnNaming(NamingStrategy.underline_to_camel)
    .addSuperEntityColumns("id"， "created_by"， "created_time"， "updated_by"， "updated_time")
    .addIgnoreColumns("age")
    .addTableFills(new Column("create_time"， FieldFill.INSERT))
    .addTableFills(new Property("updateTime"， FieldFill.INSERT_UPDATE))
    .idType(IdType.AUTO)
    .formatFileName("%sEntity")
    .build();
```

#### Controller策略配置

| 方法                               | 说明                           | 示例                               |
| ---------------------------------- | ------------------------------ | ---------------------------------- |
| superClass(Class<?>)               | 设置父类                       | BaseController.class               |
| superClass(String)                 | 设置父类                       | com.baomidou.global.BaseController |
| enableHyphenStyle                  | 开启驼峰转连字符               | 默认值:false                       |
| enableRestStyle                    | 开启生成@RestController 控制器 | 默认值:false                       |
| convertFileName(ConverterFileName) | 转换文件名称                   |                                    |
| formatFileName(String)             | 格式化文件名称                 |                                    |

```java
new StrategyConfig.Builder()
    .controllerBuilder()
    .superClass(BaseController.class)
    .enableHyphenStyle()
    .enableRestStyle()
    .formatFileName("%sAction")
    .build();
```

#### Service策略配置

| 方法                                          | 说明                          | 示例                                |
| --------------------------------------------- | ----------------------------- | ----------------------------------- |
| superServiceClass(Class<?>)                   | 设置 service 接口父类         | BaseService.class                   |
| superServiceClass(String)                     | 设置 service 接口父类         | com.baomidou.global.BaseService     |
| superServiceImplClass(Class<?>)               | 设置 service 实现类父类       | BaseServiceImpl.class               |
| superServiceImplClass(String)                 | 设置 service 实现类父类       | com.baomidou.global.BaseServiceImpl |
| convertServiceFileName(ConverterFileName)     | 转换 service 接口文件名称     |                                     |
| convertServiceImplFileName(ConverterFileName) | 转换 service 实现类文件名称   |                                     |
| formatServiceFileName(String)                 | 格式化 service 接口文件名称   |                                     |
| formatServiceImplFileName(String)             | 格式化 service 实现类文件名称 |                                     |

```java
new StrategyConfig.Builder()
    .serviceBuilder()
    .superServiceClass(BaseService.class)
    .superServiceImplClass(BaseServiceImpl.class)
    .formatServiceFileName("%sService")
    .formatServiceImplFileName("%sServiceImp")
    .build();
```

#### Mapper策略配置

| 方法                                     | 说明                      | 示例                           |
| ---------------------------------------- | ------------------------- | ------------------------------ |
| superClass(Class<?>)                     | 设置父类                  | BaseMapper.class               |
| superClass(String)                       | 设置父类                  | com.baomidou.global.BaseMapper |
| enableMapperAnnotation                   | 开启 @Mapper 注解         | 默认值:false                   |
| enableBaseResultMap                      | 启用 BaseResultMap 生成   | 默认值:false                   |
| enableBaseColumnList                     | 启用 BaseColumnList       | 默认值:false                   |
| cache(Class<? extends Cache>)            | 设置缓存实现类            | MyMapperCache.class            |
| convertMapperFileName(ConverterFileName) | 转换 mapper 类文件名称    |                                |
| convertXmlFileName(ConverterFileName)    | 转换 xml 文件名称         |                                |
| formatMapperFileName(String)             | 格式化 mapper 文件名称    |                                |
| formatXmlFileName(String)                | 格式化 xml 实现类文件名称 |                                |

```java
new StrategyConfig.Builder()
    .mapperBuilder()
    .superClass(BaseMapper.class)
    .enableMapperAnnotation()
    .enableBaseResultMap()
    .enableBaseColumnList()
    .cache(MyMapperCache.class)
    .formatMapperFileName("%sDao")
    .formatXmlFileName("%sXml")
    .build();
```

# MyBatisPlusCRUD接口

## Service CRUD 接口

### 说明

- 通用 Service CRUD 封装[IService (opens new window)](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/service/IService.java)接口，进一步封装 CRUD 采用 `get 查询单行` `remove 删除` `list 查询集合` `page 分页` 前缀命名方式区分 `Mapper` 层避免混淆，
- 泛型 `T` 为任意实体对象
- 建议如果存在自定义通用 Service 方法的可能，请创建自己的 `IBaseService` 继承 `Mybatis-Plus` 提供的基类
- 对象 `Wrapper` 为 [条件构造器](https://baomidou.com/guide/wrapper.html)

### Save

```java
// 插入一条记录（选择字段，策略插入）
boolean save(T entity);
// 插入（批量）
boolean saveBatch(Collection<T> entityList);
// 插入（批量）
boolean saveBatch(Collection<T> entityList， int batchSize);
```

#### 参数说明

|     类型      |   参数名   |     描述     |
| :-----------: | :--------: | :----------: |
|       T       |   entity   |   实体对象   |
| Collection<T> | entityList | 实体对象集合 |
|      int      | batchSize  | 插入批次数量 |

### SaveOrUpdate

```java
// TableId 注解存在更新记录，否插入一条记录
boolean saveOrUpdate(T entity);
// 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
boolean saveOrUpdate(T entity， Wrapper<T> updateWrapper);
// 批量修改插入
boolean saveOrUpdateBatch(Collection<T> entityList);
// 批量修改插入
boolean saveOrUpdateBatch(Collection<T> entityList， int batchSize);
```

##### [#](https://baomidou.com/guide/crud-interface.html#参数说明-2)参数说明

|     类型      |    参数名     |               描述               |
| :-----------: | :-----------: | :------------------------------: |
|       T       |    entity     |             实体对象             |
|  Wrapper<T>   | updateWrapper | 实体对象封装操作类 UpdateWrapper |
| Collection<T> |  entityList   |           实体对象集合           |
|      int      |   batchSize   |           插入批次数量           |

### Remove

```java
// 根据 entity 条件，删除记录
boolean remove(Wrapper<T> queryWrapper);
// 根据 ID 删除
boolean removeById(Serializable id);
// 根据 columnMap 条件，删除记录
boolean removeByMap(Map<String， Object> columnMap);
// 删除（根据ID 批量删除）
boolean removeByIds(Collection<? extends Serializable> idList);
```

#### 参数说明

|                类型                |    参数名    |          描述           |
| :--------------------------------: | :----------: | :---------------------: |
|             Wrapper<T>             | queryWrapper | 实体包装类 QueryWrapper |
|            Serializable            |      id      |         主键ID          |
|        Map<String， Object>        |  columnMap   |     表字段 map 对象     |
| Collection<? extends Serializable> |    idList    |       主键ID列表        |

### Update

```java
// 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
boolean update(Wrapper<T> updateWrapper);
// 根据 whereWrapper 条件，更新记录
boolean update(T updateEntity， Wrapper<T> whereWrapper);
// 根据 ID 选择修改
boolean updateById(T entity);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList);
// 根据ID 批量更新
boolean updateBatchById(Collection<T> entityList， int batchSize);
```

#### 参数说明

|     类型      |    参数名     |               描述               |
| :-----------: | :-----------: | :------------------------------: |
|  Wrapper<T>   | updateWrapper | 实体对象封装操作类 UpdateWrapper |
|       T       |    entity     |             实体对象             |
| Collection<T> |  entityList   |           实体对象集合           |
|      int      |   batchSize   |           更新批次数量           |

### Get

```java
// 根据 ID 查询
T getById(Serializable id);
// 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
T getOne(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
T getOne(Wrapper<T> queryWrapper， boolean throwEx);
// 根据 Wrapper，查询一条记录
Map<String， Object> getMap(Wrapper<T> queryWrapper);
// 根据 Wrapper，查询一条记录
<V> V getObj(Wrapper<T> queryWrapper， Function<? super Object， V> mapper);
```

#### 参数说明

|             类型             |    参数名    |              描述               |
| :--------------------------: | :----------: | :-----------------------------: |
|         Serializable         |      id      |             主键ID              |
|          Wrapper<T>          | queryWrapper | 实体对象封装操作类 QueryWrapper |
|           boolean            |   throwEx    |   有多个 result 是否抛出异常    |
|              T               |    entity    |            实体对象             |
| Function<? super Object， V> |    mapper    |            转换函数             |

### List

```java
// 查询所有
List<T> list();
// 查询列表
List<T> list(Wrapper<T> queryWrapper);
// 查询（根据ID 批量查询）
Collection<T> listByIds(Collection<? extends Serializable> idList);
// 查询（根据 columnMap 条件）
Collection<T> listByMap(Map<String， Object> columnMap);
// 查询所有列表
List<Map<String， Object>> listMaps();
// 查询列表
List<Map<String， Object>> listMaps(Wrapper<T> queryWrapper);
// 查询全部记录
List<Object> listObjs();
// 查询全部记录
<V> List<V> listObjs(Function<? super Object， V> mapper);
// 根据 Wrapper 条件，查询全部记录
List<Object> listObjs(Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录
<V> List<V> listObjs(Wrapper<T> queryWrapper， Function<? super Object， V> mapper);
```

#### 参数说明

|                类型                |    参数名    |              描述               |
| :--------------------------------: | :----------: | :-----------------------------: |
|             Wrapper<T>             | queryWrapper | 实体对象封装操作类 QueryWrapper |
| Collection<? extends Serializable> |    idList    |           主键ID列表            |
|       Map<?String， Object>        |  columnMap   |         表字段 map 对象         |
|    Function<? super Object， V>    |    mapper    |            转换函数             |

### Page

```java
// 无条件分页查询
IPage<T> page(IPage<T> page);
// 条件分页查询
IPage<T> page(IPage<T> page， Wrapper<T> queryWrapper);
// 无条件分页查询
IPage<Map<String， Object>> pageMaps(IPage<T> page);
// 条件分页查询
IPage<Map<String， Object>> pageMaps(IPage<T> page， Wrapper<T> queryWrapper);
```

#### 参数说明

|    类型    |    参数名    |              描述               |
| :--------: | :----------: | :-----------------------------: |
|  IPage<T>  |     page     |            翻页对象             |
| Wrapper<T> | queryWrapper | 实体对象封装操作类 QueryWrapper |

### Count

```java
// 查询总记录数
int count();
// 根据 Wrapper 条件，查询总记录数
int count(Wrapper<T> queryWrapper);
```

#### 参数说明

|    类型    |    参数名    |              描述               |
| :--------: | :----------: | :-----------------------------: |
| Wrapper<T> | queryWrapper | 实体对象封装操作类 QueryWrapper |

### Chain

#### query

```java
// 链式查询 普通
QueryChainWrapper<T> query();
// 链式查询 lambda 式。注意：不支持 Kotlin
LambdaQueryChainWrapper<T> lambdaQuery(); 

// 示例：
query().eq("column"， value).one();
lambdaQuery().eq(Entity::getId， value).list();
```

#### update

```java
// 链式更改 普通
UpdateChainWrapper<T> update();
// 链式更改 lambda 式。注意：不支持 Kotlin 
LambdaUpdateChainWrapper<T> lambdaUpdate();

// 示例：
update().eq("column"， value).remove();
lambdaUpdate().eq(Entity::getId， value).update(entity);
```

## Mapper CRUD 接口

说明：

- 通用 CRUD 封装[BaseMapper (opens new window)](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-core/src/main/java/com/baomidou/mybatisplus/core/mapper/BaseMapper.java)接口，为 `Mybatis-Plus` 启动时自动解析实体表关系映射转换为 `Mybatis` 内部对象注入容器
- 泛型 `T` 为任意实体对象
- 参数 `Serializable` 为任意类型主键 `Mybatis-Plus` 不推荐使用复合主键约定每一张表都有自己的唯一 `id` 主键
- 对象 `Wrapper` 为 [条件构造器](https://baomidou.com/guide/wrapper.html)

### Insert

```java
// 插入一条记录
int insert(T entity);
```

#### 参数说明

| 类型 | 参数名 |   描述   |
| :--: | :----: | :------: |
|  T   | entity | 实体对象 |

### Delete

```java
// 根据 entity 条件，删除记录
int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);
// 删除（根据ID 批量删除）
int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
// 根据 ID 删除
int deleteById(Serializable id);
// 根据 columnMap 条件，删除记录
int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String， Object> columnMap);
```

#### 参数说明

|                类型                |  参数名   |                描述                |
| :--------------------------------: | :-------: | :--------------------------------: |
|             Wrapper<T>             |  wrapper  | 实体对象封装操作类（可以为 null）  |
| Collection<? extends Serializable> |  idList   | 主键ID列表(不能为 null 以及 empty) |
|            Serializable            |    id     |               主键ID               |
|        Map<String， Object>        | columnMap |          表字段 map 对象           |

### Update

```java
// 根据 whereWrapper 条件，更新记录
int update(@Param(Constants.ENTITY) T updateEntity， @Param(Constants.WRAPPER) Wrapper<T> whereWrapper);
// 根据 ID 修改
int updateById(@Param(Constants.ENTITY) T entity);
```

#### 参数说明

|    类型    |    参数名     |                             描述                             |
| :--------: | :-----------: | :----------------------------------------------------------: |
|     T      |    entity     |               实体对象 (set 条件值，可为 null)               |
| Wrapper<T> | updateWrapper | 实体对象封装操作类（可以为 null，里面的 entity 用于生成 where 语句） |

### Select

```java
// 根据 ID 查询
T selectById(Serializable id);
// 根据 entity 条件，查询一条记录
T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 查询（根据ID 批量查询）
List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
// 根据 entity 条件，查询全部记录
List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 查询（根据 columnMap 条件）
List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String， Object> columnMap);
// 根据 Wrapper 条件，查询全部记录
List<Map<String， Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录。注意： 只返回第一个字段的值
List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

// 根据 entity 条件，查询全部记录（并翻页）
IPage<T> selectPage(IPage<T> page， @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询全部记录（并翻页）
IPage<Map<String， Object>> selectMapsPage(IPage<T> page， @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
// 根据 Wrapper 条件，查询总记录数
Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
```

#### 参数说明

|                类型                |    参数名    |                   描述                   |
| :--------------------------------: | :----------: | :--------------------------------------: |
|            Serializable            |      id      |                  主键ID                  |
|             Wrapper<T>             | queryWrapper |    实体对象封装操作类（可以为 null）     |
| Collection<? extends Serializable> |    idList    |    主键ID列表(不能为 null 以及 empty)    |
|        Map<String， Object>        |  columnMap   |             表字段 map 对象              |
|             IPage\<T>              |     page     | 分页查询条件（可以为 RowBounds.DEFAULT） |

## mapper 层 选装件

### 说明

选装件位于 `com.baomidou.mybatisplus.extension.injector.methods` 包下 需要配合[Sql 注入器](https://baomidou.com/guide/sql-injector.html)使用，[案例(opens new window)](https://gitee.com/baomidou/mybatis-plus-samples/tree/master/mybatis-plus-sample-sql-injector)
使用详细见[源码注释(opens new window)](https://gitee.com/baomidou/mybatis-plus/tree/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/injector/methods)。

[#](https://baomidou.com/guide/crud-interface.html#alwaysupdatesomecolumnbyid)[AlwaysUpdateSomeColumnById(opens new window)](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/injector/methods/AlwaysUpdateSomeColumnById.java) 

```java
int alwaysUpdateSomeColumnById(T entity);
```

[#](https://baomidou.com/guide/crud-interface.html#insertbatchsomecolumn)[insertBatchSomeColumn(opens new window)](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/injector/methods/InsertBatchSomeColumn.java) 

```java
int insertBatchSomeColumn(List<T> entityList);
```

[#](https://baomidou.com/guide/crud-interface.html#logicdeletebyidwithfill)[logicDeleteByIdWithFill(opens new window)](https://gitee.com/baomidou/mybatis-plus/blob/3.0/mybatis-plus-extension/src/main/java/com/baomidou/mybatisplus/extension/injector/methods/LogicDeleteByIdWithFill.java) 

```java
int logicDeleteByIdWithFill(T entity);
```

# MyBatisPlus条件构造器

## 说明

- 以下出现的第一个入参**boolean condition**表示该条件**是否**加入最后生成的sql中

  - 例如：

  ```java
  query.like(StringUtils.isNotBlank(name), Entity::getName， name).eq(age!=null && age >= 0, Entity::getAge, age)
  ```

- 以下代码块内的多个方法均为从上往下补全个别 **boolean** 类型的入参，默认为 **true**

- 以下出现的泛型 **Param** 均为 **Wrapper** 的**子类实例**（均具有AbstractWrapper的所有方法）

- 以下方法在入参中出现的**R**为泛型，在普通wrapper中是**String**，在LambdaWrapper中是**函数**（例:Entity::getId，Entity为**实体类**，getId为字段id的**getMethod**）

- 以下方法入参中的 **R column** 均表示数据库字段，当R具体类型为String时则为数据库字段名（**字段名是数据库关键字的自己用转义符包裹!**）而不是实体类数据字段名!!!，另当 R 具体类型为 SFunction 时项目runtime不支持eclipse自家的编译器!!!

- 以下举例均为使用普通wrapper，入参为 **Map** 和 **List** 的均以 **json** 形式表现!

- 使用中如果入参的 **Map** 或者 **List** 为**空**，则不会加入最后生成的sql中!!!

## 警告

不支持以及不赞成在 RPC 调用中把 Wrapper 进行传输

1. wrapper 很重
2. 传输 wrapper 可以类比为你的 controller 用 map 接收值(开发一时爽,维护火葬场)
3. 正确的 RPC 调用姿势是写一个 DTO 进行传输,被调用方再根据 DTO 执行相应的操作
4. 我们拒绝接受任何关于 RPC 传输 Wrapper 报错相关的 issue 甚至 pr

## AbstractWrapper

### 说明

QueryWrapper(LambdaQueryWrapper) 和 UpdateWrapper(LambdaUpdateWrapper) 的父类
用于生成 sql 的 where 条件, entity 属性也用于生成 sql 的 where 条件
注意：：entity 生成的 where 条件与 使用各个 api 生成的 where 条件**没有任何关联行为**

### allEq

#### 描述

- 全部eq（或个别isNull）

```java
allEq(Map<R, V> params)
allEq(Map<R, V> params, boolean null2IsNull)
allEq(boolean condition, Map<R, V> params, boolean null2IsNull)
```

#### 个别参数说明

- **params** ： key为数据库字段名，value为字段值
- **null2IsNull** ： 为true则在map的value为null时调用 isNull 方法，为false时则忽略value为null的

- 例1: allEq({id:1,name:"老王",age:null}) ---> id = 1 and name = '老王' and age is null
- 例2: allEq({id:1,name:"老王",age:null}, false) ---> id = 1 and name = '老王'

```java
allEq(BiPredicate<R, V> filter, Map<R, V> params)
allEq(BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull)
allEq(boolean condition, BiPredicate<R, V> filter, Map<R, V> params, boolean null2IsNull) 
```

#### 个别参数说明

- **filter** ： 过滤函数,是否允许字段传入比对条件中
- **params** 与 null2IsNull ： 同上

- 例1: allEq((k,v) -> k.indexOf("a") >= 0, {id:1,name:"老王",age:null})  --->  name = '老王' and age is null
- 例2: allEq((k,v) -> k.indexOf("a") >= 0, {id:1,name:"老王",age:null}, false)  --->  name = '老王'

### eq

#### 描述

- 等于 =

```java
eq(R column, Object val)
eq(boolean condition, R column, Object val)
```

- 例： eq("name", "老王")  --->  name = '老王'

### ne

#### 描述

- 不等于 <>

```java
ne(R column, Object val)
ne(boolean condition, R column, Object val)
```

- 例： `ne("name", "老王")`--->`name <> '老王'`

### gt

#### 描述

- 大于 >

```java
gt(R column, Object val)
gt(boolean condition, R column, Object val)
```

- 例： gt("age", 18)  --->  age > 18

### ge

#### 描述

- 大于等于 >=

```java
ge(R column, Object val)
ge(boolean condition, R column, Object val)
```

- 例： ge("age", 18)  --->  age >= 18

### lt

#### 描述

-  小于 <

```java
lt(R column, Object val)
lt(boolean condition, R column, Object val)
```

- 例： lt("age", 18)  --->  age < 18

### le

#### 描述

- 小于等于 <=

```java
le(R column, Object val)
le(boolean condition, R column, Object val)
```

- 例： le("age", 18)  --->  age <= 18

### between

#### 描述

- BETWEEN 值1 AND 值2

```java
between(R column, Object val1, Object val2)
between(boolean condition, R column, Object val1, Object val2)
```

- 例： between("age", 18, 30)  --->  age between 18 and 30

### notBetween

#### 描述

-  NOT BETWEEN 值1 AND 值2

```java
notBetween(R column, Object val1, Object val2)
notBetween(boolean condition, R column, Object val1, Object val2)
```

- 例： notBetween("age", 18, 30)  --->  age not between 18 and 30

### like

#### 描述

- LIKE '%值%'

```java
like(R column, Object val)
like(boolean condition, R column, Object val)
```

- 例： like("name", "王")  --->  name like '%王%'

### notLike

#### 描述

- NOT LIKE '%值%'

```java
notLike(R column, Object val)
notLike(boolean condition, R column, Object val)
```

- 例： notLike("name", "王")--->name not like '%王%'

### likeLeft

#### 描述

- LIKE '%值'

```java
likeLeft(R column, Object val)
likeLeft(boolean condition, R column, Object val)
```

- 例： likeLeft("name", "王")  --->  name like '%王'

### likeRight

#### 描述

- LIKE '值%'

```java
likeRight(R column, Object val)
likeRight(boolean condition, R column, Object val)
```

- 例： likeRight("name", "王")  --->  name like '王%'

### isNull

#### 描述

-  字段 IS NULL

```java
isNull(R column)
isNull(boolean condition, R column)
```

- 例： isNull("name")  --->  name is null

### isNotNull

#### 描述

- 字段 IS NOT NULL

```java
isNotNull(R column)
isNotNull(boolean condition, R column)
```

- 例： isNotNull("name")  --->  name is not null

### in

#### 描述

- 字段 IN (value.get(0), value.get(1), ...)

```java
in(R column, Collection<?> value)
in(boolean condition, R column, Collection<?> value)
```

- 例： in("age",{1,2,3})  --->  age in (1,2,3)

#### 描述

- 字段 IN (v0, v1, ...)

```java
in(R column, Object... values)
in(boolean condition, R column, Object... values)
```

- 例： in("age", 1, 2, 3)  --->  age in (1,2,3)

### notIn

#### 描述

- 字段 NOT IN (value.get(0), value.get(1), ...)

```java
notIn(R column, Collection<?> value)
notIn(boolean condition, R column, Collection<?> value)
```

- 例： notIn("age",{1,2,3})  --->  age not in (1,2,3)

#### 描述

-  字段 NOT IN (v0, v1, ...)

```java
notIn(R column, Object... values)
notIn(boolean condition, R column, Object... values)
```

- 例： notIn("age", 1, 2, 3)  --->  age not in (1,2,3)

### inSql

描述

- 字段 IN ( sql语句 )

```java
inSql(R column, String inValue)
inSql(boolean condition, R column, String inValue)
```

- 例： inSql("age", "1,2,3,4,5,6")  --->  age in (1,2,3,4,5,6)
- 例： inSql("id", "select id from table where id < 3")  --->  id in (select id from table where id < 3)

### notInSql

#### 描述

- 字段 NOT IN ( sql语句 )

```java
notInSql(R column, String inValue)
notInSql(boolean condition, R column, String inValue)
```

- 例： notInSql("age", "1,2,3,4,5,6")  --->  age not in (1,2,3,4,5,6)
- 例： notInSql("id", "select id from table where id < 3")  --->  id not in (select id from table where id < 3)

### groupBy

#### 描述

- 分组：GROUP BY 字段, ...

```java
groupBy(R... columns)
groupBy(boolean condition, R... columns)
```

- 例： groupBy("id", "name")  --->  group by id,name

### orderByAsc

#### 描述

- 排序：ORDER BY 字段, ... ASC

```java
orderByAsc(R... columns)
orderByAsc(boolean condition, R... columns)
```

- 例： orderByAsc("id", "name")  --->  order by id ASC,name ASC

### orderByDesc

#### 描述

- 排序：ORDER BY 字段, ... DESC

```java
orderByDesc(R... columns)
orderByDesc(boolean condition, R... columns)
```

- 例： orderByDesc("id", "name")  --->  order by id DESC,name DESC

### orderBy

#### 描述

- 排序：ORDER BY 字段, ...

```java
orderBy(boolean condition, boolean isAsc, R... columns)
```

- 例： orderBy(true, true, "id", "name")  --->  order by id ASC,name ASC

### having

#### 描述

- HAVING ( sql语句 )

```java
having(String sqlHaving, Object... params)
having(boolean condition, String sqlHaving, Object... params)
```

- 例： having("sum(age) > 10")  --->  having sum(age) > 10
- 例： having("sum(age) > {0}", 11)  --->  having sum(age) > 11

### func

#### 描述

- func 方法(主要方便在出现if...else下调用不同方法能不断链)

```java
func(Consumer<Children> consumer)
func(boolean condition, Consumer<Children> consumer)
```

- 例： func(i -> if(true) {i.eq("id", 1)} else {i.ne("id", 1)})

### or

#### 描述

- 拼接 OR
  -  注意事项：主动调用or表示紧接着下一个方法不是用and连接!(不调用or则默认为使用and连接)

```java
or()
or(boolean condition)
```

- 例： eq("id",1).or().eq("name","老王")  --->  id = 1 or name = '老王'

#### 描述

- OR 嵌套

```java
or(Consumer<Param> consumer)
or(boolean condition, Consumer<Param> consumer)
```

- 例： or(i -> i.eq("name", "李白").ne("status", "活着"))  --->  or (name = '李白' and status <> '活着')

### and

#### 描述

- AND 嵌套

```java
and(Consumer<Param> consumer)
and(boolean condition, Consumer<Param> consumer)
```

- 例： and(i -> i.eq("name", "李白").ne("status", "活着"))  --->  and (name = '李白' and status <> '活着')

### nested

#### 描述

- 正常嵌套 不带 AND 或者 OR

```java
nested(Consumer<Param> consumer)
nested(boolean condition, Consumer<Param> consumer)
```

- 例： nested(i -> i.eq("name", "李白").ne("status", "活着"))  --->  (name = '李白' and status <> '活着')

### apply

#### 描述

- 拼接 sql

  注意事项：

  该方法可用于数据库**函数** 动态入参的 **params** 对应前面 **applySql** 内部的 **{index}** 部分。这样是不会有sql注入风险的，反之会有!

```java
apply(String applySql, Object... params)
apply(boolean condition, String applySql, Object... params)
```

- 例： apply("id = 1")  --->  id = 1
- 例： apply("date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")  --->  date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")
- 例： apply("date_format(dateColumn,'%Y-%m-%d') = {0}", "2008-08-08")  --->  date_format(dateColumn,'%Y-%m-%d') = '2008-08-08'")

### last

#### 描述

- 无视优化规则直接拼接到 sql 的最后

  注意事项：

  只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用

```java
last(String lastSql)
last(boolean condition, String lastSql)
```

- 例： last("limit 1")

### exists

#### 描述

- 拼接 EXISTS ( sql语句 )

```java
exists(String existsSql)
exists(boolean condition, String existsSql)
```

- 例： exists("select id from table where age = 1")--->exists (select id from table where age = 1)

### notExists

#### 描述

- 拼接 NOT EXISTS ( sql语句 )

```java
notExists(String notExistsSql)
notExists(boolean condition, String notExistsSql)
```

- 例： notExists("select id from table where age = 1")  --->  not exists (select id from table where age = 1)

## QueryWrapper

说明：

继承自 AbstractWrapper ，自身的内部属性 entity 也用于生成 where 条件 及 LambdaQueryWrapper，可以通过 new QueryWrapper().lambda() 方法获取

### select

#### 描述

- 设置查询字段

  说明：

  以下方法分为两类.
  第二类方法为：过滤查询字段(主键除外)，入参不包含 class 的调用前需要`wrapper`内的`entity`属性有值! 这两类方法重复调用以最后一次为准

```java
select(String... sqlSelect)
select(Predicate<TableFieldInfo> predicate)
select(Class<T> entityClass, Predicate<TableFieldInfo> predicate)
```

- 例： select("id", "name", "age")
- 例： select(i -> i.getProperty().startsWith("test"))

## UpdateWrapper

说明：

继承自 AbstractWrapper ,自身的内部属性 entity 也用于生成 where 条件及 LambdaUpdateWrapper, 可以通过 new UpdateWrapper().lambda() 方法获取!

### set

#### 描述

- SQL SET 字段

```java
set(String column, Object val)
set(boolean condition, String column, Object val)
```

- 例： set("name", "老李头")
- 例： set("name", "")  --->  数据库字段值变为**空字符串**
- 例： set("name", null)  --->  数据库字段值变为**null**

### setSql

#### 描述

- 设置 SET 部分 SQL

```java
setSql(String sql)
```

- 例： setSql("name = '老李头'")

### lambda

- 获取 LambdaWrapper
  在QueryWrapper中是获取LambdaQueryWrapper
  在UpdateWrapper中是获取LambdaUpdateWrapper

# MyBatisPlusSequence主键

**TIP**

**主键生成策略必须使用INPUT**

支持父类定义@KeySequence子类继承使用

支持主键类型指定(3.3.0开始自动识别主键类型)

内置支持：

- DB2KeyGenerator
- H2KeyGenerator
- KingbaseKeyGenerator
- OracleKeyGenerator
- PostgreKeyGenerator

如果内置支持不满足你的需求，可实现IKeyGenerator接口来进行扩展.

**例子**

```java
@KeySequence(value = "SEQ_ORACLE_STRING_KEY", clazz = String.class)
public class YourEntity {
    @TableId(value = "ID_STR", type = IdType.INPUT)
    private String idStr;
}
```

## Spring-Boot

### 方式一：使用配置类

```java
@Bean
public IKeyGenerator keyGenerator() {
    return new H2KeyGenerator();
}
```

### 方式二：通过MybatisPlusPropertiesCustomizer自定义

```java
@Bean
public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
    return plusProperties -> plusProperties.getGlobalConfig().getDbConfig().setKeyGenerator(new H2KeyGenerator());
}
```

## Spring

### 方式一： XML配置

```xml
<bean id="globalConfig" class="com.baomidou.mybatisplus.core.config.GlobalConfig">
   <property name="dbConfig" ref="dbConfig"/>
</bean>

<bean id="dbConfig" class="com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig">
   <property name="keyGenerator" ref="keyGenerator"/>
</bean>

<bean id="keyGenerator" class="com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator"/>
```

### 方式二：注解配置

```java
@Bean
public GlobalConfig globalConfig() {
	GlobalConfig conf = new GlobalConfig();
	conf.setDbConfig(new GlobalConfig.DbConfig().setKeyGenerator(new H2KeyGenerator()));
	return conf;
}
```

# MyBatisPlus自定义ID生成器

**TIP**

自3.3.0开始,默认使用雪花算法+UUID(不含中划线)

| 方法     | 主键生成策略                            | 主键类型            | 说明                                                         |
| -------- | --------------------------------------- | ------------------- | ------------------------------------------------------------ |
| nextId   | ASSIGN_ID，~~ID_WORKER，ID_WORKER_STR~~ | Long,Integer,String | 支持自动转换为String类型，但数值类型不支持自动转换，需精准匹配，例如返回Long，实体主键就不支持定义为Integer |
| nextUUID | ASSIGN_UUID，UUID                       | String              | 默认不含中划线的UUID生成                                     |

##  Spring-Boot

### 方式一：声明为bean供spring扫描注入

```java
@Component
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
      	//可以将当前传入的class全类名来作为bizKey,或者提取参数来生成bizKey进行分布式Id调用生成.
      	String bizKey = entity.getClass().getName();
        //根据bizKey调用分布式ID生成
        long id = ....;
      	//返回生成的id值即可.
        return id;
    }
}
```

### 方式二：使用配置类

```java
@Bean
public IdentifierGenerator idGenerator() {
    return new CustomIdGenerator();
}
```

### 方式三：通过MybatisPlusPropertiesCustomizer自定义

```java
@Bean
public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
    return plusProperties -> plusProperties.getGlobalConfig().setIdentifierGenerator(new CustomIdGenerator());
}
```

## Spring

### 方式一： XML配置

```xml
<bean name="customIdGenerator" class="com.baomidou.samples.incrementer.CustomIdGenerator"/>

<bean id="globalConfig" class="com.baomidou.mybatisplus.core.config.GlobalConfig">
		<property name="identifierGenerator" ref="customIdGenerator"/>
</bean>
```

### 方式二：注解配置

```java
@Bean
public GlobalConfig globalConfig() {
	GlobalConfig conf = new GlobalConfig();
	conf.setIdentifierGenerator(new CustomIdGenerator());
	return conf;
}
```

# MyBatisPlus插件

## 分页插件

```xml
<!-- spring xml 方式 -->
<property name="plugins">
    <array>
        <bean class="com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor">
            <property name="sqlParser" ref="自定义解析类、可以没有"/>
            <property name="dialectClazz" value="自定义方言类、可以没有"/>
            <!-- COUNT SQL 解析.可以没有 -->
            <property name="countSqlParser" ref="countSqlParser"/>
        </bean>
    </array>
</property>

<bean id="countSqlParser" class="com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize">
    <!-- 设置为 true 可以优化部分 left join 的sql -->
    <property name="optimizeJoin" value="true"/>
</bean>
```

```java
// Spring boot方式
@Configuration
@MapperScan("com.baomidou.cloud.service.*.mapper*")
public class MybatisPlusConfig {

    // 旧版
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        // paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }

    // 最新版
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

}
```

### XML 自定义分页

- UserMapper.java 方法内容

```java
public interface UserMapper {//可以继承或者不继承BaseMapper
    /**
     * <p>
     * 查询 ： 根据state状态查询用户列表，分页显示
     * </p>
     *
     * @param page 分页对象,xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
     * @param state 状态
     * @return 分页对象
     */
    IPage<User> selectPageVo(Page<?> page, Integer state);
}
```

- UserMapper.xml 等同于编写一个普通 list 查询，mybatis-plus 自动替你分页

```xml
<select id="selectPageVo" resultType="com.baomidou.cloud.entity.UserVo">
    SELECT id,name FROM user WHERE state=#{state}
</select>
```

- UserServiceImpl.java 调用分页方法

```java
public IPage<User> selectUserPage(Page<User> page, Integer state) {
    // 不进行 count sql 优化，解决 MP 无法自动优化 SQL 问题，这时候你需要自己查询 count 部分
    // page.setOptimizeCountSql(false);
    // 当 total 为小于 0 或者设置 setSearchCount(false) 分页插件不会进行 count 查询
    // 要点!! 分页返回的对象与传入的对象是同一个
    return userMapper.selectPageVo(page, state);
}
```

# MyBatisPlus扩展

## 逻辑删除

### 说明

只对自动注入的sql起效：

- 插入： 不作限制
- 查找： 追加where条件过滤掉已删除数据，且使用 wrapper.entity 生成的where条件会忽略该字段
- 更新： 追加where条件防止更新到已删除数据，且使用 wrapper.entity 生成的where条件会忽略该字段
- 删除： 转变为 更新

### 例如

- 删除： `update user set deleted=1 where id = 1 and deleted=0` 
- 查找： `select id,name,deleted from user where deleted=0` 

字段类型支持说明：

- 支持所有数据类型(推荐使用 `Integer`,`Boolean`,`LocalDateTime`) 
- 如果数据库字段使用`datetime`，逻辑未删除值和已删除值支持配置为字符串`null`，另一个值支持配置为函数来获取值如`now()` 

### 附录

- 逻辑删除是为了方便数据恢复和保护数据本身价值等等的一种方案，但实际就是删除。
- 如果你需要频繁查出来看就不应使用逻辑删除，而是以一个状态去表示。
- 假删除、逻辑删除：并不会真正的从数据库中将数据删除掉，而是将当前被删除的这条数据中的一个**逻辑删除字段置为删除状态**。

### 使用方法

步骤1：配置`com.baomidou.mybatisplus.core.config.GlobalConfig$DbConfig`

- 例： application.yml

- ```java
  mybatis-plus:
    global-config:
      db-config:
        logic-delete-field: flag  # 全局逻辑删除的实体字段名(since 3.3.0,配置后可以忽略不配置步骤2)
        logic-delete-value: 1 # 逻辑已删除值(默认为 1)
        logic-not-delete-value: 0 # 逻辑未删除值(默认为 0)
  ```

步骤2：实体类字段上加上@TableLogic注解

```java
@TableLogic
private Integer deleted;
```

### 常见问题

#### 1. 如何 insert ?

> 1. 字段在数据库定义默认值(推荐)
> 2. insert 前自己 set 值
> 3. 使用自动填充功能

#### 2. 删除接口自动填充功能失效

> 1. 使用 `update` 方法并： `UpdateWrapper.set(column, value)`(推荐)
> 2. 使用 `update` 方法并： `UpdateWrapper.setSql("column=value")`
> 3. 使用[Sql注入器](https://baomidou.com/guide/sql-injector.html)注入`com.baomidou.mybatisplus.extension.injector.methods.LogicDeleteByIdWithFill`并使用(推荐)

## 通用枚举

解决了繁琐的配置，让 mybatis 优雅的使用枚举属性

> 自`3.1.0`开始，如果你无需使用原生枚举，可配置默认枚举来省略扫描通用枚举配置 [默认枚举配置](https://baomidou.com/config/#defaultEnumTypeHandler)
>
> - 升级说明：
>
>   `3.1.0` 以下版本改变了原生默认行为,升级时请将默认枚举设置为`EnumOrdinalTypeHandler`
>
> - 影响用户：
>
>   实体中使用原生枚举
>
> - 其他说明：
>
>   配置枚举包扫描的时候能提前注册使用注解枚举的缓存

### 声明通用枚举属性

方式一： 使用 **@EnumValue** 注解枚举属性

```java
public enum GradeEnum {

    PRIMARY(1, "小学"),  SECONDORY(2, "中学"),  HIGH(3, "高中");

    GradeEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }
    
	// 标记数据库存的值是code
    @EnumValue
    private final int code;
    // 。。。
}
```

方式二： 枚举属性，实现 IEnum 接口如下

```java
public enum AgeEnum implements IEnum<Integer> {
    ONE(1, "一岁"),
    TWO(2, "二岁"),
    THREE(3, "三岁");
    
    private int value;
    private String desc;
    
    @Override
    public Integer getValue() {
        return this.value;
    }
}
```

实体属性使用枚举类型

```java
public class User {
    /**
     * 名字
     * 数据库字段： name varchar(20)
     */
    private String name;
    
    /**
     * 年龄，IEnum接口的枚举处理
     * 数据库字段：age INT(3)
     */
    private AgeEnum age;
        
        
    /**
     * 年级，原生枚举（带{@link com.baomidou.mybatisplus.annotation.EnumValue})：
     * 数据库字段：grade INT(2)
     */
    private GradeEnum grade;
}
```

### 配置扫描通用枚举

- **注意**： spring mvc 配置参考，安装集成 MybatisSqlSessionFactoryBean 枚举包扫描，spring boot 例子配置如下：
- 配置文件 resources/application.yml

```text
mybatis-plus:
    # 支持统配符 * 或者 ; 分割
    typeEnumsPackage: com.baomidou.springboot.entity.enums
  ....
```

### 序列化枚举值为数据库存储值

#### Jackson

##### 一、重写toString方法

###### springboot

```java
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer(){
        return builder -> builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    }
```

###### jackson

```java
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
```

以上两种方式任选其一,然后在枚举中复写toString方法即可.

##### 二、注解处理

```java
public enum GradeEnum {

    PRIMARY(1, "小学"),  SECONDORY(2, "中学"),  HIGH(3, "高中");

    GradeEnum(int code, String descp) {
        this.code = code;
        this.descp = descp;
    }

    @EnumValue
  	@JsonValue	//标记响应json值
    private final int code;
}
```

#### Fastjson

##### 一、重写toString方法

###### 全局处理方式

```java
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteEnumUsingToString);
```

###### 局部处理方式

```java
		@JSONField(serialzeFeatures= SerializerFeature.WriteEnumUsingToString)
		private UserStatus status;
```

以上两种方式任选其一,然后在枚举中复写toString方法即可.

## 乐观锁

> 当要更新一条记录的时候，希望这条记录没有被别人更新
>
> 乐观锁实现方式：
>
> - 取出记录时，获取当前version
> - 更新时，带上这个version
> - 执行更新时， set version = newVersion where version = oldVersion
> - 如果version不对，就更新失败

### 乐观锁配置需要两步

#### 1.配置插件

spring xml方式:

```xml
<bean class="com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor" id="optimisticLockerInnerInterceptor"/>

<bean id="mybatisPlusInterceptor" class="com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor">
    <property name="interceptors">
        <list>
            <ref bean="optimisticLockerInnerInterceptor"/>
        </list>
    </property>
</bean>
```

spring boot注解方式:

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    return interceptor;
}
```

#### 2.在实体类的字段上加上`@Version`注解

```java
@Version
private Integer version;
```

说明

- **支持的数据类型只有：int,Integer,long,Long,Date,Timestamp,LocalDateTime**
- 整数类型下 `newVersion = oldVersion + 1`
- `newVersion` 会回写到 `entity` 中
- 仅支持 `updateById(id)` 与 `update(entity, wrapper)` 方法
- **在 `update(entity, wrapper)` 方法下, `wrapper` 不能复用!!!** 

```java
// Spring Boot 方式
@Configuration
@MapperScan("按需修改")
public class MybatisPlusConfig {
    /**
     * 旧版
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    
    /**
     * 新版
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
```

## 字段类型处理器

> 类型处理器
>
> - 用于 JavaType 与 JdbcType 之间的转换
> - 用于 PreparedStatement 设置参数值
> - 从 ResultSet 或 CallableStatement 中取出一个值
> - 本文讲解 mybaits-plus 内置常用类型处理器如何通过**TableField**注解快速注入到 mybatis 容器中。

- JSON 字段类型

```java
@Data
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class User {
    private Long id;

    ...

    /**
     * 注意！！ 必须开启映射注解
     *
     * @TableName(autoResultMap = true)
     *
     * 以下两种类型处理器，二选一 也可以同时存在
     *
     * 注意！！选择对应的 JSON 处理器也必须存在对应 JSON 解析依赖包
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    // @TableField(typeHandler = FastjsonTypeHandler.class)
    private OtherInfo otherInfo;

}
```

该注解对应了 XML 中写法为

```xml
<result column="other_info" 
        jdbcType="VARCHAR" 
        property="otherInfo" 
        typeHandler="com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler" 
/>
```

## 自动填充功能

**原理** 

- 实现元对象处理器接口：com.baomidou.mybatisplus.core.handlers.MetaObjectHandler
- 注解填充字段 `@TableField(.. fill = FieldFill.INSERT)` 生成器策略部分也可以配置！

```java
public class User {

    // 注意！这里需要标记为填充字段
    @TableField(.. fill = FieldFill.INSERT)
    private String fillField;

    ....
}
```

- 自定义实现类 MyMetaObjectHandler

```java
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
        // 或者
        this.strictInsertFill(metaObject, "createTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "createTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐)
        // 或者
        this.strictUpdateFill(metaObject, "updateTime", () -> LocalDateTime.now(), LocalDateTime.class); // 起始版本 3.3.3(推荐)
        // 或者
        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug)
    }
}
```

注意事项：

- 填充原理是直接给 **entity **的属性设置值!!!
- 注解则是指定该属性在对应情况下必有值，如果无值则入库会是 **null** 
- **MetaObjectHandler**提供的默认方法的策略均为：如果属性有值则不覆盖，如果填充值为**null**则不填充
- 字段必须声明**@TableField**注解，属性fill选择对应策略，该声明告知Mybatis-Plus需要预留注入SQL字段
- 填充处理器**MyMetaObjectHandler**在 Spring Boot 中需要声明**@Component**或**@Bean**注入
- 要想根据注解**FieldFill.xxx**和**字段名**以及**字段类型**来区分必须使用父类的**strictInsertFill**或者**strictUpdateFill**方法
- 不需要根据任何来区分可以使用父类的**fillStrategy**方法

```java
public enum FieldFill {
    /**
     * 默认不处理
     */
    DEFAULT,
    /**
     * 插入填充字段
     */
    INSERT,
    /**
     * 更新填充字段
     */
    UPDATE,
    /**
     * 插入和更新填充字段
     */
    INSERT_UPDATE
}
```

## Sql 注入器

注入器配置

全局配置 **sqlInjector** 用于注入 **ISqlInjector** 接口的子类，实现自定义方法注入。

- SQL 自动注入器接口 `ISqlInjector`

```java
public interface ISqlInjector {

    /**
     * <p>
     * 检查SQL是否注入(已经注入过不再注入)
     * </p>
     *
     * @param builderAssistant mapper 信息
     * @param mapperClass      mapper 接口的 class 对象
     */
    void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass);
}
```

自定义自己的通用方法可以实现接口 **ISqlInjector** 也可以继承抽象类 **AbstractSqlInjector** 注入通用方法 **SQL 语句** 然后继承 **BaseMapper** 添加自定义方法，全局配置 **sqlInjector** 注入 MP 会自动将类所有方法注入到 **mybatis** 容器中。



## 执行 SQL 分析打印

该功能依赖 **p6spy** 组件，完美的输出打印 SQL 及执行时长 3.1.0 以上版本

- p6spy 依赖引入

```xml
<dependency>
  <groupId>p6spy</groupId>
  <artifactId>p6spy</artifactId>
  <version>最新版本</version>
</dependency>
```

- application.yml 配置：

```yaml
spring:
  datasource:
    driver-class-name: com.p6spy.engine.spy.P6SpyDriver
    url: jdbc:p6spy:h2:mem:test
    ...
```

- spy.properties 配置：

```properties
#3.2.1以上使用
modulelist=com.baomidou.mybatisplus.extension.p6spy.MybatisPlusLogFactory,com.p6spy.engine.outage.P6OutageFactory
#3.2.1以下使用或者不配置
#modulelist=com.p6spy.engine.logging.P6LogFactory,com.p6spy.engine.outage.P6OutageFactory
# 自定义日志打印
logMessageFormat=com.baomidou.mybatisplus.extension.p6spy.P6SpyLogger
#日志输出到控制台
appender=com.baomidou.mybatisplus.extension.p6spy.StdoutLogger
# 使用日志系统记录 sql
#appender=com.p6spy.engine.spy.appender.Slf4JLogger
# 设置 p6spy driver 代理
deregisterdrivers=true
# 取消JDBC URL前缀
useprefix=true
# 配置记录 Log 例外,可去掉的结果集有error,info,batch,debug,statement,commit,rollback,result,resultset.
excludecategories=info,debug,result,commit,resultset
# 日期格式
dateformat=yyyy-MM-dd HH:mm:ss
# 实际驱动可多个
#driverlist=org.h2.Driver
# 是否开启慢SQL记录
outagedetection=true
# 慢SQL记录标准 2 秒
outagedetectioninterval=2
```

**注意** 

- driver-class-name 为 p6spy 提供的驱动类
- url 前缀为 jdbc:p6spy 跟着冒号为对应数据库连接地址
- 打印出sql为null,在excludecategories增加commit
- 批量操作不打印sql,去除excludecategories中的batch
- 批量操作打印重复的问题请使用MybatisPlusLogFactory (3.2.1新增）
- 该插件有性能损耗，不建议生产环境使用。

# 问题

## 1、忽略表中不存在的字段

使用@TableFiled(exit = false)忽略

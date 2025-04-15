> ver.2025.04.14.2

# 1、Spring Data JPA

## 1、ORM & JPA

在软件开发中，尤其是在企业级应用开发中，经常需要与数据库进行交互以持久化数据，传统上，开发者需要编写大量的 SQL 语句来完成这些操作，然而，面向对象编程范式与关系型数据库在数据表示和操作方式上存在差异，这种差异被称为“对象关系映射的阻抗失配”（Object-Relational Impedance Mismatch）

对象关系映射（Object-Relational Mapping，ORM）技术应运而生，其核心目标是在关系型数据库和面向对象编程语言之间建立一座桥梁，使得开发者可以使用面向对象的方式来操作数据库，而无需过多关注底层的 SQL 细节，ORM 框架负责将对象模型的数据自动转换（映射）为数据库中的表结构，并将数据库查询结果转换回对象

Java 持久化 API（Java Persistence API，JPA）是 Java EE 规范中关于对象持久化的标准，它定义了一组接口和注解，为 Java 开发者提供了一种统一的方式来管理关系型数据库中的数据，JPA 本身只是一种规范，需要具体的实现（Persistence Provider）来完成实际的数据库操作，目前流行的 JPA 实现包括 Hibernate、EclipseLink 和 Apache OpenJPA 等，使用 JPA 的好处在于它提供了一种标准化的方式来处理持久化，使得应用程序在不同的 JPA 实现之间具有一定的可移植性



## 2、优势与特点

Spring Data 项目是 Spring 生态系统中的一个重要组成部分，其目标是简化各种数据访问技术的开发

Spring Data JPA 是 Spring Data 的一个子项目，专门用于简化基于 JPA 的数据访问操作，它在 JPA 的基础上进行了进一步的抽象和封装，为开发者提供了更加便捷和高效的数据访问方式

Spring Data JPA 的主要优势和特点包括：

- **减少样板代码：**Spring Data JPA 能够自动生成大部分常用的数据访问代码，例如基本的 CRUD（创建、读取、更新、删除）操作，开发者只需要定义接口即可，无需编写具体的实现
- **自动 Repository 实现：**通过简单的接口定义和遵循特定的命名约定，Spring Data JPA 能够自动生成符合 JPA 规范的 Repository 接口实现，极大地减少了开发工作量
- **轻松集成 Spring 框架：**作为 Spring 生态系统的一部分，Spring Data JPA 可以无缝地与 Spring 框架的其他组件集成，例如依赖注入、事务管理等
- **支持多种数据源：**Spring Data JPA 构建在 JPA 规范之上，因此可以支持任何符合 JPA 规范的数据库
- **强大的查询能力：**Spring Data JPA 提供了多种查询方式，包括基于方法名的自动查询生成、JPQL（Java Persistence Query Language）查询、原生 SQL 查询以及基于 Specification 的动态查询
- **Repository 抽象**：Spring Data JPA 引入了 Repository 接口的概念，将数据访问逻辑与具体的持久化技术解耦，提高了代码的可测试性和可维护性
- **自动查询派生：**Spring Data JPA 可以根据 Repository 接口中定义的方法名自动生成对应的查询语句，这极大地简化了常用查询的实现
- **支持自定义查询：**对于更复杂的查询需求，Spring Data JPA 允许开发者使用 `@Query` 注解编写自定义的 JPQL 或原生 SQL 查询
- **审计功能：**Spring Data JPA 提供了方便的审计功能，可以自动记录实体的创建和修改时间等信息
- **事件处理机制：**Spring Data JPA 支持实体生命周期事件的监听，允许开发者在实体被持久化、更新或删除前后执行自定义的逻辑
- **与其他 Spring Data 模块集成：**Spring Data JPA 可以与其他 Spring Data 模块（如 Spring Data REST）无缝集成，从而快速构建 RESTful API



## 3、环境搭建与基本配置

### 1、环境搭建

在开始使用 Spring Data JPA 之前，需要搭建相应的开发环境并进行基本的配置，以下是环境搭建和基本配置的步骤：

1. **安装 Java 开发工具包（JDK）**：确保系统中安装了兼容的 JDK 版本，Spring Data JPA 通常需要 Java 8 或更高版本

2. **选择构建工具**：可以选择 Maven 或 Gradle 作为项目的构建工具，需要在系统中安装 Maven 并配置好环境变量

3. **集成开发环境（IDE）**：推荐使用 IntelliJ IDEA、Eclipse 或 Spring Tool Suite (STS) 等 IDE，它们提供了对 Spring Boot 和 Spring Data JPA 的良好支持

4. **添加 Spring Data JPA 依赖**：在 Maven 项目的 pom.xml 文件中，需要添加 Spring Data JPA 的依赖，通常情况下，会使用 Spring Boot Starter Data JPA 依赖，它包含了使用 Spring Data JPA 所需的所有核心依赖，包括 JPA 实现（默认是 Hibernate）、Spring Data JPA 核心库以及数据库连接池等

   ```XML
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
   ```

5. **配置数据源**：需要在 Spring Boot 的配置文件（通常是 application.properties 或 application.yml）中配置数据库连接的相关信息，例如数据库的 URL、用户名、密码以及数据库驱动等

   ```Properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_database?useSSL=false&serverTimezone=UTC
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   ```

6. **设置 JPA 提供者**：虽然 Spring Boot Starter Data JPA 默认使用 Hibernate 作为 JPA 的实现，但也可以根据需要选择其他的实现，例如 EclipseLink，如果需要进行 Hibernate 的特定配置，可以在配置文件中进行设置，例如配置数据库方言（dialect）以及是否显示 SQL 语句

   ```Properties
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   spring.jpa.show-sql=true
   spring.jpa.format-sql=true
   spring.jpa.hibernate.ddl-auto=update
   ```

   spring.jpa.hibernate.ddl-auto 属性用于控制 Hibernate 如何处理数据库 schema，update 表示如果 schema 与实体类的定义不一致，Hibernate 会尝试更新 schema

   其他常用的值包括 create（每次启动都创建新的 schema，并删除之前的数据）、create-drop（启动时创建 schema，关闭时删除 schema）和 none（不自动管理 schema）

7. **启用 Spring Data JPA Repositories**： 在 Spring Boot 应用的主类上添加 @EnableJpaRepositories 注解，以启用 Spring Data JPA 的 Repository 功能，这个注解会扫描指定的包路径（或者默认的包路径）下的 Repository 接口，并自动创建其实现

   ```Java
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
   
   @SpringBootApplication
   @EnableJpaRepositories
   public class DemoApplication {
       public static void main(String args) {
           SpringApplication.run(DemoApplication.class, args);
       }
   }
   ```

完成以上步骤后，基本的 Spring Data JPA 开发环境就搭建完成了，可以开始创建实体类和 Repository 接口，并利用 Spring Data JPA 提供的强大功能进行数据访问操作



### 2、应用示例

创建一个简单的示例应用，假设需要管理用户信息，包括用户的 ID、姓名和邮箱

1. **创建实体类 User**：首先，创建一个名为 User 的 Java 类，并使用 JPA 的注解来将其映射到数据库表

   ```Java
   import javax.persistence.Entity;
   import javax.persistence.GeneratedValue;
   import javax.persistence.GenerationType;
   import javax.persistence.Id;
   
   @Entity
   public class User {
   
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
   
       private String name;
       private String email;
   
       // 构造方法、Getter 和 Setter 方法省略
   }
   ```

   @Entity 注解表示这是一个 JPA 实体类，将会映射到数据库中的一个表（默认表名是类名，可以自定义）

   @Id 注解标记了实体类的主键字段

   @GeneratedValue 注解指定了主键的生成策略，GenerationType.IDENTITY 表示主键由数据库自动生成（例如 MySQL 的自增主键）

2. **定义 Repository 接口 UserRepository**：接下来，创建一个名为 UserRepository 的接口，并继承 JpaRepository 接口。

   ```Java
   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.stereotype.Repository;
   
   @Repository
   public interface UserRepository extends JpaRepository<User, Long> {
       // 可以添加自定义查询方法
   }
   ```

   JpaRepository<User, Long> 是 Spring Data JPA 提供的一个泛型接口，它继承自 CrudRepository 和 PagingAndSortingRepository，提供了基本的 CRUD 操作以及分页和排序功能

   <User, Long> 中的 User 指定了该 Repository 操作的实体类型，Long 指定了实体类主键的类型

   @Repository 注解将该接口标记为一个 Spring 管理的 Repository 组件

3. **创建 Spring Boot 应用来使用 UserRepository**：创建一个简单的 Spring Boot 应用，并注入 UserRepository 来进行数据操作

   ```Java
   import org.springframework.boot.CommandLineRunner;
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.context.annotation.Bean;
   
   @SpringBootApplication
   public class DemoApplication {
   
       public static void main(String args) {
           SpringApplication.run(DemoApplication.class, args);
       }
   
       @Bean
       public CommandLineRunner demo(UserRepository userRepository) {
           return (args) -> {
               // 创建并保存用户
               User user1 = new User();
               user1.setName("Alice");
               user1.setEmail("alice@example.com");
               userRepository.save(user1);
   
               User user2 = new User();
               user2.setName("Bob");
               user2.setEmail("bob@example.com");
               userRepository.save(user2);
   
               // 查询所有用户
               Iterable<User> users = userRepository.findAll();
               System.out.println("Users found with findAll():");
               users.forEach(user -> System.out.println(user));
   
               // 根据 ID 查询用户
               userRepository.findById(1L).ifPresent(user -> {
                   System.out.println("User found with findById(1L):");
                   System.out.println(user);
               });
   
               // 更新用户
               userRepository.findById(2L).ifPresent(user -> {
                   user.setEmail("bob.updated@example.com");
                   userRepository.save(user);
                   System.out.println("User updated:");
                   System.out.println(user);
               });
   
               // 删除用户
               userRepository.deleteById(1L);
               System.out.println("User deleted with deleteById(1L)");
   
               // 统计用户数量
               long count = userRepository.count();
               System.out.println("Number of users: " + count);
           };
       }
   }
   ```

   在这个示例中，CommandLineRunner 接口的 run 方法会在应用启动后执行，通过依赖注入获取了 UserRepository 的实例，并使用其提供的 save()、findAll()、findById()、deleteById() 和 count() 等方法进行了基本的 CRUD 操作

   

# 2、核心概念与基本操作

## 1、Repository 接口

在 Spring Data JPA 中，Repository 接口是数据访问的核心抽象，它是一个标记接口，用于指示 Spring Data JPA 为其提供实现

Spring Data JPA 提供了一些预定义的 Repository 接口，可以根据需求选择继承它们，从而获得相应的基本数据访问功能

主要的 Repository 接口包括：

- **Repository**： 这是最基本的接口，是一个空接口，用于标记一个接口为 Repository，它没有任何方法定义，主要用于类型识别
- **CrudRepository**： 继承自 Repository，提供了基本的 CRUD 操作方法，包括 save()、findById()、existsById()、findAll()、findAllById()、deleteById()、delete() 和 deleteAll() 等
- **PagingAndSortingRepository**： 继承自 CrudRepository，在 CRUD 操作的基础上增加了分页和排序的功能，提供了 findAll(Pageable pageable) 和 findAll(Sort sort) 等方法
- **JpaRepository**： 继承自 PagingAndSortingRepository，是 Spring Data JPA 提供的最常用的 Repository 接口，它除了包含 CrudRepository 和 PagingAndSortingRepository 的所有方法外，还提供了一些 JPA 特有的方法，例如 flush()、saveAndFlush()、deleteInBatch() 和 deleteAllInBatch() 等

通常会直接继承 JpaRepository，因为它提供了最全面的功能，在定义自己的 Repository 接口时，只需要继承这些预定义的接口，并指定操作的实体类型和主键类型即可，例如，UserRepository 继承了 JpaRepository<User, Long>，因此自动拥有了对 User 实体进行 CRUD 操作以及分页排序等功能的方法

除了使用 Spring Data JPA 提供的预定义接口外，还可以创建自己的自定义 Repository 接口，这样做的好处是可以根据具体的业务需求定义特定的数据访问方法，例如，如果需要根据用户的邮箱查询用户，可以在 UserRepository 接口中声明一个名为 findByEmail 的方法，Spring Data JPA 会根据这个方法名自动生成相应的查询逻辑，选择合适的 Repository 接口可以避免引入不必要的方法，使代码更加清晰和专注

| **Repository 接口**               | **功能描述**                                                 |
| --------------------------------- | ------------------------------------------------------------ |
| Repository                        | 最基本的标记接口，不提供任何方法                             |
| CrudRepository<T, ID>             | 提供了基本的 CRUD 操作方法，如 save()、findById()、existsById()、findAll()、deleteById()、delete()、deleteAll()、count() 等。T 是实体类型，ID 是主键类型 |
| PagingAndSortingRepository<T, ID> | 继承自 CrudRepository，增加了分页和排序的功能，提供了 findAll(Pageable pageable) 和 findAll(Sort sort) 等方法 |
| JpaRepository<T, ID>              | 继承自 PagingAndSortingRepository，是功能最全面的 Repository 接口，除了包含上述所有功能外，还提供了一些 JPA 特有的方法，如 flush()、saveAndFlush()、deleteInBatch()、deleteAllInBatch() 等 |



## 2、内置 CRUD 操作方法

继承了 CrudRepository 或其子接口（如 JpaRepository）的 Repository 会自动拥有许多内置的 CRUD 操作方法，这些方法极大地简化了基本的数据访问任务，开发者无需编写任何实现代码即可使用

以下是对 CrudRepository 中常用方法的详细解释：

- **save(S entity)**： 保存给定的实体，如果实体已经存在（根据其 ID 判断），则执行更新操作，如果实体不存在，则执行插入操作，返回保存后的实体
- **findById(ID id)**： 根据给定的 ID 查找实体，返回一个 Optional 对象，该对象可能包含找到的实体，也可能为空（如果不存在），使用 Optional 可以更好地处理实体不存在的情况，避免空指针异常
- **existsById(ID id)**： 根据给定的 ID 判断实体是否存在，返回一个布尔值
- **findAll()**： 查询所有实体，返回一个包含所有实体的 Iterable 集合
- **findAllById(Iterable<ID> ids)**： 根据给定的 ID 集合查询多个实体，返回一个包含找到的实体的 Iterable 集合
- **deleteById(ID id)**： 根据给定的 ID 删除实体
- **delete(T entity)**： 删除给定的实体
- **deleteAll(Iterable<? extends T> entities)**： 删除给定的实体集合
- **deleteAll()**： 删除所有实体
- **count()**： 返回实体的总数量

这些内置方法覆盖了绝大多数基本的数据库操作需求，例如，在 UserRepository 中，直接调用 userRepository.save(user) 来保存一个 User 对象，调用 userRepository.findById(1L) 来查找 ID 为 1 的用户，调用 userRepository.findAll() 来获取所有用户列表，等等



## 3、方法名定义查询

Spring Data JPA 最强大的特性之一是能够根据 Repository 接口中定义的方法名自动生成查询语句，这种机制遵循一套特定的命名规则，开发者只需要按照这些规则定义方法名，Spring Data JPA 就会自动解析方法名并生成相应的查询逻辑

自定义查询方法的方法名通常由以下几个部分组成：

- **查询关键字**：例如 findBy、existsBy、countBy、deleteBy 等，用于指示要执行的查询类型（查找、判断存在、计数、删除）
- **属性名**：指定要进行查询的实体类的属性名，如果需要根据嵌套属性进行查询，可以使用下划线 _ 来分隔属性名，例如，如果 User 实体有一个 Address 类型的属性，而 Address 又有一个 city 属性，那么可以使用 findByAddress_City 来根据城市查询用户
- **条件关键字**：用于指定查询的条件，例如 And、Or、Between、LessThan、GreaterThan、Like、IgnoreCase 等

以下是一些常用的方法命名规则示例：

- **根据属性精确查找**：findByEmail(String email) 会生成类似于 SELECT u FROM User u WHERE u.email = :email 的查询
- **根据属性模糊查找**：findByNameLike(String name) 会生成类似于 SELECT u FROM User u WHERE u.name LIKE :name 的查询
- **根据多个属性组合查找**：findByEmailAndName(String email, String name) 会生成类似于 SELECT u FROM User u WHERE u.email = :email AND u.name = :name 的查询
- **根据属性范围查找**：findByAgeBetween(int startAge, int endAge) 会生成类似于 SELECT u FROM User u WHERE u.age BETWEEN :startAge AND :endAge 的查询
- **判断属性是否存在**：existsByEmail(String email) 会生成类似于 SELECT count(u) > 0 FROM User u WHERE u.email = :email 的查询
- **统计符合条件的记录数**：countByName(String name) 会生成类似于 SELECT count(u) FROM User u WHERE u.name = :name 的查询
- **根据属性排序**：findAllByOrderByAgeAsc() 会查询所有用户并按年龄升序排序
- **忽略大小写查找**：findByNameIgnoreCase(String name) 会生成忽略大小写的查询

Spring Data JPA 的方法命名规则非常强大且灵活，能够满足大部分简单的查询需求，通过合理地命名 Repository 接口中的方法，可以避免编写大量的查询代码，从而提高开发效率，需要注意的是，对于更复杂的查询需求，方法命名规则可能会显得不够灵活，这时可以考虑使用其他查询方式，例如 JPQL 查询或 Criteria API



## 4、分页与排序

### 1、概述

在实际应用中，经常需要对查询结果进行分页和排序，以便更好地展示和处理大量数据，Spring Data JPA 提供了便捷的方式来实现这些功能。



### 2、分页

要实现分页查询，需要在 Repository 方法的参数中添加一个 org.springframework.data.domain.Pageable 类型的参数

Pageable 接口定义了分页的相关信息，例如当前页码、每页大小以及排序方式等。Spring Data JPA 会根据 Pageable 对象中的信息自动生成分页查询语句

例如，要在 UserRepository 中实现分页查询所有用户，可以定义如下方法：

```Java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}
```

在调用这个方法时，需要创建一个 Pageable 接口的实现类 org.springframework.data.domain.PageRequest 的实例，指定页码（从 0 开始）和每页大小

```Java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getAllUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pageable);
    }
}
```

findAll(Pageable pageable) 方法会返回一个 org.springframework.data.domain.Page 对象，该对象包含了当前页的数据以及分页的相关信息，例如总记录数、总页数等



### 3、排序

要实现排序，可以在 Repository 方法的参数中添加一个 org.springframework.data.domain.Sort 类型的参数

Sort 接口定义了排序的字段和排序的方向（升序或降序）

例如，要在 UserRepository 中实现按姓名升序排序查询所有用户，可以定义如下方法：

```Java
import org.springframework.data.domain.Sort;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll(Sort sort);
}
```

在调用这个方法时，需要创建一个 Sort 接口的实现类 org.springframework.data.domain.Sort.by() 的实例，指定排序的字段和方向

```Java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsersSortedByNameAsc() {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        return userRepository.findAll(sort);
    }
}
```



### 4、结合使用

也可以在同一个 Repository 方法中同时使用 Pageable 参数来实现分页和排序

```Java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);
}
```

在创建 PageRequest 对象时，可以同时指定页码、每页大小和排序方式

```Java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Page<User> getAllUsersWithPaginationAndSorting(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection.toUpperCase()), sortBy);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return userRepository.findAll(pageable);
    }
}
```

通过使用 Pageable 和 Sort 接口，Spring Data JPA 提供了一种非常灵活和方便的方式来处理数据的分页和排序需求，避免了手动编写复杂的 SQL 语句，在处理大量数据时，合理地使用分页和排序对于提高应用性能和用户体验至关重要



## 5、持久化上下文

### 1、概述

持久化上下文是 Java 持久化 API (JPA) 中至关重要的概念，它在应用程序的**对象模型**和**关系数据库**之间扮演着**桥梁**的角色

持久化上下文，由 javax.persistence.EntityManager 接口的实例管理，本质上是一个存储被管理实体实例的一级缓存，对于任何持久化实体标识，在给定的持久化上下文中都存在一个唯一的实体实例，这确保了在上下文中的对象标识，它充当事务回写缓存，延迟数据库同步直到必要时 。在同一个持久化上下文中，每个数据库记录只对应一个唯一的实体对象实例，这保证了对象引用的唯一性



**主要职责**：

- **身份标识（Identity）**：持久化上下文确保每个数据库记录在其中只对应一个唯一的实体对象实例 
- **一级缓存（First-Level Cache）**：它缓存已加载和创建的实体，减少在同一事务或工作单元中重复查询数据库的需求，一级缓存通过充当本地数据存储来提高性能，最大限度地减少当前操作中频繁访问的实体对数据库的访问，这对于读取密集型操作和相关实体访问尤其有益，当请求一个实体时，持久化上下文首先检查其缓存，如果找到，它将返回缓存的实例，从而避免了数据库查询的开销，这种缓存机制对于 JPA 应用程序的性能优化至关重要
- **事务感知（Transactional Awareness）**：持久化上下文的生命周期通常与事务相关联，确保上下文中的所有操作都是单个原子工作单元的一部分，事务感知保证了数据的一致性，在持久化上下文中对实体所做的更改只有在事务成功提交后才会持久化到数据库，如果发生故障则会回滚，与事务的关联确保了原子性，持久化上下文中所有更改要么全部应用于数据库，要么都不应用，从而维护了数据的完整性
- **延迟加载（Lazy Loading）**：它根据定义的获取策略管理相关实体的加载，通过仅加载必要的数据来提高性能，延迟加载通过延迟加载相关实体直到显式访问它们来优化资源利用，这避免了不必要的数据检索，特别是对于具有复杂关系的实体



**优势**：

- **减少数据库访问**：缓存最大限度地减少冗余数据库查询，从而提高应用程序性能
- **数据一致性**：确保在事务中，应用程序使用一致的数据视图
- **简化对象管理**：持久化上下文管理实体的状态和生命周期，使开发人员无需手动跟踪
- **自动脏检查（Automatic Dirty Checking）**：跟踪对被管理实体所做的更改，在事务提交时自动将这些更改同步到数据库，而无需显式的更新语句，自动脏检查通过消除手动状态管理和更新操作的需求来简化开发，JPA 提供程序（如 Hibernate）有效地跟踪实体修改并生成必要的 SQL 更新，当加载实体时，持久化上下文会捕获其状态快照，刷新时，它将当前状态与快照进行比较，并为任何已更改的属性生成更新语句



### 2、生命周期

**JPA 和 Spring Data JPA 环境中的创建和初始化**：

- 在传统的 JPA 中，持久化上下文通常通过 EntityManagerFactory 创建 EntityManager 时获取
- 在 Spring Data JPA 中，EntityManager 及其关联的持久化上下文的创建和管理主要由 Spring 容器处理，其中 @PersistenceContext 注解通常用于将 EntityManager 注入到 Spring 管理的组件中，Spring 通过抽象出 EntityManager 的创建和生命周期处理来简化持久化上下文的管理

**管理上下文中的实体：持久化、检索、更新和删除**：

- EntityManager 提供了与持久化上下文交互并在其中管理实体生命周期的 API，每个 EntityManager 操作（persist、find、merge、remove）都会影响关联的持久化上下文中实体的状态，使其在瞬态、被管理、分离和已删除状态之间转换
  - 通过 persist()（使瞬态实体成为被管理的）、find()（检索现有实体）等操作，实体被带入持久化上下文
  - 更新实体涉及在它们被持久化上下文中管理时修改其状态，这些更改会被自动跟踪
  - 使用 remove() 操作删除实体，该操作标记实体以便在事务提交时从数据库中删除

**与数据库同步：理解刷新操作和刷新模式（AUTO、COMMIT 和 Hibernate 特有的模式）**：

- flush() 操作将持久化上下文中被管理实体的状态与数据库同步，JPA 定义了两种标准的刷新模式： 

  - **AUTO（默认）**：在查询执行之前和事务提交之前自动刷新持久化上下文，以确保查询不会返回过时的数据

  - **COMMIT**： 仅在事务提交时刷新持久化上下文
  - **Hibernate 提供了额外的刷新模式**：如 ALWAYS（在每个查询之前刷新）和 MANUAL（仅在显式调用时刷新） 

- AUTO 模式通过确保在查询之前同步数据库来优先考虑数据一致性，COMMIT 模式可以通过减少刷新频率来提供性能优势，但如果管理不当可能会导致不一致

**终止阶段：上下文关闭和销毁后的实体状态**：

- 持久化上下文通常在关联的 EntityManager 关闭时关闭，也即在事务或请求生命周期结束时，当 EntityManager 关闭时，内存缓存（持久化上下文）将被丢弃，此缓存中的实体将失去其被管理状态并与数据库断开连接
- 上下文关闭后，被管理实体将变为分离状态，这意味着它们不再与持久化上下文关联，并且它们的状态更改不再被自动跟踪，要持久化对分离实体的更改，需要使用诸如 merge() 之类的操作将它们重新附加到新的持久化上下文



### 3、作用域管理

**请求范围 (Request Scope)**：

- **场景**：Web 应用
- **生命周期**：与 HTTP 请求绑定，请求开始时创建，结束后关闭
- **实现 (Spring)**：Spring 提供了两个主要组件来实现请求范围的持久化上下文：
  - **OpenEntityManagerInViewFilter**：作为Servlet过滤器工作，它在每个请求开始时打开一个EntityManager，并在请求完成后关闭它，这一机制使得在视图层（如 JSP、Thymeleaf 等）中依然能延迟加载关联数据（即懒加载）
  - **OpenEntityManagerInViewInterceptor**：类似功能的拦截器版本，通常与 Spring MVC 的 HandlerInterceptor 配合使用，同样目的是为每个 Web 请求提供一个独立的 EntityManager
- **优点**：每个请求独立，避免数据干扰

**事务范围 (Transaction Scope)**：

- **场景**：非 Web 应用或需细粒度控制
- **生命周期**：与事务绑定，事务开始时创建，结束后关闭，所有托管的实体进入游离状态
- **实现 (Spring)**：
  - Spring事务管理：当在方法上使用 @Transactional 注解时，Spring 会在事务开始时自动创建一个与当前事务绑定的EntityManager，其管理的持久化上下文在整个事务期间有效
  - 共享性：在同一事务范围内，所有通过 Spring Data JPA 操作的 Repository 方法调用都会共享相同的持久化上下文，这保证了在同一事务内，不同 Repository 之间对同一数据的读写操作能够保持数据一致性，同时还可以享受一级缓存带来的性能优化

**扩展的持久化上下文 (Extended Persistence Context)**：

- **场景**：特定有状态会话 Bean (Stateful Session Bean)，例如在需要跨越多个事务保持实体状态的长会话中，此种方式适用于那些需要在多个方法调用或事务中共享同一组实体对象（保持对象处于托管状态）的情况
- **生命周期**：扩展的持久化上下文的生命周期不再限于单个事务，而是跟随 stateful session bean 的生命周期，也就是说，一个 stateful Bean 在其整个生命周期内始终使用同一个持久化上下文，实体对象的状态可以跨越多个事务而保持
- **实现**：
  - 使用方式：在 EJB 中，通过将 EntityManager 注入时设置 @PersistenceContext(type = PersistenceContextType.EXTENDED) 来获得扩展的持久化上下文
  - 跨事务共享：不同于事务范围下每次事务结束后持久化上下文就会被关闭，扩展持久化上下文可以跨多个事务，在第一次调用时载入的实体状态能够在后续的事务中继续使用，不必重新从数据库加载
- **注意**：需谨慎使用，可能长期持有数据库连接和资源



### 4、与 EntityManager 的关系

**EntityManager 作为交互的主要接口**：EntityManager 是 JPA 中与持久化上下文交互的中心接口 。它提供了管理实体生命周期的方法（persist、merge、remove、find、refresh、detach）和查询实体的方法（createQuery、createNativeQuery） 。每个 EntityManager 实例都与恰好一个持久化上下文关联 。EntityManager 是持久化上下文的门户，提供了丰富的 API 来与被管理实体和底层数据库进行交互。开发人员主要使用 EntityManager 来执行数据访问操作。JPA 应用程序中所有对实体的操作都通过 EntityManager 进行调解。它充当应用程序代码和持久化上下文之间的中介，确保实体状态和数据库交互的正确管理



**影响持久化上下文的基本 EntityManager 操作**：

- persist()：使瞬态实体成为被管理的实体并将其添加到持久化上下文，计划将其插入到数据库中
- find()：按主键检索被管理的实体，如果该实体已在持久化上下文中，则从缓存中返回，否则，从数据库加载并添加到上下文中
- merge()：将分离实体的状态合并到当前的持久化上下文，返回一个新的被管理实例，在某些情况下，它也可以用于持久化新的实体
- remove()：标记一个被管理的实体以便在事务提交时从数据库中删除
- refresh()：从数据库重新加载被管理实体的状态，覆盖持久化上下文中进行的任何更改
- detach()：从持久化上下文中移除一个实体，使其变为分离状态



**EntityManagerFactory 在生命周期中的作用**：

- EntityManagerFactory 是一个工厂类，负责创建 EntityManager 实例，它通常在应用程序中只创建一次，并且是一个开销较大的操作，通常在应用程序启动时执行

- EntityManagerFactory 是线程安全的，而 EntityManager 实例则不是，并且旨在单个工作单元中使用
- EntityManagerFactory 充当 JPA 的引导机制，提供了创建和管理 EntityManager 实例的方法，它的生命周期与应用程序相关联，确保了高效的资源利用，EntityManagerFactory 就像连接池工厂，而 EntityManager 就像从池中获得的连接，创建工厂的开销很大，但从中创建单个实体管理器则相对轻量



### 5、Spring Data JPA 如何管理持久化上下文

自动管理：注入 JpaRepository 时，Spring 自动处理 EntityManager 的创建、注入和关闭

@Transactional 注解：

- 方法执行前创建（或获取）与当前事务关联的持久化上下文。
- 方法执行后根据事务结果（提交/回滚）刷新并关闭上下文。

Repository 方法：执行时自动获取当前线程关联的持久化上下文进行数据库操作



## 6、实体生命周期

### 1、概述

在 JPA 中，实体（Entity）不仅仅是一个普通的 Java 对象（POJO），当它与持久化上下文（Persistence Context）交互时，它会经历一个生命周期，并处于不同的状态，JPA 规范主要定义了以下四种状态：

1. **新创建 / 瞬时态 (New / Transient)**
2. **受管状态 (Managed)**
3. **游离态 / 分离态 (Detached)**
4. **已删除 / 移除态 (Removed)**



### 2、新创建 / 瞬时态

**定义**：一个刚刚通过 new 操作符实例化的对象，尚未与任何持久化上下文（Persistence Context）建立联系

**核心特征**：

- 不持有数据库生成的持久化标识（ID 通常为 null 或默认值，除非手动设置）
- 与数据库中的任何特定行都没有对应关系
- 对其进行的任何修改都不会被 JPA 自动追踪或同步到数据库
- 本质上是一个普通的、未被 JPA "察觉" 的 Java 对象

**如何进入此状态**：直接使用 new Entity() 创建实例时



### 3、受管状态

**定义**：实体实例当前正被一个**活动的持久化上下文**所管理，并且它具有一个数据库持久化标识（ID）

**核心特征**：

- 代表数据库中的某一行数据（或者在 persist 后，准备在 flush/commit 时插入数据库）
- **关键**：JPA 会**主动追踪**对此对象属性的任何更改，用于**脏检查 (Dirty Checking)**
- 在**事务提交**时，如果发生过更改（变“脏”），JPA 会自动生成 UPDATE 语句同步到数据库
- 支持 JPA 提供的特性，如**延迟加载 (Lazy Loading)**
- 在一个活动的持久化上下文中，对于同一个数据库记录，JPA 保证只存在一个受管实例（**一级缓存**）

**如何进入此状态**:

- 调用 entityManager.persist(newEntity) 将新实体持久化后
- 通过 entityManager.find(...) 或 entityManager.getReference(...) 从数据库加载后
- 执行 JPA 查询（如 JPQL 或 Criteria API）返回的实体对象
- 调用 entityManager.merge(detachedEntity) 后返回的那个实体实例
- （在 Spring Data JPA 中）通常是在 @Transactional 方法内，通过 Repository 的查询方法（findById, findAll, 自定义查询等）获取的实体，或 save() 新实体/合并游离实体后返回的结果



### 4、游离态 / 分离态

**定义**：实体实例**曾经**处于受管状态，但管理它的那个持久化上下文**已经关闭**或**不再对其进行管理**

**核心特征**:

- 通常拥有一个有效的数据库持久化标识（ID）
- 对象内部持有从数据库加载的数据
- **关键**：JPA **不再追踪**对此对象属性的更改，任何修改都不会被自动同步到数据库
- **关键**：如果尝试访问尚未初始化的延迟加载（Lazy）关联属性，会抛出 LazyInitializationException，因为已经没有活动的持久化上下文来加载数据了
- 可以看作是一个携带了数据库数据的普通 Java 对象

**如何进入此状态**:

- 包含它的持久化上下文被关闭（例如，Spring 中 @Transactional 方法执行完毕，或手动调用 entityManager.close()）
- 显式调用 entityManager.detach(entity) 将实体从上下文中分离
- 实体对象经过序列化和反序列化过程
- 调用 entityManager.clear() 清空持久化上下文后，其中的所有实体变为游离态



### 5、已删除 / 移除态

**定义**：实体实例仍然与一个活动的持久化上下文关联，但已经被明确标记为“待删除”

**核心特征**：

- 在事务提交（或 flush）之前，它技术上仍处于受管状态（被上下文追踪）
- 已被调度，将在事务提交时对应执行数据库的 DELETE 操作
- 一旦事务成功提交，数据库中对应的记录被删除，这个 Java 对象实例通常会转变为**游离态**（或瞬时态，取决于实现）

**如何进入此状态**：

- 对一个**受管状态 (Managed)** 的实体实例调用 entityManager.remove(managedEntity)



# 3、进阶查询技巧

## 1、JPQL 查询语言

Java Persistence Query Language (JPQL) 是一种基于实体模型的查询语言，它类似于 SQL，但操作的是实体类及其属性，而不是数据库表和列，JPQL 允许开发者以面向对象的方式查询数据库，从而避免了直接编写 SQL 语句带来的数据库依赖性问题

JPQL 的基本语法包括：

- **SELECT 子句**：用于指定要查询的实体或属性，例如，SELECT u FROM User u 查询所有 User 实体，可以使用 NEW 关键字创建新的对象，或者使用聚合函数（如 COUNT、AVG、SUM、MIN、MAX）
- **FROM 子句**：用于指定查询的根实体和别名。例如，FROM User u 表示查询 User 实体，并为其指定别名 u，可以进行连接查询（JOIN、LEFT JOIN、RIGHT JOIN、INNER JOIN）来查询关联的实体
- **WHERE 子句**：用于指定查询的条件，可以使用比较运算符（如 =、>、<、>=、<=、<>、BETWEEN、LIKE、IN、IS NULL、IS NOT NULL）、逻辑运算符（AND、OR、NOT）以及其他 JPQL 提供的函数
- **ORDER BY 子句**：用于指定查询结果的排序方式，可以使用 ASC 表示升序，DESC 表示降序
- **GROUP BY 子句**：用于对查询结果进行分组，通常与聚合函数一起使用
- **HAVING 子句**：用于在分组之后对结果进行过滤，类似于 SQL 中的 WHERE 子句，但用于分组后的结果

在 Spring Data JPA 中，可以使用 **@Query** 注解来执行 JPQL 查询，例如，在 UserRepository 中定义一个根据姓名模糊查询用户的方法：

```Java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> findByNameLike(@Param("name") String name);
}
```

@Query 注解中的字符串就是 JPQL 查询语句，**:** 开头的参数是命名参数，需要使用 **@Param** 注解来指定方法参数与 JPQL 参数之间的映射



## 2、自定义复杂查询 @Query

### 1、概述

@Query 注解是 Spring Data JPA 提供的一种非常灵活的方式，用于定义自定义的查询，通过 @Query 注解，开发者可以直接在 Repository 接口的方法上编写 JPQL 或原生 SQL 查询语句，从而满足各种复杂的查询需求



### 2、使用 JPQL 查询

可以在 @Query 注解中编写 JPQL 查询语句，可以使用命名参数（以 : 开头）或位置参数（以 ? 加索引开头）来传递方法参数

推荐使用命名参数，因为它们更易于理解和维护

```Java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.name = :name")
    List<User> findByEmailAndName(@Param("email") String email, @Param("name") String name);

    @Query("SELECT u.name FROM User u WHERE u.id = :id")
    String findNameById(@Param("id") Long id);
}
```



### 3、执行更新和删除操作

@Query 注解不仅可以用于查询，还可以用于执行更新和删除操作，当需要执行修改数据的 JPQL 语句时，需要在 @Query 注解中设置 Modifying 属性为 true，并且通常还需要使用 @Transactional 注解来确保操作的原子性

```Java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.id = :id")
    void updateEmailById(@Param("id") Long id, @Param("newEmail") String newEmail);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.name = :name")
    void deleteByName(@Param("name") String name);
}
```

@Modifying 注解告诉 Spring Data JPA 这是一个修改操作（更新或删除），而不是查询操作

@Transactional 注解用于声明事务，确保更新操作在事务上下文中执行



## 3、原生 SQL 查询

虽然 JPQL 已经能够满足大部分的查询需求，但在某些特定的场景下，可能需要使用原生 SQL 查询，这些场景通常包括：

- **访问数据库特定的功能**：某些数据库提供了特定的 SQL 扩展或函数，这些功能在 JPA 标准中可能没有对应的支持，如果需要使用这些数据库特定的功能，就需要编写原生 SQL 查询
- **性能优化**：在某些情况下，为了获得最佳的性能，可能需要编写特定的 SQL 语句，这些语句可能与 JPA 生成的 SQL 语句有所不同，例如，可以使用数据库的特定优化技巧或者使用存储过程等
- **执行复杂的报表查询**：对于一些复杂的报表查询，可能涉及到多个表的连接、复杂的聚合操作以及特定的数据库函数，使用原生 SQL 可能更容易实现
- **与遗留系统集成**：如果需要与一个已经存在的、使用原生 SQL 的系统进行集成，可能需要在 Spring Data JPA 中执行一些已有的原生 SQL 查询

在 Spring Data JPA 中，可以使用 @Query 注解的 **nativeQuery** 属性来执行原生 SQL 查询，只需要将 nativeQuery 属性设置为 true，然后在注解中编写 SQL 语句即可

```Java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE name LIKE %:name%", nativeQuery = true)
    List<User> findUsersByNameLikeNative(@Param("name") String name);

    @Query(value = "SELECT COUNT(*) FROM users", nativeQuery = true)
    int countAllUsersNative();
}
```

在这个示例中，findUsersByNameLikeNative 方法执行了一个原生 SQL 查询，用于根据姓名模糊查找用户（假设数据库表名为 users），countAllUsersNative 方法执行了一个原生 SQL 查询，用于统计所有用户的数量

**映射原生 SQL 查询结果**：默认情况下，原生 SQL 查询的结果会映射到对应的实体类，如果查询返回的列名与实体类的属性名不一致，或者查询返回的结果不是完整的实体对象，可以使用 @SqlResultSetMapping 和 @ConstructorResult 等注解来定义如何映射查询结果



**注意事项**：

- 虽然原生 SQL 提供了更大的灵活性，但也带来了一些潜在的缺点：

  - **数据库依赖性增加**：原生 SQL 语句通常是特定于数据库的，使用原生 SQL 会增加应用程序对特定数据库的依赖性，降低了可移植性

  - **维护性降低**：原生 SQL 语句是字符串形式的，编译器无法对其进行语法检查，可能会存在潜在的错误，同时，如果数据库 schema 发生变化，可能需要手动修改 SQL 语句




## 4、动态查询

### 1、概述

在实际应用中，经常会遇到需要根据不同的条件组合来查询数据的场景，如果条件是固定的，可以使用方法名自动查询或 JPQL 查询，但是，当查询条件在运行时动态变化时，就需要使用动态查询，Spring Data JPA 提供了多种实现动态查询的方式，包括 JPA Criteria API 和 Querydsl.



### 2、JPA Criteria API

JPA Criteria API 是一种类型安全的方式，用于构建动态查询，它允许开发者通过编程的方式构建查询的各个部分，例如 SELECT、FROM、WHERE、ORDER BY 等子句，Criteria API 特别适用于需要在**运行时根据用户输入**或**其他条件动态生成**查询语句的场景

要使用 Criteria API 进行动态查询，通常需要以下步骤 :  

1. **获取 CriteriaBuilder 实例**：通过 EntityManager 获取 CriteriaBuilder，它是构建 Criteria 查询的工厂类
2. **创建 CriteriaQuery 实例**：使用 CriteriaBuilder 创建 CriteriaQuery 对象，指定查询结果的类型
3. **定义根实体**：使用 CriteriaQuery 的 from() 方法指定查询的根实体
4. **构建查询条件（WHERE 子句）**：使用 CriteriaBuilder 的各种方法（如 equal()、like()、greaterThan()等）创建 Predicate 对象，表示查询条件。可以根据运行时条件动态地添加这些谓词，并使用 and() 或 or() 方法组合多个谓词
5. **设置查询的其他部分**：可以使用 CriteriaQuery 的 select()、orderBy()、groupBy() 等方法设置查询的选择列表、排序方式和分组方式
6. **创建并执行 TypedQuery**：使用 EntityManager的 createQuery()方法创建 TypedQuery 对象，并执行查询获取结果

以下是一个使用 JPA Criteria API 构建动态查询的示例 :  

```Java
public List<Customer> findCustomer(Long myId, String myName, String mySurName, String myAddress) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
    Root<Customer> root = query.from(Customer.class);
    List<Predicate> predicates = new ArrayList<>();

    if (myId!= null) {
        predicates.add(cb.equal(root.get("ID"), myId));
    }
    if (myName!= null &&!myName.isEmpty()) {
        predicates.add(cb.equal(root.get("name"), myName));
    }
    if (mySurName!= null &&!mySurName.isEmpty()) {
        predicates.add(cb.equal(root.get("surename"), mySurName));
    }
    if (myAddress!= null &&!myAddress.isEmpty()) {
        predicates.add(cb.equal(root.get("address"), myAddress));
    }

    query.select(root).where(cb.and(predicates.toArray(new Predicate)));
    return entityManager.createQuery(query).getResultList();
}
```

Criteria API 提供了类型安全的查询构建方式，可以在编译时检查查询语法的正确性，并且易于进行重构，然而对于复杂的查询，Criteria API 的代码可能会显得比较冗长



### 3、Querydsl

Querydsl 是一个独立的框架，可以与 Spring Data JPA 集成，用于构建类型安全的动态查询，它提供了一个流畅的 API，允许开发者以类似于 SQL 的方式编写查询，同时保证类型安全，Querydsl 支持多种后端技术，包括 JPA、SQL、MongoDB 等

要使用 Querydsl 进行动态查询，通常需要以下步骤：

1. **添加 Querydsl 依赖**：在项目的构建文件中添加 Querydsl 的相关依赖，包括 querydsl-jpa 和 querydsl-apt。querydsl-apt用于在编译时生成 Q-类，这些类是实体类的元模型，用于类型安全地引用实体属性

2. **配置 Querydsl Maven 插件**：配置 Maven 插件以生成 Q-类

3. **在 Repository 中继承 QuerydslPredicateExecutor**：让 Spring Data JPA Repository 接口继承 QuerydslPredicateExecutor 接口，这个接口提供了使用 Querydsl Predicate 进行查询的方法

4. **创建 Q-类实例**：在代码中创建需要查询的实体对应的 Q-类实例

5. **构建查询条件（Predicate）**：使用 Q-类实例的属性和 Querydsl 提供的各种方法（如 eq()、like()、gt() 等）构建 Predicate 对象，表示查询条件，可以根据运行时条件动态地组合这些谓词

6. **执行查询**：使用 Repository 的 findOne() 或 findAll() 方法，并传入构建好的 Predicate 对象作为参数来执行查询

以下是一个使用 Querydsl 构建动态查询的示例 :  

```Java
BooleanBuilder builder = new BooleanBuilder();
QMember member = QMember.member;
QTeam team = QTeam.team;

if (hasText(condition.getUsername())) {
    builder.and(member.username.eq(condition.getUsername()));
}
if (hasText(condition.getTeamName())) {
    builder.and(team.name.eq(condition.getTeamName()));
}
if (condition.getAgeGoe()!= null) {
    builder.and(member.age.goe(condition.getAgeGoe()));
}
if (condition.getAgeLoe()!= null) {
    builder.and(member.age.loe(condition.getAgeLoe()));
}

return queryFactory
   .select(new QMemberTeamDto(
        member.id,
        member.username,
        member.age,
        team.id,
        team.name
    ))
   .from(member)
   .leftJoin(member.team, team)
   .where(builder)
   .fetch();
```

Querydsl 提供了比 Criteria API 更简洁和易读的 API，尤其是在处理复杂的查询条件时，它还支持更多的后端技术，具有更广泛的适用性



### 4、Spring Data JPA Specifications

Spring Data JPA Specifications 提供了一种基于 JPA Criteria API 的方式来构建可重用的查询谓词，通过实现 Specification 接口，可以将查询条件封装成独立的组件，并在 Repository 中使用，Specifications 特别适合需要在**运行时动态组合查询条件**的场景

要使用 Specifications 进行动态查询，通常需要以下步骤：

1. **在 Repository 中继承 JpaSpecificationExecutor**：让 Spring Data JPA Repository 接口继承 JpaSpecificationExecutor 接口，这个接口提供了执行 Specification 查询的方法
2. **创建 Specification 实现类**：创建实现 org.springframework.data.jpa.domain.Specification 接口的类，该接口的 toPredicate() 方法用于构建 JPA Criteria API 的 Predicate 对象
3. **构建查询条件**：在 toPredicate() 方法中，使用 CriteriaBuilder 构建查询的谓词，可以根据运行时条件动态地添加和组合谓词
4. **执行查询**：使用 Repository 的 findAll() 方法，并传入 Specification 对象作为参数来执行查询

以下是一个使用 Spring Data JPA Specifications 构建动态查询的示例 :  

```Java
public static Specification<Student> nameEndsWithIgnoreCase(String name) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase());
}

public static Specification<Student> isAge(Integer age) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("age"), age);
}

public static Specification<Student> isSchoolBorough(String borough) {
    return (root, query, criteriaBuilder) -> {
        Join<Student, School> schoolJoin = root.join("school", JoinType.INNER);
        return criteriaBuilder.equal(schoolJoin.get("borough"), borough);
    };
}

Specification<Student> studentSpec = Specification
   .where(StudentSpecification.nameEndsWithIgnoreCase("smith"))
   .and(StudentSpecification.isAge(20))
   .and(StudentSpecification.isSchoolBorough("Ealing"));

List<Student> studentList = studentRepository.findAll(studentSpec);
```

Specifications 提供了一种将查询条件定义为可重用组件的方式，使得代码更加模块化和易于测试，可以方便地使用 and() 和 or() 方法组合多个 Specification 对象，构建复杂的查询条件



### 5、动态查询方式对比

| **特性**             | **JPA Criteria API** | **Querydsl**                             | **Spring Data JPA Specifications** |
| -------------------- | -------------------- | ---------------------------------------- | ---------------------------------- |
| 类型安全             | 是                   | 是                                       | 是                                 |
| 动态性               | 强                   | 强                                       | 强                                 |
| 语法                 | 较为冗长             | 流畅，类似于 SQL                         | 基于 Criteria API                  |
| 可读性               | 对于复杂查询可能较差 | 较好                                     | 良好，条件可重用                   |
| 多后端支持           | 仅限 JPA 实现        | 支持多种后端                             | 仅限 JPA 实现                      |
| 集成 Spring Data JPA | 内置                 | 通过 `QuerydslPredicateExecutor`         | 通过 `JpaSpecificationExecutor`    |
| 适用场景             | 复杂的动态查询       | 复杂的动态查询，需要类型安全和多后端支持 | 需要组合和重用查询条件的场景       |

  

## 5、查询投影

### 1、概述

**场景**： 执行查询时，不需要返回完整的实体对象，而只需要实体中的部分字段、关联实体的字段或者一些聚合计算结果，直接返回所需数据的自定义对象（DTO - Data Transfer Object）可以提高性能（减少数据传输量）并使 API 更清晰，JPQL 的构造器表达式是实现此目标的一种常用方法



### 2、构造器表达式配合 DTO 类

**定义 DTO 类**：创建一个普通的 Java 类（POJO），它包含你需要从查询中返回的数据字段，关键在于提供一个构造函数，其参数列表的类型和顺序必须与后续 JPQL 查询 SELECT 子句中的表达式严格对应

~~~java
package com.park.carTrace.pojo; // 包名很重要

import java.util.Calendar; // 注意使用的类型

// DTO 类，无需 JPA 注解
public class CarTraceResult {
    private String plateNo;
    private Integer plateColor;
    private String typeName;
    private String parkName;
    private Calendar time; // 对应 max(a.time)
    private Long times;    // 对应 count(a.id)

    // 构造函数的参数类型和顺序必须与 JPQL 中 SELECT new ...(...) 对应
    public CarTraceResult(String plateNo, Integer plateColor, String typeName, String parkName, Calendar time, Long times) {
        this.plateNo = plateNo;
        this.plateColor = plateColor;
        this.typeName = typeName;
        this.parkName = parkName;
        this.time = time;
        this.times = times;
    }

    // Getters (通常需要) 和 Setters (可选) ...
    // toString(), equals(), hashCode() (可选) ...
}
// 注意: 也可以使用 Java Record (Java 14+) 简化 DTO 定义
// public record CarTraceResult(String plateNo, Integer plateColor, ...) {}
~~~

**在 Repository 中编写 JPQL 查询**：使用 @Query 注解，并在 JPQL 中使用 select new 语法，后跟 DTO 类的完全限定名和传递给构造函数的参数列表

~~~java
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
// ... 其他 import ...

public interface CarTraceRecordRepository extends JpaRepository<CarTraceRecordEntity, Long> {

    @Query(value = "SELECT new com.park.carTrace.pojo.CarTraceResult(a.plateNo, a.plateColor, a.typeName, a.parkName, max(a.time), count(a.id)) " + // 使用 DTO 的完全限定名
                   " FROM CarTraceRecordEntity a " +
                   " WHERE a.plateNo = :plateNo AND a.plateColor = :plateColor AND a.type = :type " + // 推荐使用命名参数
                   " GROUP BY a.parkNo " + // 聚合查询需要 GROUP BY
                   " ORDER BY time DESC") // 注意：ORDER BY 中的 time 可能需要别名或引用聚合函数 max(a.time)
    List<CarTraceResult> queryCarTraceRecord(@Param("plateNo") String plateNo,
                                             @Param("plateColor") Integer plateColor,
                                             @Param("type") Integer type);
}
~~~



**注意**

1. **完全限定名**：在 select new 后面必须使用 DTO 类的完全限定名（包含包名）
2. **构造函数匹配**：DTO 类必须有一个公共构造函数，其参数的数量、类型和顺序必须与 JPQL 查询 SELECT 子句中表达式的数量、类型和顺序完全一致
3. **直接返回 DTO 列表**：使用此方法，Repository 方法的返回类型直接就是 List<YourDtoClass>（例如 List<CarTraceResult>），而不是 List<Object[]>，JPA 会处理对象的实例化过程
4. **聚合函数**：可以直接在 SELECT 子句中使用聚合函数（如 COUNT, MAX, MIN, AVG, SUM），并将结果映射到 DTO 构造函数的对应参数
5. **替代方案**：Spring Data JPA 还支持**基于接口的投影 (Interface-based Projections)**，通常更简洁



### 3、基于接口的投影

这种方法不要求创建一个单独的 DTO 类，而是定义一个 Java 接口，该接口声明一系列 getter 方法，这些方法的名字对应想要从查询结果中获取的属性

**定义投影接口**：创建一个接口，声明需要查询返回的数据的 getter 方法

- **命名约定**：getter 方法的名称必须遵循 Java Bean 的规范（例如 getPlateNo() 对应 plateNo 属性）
- **属性匹配**：方法名（去掉 get 并将首字母小写后）需要匹配 JPA 实体类的属性名
- **聚合/别名**：如果需要返回聚合结果（如 COUNT, MAX）或实体中不存在的计算值，或者想使用不同的名字，需要在 @Query 中使用别名 (Alias)，并且接口中的 getter 方法名需要精确匹配这个别名

~~~java
package com.park.carTrace.projection; // 建议将投影接口放在单独的包中

import java.util.Calendar;

// 定义一个投影接口
public interface CarTraceSummary {

    // 直接映射到 CarTraceRecordEntity 的属性
    String getPlateNo();
    Integer getPlateColor();
    String getTypeName();
    String getParkName();

    // 映射到 JPQL 查询中的别名 (对应聚合函数)
    // getXXX() -> 对应查询中的 AS xxx
    Calendar getMaxTime(); // 对应 SELECT ... max(a.time) AS maxTime ...
    Long getRecordCount(); // 对应 SELECT ... count(a.id) AS recordCount ...

    // 还可以包含默认方法 (Java 8+) 进行简单计算
    default String getFullPlateInfo() {
        return getPlateNo() + " (" + getPlateColor() + ")";
    }

    // 甚至可以使用 @Value 注解执行 SpEL 表达式进行更复杂的计算 (需 Spring Framework 支持)
    // @Value("#{target.plateNo + ' - ' + target.parkName}")
    // String getPlateAndPark();
}
~~~

~~~java
package com.example.jpa.projection;

// 投影接口，只选择部分用户信息
public interface UsernameAndEmail {

    // Getter 方法名精确对应 User 实体的属性名
    String getUsername();
    String getEmail();

    // 注意：这里没有包含 userId, passwordHash, firstName, registrationDate 的 getter
    // 因此，通过此接口进行的投影查询不会选择这些列

    // 可以在接口中添加默认方法
    default String getMaskedUsername() {
        if (getUsername() == null || getUsername().length() <= 1) {
            return getUsername();
        }
        return getUsername().charAt(0) + "***";
    }
}
~~~

**修改 Repository 方法**：将 Repository 方法的返回类型改为定义的投影接口（或其列表/Optional等）

- 对于简单投影：如果接口中的所有 getter 方法都**直接对应实体类的属性**，甚至可能不需要写 @Query，可以直接使用派生查询 (Derived Query)，Spring Data JPA 会自动优化查询，只选择接口中定义的属性

  - ~~~java
    // 假设有一个只包含 plateNo 和 plateColor 的简单接口 SimpleCarInfo
    // List<SimpleCarInfo> findByTypeName(String typeName); // 可能自动投影
    ~~~

  - ~~~java
    package com.example.jpa.repository;
    
    import com.example.jpa.entity.User;
    import com.example.jpa.projection.UsernameAndEmail; // 导入投影接口
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    
    import java.time.Instant;
    import java.util.List;
    import java.util.Optional;
    
    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
    
        // 示例 1: 根据 email 查找用户，返回投影列表
        // 方法名 "findByRegistrationDateAfter" 会被解析为 WHERE registrationDate > ?1
        // 返回类型 List<UsernameAndEmail> 告诉 Spring Data 只需查询和映射接口定义的字段
        List<UsernameAndEmail> findByRegistrationDateAfter(Instant date);
    
        // 示例 2: 根据用户名精确查找，返回单个投影 (可能为空)
        // 方法名 "findByUsername" 会被解析为 WHERE username = ?1
        // 返回类型 Optional<UsernameAndEmail> 处理可能找不到用户的情况
        Optional<UsernameAndEmail> findByUsername(String username);
    
        // 示例 3: 如果确定用户存在，可以直接返回投影接口类型
        // 方法名 "getByEmail" 会被解析为 WHERE email = ?1 (如果不存在会抛异常)
        UsernameAndEmail getByEmail(String email);
    
        // --- 为了对比，可以保留返回完整实体的方法 ---
        Optional<User> findFullUserByUsername(String username);
    }
    ~~~

- **对于包含聚合/别名的复杂投影**：仍然需要使用 @Query 注解，但在 SELECT 子句中，需要为希望映射到接口 getter 的每个表达式（特别是聚合函数或重命名的属性）指定别名 (AS)，别名必须与接口 getter 方法名（去掉 get 并首字母小写）匹配

  - ~~~java
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import java.util.List;
    // ... 其他 import ...
    import com.park.carTrace.projection.CarTraceSummary; // 导入投影接口
    
    public interface CarTraceRecordRepository extends JpaRepository<CarTraceRecordEntity, Long> {
    
        // 返回类型改为投影接口列表 List<CarTraceSummary>
        @Query(value = "SELECT " +
                       "   a.plateNo AS plateNo, " +         // 别名匹配 getPlateNo()
                       "   a.plateColor AS plateColor, " +   // 别名匹配 getPlateColor()
                       "   a.typeName AS typeName, " +       // 别名匹配 getTypeName()
                       "   a.parkName AS parkName, " +       // 别名匹配 getParkName()
                       "   max(a.time) AS maxTime, " +       // 别名匹配 getMaxTime()
                       "   count(a.id) AS recordCount " +    // 别名匹配 getRecordCount()
                       " FROM CarTraceRecordEntity a " +
                       " WHERE a.plateNo = :plateNo AND a.plateColor = :plateColor AND a.type = :type " +
                       " GROUP BY a.plateNo, a.plateColor, a.typeName, a.parkName, a.parkNo " + // Group By 通常包含所有非聚合列
                       " ORDER BY maxTime DESC")
        List<CarTraceSummary> findCarTraceSummary(@Param("plateNo") String plateNo,
                                                  @Param("plateColor") Integer plateColor,
                                                  @Param("type") Integer type);
    }
    ~~~



**注意**：

- 当使用聚合函数并选择其他非聚合字段时，GROUP BY 子句通常需要包含所有未被聚合的 SELECT 列（或者其来源的实体属性）



### 4、工作原理简述

当调用返回接口投影的方法时，Spring Data JPA 不会去查找该接口的实现类，相反，它在运行时动态地生成一个代理（Proxy）实例，这个代理实例直接从查询结果中提取所需的数据来响应接口中定义的 getter 方法调用，数据库查询本身也会被优化，通常只包含接口中涉及的列







# 4、实体关系映射

## 1、概述

在关系型数据库中，表之间存在各种关系，例如一对一、一对多、多对多等，JPA 通过注解的方式来定义实体类之间的关系，Spring Data JPA 会根据这些注解来管理实体之间的关联



## 2、@OneToOne

### 1、概述

@OneToOne 注解用于定义两个实体之间的一对一关系，这意味着在一个实体实例中，关联的另一个实体实例最多只有一个



### 2、单向关联

在单向关联中，只有一个实体拥有对另一个实体的引用，例如，一个 Person 实体可能拥有一个 Passport 实体，但 Passport 实体不一定需要知道它属于哪个 Person

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;

    //...
}

@Entity
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

    //...
}
```

在 Person 实体中，@OneToOne 注解标记了 passport 属性与 Passport 实体之间的关联，@JoinColumn(name = "passport_id") 注解指定了在 person 表中用于存储关联 Passport 实体外键的列名是 passport_id



### 3、双向关联

在双向关联中，两个实体都拥有对彼此的引用，在 @OneToOne 关系中，需要指定关系的拥有者（owning side）和被拥有者（inverse side），拥有者负责维护关系的外键，可以使用 mappedBy 属性在被拥有者一方指定拥有者一方的属性名

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "passport_id")
    private Passport passport;

    //...
}

@Entity
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passportNumber;

    @OneToOne(mappedBy = "passport")
    private Person person;

    //...
}
```

在这个双向关联的例子中，Person 是拥有者，因为它使用了 @JoinColumn 注解，Passport 是被拥有者，它使用 mappedBy = "passport" 来指定对应的拥有者一方的属性名是 passport

选择单向还是双向关联取决于实体之间的导航需求，如果只需要从一个实体导航到另一个实体，则单向关联就足够了，如果需要双向导航，则需要使用双向关联，在 @OneToOne 关系中，通常会使用双向关联，以便于在两个实体之间进行方便的导航



## 3、@ManyToOne & @OneToMany

### 1、概述

@ManyToOne 注解用于定义多对一的关系，而 @OneToMany 注解用于定义一对多的关系，这两种关系通常是成对出现的



### 2、单向关联 @ManyToOne 

在单向 @ManyToOne 关联中，多个实体实例可以关联到同一个实体实例，但反过来不行，例如，多个 Order 实体可能关联到同一个 Customer 实体，但 Customer 实体本身并不直接拥有对其关联的 Order 实体的引用

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    //...
}

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //...
}
```

在 Order 实体中，@ManyToOne 注解标记了 customer 属性与 Customer 实体之间的关联，@JoinColumn(name = "customer_id") 注解指定了在 order 表中用于存储关联 Customer 实体外键的列名是 customer_id



### 3、单向关联 @OneToMany

单向 @OneToMany 关联相对较少使用，因为它在数据库层面通常需要一个额外的连接表来维护关系



### 4、双向关联

双向 @OneToMany 与 @ManyToOne 关联是最常见的关系类型，在这种关系中，一个实体拥有对多个其他实体的引用，而这些被引用的实体反过来也拥有对拥有者的引用

同样需要指定关系的拥有者和被拥有者，@ManyToOne 注解通常在拥有者一方，并使用 @JoinColumn 指定外键列，@OneToMany 注解在被拥有者一方，并使用 mappedBy 属性指向拥有者一方的关联属性

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    //...
}

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    //...
}
```

Order 实体使用 @ManyToOne 和 @JoinColumn 关联到 Customer 实体，是关系的拥有者，Customer 实体使用 @OneToMany 和 mappedBy = "customer" 关联到 Order 实体，是被拥有者，mappedBy 属性指向 Order 实体中名为 customer 的属性

在维护双向关联时，需要注意保持关系的一致性，通常需要在代码中同时更新双方的关联关系，以避免出现数据不一致的情况



## 4、@ManyToMany

### 1、概述

@ManyToMany 注解用于定义两个实体之间的多对多关系，这意味着一个实体实例可以关联到多个另一个实体实例，反之亦然，在数据库层面，多对多关系通常需要一个额外的中间表（连接表）来存储两个实体之间的关联关系



### 2、中间表 @JoinTable

#### 1、隐式中间表

在最简单的多对多关系中，JPA 会自动创建一个中间表来管理关联，而不需要显式地定义一个中间实体类，可以使用 @JoinTable 注解来定制这个中间表的名称、外键列名等



#### 2、显式中间表

在某些情况下，仅仅使用 @ManyToMany 和 @JoinTable 可能无法满足需求，例如，当中间表需要包含额外的属性（如关联创建时间、状态等）时，就需要显式地创建一个中间实体类来映射中间表

~~~java
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_course_enrollment")
@IdClass(StudentCourseId.class)
public class StudentCourseEnrollment {

  @Id
  @Column(name = "student_id")
  private Long studentId;

  @Id
  @Column(name = "course_id")
  private Long courseId;

  @ManyToOne
  @MapsId("studentId")
  @JoinColumn(name = "student_id")
  private Student student;

  @ManyToOne
  @MapsId("courseId")
  @JoinColumn(name = "course_id")
  private Course course;

  @Column(name = "enrollment_date")
  private LocalDateTime enrollmentDate;

  // Constructors, Getters, Setters
}

@Embeddable
class StudentCourseId implements Serializable {
  private Long studentId;
  private Long courseId;

  // Constructors, Getters, Setters, equals(), hashCode()
}

@Entity
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @OneToMany(mappedBy = "student")
  private List<StudentCourseEnrollment> enrollments;

  // Constructors, Getters, Setters
}

@Entity
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;

  @OneToMany(mappedBy = "course")
  private List<StudentCourseEnrollment> enrollments;

  // Constructors, Getters, Setters
}
~~~

在这个例子中，StudentCourseEnrollment 是一个显式的中间实体，它包含了 studentId、courseId 和 enrollmentDate 属性，其中 @IdClass(StudentCourseId.class) 注解指定了复合主键类，@ManyToOne 和 @MapsId 注解用于将外键映射到关联的 Student 和 Course 实体，并将它们作为复合主键的一部分，这种方式允许在中间表中添加额外的属性

选择使用隐式中间表还是显式中间实体取决于是否需要在中间表中存储额外的非主键信息，如果仅仅需要维护两个实体之间的关联，那么使用 @ManyToMany 和 @JoinTable 更加简洁，如果需要额外的属性或更复杂的关联管理，则需要使用显式中间实体



### 3、单向关联

~~~java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToMany
  @JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private List<Course> courses = new ArrayList<>();

  //...
}

@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  //...
}
~~~

在这个例子中，@JoinTable 注解指定了中间表名为 student_course，以及连接 Student 和 Course 表的外键列名



### 4、双向关联

在双向多对多关系中，需要在拥有关系的一侧使用 @JoinTable，而在另一侧使用 mappedBy 属性

~~~java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToMany
  @JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private List<Course> courses = new ArrayList<>();

  //...
}

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToMany(mappedBy = "courses")
  private List<Student> students = new ArrayList<>();

  //...
}
~~~

这里，Student 实体是关系的拥有者，而 Course 实体通过 mappedBy 引用了 Student 实体中的 courses 属性



## 5、加载策略

### 1、概述

在 JPA 中，当一个实体关联了其他实体时，加载这些关联实体的方式有两种策略：延迟加载（Lazy Loading）和立即加载（Eager Loading），选择合适的加载策略对于优化应用程序的性能至关重要



### 2、延迟加载（Lazy Loading）

延迟加载是指在访问关联实体时才从数据库加载该实体，当第一次访问关联属性时，JPA 会发送一个额外的 SQL 查询来加载关联的数据，可以使用 FetchType.LAZY 来指定延迟加载策略

```Java
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    //...
}
```

在这个例子中，Order 实体与 Customer 实体之间是 ManyToOne 的关系，并且指定了 fetch = FetchType.LAZY，这意味着在加载 Order 实体时，不会立即加载关联的 Customer 实体，只有当第一次访问 order.getCustomer() 时，才会触发一个额外的 SQL 查询来加载 Customer 实体的数据



### 3、立即加载（Eager Loading）

立即加载是指在加载实体时，同时加载所有关联的实体，可以使用 FetchType.EAGER 来指定立即加载策略

```Java
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passport_id")
    private Passport passport;

    //...
}
```

在这个例子中，Person 实体与 Passport 实体之间是 OneToOne 的关系，并且指定了 fetch = FetchType.EAGER，这意味着在加载 Person 实体时，会立即加载关联的 Passport 实体的数据

**选择加载策略的考虑因素：**

- **性能**：延迟加载通常可以提高初始加载实体的性能，因为它只加载需要的数据。然而，如果在后续操作中需要访问延迟加载的关联实体，可能会导致 N+1 查询问题，从而降低性能，立即加载可以避免 N+1 查询问题，但可能会加载不必要的关联数据，增加内存消耗
- **数据访问模式**：选择哪种加载策略应该基于应用程序的数据访问模式，如果经常需要访问某个关联实体，那么立即加载可能更合适，如果关联实体很少被访问，那么延迟加载可能更有效
- **默认策略**：JPA 实现通常会对不同的关联类型设置默认的加载策略，例如，@ManyToOne 和 @OneToOne 关系通常默认是立即加载，而 @OneToMany 和 @ManyToMany 关系通常默认是延迟加载，可以根据需要显式地指定加载策略来覆盖默认设置



## 6、关系映射常用注解

| **注解**          | **描述**                                                     |
| ----------------- | ------------------------------------------------------------ |
| @OneToOne         | 定义两个实体之间的一对一关系                                 |
| @ManyToOne        | 定义多个实体关联到一个实体的多对一关系                       |
| @OneToMany        | 定义一个实体关联到多个其他实体的一对多关系                   |
| @ManyToMany       | 定义多个实体关联到多个其他实体的多对多关系                   |
| @JoinColumn       | 用于 @OneToOne 和 @ManyToOne 关系中，指定外键列的名称        |
| @JoinTable        | 用于 @ManyToMany 关系中，指定中间表的名称以及外键列的名称    |
| mappedBy          | 用于双向关联中，在被拥有者一方指定拥有者一方的关联属性名     |
| fetch = FetchType | 指定关联关系的加载策略，可以是 LAZY（延迟加载）或 EAGER（立即加载） |
| @IdClass          | 用于在实体类中指定复合主键类                                 |
| @Embeddable       | 标记一个类可以嵌入到其他实体中                               |
| @EmbeddedId       | 标记一个嵌入式对象作为实体的主键                             |
| @MapsId           | 用于将 @ManyToOne 或 @OneToOne 关联映射到嵌入式主键或 @IdClass 的属性 |



# 5、事务管理与并发控制

## 1、概述

在企业级应用开发中，事务管理和并发控制是保证数据一致性和完整性的关键，Spring 框架提供了强大的事务管理支持，而 JPA 作为持久化技术，也需要考虑并发访问时的数据一致性问题



## 2、声明式事务管理

Spring 提供了两种事务管理的方式：编程式事务管理和声明式事务管理，声明式事务管理是更常用和推荐的方式，因为它将事务管理逻辑与业务逻辑分离，使得代码更加清晰和易于维护

Spring 的声明式事务管理主要是通过 AOP（面向切面编程）来实现的，开发者只需要通过注解或 XML 配置的方式声明哪些方法需要进行事务管理，Spring 框架会自动在这些方法执行前后进行事务的开启、提交或回滚操作，而无需开发者编写显式的事务管理代码

使用声明式事务管理的主要优点包括：

- **代码简洁**：开发者无需编写大量的 try-catch-finally 块来处理事务的开启、提交和回滚，业务逻辑代码更加干净
- **易于维护**：事务管理的策略统一配置，修改事务行为更加方便
- **更好的可读性**：通过注解或 XML 配置，可以清晰地看到哪些方法是需要事务管理的

在 Spring 中启用声明式事务管理通常需要在配置类上添加 @EnableTransactionManagement 注解，对于 Spring Boot 应用，通常不需要显式添加这个注解，因为它已经被自动配置了



## 3、@Transactional 注解详解

@Transactional 注解是 Spring 声明式事务管理的核心，它可以应用在**类级别**或**方法级别**，当应用在类级别时，表示该类中所有 public 方法都将具有事务特性，当应用在方法级别时，表示只有该方法具有事务特性，方法级别的注解会覆盖类级别的注解

@Transactional 注解提供了多个属性，用于配置事务的行为：

- **propagation**：定义事务的传播行为，传播行为指定了当一个被调用的方法已经存在一个事务时，应该如何处理，常用的传播行为包括：
  - **REQUIRED**（默认）：如果当前存在事务，则加入该事务，如果当前没有事务，则创建一个新的事务
   - **REQUIRES_NEW**：无论当前是否存在事务，都创建一个新的事务，如果当前存在事务，则将当前事务挂起
  - **SUPPORTS**：如果当前存在事务，则加入该事务，如果当前没有事务，则以非事务方式执行
  - **NOT_SUPPORTED**：以非事务方式执行操作，如果当前存在事务，则将当前事务挂起
  - **MANDATORY**：如果当前存在事务，则加入该事务，如果当前没有事务，则抛出异常
  - **NEVER**：以非事务方式执行，如果当前存在事务，则抛出异常
  - **NESTED**：如果当前存在事务，则创建一个嵌套事务（需要底层数据源支持，如 JDBC 3.0），嵌套事务可以独立于外部事务进行提交或回滚
  
- **isolation**：定义事务的隔离级别，隔离级别指定了多个并发事务在访问同一数据时应该相互隔离的程度，常用的隔离级别包括：
  - **DEFAULT**（默认）：使用底层数据库的默认隔离级别
   - **READ_UNCOMMITTED**：允许读取尚未提交的数据（可能导致脏读）
  - **READ_COMMITTED**：只允许读取已经提交的数据（可以防止脏读，但可能导致不可重复读）
  - **REPEATABLE_READ**：在同一个事务中多次读取同一数据时，结果应该保持一致（可以防止脏读和不可重复读，但可能导致幻读）
  - **SERIALIZABLE**：提供最高的隔离级别，事务串行执行，可以防止所有并发问题，但性能开销也最大
  
- **timeout**：定义事务的超时时间，单位为秒，如果事务在指定时间内没有完成，则会被强制回滚

- **readOnly**：指定事务是否为只读事务，对于只进行查询操作的方法，可以将其标记为只读事务，这可以帮助数据库进行一些优化

- **rollbackFor**：指定哪些异常应该导致事务回滚，默认情况下，只有未检查异常（RuntimeException 和 Error 的子类）会导致事务回滚。可以指定要回滚的已检查异常

- **noRollbackFor**：指定哪些异常不应该导致事务回滚，即使发生了指定的异常，事务也会尝试提交



## 4、悲观锁与乐观锁的实现与应用

### 1、概述

在并发环境下，多个事务可能会同时访问和修改同一份数据，如果不进行适当的控制，可能会导致数据不一致的问题，JPA 提供了两种主要的锁机制来解决这个问题：悲观锁（Pessimistic Locking）和乐观锁（Optimistic Locking）



### 2、悲观锁（Pessimistic Locking）

悲观锁的思想是在事务开始时就将需要操作的数据锁定，防止其他事务在当前事务完成之前修改这些数据，悲观锁通常通过数据库的锁机制来实现

在 Spring Data JPA 中，可以使用 @Lock 注解来应用悲观锁，需要将 @Lock 注解与 @Query 注解一起使用，或者在 Repository 方法中使用特定的命名约定

```Java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithPessimisticLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<User> findById(Long id); // 默认的 findById 不加锁，这里只是为了演示
}
```

LockModeType.PESSIMISTIC_WRITE 会在读取数据时加上写锁，阻止其他事务读取或修改这些数据

LockModeType.PESSIMISTIC_READ 会在读取数据时加上读锁，允许其他事务读取，但阻止其他事务修改

悲观锁的优点是可以保证数据的一致性，但缺点是可能会降低系统的并发性能，因为锁的持有时间可能会比较长，导致其他事务需要等待



### 3、乐观锁（Optimistic Locking）

乐观锁的思想是在事务提交时才检查数据是否被其他事务修改过，它假设在大部分情况下，并发修改的冲突不会发生，因此在读取数据时不加锁，当需要更新数据时，会检查版本号或其他标识是否与读取时一致，如果一致，则允许更新并递增版本号，如果不一致，则说明数据已被其他事务修改，当前事务会回滚或抛出异常

在 JPA 中，通常使用 @Version 注解来实现乐观锁，需要在实体类中添加一个用于记录版本号的字段

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;

    @Version
    private Long version;

    //...
}
```

当更新 Product 实体时，JPA 会自动检查 version 字段的值，如果在更新过程中，该字段的值被其他事务修改过，那么当前事务会抛出 javax.persistence.OptimisticLockException，开发者需要在代码中捕获这个异常并进行相应的处理（例如，重试操作或通知用户）

乐观锁的优点是并发性能较高，因为它在读取数据时不会加锁，缺点是可能会导致更新失败，需要应用程序处理乐观锁冲突

**选择锁机制的考虑因素：**选择使用悲观锁还是乐观锁取决于应用程序的具体需求和并发访问的特点，如果并发冲突的概率较高，并且对数据一致性要求非常严格，那么悲观锁可能更合适，如果并发冲突的概率较低，并且希望提高系统的并发性能，那么乐观锁可能更合适



## 5、事务的传播行为

事务的传播行为定义了在方法调用链中，当一个方法被调用时，如果当前已经存在一个事务，应该如何处理这个事务，Spring 的 @Transactional 注解提供了多种传播行为，用于灵活地控制事务的边界

以下是常用的事务传播行为的详细解释：

- **REQUIRED**（默认）：如果当前存在一个事务，则加入该事务，如果当前没有事务，则创建一个新的事务，这是最常用的传播行为，适用于大多数场景
- **REQUIRES_NEW**：无论当前是否存在事务，都创建一个新的事务，如果当前存在一个事务，则将当前的事务挂起，直到新的事务完成，这个传播行为通常用于需要独立于当前事务进行提交或回滚的操作，例如记录日志
- **SUPPORTS**：如果当前存在一个事务，则加入该事务，如果当前没有事务，则以非事务的方式执行，这个传播行为适用于那些不需要事务支持，但在事务上下文中执行可以享受事务带来的好处（例如，数据一致性）的操作
- **NOT_SUPPORTED**：以非事务的方式执行操作，如果当前存在一个事务，则将当前的事务挂起，直到当前方法执行完成，这个传播行为适用于那些不应该在事务上下文中执行的操作
- **MANDATORY**：如果当前存在一个事务，则加入该事务，如果当前没有事务，则抛出一个异常（例如，IllegalTransactionStateException），这个传播行为适用于那些必须在事务上下文中执行的操作
- **NEVER**：以非事务的方式执行操作，如果当前存在一个事务，则抛出一个异常（例如，IllegalTransactionStateException），这个传播行为适用于那些绝对不能在事务上下文中执行的操作
- **NESTED**：如果当前存在一个事务，则创建一个嵌套事务，嵌套事务是外部事务的一个子事务，它可以独立于外部事务进行提交或回滚，如果外部事务回滚，那么嵌套事务也会回滚，如果嵌套事务回滚，外部事务可以选择是否回滚，这个传播行为需要底层的数据源支持（例如，JDBC 3.0 规范中的 Savepoint）

| **传播行为**       | **描述**                                                     | **典型应用场景**                                     |
| ------------------ | ------------------------------------------------------------ | ---------------------------------------------------- |
| `REQUIRED`（默认） | 如果当前存在事务，则加入；否则创建新事务。                   | 大部分业务场景。                                     |
| `REQUIRES_NEW`     | 无论当前是否存在事务，都创建新事务，并挂起当前事务（如果存在）。 | 独立的操作，如记录日志。                             |
| `SUPPORTS`         | 如果当前存在事务，则加入；否则以非事务方式执行。             | 不需要事务，但在事务上下文中执行可以受益的操作。     |
| `NOT_SUPPORTED`    | 以非事务方式执行，并挂起当前事务（如果存在）。               | 不应该在事务中执行的操作。                           |
| `MANDATORY`        | 如果当前存在事务，则加入；否则抛出异常。                     | 必须在事务上下文中执行的操作。                       |
| `NEVER`            | 以非事务方式执行，如果当前存在事务，则抛出异常。             | 绝对不能在事务上下文中执行的操作。                   |
| `NESTED`           | 如果当前存在事务，则创建嵌套事务；否则行为类似 `REQUIRED`。  | 需要在外部事务中创建可独立提交或回滚的子事务的操作。 |



## 6、事务的隔离级别

事务隔离级别定义了在并发事务执行时，一个事务对数据的修改能被其他事务看到什么程度. 不同的隔离级别旨在解决并发访问时可能出现的数据不一致问题，例如脏读（Dirty Read）、不可重复读（Non-repeatable Read）和幻读（Phantom Read）

Spring 的 @Transactional 注解提供了 isolation 属性来设置事务的隔离级别，该属性使用 org.springframework.transaction.annotation.Isolation 枚举

以下是 Spring 支持的事务隔离级别:

- **DEFAULT**：使用底层数据库的默认隔离级别，大多数数据库的默认隔离级别是 READ_COMMITTED，但 MySQL 是 REPEATABLE_READ
- **READ_UNCOMMITTED**：这是最低的隔离级别，允许一个事务读取另一个事务尚未提交的更改，可能导致脏读，此外，还可能发生不可重复读和幻读，实际应用中很少使用此级别，因为它容易导致数据不一致，PostgreSQL 和 Oracle 不支持此级别
- **READ_COMMITTED**：确保一个事务只能读取到其他事务已经提交的更改，从而避免了脏读，但是，在同一个事务中多次读取同一数据行时，可能会得到不同的结果（不可重复读），并且范围查询可能会返回不同的行数（幻读），这是包括 PostgreSQL、SQL Server 和 Oracle 在内的大多数数据库的默认隔离级别
- **REPEATABLE_READ**：在 READ_COMMITTED 的基础上，保证在同一个事务中多次读取同一数据行时，结果保持一致，从而防止了不可重复读，但仍然可能发生幻读，即当其他事务插入或删除满足查询条件的行时，后续的范围查询可能会返回不同的结果集，MySQL 的默认隔离级别是 REPEATABLE_READ. PostgreSQL 虽然允许设置此级别，但在某些实现中可能允许幻读
- **SERIALIZABLE**：这是最高的隔离级别，它强制事务串行执行，就像事务是一个接一个地执行，而不是并发执行，这可以防止所有并发问题，包括脏读、不可重复读和幻读，提供了最强的数据一致性保证，然而，由于需要更严格的锁定机制，SERIALIZABLE 可能会显著降低系统的并发性能

大多数应用程序通常使用 READ_COMMITTED 或 REPEATABLE_READ 级别，只有在对数据一致性有极高要求的特定场景下才考虑使用 SERIALIZABLE



# 6、性能优化

## 1、概述

性能优化是任何应用程序开发中都非常重要的一个环节，尤其是在处理大量数据和高并发请求的企业级应用中，Spring Data JPA 作为一种数据访问框架，也需要关注其性能优化



## 2、N+1 问题

N+1 问题是使用 ORM 框架（包括 JPA）时常见的一种性能瓶颈，当需要加载一个实体，并且该实体存在关联的子实体集合时，可能会发生 N+1 问题

**问题描述**：假设有一个 Author 实体关联了多个 Book 实体（一对多关系），当查询所有的 Author 实体时，JPA 通常会先执行一条 SQL 查询语句获取所有的 Author 记录（这对应了“1”），然后，对于每一个查询到的 Author 实体，如果需要访问其关联的 Book 集合，JPA 可能会再执行一条 SQL 查询语句来加载该作者的所有书籍（这对应了“N”，其中 N 是查询到的 Author 数量），这样，原本只需要一次查询就能完成的任务，却执行了 1+N 次 SQL 查询，当 N 很大时，这会严重影响应用程序的性能

**解决方案**：有几种常用的方法可以解决 JPA 的 N+1 问题：

1. **Fetch Join（抓取连接）**：Fetch Join 是一种在 JPQL 查询中使用 JOIN FETCH 关键字来显式指定需要同时加载的关联实体的方式，通过 Fetch Join，可以在一条 SQL 查询语句中同时获取父实体及其关联的子实体，从而避免了额外的 N 次查询

   例如，要查询所有作者及其书籍，可以使用如下 JPQL 查询：

   ```Java
   @Query("SELECT a FROM Author a JOIN FETCH a.books")
   List<Author> findAllAuthorsWithBooks();
   ```

2. **Entity Graph（实体图）**：Entity Graph 是 JPA 2.1 引入的一个特性，它允许开发者在运行时定义需要加载的实体及其关联关系的图，可以使用 @NamedEntityGraph 注解在实体类上定义命名的 Entity Graph，然后在 Repository 方法中使用 @EntityGraph 注解来指定需要加载的关联

   在 Author 实体类上定义 Entity Graph：

   ```Java
   import javax.persistence.Entity;
   import javax.persistence.GeneratedValue;
   import javax.persistence.GenerationType;
   import javax.persistence.Id;
   import javax.persistence.OneToMany;
   import javax.persistence.NamedEntityGraph;
   import javax.persistence.NamedAttributeNode;
   import java.util.List;
   
   @Entity
   @NamedEntityGraph(name = "author-with-books", attributeNodes = @NamedAttributeNode("books"))
   public class Author {
   
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
   
       private String name;
   
       @OneToMany(mappedBy = "author")
       private List<Book> books;
   
       //...
   }
   ```

   在 Repository 方法中使用 @EntityGraph 注解：

   ```Java
   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.data.jpa.repository.EntityGraph;
   import java.util.List;
   
   public interface AuthorRepository extends JpaRepository<Author, Long> {
   
       @Override
       @EntityGraph(value = "author-with-books", type = EntityGraph.EntityGraphType.FETCH)
       List<Author> findAll();
   }
   ```

3. **Batch Fetching（批量抓取）**：一些 JPA 实现（如 Hibernate）提供了批量抓取的功能，通过配置，当加载某个实体集合时，如果这些实体关联了其他实体，Hibernate 会将加载这些关联实体的查询进行批处理，从而减少查询次数，例如，可以配置 Hibernate 一次加载最多 N 个作者的书籍

   可以在 Hibernate 的配置文件（如 hibernate.properties 或 Spring Boot 的 application.properties）中进行相关配置，例如：

   ```Properties
   spring.jpa.properties.hibernate.default_batch_fetch_size=20
   ```

   这个配置表示当加载关联集合时，Hibernate 会尝试一次加载最多 20 个



## 3、Fetch Join

### 1、概述

Fetch Join 是 JPQL (Java Persistence Query Language) 提供的一种优化查询性能的机制，它允许在查询主实体时，通过 JOIN FETCH 关键字，显式地指定需要立即加载（Eager Loading）的关联实体或实体集合，这样做的主要目的是解决 N+1 查询问题，通过一次数据库查询获取所有需要的数据，避免了因延迟加载（Lazy Loading）导致的多次额外查询



### 2、单一集合

当只需要抓取一个关联集合（例如，一个作者的所有书籍）时，使用 JOIN FETCH 通常比较直接，不会引起根实体（如 Author）在结果列表中的重复

**示例 JPQL**：

```java
// 查询所有作者并立即加载其所有书籍
@Query("SELECT a FROM Author a JOIN FETCH a.books")
List<Author> findAllAuthorsWithBooks();
```

**生成的 SQL (示例)**：

```sql
select
    a1_0.id,
    b1_0.author_id,
    b1_0.id,
    b1_0.title,
    a1_0.name
from
    author a1_0
join
    book b1_0
        on a1_0.id=b1_0.author_id
```

**行为分析**：

- 数据库执行一次 JOIN 查询
- Hibernate 将结果映射回 List<Author>
- 返回的列表中，每个 Author 对象都是唯一的，并且其 books 集合已经被完全加载，可以直接访问，无需额外查询



### 3、多个集合

笛卡尔积问题通常出现在需要抓取多个一对多 (@OneToMany) 或多对多 (@ManyToMany) 关联集合时

**示例场景**：查询所有作者，并同时加载他们的所有书籍 (books) 和所有评论 (reviews)

**示例 JPQL (无 DISTINCT)**：

```java
@Query("SELECT a FROM Author a JOIN FETCH a.books JOIN FETCH a.reviews")
List<Author> findAllAuthorsWithBooksAndReviews();
```

**生成的 SQL (无 DISTINCT 示例)**：

```sql
select
    a1_0.id,
    b1_0.author_id,
    b1_0.id,
    b1_0.title,
    a1_0.name,
    r1_0.author_id,
    r1_0.id,
    r1_0.content
from
    author a1_0
join
    book b1_0
        on a1_0.id=b1_0.author_id
join
    review r1_0
        on a1_0.id=r1_0.author_id
```

**示例 JPQL (有 DISTINCT)**：

```java
@Query("SELECT DISTINCT a FROM Author a JOIN FETCH a.books JOIN FETCH a.reviews")
List<Author> findAllAuthorsWithBooksAndReviewsDistinct();
```

**生成的 SQL (有 DISTINCT 示例)**：

```sql
select
    distinct a1_0.id, -- SQL DISTINCT 应用于所有选择的列
    b1_0.author_id,
    b1_0.id,
    b1_0.title,
    a1_0.name,
    r1_0.author_id,
    r1_0.id,
    r1_0.content
from
    author a1_0
join
    book b1_0
        on a1_0.id=b1_0.author_id
join
    review r1_0
        on a1_0.id=r1_0.author_id
```

行为分析与 DISTINCT 的作用：

1. **SQL 层面的笛卡尔积**：
   - 当 JOIN 多个集合表时，数据库会生成笛卡尔积，如果一个 Author 有 M 本 Book 和 N 条 Review，那么在原始的 SQL 结果集中，这个 Author 的数据会出现 M * N 次
   - SQL 的 DISTINCT 关键字作用于所有 SELECT 出来的列，由于关联集合的列（如 book.id, review.id）在笛卡尔积产生的不同行中通常是不同的，因此 SQL DISTINCT 往往无法在数据库层面完全消除由笛卡尔积带来的行重复
2. **Hibernate/JPA 层面的结果处理**：
   - 无 DISTINCT：Hibernate 在收到数据库返回的多行结果后，需要将其转换回 Java 对象图 (List<Author>)，对于 JOIN FETCH 查询，Hibernate 的内部处理逻辑足够智能，能够识别出哪些行对应同一个根实体 Author（基于其主键 ID），因此，即使 JPQL 中没有 DISTINCT，Hibernate 在构建最终的 List<Author> 时，通常也会确保每个 Author 实例只包含一次，它会将从不同行中获取的 Book 和 Review 对象正确地聚合到对应的唯一 Author 实例的集合属性中
   - 有 DISTINCT：JPQL 中的 DISTINCT 关键字，其更重要的作用是作为给 JPA 提供者（如 Hibernate）的一个明确指令或提示，它告诉 JPA 提供者：“期望最终返回的 Java 集合 (List<Author>) 中的根实体是唯一的”，虽然 Hibernate 默认可能已经这样做了，但使用 DISTINCT 是 符合 JPA 规范的标准方式，它提供了更强的语义保证和跨 JPA 实现的可移植性，它确保了在内存中对根实体进行去重



## 4、Entity Graph

Entity Graph 提供了一种更灵活的方式来定义实体的加载计划，它可以在实体类上通过 @NamedEntityGraph 注解定义命名的图，也可以在 Repository 方法上使用 @EntityGraph 注解动态地指定需要加载的属性

**命名 Entity Graph**：在实体类上使用 @NamedEntityGraph 注解定义图，并通过 @NamedAttributeNode 注解指定需要加载的属性，可以定义多个命名的 Entity Graph，以满足不同查询场景的需求

```Java
@Entity
@NamedEntityGraph(
    name = "author-with-details",
    attributeNodes = {
        @NamedAttributeNode("address"),
        @NamedAttributeNode("books")
    }
)
public class Author {
    //...
}
```

**在 Repository 方法中使用 @EntityGraph**：可以使用 @EntityGraph 注解来指定需要使用的命名 Entity Graph，或者通过 attributePaths 属性动态地指定需要加载的属性路径

```Java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @EntityGraph(value = "author-with-details", type = EntityGraph.EntityGraphType.FETCH)
    List<Author> findAll();

    @EntityGraph(attributePaths = {"address"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Author> findById(Long id);
}
```

type 属性指定了加载方式，FETCH 表示立即加载指定的属性，LOAD 表示如果属性是延迟加载的，则将其加载，否则保持原有的加载策略

**Fetch Join 与 Entity Graph 的比较：**

- Fetch Join 是在 JPQL 查询层面指定的，适用于简单的场景，但对于复杂的加载需求可能会导致 JPQL 语句过于复杂
- Entity Graph 提供了更清晰和灵活的方式来定义加载计划，可以将加载逻辑与查询逻辑分离，可以为不同的查询场景创建不同的 Entity Graph
- Entity Graph 可以在实体类上预先定义，也可以在 Repository 方法中动态指定，提供了更大的灵活性

在实际应用中，可以根据具体的查询需求选择使用 Fetch Join 还是 Entity Graph，对于简单的关联加载，Fetch Join 可能更直接，对于复杂的、需要在不同场景下有不同加载策略的需求，Entity Graph 可能是更好的选择



## 5、缓存机制

### 1、概述

缓存是提高应用程序性能的常用手段，JPA 提供了两级缓存机制：一级缓存（Persistence Context Cache）和二级缓存（Shared Cache）



### 2、一级缓存（Persistence Context Cache）

一级缓存是与 JPA 的持久化上下文（Persistence Context）关联的，持久化上下文相当于一个工作单元，它跟踪所有被加载到其中的实体对象

**作用范围**：一级缓存的生命周期与持久化上下文的生命周期相同，通常情况下，对于一个 Web 请求或一个事务，会创建一个持久化上下文

**工作原理**：当通过 EntityManager 的 find() 方法查询一个实体时，JPA 首先会在当前持久化上下文中查找该实体，如果找到，则直接返回缓存中的对象，如果没有找到，则从数据库加载，并将加载到的实体对象放入缓存中，当事务提交或持久化上下文关闭时，一级缓存也会失效

**特点**：一级缓存是默认启用的，开发者无需进行额外的配置，它主要用于提高在同一个持久化上下文中重复访问同一个实体的性能



### 2、二级缓存（Shared Cache）

二级缓存是跨越多个持久化上下文的缓存，它被所有的 EntityManagerFactory 实例所共享，二级缓存可以显著提高应用程序的性能，因为它减少了对数据库的访问次数

**作用范围**：二级缓存的生命周期与 EntityManagerFactory 的生命周期相同，即在应用程序的整个运行期间都有效

**工作原理**：当一个实体被加载到一级缓存后，如果配置了二级缓存，并且该实体是可缓存的，那么该实体也会被放入二级缓存中，当后续的查询需要访问该实体时，JPA 首先会尝试从二级缓存中查找，如果找到则直接返回，否则再查询一级缓存和数据库

**配置**：二级缓存需要显式地配置和启用，需要选择一个合适的二级缓存提供商（如 Hibernate Ehcache、Caffeine 等），并在 JPA 的配置中指定使用该提供商，同时，需要在实体类上使用 @Cacheable 注解标记该实体是否可以被缓存

例如，在使用 Hibernate 作为 JPA 实现时，可以在 Spring Boot 的 application.properties 文件中配置使用 Ehcache 作为二级缓存提供商

```Properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory
```

然后在需要缓存的实体类上添加 @Cacheable 注解：

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Cacheable;

@Entity
@Cacheable
public class Product {
    //...
}
```

**缓存失效**：二级缓存中的数据可能会因为数据库中的更新而失效，需要配置合适的缓存失效策略，以确保缓存中的数据与数据库中的数据保持一致

**选择缓存策略的考虑因素**：

- **数据变化频率**：对于变化频繁的数据，不适合进行缓存，或者需要使用更积极的缓存失效策略
- **数据访问模式**：对于经常被访问且变化不频繁的数据，使用二级缓存可以显著提高性能
- **缓存一致性**：需要仔细考虑缓存一致性的问题，确保应用程序读取到的是最新的数据



## 6、批量操作

### 1、概述

当需要处理大量数据时，逐条进行数据库操作可能会非常耗时，JPA 提供了一些批量操作的机制，可以显著提高性能



### 2、批量插入

可以使用 EntityManager 的 persist() 方法将多个实体对象添加到持久化上下文中，然后通过 flush() 方法一次性将这些实体插入到数据库中

```Java
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class BatchUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveInBatch(List<User> users) {
        int i = 0;
        for (User user : users) {
            entityManager.persist(user);
            i++;
            if (i % 50 == 0) { // 每 50 条数据刷新一次
                entityManager.flush();
                entityManager.clear(); // 清理持久化上下文，释放内存
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
```

在这个例子中，每处理 50 个用户就刷新一次持久化上下文，将数据批量插入到数据库中，同时，为了避免持久化上下文中的实体过多导致内存溢出，还定期清理持久化上下文



### 3、批量更新和删除

可以使用 JPQL 或原生 SQL 的批量更新和删除语句来提高性能，通过 @Query 注解并设置 @Modifying 为 true，可以执行批量更新或删除操作

```Java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.name LIKE %:name%")
    int updateEmailByNameLike(String name, String newEmail);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.age < :age")
    int deleteByAgeLessThan(int age);
}
```

这些批量操作只需要执行一次数据库交互，可以显著减少与数据库的通信次数，提高性能



## 7、性能调优技巧

**索引优化**：确保数据库表上的关键查询字段都建立了合适的索引

**优化 JPQL 查询**：避免在 JPQL 查询中使用 `SELECT *`，只选择需要的字段，尽量使用连接查询（JOIN）而不是多次查询

**选择合适的关联关系映射**：根据实际需求选择合适的关联关系类型和加载策略

**使用合适的事务边界**：事务的范围应该尽可能小，以减少锁的持有时间

**分析 SQL 执行计划**：使用数据库提供的工具分析 SQL 查询的执行计划，找出潜在的性能瓶颈

**避免在循环中进行数据库操作**：将循环中的数据库操作改为批量操作

**使用只读事务**：对于只进行查询操作的事务，可以将其标记为只读，以提高性能



# 7、高级特性与扩展

## 1、概述

Spring Data JPA 除了提供基本的 CRUD 操作和查询功能外，还提供了一些高级特性和扩展，可以帮助开发者更好地满足特定的业务需求



## 2、审计功能

审计功能是指记录实体的创建时间、创建人、最后修改时间、最后修改人等信息，Spring Data JPA 提供了方便的注解来实现这些功能

**启用审计功能**：首先需要在 Spring Boot 应用的主类上添加 @EnableJpaAuditing 注解来启用 JPA 审计功能

```Java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemoApplication {

    public static void main(String args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

**使用审计注解**：在实体类中使用以下注解来标记需要审计的字段：

- **@CreatedDate**：标记实体的创建时间字段，该字段在实体被首次保存时会自动设置当前时间
- **@CreatedBy**：标记实体的创建人字段，需要配置一个 AuditorAware 的 Bean 来提供当前操作人信息
- **@LastModifiedDate**：标记实体的最后修改时间字段，该字段在实体被更新时会自动更新为当前时间
- **@LastModifiedBy**：标记实体的最后修改人字段，同样需要配置 AuditorAware 的 Bean

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double price;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    //...
}
```

**配置 AuditorAware**：如果需要使用 @CreatedBy 和 @LastModifiedBy 注解，需要实现 org.springframework.data.domain.AuditorAware 接口，并将其注册为 Spring 的 Bean，AuditorAware 接口的 getCurrentAuditor() 方法用于返回当前操作人的信息（例如，用户名）

```Java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null ||!authentication.isAuthenticated()) {
                return Optional.empty();
            }
            return Optional.ofNullable(authentication.getName());
        };
    }
}
```

在这个例子中，假设应用程序使用了 Spring Security 来进行身份验证，getCurrentAuditor() 方法从 Spring Security 的上下文中获取当前用户的用户名



## 3、事件监听机制

JPA 提供了一套实体生命周期事件，允许开发者在实体的不同生命周期阶段执行自定义的逻辑，Spring Data JPA 支持通过注解的方式来监听这些事件

常用的事件监听注解包括：

- **@PrePersist**：在实体被持久化到数据库之前调用
- **@PostPersist**：在实体被持久化到数据库之后调用
- **@PreUpdate**：在实体被更新到数据库之前调用
- **@PostUpdate**：在实体被更新到数据库之后调用
- **@PreRemove**：在实体从数据库中删除之前调用
- **@PostRemove**：在实体从数据库中删除之后调用
- **@PostLoad**：在实体从数据库加载之后调用

可以在实体类的方法上使用这些注解来注册事件监听器

```Java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PostPersist;
import java.time.LocalDateTime;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private LocalDateTime orderDate;

    @PrePersist
    public void beforePersist() {
        this.orderDate = LocalDateTime.now();
        System.out.println("Order is about to be persisted: " + this.orderNumber);
    }

    @PostPersist
    public void afterPersist() {
        System.out.println("Order has been persisted with ID: " + this.id);
    }

    //...
}
```

在这个例子中，beforePersist() 方法使用 @PrePersist 注解标记，它会在 Order 实体被保存到数据库之前自动调用，用于设置订单创建时间，afterPersist() 方法使用 @PostPersist 注解标记，它会在 Order 实体被保存到数据库之后自动调用，用于输出日志信息

通过使用事件监听机制，可以在实体的生命周期中插入自定义的逻辑，例如数据校验、记录日志、发送通知等，这提供了一种松耦合的方式来处理与实体状态变化相关的操作



## 4、自定义 Repository 接口与实现

Spring Data JPA 允许开发者创建自定义的 Repository 接口和实现，以满足特定的数据访问需求

**创建自定义 Repository 接口**：首先，定义一个自定义的接口，该接口包含需要扩展的方法

```Java
public interface CustomUserRepository {
    void updateLastLogin(Long userId);
}
```

**创建自定义 Repository 实现类**：创建一个实现该自定义接口的类，为了让 Spring Data JPA 能够识别并使用这个实现，需要按照一定的命名约定：实现类的名称应该是在自定义 Repository 接口名称的基础上添加 Impl 后缀，例如，CustomUserRepository 的实现类应该是 CustomUserRepositoryImpl，在实现类中，可以注入 EntityManager 来执行 JPA 操作

```Java
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void updateLastLogin(Long userId) {
        entityManager.createQuery("UPDATE User u SET u.lastLogin = CURRENT_TIMESTAMP WHERE u.id = :id")
         .setParameter("id", userId)
         .executeUpdate();
    }
}
```

**在主 Repository 接口中继承自定义接口**：在 Spring Data JPA 的主 Repository 接口中继承自定义的接口

```Java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    //... 其他方法
}
```

通过这种方式，UserRepository 接口不仅拥有了 JpaRepository 提供的基本 CRUD 功能，还拥有了 CustomUserRepository 中定义的 updateLastLogin 方法，Spring Data JPA 会自动将 CustomUserRepositoryImpl 中的实现与 UserRepository 接口关联起来

自定义 Repository 接口和实现提供了一种扩展 Spring Data JPA 功能的强大方式，可以处理一些无法通过自动查询派生或 @Query 注解实现的复杂逻辑



## 5、集成其他 Spring Data 模块

Spring Data 项目包含多个模块，用于支持不同的数据存储技术，例如关系型数据库（JPA）、NoSQL 数据库（MongoDB、Redis、Neo4j 等）以及 RESTful 数据访问，Spring Data JPA 可以与其他 Spring Data 模块进行集成，以提供更全面的解决方案

**集成 Spring Data REST**：

Spring Data REST 可以将 Spring Data JPA Repository 直接暴露为 RESTful API，无需编写额外的 Controller 代码，要集成 Spring Data REST，只需要添加相应的依赖，并在 Repository 接口上添加 @RepositoryRestResource 注解

1. **添加 Spring Data REST 依赖**：

   ```XML
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-data-rest</artifactId>
   </dependency>
   ```

2. **在 Repository 接口上添加 @RepositoryRestResource 注解**：

   ```Java
   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.data.rest.core.annotation.RepositoryRestResource;
   
   @RepositoryRestResource(path = "users")
   public interface UserRepository extends JpaRepository<User, Long> {
       //...
   }
   ```

   @RepositoryRestResource(path = "users") 注解会将 UserRepository 暴露在 /users 路径下，Spring Data REST 会自动为该 Repository 提供标准的 RESTful API，包括 GET（查询单个、查询所有、分页、排序）、POST（创建）、PUT（更新）、DELETE（删除）等操作

**集成其他 Spring Data 模块**：

类似地，可以将 Spring Data JPA 与其他 Spring Data 模块集成，例如：

- **Spring Data MongoDB**：用于操作 MongoDB 数据库
- **Spring Data Redis**：用于操作 Redis 缓存或数据存储
- **Spring Data Neo4j**：用于操作 Neo4j 图数据库

只需要添加相应的依赖，并创建对应的 Repository 接口继承 Spring Data 提供的特定 Repository 接口即可，例如，要使用 Spring Data MongoDB，可以添加 spring-boot-starter-data-mongodb 依赖，并创建一个继承自 MongoRepository 的接口

通过集成其他 Spring Data 模块，可以构建能够处理多种数据存储类型的应用程序，并且可以利用 Spring Data 提供的统一的编程模型，简化开发工作



## 6、实体新旧判断策略(Newness Detection Strategy)

### 1、概述

**目的** (Purpose)：当调用 Repository.save(entity) 方法时，Spring Data JPA 需要判断传入的 entity 对象是应该被视为一个全新的记录插入数据库，还是一个已存在记录的更新（或使其被管理）

**执行者** (Who)：Spring Data JPA 的 SimpleJpaRepository 实现（或其他自定义 Repository 实现）

**时机** (When)：调用 repository.save(entity) 方法时

**决策** (Outcome)：决定内部调用 EntityManager.persist(entity)（对于新实体）还是 EntityManager.merge(entity)（对于已存在或游离态实体）

**重要性** (Why)：确保新实体被正确插入，已存在实体能被正确关联到持久化上下文并可能被更新，特别是对于需要手动分配 ID 的场景，正确区分新旧至关重要



### 2、默认策略

**默认策略**：检查版本（@Version）和标识符（@Id）

1. 检查实体是否有非原始类型（如 Long, Integer 而非 long, int）的 @Version 属性，如果有且值为 null，则认为是新实体
2. 如果没有符合条件的 @Version 属性，则检查 @Id 属性，如果 ID 值为 null（对于包装类型）或 0（对于某些原始类型，但不推荐），则认为是新实体
3. 否则，认为是已存在实体

~~~java
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 由数据库生成，初始为 null
    private Long id;

    private String name;

    @Version // 版本属性，非原始类型
    private Long version;

    // Getters and Setters...

    // toString, equals, hashCode...
}

// --- In Service/Test ---
@Autowired
ProductRepository productRepository;

public void demonstrateDefaultStrategy() {
    // 场景1: 创建新产品
    Product newProduct = new Product();
    newProduct.setName("Laptop");
    // 此刻: newProduct.id is null, newProduct.version is null
    Product savedProduct = productRepository.save(newProduct);
    // Spring Data JPA 检测到 id/version 为 null -> 调用 EntityManager.persist()
    // savedProduct 现在有 id 和 version (通常为 0 或 1)

    System.out.println("Saved new product ID: " + savedProduct.getId() + ", Version: " + savedProduct.getVersion());

    // 场景2: 加载并尝试保存已存在的（模拟游离态）
    Product detachedProduct = new Product();
    detachedProduct.setId(savedProduct.getId()); // 设置了 ID
    detachedProduct.setName("Updated Laptop Name");
    // detachedProduct.version 可能为 null 或之前的值

    // 即使 version 为 null，因为 id 不为 null，通常会认为是已存在的
    Product mergedProduct = productRepository.save(detachedProduct);
    // Spring Data JPA 检测到 id 不为 null -> 调用 EntityManager.merge()
    // merge 会查找或加载对应 ID 的实体，复制 detachedProduct 的状态，并返回一个受管实例
    System.out.println("Merged product ID: " + mergedProduct.getId() + ", Version: " + mergedProduct.getVersion());
}
~~~



### 3、实现 Persistable 接口

如果实体类实现了 org.springframework.data.domain.Persistable<ID> 接口，Spring Data JPA 会优先调用该接口的 isNew() 方法来判断新旧，忽略默认的 ID/Version 检查

这对于 ID 在持久化之前就需要手动设置（如 UUID、业务主键）的情况非常有用

~~~java
import org.springframework.data.domain.Persistable;
import jakarta.persistence.*; // 或者 javax.persistence.*
import java.util.UUID;

@MappedSuperclass // 基类，不直接映射到表
public abstract class AbstractPersistable<ID> implements Persistable<ID> {

    @Transient // 不持久化此字段
    private boolean isNew = true; // 默认为新

    @Override
    public boolean isNew() {
        return isNew;
    }

    // JPA 回调：在加载后或首次持久化前，将 isNew 设为 false
    @PostLoad
    @PrePersist
    void markNotNew() {
        this.isNew = false;
    }

    // 需要子类提供 getId() 实现
    @Override
    public abstract ID getId();
}

@Entity
public class Order extends AbstractPersistable<UUID> {

    @Id // ID 类型为 UUID
    private UUID id;

    private String orderDetails;

    public Order() {
        // 在构造时就生成 UUID，此时 id != null
        this.id = UUID.randomUUID();
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    // other getters/setters
}

// --- In Service/Test ---
@Autowired
OrderRepository orderRepository;

public void demonstratePersistable() {
    Order newOrder = new Order(); // id 已经有值, 但 isNew() 返回 true
    newOrder.setOrderDetails("Test Order");

    System.out.println("Before save - ID: " + newOrder.getId() + ", isNew: " + newOrder.isNew());

    Order savedOrder = orderRepository.save(newOrder); // 调用 isNew() -> true -> EntityManager.persist()

    System.out.println("After save - ID: " + savedOrder.getId() + ", isNew: " + savedOrder.isNew()); // isNew 现在为 false

    // 加载后 isNew 也是 false
    Order loadedOrder = orderRepository.findById(savedOrder.getId()).get();
    System.out.println("Loaded order - ID: " + loadedOrder.getId() + ", isNew: " + loadedOrder.isNew()); // isNew 为 false

    loadedOrder.setOrderDetails("Updated Order Details");
    Order mergedOrder = orderRepository.save(loadedOrder); // 调用 isNew() -> false -> EntityManager.merge()
    System.out.println("Merged order - ID: " + mergedOrder.getId() + ", isNew: " + mergedOrder.isNew());
}
~~~



### 4、自定义 EntityInformation

通过继承 JpaRepositoryFactory 并重写 getEntityInformation 方法，可以提供完全自定义的逻辑来判断实体新旧及获取其他元数据，使用场景较少



## 7、实体状态变化检测(Dirty Checking)

### 1、概述

**目的 (Purpose)**：自动检测**受管状态 (Managed)** 的实体，其属性值是否与加载时或上次同步数据库时相比发生了变化

**执行者 (Who)**：底层的 JPA Provider (如 Hibernate)

**时机 (When)**：持久化上下文（Persistence Context）执行 flush 操作时，flush 通常在事务提交前、执行可能受影响的 JPQL/Native 查询前，或显式调用 entityManager.flush() 时触发

**决策 (Outcome)**：如果检测到变化（实体变“脏”了），则自动生成对应的 SQL UPDATE 语句并准备执行，如果没变化，则不生成 UPDATE 语句

**重要性 (Why)**：实现对象关系映射的“透明持久化”，开发者只需修改 Java 对象属性，JPA 负责将更改同步到数据库，无需手动编写 UPDATE 语句，并避免了不必要的数据库更新操作



受管状态：实体只有在活动的持久化上下文中才是受管状态，一个活动的上下文意味着它关联的 EntityManager 是打开的，并且能够追踪实体变化、缓存数据、处理懒加载等





### 2、默认策略

**基于快照的比较 (Snapshot Comparison)**：

1. 当实体首次被纳入持久化上下文管理时（如通过 find, getReference 加载，或 persist, merge 的结果），Hibernate 在内存中为该实体创建一个快照 (Snapshot)，保存其所有持久化字段的初始值
2. 在 flush 时，Hibernate 遍历所有受管实体，逐一比较实体当前的属性值与其快照值
3. 任何不匹配都表示实体是“脏”的，需要生成 UPDATE 语句

~~~java
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 必须在事务内才能利用脏检查
    // 确保实体在被修改时仍然处于受管状态，并且在事务提交时，有一个活动的上下文来执行 flush 和脏检查
    @Transactional
    public void updateProductName(Long productId, String newName) {
        // 1. 加载实体 -> 纳入持久化上下文管理，Hibernate 创建快照
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            System.out.println("Original Name: " + product.getName());

            // 2. 修改受管实体的属性
            product.setName(newName); // 直接修改对象状态

            // 3. 方法结束，事务准备提交
            // 在提交前，Hibernate 执行 flush 操作：
            // - 比较 product 当前状态 (name=newName) 与快照状态 (name=originalName)
            // - 检测到 name 属性变化 (实体变脏)
            // - 自动生成 UPDATE Product SET name = ?, version = version + 1 WHERE id = ? AND version = ?
            // - 执行 UPDATE 语句

            System.out.println("Name updated in memory to: " + product.getName());
            // !!! 注意：这里不需要调用 productRepository.save(product) !!!
            // 脏检查会自动处理更新
        } else {
            System.out.println("Product not found with ID: " + productId);
        }
    } // <-- 事务在这里提交 (或回滚)

    @Transactional
    public void loadAndDoNothing(Long productId) {
         Optional<Product> optionalProduct = productRepository.findById(productId);
         if (optionalProduct.isPresent()) {
             Product product = optionalProduct.get();
             System.out.println("Loaded product: " + product.getName());
             // 没有修改任何属性
         }
         // 在 flush 时，Hibernate 比较当前状态与快照，发现没有变化
         // 不会生成 UPDATE 语句
         System.out.println("No changes made to product.");
    }
}
~~~



**字节码增强 (Bytecode Enhancement)**：

1. 通过在编译时或类加载时修改实体类的字节码，让 setter 方法在被调用时直接标记实体或属性为“脏”状态
2. flush 时只需检查这个标记，可能比全量比较快照更高效，但增加了构建/运行时的复杂性



# 8、最佳实践与常见问题

## 1、概述

为了更好地使用 Spring Data JPA 并避免一些常见的问题，以下是一些最佳实践和常见问题的总结



## 2、代码规范与设计原则

遵循良好的代码规范和设计原则可以提高代码的可读性、可维护性和可扩展性。以下是一些与 Spring Data JPA 相关的建议：

- **命名约定**：
  - 实体类名应该清晰地反映其代表的业务概念，通常使用名词或名词短语
  - Repository 接口名应该以实体类名开头，并以 Repository 结尾（例如，UserRepository）
  - 自定义查询方法名应该遵循 Spring Data JPA 的命名规则，清晰地表达查询意图
  - 关联属性名应该选择具有描述性的名称，反映实体之间的关系
- **实体设计**：
  - 确保每个实体类都有一个明确的主键（使用 @Id 和 @GeneratedValue）
  - 合理设计实体之间的关联关系，选择合适的关联类型（@OneToOne、@ManyToOne、@OneToMany、@ManyToMany）和加载策略（LAZY、EAGER）
  - 考虑是否需要在实体类中添加版本控制字段（使用 @Version）来实现乐观锁
  - 使用合适的 JPA 注解来映射实体属性到数据库表的列
- **Repository 设计**：
  - 尽量使用 Spring Data JPA 提供的预定义 Repository 接口（如 JpaRepository），以减少代码量
  - 对于简单的查询需求，优先考虑使用方法名自动查询
  - 对于复杂的查询需求，可以使用 @Query 注解编写 JPQL 或原生 SQL 查询，或者使用 Specifications 来构建动态查询
  - 将自定义的查询逻辑封装到自定义的 Repository 接口和实现中
- **事务管理**：
  - 使用 Spring 的声明式事务管理（@Transactional 注解）来管理事务
  - 根据业务需求选择合适的事务传播行为和隔离级别
  - 对于只读操作，将事务标记为 readOnly = true
  - 避免在同一个事务中进行过多的操作，保持事务的短小
- **性能考虑**：
  - 了解并避免 JPA 的 N+1 问题，使用 Fetch Join 或 Entity Graph 来优化关联查询
  - 合理使用 JPA 的缓存机制（一级缓存和二级缓存）
  - 对于批量操作，使用 EntityManager 的 persist() 和 flush() 方法，或者使用 JPQL/SQL 的批量更新和删除语句
  - 关注数据库索引的创建和优化



## 3、常见异常与错误排查

LazyInitializationException：这个异常通常发生在尝试访问一个延迟加载的关联实体，但此时持久化上下文已经关闭的情况下，解决方法包括：

- 将关联关系的加载策略改为 EAGER（需要谨慎使用，可能会导致性能问题）
- 在同一个事务或持久化上下文中访问关联实体
- 使用 Fetch Join 或 Entity Graph 在查询时就加载关联实体

PersistenceException：这是一个通用的 JPA 异常，可能由多种原因引起，例如数据库连接问题、SQL 语法错误、违反数据库约束等，需要查看具体的异常信息和堆栈跟踪来确定问题的根源

DataIntegrityViolationException：这个异常通常发生在尝试插入或更新数据时，违反了数据库的完整性约束，例如唯一性约束、外键约束等，需要检查实体类和数据库表的定义以及要插入或更新的数据是否符合约束

OptimisticLockException：这个异常在使用乐观锁时发生，表示在当前事务提交之前，数据已经被其他事务修改过，需要捕获这个异常并进行相应的处理，例如重试操作或通知用户

SQL 语法错误：如果在使用 @Query 注解编写 JPQL 或原生 SQL 查询时出现语法错误，会导致查询失败并抛出异常，需要仔细检查 SQL 语句的语法是否正确

数据库连接错误：如果应用程序无法连接到数据库，会导致异常。需要检查数据库的配置信息（URL、用户名、密码、驱动等）是否正确，以及数据库服务是否正常运行

**错误排查技巧**：

- 启用 SQL 日志：在 Spring Boot 的配置文件中启用 spring.jpa.show-sql=true 和 spring.jpa.properties.hibernate.format_sql=true 可以打印 Hibernate 生成的 SQL 语句，有助于理解 JPA 的行为和排查 SQL 相关的问题
- 查看详细的异常信息和堆栈跟踪：异常信息和堆栈跟踪通常包含了问题的详细描述和发生的位置，是排查错误的重要依据
- 使用数据库客户端工具：可以使用数据库客户端工具（如 MySQL Workbench、pgAdmin 等）直接执行 SQL 语句，验证查询逻辑和数据库状态
- 单元测试和集成测试：编写单元测试和集成测试可以帮助尽早发现潜在的问题



## 4、单元测试与集成测试策略

编写充分的单元测试和集成测试是保证应用程序质量的关键环节，对于 Spring Data JPA 应用程序，需要测试 Repository 的数据访问逻辑是否正确

**单元测试策略：**

单元测试通常关注于测试 Repository 方法的单个功能，例如保存、查询、更新、删除等，可以使用内存数据库（如 H2 或 Apache Derby）来创建一个隔离的测试环境，避免对实际的数据库产生影响

Spring Boot 提供了 @DataJpaTest 注解，可以方便地进行 JPA 相关的单元测试，使用 @DataJpaTest 注解会自动配置一个内存数据库，并提供一个 TestEntityManager，用于进行测试数据操作

```Java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByName_thenReturnUser() {
        // given
        User user = new User();
        user.setName("Test User");
        entityManager.persistAndFlush(user);

        // when
        User found = userRepository.findByName("Test User");

        // then
        assertThat(found.getName()).isEqualTo(user.getName());
    }
}
```

在这个例子中，@DataJpaTest 注解用于创建一个 JPA 相关的测试环境，TestEntityManager 用于在测试之前准备数据，userRepository 是需要测试的 Repository 实例，测试方法 whenFindByName_thenReturnUser() 测试了 findByName 方法是否能够正确地根据姓名查询到用户

**集成测试策略：**

集成测试通常关注于测试应用程序与实际数据库的交互是否正确，需要配置连接到真实数据库的测试环境，可以使用 Spring Boot 的 @SpringBootTest 注解来启动完整的 Spring 应用上下文，并在其中注入 Repository 实例进行测试

```Java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUsers_whenFindAll_thenReturnAllUsers() {
        // given
        User user1 = new User();
        user1.setName("User 1");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("User 2");
        userRepository.save(user2);

        // when
        Iterable<User> users = userRepository.findAll();

        // then
        assertThat(users).hasSize(2);
    }
}
```

在这个例子中，@SpringBootTest 注解用于启动完整的 Spring 应用上下文，@Transactional 注解用于在每个测试方法执行后回滚事务，以保证测试数据的隔离性，测试方法 givenUsers_whenFindAll_thenReturnAllUsers() 测试了 findAll 方法是否能够正确地返回所有用户

通过编写充分的单元测试和集成测试，可以确保 Spring Data JPA Repository 的数据访问逻辑的正确性，并提高应用程序的整体质量



## 5、不同场景下的 JPA 使用建议

在不同的应用场景下，使用 JPA 和 Spring Data JPA 的方式可能会有所不同，以下是一些针对不同场景的建议：

- **小型应用或原型开发**：可以利用 Spring Boot 的自动配置和 Spring Data JPA 的快速开发特性，快速搭建数据访问层，可以更多地使用方法名自动查询和基本的 CRUD 操作
- **中大型企业级应用**：需要更仔细地考虑实体关系的设计、加载策略的选择、事务的管理以及性能的优化，可能需要更多地使用 JPQL 查询、Specifications 和缓存机制
- **微服务架构**：每个微服务通常负责管理自己的数据，可以使用 Spring Data JPA 来实现每个微服务的数据访问层，需要注意在分布式环境下事务的管理和数据一致性的问题
- **高并发应用**：需要特别关注并发控制的问题，选择合适的锁机制（悲观锁或乐观锁），同时，需要进行充分的性能测试和调优，例如使用批量操作、优化 SQL 查询、合理使用缓存等
- **只读应用或报表系统**：可以将事务标记为只读，并积极使用二级缓存来提高查询性能
- **需要与其他数据存储技术集成的应用**：可以利用 Spring Data 提供的多个模块，将 Spring Data JPA 与其他数据存储技术（如 NoSQL 数据库）集成，以满足不同的数据存储和访问需求

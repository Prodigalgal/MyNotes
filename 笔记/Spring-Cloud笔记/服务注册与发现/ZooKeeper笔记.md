# ZooKeeper 简介

zookeeper 是一个分布式协调工具，可以实现注册中心功能

关闭 Linux 服务器防火墙后启动 zookeeper 服务器

zookeeper 服务器取代 Eureka 服务器作为服务注册中心



# ZooKeeperClient 端

## 1、引入 POM

```xml
<!-- SpringBoot 整合 zookeeper 客户端 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
</dependency>
```



## 2、配置YML

```yml
# 8004 表示注册到 zookeeper 服务器的支付服务提供者端口号
server:
  port: 8004
# 服务别名----注册 zookeeper 到注册中心名称
spring:
  application:
    name: cloud-provider-payment
  cloud:
    zookeeper:
    # ZK 的地址以及端口
      connect-string: 192.168.111.144:2181
```

## 3、添加注解

```java
@EnableDiscoveryClient // 该注解用于向使用 consul 或者 zookeeper 作为注册中心时注册服务
```



# 问题

## 1、版本问题

![image-20220115183505789](images/image-20220115183505789.png) 

![image-20220115183453424](images/image-20220115183453424.png) 

![image-20220115183458200](images/image-20220115183458200.png) 

```xml
<!-- SpringBoot整合zookeeper客户端 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
    <!--先排除自带的zookeeper3.5.3-->
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--添加zookeeper3.4.9版本-->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.9</version>
</dependency>
```






























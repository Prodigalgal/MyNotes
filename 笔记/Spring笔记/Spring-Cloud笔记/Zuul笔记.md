# Zuul简介

Zuul包含了对请求的**路由**和**过滤**两个最主要的功能：

- 路由功能负责将外部请求转发到具体的微服务实例上，是实现外部访问统一入口的基础。

- 过滤器功能则负责对请求的处理过程进行干预，是实现请求校验、服务聚合等功能的基础。

Zuul和Eureka进行整合，将Zuul自身注册为Eureka服务治理下的应用，同时从Eureka中获得其他微服务的消息，也即以后的访问微服务都是通过Zuul跳转后获得。

    注意：Zuul服务最终还是会注册进Eureka

提供=代理+路由+过滤三大功能。

# 路由基本配置

## 构建步骤

 新建Module模块microservicecloud-zuul-gateway-9527

 POM文件，引入zuul路由网关依赖

```xml
 
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
 
  <parent>
   <groupId>com.atguigu.springcloud</groupId>
   <artifactId>microservicecloud</artifactId>
   <version>0.0.1-SNAPSHOT</version>
  </parent>
 
  <artifactId>microservicecloud-zuul-gateway-9527</artifactId>
 
  <dependencies>
   <!-- zuul路由网关 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-zuul</artifactId>
   </dependency> 
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-eureka</artifactId>
   </dependency>
   <!-- actuator监控 -->
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-actuator</artifactId>
   </dependency>
   <!--  hystrix容错-->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-hystrix</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-config</artifactId>
   </dependency>
   <!-- 日常标配 -->
   <dependency>
     <groupId>com.atguigu.springcloud</groupId>
     <artifactId>microservicecloud-api</artifactId>
     <version>${project.version}</version>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-jetty</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-test</artifactId>
   </dependency>
   <!-- 热部署插件 -->
   <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>springloaded</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-devtools</artifactId>
   </dependency>
  </dependencies>
 
</project>
```

yml文件

```yml
server: 
  port: 9527
 
spring: 
  application:
    name: microservicecloud-zuul-gateway
 
eureka: 
  client: 
    service-url: 
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka,http://eureka7003.com:7003/eureka  
  instance:
    instance-id: gateway-9527.com
    prefer-ip-address: true 
 
 
info:
  app.name: atguigu-microcloud
  company.name: www.atguigu.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
```

hosts修改		127.0.0.1  myzuul.com

主启动类添加注解@EnableZuulProxy

```java
package com.atguigu.springcloud;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
 
@SpringBootApplication
@EnableZuulProxy
public class Zuul_9527_StartSpringCloudApp
{
  public static void main(String[] args)
  {
   SpringApplication.run(Zuul_9527_StartSpringCloudApp.class, args);
  }
}
```

## 测试

三个eureka集群

一个服务提供类microservicecloud-provider-dept-8001

一个路由

http://localhost:8001/dept/get/2

http://myzuul.com:9527/microservicecloud-dept/dept/get/2

## 路由访问映射规则

修改工程microservicecloud-zuul-gateway-9527

代理名称，修改yml文件，但是旧路径和新路径都可以访问

```yml
zuul: 
  routes: 
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
    
修改前，访问路径
http://myzuul.com:9527/microservicecloud-dept/dept/get/2
修改后，访问路径
http://myzuul.com:9527/mydept/dept/get/1
```

再次修改yml文件，使得旧路径无法访问，单个具体可以具体写明，多个可以用"*"替代

```yml
 
zuul: 
  ignored-services: microservicecloud-dept  # 忽略服务名称
  routes: 
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
```

设置统一公共前缀

```yml
 zuul: 
  prefix: /atguigu
  ignored-services: "*" # 忽略所有服务名称
  routes: 
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
```

最后成果http://myzuul.com:9527/mircoservice/mydept/dept/get/1

```yml
server: 
  port: 9527
 
spring: 
  application:
    name: microservicecloud-zuul-gateway
 
zuul: 
  prefix: /mircoservice
  ignored-services: "*"
  routes: 
    mydept.serviceId: microservicecloud-dept
    mydept.path: /mydept/**
 
eureka: 
  client: 
    service-url: 
      defaultZone: http://eureka7001.com:7001/eureka,http://eureka7002.com:7002/eureka,http://eureka7003.com:7003/eureka  
  instance:
    instance-id: gateway-9527.com
    prefer-ip-address: true 
 
info:
  app.name: atguigu-microcloud
  company.name: www.atguigu.com
  build.artifactId: $project.artifactId$
  build.version: $project.version$
```

**注意**：

http://myzuul.com:9527/mircoservice/mydept/haha/dept/get/1

/myzuul.com:9527 根据路由设置，后面紧跟前缀

/mircoservice 根据路由设置

/mydept 根据路由设置

/haha 根据特定服务的context设置，如果有的话，没有直接的话调用/dept/get/1
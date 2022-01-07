# Feign构建

## 步骤

参考microservicecloud-consumer-dept-80

新建microservicecloud-consumer-dept-feign，新建Consumer模块

microservicecloud-consumer-dept-feign工程pom.xml修改，主要**添加对feign的支持**。

```xml
 <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-feign</artifactId>
</dependency>
```

修改microservicecloud-api工程，**添加入feign依赖** 

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
 
  <parent><!-- 子类里面显示声明才能有明确的继承表现，无意外就是父类的默认版本否则自己定义 -->
   <groupId>com.atguigu.springcloud</groupId>
   <artifactId>microservicecloud</artifactId>
   <version>0.0.1-SNAPSHOT</version>
  </parent>
 
  <artifactId>microservicecloud-api</artifactId><!-- 当前Module我自己叫什么名字 -->
 
  <dependencies><!-- 当前Module需要用到的jar包，按自己需求添加，如果父类已经包含了，可以不用写版本号 -->
   <dependency>
     <groupId>org.projectlombok</groupId>
     <artifactId>lombok</artifactId>
   </dependency>
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-feign</artifactId>
   </dependency>
  </dependencies>
</project>
```

api模块新建DeptClientService接口并**新增注解@FeignClient ** 

```java
package com.atguigu.springcloud.service;
 
import java.util.List;
 
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
 
import com.atguigu.springcloud.entities.Dept;
 
// 表明是FeignClient端，value填写服务名
@FeignClient(value = "MICROSERVICECLOUD-DEPT")
public interface DeptClientService{
    
  @RequestMapping(value = "/dept/get/{id}",method = RequestMethod.GET)
  public Dept get(@PathVariable("id") long id);
 
  @RequestMapping(value = "/dept/list",method = RequestMethod.GET)
  public List<Dept> list();
 
  @RequestMapping(value = "/dept/add",method = RequestMethod.POST)
  public boolean add(Dept dept);
    
}
```

 microservicecloud-consumer-dept-feign工程即Consumer模块修改 Controller，添加上一步新建的DeptClientService接口，使用接口调用。

```java
package Core.Controller;

import Core.pojo.Dept;
import Core.service.DeptClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeptConsumerController {

    @Autowired
    private DeptClientService service;

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept get(@PathVariable("id") Long id)
    {
        return this.service.get(id);
    }

    @RequestMapping(value = "/consumer/dept/list")
    public List<Dept> list()
    {
        return this.service.list();
    }

    @RequestMapping(value = "/consumer/dept/add")
    public Object add(Dept dept)
    {
        return this.service.add(dept);
    }

}
```

microservicecloud-consumer-dept-feign工程修改主启动类，**添加@EnableFeignClients注解** 

```java
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages= {"Core"}) // 扫描所有标注了@FeignClient注解的类
public class Consumer_Dept_Feign {
    public static void main(String[] args) {
        SpringApplication.run(Consumer_Dept_Feign.class, args);
    }
}
```

## 测试

启动3个eureka集群

启动3个Provider微服务8001/8002/8003

启动Feign启动

**注意**：Feign自带负载均衡配置项

## 总结

 Feign**通过接口的方法**调用Rest服务（之前是Ribbon+RestTemplate），
该请求发送给Eureka服务器（http://MICROSERVICECLOUD-DEPT/dept/list）,
通过Feign直接找到服务接口，由于在进行服务调用的时候融合了Ribbon技术，所以也支持负载均衡作用。

 

 
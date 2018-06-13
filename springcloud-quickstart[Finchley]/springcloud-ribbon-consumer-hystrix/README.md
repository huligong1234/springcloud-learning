## 服务治理组件-服务容错保护 Spring Cloud Hystrix 示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.0.BUILD-SNAPSHO
* SpringCloud:Finchley.BUILD-SNAPSHOT

### 三、配置实现

#### 3.1 pom.xml配置支持Hystrix
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```

#### 3.2 启动类增加注解支持
```java
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudRibbonConsumerHystrixApplication {

	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudRibbonConsumerHystrixApplication.class, args);
	}
}
```

#### 3.3  配置 application.properties
```properties
spring.application.name=ribbon-hystrix-consumer
server.port=8091
eureka.client.service-url.defaultZone=http://localhost:1111/eureka
```

#### 3.4  编写HelloService类来消费(调用)服务
```java
package org.jeedevframework.springcloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class HelloService {

	@Autowired
	private RestTemplate restTemplate;
	
	@HystrixCommand(fallbackMethod = "helloFallback")
	public String helloService(){
		return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
	}
	
	public String helloFallback(){
		return "error";
	}
}
```

#### 3.5  编写ConsumerController类实现简单的通过Hystrix包装后调用服务
```java
package org.jeedevframework.springcloud.web;

import org.jeedevframework.springcloud.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

	@Autowired
	private HelloService helloService;
	
	@GetMapping(value="/ribbon-hystrix-consumer")
	public String helloConsumer(){
		return helloService.helloService();
	}
}
```

###  四、测试准备
* 启动 springcloud-eureka-server(服务注册中心)
* 启动 springcloud-eureka-client(hello-service服务,服务提供者)
* 访问 http://localhost:1111/ 服务注册中心，查看相关服务是否已注册

### 五、测试
* 启动 Application (springcloud-ribbon-consumer-hystrix)
* 访问 http://localhost:1111/ 服务注册中心，查看ribbon-consumer-hystrix服务是否已注册
* 访问  http://localhost:8091/ribbon-hystrix-consumer 正确返回“Hello World”内容  
  然后关闭springcloud-eureka-client(hello-service服务)  
  再次访问 http://localhost:8091/ribbon-hystrix-consumer 正确返回“error”内容 即为测试成功  
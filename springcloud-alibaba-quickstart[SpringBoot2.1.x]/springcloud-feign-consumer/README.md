## 服务治理组件-服务消费者 Spring Cloud Alibaba Feign 声明式服务调用  示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.17.RELEASE
* SpringCloud:Greenwich.RELEASE
* SpringCloudAlibaba:2.1.2.RELEASE

### 三、配置实现

#### 3.1 pom.xml配置支持Feign
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>${spring-cloud-alibaba.version}</version>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

#### 3.2 启动类增加注解支持
```java
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudFeignConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudFeignConsumerApplication.class, args);
	}
}
```

#### 3.3  配置 application.properties
```properties
spring.application.name=feign-consumer
server.port=9001
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```

feign可以指定client类型
```yaml
feign:
  httpclient:
    enabled: true
  okhttp:
    enabled: false
```

#### 3.4  编写HelloService接口类 来声明服务
```java
package org.jeedevframework.springcloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("hello-service")
public interface HelloService {

	@RequestMapping("/hello")
	String hello();
}

```

#### 3.5  编写ConsumerController类实现简单的声明式服务调用服务
```java
package org.jeedevframework.springcloud.web;

import org.jeedevframework.springcloud.service.HelloSentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

	@Autowired
	private HelloService helloService;
	
	@GetMapping(value="/feign-consumer")
	public String helloConsumer(){
		return helloService.hello();
	}
}

```

###  四、测试准备
* 启动 nacos-server(服务治理中心)
  Windows: D:/opt/soft/nacos/bin/startup.cmd -m standalone
  Linux: sh /opt/soft/nacos/bin/startup.sh -m standalone
* 启动 springcloud-hello-service(hello-service服务,服务提供者)
* 访问 http://localhost:8084/nacos 服务治理中心，查看相关服务是否已注册

### 五、测试
* 启动 Application (springcloud-feign-consumer)
* 访问 http://localhost:8084/nacos 服务注册中心，查看feign-consumer服务是否已注册
* 访问 服务接口 http://localhost:9001/feign-consumer 正确返回“Hello World”内容即为成功
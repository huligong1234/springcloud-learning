## 服务治理组件-分布式配置中心 Spring Cloud Config Client 客户端 示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:1.5.13.RELEASE
* SpringCloud:Edgware.SR3

### 三、配置实现

#### 3.1 pom.xml配置支持Config
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>

<!--增加如下两个依赖，可自动支持失败快速响应和重试-->
<dependency>
	<groupId>org.springframework.retry</groupId>
	<artifactId>spring-retry</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

#### 3.2 启动类增加注解支持
```java
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudConfigClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudConfigClientApplication.class, args);
	}
}
```


#### 3.3  新增配置 bootstrap.properties
使用新增的 bootstrap.properties文件配置，而不使用默认的application.properties进行配置，注意是考虑加载顺序，实际可根据具体业务情况决定
```properties
spring.application.name=config-client
server.port=7002
eureka.client.service-url.defaultZone=http://localhost:1111/eureka/

spring.cloud.config.discovery.enabled=true
spring.cloud.config.discovery.serviceId=config-server
spring.cloud.config.profile=dev
```


#### 3.4  编写TestController类实现获取配置中心的属性值
```java
package org.jeedevframework.springcloud.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {

	@Value("${from}")
	private String from;
	
	@RequestMapping("/from")
	public String from(){
		return this.from;
	}
}

```


###  四、测试准备
* 启动 springcloud-eureka-server(服务注册中心)
* 启动 springcloud-config-server
* 访问 http://localhost:1111/ 服务注册中心，查看config-server服务是否已注册

### 五、测试
* 启动 Application (springcloud-config-client)
* 访问 http://localhost:1111/ 服务注册中心，查看config-client服务是否已注册
* 访问 http://localhost:7002/from 返回内容 “local-dev-1.0” 表示测试成功
可以直接修改相应的springcloud-config-server 中config-client-dev.properties文件中from属性值，然后再访问查看相应的变化
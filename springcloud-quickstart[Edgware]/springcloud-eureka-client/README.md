## 服务治理组件-服务提供者 Spring Cloud Eureka Client 示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:1.5.13.RELEASE
* SpringCloud:Edgware.SR3

### 三、配置实现
#### 3.1 pom.xml配置支持Eureka
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions>  
        <exclusion>   
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-logging</artifactId>  
        </exclusion>  
    </exclusions>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>  
     <artifactId>spring-boot-starter-log4j</artifactId>
     <version>1.3.8.RELEASE</version>
</dependency> 
```

#### 3.2  启动类增加注解支持

通过`@EnableDiscoveryClient`注解让该应用注册为`Eureka`客户端应用，以获得服务发现的能力。


```java
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudEurekaClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudEurekaClientApplication.class, args);
	}
}
```

#### 3.3  配置 application.properties
```properties
spring.application.name=hello-service
server.port=8080
eureka.client.service-url.defaultZone=http://localhost:1111/eureka/
```

#### 3.4 编写HelloController类实现简单的hello接口服务
```java
package org.jeedevframework.springcloud.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private DiscoveryClient client;
	
	@GetMapping("/hello")
	public String index(){
		List<String> serviceNameList = client.getServices();
		logger.info("serviceNameList:"+serviceNameList);
		for(String serviceId : serviceNameList){
			List<ServiceInstance> serviceClientList = client.getInstances(serviceId);
			if(null != serviceClientList && !serviceClientList.isEmpty()){
				for(ServiceInstance serviceClient : serviceClientList){
					logger.info("serviceClient:"+serviceClient.getUri()+","+serviceClient.getHost()+":"+serviceClient.getPort()+","+serviceClient.getServiceId());
				}
			}
		}
		return "Hello World";
	}
}

```

###  四、测试准备
* 启动 springcloud-eureka-server(服务注册中心)

### 五、测试
* 启动 Application (springcloud-eureka-client)
* 访问 http://localhost:1111/ 服务注册中心，查看hello-service服务是否已注册
* 访问 服务接口 http://localhost:8080/hello 正确返回“Hello World”内容即为成功
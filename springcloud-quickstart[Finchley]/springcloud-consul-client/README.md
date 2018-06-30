## 服务治理组件-服务提供者 Spring Cloud Consul 示例学习

### 一、概述

Consul 官网：https://www.consul.io/

### 二、依赖版本信息
* JDK1.8
* consul: 1.2.0
* SpringBoot:2.1.0.BUILD-SNAPSHO
* SpringCloud:Finchley.BUILD-SNAPSHOT

### 三、配置实现
#### 3.1 pom.xml配置支持Consul
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### 3.2  启动类增加注解支持

通过`@EnableDiscoveryClient`注解让该应用注册为`Consul`客户端应用，以获得服务发现的能力。


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

spring.cloud.consul.host=127.0.0.1
spring.cloud.consul.port=2222
spring.cloud.consul.enabled=true
spring.cloud.consul.discovery.enabled=true
spring.cloud.consul.discovery.tags=dev
spring.cloud.consul.discovery.hostname=127.0.0.1
spring.cloud.consul.discovery.default-query-tag=dev
spring.profiles.active=dev
```

#### 3.4 编写HelloController类实现简单的hello接口服务
```java
package org.jeedevframework.springcloud.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
* 启动 consul (服务注册中心)
```
$ consul agent -dev -http-port=2222
```

### 五、测试
* 启动 Application (springcloud-consul-client)
* 访问 http://localhost:2222/ 服务注册中心，查看hello-service服务是否已注册
* 访问 服务接口 http://localhost:8080/hello 正确返回“Hello World”内容即为成功



### 六、参考资料

* Spring Cloud Consul 中文文档 参考手册  
https://springcloud.cc/spring-cloud-consul.html
* Consul 简介、安装、常用命令的使用
https://blog.csdn.net/u010046908/article/details/61916389
* spring cloud搭建微服务second-fiberhome（二）：结合consul实现服务注册与服务发现
https://blog.csdn.net/moyu_2012/article/details/72781001

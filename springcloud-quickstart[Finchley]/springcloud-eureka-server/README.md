## 服务治理组件-服务注册中心 SpringCloud Eureka Server 示例学习

### 一、概述


###  二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.0.BUILD-SNAPSHO
* SpringCloud:Finchley.BUILD-SNAPSHOT

### 三、配置实现

#### 3.1 pom.xml配置支持Eureka
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

#### 3.2 启动类增加注解支持
```java
@EnableEurekaServer
@SpringBootApplication
public class SpringcloudEurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudEurekaServerApplication.class, args);
	}
}
```

#### 3.3  配置 application.properties
```properties
spring.application.name=eureka-server
server.port=1111

eureka.instance.hostname=localhost
eureka.client.fetch-registry=false
eureka.client.register-with-eureka=false
eureka.client.service-url.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
```

###  四、测试准备


### 五、测试
* 访问 http://localhost:1111/ 服务注册中心
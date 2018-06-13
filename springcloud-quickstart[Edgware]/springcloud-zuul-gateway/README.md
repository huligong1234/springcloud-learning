## 服务治理组件-API网关服务  Spring Cloud Zuul 示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:1.5.13.RELEASE
* SpringCloud:Edgware.SR3

### 配置实现
#### 3.1 pom.xml配置支持Zuul
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zuul</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-eureka</artifactId>
</dependency>
```


#### 3.2 启动类增加注解支持
```java
@EnableZuulProxy
@SpringCloudApplication
public class SpringcloudZuulGatewayApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudZuulGatewayApplication.class, args);
	}
}
```
#### 3.3 增加配置 application.yml
```yaml
server:
  port: 5555
  
eureka:
  client:
    service-url:
      defaultZone: http://localhost:1111/eureka/
      
spring:
  application:
    name: zuul-gateway
    
zuul:
  routes:
    api-a:
      path: /api-a/**
      serviceId: hello-service
    api-b:
      path: /api-b/**
      serviceId: feign-consumer
```

###  四、测试准备
* 启动 springcloud-eureka-server(服务注册中心)
* 启动 springcloud-eureka-client(hello-service服务,服务提供者)
* 启动 springcloud-feign-consumer(声明式服务调用示例)
* 访问 http://localhost:1111/ 服务注册中心，查看相关服务是否已注册

### 五、测试
* 启动 Application (springcloud-zuul-gateway)
* 访问 http://localhost:1111/ 服务注册中心，查看zuul-gateway服务是否已注册
* 访问 http://localhost:5555/api-a/hello  正确返回“Hello World”内容即为成功
* 访问 http://localhost:5555/api-b/feign-consumer  正确返回“Hello World”内容即为成功
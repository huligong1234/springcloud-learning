## 服务治理组件-API网关服务  Spring Cloud Gateway 示例学习

### 一、概述
* 官方文档：http://cloud.spring.io/spring-cloud-gateway/single/spring-cloud-gateway.html

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.0.1.RELEASE
* SpringCloud:Finchley.BUILD-SNAPSHOT

### 配置实现
#### 3.1 pom.xml配置支持gateway
```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-gateway</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	</dependency>
</dependencies>
```

#### 3.2 启动类增加注解支持
```java
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudGatewayApplication.class, args);
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
    name: springcloud-gateway
  cloud:
    gateway: 
      #locator:
       # enabled: true
      routes:
      - id: hello-service
        uri: lb://hello-service
        #uri: http://localhost:8080
        predicates:
        - Path=/api-a/**
        filters:
        - StripPrefix=1
      - id: feign-consumer
        uri: lb://feign-consumer
        predicates:
        - Path=/api-b/**
        filters:
        - StripPrefix=1
```

#### 3.4 增加Java代码方式路由配置

```java
package org.jeedevframework.springcloud.config;

import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

	 @Bean
	    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	        StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
	        config.setParts(1);
	        return builder.routes()
	                .route("hello-service", r -> r.path("/api-c/**").filters(f -> f.stripPrefix(1)).uri("lb://hello-service"))
	                .route("feign-consumer", r -> r.path("/api-d/**").filters(f -> f.stripPrefix(1)).uri("lb://feign-consumer"))
	                .build();
	    }
}
```

###  四、测试准备
* 启动 springcloud-eureka-server(服务注册中心)
* 启动 springcloud-eureka-client(hello-service服务,服务提供者)
* 启动 springcloud-feign-consumer(声明式服务调用示例)
* 访问 http://localhost:1111/ 服务注册中心，查看相关服务是否已注册

### 五、测试
* 启动 Application (springcloud-gateway)
* 访问 http://localhost:1111/ 服务注册中心，查看springcloud-gateway服务是否已注册
* 访问 http://localhost:5555/api-a/hello  正确返回“Hello World”内容即为成功
* 访问 http://localhost:5555/api-b/feign-consumer  正确返回“Hello World”内容即为成功
* 访问 http://localhost:5555/api-c/hello  正确返回“Hello World”内容即为成功
* 访问 http://localhost:5555/api-d/feign-consumer  正确返回“Hello World”内容即为成功

### 相关资料
* Spring Cloud Gateway 入门  https://juejin.im/post/5aa4eacbf265da237a4ca36f
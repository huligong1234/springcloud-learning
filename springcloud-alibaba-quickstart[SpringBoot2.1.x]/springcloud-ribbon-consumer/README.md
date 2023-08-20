## 服务治理组件-服务消费者 Spring Cloud Ribbon 客户端负载均衡调用  示例学习

### 一、概述
https://nacos.io/

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.17.RELEASE
* SpringCloud:Greenwich.RELEASE
* SpringCloudAlibaba:2.1.2.RELEASE


### 三、配置实现
#### 3.1 pom.xml配置支持Ribbon
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
```

#### 3.2 启动类增加注解支持
```java
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudRibbonConsumerApplication {

  @Bean
  @LoadBalanced
  RestTemplate restTemplate(){
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringcloudRibbonConsumerApplication.class, args);
  }
}
```

#### 3.2 配置 application.properties
```properties
spring.application.name=ribbon-consumer
server.port=8090
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```

ribbon其他常用参数
```yaml
# 全局参数设置
ribbon:
  ReadTimeout: 120000
  ConnectTimeout: 10000
  SocketTimeout: 10000
  MaxAutoRetries: 0
  MaxAutoRetriesNextServer: 1
```


#### 3.4 编写ConsumerController类实现简单的接口调用

服务消费者直接通过调用被`@LoadBalanced`注解修饰过的`RestTemplate`来实现面向服务的接口调用  
另外，访问的地址是服务名HELLO-SERVICE,而不是具体的地址

```java
package org.jeedevframework.springcloud.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping(value="/ribbon-consumer")
  public String helloConsumer(){
    return restTemplate.getForEntity("http://hello-service/hello", String.class).getBody();
  }
}
```

### 四、 测试准备
* 启动 nacos-server(服务治理中心)
  Windows: D:/opt/soft/nacos/bin/startup.cmd -m standalone
  Linux: sh /opt/soft/nacos/bin/startup.sh -m standalone
* 启动 springcloud-hello-service(hello-service服务,服务提供者)
```shell
可通过如下方式运行多个实例
打包：mvn clean compile package -Dmaven.test.skip=true
通过不同端口号启动：
java -jar springcloud-hello-service-0.0.1-SNAPSHOT.jar --server.port=7091
java -jar springcloud-hello-service-0.0.1-SNAPSHOT.jar --server.port=7092
```

* 访问 http://localhost:8084/nacos 服务注册中心，查看相关服务是否已注册

### 五、 测试
* 启动 Application (springcloud-ribbon-consumer)
* 访问 服务接口 http://localhost:8090/ribbon-consumer 正确返回“Hello World”内容即为成功
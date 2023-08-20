## 服务治理组件-服务容错保护 Spring Cloud Alibaba Sentinel 示例学习

### 一、概述
常见的容错组件有 Hystrix，Resilience4J，Sentinel。
Sentinel (分布式系统的流量防卫兵) 是阿里开源的一套用于服务容错的综合性解决方案。它以流量
为切入点, 从流量控制、熔断降级、系统负载保护等多个维度来保护服务的稳定性。
Sentinel 具有以下特征:
丰富的应用场景：Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景, 例如秒杀（即
突发流量控制在系统容量可以承受的范围）、消息削峰填谷、集群流量控制、实时熔断下游不可用
应用等。
完备的实时监控：Sentinel 提供了实时的监控功能。通过控制台可以看到接入应用的单台机器秒
级数据, 甚至 500 台以下规模的集群的汇总运行情况。
广泛的开源生态：Sentinel 提供开箱即用的与其它开源框架/库的整合模块, 例如与 Spring
Cloud、Dubbo、gRPC 的整合。只需要引入相应的依赖并进行简单的配置即可快速地接入
Sentinel。
完善的 SPI 扩展点：Sentinel 提供简单易用、完善的 SPI 扩展接口。您可以通过实现扩展接口来快
速地定制逻辑。例如定制规则管理、适配动态数据源等。

Sentinel 分为两个部分:
核心库（Java 客户端）不依赖任何框架/库,能够运行于所有 Java 运行时环境，同时对 Dubbo /
Spring Cloud 等框架也有较好的支持。
控制台（Dashboard）基于 Spring Boot 开发，打包后可以直接运行，不需要额外的 Tomcat 等
应用容器

官网：http://sentinelguard.io/zh-cn/index.html
http://sentinelguard.io/zh-cn/docs/open-source-framework-integrations.html
https://github.com/alibaba/spring-cloud-alibaba/wiki/Sentinel

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.17.RELEASE
* SpringCloud:Greenwich.RELEASE
* SpringCloudAlibaba:2.1.2.RELEASE
* Sentinel:1.8.6

### 三、配置实现

#### 3.1 pom.xml配置支持Sentinel
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
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
    <version>${spring-cloud-alibaba.version}</version>
</dependency>
```

#### 3.2 启动类增加注解支持
```java
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudConsumerSentinelApplication {

	@Bean
	@LoadBalanced
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudConsumerSentinelApplication.class, args);
	}
}
```

#### 3.3  配置 application.properties
```properties
spring.application.name=sentinel-consumer
server.port=8091
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
spring.cloud.sentinel.transport.dashboard=127.0.0.1:8081
```

#### 3.4  编写HelloSentinelService类来消费(调用)服务
```java
package org.jeedevframework.springcloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloSentinelService {

  @Autowired
  private RestTemplate restTemplate;

  @SentinelResource(value = "HelloSentinelService#hello", blockHandler = "helloBlockHandler", fallback = "helloFallback")
  public String hello(){
    return restTemplate.getForEntity("http://hello-service/hello", String.class).getBody();
  }

  // blockHandler 函数，原方法调用被限流/降级/系统保护的时候调用
  public String helloBlockHandler(String id, BlockException ex) {
    ex.printStackTrace();
    return "Block-hello";
  }

  public String helloFallback(Throwable t) {
    t.printStackTrace();
    if (BlockException.isBlockException(t)) {
      return "Blocked by Sentinel: " + t.getClass().getSimpleName();
    }
    return "Error, failed: " + t.getClass().getCanonicalName();
  }
}
```

#### 3.5  编写ConsumerController类实现简单的通过Sentinel包装后调用服务

```java
package org.jeedevframework.springcloud.web;

import org.jeedevframework.springcloud.service.HelloSentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

  @Autowired
  private HelloSentinelService helloSentinelService;

  @GetMapping(value="/sentinel-consumer")
  public String helloConsumer(){
    return helloSentinelService.hello();
  }
}
```

###  四、测试准备
* 启动 nacos-server(服务治理中心)
  Windows: D:/opt/soft/nacos/bin/startup.cmd -m standalone
  Linux: sh /opt/soft/nacos/bin/startup.sh -m standalone
* 启动 Sentinel-Dashboard(Sentinel控制台)
  java -Dserver.port=8088 -Dcsp.sentinel.dashboard.server=localhost:8088 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
* 启动 springcloud-hello-service(hello-service服务,服务提供者)
* 访问 http://localhost:8084/nacos Nacos服务治理中心，查看相关服务是否已注册

### 五、测试
* 启动 Application (springcloud-sentinel-consumer)
* 访问 http://localhost:8084/nacos Nacos服务治理中心，查看sentinel-consumer服务是否已注册
* 访问  http://localhost:8091/sentinel-consumer 正确返回“Hello World”内容  
  然后关闭springcloud-hello-service(hello-service服务)  
  再次访问 http://localhost:8091/sentinel-consumer 正确返回“error”内容 即为测试成功  
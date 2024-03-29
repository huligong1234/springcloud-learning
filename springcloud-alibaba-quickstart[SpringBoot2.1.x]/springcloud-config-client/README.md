## 服务治理组件-分布式配置中心 Spring Cloud Nacos Config Client 客户端 示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.17.RELEASE
* SpringCloud:Greenwich.RELEASE
* SpringCloudAlibaba:2.1.2.RELEASE

### 三、配置实现

#### 3.1 pom.xml配置支持Config
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
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
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
spring.profiles.active=dev

spring.cloud.nacos.config.server-addr=127.0.0.1:8848

#spring.cloud.nacos.config.enabled=true
#spring.cloud.nacos.config.name=config-client
#spring.cloud.nacos.config.file-extension=properties

```


#### 3.4  编写TestController类实现获取配置中心的属性值
```java
package org.jeedevframework.springcloud.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Value("${from:}")
    private String from;

    @RequestMapping("/from")
    public String from(){
        return this.from;
    }

    @RequestMapping("/from2")
    public String from2(){
        return configurableApplicationContext.getEnvironment().getProperty("from");
    }
}
```


###  四、测试准备
* 启动 nacos-server(服务治理中心)
  Windows: D:/opt/soft/nacos/bin/startup.cmd -m standalone
  Linux: sh /opt/soft/nacos/bin/startup.sh -m standalone
* 访问 http://localhost:8084/nacos 服务治理中心，查看相关服务是否已注册

### 五、测试
* 启动 Application (springcloud-config-client)
* 访问 http://localhost:8084/nacos 服务治理中心，查看config-client服务是否已注册
* 访问 http://localhost:7002/from 返回内容 “local-dev-1.0” 表示测试成功
可以直接修改相应的nacos-server 中config-client-dev.properties文件中from属性值，然后再访问查看相应的变化

![](./attachment/nacos-config01.png)
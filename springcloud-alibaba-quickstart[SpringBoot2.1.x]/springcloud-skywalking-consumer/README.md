## 服务治理组件-链路追踪 Spring Cloud Skywalking 示例学习

### 一、概述

SkyWalking是分布式链路调用服务监控组件，
微服务上线后，需要监控运行指标，比如系统的吞吐量，服务响应时间，CPU内存暂用率，异常等信息，
它通过Agent收集日志，可以把数据存储在ES，MySQL等介质中。

SkyWalking官网：https://skywalking.apache.org/
Nacos官网：https://nacos.io/

Spring Cloud Alibaba系列(二)微服务监控组件Skywalking的简单使用
https://www.cnblogs.com/fxhui/p/17241544.html

【skywalking 部署测试】
https://blog.csdn.net/qq_39738963/article/details/128356059

skywalking9.3.0部署
https://blog.csdn.net/kang5789/article/details/130851756


### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.17.RELEASE
* SpringCloud:Greenwich.RELEASE
* SpringCloudAlibaba:2.1.2.RELEASE
* skywalking:8.16.0

### 三、Skywalking服务安装和启动
#### 3.1.下载安装包
wget --no-check-certificate https://dlcdn.apache.org/skywalking/9.5.0/apache-skywalking-apm-9.5.0.tar.gz
解压安装到/opt/soft/skywalking
由于skywalking-9.5依赖JDK11，如果本地JDK默认环境是JDK8，则需要显式指定依赖JDK11路径：
修改 /opt/soft/skywalking/bin/oapService.sh 和 /opt/soft/skywalking/bin/webappService.sh
顶部增加 export JAVA11_HOME=/opt/soft/jdk-11.0.20
涉及JAVA_HOME都修改为JAVA11_HOME：
_RUNJAVA=${JAVA11_HOME}/bin/java
[ -z "$JAVA11_HOME" ] && _RUNJAVA=java

#### 3.2.创建数据库
在MySQL中创建skywalking_dev库
```mysql
create user 'skywalking'@'localhost' identified by 'skywalking123';  
create user 'skywalking'@'%' identified by 'skywalking123';

create database skywalking_dev DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
grant all privileges on `skywalking_dev`.* to 'skywalking'@'%';

flush privileges;
```

#### 3.3.修改配置
进入/opt/soft/skywalking/config目录，打开application.yml，修改存储介质为MySQL
```yaml
storage:
  selector: ${SW_STORAGE:mysql}
  mysql:
    properties:
      jdbcUrl: ${SW_JDBC_URL:"jdbc:mysql://127.0.0.1:3306/skywalking_dev?rewriteBatchedStatements=true&allowMultiQueries=true"}
      dataSource.user: ${SW_DATA_SOURCE_USER:skywalking}
      dataSource.password: ${SW_DATA_SOURCE_PASSWORD:skywalking123}
```
#### 3.4.上传MySQL驱动
在/opt/soft/skywalking/oap-libs/中上传mysql驱动mysql-connector-java-8.0.28.jar

#### 3.5.启动Skywalking
Windows: D:/opt/soft/skywalking/bin/startup.bat
Linux: sh /opt/soft/skywalking/bin/startup.sh
启动后在浏览器打开链接http://localhost:8080/

### 四、配置实现
#### 4.1 pom.xml配置支持skywalking

**注：pom.xml和log4j-spring.xml配置调整非必须，只有期望在日志文件中记录TraceId时才需要**

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>${spring-cloud-alibaba.version}</version>
</dependency>

<!--skywalking traceId 记录到logback日志-->
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-logback-1.x</artifactId>
    <version>8.16.0</version>
</dependency>
<dependency>
    <groupId>org.apache.skywalking</groupId>
    <artifactId>apm-toolkit-trace</artifactId>
    <version>8.16.0</version>
</dependency>
```

#### 4.2 启动类增加注解支持
```java
@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudSkywalkingClientApplication {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudSkywalkingClientApplication.class, args);
    }
}
```

#### 4.3 配置 application.properties
```properties
spring.application.name=skywalking-client
server.port=8091
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```


#### 4.4 编写ConsumerController类实现简单的接口调用

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
		return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
	}
}
```

#### 4.5 编写logback-spring.xml以在日志中记录TraceId
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="ch.qos.logback.core.encoder.LayoutWrappingEncoder">
            <layout class="org.apache.skywalking.apm.toolkit.log.logback.v1.x.TraceIdPatternLogbackLayout">
                <pattern>%d{HH:mm:ss.SSS} [%tid] [%thread] %-5level %logger{36} - %msg%n</pattern>
            </layout>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```

### 四、 测试准备
* 启动 nacos-server(Nacos服务治理中心)
* 启动 springcloud-hello-service(hello-service服务,服务提供者)
```shell
可通过如下方式运行多个实例
打包：mvn clean compile package -Dmaven.test.skip=true
通过不同端口号启动：
java -javaagent:D:\skywalking-agent\skywalking-agent.jar
-Dskywalking.agent.service_name=hello-service
-Dskywalking.collector.backend_service=127.0.0.1:11800
--server.port=7091
-jar target\springcloud-hello-service-0.0.1-SNAPSHOT.jar

java -javaagent:D:\skywalking-agent\skywalking-agent.jar
-Dskywalking.agent.service_name=hello-service
-Dskywalking.collector.backend_service=127.0.0.1:11800
--server.port=7092
-jar target\springcloud-hello-service-0.0.1-SNAPSHOT.jar
```
agent.service_name：指定服务名称，必须为字符串英文标识；
collector.backend_service：指定OAP收集数据的地址；
添加-javaagent:/path/to/skywalking-package/agent/skywalking-agent.jar作为虚拟机参数，必须在-jar前面的参数；
其中agent/agent.config配置文件必须和skywalking-agent.jar在同一级目录；
默认日志输出默认路径是agent/logs；

* 访问 http://localhost:8084/nacos 服务注册中心，查看相关服务是否已注册

### 五、 测试
* 启动 nacos-server(服务治理中心)
  Windows: D:/opt/soft/nacos/bin/startup.cmd -m standalone
  Linux: sh /opt/soft/nacos/bin/startup.sh -m standalone
* 启动 Skywalking服务
  Windows: D:/opt/soft/skywalking/bin/startup.bat
  Linux: sh /opt/soft/skywalking/bin/startup.sh
* 启动 springcloud-hello-service(hello-service服务,服务提供者)
* 访问 http://localhost:8084/nacos Nacos服务治理中心，查看相关服务是否已注册
* 启动 Application (springcloud-skywalking-consumer)
  java -javaagent:D:\skywalking-agent\skywalking-agent.jar
  -Dskywalking.agent.service_name=skywalking-consumer
  -Dskywalking.collector.backend_service=127.0.0.1:11800
  -jar target\springcloud-skywalking-consumer-0.0.1-SNAPSHOT.jar

* 访问 服务接口 http://localhost:8090/hello-consumer 正确返回“Hello World”内容即为成功
* 访问 Skywalking服务控制台http://localhost:8080/ 查看请求跟踪日志和图形化展示 
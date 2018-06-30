## 服务治理组件-分布式配置中心 Spring Cloud Config Server 服务端 示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.0.BUILD-SNAPSHOT
* SpringCloud:Finchley.BUILD-SNAPSHOT

### 三、配置实现

#### 3.1 pom.xml配置支持Config
```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
```

#### 3.2 启动类增加注解支持
```java
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class SpringcloudConfigServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudConfigServerApplication.class, args);
	}
}
```

#### 3.3  创建测试用的配置信息数据表
```shell
--创建数据库
CREATE DATABASE IF NOT EXISTS jeedev DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

CREATE TABLE `properties` (
  `id` int(11) NOT NULL,
  `key` varchar(50) NOT NULL,
  `value` varchar(500) NOT NULL,
  `application` varchar(50) NOT NULL,
  `profile` varchar(50) NOT NULL,
  `label` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO properties VALUES(1, 'jeedev.message', 'test-dev-master', 'config-client', 'dev', 'master');
INSERT INTO properties VALUES(2, 'jeedev.message', 'test-prod-master', 'config-client', 'prod', 'master');
INSERT INTO properties VALUES(3, 'jeedev.message', 'test-prod-develop', 'config-client', 'prod', 'develop');
INSERT INTO properties VALUES(4, 'jeedev.message', 'hello-prod-master', 'hello-service', 'prod', 'master');
INSERT INTO properties VALUES(5, 'jeedev.message', 'hello-prod-develop', 'hello-service', 'prod', 'develop');
```

#### 3.4  配置 application.properties
```properties
spring.application.name=config-server
server.port=7001

eureka.client.service-url.defaultZone=http://localhost:1111/eureka/

spring.profiles.active=jdbc

spring.cloud.config.server.jdbc.sql=SELECT `KEY`, `VALUE` from PROPERTIES where APPLICATION=? and PROFILE=? and LABEL=?

spring.datasource.url=jdbc:mysql://localhost:3306/jeedev?useUnicode=true&characterEncoding=utf8&useSSL=false
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```



###  四、测试准备
* 启动 springcloud-eureka-server(服务注册中心)
* 访问 http://localhost:1111/ 服务注册中心

### 五、测试
* 启动 Application (springcloud-config-server)

* 访问 http://localhost:1111/ 服务注册中心，查看config-server服务是否已注册

* 访问如下地址就可以查看相应的配置信息文件内容
  http://localhost:7001/config-client/prod  
  http://localhost:7001/config-client/prod/master  

  

  访问 http://localhost:7001/config-client/dev/master 返回配置信息文件示例：
```json
{
    "name": "config-client", 
    "profiles": [
        "dev"
    ], 
    "label": "master", 
    "version": null, 
    "state": null, 
    "propertySources": [
        {
            "name": "config-client-dev", 
            "source": {
                "jeedev.message": "test-dev-master"
            }
        }
    ]
}

```
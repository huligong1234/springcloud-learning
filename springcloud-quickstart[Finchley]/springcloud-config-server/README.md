## 服务治理组件-分布式配置中心 Spring Cloud Config Server 服务端 示例学习

### 一、概述

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.0.BUILD-SNAPSHO
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

#### 3.3  创建测试用的配置信息文件
```shell
在src/main/resources目录下创建config-repo目录，用于存放配置信息文件

目录结构如下：
src
│  ├─main
│  │  └─resources
│  │      │  application.properties
│  │      │
│  │      └─config-repo
│  │              config-client-dev.properties
│  │              config-client-prod.properties
│  │              config-client-test.properties
│  │              config-client.properties

config-client-dev.properties文件中内容:
from=local-dev-1.0

config-client-prod.properties文件中内容:
from=local-prod-1.0

config-client-test.properties文件中内容:
from=local-test-1.0

config-client.properties文件中内容:
from=local-default-1.0

其中from为任意创建的属性key(label)
```

#### 3.4  配置 application.properties
```properties
spring.application.name=config-server
server.port=7001

eureka.client.service-url.defaultZone=http://localhost:1111/eureka/

spring.profiles.active=native
spring.cloud.config.server.native.searchLocations=classpath:/config-repo
```
spring.profiles.active=native表示采用本地文件系统(Config支持Git,SVN,JDBC,本地文件系统等方式存储配置信息文件) 
spring.cloud.config.server.native.searchLocations表示采用native时配置文件的位置，如果不配置，则默认在src/main/resources目录下搜索




###  四、测试准备
* 启动 springcloud-eureka-server(服务注册中心)
* 访问 http://localhost:1111/ 服务注册中心

### 五、测试
* 启动 Application (springcloud-config-server)

* 访问 http://localhost:1111/ 服务注册中心，查看config-server服务是否已注册

* 访问如下地址就可以查看相应的配置信息文件内容
  http://localhost:7001/config-client/prod  
  http://localhost:7001/config-client/prod/from  

  

  访问 http://localhost:7001/config-client/dev/from 返回配置信息文件示例：
```json
{
    "name": "config-client", 
    "profiles": [
        "dev"
    ], 
    "label": "from", 
    "version": null, 
    "state": null, 
    "propertySources": [
        {
            "name": "classpath:/config-repo/config-client-dev.properties", 
            "source": {
                "from": "local-dev-1.0"
            }
        }, 
        {
            "name": "classpath:/config-repo/config-client.properties", 
            "source": {
                "from": "local-default-1.0"
            }
        }
    ]
}

```
可以直接修改相应的properties文件中from属性值或新增属性和值，然后再访问查看相应的变化
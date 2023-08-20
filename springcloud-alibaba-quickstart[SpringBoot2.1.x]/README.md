## Spring Cloud QuickStart[Greenwich] 

Spring Cloud Alibaba学习笔记  

Greenwich builds and works with Spring Boot 2.1.x

### 一、Spring官方文档中心
* [https://spring.io/docs](https://spring.io/docs)


### 二、Spring Cloud 学习(示例)建议顺序
* springcloud-hello-service 服务提供者

* springcloud-ribbon-consumer 服务消费--客户端负载均衡

* springcloud-sentinel-consumer 服务器容错限流保护

* springcloud-feign-consumer 服务消费--声明式服务调用

* springcloud-gateway 基于spring-cloud-gateway的 API网关路由服务

* springcloud-config-client Nacos分布式配置中心-客户端

* springcloud-skywalking-consumer 服务链路追踪(基于skywalking)

  


* springcloud-stream-rabbitmq  分布式消息驱动(基于SpringCloud Stream + RabbitMQ)

* springcloud-stream-rocketmq  分布式消息驱动(基于SpringCloud Stream + RocketMQ)

  


* springcloud-seata-account  分布式事务(基于Seata)，账户示例
* springcloud-seata-storage  分布式事务(基于Seata)，库存示例
* springcloud-seata-order  分布式事务(基于Seata)，订单示例（入口）



### 三、Nacos实际使用补充说明

#### 3.1.namespace和group

namespace
区分环境：开发环境、测试环境、预发布环境、⽣产环境。

group
区分不同应⽤：同⼀个环境内，不同应⽤的配置，通过group来区分。



#### 3.2.shared-configs(共享配置)和extension-config(扩展配置)

**共享配置和扩展配置共同点**

* 两类配置都⽀持三个属性：data-id、group(默认为字符串DEFAULT_GROUP)、refresh(默认为true)

-refresh：是动态刷新，在Nacos修改配置后，服务可以动态感知而无需重启项目。

* 允许指定⼀个或多个共享配置 或扩展配置。

* 关于修改配置文件级别：建议将配置文件改成由application改成bootstrap。application.yml作用域在于当前应用有效，bootstrap.yml系统级别的配置有效（一般采用远程配置的时候才会用到）。故可将项目中原来的application.yml、application-dev.yml对应改成bootstrap.yml、bootstrap-dev.yml 。（根据实际需求定）



配置示例：

```yaml
spring:
  application:
    name: nacos-config-demo
  main:
    allow-bean-definition-overriding: true
  cloud:
    nacos:
      username: ${nacos.username}
      password: ${nacos.password}
      config:
        server-addr: ${nacos.server-addr}
        namespace: ${nacos.namespace}
        # 用于共享的配置文件
        shared-configs:
          - data-id: common-mysql.yaml
            group: SPRING_CLOUD_EXAMPLE_GROUP

          - data-id: common-redis.yaml
            group: SPRING_CLOUD_EXAMPLE_GROUP

          - data-id: common-base.yaml
            group: SPRING_CLOUD_EXAMPLE_GROUP

        # 常规配置文件
        # 优先级大于 shared-configs，在 shared-configs 之后加载
        extension-configs:
          - data-id: nacos-config-advanced.yaml
            group: SPRING_CLOUD_EXAMPLE_GROUP
            refresh: true

          - data-id: nacos-config-base.yaml
            group: SPRING_CLOUD_EXAMPLE_GROUP
            refresh: true
```

参数解析

    data-id : Data Id
    group：自定义 Data Id 所在的组，不明确配置的话，默认是 DEFAULT_GROUP。
    refresh: 控制该 Data Id 在配置变更时，是否支持应用中可动态刷新， 感知到最新的配置值。默认是不支持的。

注意

Data ID后面是加.yaml后缀的，且不需要指定file-extension。



**两者的选择**

**主配置是应⽤专有的配置**

主配置应当在dataId上要区分，同时最好还要有group的区分，因为group区分应⽤（虽然dataId上区分了，不⽤设置group也能按应⽤单独加载）。

**使用公共配置**

如果各应⽤之间共享⼀个配置，则使用 shared-configs。

**使⽤ extension-config细节**

如果要在特定范围内（⽐如某个应⽤上）覆盖某个共享dataId上的特定属性，则使⽤ extension-config。

⽐如，其他应⽤的数据库url，都是⼀个固定的url，使⽤shared-configs.dataId = mysql的共享配置。但其中有⼀个应⽤oa-demo是特例，需要为该应⽤配置扩展属性来覆盖。示例如下：

```yaml
spring:
 application:
   name: 你的应用名称
 cloud: 
   nacos:
     config:
       server-addr: 你的nacos地址
       namespace: myproj-test
       group: xxx-demo
       ......
       shared-configs[3]:
         data-id: mysql.yaml
         refresh: true
       ......
       extension-configs[3]:
         data-id: mysql.yaml
         group: oa-demo
         refresh: true
```


**配置文件优先级解释**

* 1、 上述两类配置都是数组，对同种配置，数组元素对应的下标越⼤，优先级越⾼。也就是排在后⾯的相同配置，将覆盖排在前⾯的同名配置。
    同为扩展配置，存在如下优先级关系：extension-configs[3] > extension-configs[2] > extension-configs[1] > extension-configs[0]。

    同为共享配置，存在如下优先级关系：shared-configs[3] > shared-configs[2] > shared-configs[1] > shared-configs[0]。

* 2、不同种类配置之间，优先级按顺序如下

主配置 > 扩展配置(extension-configs) > 共享配置(shared-configs)


### 参考资料
* Spring Cloud Alibaba 从入门到实战.pdf
* Spring Cloud Alibaba笔记.pdf
* Nacos架构&原理-阿里.pdf
* Nacos官网：https://nacos.io/zh-cn/index.html
* Seata官网：https://seata.io/zh-cn/index.html
* Skywalking官网：https://skywalking.apache.org/
* RocketMQ官网：https://rocketmq.apache.org/zh/
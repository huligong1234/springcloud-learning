## Spring Cloud QuickStart[Finchley] 

Spring Cloud 学习笔记  

Finchley builds and works with Spring Boot 2.0.x, and is not expected to work with Spring Boot 1.5.x.

### Spring官方文档中心
* [https://spring.io/docs](https://spring.io/docs)


#### Spring Cloud 学习(示例)建议顺序
* springcloud-eureka-server  服务注册中心

* springcloud-eureka-client 服务提供者

* springcloud-ribbon-consumer 服务消费--客户端负载均衡

* springcloud-ribbon-consumer-hystrix 服务器容错保护

* springcloud-feign-consumer 服务消费--声明式服务调用

* springcloud-zuul-gateway 基于Zuul的API网关路由服务

* springcloud-gateway 基于spring-cloud-gateway的 API网关路由服务

  

* springcloud-config-server 分布式配置中心-服务端(使用本地文件存储配置)

* springcloud-config-client 分布式配置中心-客户端

* springcloud-config-server-jdbc  分布式配置中心-服务端(使用JDBC存储配置)

  

* springcloud-consul-client 服务提供者(基于Consul服务注册中心)

* springcloud-feign-consumer-consul  服务消费 Feign方式 (基于Consul服务注册中心)

  

  


### 参考资料
* [《Spring Cloud微服务实战》翟永超 著](http://blog.didispace.com/)
## 使用 Spring Cloud Stream (RabbitMQ) 构建消息驱动微服务 示例学习

### 一、概述
* 官网 https://spring.io/projects/spring-cloud-stream
* https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
* Spring Cloud Stream参考指南 https://springcloud.cc/spring-cloud-stream-zhcn.html
* Spring Cloud Stream中文指导手册 https://blog.csdn.net/qq_32734365/article/details/81413218

```
消息的发布（Publish）和订阅（Subscribe）是事件驱动的经典模式。
Spring Cloud Stream 的数据交互也是基于这个思想。
生产者把消息通过某个 topic 广播出去（Spring Cloud Stream 中的 destinations）。
其他的微服务，通过订阅特定 topic 来获取广播出来的消息来触发业务的进行。
```

### 二、依赖版本信息
* JDK1.8
* SpringBoot:2.1.0.BUILD-SNAPSHOT
* SpringCloud:Finchley.BUILD-SNAPSHOT
* spring-cloud-starter-stream-rabbit
* RabbitMQ 3.7.8

### 三、配置实现
#### 3.1 pom.xml配置支持Stream + RabbitMQ
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
	<exclusions>  
        <exclusion>   
            <groupId>org.springframework.boot</groupId>  
            <artifactId>spring-boot-starter-logging</artifactId>  
        </exclusion>  
    </exclusions>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>  
     <artifactId>spring-boot-starter-log4j</artifactId>
     <version>1.3.8.RELEASE</version>
</dependency> 
```

#### 3.2  启动类

```java
@SpringBootApplication
public class SpringcloudStreamApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudStreamApplication.class, args);
	}
}

```

#### 3.3  配置 application.yml
```yaml
server:
  port: 8080
spring:
  application:
    name: spring-cloud-stream
  cloud:
    stream:
      defaultBinder: defaultRabbit
      bindings: 
        inputContent:
          destination: myExchange01     # 对应 MQ Exchange
          group: myQueue01              # 对应 MQ Queue
        outputContent:
          destination: myExchange01
      binders:
        defaultRabbit:
          type: rabbit
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
                virtual-host: test
```

#### 3.4 定义绑定器 （关联Exchange和Queue,具体上述在yaml中定义）

通过定义绑定器作为中间层，实现了应用程序与消息中间件细节之间的隔离。通过向应用程序暴露统一的Channel通过，是的应用程序不需要再考虑各种不同的消息中间件的实现。当需要升级消息中间件，或者是更换其他消息中间件产品时，我们需要做的就是更换对应的Binder绑定器而不需要修改任何应用逻辑 。

```java
package org.jeedevframework.springcloud.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MyMessageProcessor {
	String INPUT_CONTENT = "inputContent";
    String OUTPUT_CONTENT = "outputContent";

    @Input(INPUT_CONTENT)
    SubscribableChannel inputContent();

    @Output(OUTPUT_CONTENT)
    MessageChannel outputContent();
}

```

#### 3.5 消息发布者（生产者）
```java
package org.jeedevframework.springcloud.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyMessageProducer {

	@Autowired
    @Qualifier("outputContent")
    MessageChannel outputContent;
	
	public void send(String body) {
		outputContent.send(MessageBuilder.withPayload(body).build());
	}
}
```

#### 3.6 消息接收者
```java
package org.jeedevframework.springcloud.stream;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableBinding({Processor.class, MyMessageProcessor.class})
public class MyMessageConsumer {

	Logger logger = LoggerFactory.getLogger(MyMessageConsumer.class);
	
	// 监听 binding 为 MyMessageProcessor.INPUT_CONTENT 的消息
    @StreamListener(MyMessageProcessor.INPUT_CONTENT)
    public void receive(String body) {
    	logger.info("==============MyMessageProcessor.receive: "+ body);
    }

}

```

#### 3.7 还可以定义消息中间加工处理(非必须)
```java
package org.jeedevframework.springcloud.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.annotation.Transformer;

@EnableBinding(MyMessageProcessor.class)
public class MyTransformProcessor {
	
  Logger logger = LoggerFactory.getLogger(MyTransformProcessor.class);
	
  @Transformer(inputChannel = MyMessageProcessor.INPUT_CONTENT, outputChannel = MyMessageProcessor.OUTPUT_CONTENT)
  public Object transform(String message) {
	  logger.info("############TransformProcessor.transform: "+message);
    return message.toUpperCase();
  }
  
}
``` 

###  四、测试准备
* 启动 rabbitmq (安装略)

### 五、测试
* 启动 Application (springcloud-stream)
* 访问 服务接口 http://localhost:8080/send?message=hello 发生消息
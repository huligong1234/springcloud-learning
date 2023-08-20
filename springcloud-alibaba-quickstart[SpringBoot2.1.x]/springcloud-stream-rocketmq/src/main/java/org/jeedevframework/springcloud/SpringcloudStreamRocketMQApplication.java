package org.jeedevframework.springcloud;

import java.util.Date;

import org.jeedevframework.springcloud.stream.MyMessageProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

@SpringBootApplication
public class SpringcloudStreamRocketMQApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudStreamRocketMQApplication.class, args);
	}
	
	
	 // 定时轮询发送消息到 binding 为 MyMessageProcessor.OUTPUT_CONTENT
	@Bean
    @InboundChannelAdapter(value = MyMessageProcessor.OUTPUT_CONTENT, poller = @Poller(fixedDelay = "3000", maxMessagesPerPoll = "1"))
    public MessageSource<String> timerMessageSource() {
        return () -> MessageBuilder.withPayload("abcd短消息-" + new Date()).build();
    }

}

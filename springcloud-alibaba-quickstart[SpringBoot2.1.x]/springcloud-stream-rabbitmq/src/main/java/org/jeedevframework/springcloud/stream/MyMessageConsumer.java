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

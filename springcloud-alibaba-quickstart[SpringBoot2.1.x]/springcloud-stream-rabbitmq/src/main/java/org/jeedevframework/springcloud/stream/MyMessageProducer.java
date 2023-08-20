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

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
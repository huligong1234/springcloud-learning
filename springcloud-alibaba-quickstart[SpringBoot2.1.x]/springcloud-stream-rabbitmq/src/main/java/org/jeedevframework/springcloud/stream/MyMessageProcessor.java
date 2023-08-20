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

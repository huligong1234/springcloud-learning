package org.jeedevframework.springcloud.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.jeedevframework.springcloud.stream.MyMessageProducer;

@RestController
public class SendMessageController {
	
    @Autowired
    MyMessageProducer myMessageProducer;
	
	@GetMapping("/send")
	public String index(@RequestParam(value="message", required=true) String message){
		myMessageProducer.send(message);
		return "ok";
	}
}

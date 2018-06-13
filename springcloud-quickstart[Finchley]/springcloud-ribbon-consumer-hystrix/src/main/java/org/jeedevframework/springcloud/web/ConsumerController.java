package org.jeedevframework.springcloud.web;

import org.jeedevframework.springcloud.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

	@Autowired
	private HelloService helloService;
	
	@GetMapping(value="/ribbon-hystrix-consumer")
	public String helloConsumer(){
		return helloService.helloService();
	}
}

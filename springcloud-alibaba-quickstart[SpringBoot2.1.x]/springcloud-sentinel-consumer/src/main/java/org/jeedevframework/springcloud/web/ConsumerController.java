package org.jeedevframework.springcloud.web;

import org.jeedevframework.springcloud.service.HelloSentinelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

	@Autowired
	private HelloSentinelService helloSentinelService;
	
	@GetMapping(value="/sentinel-consumer")
	public String helloConsumer(){
		return helloSentinelService.hello();
	}
}

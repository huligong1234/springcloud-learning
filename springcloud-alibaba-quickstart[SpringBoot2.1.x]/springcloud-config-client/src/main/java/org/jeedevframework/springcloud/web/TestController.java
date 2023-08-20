package org.jeedevframework.springcloud.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {

	@Autowired
	private ConfigurableApplicationContext configurableApplicationContext;

	@Value("${from:}")
	private String from;
	
	@RequestMapping("/from")
	public String from(){
		return this.from;
	}

	@RequestMapping("/from2")
	public String from2(){
		return configurableApplicationContext.getEnvironment().getProperty("from");
	}
}

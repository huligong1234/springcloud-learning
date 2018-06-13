package org.jeedevframework.springcloud.web;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	private final Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private DiscoveryClient client;
	
	@GetMapping("/hello")
	public String index(){
		List<String> serviceNameList = client.getServices();
		logger.info("serviceNameList:"+serviceNameList);
		for(String serviceId : serviceNameList){
			List<ServiceInstance> serviceClientList = client.getInstances(serviceId);
			if(null != serviceClientList && !serviceClientList.isEmpty()){
				for(ServiceInstance serviceClient : serviceClientList){
					logger.info("serviceClient:"+serviceClient.getUri()+","+serviceClient.getHost()+":"+serviceClient.getPort()+","+serviceClient.getServiceId());
				}
			}
		}
		return "Hello World";
	}
}

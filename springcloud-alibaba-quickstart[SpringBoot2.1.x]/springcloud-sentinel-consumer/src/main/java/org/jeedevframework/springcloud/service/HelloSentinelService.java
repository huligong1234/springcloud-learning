package org.jeedevframework.springcloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloSentinelService {

	@Autowired
	private RestTemplate restTemplate;

	@SentinelResource(value = "HelloSentinelService#hello", blockHandler = "helloBlockHandler", fallback = "helloFallback")
	public String hello(){
		return restTemplate.getForEntity("http://hello-service/hello", String.class).getBody();
	}

	// blockHandler 函数，原方法调用被限流/降级/系统保护的时候调用
	public String helloBlockHandler(String id, BlockException ex) {
		ex.printStackTrace();
		return "Block-hello";
	}

	public String helloFallback(Throwable t) {
		t.printStackTrace();
		if (BlockException.isBlockException(t)) {
			return "Blocked by Sentinel: " + t.getClass().getSimpleName();
		}
		return "Error, failed: " + t.getClass().getCanonicalName();
	}
}

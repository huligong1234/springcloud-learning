package org.jeedevframework.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringCloudApplication
public class SpringcloudZuulGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudZuulGatewayApplication.class, args);
	}
}

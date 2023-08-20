package org.jeedevframework.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudNacosClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudNacosClientApplication.class, args);
	}
}

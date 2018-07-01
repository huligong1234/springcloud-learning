package org.jeedevframework.springcloud.config;

import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

	 @Bean
	    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
	        StripPrefixGatewayFilterFactory.Config config = new StripPrefixGatewayFilterFactory.Config();
	        config.setParts(1);
	        return builder.routes()
	                .route("hello-service", r -> r.path("/api-c/**").filters(f -> f.stripPrefix(1)).uri("lb://hello-service"))
	                .route("feign-consumer", r -> r.path("/api-d/**").filters(f -> f.stripPrefix(1)).uri("lb://feign-consumer"))
	                .build();
	    }
}

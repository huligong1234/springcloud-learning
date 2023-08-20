package org.jeedevframework.springcloud;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import org.jeedevframework.springcloud.utils.ExceptionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringcloudSentinelConsumerApplication {

	@Bean
	@LoadBalanced
	@SentinelRestTemplate(blockHandler = "handleException", blockHandlerClass = ExceptionUtil.class)
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudSentinelConsumerApplication.class, args);
	}

	//硬编码的方式定义流量控制规则
	private static void initFlowQpsRule() {
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule1 = new FlowRule();
		rule1.setResource("HelloSentinelService#hello");
		// Set max qps to 20
		rule1.setCount(20);
		rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule1.setLimitApp("default");
		rules.add(rule1);
		FlowRuleManager.loadRules(rules);
	}

	//硬编码的方式定义熔断降级规则
	private static void initDegradeRule() {
		List<DegradeRule> rules = new ArrayList<>();
		DegradeRule rule = new DegradeRule("HelloSentinelService#hello");
		rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_RATIO);
		rule.setCount(0.7); // Threshold is 70% error ratio
		rule.setMinRequestAmount(100)
				//.setStatIntervalMs(30000) // 30s
				.setTimeWindow(10);
		rules.add(rule);
		DegradeRuleManager.loadRules(rules);
	}

	//硬编码的方式定义系统保护规则
	private void initSystemProtectionRule() {
		List<SystemRule> rules = new ArrayList<>();
		SystemRule rule = new SystemRule();
		rule.setHighestSystemLoad(10);
		rules.add(rule);
		SystemRuleManager.loadRules(rules);
	}

	//硬编码的方式定义访问控制规则(黑白名单规则)
	//http://sentinelguard.io/zh-cn/docs/origin-authority-control.html
	private void initOriginAuthorityControl(){
		AuthorityRule rule = new AuthorityRule();
		rule.setResource("HelloSentinelService#hello");
		rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
		rule.setLimitApp("appA,appB");
		AuthorityRuleManager.loadRules(Collections.singletonList(rule));
	}
}

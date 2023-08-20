package org.jeedevframework.springcloud.service;

import org.jeedevframework.springcloud.common.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:调用账户接口的feign
 */
@FeignClient(value = "seata-account")
@RequestMapping("/account")
public interface AccountFeignService {
    @RequestMapping("/decrease")
    R decrease(@RequestParam("userId") Long userId, @RequestParam("money") Integer money);
}


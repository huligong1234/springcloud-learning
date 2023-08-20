package org.jeedevframework.springcloud.service;

import org.jeedevframework.springcloud.common.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:调用库存接口的feign

 */
@FeignClient(value = "seata-storage")
@RequestMapping("/storage")
public interface StorageFeignService {
    @RequestMapping("/decrease")
    R decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}


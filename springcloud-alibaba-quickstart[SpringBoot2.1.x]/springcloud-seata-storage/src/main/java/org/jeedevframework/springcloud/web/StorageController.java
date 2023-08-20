package org.jeedevframework.springcloud.web;

import io.seata.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Storage;
import org.jeedevframework.springcloud.service.StorageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description：库存接口
 */
@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @RequestMapping("/listAll")
    public R listAll() {
        List<Storage> storageList = storageService.list();
        return R.ok(2000, StringUtils.EMPTY,storageList);
    }

    @RequestMapping("/decrease")
    public R decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count) {
        return storageService.decrease(productId,count);
    }
}


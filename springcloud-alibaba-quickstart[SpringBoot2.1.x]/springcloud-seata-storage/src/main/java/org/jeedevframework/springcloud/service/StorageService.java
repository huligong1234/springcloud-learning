package org.jeedevframework.springcloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Storage;

/**
 * Description:库存接口

 */
public interface StorageService extends IService<Storage> {
    R decrease(Long productId, Integer count);
}


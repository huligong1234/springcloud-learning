package org.jeedevframework.springcloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Storage;
import org.jeedevframework.springcloud.mapper.StorageMapper;
import org.jeedevframework.springcloud.service.StorageService;
import org.springframework.stereotype.Service;

/**
 * Description:库存接口实现

 */
@Service
public class StorageServiceImpl extends ServiceImpl<StorageMapper, Storage> implements StorageService {
    @Override
    public R decrease(Long productId, Integer count) {
        QueryWrapper<Storage> queryWrapper = new QueryWrapper();
        queryWrapper.eq("product_id",productId);
        Storage storage = getOne(queryWrapper);
        if(null == storage) {
            return R.error(2001,"库存不存在此产品!");
        }
        if(storage.getCount()>=count) {
            storage.setCount(storage.getCount()-count);
            updateById(storage);
        }else {
            return R.error(2002,"库存不足!");
        }
        return R.ok(2000,"库存更新成功!");
    }
}


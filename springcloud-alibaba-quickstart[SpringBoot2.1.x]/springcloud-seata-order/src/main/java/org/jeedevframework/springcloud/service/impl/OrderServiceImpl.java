package org.jeedevframework.springcloud.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Order;
import org.jeedevframework.springcloud.mapper.OrderMapper;
import org.jeedevframework.springcloud.service.OrderService;
import org.jeedevframework.springcloud.service.AccountFeignService;
import org.jeedevframework.springcloud.service.StorageFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description：创建订单service
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private AccountFeignService accountFeignService;

    @Autowired
    private StorageFeignService storageFeignService;

    @Override
    @GlobalTransactional(name = "seata-create-order",rollbackFor = Exception.class)
    public R createOrder() throws Exception {
        Order order = Order.builder()
                .count(10)
                .money(100)
                .productId(1L)
                .status(0)
                .userId(1L)
                .build();
        // 创建订单
        save(order);
        // 扣除库存
        R storageRet = storageFeignService.decrease(order.getProductId(), order.getCount());
        if (2000 != storageRet.getCode()) {
            //return R.error(storageRet.getMessage());
            throw new Exception(storageRet.getMessage());
        }
        // 扣余额
        R accountRet = accountFeignService.decrease(order.getUserId(), order.getMoney());
        if (2000 != accountRet.getCode()) {
            //return R.error(accountRet.getMessage());
            throw new Exception(accountRet.getMessage());
        }

        //更新订单状态
        order.setStatus(1);
        updateById(order);
        return R.ok();
    }
}

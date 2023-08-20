package org.jeedevframework.springcloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Order;

public interface OrderService extends IService<Order> {
   R createOrder() throws Exception;
}

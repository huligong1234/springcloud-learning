package org.jeedevframework.springcloud.web;

import io.seata.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.jeedevframework.springcloud.common.R;
import org.jeedevframework.springcloud.entity.Order;
import org.jeedevframework.springcloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description：创建订单控接口
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/listAll")
    public R listAll() {
        List<Order> orderList = orderService.list();
        return R.ok(2000, StringUtils.EMPTY,orderList);
    }

    @RequestMapping("/createOrder")
    public R createOrder() {
        try {
            orderService.createOrder();
        }catch (Exception e) {
            return R.ok(2100,"订单创建失败,"+e.getMessage());
        }
        return R.ok(2000,"订单创建成功");
    }
}


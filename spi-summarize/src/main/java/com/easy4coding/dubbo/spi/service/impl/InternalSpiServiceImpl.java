package com.easy4coding.dubbo.spi.service.impl;

import com.easy4coding.dubbo.spi.service.ioc.OrderService;
import com.easy4coding.dubbo.spi.service.SpiService;

/**
 * @author dmz
 * @date Create in 3:22 下午 2021/12/12
 */
public class InternalSpiServiceImpl implements SpiService {

    private OrderService orderService;

    @Override
    public void say(String words) {
        System.out.println("dmz say " + words);
    }

    @Override
    public void pay(Integer money) {
        orderService.pay(money);
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
        System.out.println("setter invoke " + orderService);
    }
}

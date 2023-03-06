package com.easy4coding.dubbo.spi.service.ioc;

/**
 * @author dmz
 * @date Create in 12:17 上午 2021/12/17
 */
public class CashServiceImpl implements OrderService {
    @Override
    public void pay(Integer money) {
        System.out.println("使用现金支付：" + money);
    }
}

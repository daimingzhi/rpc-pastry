package com.easy4coding.dubbo.spi.service.ioc;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @author dmz
 * @date Create in 7:53 下午 2021/12/17
 */
@Adaptive
public class OrderAdaptiveService implements OrderService {
    @Override
    public void pay(Integer money) {
        if (money > 100) {
            ExtensionLoader.getExtensionLoader(OrderService.class).getExtension("card").pay(money);
        } else {
            ExtensionLoader.getExtensionLoader(OrderService.class).getExtension("cash").pay(money);
        }
    }
}

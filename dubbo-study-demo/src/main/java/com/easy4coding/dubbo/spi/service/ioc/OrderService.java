package com.easy4coding.dubbo.spi.service.ioc;

import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * @author dmz
 * @date Create in 12:18 上午 2021/12/17
 */
@SPI
public interface OrderService {
    @Adaptive
    void pay(Integer money);
}

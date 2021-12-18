package com.easy4coding.dubbo.spi.service;

import org.apache.dubbo.common.extension.SPI;

/**
 * @author dmz
 * @date Create in 3:22 下午 2021/12/12
 */
@SPI("internal")
public interface SpiService {
    void say(String words);

    void pay(Integer money);
}

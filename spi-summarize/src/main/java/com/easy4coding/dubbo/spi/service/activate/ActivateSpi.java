package com.easy4coding.dubbo.spi.service.activate;

import org.apache.dubbo.common.extension.SPI;

/**
 * @author dmz
 * @date Create in 11:18 下午 2021/12/20
 */
@SPI
public interface ActivateSpi {
    void activateMethod();
}

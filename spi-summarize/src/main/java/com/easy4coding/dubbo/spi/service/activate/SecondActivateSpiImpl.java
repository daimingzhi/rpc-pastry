package com.easy4coding.dubbo.spi.service.activate;

import org.apache.dubbo.common.extension.Activate;

/**
 * @author dmz
 * @date Create in 11:18 下午 2021/12/20
 */
@Activate("second")
public class SecondActivateSpiImpl implements ActivateSpi {
    @Override
    public void activateMethod() {
        System.out.println("activate second");
    }
}

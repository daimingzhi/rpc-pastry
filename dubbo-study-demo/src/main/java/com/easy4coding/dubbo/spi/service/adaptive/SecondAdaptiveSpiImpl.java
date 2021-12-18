package com.easy4coding.dubbo.spi.service.adaptive;

import org.apache.dubbo.common.URL;

/**
 * @author dmz
 * @date Create in 3:46 下午 2021/12/18
 */
public class SecondAdaptiveSpiImpl implements AdaptiveSpi {
    @Override
    public void adaptiveMethod(URL url) {
        System.out.println("second");
    }

    @Override
    public void adaptiveMethod(URLHolder url) {
        System.out.println("second");
    }
}

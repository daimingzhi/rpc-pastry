package com.easy4coding.dubbo.spi.service.activate;

/**
 * @author dmz
 * @date Create in 11:18 下午 2021/12/20
 */
public class FirstActivateSpiImpl implements ActivateSpi {
    @Override
    public void activateMethod() {
        System.out.println("activate first");
    }
}

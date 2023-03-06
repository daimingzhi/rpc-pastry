package com.easy4coding.dubbo.spi.service.aop;

import com.easy4coding.dubbo.spi.service.SpiService;

/**
 * @author dmz
 * @date Create in 1:43 上午 2021/12/16
 */
public class SpiServiceWrapper implements SpiService {

    private final SpiService spiService;

    public SpiServiceWrapper(SpiService spiService) {
        this.spiService = spiService;
    }

    @Override
    public void say(String words) {
        System.out.println("do something before");
        try {
            spiService.say(words);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("do something when occur error");
        }
        System.out.println("do something after");
    }

    @Override
    public void pay(Integer money) {
        spiService.pay(money);
    }
}

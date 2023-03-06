package com.easy4coding.dubbo.spi.service.adaptive;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;

/**
 * @author dmz
 * @date Create in 3:46 下午 2021/12/18
 */
public class FirstAdaptiveSpiImpl implements AdaptiveSpi {
    @Override
    public void adaptiveMethod1(URL url) {
        System.out.println("first");
    }

    @Override
    public void adaptiveMethod2(URLHolder url) {
        System.out.println("first");
    }

    @Override
    public void adaptiveMethod3(URLHolder url, Invocation invocation) {

    }

    @Override
    public void normalMethod() {

    }
}

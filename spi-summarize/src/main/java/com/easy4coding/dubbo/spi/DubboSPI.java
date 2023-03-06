package com.easy4coding.dubbo.spi;

import com.easy4coding.dubbo.spi.service.SpiService;
import com.easy4coding.dubbo.spi.service.activate.ActivateSpi;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.List;

/**
 * @author dmz
 * @date Create in 12:04 上午 2021/12/13
 */
public class DubboSPI {
    public static void main(String[] args) {

        final ExtensionLoader<SpiService> extensionLoader = ExtensionLoader.getExtensionLoader(SpiService.class);
        final SpiService internal = extensionLoader.getExtension("internal");
        internal.say("aaa");

//        final ExtensionLoader<AdaptiveSpi> adaptiveSpiExtensionLoader = ExtensionLoader.getExtensionLoader(AdaptiveSpi.class);
//        final AdaptiveSpi adaptiveExtension = adaptiveSpiExtensionLoader.getAdaptiveExtension();
//
//        //        adaptiveExtension.adaptiveMethod(URL.valueOf("dubbo://easy4coding.com?adaptive=second"));
////        adaptiveExtension.adaptiveMethod(URL.valueOf("dubbo://easy4coding.com?adaptive=first"));
//        adaptiveExtension.adaptiveMethod2(new AdaptiveSpi.URLHolder(URL.valueOf("dubbo://easy4coding.com?adaptive=second")));
//        adaptiveExtension.adaptiveMethod2(new AdaptiveSpi.URLHolder(URL.valueOf("dubbo://easy4coding.com?adaptive=first")));
//
//        final ExtensionLoader<OrderService> orderServiceExtensionLoader = ExtensionLoader.getExtensionLoader(OrderService.class);
//        final OrderService orderService = orderServiceExtensionLoader.getAdaptiveExtension();

        URL url = new URL("","",0);
        url =  url.addParameter("second","1");
        String[] extensionNames  = new String[]{"-first","default"};
        final List<ActivateSpi> activateExtension = ExtensionLoader.getExtensionLoader(ActivateSpi.class).getActivateExtension(url, extensionNames);
        activateExtension.forEach(ActivateSpi::activateMethod);
    }
}

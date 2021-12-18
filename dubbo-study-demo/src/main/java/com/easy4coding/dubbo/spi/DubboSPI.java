package com.easy4coding.dubbo.spi;

import com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * @author dmz
 * @date Create in 12:04 上午 2021/12/13
 */
public class DubboSPI {
    public static void main(String[] args) {
//        final ExtensionLoader<SpiService> extensionLoader = ExtensionLoader.getExtensionLoader(SpiService.class);
//        final SpiService internal = extensionLoader.getExtension("internal");
//        internal.pay(200);
//        internal.pay(50);
        final AdaptiveSpi adaptiveExtension = ExtensionLoader.getExtensionLoader(AdaptiveSpi.class).getAdaptiveExtension();
//        adaptiveExtension.adaptiveMethod(URL.valueOf("dubbo://easy4coding.com?adaptive=second"));
//        adaptiveExtension.adaptiveMethod(URL.valueOf("dubbo://easy4coding.com?adaptive=first"));
        adaptiveExtension.adaptiveMethod(new AdaptiveSpi.URLHolder(URL.valueOf("dubbo://easy4coding.com?adaptive=second")));
        adaptiveExtension.adaptiveMethod(new AdaptiveSpi.URLHolder(URL.valueOf("dubbo://easy4coding.com?adaptive=first")));
    }
}

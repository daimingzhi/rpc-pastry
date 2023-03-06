package com.easy4coding.dubbo.spi.service.adaptive;

import org.apache.dubbo.common.extension.ExtensionLoader;

public class AdaptiveSpi$Adaptive implements com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi {
    public void adaptiveMethod2(com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi.URLHolder arg0) {
        org.apache.dubbo.common.URL url = arg0.getUrl();
        String extName = url.getParameter("adaptive2");
        com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi extension = (com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi) ExtensionLoader.getExtensionLoader(com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi.class).getExtension(extName);
        extension.adaptiveMethod2(arg0);
    }

    public void adaptiveMethod1(org.apache.dubbo.common.URL arg0) {
        org.apache.dubbo.common.URL url = arg0;
        String extName = url.getParameter("adaptive1");
        com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi extension = (com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi) ExtensionLoader.getExtensionLoader(com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi.class).getExtension(extName);
        extension.adaptiveMethod1(arg0);
    }

    public void adaptiveMethod3(com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi.URLHolder arg0, org.apache.dubbo.rpc.Invocation arg1) {
        org.apache.dubbo.common.URL url = arg0.getUrl();
        String methodName = arg1.getMethodName();
        String extName = url.getMethodParameter(methodName, "adaptive.spi", "null");
        com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi extension = (com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi) ExtensionLoader.getExtensionLoader(com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi.class).getExtension(extName);
        extension.adaptiveMethod3(arg0, arg1);
    }

    public void normalMethod() {
        throw new UnsupportedOperationException("The method public abstract void com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi.normalMethod() of interface com.easy4coding.dubbo.spi.service.adaptive.AdaptiveSpi is not adaptive method!");
    }
}
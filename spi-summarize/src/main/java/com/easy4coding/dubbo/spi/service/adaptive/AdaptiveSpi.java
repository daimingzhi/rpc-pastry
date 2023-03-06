package com.easy4coding.dubbo.spi.service.adaptive;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.Invocation;

/**
 * @author dmz
 * @date Create in 12:54 上午 2021/12/18
 */
@SPI
public interface AdaptiveSpi {

    /**
     * 注意这个方法有两个特征
     * <p>
     * 1.方法上有adaptive注解
     * <p>
     * 2.方法的参数中有一个URL，需要注意的是这是一个org.apache.dubbo.common.URL,不是java.net.URL
     */
    @Adaptive("adaptive1")
    void adaptiveMethod1(URL url);

    @Adaptive("adaptive2")
    void adaptiveMethod2(URLHolder url);

    @Adaptive
    void adaptiveMethod3(URLHolder url, Invocation invocation);

    /**
     * 普通方法，用于观察最终生成的代码
     */
    void normalMethod();

    class URLHolder {

        private final URL url;

        public URLHolder(URL url) {
            this.url = url;
        }

        public URL getUrl() {
            return url;
        }
    }
}

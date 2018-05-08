package io.renren.listener;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import io.renren.config.AlipayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void onApplicationEvent(ContextRefreshedEvent event) {
        AlipayConfig alipayConfig = event.getApplicationContext().getBean(AlipayConfig.class);

        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(),
                alipayConfig.getFormat(),
                alipayConfig.getCharset(),
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getSignType());
        AlipayConfig.setAlipayClient(alipayClient);
        logger.info("初始化支付宝参数成功！");
    }
}
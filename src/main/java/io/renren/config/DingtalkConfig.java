package io.renren.config;

import com.dingtalk.chatbot.DingtalkChatbotClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单群消息推送客户端
 *
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-04-20 18:33
 */
@Configuration
public class DingtalkConfig {

    @Bean
    public DingtalkChatbotClient dingtalkChatbotClient(){
        return new DingtalkChatbotClient();
    }
}

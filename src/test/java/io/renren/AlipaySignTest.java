package io.renren;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.MonitorHeartbeatSynRequest;
import com.alipay.api.response.MonitorHeartbeatSynResponse;
import io.renren.config.AlipayConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ITMX on 2017/12/22.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlipaySignTest {
    @Test
    public void contextLoads() throws AlipayApiException {
        AlipayClient alipayClient = AlipayConfig.getAlipayClient();
        MonitorHeartbeatSynRequest request = new MonitorHeartbeatSynRequest();
        request.setBizContent("{任意值}");
        MonitorHeartbeatSynResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}

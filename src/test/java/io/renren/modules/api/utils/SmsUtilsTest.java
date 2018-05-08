package io.renren.modules.api.utils;

import io.renren.modules.admin.service.DataDictionariesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by ITMX on 2018/3/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUtilsTest {

    @Autowired
    private SmsUtils smsUtils;
    @Autowired
    private DataDictionariesService dataDictionariesService;

    /**
     * 测试发送邀请注册成功初始密码短信
     * @throws Exception
     */
    @Test
    public void inviteRegSuccess() throws Exception {
        //短信发送密码到用户
        String inviteRegSmsHeader = dataDictionariesService.queryByKey("邀请注册成功短信前缀", "1.0.0");
        String inviteRegSmsFooter = dataDictionariesService.queryByKey("邀请注册成功短信后缀", "1.0.0");
        smsUtils.inviteRegSuccess("18929888847",inviteRegSmsHeader,"123456" + inviteRegSmsFooter);
    }

}
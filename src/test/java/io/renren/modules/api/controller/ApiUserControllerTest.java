package io.renren.modules.api.controller;

import io.renren.PostUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by ITMX on 2017/12/7.
 */
public class ApiUserControllerTest {

    /**
     * 密码注册测试
     * @throws Exception
     */
    @Test
    public void register() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone", "15992999163");
        map.add("smsCode", "302032");
        map.add("password", "123456789");
        map.add("inviteCode", "");
        map.add("deviceId", "");

        PostUtils.postData("/api/user/reg",map);

    }

    /**
     * 密码登录测试
     * @throws Exception
     */
    @Test
    public void login() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone", "15992999163");
        map.add("password", "123456789");
        map.add("deviceId", "");

        PostUtils.postData("/api/user/login",map);

    }

    /**
     * 短信登录测试
     * @throws Exception
     */
    @Test
    public void smsLogin() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone", "15992999163");
        map.add("smsCode", "129774");
        map.add("deviceId", "");

        PostUtils.postData("/api/user/smsLogin",map);

    }

    /**
     * 找回密码测试
     * @throws Exception
     */
    @Test
    public void forgetPassword() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone", "15992999163");
        map.add("smsCode", "123456");
        map.add("password", "396173938");

        PostUtils.postData("/api/user/forgetPassword",map);

    }


    /**
     * 修改密码测试
     * @throws Exception
     */
    @Test
    public void changePassword() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("oldPassword", "123456789");
        map.add("password", "123456789");

        PostUtils.postData("/api/user/changePassword",map);
    }

    /**
     * 获取用户信息测试
     * @throws Exception
     */
    @Test
    public void info() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();

        PostUtils.postData("/api/user/info",map);
    }

    /**
     * 绑定手机号测试
     * @throws Exception
     */
    @Test
    public void bindPhone() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();

        map.add("phone","15271519187");
        map.add("smsCode","1234");
        map.add("password","123456789");

        PostUtils.postData("/api/user/bindPhone",map);
    }

    /**
     * 修改手机号测试
     * @throws Exception
     */
    @Test
    public void changePhone() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();

        map.add("phone","15992999163");
        map.add("smsCode","278069");
        map.add("password","123456789");

        PostUtils.postData("/api/user/changePhone",map);
    }

    /**
     * 修改用户信息测试
     * @throws Exception
     */
    @Test
    public void updateInfo() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();

        map.add("key","gender");
        map.add("value","1");

        PostUtils.postData("/api/user/updateInfo",map);
    }

    /**
     * 微信登录测试类
     * @throws Exception
     */
    @Test
    public void wxLogin() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("code", "081B0dq80zKQTH1R8gp80tXjq80B0dqo");

        PostUtils.postData("/api/user/wxLogin",map);
    }

    /**
     * 支付宝登录测试类
     * @throws Exception
     */
    @Test
    public void alipayLogin() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("code", "33fe61757d7d4db998e70404e4e0SX35");

        PostUtils.postData("/api/user/alipayLogin",map);
    }

    /**
     * 微信绑定测试
     * @throws Exception
     */
    @Test
    public void bindWeChat() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("code", "33fe61757d7d4db998e70404e4e0SX35");
        PostUtils.postData("/api/user/bindWeChat",map);
    }

    /**
     * 微信绑定测试
     * @throws Exception
     */
    @Test
    public void verifyPwd() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("oldPassword", "123456");
        PostUtils.postData("/api/user/verifyPwd",map);
    }
}
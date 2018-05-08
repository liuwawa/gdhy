package io.renren.modules.api.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qiniu.util.Json;
import io.renren.PostUtils;
import io.renren.modules.api.entity.UploadType;
import io.renren.modules.api.utils.SmsUtils;
import net.sf.json.JSON;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ITMX on 2017/12/7.
 */
public class ApiUtilsControllerTest {

    /**
     * 短信验证码发送测试
     * @throws Exception
     */
    @Test
    public void sendSmsCode() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone", "15992999163");
        map.add("type", SmsUtils.SMS_TYPE.SMS_LOGIN.toString());

        PostUtils.postData("/api/utils/sendSmsCode",map);
    }

    /**
     * 短信验证码发送测试(空手机号)
     * @throws Exception
     */
    @Test
    public void sendSmsCodeNoPhone() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone", "");
        map.add("type", SmsUtils.SMS_TYPE.MODIFY_OR_FIND_PASS.toString());

        PostUtils.postData("/api/utils/sendSmsCode",map);
    }

    /**
     * 文件上传测试
     * @throws Exception
     */
    @Test
    public void upload() throws Exception {


        String url = PostUtils.BASE_URL + "/api/utils/upload";

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("type", UploadType.AVATAR.toString());

        PostUtils.fillSign(map,PostUtils.SECRET);

        FileSystemResource resource = new FileSystemResource(("C:/Users/Administrator/Desktop/demo3.jpg"));
        FileSystemResource resource1 = new FileSystemResource(("C:/Users/Administrator/Desktop/demo2.jpg"));
        FileSystemResource resource2 = new FileSystemResource(("C:/Users/Administrator/Desktop/500472862.jpg"));

//        s.add(resource);
//        s.add(resource1);
//        s.add(resource2);new FileSystemResource[]{resource,resource1,resource2}
        map.add("file",resource);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("TOKEN","eyJ0eXAiOiJKV1QiLCJ1dWlkIjoiZmU3ODA3ZGE2YjJmNGExY2E2MDJmZDBiZmEzZTEyMmIiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2NyIsImlhdCI6MTUxNTExNjk4MCwiZXhwIjoxNTE1NzIxNzgwfQ.FKyDYKKd4dbNkkR0aw1fIokCVIyi1lzmJgb0Tj0WK8JVH36HcMyM8fFhTEuJkmv1EUdliimWWPPDvJfY4gftDg");

        String result = PostUtils.TEMPLATE.postForObject(url,new HttpEntity<>(map, httpHeaders),String.class,map);

        System.out.println(result);
        assertNotNull(result);
        JsonObject r = new JsonParser().parse(result).getAsJsonObject();
        assertTrue(r.get("msg").getAsString(),r.get("status").getAsBoolean());
    }

    /**
     * 获取字典数据测试
     * @throws Exception
     */
    @Test
    public void dataDictionaries() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("keys", "APP分享链接,APP分享标题,APP分享描述");
        map.add("version", "1.1.0");
        PostUtils.postData("/api/utils/dataDictionaries",map);
    }

    /**
     * 检查更新测试
     * @throws Exception
     */
    @Test
    public void checkUpdate() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("version", "1.0.0");
        map.add("appId", "dzkandian");
        PostUtils.postData("/api/utils/checkUpdate",map);
    }

    /**
     * 验证码校验测试
     * @throws Exception
     */
    @Test
    public void verifySms() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("smsCode", "123456");
        map.add("smsType", "MODIFY_OR_FIND_PASS");
        map.add("version", "1.0.0");
        PostUtils.postData("/api/utils/verifySms",map);
    }

    /**
     * 设备信息上传测试
     * @throws Exception
     */
    @Test
    public void uploadDeviceInfo() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("info", URLEncoder.encode("/a/video.html?type=%E5%BD%B1%E8%A7%86&num=8&start=3&beforeId=641783056aacc2bbb3128b630cd3c6f2","utf-8"));
        map.add("deviceId", "QWRUYFHSKVNSFFFSDKFJASDKFJLASKDF");
        map.add("version", "1.0.0");
        PostUtils.postData("/api/utils/uploadDeviceInfo",map);
    }
}
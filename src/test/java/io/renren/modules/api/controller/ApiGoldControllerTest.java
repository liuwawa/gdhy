package io.renren.modules.api.controller;

import io.renren.PostUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by ITMX on 2018/1/2.
 */
public class ApiGoldControllerTest {

    @Test
    public void mine() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/gold/mine",map);
    }

    @Test
    public void exchangeList() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/gold/exchangeList",map);
    }

    @Test
    public void withdrawals() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("rmb",100);
        map.add("channel","alipay");
        PostUtils.postData("/api/gold/withdrawals",map);
    }
    @Test
    public void withdrawalsList() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("status","");
        map.add("page",1);
        map.add("limit",2);
        PostUtils.postData("/api/gold/withdrawalsList",map);
    }
}
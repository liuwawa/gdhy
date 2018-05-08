package io.renren.modules.api.controller;

import io.renren.PostUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.Assert.*;

/**
 * Created by ITMX on 2018/3/18.
 */
public class ApiInviteControllerTest {

    @Test
    public void getInviteCode() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/invite/getInviteCode",map);
    }

    @Test
    public void sendSmsCode(){
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone","15992999163");
        PostUtils.postData("/api/invite/sendSmsCode",map);
    }

    @Test
    public void register(){
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone","15992999165");
        map.add("smsCode","333858");
        map.add("inviteCode","1000006");
        PostUtils.postData("/api/invite/register",map);
    }

    @Test
    public void pageData(){
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/invite/pageData",map);
    }

    @Test
    public void apprentices(){
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("page",1);
        map.add("limit",10);
        PostUtils.postData("/api/invite/apprentices",map);
    }

    @Test
    public void profitList(){
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("page",1);
        map.add("limit",10);
        PostUtils.postData("/api/invite/profitList",map);
    }

    @Test
    public void share() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/invite/share",map);
    }
}
package io.renren.modules.api.controller;

import io.renren.PostUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

import static org.junit.Assert.*;

/**
 * Created by hc on 2018/3/27.
 */
public class ApiCommentControllerTest {
    @Test
    public void save() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("aid", "1465245");
        map.add("comment", "今天白菜买一送一，只要9块9，购买链接请到淘宝门店大魔王淘宝店");

        PostUtils.postData("/api/comment/save",map);
    }

    @Test
    public void saveFab() throws Exception {
    }

    @Test
    public void findCommAndFabulous() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("aid","1");
        PostUtils.postData("/api/comment/findCommAndFabulous",map);
    }

    @Test
    public void saveCommentFabulos() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("cid", "2");
        map.add("aid","1");
        PostUtils.postData("/api/comment/saveCommentFabulos",map);
    }

}
package io.renren.modules.api.controller;

import io.renren.PostUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by ITMX on 2017/12/20.
 */
public class ApiActivityCenterControllerTest {

    /**
     * 测试获取列表
     * @throws Exception
     */
    @Test
    public void list() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("page", "1");
        map.add("limit", "10");

        PostUtils.postData("/api/activitycenter/list",map);
    }

}
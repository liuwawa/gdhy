package io.renren.modules.api.controller;

import io.renren.PostUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by ITMX on 2017/12/28.
 */
public class ApiTaskControllerTest {

    /**
     * 获取任务列表
     * @throws Exception
     */
    @Test
    public void list() throws Exception {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/task/list",map);
    }

    /**
     * 测试时段奖励
     * @throws Exception
     */
    @Test
    public void duration() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/duration",map);

        Thread.sleep(100);
        map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/duration",map);

        Thread.sleep(100);
        map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/duration",map);
    }

    /**
     * 测试阅读奖励
     * @throws Exception
     */
    @Test
    public void read() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/read",map);

        Thread.sleep(100);
        map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/read",map);

        Thread.sleep(100);
        map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/read",map);
    }

    /**
     * 测试阅读奖励
     * @throws Exception
     */
    @Test
    public void watch() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/watch",map);

        Thread.sleep(100);
        map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/watch",map);

        Thread.sleep(100);
        map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        PostUtils.postData("/api/task/watch",map);
    }

    /**
     * 测试日常任务
     * @throws Exception
     */
    @Test
    public void daily() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        map.add("typeId", 35);
        PostUtils.postData("/api/task/daily",map);

    }

    /**
     * 测试新手任务
     * @throws Exception
     */
    @Test
    public void novice() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        map.add("typeId", 39);
        PostUtils.postData("/api/task/novice",map);

    }

    /**
     * 测试获取收益记录
     * @throws Exception
     */
    @Test
    public void record() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("id", System.currentTimeMillis());
        map.add("page", 1);
        map.add("limit", 15);
        PostUtils.postData("/api/task/record",map);

    }

    /**
     * 测试获取签到列表
     * @throws Exception
     */
    @Test
    public void signRecord() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/task/signRecord",map);

    }


    /**
     * 测试签到
     * @throws Exception
     */
    @Test
    public void sign() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        PostUtils.postData("/api/task/sign",map);

    }

}
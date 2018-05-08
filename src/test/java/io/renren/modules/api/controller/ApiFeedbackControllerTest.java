package io.renren.modules.api.controller;

import io.renren.PostUtils;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;

/**
 * Created by ITMX on 2017/12/20.
 */
public class ApiFeedbackControllerTest {

    /**
     * 测试保存用户反馈信息
     * @throws Exception
     */
    @Test
    public void save() throws Exception {

        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<>();
        map.add("phone","15271519187");
        map.add("imgurl", "https://watch-everyday.oss-cn-shenzhen.aliyuncs.com/custom/20180105/f854179650ca46a4affbc4ca03346803.png,https://watch-everyday.oss-cn-shenzhen.aliyuncs.com/custom/20180105/43118d41059242519eda07327e38bab5.png,https://watch-everyday.oss-cn-shenzhen.aliyuncs.com/custom/20180105/4bebe73c49a246f3a3b315fc3d169ff9.png");
        map.add("opinion", "\\U8bf7\\U60a8\\U5199\\U4e0b\\U610f\\U89c1\\U6216\\U5efa\\U8bae");
        map.add("app_edition", "");
        map.add("sys_edition", "");
        map.add("sys_name", "");
        map.add("phone_mark", "");
        map.add("phone_equipment_id", "");
        PostUtils.postData("/api/feedback/save",map);
    }
}
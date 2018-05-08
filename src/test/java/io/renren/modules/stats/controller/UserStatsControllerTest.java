package io.renren.modules.stats.controller;

import io.renren.common.utils.DateUtils;
import io.renren.modules.stats.entity.UserStatsEntity;
import io.renren.modules.stats.service.UserStatsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by ITMX on 2018/2/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserStatsControllerTest {

    @Autowired
    private UserStatsService userStatsService;

    @Test
    public void range() throws Exception {
        String[] range = "2018-02-01 00:00:00 - 2018-02-08 00:00:00".split(" - ");

        List<UserStatsEntity> day = userStatsService.queryByRange("DAY", DateUtils.parse(range[0]), DateUtils.parse(range[1]));
        Assert.assertNotNull(day);
    }

}
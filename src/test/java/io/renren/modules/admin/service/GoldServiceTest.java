package io.renren.modules.admin.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ITMX on 2017/12/28.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoldServiceTest {

    @Autowired
    private GoldService goldService;

    @Test
    public void createNewEntity() throws Exception {
        goldService.createNewEntity(43L);
        goldService.createNewEntity(45L);
        goldService.createNewEntity(48L);
        goldService.createNewEntity(50L);
        goldService.createNewEntity(51L);
        goldService.createNewEntity(52L);
    }

}
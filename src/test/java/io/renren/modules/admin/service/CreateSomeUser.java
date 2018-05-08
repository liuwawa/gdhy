package io.renren.modules.admin.service;

import io.renren.common.utils.SignUtils;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ITMX on 2018/1/24.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateSomeUser {

    @Autowired
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() throws IOException {
        UserEntity userEntity = new UserEntity();
        long phone = 15992999163L;
        long start = System.currentTimeMillis();
        String password = SignUtils.getMD5("111111");
        File file = ResourceUtils.getFile("classpath:user.csv");
        file.delete();
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.append("phone,passwd,username\n");

        for (int i = 70079; i < 75079; i++) {
            userEntity.setUserId((long) i);
            userEntity.setUsername("用户ID" + i);
            userEntity.setPhone(String.valueOf(phone+i));
            userEntity.setAlipayBindOpenid(String.valueOf(i));
            userEntity.setWeixinBindOpenid(String.valueOf(i));
            userEntity.setPassword(password);
            userService.save(userEntity);
            fileWriter.append(userEntity.getPhone()).append(",96e79218965eb72c92a549dd5a330112,user_").append(String.valueOf(i)).append("\n");
        }
        fileWriter.flush();
        fileWriter.close();
        logger.info("消耗时间：" + (System.currentTimeMillis() - start));
    }
}

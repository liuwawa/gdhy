package io.renren;

import io.renren.common.utils.RedisCacheUtils;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
	@Autowired
	private RedisCacheUtils redisCacheUtils;
	@Autowired
	private UserService userService;


	@Test
	public void contextLoads() {
//		//测试保存实体
//		SysUserEntity user = new SysUserEntity();
//		user.setEmail("qqq@qq.com");
//		redisUtils.set("user", user);
//
//		//测试获取实体
//		user = redisUtils.get("user");
//		System.out.print("35，应该有值:");System.out.println(ToStringBuilder.reflectionToString(user));
//
//		//测试获取后过期
//		user = redisUtils.get("user",1);
//		System.out.print("39，应该有值:");System.out.println(ToStringBuilder.reflectionToString(user));
//
//		try { Thread.sleep(3000); } catch (InterruptedException ignore) {}
//		user = redisUtils.get("user");
//		System.out.print("43，应该为null:");System.out.println(ToStringBuilder.reflectionToString(user));
//
//		//测试保存实体，并自动过期
		UserEntity userEntity = new UserEntity();
//		redisUtils.set("userEntity",userEntity,5);
//
//		userEntity = redisUtils.get("userEntity");
//		System.out.print("50，应该有值:");System.out.println(ToStringBuilder.reflectionToString(userEntity));
//
//		try { Thread.sleep(6000); } catch (InterruptedException ignore) {}
//		userEntity = redisUtils.get("userEntity");
//		System.out.print("54，应该为null:");System.out.println(ToStringBuilder.reflectionToString(userEntity));
//
//		//测试基本数据类型
//		Integer integer = 123456;
//		redisUtils.set("integer",integer);
//		integer = redisUtils.get("integer");
//		System.out.print("60，应该有值:");System.out.println(integer);
//
//		Long aLong = 123456L;
//		redisUtils.set("aLong",aLong);
//		aLong = redisUtils.get("aLong");
//		System.out.print("65，应该有值:");System.out.println(aLong);
//
//		Float aFloat = 123456.1234F;
//		redisUtils.set("aFloat",aFloat);
//		aFloat = redisUtils.get("aFloat");
//		System.out.print("70，应该有值:");System.out.println(aFloat);
//
//		Double aDouble = 123456.1234D;
//		redisUtils.set("aDouble",aDouble);
//		aDouble = redisUtils.get("aDouble");
//		System.out.print("75，应该有值:");System.out.println(aDouble);
//
//		Boolean aBoolean = true;
//		redisUtils.set("aBoolean",aBoolean);
//		aBoolean = redisUtils.get("aBoolean");
//		System.out.print("80，应该有值:");System.out.println(aBoolean);
//
//		String string = "123456aa";
//		redisUtils.set("string",string);
//		string = redisUtils.get("string");
//		System.out.print("85，应该有值:");System.out.println(string);
//
//		//测试@Cacheable
//		userEntity = userService.queryObject(1L);
//		redisUtils.set("other_user", userEntity);
//		userEntity = redisUtils.get("other_user");
//		System.out.print("91，应该有值:");System.out.println(ToStringBuilder.reflectionToString(userEntity));

		userEntity = userService.queryObject(43L);
		userEntity = userService.queryObject(43L);
//		UserEntity userEntity = userService.queryObject(1L);
//
//
//		//存取list
//		ArrayList<SysUserEntity> list = new ArrayList<>();
//		for (int i = 0; i < 10; i++) {
//			SysUserEntity entity = new SysUserEntity();
//			entity.setUserId((long) i);
//			entity.setUsername("用户"+i);
//			entity.setEmail("email@qqq.com"+i);
//			entity.setMobile("15992222163");
//			entity.setCreateTime(new Date());
//			entity.setStatus(1);
//			entity.setCreateUserId((long) i);
//			entity.setPassword("jfsakdfjaslkdjflkasdfj"+i);
//			list.add(entity);
//		}
//		redisUtils.set("userList",list);
//		ArrayList<SysUserEntity> userList = redisUtils.get("userList");
//		System.out.print("114，应该有值:");System.out.println(ToStringBuilder.reflectionToString(userList));


	}

}

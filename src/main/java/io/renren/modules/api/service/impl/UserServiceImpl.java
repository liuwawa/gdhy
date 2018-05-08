package io.renren.modules.api.service.impl;


import io.renren.common.exception.RRException;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.MsgStatus;
import io.renren.modules.admin.service.EverydaySignService;
import io.renren.modules.admin.service.GoldService;
import io.renren.modules.api.dao.UserDao;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.entity.UserStatus;
import io.renren.modules.api.service.UserService;
import io.renren.modules.api.utils.AppLogUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("userService")
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private AppLogUtils appLogUtils;
	@Autowired
	private GoldService goldService;
	@Autowired
	private EverydaySignService everydaySignService;

	@Override
	@Cacheable(key = "'user:'+#userId")
	public UserEntity queryObject(Long userId){
		return userDao.queryObject(userId);
	}
	
	@Override
	public List<UserEntity> queryList(Map<String, Object> map){
		return userDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(UserEntity userEntity){
		if(userEntity.getPassword()!=null){
			//sha256加密密码
			String salt = RandomStringUtils.randomAlphanumeric(20);
			userEntity.setSalt(salt);
			userEntity.setPassword(new Sha256Hash(userEntity.getPassword(), salt).toHex());
		}
		//设置默认值
		userEntity.setGender(0);
		userEntity.setCreateTime(new Date());
		userEntity.setModifyTime(new Date());
		userEntity.setDisableFlag(0);
		userEntity.setAutoEnable(0);
		userEntity.setNoviceTask(0);
		userEntity.setStatus(UserStatus.NORMAL);

		userEntity.setDelFlag(0);

		userDao.save(userEntity);

		//创建金币账户
		goldService.createNewEntity(userEntity.getUserId());
		//创建签到账户
		everydaySignService.createNewEntity(userEntity.getUserId());
	}

	@Override
	@CacheEvict(key ="'user:'+#user.userId")
	public void update(UserEntity user){
		user.setModifyTime(new Date());
		userDao.update(user);
	}

	@Override
	@CacheEvict(key ="'user:'+#userId")
	public void delete(Long userId){
		userDao.delete(userId);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void deleteBatch(Long[] userIds){
		userDao.deleteBatch(userIds);
	}

	@Override
	public UserEntity queryByPhone(String phone) {
		return userDao.queryByPhone(phone);
	}

	/**
	 * 检查用户的状态，用于登录前
	 * @param user
	 */
	@Override
	public void checkStatus(UserEntity user) {
		if(user == null){
			throw new RRException(MsgStatus.USER_NOT_REGISTER);
		}
		//检查是否被禁用
		if(user.getDisableFlag() == 1){
			//是否为自动解禁
			if(user.getAutoEnable() == 0 || user.getAutoEnableTime() == null) {
				//不是自动解禁/无法自动解禁
				appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.DISABLE,"长期禁用",user.getUserId());
				if(user.getDisableFlag() == null){
    				throw new RRException(MsgStatus.USER_DISABLE);
                }else{
    				throw new RRException(user.getDisableMsg());
                }
			}else if(user.getAutoEnableTime().getTime()>System.currentTimeMillis()){
				//还没到解除禁用的时间
				String recoveryTime = " 恢复时间：" + DateUtils.format(user.getAutoEnableTime());
				String msg = user.getDisableMsg();
				msg = msg == null? MsgStatus.USER_DISABLE.getReasonPhrase() + recoveryTime : msg + recoveryTime;

				//记录日志
				appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.DISABLE,"暂时禁用，" + recoveryTime,user.getUserId());
				throw new RRException(msg ,MsgStatus.USER_DISABLE.value());
			}else{
				//解除禁用
				UserEntity userEntity = new UserEntity();
				userEntity.setUserId(user.getUserId());
				userEntity.setDisableFlag(0);
				userEntity.setDisableMsg(null);
				userEntity.setAutoEnable(0);
				userEntity.setAutoEnableTime(null);
				userDao.update(userEntity);

				//记录日志
				appLogUtils.i(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.AUTO_ENABLE,"自动解禁成功",user.getUserId());
			}
		}
	}

	/**
	 * 检查密码是否正确
	 * @param user
	 * @param password
	 */
	@Override
	public void checkPwd(UserEntity user, String password) {
		//如果密码错误
		if(user == null || !Objects.equals(user.getPassword(), new Sha256Hash(password, user.getSalt()).toHex())){
			throw new RRException(MsgStatus.BAD_PASSWORD);
		}
	}

	/**
	 * 修改密码
	 * @param user
	 * @param password
	 */
	@Override
	@CacheEvict(key = "'user:'+#user.userId")
	public void changePwd(UserEntity user, String password) {
		if(user == null){
			throw new RRException(MsgStatus.USER_NOT_REGISTER);
		}
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(user.getUserId());
		//sha256加密密码
		String salt = RandomStringUtils.randomAlphanumeric(20);
		userEntity.setSalt(salt);
		userEntity.setPassword(new Sha256Hash(password, salt).toHex());
		update(userEntity);
	}

	/**
	 * 修改绑定手机
	 * @param userEntity
	 * @param phone
	 */
	@Override
	@CacheEvict(key = "'user:'+#userEntity.userId")
	public void changePhone(UserEntity userEntity, String phone) {

		if(Objects.equals(userEntity.getPhone(), phone)){
			appLogUtils.w(AppLogUtils.LOG_TYPE.USER, AppLogUtils.OPERATION.MODIFY_PHONE,"新手机号与原绑定号码一致",userEntity.getUserId());
			throw new RRException(MsgStatus.PHONE_IDENTICAL);
		}
		UserEntity user = new UserEntity();
		user.setUserId(userEntity.getUserId());
		user.setPhone(phone);

		try {
			update(user);
		} catch (DuplicateKeyException e) {
			throw new RRException(MsgStatus.PHONE_ALREADY_BIND);
		}

	}

	@Override
	public UserEntity queryByWeChat(String openid) {
		return userDao.queryByWeChat(openid);
	}

	@Override
	public UserEntity queryByAlipay(String userId) {
		return userDao.queryByAlipay(userId);
	}
}

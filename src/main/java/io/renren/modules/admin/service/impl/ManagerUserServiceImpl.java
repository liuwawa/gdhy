package io.renren.modules.admin.service.impl;

import io.renren.modules.admin.service.ManagerUserService;
import io.renren.modules.api.dao.UserDao;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.utils.JwtUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("managerUserService")
public class ManagerUserServiceImpl implements ManagerUserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private JwtUtils jwtUtils;
	
	@Override
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
	public void save(UserEntity user){
		userDao.save(user);
	}
	
	@Override
	public void update(UserEntity user){
		userDao.update(user);
	}
	
	@Override
	public void delete(Long userId){
		userDao.delete(userId);
	}
	
	@Override
	public void deleteBatch(Long[] userIds){
		for (Long id : userIds) {
			jwtUtils.abandoned(id);
		}
		userDao.deleteBatch(userIds);
	}

	@Override
	public void updatedisable(JSONObject dailydata) {
		userDao.update(dailydata);
	}

}

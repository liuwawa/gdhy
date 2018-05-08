package io.renren.modules.api.service.impl;

import io.renren.common.exception.RRException;
import io.renren.common.utils.MsgStatus;
import io.renren.modules.api.dao.UserOssDao;
import io.renren.modules.api.entity.UploadType;
import io.renren.modules.api.entity.UserOssEntity;
import io.renren.modules.api.service.UserOssService;
import io.renren.modules.api.utils.AppLogUtils;
import io.renren.modules.oss.cloud.OSSFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ITMX on 2017/12/8.
 */
@Service("userOssService")
public class UserOssServiceImpl implements UserOssService {

    @Autowired
    private UserOssDao userOssDao;

    @Override
    public UserOssEntity queryObject(Long id) {
        return userOssDao.queryObject(id);
    }

    @Override
    public List<UserOssEntity> queryList(Map<String, Object> map) {
        return userOssDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return userOssDao.queryTotal(map);
    }

    @Override
    public void save(UserOssEntity userOssEntity) {
        userOssEntity.setCreateTime(new Date());
        userOssEntity.setDelFlag(0);
        userOssDao.save(userOssEntity);
    }

    @Override
    public void update(UserOssEntity userOssEntity) {
    }

    @Override
    public void delete(Long id) {
        userOssDao.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        userOssDao.deleteBatch(ids);
    }

    @Override
    public UserOssEntity uploadFile(MultipartFile file, UploadType type, Long userId) throws IOException {

        //上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String  url= OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

        //保存文件信息
        UserOssEntity userOssEntity = new UserOssEntity();
        userOssEntity.setCreateUser(userId);
        userOssEntity.setUrl(url);
        userOssEntity.setType(type);
        save(userOssEntity);

        return userOssEntity;

    }
}

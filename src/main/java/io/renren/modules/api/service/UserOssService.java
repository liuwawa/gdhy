package io.renren.modules.api.service;

import io.renren.modules.api.entity.UploadType;
import io.renren.modules.api.entity.UserOssEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ITMX on 2017/12/8.
 */
public interface UserOssService {

    UserOssEntity queryObject(Long id);

    List<UserOssEntity> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);

    void save(UserOssEntity userOssEntity);

    void update(UserOssEntity userOssEntity);

    void delete(Long id);

    void deleteBatch(Long[] ids);

    UserOssEntity uploadFile(MultipartFile file, UploadType type, Long userId) throws IOException;
}

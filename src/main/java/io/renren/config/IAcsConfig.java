package io.renren.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ITMX on 2018/4/4.
 */
@Configuration
public class IAcsConfig {

    @Value("${private.iacs.accessKeyId}")
    public String accessKeyId;
    @Value("${private.iacs.accessKeySecret}")
    public String accessKeySecret;
    @Value("${private.iacs.regionId}")
    public String regionId;
    @Value("${private.iacs.endpointName}")
    public String endpointName;
    @Value("${private.iacs.domain}")
    public String domain;

    @Bean
    public IAcsClient iacsClient() throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient defaultAcsClient = new DefaultAcsClient(profile);
        DefaultProfile.addEndpoint(endpointName, regionId, "afs", domain);
        return defaultAcsClient;
    }
}

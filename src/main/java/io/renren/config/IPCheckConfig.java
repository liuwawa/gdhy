package io.renren.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ITMX on 2018/1/18.
 */
@Component("iPCheckConfig")
@ConfigurationProperties(prefix = "private.ip-check")
public class IPCheckConfig {
    private String url = "https://smartip.market.alicloudapi.com/ssdata/dataservice/risk/ipprofile/query.json";
    private String appcode = "e68424a3e13140ffa22639136673a235";
    private String province = "广东省";
    private String city ="广州市";
    private String district = "花都区";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppcode() {
        return appcode;
    }

    public void setAppcode(String appcode) {
        this.appcode = appcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}

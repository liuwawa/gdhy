package io.renren;

import com.alibaba.druid.util.StringUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.renren.common.utils.SignUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by ITMX on 2017/12/21.
 */
public class PostUtils {

    private static Logger logger = LoggerFactory.getLogger(PostUtils.class);

    public final static RestTemplate TEMPLATE = new RestTemplate();

//    public final static String SECRET = "app_secret=6849ca01-4ee-e8752-93ac00008-cd33427";
//    public final static String BASE_URL = "http://app.v1.dzkandian.com:81";

    public final static String SECRET = "app_secret=3d0bffb0-6f-4fd09950fc76-ac5c9ac8-16";
    public final static String BASE_URL = "http://localhost:8080";

    public final static String TOKEN = "eyJ0eXAiOiJKV1QiLCJ1dWlkIjoiYjI1Y2VmNzM1ZGNmNDg3MmEzNGE1OTI3ZGU2NTE4ZmMiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3NTA4NCIsImlhdCI6MTUyNDEwMjMyNCwiZXhwIjoxNTI0NzA3MTI0fQ.gKOAbwX6OEBwBgqNtdmDKYAJWMuIAhXwHmUborLAlLpVtamLfMS_G_2qIvRja3er-fbsOALIj48j2Gc6ZfRTmw";

    /**
     * post提交数据
     * @param url
     * @param map
     */
    public static void postData(String url, LinkedMultiValueMap<String,Object> map){

        fillSign(map, SECRET);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("TOKEN", TOKEN);

        String result = TEMPLATE.postForObject(BASE_URL + url,new HttpEntity<>(map, httpHeaders),String.class,map);
        System.out.println(result);
        assertNotNull(result);
        JsonObject r = new JsonParser().parse(result).getAsJsonObject();
        assertTrue(r.get("msg").getAsString(),r.get("status").getAsBoolean());
    }


    /**
     * 签名数据以备提交，计算签名并填充到map
     * @param info
     * @param secret
     */
    public static void fillSign(LinkedMultiValueMap<String,Object> info, String secret) {

        info.add("timestamp", String.valueOf(System.currentTimeMillis()));

        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(info.toSingleValueMap().entrySet());
        infoIds.sort(Comparator.comparing(arg0 -> (arg0.getKey())));
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Object> entry : infoIds) {
            String entryKey = entry.getKey();
            String entryValue = String.valueOf(entry.getValue());

            //除去sign字段和空字段
            if("sign".equals(entryKey) || entryValue == null || StringUtils.isEmpty(entryValue)){
                continue;
            }
            sb.append(entryKey);
            sb.append("=");
            sb.append(entryValue);
            sb.append("&");
        }
        sb.append(secret);
        logger.debug(sb.toString());
        info.add("sign",SignUtils.getMD5(sb.toString()));
    }
}

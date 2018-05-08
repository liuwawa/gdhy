package io.renren.common.utils;

import com.alibaba.druid.util.StringUtils;
import io.renren.common.exception.RRException;
import io.renren.config.IPCheckConfig;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * IP地址
 * 
 * @author itmx.xyz
 * @email itmx@foxmail.com
 * @date 2018年1月18日 下午14:57:02
 */
public class IPUtils {

    private static RestTemplate restTemplate = new RestTemplateBuilder().additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8"))).build();
    private static Logger logger = LoggerFactory.getLogger(IPUtils.class);

	/**
	 * 获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
    	String ip;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
        	logger.error("IPUtils ERROR ", e);
        	return "";
        }

        //使用代理，则获取第一个IP地址
        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
			if(ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}

        logger.info("操作IP：" + ip);
        return ip;
    }

    /**
     * 检查操作ip归属区域是否正确 如果检查不通过会抛出异常
     * @param ip
     */
    public static void checkIP(String ip){

        //排除开发者内网
	    if(ip.startsWith("192.168.0.") || "0:0:0:0:0:0:0:1".equals(ip)){
	        return;
        }

        try {
            JSONObject jsonObject = ipToAddress(ip);
            if(jsonObject == null){
                throw new RRException("IP地址检查功能故障，请联系开发人员");
            }

            //获取ip配置
            IPCheckConfig iPCheckConfig = SpringContextUtils.getBean("iPCheckConfig", IPCheckConfig.class);

            if(
                    jsonObject.getInt("showapi_res_code") == 0 &&
                    Objects.equals(iPCheckConfig.getProvince(), jsonObject.getJSONObject("showapi_res_body").getString("region")) && //检查真实使用地所在省
                    Objects.equals(iPCheckConfig.getCity(), jsonObject.getJSONObject("showapi_res_body").getString("city")) && //真实使用地所在市
                    Objects.equals(iPCheckConfig.getDistrict(), jsonObject.getJSONObject("showapi_res_body").getString("county"))//检查真实使用地所在区县
                    ){
                //只有在这个县区才能继续操作
                return;
            }
            logger.warn("IP地址异常：" + jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RRException("IP地址异常，请不要异地操作");
    }

    /**
     * 通过IP地址获取归属地址
     * @param ip
     * @return
     */
    public static JSONObject ipToAddress(String ip){

        if(ip == null){
            return null;
        }

        //取第一个IP地址
        if(ip.contains(",")){
            ip = ip.split(",")[0];
        }

        if(StringUtils.isEmpty(ip)){
            return null;
        }

        //获取ip配置
        IPCheckConfig iPCheckConfig = SpringContextUtils.getBean("iPCheckConfig", IPCheckConfig.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "APPCODE " + iPCheckConfig.getAppcode());
        JSONObject jsonObject = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(iPCheckConfig.getUrl() + "?ip=" + ip, HttpMethod.GET, new HttpEntity<String>(null, headers), String.class);
            String result = response.getBody();
            jsonObject = JSONObject.fromObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }

    public static JSONObject ipToAddressByTaoBao(String ip){

        if(ip == null){
            return null;
        }

        //取第一个IP地址
        if(ip.contains(",")){
            ip = ip.split(",")[0];
        }

        if(StringUtils.isEmpty(ip)){
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
        headers.add(HttpHeaders.HOST,"ip.taobao.com");
        headers.add(HttpHeaders.COOKIE,"cookie2=1c6fc3d57dd2b61a70b3f290b807e5b2; t=d2336ee203ad9258f805df4505b45604; _tb_token_=e5e7d3333e3bd; cna=UbXgEqkD51ACAbc26GIpRq9q; thw=cn; hng=CN%7Czh-CN%7CCNY%7C156; miid=6513251921664598760; tg=0; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; ctoken=bjsS52kZw44zgEpYJwYNp4p-render; tk_trace=oTRxOWSBNwn9dPy5J8INBQzvy4fzIaMzvDawskiS2V%2FQXa%2FDQpibpZXvRTMHfRMnphHJQ3wcRskPtpmT%2FtYXQU4oCbVJVwSwchPbyaIs2M11wuLJW%2FPv2dax%2F0%2FByMGuIfz6RKGyUZIxb92yZkxWzqTq%2Ff%2FLGK9nkXs2OxEy8cFtWvZCWh0pPtj3y%2BdIkOfviIyc8ySfIj6OCVXWUydOCU1%2FyNnNl9RXV9rcfAPc6953WJasQEW8pZ1t7DNk9tsmdBaadlz3aqdVDyTx3VyAgu58nundyEVD%2FgG6M7yAF3HBJwt8vLkgslvO00eiULL9gXFL31QqHPRoXzsYyM1kuS3npvCDfAR3egGDCkWqLeY0W1JpkK2UMhZ7TTQU5GaLbWwg7kk%3D; _m_h5_tk=bb5e1018ee2cff3073ff4db43dcf060d_1523530850588; _m_h5_tk_enc=42966e55d74504deedca2618b3ac4171; UM_distinctid=162b95d6b9b1bb-0b714c8528f168-454f032b-1fa400-162b95d6b9c8c0; v=0; lgc=%5Cu5C0F%5Cu8C2D%5Cu5929%5Cu5929; tracknick=%5Cu5C0F%5Cu8C2D%5Cu5929%5Cu5929; dnk=%E5%B0%8F%E8%B0%AD%E5%A4%A9%E5%A4%A9; uc3=nk2=sym%2B%2BJM5sXQ%3D&id2=UoCLHt6j8rC9qA%3D%3D&vt3=F8dBz4D93SWUhVTPQRw%3D&lg2=UtASsssmOIJ0bQ%3D%3D; existShop=MTUyMzkyNjk3Mw%3D%3D; sg=%E5%A4%A96f; csg=0b1259cd; cookie1=VAMUH%2BqpVw%2F%2F%2FKwllIdzzJSIhbKy9v9zjRsK7YsoW94%3D; unb=1129499726; skt=ed3cc66c9ab157b4; _cc_=URm48syIZQ%3D%3D; _l_g_=Ug%3D%3D; _nk_=%5Cu5C0F%5Cu8C2D%5Cu5929%5Cu5929; cookie17=UoCLHt6j8rC9qA%3D%3D; uc1=cookie16=VFC%2FuZ9az08KUQ56dCrZDlbNdA%3D%3D&cookie21=URm48syIYn73&cookie15=WqG3DMC9VAQiUQ%3D%3D&existShop=false&pas=0&cookie14=UoTePTwbUiFNmg%3D%3D&cart_m=0&tag=8&lng=zh_CN; mt=ci=6_1; whl=-1%260%261517882547376%261523927192532");

        JSONObject jsonObject = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip, HttpMethod.GET, new HttpEntity<String>(null, headers), String.class);
            String result = response.getBody();
            jsonObject = JSONObject.fromObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }
}

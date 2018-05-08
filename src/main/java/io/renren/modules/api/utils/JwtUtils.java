package io.renren.modules.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * jwt工具类
 * Created by ITMX on 2017/12/6.
 */
@ConfigurationProperties(prefix = "private.jwt")
@Component
public class JwtUtils {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String,Object> tokenRedisTemplate;

    private String secret;
    private Long expire;
    private String header;

    /**
     * 生成jwt token
     */
    public String generateToken(Long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        String uuid = UUID.randomUUID().toString().replace("-", "");

        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("uuid", uuid)
                .setSubject(Long.toString(userId))
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        tokenRedisTemplate.opsForValue().set(Long.toString(userId),uuid,expire, TimeUnit.SECONDS);
        return jwt;
    }

    /**
     * 获取jwt中存储的内容
     * @param token
     * @return
     */
    public Claims getClaimByToken(String token) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            Claims body = jws.getBody();
            String headUUID = (String) jws.getHeader().get("uuid");
            String redisUUID = (String) tokenRedisTemplate.opsForValue().get(body.getSubject());
            if(!Objects.equals(redisUUID, headUUID)){
                return null;
            }
            tokenRedisTemplate.expire(body.getSubject(),expire,TimeUnit.SECONDS);
            return body;
        }catch (Exception e){
            logger.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * 在redis中废弃某个用户的token
     * @param userId
     */
    public void abandoned(Long userId){
        tokenRedisTemplate.delete(Long.toString(userId));
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        //TODO 暂时不理会强制过期时间，因为使用了redis续期
        return false;
//        return expiration.before(new Date());
    }

    /**
     * 将token和过期时间装入map中
     * @param token
     * @return
     */
    public Map<String,Object> makeToMap(String token) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("token", token);
        map.put("expire", expire);
        return map;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}

package io.renren.config.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ITMX on 2017/12/22.
 */
public class BaseRedisConfig {
    private Integer database = 0;
    private String host = "localhost";
    private Integer port = 6379;
    private String password = "";
    private Integer timeout = 6000;//（毫秒）
    private Map<String,Object> pool = new HashMap<>();

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Map<String, Object> getPool() {
        return pool;
    }

    public void setPool(Map<String, Object> pool) {
        this.pool = pool;
    }
}
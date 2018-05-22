package com.eposapp.repository.redis;

import com.eposapp.common.constant.RedisConstans;
import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepository {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOps;

    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOps;

    public void setSession(Map user) {
        valueOps.set(RedisConstans.USER_ACCESS_TOKEN+String.valueOf(user.get(SysConstants.USER_TOKEN)), user, 30, TimeUnit.MINUTES);
    }

    public Map getSession(String userAccessToken) {
        Map user = (Map) valueOps.get(RedisConstans.USER_ACCESS_TOKEN+userAccessToken);
        if (user == null) {
            return null;
        }
        setSession(user);
        return user;
    }

    public Boolean delSession(String userAccessToken){
        if(StringUtils.isBlank(userAccessToken)){
            return  false;
        }
        return valueOps.getOperations().delete(RedisConstans.USER_ACCESS_TOKEN+userAccessToken);
    }


}
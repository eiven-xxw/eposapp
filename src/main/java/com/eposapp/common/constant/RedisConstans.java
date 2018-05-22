package com.eposapp.common.constant;

import com.eposapp.common.util.MD5Util;

import java.util.Random;

/**
 * @Author: eiven
 * @Date: Created in 21:40 2018/5/17
 * @Description: redis 目录结构
 */

public class RedisConstans {
    private final static Random RD = new Random();
    /**
     * redis 库名
     * */
    public static final String REDIS_DB = "EPOSAPP:";

    /**
     * USER_ACCESS_TOKEN 目录 存放用户信息
     * 用于登录验证
     * */
    public static final String USER_ACCESS_TOKEN = "EPOSAPP:USERACCESSTOKEN:";

}

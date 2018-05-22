package com.eposapp.common.constant;

import com.eposapp.common.util.MD5Util;

/**
 * @Author: eiven
 * @Date: Created in 23:24 2018/5/17
 */
public class TokenConstants {


    public static String generateToken(){
        String token = System.currentTimeMillis()+""+(int)((Math.random()*9+1)*10000);

        return MD5Util.encodeMD5(token);
    }
    public static String generateUserId(){
        String userId = System.currentTimeMillis()+""+(int)((Math.random()*9+1)*10000);

        return userId;
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.encodeMD5("123456"));
    }
}

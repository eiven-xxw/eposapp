
package com.eposapp.controller;

import com.eposapp.common.util.JsonResult;
import com.eposapp.repository.redis.RedisRepository;
import com.eposapp.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
/**
 * @author eiven
 * */
@RestController
public class SysLoginController extends BaseController{
	
	  /**
     * 日志（slf4j->logback）
     */
    private static final Logger logger = LoggerFactory.getLogger(SysLoginController.class);

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisRepository redisRepository;


    @RequestMapping(value = "doLogin",method = RequestMethod.POST)
    public JsonResult doLogin(@RequestBody Map paramsMap) throws Exception{
        return sysUserService.doLogin(paramsMap);
    }
    @RequestMapping(value = "layout",method = RequestMethod.POST)
    public JsonResult layout(HttpServletRequest request,HttpServletResponse response) throws Exception{
        if(sysUserService.layout(request)){
           return JsonResult.putSuccess();
        }else{
           return JsonResult.putFail();
        }
    }
}
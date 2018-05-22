
package com.eposapp.controller;

import com.eposapp.common.constant.JsonConstans;
import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.MD5Util;
import com.eposapp.common.util.StringUtils;
import com.eposapp.entity.SysUserEntity;
import com.eposapp.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
/**
 * @author eiven
 * */
@RestController
@RequestMapping("user")
public class SysUserController extends BaseController{



	  /**
     * 日志（slf4j->logback）
     */
    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("list")
    public JsonResult userList(@RequestBody Map paramsMap){
        return JsonResult.putSuccess(sysUserService.findUserList(paramsMap));
    }
    /**
     * @ RequestParam Map paramsMap @ModelAttribute SysUserEntity userEntity
     * */
    @RequestMapping("doSave")
    public JsonResult doSave(@RequestBody Map paramsMap)  {

        return  sysUserService.doSaveOrUpdate(paramsMap);
    }
    @RequestMapping("delete")
    public JsonResult delete(@RequestBody Map paramsMap){
        String id = StringUtils.getMapKeyValue(paramsMap,"id");
        if(sysUserService.delete(SysUserEntity.class,id)){
            return JsonResult.putSuccess();
        }else{
            return JsonResult.putFail();
        }
    }

    @RequestMapping("updatePassword")
    public JsonResult updatePassword(@RequestBody Map paramsMap){
        String id = StringUtils.getMapKeyValue(paramsMap,"id");
        String oldPassword = StringUtils.getMapKeyValue(paramsMap,"oldPassword");
        String newPassword = StringUtils.getMapKeyValue(paramsMap,"newPassword");

        SysUserEntity sysUserEntity = sysUserService.findById(new SysUserEntity(),id);

        if(sysUserEntity.getPassword().equals(MD5Util.encodeMD5(oldPassword))){
            sysUserEntity.setPassword(MD5Util.encodeMD5(newPassword));

            return JsonResult.getResult(sysUserService.update(sysUserEntity));
        }else{
            return JsonResult.putFail(JsonConstans.ERR_PASSORD_NOTEQUAL);
        }
    }


}
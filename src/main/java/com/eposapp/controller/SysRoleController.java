
package com.eposapp.controller;

import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.entity.SysRoleEntity;
import com.eposapp.service.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author eiven
 * */
@RestController
@RequestMapping("role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

	  /**
     * 日志（slf4j->logback）
     */
    private static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);



    @RequestMapping("list")
    public JsonResult roleList(@RequestBody Map paramsMap){

		return JsonResult.putSuccess(sysRoleService.findRoleList(paramsMap));
    }

    @RequestMapping("doSave")
    public JsonResult doSave(@RequestBody Map paramsMap){

        return sysRoleService.doSaveOrUpdate(paramsMap);
    }
    @RequestMapping("delete")
    public JsonResult delete(@RequestBody Map paramsMap){
        String id = StringUtils.getMapKeyValue(paramsMap,"id");
        if(sysRoleService.delete(SysRoleEntity.class,id)){
            return JsonResult.putSuccess();
        }else{
            return JsonResult.putFail();
        }
    }

}
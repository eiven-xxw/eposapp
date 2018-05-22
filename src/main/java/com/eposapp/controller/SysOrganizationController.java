
package com.eposapp.controller;

import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.entity.SysOrganizationEntity;
import com.eposapp.entity.SysRoleEntity;
import com.eposapp.service.SysOrganizationService;
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
@RequestMapping("organization")
public class SysOrganizationController {

    @Autowired
    private SysOrganizationService sysOrganizationService;

    /**
     * 日志（slf4j->logback）
     */
    private static final Logger logger = LoggerFactory.getLogger(SysOrganizationController.class);


    
    @RequestMapping("list")
    public JsonResult orgList(@RequestBody Map paramsMap){


		return JsonResult.putSuccess(sysOrganizationService.findOrgList(paramsMap));
    }

    @RequestMapping("doSave")
    public JsonResult doSave(@RequestBody Map paramsMap){

        return sysOrganizationService.doSaveOrUpdate(paramsMap);
    }
    @RequestMapping("delete")
    public JsonResult delete(@RequestBody Map paramsMap){
        String id = StringUtils.getMapKeyValue(paramsMap,"id");
        if(sysOrganizationService.delete(SysOrganizationEntity.class,id)){
            return JsonResult.putSuccess();
        }else{
            return JsonResult.putFail();
        }
    }

}
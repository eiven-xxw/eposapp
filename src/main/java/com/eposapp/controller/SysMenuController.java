
package com.eposapp.controller;

import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.entity.SysMenuEntity;
import com.eposapp.entity.SysRoleEntity;
import com.eposapp.service.SysMenuService;
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
@RequestMapping("menu")
public class SysMenuController extends BaseController{

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 日志（slf4j->logback）
     */
    private static final Logger logger = LoggerFactory.getLogger(SysMenuController.class);


    @RequestMapping("initMenu")
    public JsonResult initMenu(@RequestBody Map paramsMap){

		return null;
    }

    @RequestMapping("list")
    public JsonResult menuList(@RequestBody Map paramsMap){

        return JsonResult.putSuccess(sysMenuService.findMenuList(paramsMap));
    }

    @RequestMapping("doSave")
    public JsonResult doSave(@RequestBody Map paramsMap){

        return sysMenuService.doSaveOrUpdate(paramsMap);
    }
    @RequestMapping("delete")
    public JsonResult delete(@RequestBody Map paramsMap){
        String id = StringUtils.getMapKeyValue(paramsMap,"id");
        if(sysMenuService.delete(SysMenuEntity.class,id)){
            return JsonResult.putSuccess();
        }else{
            return JsonResult.putFail();
        }
    }
}
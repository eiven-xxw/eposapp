
package com.eposapp.controller;

import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.entity.SysDepartmentEntity;
import com.eposapp.entity.SysUserEntity;
import com.eposapp.service.SysDepartmentService;
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
@RequestMapping("department")
public class SysDepartmentController {

    @Autowired
    private SysDepartmentService sysDepartmentService;
	  /**
     * 日志（slf4j->logback）
     */
    private static final Logger logger = LoggerFactory.getLogger(SysDepartmentController.class);



    @RequestMapping("list")
    public JsonResult roleList(@RequestBody Map paramsMap){

		return JsonResult.putSuccess(sysDepartmentService.findDeptList(paramsMap));
    }

    @RequestMapping("doSave")
    public JsonResult doSave(@RequestBody Map paramsMap){


        return sysDepartmentService.doSaveOrUpdate(paramsMap);
    }
    @RequestMapping("delete")
    public JsonResult delete(@RequestBody Map paramsMap){
        String id = StringUtils.getMapKeyValue(paramsMap,"id");
        if(sysDepartmentService.delete(SysDepartmentEntity.class,id)){
            return JsonResult.putSuccess();
        }else{
            return JsonResult.putFail();
        }
    }

}
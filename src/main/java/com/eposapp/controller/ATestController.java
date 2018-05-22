
package com.eposapp.controller;

import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.entity.SysDepartmentEntity;
import com.eposapp.repository.mysql.SysDepartmentRepository;
import com.eposapp.service.SysDepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author eiven
 * */
@RestController
public class ATestController {

    @Autowired
    private SysDepartmentService sysDepartmentService;
    @Autowired
    private SysDepartmentRepository sysDepartmentRepository;

	  /**
     * 日志（slf4j->logback）
     */
    private static final Logger logger = LoggerFactory.getLogger(ATestController.class);



    @RequestMapping("test")
    public JsonResult doSave(@ModelAttribute SysDepartmentEntity sysDepartmentEntity){
        sysDepartmentEntity = sysDepartmentRepository.findById(new SysDepartmentEntity() ,sysDepartmentEntity.getId());
        sysDepartmentRepository.update(sysDepartmentEntity);
        return null;
    }

}
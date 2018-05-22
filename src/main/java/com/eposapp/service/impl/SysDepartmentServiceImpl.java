

package com.eposapp.service.impl;

import com.eposapp.common.constant.JsonConstans;
import com.eposapp.common.constant.ResponseCodeConstans;
import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.util.EntityUtil;
import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.common.util.ValidationUtils;
import com.eposapp.entity.SysDepartmentEntity;
import com.eposapp.entity.SysOrganizationEntity;
import com.eposapp.repository.mysql.SysDepartmentRepository;
import com.eposapp.repository.mysql.SysOrganizationRepository;
import com.eposapp.service.SysDepartmentService;
import com.eposapp.service.SysOrganizationService;
import com.eposapp.threadlocal.SystemSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eiven
 * */
@Service
public class SysDepartmentServiceImpl implements SysDepartmentService {

	@Autowired
	private SysDepartmentRepository sysDepartmentRepository;

	@Override
	public JsonResult doSaveOrUpdate(Map paramsMap) {
		try {
			String  isVali= ValidationUtils.validation(paramsMap);
			if(!isVali.equals(ResponseCodeConstans.SUCCESS)){
				return JsonResult.putFail(isVali);
			}
			String id = StringUtils.getMapKeyValue(paramsMap,"id");
			String code = StringUtils.getMapKeyValue(paramsMap,"code");
			boolean isExistCode = sysDepartmentRepository.isExistCode(SysDepartmentEntity.class,code,id);
			if(isExistCode){
				return JsonResult.putFail(JsonConstans.ERR_CODE_EXISTS);
			}
			SysDepartmentEntity sysDepartmentEntity = null;
			boolean isSave = false;
			if(StringUtils.isNotBlank(id)){
				sysDepartmentEntity = sysDepartmentRepository.findById(new SysDepartmentEntity(),id);
				if(sysDepartmentEntity==null){
					return JsonResult.putFail(JsonConstans.ERR_NOT_EXISTED);
				}
				EntityUtil.putMapDataIntoEntity(paramsMap,sysDepartmentEntity);
				isSave = sysDepartmentRepository.update(sysDepartmentEntity);
			}else{
				sysDepartmentEntity = new SysDepartmentEntity();
				EntityUtil.putMapDataIntoEntity(paramsMap,sysDepartmentEntity);
				isSave = sysDepartmentRepository.save(sysDepartmentEntity);
			}
			if(isSave){
				return JsonResult.putSuccess();
			}else{
				return JsonResult.putFail(JsonConstans.OPERATION_FAILURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.putFail(JsonConstans.OPERATION_FAILURE);
		}
	}
	@Override
	public boolean delete(Class clazz, String id) {
		return sysDepartmentRepository.delete(clazz,id);
	}

	@Override
	public Map<String, Object> findDeptList(Map paramsMap) {
		String cnName = StringUtils.getMapKeyValue(paramsMap,"cnName");
		String pageNoStr = StringUtils.getMapKeyValue(paramsMap,"pageNo");
		String pageSizeStr = StringUtils.getMapKeyValue(paramsMap,"pageSize");
		String sql = "SELECT depart.id,depart.parentId,depart2.cnName AS parentName,\n" +
				"	depart.`code`,depart.cnName,depart.enName,depart.sequence,\n" +
				"	depart.phone,depart.fax,depart.principal,depart.remarks,depart.version,\n" +
				"	depart.isDelete,depart.isUse,depart.createId,depart.createTime,u.cnName AS createName\n" +
				" FROM sys_department depart \n" +
				"LEFT JOIN sys_department depart2 ON depart2.id = depart.id\n" +
				"LEFT JOIN sys_user u ON u.id = depart.createId" ;
		String orgId= SystemSession.getOrgId();
		boolean isHasWhere = false;
		if(StringUtils.isNotBlank(orgId)&& !SysConstants.ROOT_ID.equals(orgId)){
			sql+=" WHERE depart.orgId ='"+ orgId +"'";
			isHasWhere = true;
		}
		Map<String,String> conditionParams = new HashMap<>(3);
		if(StringUtils.isNotBlank(cnName)){
			if(isHasWhere){
				sql +=" AND depart.cnName =:cnName ";
			}else{
				sql +=" WHERE depart.cnName =:cnName ";
			}
			conditionParams.put("cnName",cnName);
		}
		Integer pageNo =null;
		Integer pageSize =null;
		if(StringUtils.isNotBlank(pageNoStr)){
			pageNo = Integer.valueOf(pageNoStr);
		}
		if(StringUtils.isNotBlank(pageSizeStr)){
			pageSize = Integer.valueOf(pageSizeStr);
		}
		return sysDepartmentRepository.findByNativeQuery(sql,conditionParams,Transformers.ALIAS_TO_ENTITY_MAP,pageNo,pageSize);
	}
}
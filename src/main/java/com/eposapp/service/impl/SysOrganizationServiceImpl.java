

package com.eposapp.service.impl;

import com.eposapp.common.constant.JsonConstans;
import com.eposapp.common.constant.ResponseCodeConstans;
import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.util.EntityUtil;
import com.eposapp.common.util.JsonResult;
import com.eposapp.common.util.StringUtils;
import com.eposapp.common.util.ValidationUtils;
import com.eposapp.entity.SysOrganizationEntity;
import com.eposapp.repository.mysql.SysOrganizationRepository;
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
public class SysOrganizationServiceImpl implements SysOrganizationService {

	@Autowired
	private SysOrganizationRepository sysOrganizationRepository;

	@Override
	public JsonResult doSaveOrUpdate(Map paramsMap) {
		try {
			String  isVali= ValidationUtils.validation(paramsMap);
			if(!isVali.equals(ResponseCodeConstans.SUCCESS)){
				return JsonResult.putFail(isVali);
			}
			String id = StringUtils.getMapKeyValue(paramsMap,"id");
			String code = StringUtils.getMapKeyValue(paramsMap,"code");
			boolean isExistCode = sysOrganizationRepository.isExistCode(SysOrganizationEntity.class,code,id);
			if(isExistCode){
				return JsonResult.putFail(JsonConstans.ERR_CODE_EXISTS);
			}
			SysOrganizationEntity sysOrganizationEntity = null;
			boolean isSave = false;
			if(StringUtils.isNotBlank(id)){
				sysOrganizationEntity = sysOrganizationRepository.findById(new SysOrganizationEntity(),id);
				EntityUtil.putMapDataIntoEntity(paramsMap,sysOrganizationEntity);
				isSave = sysOrganizationRepository.update(sysOrganizationEntity);
			}else{
				sysOrganizationEntity = new SysOrganizationEntity();
				EntityUtil.putMapDataIntoEntity(paramsMap,sysOrganizationEntity);
				isSave = sysOrganizationRepository.save(sysOrganizationEntity);
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
		return sysOrganizationRepository.delete(clazz,id);
	}

	@Override
	public Map<String, Object> findOrgList(Map paramsMap) {
		String cnName = StringUtils.getMapKeyValue(paramsMap,"cnName");
		String pageNoStr = StringUtils.getMapKeyValue(paramsMap,"pageNo");
		String pageSizeStr = StringUtils.getMapKeyValue(paramsMap,"pageSize");
		String sql = "SELECT\n" +
				"	o.id,o.`code`,o.cnName,o.enName,o.address,\n" +
				"	o.createId,o.createTime,o.email,o.fax,o.isDelete,\n" +
				"  o.isUse,o.phone,o.remarks,o.version,u.cnName AS createName\n" +
				"FROM\n" +
				"	sys_organization o\n" +
				"LEFT JOIN sys_user u ON u.id = o.createId" ;
		String orgId= SystemSession.getOrgId();
		boolean isHasWhere = false;
		if(StringUtils.isNotBlank(orgId)&& !SysConstants.ROOT_ID.equals(orgId)){
			sql+=" WHERE u.orgId ='"+ orgId +"'";
			isHasWhere = true;
		}
		Map<String,String> conditionParams = new HashMap<>(3);
		if(StringUtils.isNotBlank(cnName)){
			if(isHasWhere){
				sql +=" AND o.cnName =:cnName ";
			}else{
				sql +=" WHERE u.cnName =:cnName ";
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
		return sysOrganizationRepository.findByNativeQuery(sql,conditionParams,Transformers.ALIAS_TO_ENTITY_MAP,pageNo,pageSize);
	}
}
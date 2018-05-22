

package com.eposapp.service.impl;

import com.eposapp.common.constant.TokenConstants;
import com.eposapp.common.constant.JsonConstans;
import com.eposapp.common.constant.ResponseCodeConstans;
import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.helper.Reflector;
import com.eposapp.common.util.*;
import com.eposapp.entity.SysUserEntity;
import com.eposapp.repository.mysql.SysUserRepository;
import com.eposapp.repository.redis.RedisRepository;
import com.eposapp.service.SysUserService;
import com.eposapp.threadlocal.SystemSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eiven
 * */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserEntity,String> implements SysUserService {

    public static final String PASSWORD = "password";

	@Autowired
	private SysUserRepository sysUserRepository;
    @Autowired
    RedisRepository redisRepository;


    @Override
    public Map<String, Object> findUserList(Map paramsMap) {
        String cnName = StringUtils.getMapKeyValue(paramsMap,"cnName");
        String pageNoStr = StringUtils.getMapKeyValue(paramsMap,"pageNo");
        String pageSizeStr = StringUtils.getMapKeyValue(paramsMap,"pageSize");
        String sql = "SELECT u.id,u.age,u.birthday,u.cnName,u.`code`,u.createTime,\n" +
                "	u.createId,cuser.cnName createName,u.departmentId,d.cnName AS departmentName,u.orgId,org.cnName AS orgName,\n" +
                "	u.email,u.enName,u.identityCard,u.isUse,u.loginIp,u.loginTime,u.mobile,u.phone,u.photo,u.remarks,u.version\n" +
                " FROM sys_user u\n" +
                " LEFT JOIN sys_organization org ON org.id = u.orgId\n" +
                " LEFT JOIN sys_department d ON d.id = u.departmentId\n" +
                " LEFT JOIN sys_user cuser ON cuser.id = u.createId\n" ;
        String orgId = SystemSession.getOrgId();
        boolean isHasWhere = false;
        if(StringUtils.isNotBlank(orgId)&& !SysConstants.ROOT_ID.equals(orgId)){
            sql+=" WHERE u.orgId ='"+ orgId +"'";
            isHasWhere =true;
        }
        Map<String,String> conditionParams = new HashMap<>(3);
        if(StringUtils.isNotBlank(cnName)){
            if(isHasWhere){
                sql +=" AND u.cnName =:cnName ";
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
        return sysUserRepository.findByNativeQuery(sql,conditionParams,Transformers.ALIAS_TO_ENTITY_MAP,pageNo,pageSize);
    }

    public static void main(String[] args) {
        SysUserEntity sysUserEntity = new SysUserEntity();
        System.out.println(sysUserEntity.getPassword().getClass().getName());
    }


    @Override
	public JsonResult doSaveOrUpdate(Map paramsMap) {
        try {
            String  isVali= ValidationUtils.validation(paramsMap);
            if(!isVali.equals(ResponseCodeConstans.SUCCESS)){
                return JsonResult.putFail(isVali);
            }
            String id = StringUtils.getMapKeyValue(paramsMap,"id");

            String code = StringUtils.getMapKeyValue(paramsMap,"code");
            boolean isExistCode = sysUserRepository.isExistCode(SysUserEntity.class,code,id);
            if(isExistCode){
                return JsonResult.putFail(JsonConstans.ERR_CODE_EXISTS);
            }
            SysUserEntity sysUserEntity = null;
            boolean isSave = false;
            if(StringUtils.isNotBlank(id)){
                if(paramsMap.containsKey(PASSWORD)){
                    paramsMap.remove(PASSWORD);
                }
                sysUserEntity = sysUserRepository.findById(new SysUserEntity(),id);
                if(sysUserEntity==null){
                    return JsonResult.putFail(JsonConstans.ERR_NOT_EXISTED);
                }
                EntityUtil.putMapDataIntoEntity(paramsMap,sysUserEntity);
                isSave = sysUserRepository.update(sysUserEntity);
            }else{
                sysUserEntity = new SysUserEntity();
                EntityUtil.putMapDataIntoEntity(paramsMap,sysUserEntity);
                sysUserEntity.setPassword(MD5Util.encodeMD5(sysUserEntity.getPassword()));
                isSave = sysUserRepository.save(sysUserEntity);
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
	public Map<String,Object> getUserMapByCode(String code) {
		String sql ="SELECT * FROM sys_user u WHERE u.code =:code AND u.isUse = 1 ";
		Map<String,String> conditionParams = new HashMap<>(1);
		conditionParams.put("code",code);
		Map<String,Object> userMap = sysUserRepository.findByNativeQuery(sql,conditionParams, Transformers.ALIAS_TO_ENTITY_MAP,null,null);
		List list = (List) userMap.get(SysConstants.ROWS);
		if(list.size()==1){
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

    @Override
    public boolean layout(HttpServletRequest request) {
        boolean isLayout = false;
        String userAccessToken = request.getHeader(SysConstants.USER_ACCESS_TOKEN);
        if(StringUtils.isNotBlank(userAccessToken)){
            isLayout = redisRepository.delSession(userAccessToken);
        }
        return isLayout;
    }

    @Override
    public JsonResult doLogin(Map paramsMap) {
        String usercode = StringUtils.getMapKeyValue(paramsMap,"usercode");
        String password = StringUtils.getMapKeyValue(paramsMap,"password");
        if(StringUtils.isBlank(usercode)||StringUtils.isBlank(password)){
            return JsonResult.putFail(JsonConstans.ERR_WRONG_PARAM);
        }else{
            Map<String,Object> userMap = this.getUserMapByCode(usercode);
            if(userMap!=null){
                String userPassword = userMap.get("password").toString();
                password = MD5Util.encodeMD5(password);
                if(userPassword.equals(password)){
                    SysUserEntity sysUserEntity = this.findById(new SysUserEntity(),userMap.get("id").toString());
                    sysUserEntity.setUserToken(TokenConstants.generateToken());
                    this.update(sysUserEntity);
                    redisRepository.delSession(userMap.get("userToken").toString());
                    userMap.put("userToken",sysUserEntity.getUserToken());
                    userMap.remove("password");
                    redisRepository.setSession(userMap);
                    return JsonResult.putSuccess(userMap);
                }else{
                    return JsonResult.putFail(JsonConstans.ERR_USER_PASSORD_NOTEQUAL);
                }
            }else{
                return JsonResult.putFail(JsonConstans.ERR_USER_PASSORD_NOTEQUAL);
            }
        }
    }
}
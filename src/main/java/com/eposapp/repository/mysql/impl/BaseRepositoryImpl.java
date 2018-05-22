package com.eposapp.repository.mysql.impl;

import com.eposapp.common.constant.TokenConstants;
import com.eposapp.common.constant.SysConstants;
import com.eposapp.common.util.EntityUtil;
import com.eposapp.common.util.StringUtils;
import com.eposapp.entity.BaseEntity;
import com.eposapp.repository.mysql.BaseRepository;
import com.eposapp.threadlocal.SystemSession;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;


/**
 * @author eiven
 * @date 2018
 * Hibernate有三种状态：transient(瞬时状态)，persistent(持久化状态)以及detached(游离状态)。
 * */
@Repository
public abstract class BaseRepositoryImpl<T,ID extends Serializable> implements BaseRepository<T,ID> {
    private static final Logger logger = LoggerFactory.getLogger(BaseRepositoryImpl.class);
    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public boolean isExistCode(Class clazz,String code,String id) {
        String sql = "select count(1) from "+ EntityUtil.getSqlTableName(clazz) +" u where u.code =:code ";
        if(StringUtils.isNotBlank(id)){
            sql +="and u.id <>:id ";
        }
        Query nativeCountQuery = entityManager.createNativeQuery(sql);
        nativeCountQuery.setParameter("code",code);
        if(StringUtils.isNotBlank(id)){
            nativeCountQuery.setParameter("id",id);
        }
        List totalList = nativeCountQuery.getResultList();
        BigInteger total = (BigInteger) totalList.get(0);
        if(total.compareTo(BigInteger.ZERO)>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Map<String,Object> findByNativeQuery(String sql, Map<String,String> conditionParams,ResultTransformer resultTransformer , Integer pageNo, Integer pageSize) {
        String countSql = "select count(1) from ("+sql+") u";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        Query nativeCountQuery = entityManager.createNativeQuery(countSql);
        for (String paramKey:conditionParams.keySet()) {
            nativeCountQuery.setParameter(paramKey,conditionParams.get(paramKey));
            nativeQuery.setParameter(paramKey,conditionParams.get(paramKey));
        }
        List totalList = nativeCountQuery.getResultList();
        BigInteger total = (BigInteger) totalList.get(0);
        if(pageNo!=null){
            nativeQuery.setFirstResult((pageNo-1)*pageSize);
        }
        if(pageSize!=null){
            nativeQuery.setMaxResults(pageSize);
        }
        if (resultTransformer != null) {
            nativeQuery.unwrap(NativeQuery.class).setResultTransformer(resultTransformer);
        }
        List rows = nativeQuery.getResultList();
        Map<String,Object> resultMap = new HashMap<String,Object>(2);
        resultMap.put(SysConstants.ROWS,rows);
        resultMap.put(SysConstants.TOTAL,total);
        entityManager.close();
        return resultMap;
    }

    @Override
    public List<T> findAll(String hqlTableName) {
        String hql=" from "+hqlTableName+" u ";
        Query query=entityManager.createQuery(hql);
        List<T> list= query.getResultList();
        entityManager.close();
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(T entity){
        boolean flag=false;
        try {
            if (entity instanceof BaseEntity) {
                ((BaseEntity)entity).setId(TokenConstants.generateUserId());
                ((BaseEntity)entity).setCreateId(((BaseEntity)entity).getId());
                ((BaseEntity)entity).setCreateTime(new Timestamp(System.currentTimeMillis()));
                ((BaseEntity)entity).setVersion(0);
            }
            entityManager.persist(entity);
            flag=true;
        }catch (Exception e){
            throw e;
        }
        return flag;
    }
    @Transactional
    @Override
    public T findById(T entity,ID id) {

        Object obj = (T)entityManager.find(entity.getClass(),id);
        Session session = entityManager.unwrap(Session.class);
        session.clear();
        return (T) obj;
    }
    @Transactional
    @Override
    public List<T> findByHql(String hqlTableName, String filed, Object o ) {
        String sql="from "+hqlTableName+" u WHERE u."+filed+"=?";
        Query query=entityManager.createQuery(sql);
        query.setParameter(1,o);
        List<T> list= query.getResultList();
        entityManager.close();
        return list;
    }

    @Override
    public Object findObjiectByHql(String hqlTableName, String filed, Object o) {
        String sql="from "+hqlTableName+" u WHERE u."+filed+"=?";
        Query query=entityManager.createQuery(sql);
        query.setParameter(1,o);
        entityManager.close();
        return query.getSingleResult();
    }
    @Transactional
    @Override
    public List<T> findByMoreFiled(String hqlTableName,LinkedHashMap<String,Object> map) {
        String sql="from "+hqlTableName+" u WHERE ";
        Set<String> set=null;
        set=map.keySet();
        List<String> list=new ArrayList<>(set);
        List<Object> filedlist=new ArrayList<>();
        for (String filed:list){
            sql+="u."+filed+"=? and ";
            filedlist.add(filed);
        }
        sql=sql.substring(0,sql.length()-4);
        Query query=entityManager.createQuery(sql);
        for (int i=0;i<filedlist.size();i++){
            query.setParameter(i+1,map.get(filedlist.get(i)));
        }
        List<T> listRe= query.getResultList();
        entityManager.close();
        return listRe;
    }
    @Transactional
    @Override
    public List<T> findPages(String hqlTableName, String filed, Object o, int pageNo, int pageSize) {
        String sql="from "+hqlTableName+" u WHERE u."+filed+"=?";
        List<T> list=new ArrayList<>();
        try {
            Query query=entityManager.createQuery(sql);
            query.setParameter(1,o);
            query.setFirstResult((pageNo-1)*pageSize);
            query.setMaxResults(pageSize);
            list= query.getResultList();
            entityManager.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(T entity) {
        boolean flag = false;
        try {
            if (entity instanceof BaseEntity) {
                ((BaseEntity)entity).setUpdateId(SystemSession.getUserId());
                ((BaseEntity)entity).setUpdateTime(new Timestamp(System.currentTimeMillis()));
            }
            entityManager.merge(entity);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    @Transactional
    @Override
    public Integer updateMoreFiled(String hqlTableName, LinkedHashMap<String, Object> map) {
        String sql="UPDATE "+hqlTableName+" AS u SET ";
        Set<String> set=null;
        set=map.keySet();
        List<String> list=new ArrayList<>(set);
        for (int i=0;i<list.size()-1;i++){
            if (map.get(list.get(i)).getClass().getTypeName()=="java.lang.String"){
                sql+="u."+list.get(i)+"='"+map.get(list.get(i))+"' , ";
            }else {
                sql+="u."+list.get(i)+"="+map.get(list.get(i))+" , ";
            }
        }
        sql=sql.substring(0,sql.length()-2);
        sql+="where u.id=? ";
        int resurlt=0;
        try {
            Query query=entityManager.createQuery(sql);
            query.setParameter(1,map.get("id"));
            resurlt= query.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resurlt;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(T entity) {
        boolean flag=false;
        try {
            entityManager.remove(entityManager.merge(entity));
            flag=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delete(Class clazz, String id) {
        String hql = "delete from "+EntityUtil.getHqlTableName(clazz) +" u WHERE u.id = '"+id+"'";
        Query query=entityManager.createQuery(hql);
        if(query.executeUpdate()>0){
            return true;
        }else{
            return false;
        }
    }
}
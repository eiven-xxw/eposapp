package com.eposapp.repository.mysql;


import org.hibernate.transform.ResultTransformer;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface BaseRepository<T,ID extends Serializable> {

    /**
     * 检验表是否存在code , 保证code的唯一性
     * @author eiven
     * @param clazz 表
     * @param code 表字段代码
     * @param id 表字段id
     * @return true(存在) or false(不存在)
     * */
    boolean isExistCode(Class clazz,String code,String id);

    /**
     * 多字段查询分页
     * @param pageNo 第几页
     * @param pageSize 一个页面的条数
     * @param resultTransformer     Transformer.
     * @param conditionParams
     * @return
     */
    Map<String,Object> findByNativeQuery(String sql, Map<String,String> conditionParams, ResultTransformer resultTransformer, Integer pageNo, Integer pageSize);
    /**
     * 查询所有
     * */
    List<T> findAll(String hqlTableName);

    /**
     * 保存数据对象
     * @param entity
     * @return
     */
    boolean save(T entity);
    /**
     * 根据id查询
     * @param id
     * @param t
     * @return
     */
    T findById(T t, ID id);
    /**
     * 根据表名，字段，参数查询，拼接sql语句
     * @param  hqlTableName 表名
     * @param filed 字段名
     * @param o 字段参数
     * @return
     */
    List<T> findByHql(String hqlTableName, String filed, Object o);


    Object findObjiectByHql(String hqlTableName, String filed, Object o);

    /**
     * 多个字段的查询
     * @param hqlTableName 表名
     * @param map 将你的字段传入map中
     * @return
     */
    List<T> findByMoreFiled(String hqlTableName, LinkedHashMap<String, Object> map);

  /**
     * 一个字段的分页
     * @param  hqlTableName 表名
     * @param filed 字段名
     * @param o 字段参数
     * @param pageNo 第几页
     * @param pageSize 一个页面多少条数据
     * @return
     */
    List<T> findPages(String hqlTableName, String filed, Object o, int pageNo, int pageSize);
    /**
     * 只能删除持久状态的实体
     * @param  entity
     */
    boolean remove(T entity);
    /**
     * 根据表的id删除数据
     * @param  clazz
     */
    boolean delete(Class clazz,String id);
    /**
     * 更新对象
     * @param e
     * @return
     */
    boolean update(T e);
    /**
     * 根据传入的map遍历key,value拼接字符串，以id为条件更新
     * @param hqlTableName 表名
     * @param map 传入参数放入map中
     * @return
     */
    Integer updateMoreFiled(String hqlTableName, LinkedHashMap<String, Object> map);

}

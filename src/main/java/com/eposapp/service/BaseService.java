package com.eposapp.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface BaseService<T,ID extends Serializable> {

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
     * @param t
     * @param id
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
     * @param start 第几页
     * @param pageNumer 一个页面多少条数据
     * @return
     */
    List<T> findPages(String hqlTableName, String filed, Object o, int start, int pageNumer);
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

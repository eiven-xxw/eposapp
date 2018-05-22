package com.eposapp.service.impl;

import com.eposapp.repository.mysql.BaseRepository;
import com.eposapp.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author eiven
 * */
@Service("baseServiceImpl")
public abstract class BaseServiceImpl<T,ID extends Serializable> implements BaseService<T,ID> {


    @Autowired
    private BaseRepository<T,ID> baseRepository;

    @Override
    public List<T> findAll(String hqlTableName) {
        return baseRepository.findAll(hqlTableName);
    }

    @Override
    public boolean save(T entity) {
        return baseRepository.save(entity);
    }

    @Override
    public T findById(T t, ID id) {
        return baseRepository.findById(t,id);
    }

    @Override
    public List<T> findByHql(String hqlTableName, String filed, Object o) {
        return baseRepository.findByHql(hqlTableName,filed,o);
    }

    @Override
    public Object findObjiectByHql(String hqlTableName, String filed, Object o) {
        return baseRepository.findObjiectByHql(hqlTableName,filed,o);
    }

    @Override
    public List<T> findByMoreFiled(String hqlTableName, LinkedHashMap<String, Object> map) {
        return baseRepository.findByMoreFiled(hqlTableName,map);
    }

    @Override
    public List<T> findPages(String hqlTableName, String filed, Object o, int start, int pageNumer) {
        return baseRepository.findPages(hqlTableName,filed,o,start,pageNumer);
    }


    @Override
    public boolean update(T e) {
        return baseRepository.update(e);
    }

    @Override
    public Integer updateMoreFiled(String hqlTableName, LinkedHashMap<String, Object> map) {
        return baseRepository.updateMoreFiled(hqlTableName,map);
    }

    @Override
    public boolean remove(T entity) {
        return baseRepository.remove(entity);
    }

    @Override
    public boolean delete(Class clazz, String id) {
        return baseRepository.delete(clazz,id);
    }
}

package com.eisgroup.cb.dao;

import com.eisgroup.cb.model.BaseObject;

import java.io.Serializable;
import java.util.List;

public interface BaseObjectDAO<T extends BaseObject> extends Serializable {
    void save(T o);
    void delete(T o);
    T find(long id);
    List<T> getAll();
}
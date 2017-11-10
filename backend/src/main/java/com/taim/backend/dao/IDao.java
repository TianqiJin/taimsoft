package com.taim.backend.dao;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface IDao<T> {
    T save(T object);

    List<T> getAll();

    T findByID(Integer id);

    T updateObject(T object);

    T saveOrUpdateObject(T object);

    T findByName(String name);

    void deleteObject(T object);
}

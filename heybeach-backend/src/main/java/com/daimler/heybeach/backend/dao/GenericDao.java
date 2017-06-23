package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.data.core.QueryExecutor;
import com.daimler.heybeach.data.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericDao<K, V> {

    private Class<V> entityClass;

    @Autowired
    private QueryExecutor queryExecutor;

    public GenericDao() {
        this.entityClass = (Class<V>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public List<V> findAll() throws DaoException {
        try {
            return queryExecutor.findAll(entityClass);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public V findById(K key) throws DaoException {
        try {
            return queryExecutor.findById(key, entityClass);
        } catch (SQLException e) {
            throw new DaoException(e);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    public V save(V value) throws DaoException {
        try {
            return queryExecutor.save(value);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void remove(V value) throws DaoException {
        try {
            queryExecutor.remove(value);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

}

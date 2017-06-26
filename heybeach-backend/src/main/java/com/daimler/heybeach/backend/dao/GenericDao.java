package com.daimler.heybeach.backend.dao;

import com.daimler.heybeach.backend.exception.DaoException;
import com.daimler.heybeach.backend.exception.ValidationException;
import com.daimler.heybeach.data.core.Condition;
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

    public List<V> findAllWith(Condition... conditions) throws DaoException, ValidationException {
        try {
            return queryExecutor.findAll(entityClass, conditions);
        } catch (SQLException e) {
            return handleException(e);
        }
    }

    public List<V> findAll() throws DaoException, ValidationException {
        try {
            return queryExecutor.findAll(entityClass);
        } catch (SQLException e) {
            return handleException(e);
        }
    }

    public V findById(K key) throws DaoException, EntityNotFoundException, ValidationException {
        try {
            return queryExecutor.findById(key, entityClass);
        } catch (SQLException e) {
            return handleException(e);
        }
    }

    public V save(V value) throws DaoException, ValidationException {
        try {
            return queryExecutor.save(value);
        } catch (SQLException e) {
            return handleException(e);
        }
    }

    public void remove(V value) throws DaoException, ValidationException {
        try {
            queryExecutor.remove(value);
        } catch (SQLException e) {
            handleException(e);
        }
    }

    public Long count() throws DaoException, ValidationException {
        try {
            return queryExecutor.count(entityClass);
        } catch (SQLException e) {
            return handleException(e);
        }
    }

    public Long count(Condition... conditions) throws DaoException, ValidationException {
        try {
            return queryExecutor.count(entityClass, conditions);
        } catch (SQLException e) {
            return handleException(e);
        }
    }

    private <V> V handleException(SQLException e) throws DaoException, ValidationException {
        if (e.getErrorCode() != 0) {
            if (e.getErrorCode() == 1062) {
                throw new ValidationException("Already exists", e, e.getErrorCode());
            }
        }
        throw new DaoException(e);
    }
}

package com.daimler.heybeach.data.core;

import com.daimler.heybeach.data.exception.EntityNotFoundException;
import com.daimler.heybeach.data.exception.PersistenceException;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QueryExecutor {

    private final DataSource dataSource;
    private final PersistenceContext context;

    public QueryExecutor(final DataSource dataSource, final PersistenceContext context) {
        this.dataSource = dataSource;
        this.context = context;
    }

    public <T> T findById(Object id, Class<T> entityClass) throws SQLException, EntityNotFoundException {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(context.getEntityQueriesMap().get(entityClass).getSelectByIdQuery())) {
            preparedStatement.setObject(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            EntityDescription description = context.getEntityDescriptionMap().get(entityClass);
            List<T> result = new SimpleMapper<>(entityClass, description.getFieldMapping()).map(rs);
            rs.close();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Entity not found with id : " + id);
            }
            return result.iterator().next();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    public <T> List<T> findAll(Class<T> entityClass) throws SQLException {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(context.getEntityQueriesMap().get(entityClass).getSelectAllQuery())) {
            ResultSet rs = preparedStatement.executeQuery();
            EntityDescription description = context.getEntityDescriptionMap().get(entityClass);
            List<T> result = new SimpleMapper<>(entityClass, description.getFieldMapping()).map(rs);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    public <T> T save(T entity) throws SQLException {
        try {
            EntityDescription description = context.getEntityDescriptionMap().get(entity.getClass());
            Field idField = description.getFieldMapping().get(description.getIdField().getName());
            if (idField.get(entity) != null) {
                entity = update(entity);
            } else {
                entity = create(entity);
            }
            return entity;
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    private <T> T create(T entity) throws SQLException {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                context.getEntityQueriesMap().get(entity.getClass()).getCreateQuery(),
                Statement.RETURN_GENERATED_KEYS
        )) {
            EntityDescription description = context.getEntityDescriptionMap().get(entity.getClass());
            Map<String, Field> mapping = description.getFieldMapping();
            int index = 1;
            boolean generateId = description.getIdField().isGenerate();
            Object id = null;
            for (Map.Entry<String, Field> entry : mapping.entrySet()) {
                if (description.getIdField().getName().equals(entry.getKey())) {
                    if (generateId) {
                        id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
                        preparedStatement.setObject(index, id);
                    } else {
                        continue;
                    }
                } else {
                    preparedStatement.setObject(index, entry.getValue().get(entity));
                }
                index++;
            }
            int res = preparedStatement.executeUpdate();
            if (!generateId) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                while (rs.next()) {
                    id = rs.getObject(1);
                }
            }
            Field idField = mapping.get(description.getIdField().getName());
            idField.set(entity, id);
            return entity;
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    private <T> T update(T entity) throws SQLException {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(
                context.getEntityQueriesMap().get(entity.getClass()).getUpdateQuery()
        )) {
            EntityDescription description = context.getEntityDescriptionMap().get(entity.getClass());
            Map<String, Field> mapping = description.getFieldMapping();
            int index = 1;
            for (Map.Entry<String, Field> entry : mapping.entrySet()) {
                if (description.getIdField().getName().equals(entry.getKey())) {
                    continue;
                } else {
                    preparedStatement.setObject(index, entry.getValue().get(entity));
                }
                index++;
            }

            Field idField = mapping.get(description.getIdField().getName());
            preparedStatement.setObject(index, idField.get(entity));

            int res = preparedStatement.executeUpdate();
            return entity;
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    public <T> void remove(T entity) throws SQLException {
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(context.getEntityQueriesMap().get(entity.getClass()).getRemoveQuery())) {
            EntityDescription description = context.getEntityDescriptionMap().get(entity.getClass());
            Field idField = description.getFieldMapping().get(description.getIdField().getName());
            preparedStatement.setObject(1, idField.get(entity));
            preparedStatement.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

}

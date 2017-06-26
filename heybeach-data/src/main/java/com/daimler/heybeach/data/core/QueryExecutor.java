package com.daimler.heybeach.data.core;

import com.daimler.heybeach.data.exception.EntityNotFoundException;
import com.daimler.heybeach.data.exception.PersistenceException;
import com.google.common.collect.Lists;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
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

    public <T> List<T> findAll(Class<T> entityClass, Condition... conditions) throws SQLException {

        String selectAll = context.getEntityQueriesMap().get(entityClass).getSelectAllQuery();
        String queryWithConditions = new QueryBuilder().addConditions(selectAll, conditions);

        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(queryWithConditions)) {
            int index = 1;
            for (Condition condition : conditions) {
                preparedStatement.setObject(index, condition.getValue());
                index++;
            }
            ResultSet rs = preparedStatement.executeQuery();
            EntityDescription description = context.getEntityDescriptionMap().get(entityClass);
            List<T> result = new SimpleMapper<>(entityClass, description.getFieldMapping()).map(rs);
            return result;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }


    public <T> T findById(Object id, Class<T> entityClass) throws SQLException, EntityNotFoundException {

        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(context.getEntityQueriesMap().get(entityClass).getSelectByIdQuery())) {
            EntityDescription description = context.getEntityDescriptionMap().get(entityClass);

            List<EntityDescription.IdField> idFields = description.getPk();
            if (idFields.size() > 1) {
                int index = 1;
                for (EntityDescription.IdField idField : idFields) {
                    Field field = description.getFieldMapping().get(idField.getName());
                    preparedStatement.setObject(index, field.get(id));
                    index++;
                }
            } else {
                preparedStatement.setObject(1, id);
            }

            ResultSet rs = preparedStatement.executeQuery();
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
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(context.getEntityQueriesMap().get(entityClass).getSelectAllQuery())) {
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
            if (description.getPk().size() == 1) {
                Field idField = description.getFieldMapping().get(description.getIdFields().keySet().iterator().next());
                if (idField.get(entity) != null) {
                    return update(entity);
                }
            }
            return create(entity);
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    private <T> T create(T entity) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     context.getEntityQueriesMap().get(entity.getClass()).getCreateQuery(),
                     Statement.RETURN_GENERATED_KEYS
             )) {
            EntityDescription description = context.getEntityDescriptionMap().get(entity.getClass());
            Map<String, Field> mapping = description.getFieldMapping();
            int index = 1;
            boolean generateId = false;

            Object id = null;
            for (Map.Entry<String, Field> entry : mapping.entrySet()) {
                Object insertValue = null;
                if (description.getIdFields().containsKey(entry.getKey())) {
                    EntityDescription.IdField idField = description.getIdFields().get(entry.getKey());
                    if (!idField.isFk()) {
                        if (idField.isGenerate()) {
                            insertValue = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
                            id = insertValue;
                        } else {
                            continue;
                        }
                    }
                }
                preparedStatement.setObject(index, insertValue == null ? entry.getValue().get(entity) : insertValue);
                index++;
            }
            int res = preparedStatement.executeUpdate();
            //Id is generated by DB
            if (!generateId) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                while (rs.next()) {
                    id = rs.getObject(1);
                }
                rs.close();
            }

            for (Map.Entry<String, EntityDescription.IdField> idFieldEntry : description.getIdFields().entrySet()) {
                if (!idFieldEntry.getValue().isFk()) {
                    Field idField = mapping.get(idFieldEntry.getKey());
                    idField.set(entity, id);
                }
            }

            return entity;
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    private <T> T update(T entity) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(
                     context.getEntityQueriesMap().get(entity.getClass()).getUpdateQuery()
             )) {
            EntityDescription description = context.getEntityDescriptionMap().get(entity.getClass());
            Map<String, Field> mapping = description.getFieldMapping();
            int index = 1;
            for (Map.Entry<String, Field> entry : mapping.entrySet()) {
                if (description.getIdFields().containsKey(entry.getKey())) {
                    continue;
                } else {
                    preparedStatement.setObject(index, entry.getValue().get(entity));
                    index++;
                }
            }

            List<EntityDescription.IdField> idFields = description.getPk();
            for (EntityDescription.IdField idField : idFields) {
                Field field = description.getFieldMapping().get(idField.getName());
                preparedStatement.setObject(index, field.get(entity));
                index++;
            }

            int res = preparedStatement.executeUpdate();
            return entity;
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    public <T> void remove(T entity) throws SQLException {
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(context.getEntityQueriesMap().get(entity.getClass()).getRemoveQuery())) {
            EntityDescription description = context.getEntityDescriptionMap().get(entity.getClass());

            int index = 1;
            List<EntityDescription.IdField> idFields = description.getPk();
            for (EntityDescription.IdField idField : idFields) {
                Field field = description.getFieldMapping().get(idField.getName());
                preparedStatement.setObject(index, field.get(entity));
                index++;
            }
            preparedStatement.executeUpdate();
        } catch (IllegalAccessException e) {
            throw new PersistenceException(e.getMessage(), e);
        }
    }

    public Long count(Class entityClass, Condition... conditions) throws SQLException {
        String countQuery = context.getEntityQueriesMap().get(entityClass).getCountQuery();
        String queryWithConditions = new QueryBuilder().addConditions(countQuery, conditions);
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(queryWithConditions)) {

            int index = 1;
            for (Condition condition : conditions) {
                preparedStatement.setObject(index, condition.getValue());
                index++;
            }
            ResultSet rs = preparedStatement.executeQuery();
            List<Long> count = new RowMapper<Long>() {
                @Override
                public List<Long> map(ResultSet rs) throws SQLException {
                    rs.next();
                    return Lists.newArrayList(rs.getLong("COUNT"));
                }
            }.map(rs);
            return count.iterator().next();
        }
    }

    public Long count(Class entityClass) throws SQLException {
        String countQuery = context.getEntityQueriesMap().get(entityClass).getCountQuery();
        try (Connection con = dataSource.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(countQuery)) {

            ResultSet rs = preparedStatement.executeQuery();
            List<Long> count = new RowMapper<Long>() {
                @Override
                public List<Long> map(ResultSet rs) throws SQLException {
                    return Lists.newArrayList(rs.getLong("COUNT"));
                }
            }.map(rs);
            return count.iterator().next();
        }
    }

}

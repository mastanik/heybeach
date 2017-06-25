package com.daimler.heybeach.data.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class SimpleMapper<T> implements RowMapper<T> {

    private Class<T> clazz;
    private Map<String, Field> fieldMapping;

    SimpleMapper(Class<T> clazz, Map<String, Field> fieldMapping) {
        this.clazz = clazz;
        this.fieldMapping = fieldMapping;
    }

    @Override
    public List<T> map(ResultSet rs) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> ctor = clazz.getConstructor();
        List<T> result = new LinkedList<>();
        while (rs.next()) {
            T entity = ctor.newInstance();
            for (Map.Entry<String, Field> mapping : fieldMapping.entrySet()) {
                Field field = mapping.getValue();
                Object o = rs.getObject(mapping.getKey(), field.getType());
                field.set(entity, o);
            }
            result.add(entity);
        }
        return result;
    }
}

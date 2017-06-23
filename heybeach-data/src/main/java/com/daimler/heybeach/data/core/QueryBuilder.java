package com.daimler.heybeach.data.core;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

class QueryBuilder {

    public static final String SELECT = "SELECT";
    public static final String INSERT = "INSERT INTO";
    public static final String VALUES = "VALUES";
    public static final String UPDATE = "UPDATE";
    public static final String SET = "SET";
    public static final String DELETE = "DELETE";
    public static final String FROM = "FROM";
    public static final String WHERE = "WHERE";

    public static final String EMPTY = " ";
    public static final String EQUAL = " = ";
    public static final String PARAM = "?";
    public static final String COMMA_WITH_SPACE = ", ";
    public static final String COMMA_WITH_SPACE_AND_QUOTES = "', '";
    public static final String OPEN_PARENTHESIS = "(";
    public static final String CLOSE_PARENTHESIS = ")";
    public static final String QUOTE = "'";

    String buildSelectAll(EntityDescription entityDescription) {
        StringBuilder sb = new StringBuilder();
        Map<String, Field> mapping = entityDescription.getFieldMapping();

        sb.append(SELECT)
                .append(EMPTY);
        StringJoiner sj = new StringJoiner(COMMA_WITH_SPACE);
        mapping.keySet().forEach(sj::add);
        sb.append(sj.toString())
                .append(EMPTY)
                .append(FROM)
                .append(EMPTY)
                .append(entityDescription.getTable());

        return sb.toString();
    }

    String buildSelectById(EntityDescription entityDescription) {
        String select = buildSelectAll(entityDescription);
        StringBuilder sb = new StringBuilder(select)
                .append(EMPTY)
                .append(WHERE)
                .append(EMPTY)
                .append(entityDescription.getIdField().getName())
                .append(EMPTY)
                .append(EQUAL)
                .append(PARAM);
        return sb.toString();
    }

    String buildCreate(EntityDescription entityDescription) {
        StringBuilder sb = new StringBuilder();
        Map<String, Field> mapping = entityDescription.getFieldMapping();
        sb.append(INSERT)
                .append(EMPTY)
                .append(entityDescription.getTable())
                .append(EMPTY)
                .append(OPEN_PARENTHESIS);
        StringJoiner sj = new StringJoiner(COMMA_WITH_SPACE);

        boolean generateId = entityDescription.getIdField().isGenerate();

        mapping.keySet().stream().filter(key -> !entityDescription.getIdField().getName().equals(key) || generateId).forEach(sj::add);

        sb.append(sj.toString())
                .append(CLOSE_PARENTHESIS)
                .append(EMPTY)
                .append(VALUES)
                .append(OPEN_PARENTHESIS);
        StringJoiner sj2 = new StringJoiner(COMMA_WITH_SPACE);
        mapping.keySet().stream().filter(key -> !entityDescription.getIdField().getName().equals(key) || generateId).forEach(key -> sj2.add(PARAM));
        sb.append(sj2.toString())
                .append(CLOSE_PARENTHESIS);
        return sb.toString();
    }

    String buildUpdate(EntityDescription entityDescription) {
        StringBuilder sb = new StringBuilder();
        Map<String, Field> mapping = entityDescription.getFieldMapping();

        sb.append(UPDATE)
                .append(EMPTY)
                .append(entityDescription.getTable())
                .append(EMPTY)
                .append(SET)
                .append(EMPTY);

        Iterator<Map.Entry<String, Field>> it = mapping.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Field> entry = it.next();
            if (entry.getKey().equals(entityDescription.getIdField().getName())) {
                continue;
            }
            sb.append(entry.getKey())
                    .append(EQUAL)
                    .append(PARAM);
            if (it.hasNext()) {
                sb.append(COMMA_WITH_SPACE);
            }
        }

        sb.append(EMPTY)
                .append(WHERE)
                .append(EMPTY)
                .append(entityDescription.getIdField().getName())
                .append(EQUAL)
                .append(PARAM);
        return sb.toString();
    }

    String buildRemove(EntityDescription entityDescription) {
        StringBuilder sb = new StringBuilder();
        sb.append(DELETE)
                .append(EMPTY)
                .append(FROM)
                .append(EMPTY)
                .append(entityDescription.getTable())
                .append(EMPTY)
                .append(WHERE)
                .append(EMPTY)
                .append(entityDescription.getIdField().getName())
                .append(EQUAL)
                .append(PARAM);
        return sb.toString();
    }

}

package com.daimler.heybeach.data.core;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
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
    public static final String AND = "AND";

    public static final String EMPTY = " ";
    public static final String EQUAL = " = ";
    public static final String PARAM = "?";
    public static final String COMMA_WITH_SPACE = ", ";
    public static final String COMMA_WITH_SPACE_AND_QUOTES = "', '";
    public static final String OPEN_PARENTHESIS = "(";
    public static final String CLOSE_PARENTHESIS = ")";
    public static final String QUOTE = "'";

    public static final String COUNT = "SELECT COUNT(1) AS COUNT FROM ";

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
                .append(EMPTY);

        addWhereById(sb, entityDescription);

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
        Map<String, EntityDescription.IdField> idFields = entityDescription.getIdFields();
        mapping.keySet().stream().filter(key -> !idFields.containsKey(key) || idFields.get(key).isGenerate() || idFields.get(key).isFk()).forEach(sj::add);


        sb.append(sj.toString())
                .append(CLOSE_PARENTHESIS)
                .append(EMPTY)
                .append(VALUES)
                .append(OPEN_PARENTHESIS);
        StringJoiner sj2 = new StringJoiner(COMMA_WITH_SPACE);
        mapping.keySet().stream().filter(key -> !idFields.containsKey(key) || idFields.get(key).isGenerate() || idFields.get(key).isFk()).forEach(key -> sj2.add(PARAM));
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
            if (entityDescription.getIdFields().containsKey(entry.getKey())) {
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
                .append(EMPTY);

        addWhereById(sb, entityDescription);

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
                .append(EMPTY);

        addWhereById(sb, entityDescription);

        return sb.toString();
    }

    String buildCount(EntityDescription entityDescription) {
        StringBuilder sb = new StringBuilder(COUNT)
                .append(entityDescription.getTable());
        return sb.toString();
    }

    private void addWhereById(StringBuilder sb, EntityDescription entityDescription) {
        List<EntityDescription.IdField> idFields = entityDescription.getPk();
        int index = 1;
        int size = idFields.size();
        for (EntityDescription.IdField idField : idFields) {
            sb.append(idField.getName())
                    .append(EMPTY)
                    .append(EQUAL)
                    .append(PARAM);
            if (index < size) {
                sb.append(EMPTY)
                        .append(AND)
                        .append(EMPTY);
            }
            index++;
        }
    }

    String addConditions(String sql, Condition... conditions) {
        if (conditions == null) {
            return sql;
        }
        StringBuilder sb = new StringBuilder(sql);
        sb.append(EMPTY)
                .append(WHERE);

        int length = conditions.length;
        int index = 1;
        for (Condition condition : conditions) {
            if (index > 1) {
                sb.append(AND);
            }
            sb.append(EMPTY)
                    .append(condition.getFieldName())
                    .append(EMPTY)
                    .append(condition.getComparator())
                    .append(EMPTY)
                    .append(PARAM);
            if (index < length) {
                sb.append(EMPTY);
            }
            index++;
        }
        return sb.toString();
    }

}

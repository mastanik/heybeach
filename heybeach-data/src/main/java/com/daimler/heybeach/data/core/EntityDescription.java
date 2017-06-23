package com.daimler.heybeach.data.core;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

final class EntityDescription {

    private String table;
    private IdField idField;
    private LinkedHashMap<String, Field> fieldMapping;

    private EntityDescription(String table, String idName, boolean generateId, LinkedHashMap<String, Field> fieldMapping) {
        this.table = table;
        this.idField = new IdField(idName, generateId);
        this.fieldMapping = fieldMapping;
    }

    String getTable() {
        return table;
    }

    IdField getIdField() {
        return idField;
    }

    Map<String, Field> getFieldMapping() {
        return Collections.unmodifiableMap(fieldMapping);
    }

    class IdField {
        private String name;
        private boolean generate;

        private IdField(String name, boolean generate) {
            this.name = name;
            this.generate = generate;
        }

        public String getName() {
            return name;
        }

        public boolean isGenerate() {
            return generate;
        }
    }

    static class EntityDescriptionBuilder {
        private String table;
        private String idNname;
        private boolean generateId;
        private LinkedHashMap<String, Field> fieldMapping;

        EntityDescriptionBuilder() {
            this.fieldMapping = new LinkedHashMap<>();
        }

        EntityDescriptionBuilder table(String table) {
            this.table = table;
            return this;
        }

        EntityDescriptionBuilder idField(String idField) {
            this.idNname = idField;
            return this;
        }

        EntityDescriptionBuilder generateId(boolean generateId) {
            this.generateId = generateId;
            return this;
        }

        EntityDescriptionBuilder fieldMapping(String column, Field field) {
            this.fieldMapping.put(column, field);
            return this;
        }

        EntityDescription build() {
            EntityDescription description = new EntityDescription(table, idNname, generateId, fieldMapping);
            return description;
        }

    }

}

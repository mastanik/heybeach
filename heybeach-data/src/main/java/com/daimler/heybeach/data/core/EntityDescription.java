package com.daimler.heybeach.data.core;

import com.daimler.heybeach.data.exception.PersistenceException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class EntityDescription {

    private String table;
    private LinkedHashMap<String, IdField> idFields;
    private LinkedHashMap<String, Field> fieldMapping;
    private ArrayList<IdField> pks;

    private EntityDescription(String table, LinkedHashMap<String, IdField> idFields, LinkedHashMap<String, Field> fieldMapping, ArrayList<IdField> pks) {
        this.table = table;
        this.idFields = idFields;
        this.fieldMapping = fieldMapping;
        this.pks = pks;
    }

    String getTable() {
        return table;
    }

    Map<String, IdField> getIdFields() {
        return idFields;
    }

    List<IdField> getPk() {
        return pks;
    }

    Map<String, Field> getFieldMapping() {
        return Collections.unmodifiableMap(fieldMapping);
    }

    static class IdField {
        private String name;
        private boolean generate;
        private boolean fk;

        private IdField(String name, boolean generate, boolean fk) {
            this.name = name;
            this.generate = generate;
            this.fk = fk;
        }

        public String getName() {
            return name;
        }

        public boolean isGenerate() {
            return generate;
        }

        public boolean isFk() {
            return fk;
        }
    }

    static class EntityDescriptionBuilder {
        private String table;
        private LinkedHashMap<String, IdField> idFields;
        private LinkedHashMap<String, Field> fieldMapping;

        EntityDescriptionBuilder() {
            this.fieldMapping = new LinkedHashMap<>();
            this.idFields = new LinkedHashMap<>();
        }

        EntityDescriptionBuilder table(String table) {
            this.table = table;
            return this;
        }

        EntityDescriptionBuilder idField(String idFieldName, boolean generateId, boolean isFk) {
            IdField idField = new IdField(idFieldName, generateId, isFk);
            idFields.put(idFieldName, idField);
            return this;
        }

        EntityDescriptionBuilder fieldMapping(String column, Field field) {
            this.fieldMapping.put(column, field);
            return this;
        }

        EntityDescription build() {
            ArrayList<IdField> result;
            ArrayList<IdField> fk = new ArrayList<>();
            ArrayList<IdField> pk = new ArrayList<>();
            for (Map.Entry<String, IdField> entry : idFields.entrySet()) {
                if (entry.getValue().fk) {
                    fk.add(entry.getValue());
                } else {
                    pk.add(entry.getValue());
                }
            }

            if (pk.size() > 1) {
                throw new PersistenceException("Multiple PKs are not supported");
            } else if (!pk.isEmpty()) {
                result = pk;
            } else if (fk.size() > 1) {
                result = fk;
            } else {
                throw new PersistenceException("Entity must have either PK or more then 1 FK");
            }
            EntityDescription description = new EntityDescription(table, idFields, fieldMapping, result);
            return description;
        }

    }

}

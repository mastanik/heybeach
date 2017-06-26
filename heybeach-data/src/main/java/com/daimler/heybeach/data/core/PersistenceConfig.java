package com.daimler.heybeach.data.core;

import com.daimler.heybeach.data.types.Entity;
import com.daimler.heybeach.data.types.Field;
import com.daimler.heybeach.data.types.Id;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PersistenceConfig {

    private String entityPackage;

    public PersistenceConfig(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public PersistenceContext createPersistenceContext() {
        Reflections reflections = new Reflections(entityPackage);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);

        Map<Class, EntityDescription> entityDescriptionMap = parseEntities(annotated);
        Map<Class, EntityQueries> entityQueriesMap = prepareQueries(entityDescriptionMap);
        return new PersistenceContext(entityDescriptionMap, entityQueriesMap);
    }

    /**
     * Prepare basic CRUD queries for faster access.
     *
     * @param entityDescriptionMap
     * @return
     */
    Map<Class, EntityQueries> prepareQueries(Map<Class, EntityDescription> entityDescriptionMap) {

        Map<Class, EntityQueries> entityQueriesMap = new HashMap<>(entityDescriptionMap.size(), 1);

        QueryBuilder queryBuilder = new QueryBuilder();
        for (Map.Entry<Class, EntityDescription> entry : entityDescriptionMap.entrySet()) {
            Class clazz = entry.getKey();
            EntityDescription description = entry.getValue();

            EntityQueries.EntityQueriesBuilder builder = new EntityQueries.EntityQueriesBuilder();
            builder
                    .selectAllQuery(queryBuilder.buildSelectAll(description))
                    .selectByIdQuery(queryBuilder.buildSelectById(description))
                    .createQuery(queryBuilder.buildCreate(description))
                    .updateQuery(queryBuilder.buildUpdate(description))
                    .removeQuery(queryBuilder.buildRemove(description))
                    .countQuery(queryBuilder.buildCount(description));

            entityQueriesMap.put(clazz, builder.build());
        }
        return entityQueriesMap;
    }


    /**
     * Parse classes with @Entity annotation and build description objects for further use.
     *
     * @param annotated
     * @return
     */
    Map<Class, EntityDescription> parseEntities(Set<Class<?>> annotated) {
        Map<Class, EntityDescription> entityDescriptionMap = new HashMap<>();

        for (Class<?> clazz : annotated) {
            System.out.println(clazz);

            EntityDescription.EntityDescriptionBuilder descriptionBuilder = new EntityDescription.EntityDescriptionBuilder();

            Entity entityAnnotation = clazz.getAnnotation(Entity.class);
            String table = entityAnnotation.table().toUpperCase();
            if ("".equals(table)) {
                table = clazz.getSimpleName().toUpperCase();
            }
            descriptionBuilder.table(table);

            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                String fieldName;
                Field fieldAnnotation = field.getAnnotation(Field.class);
                if (fieldAnnotation != null && !"".equals(fieldAnnotation.column())) {
                    fieldName = fieldAnnotation.column().toUpperCase();
                } else {
                    fieldName = field.getName().toUpperCase();
                }
                descriptionBuilder.fieldMapping(fieldName, field);

                Id idAnnotation = field.getAnnotation(Id.class);
                if (idAnnotation != null) {
                    boolean generate = idAnnotation.generated();
                    boolean fk = idAnnotation.fk();
                    descriptionBuilder.idField(fieldName, generate, fk);
                }
            }
            EntityDescription entityDescription = descriptionBuilder.build();
            entityDescriptionMap.put(clazz, entityDescription);
        }
        return entityDescriptionMap;
    }

}

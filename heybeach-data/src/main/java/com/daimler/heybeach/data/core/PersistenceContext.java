package com.daimler.heybeach.data.core;

import java.util.Map;

class PersistenceContext {

    private Map<Class, EntityDescription> entityDescriptionMap;
    private Map<Class, EntityQueries> entityQueriesMap;

    PersistenceContext(Map<Class, EntityDescription> entityDescriptionMap, Map<Class, EntityQueries> entityQueriesMap) {
        this.entityDescriptionMap = entityDescriptionMap;
        this.entityQueriesMap = entityQueriesMap;
    }

    Map<Class, EntityDescription> getEntityDescriptionMap() {
        return entityDescriptionMap;
    }

    Map<Class, EntityQueries> getEntityQueriesMap() {
        return entityQueriesMap;
    }
}

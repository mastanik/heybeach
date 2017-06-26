package com.daimler.heybeach.data.core;

final class EntityQueries {
    private String selectAllQuery;
    private String selectByIdQuery;
    private String createQuery;
    private String updateQuery;
    private String removeQuery;
    private String countQuery;

    private EntityQueries(String selectAllQuery, String selectByIdQuery, String createQuery, String updateQuery, String removeQuery, String countQuery) {
        this.selectAllQuery = selectAllQuery;
        this.selectByIdQuery = selectByIdQuery;
        this.createQuery = createQuery;
        this.updateQuery = updateQuery;
        this.removeQuery = removeQuery;
        this.countQuery = countQuery;
    }

    public String getSelectAllQuery() {
        return selectAllQuery;
    }

    public String getSelectByIdQuery() {
        return selectByIdQuery;
    }

    public String getCreateQuery() {
        return createQuery;
    }

    public String getUpdateQuery() {
        return updateQuery;
    }

    public String getRemoveQuery() {
        return removeQuery;
    }

    public String getCountQuery() {
        return countQuery;
    }

    static class EntityQueriesBuilder {
        private String selectAllQuery;
        private String selectByIdQuery;
        private String createQuery;
        private String updateQuery;
        private String removeQuery;
        private String countQuery;

        EntityQueriesBuilder selectAllQuery(String query) {
            this.selectAllQuery = query;
            return this;
        }

        EntityQueriesBuilder selectByIdQuery(String query) {
            this.selectByIdQuery = query;
            return this;
        }

        EntityQueriesBuilder createQuery(String query) {
            this.createQuery = query;
            return this;
        }

        EntityQueriesBuilder updateQuery(String query) {
            this.updateQuery = query;
            return this;
        }

        EntityQueriesBuilder removeQuery(String query) {
            this.removeQuery = query;
            return this;
        }

        EntityQueriesBuilder countQuery(String query) {
            this.countQuery = query;
            return this;
        }

        EntityQueries build() {
            EntityQueries queries = new EntityQueries(selectAllQuery, selectByIdQuery, createQuery, updateQuery, removeQuery, countQuery);
            return queries;
        }
    }
}

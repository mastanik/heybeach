package com.daimler.heybeach.data.core;

public class Condition {
    private String fieldName;
    private String comparator;
    private Object value;

    public static String EQ() {
        return "=";
    }

    public static String GT() {
        return ">";
    }

    public static String LT() {
        return "<";
    }

    private Condition(String fieldName, String comparator, Object value) {
        this.fieldName = fieldName;
        this.comparator = comparator;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getComparator() {
        return comparator;
    }

    public Object getValue() {
        return value;
    }

    public static class ConditionBuilder {
        private String fieldName;
        private String comparator;
        private Object value;

        public ConditionBuilder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public ConditionBuilder comparator(String comparator) {
            this.comparator = comparator;
            return this;
        }

        public ConditionBuilder value(Object value) {
            this.value = value;
            return this;
        }

        public Condition build() {
            return new Condition(fieldName, comparator, value);
        }
    }


}

package com.biza.exception;

public class DomainExceptions {

    public static class Duplicate extends RuntimeException {
        private final String field;
        private final String value;
        public Duplicate(String field, String value, String message) {
            super(message);
            this.field = field;
            this.value = value;
        }
        public String getField() { return field; }
        public String getValue() { return value; }
    }

    public static class NotFound extends RuntimeException {
        public NotFound(String message) { super(message); }
    }

    public static class Conflict extends RuntimeException {
        public Conflict(String message) { super(message); }
    }

    public static class Unprocessable extends RuntimeException {
        public Unprocessable(String message) { super(message); }
    }

    public static class BadRequest extends RuntimeException {
        public BadRequest(String message) { super(message); }
    }
}
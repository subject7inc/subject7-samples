package com.subject7.samples.reporting.domain;

public enum  ExecutionType {
    ALL("All"),
    LOCAL("Local"),
    PORTAL("Portal"),
    SCHEDULED("Scheduled"),
    WEB_SERVICE("Web Service");

    private String caption;

    private ExecutionType(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }
}

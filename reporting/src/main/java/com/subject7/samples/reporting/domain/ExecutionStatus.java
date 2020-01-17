package com.subject7.samples.reporting.domain;

public enum ExecutionStatus {
    IN_EXECUTION("In execution"),
    PASS("Pass"),
    SKIP("Skip"),
    FAIL("Fail"),
    FAIL_SKIP("Fail skip"),
    ERROR("Error"),
    PENDING("Pending"),
    CANCELLED("Cancelled"),
    FORBIDDEN("Forbidden"),
    NA("Not available");

    private String caption;

    private ExecutionStatus(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }
}

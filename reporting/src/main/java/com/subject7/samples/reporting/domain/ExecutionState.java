package com.subject7.samples.reporting.domain;

public enum  ExecutionState {
    NONE("None"),
    CREATED("Created"),
    QUEUED("Queued"),
    STARTED("Started"),
    IN_EXECUTION("In Execution"),
    GATEWAY_DENIED("Gateway denied"),
    VM_ACCEPTED("Vm accepted"),
    COMPLETED("Completed"),
    REPORT_REJECTED("Report rejected"),
    PENDING("Pending"),
    RUNNING("Running"),
    RE_RUN("Rerun"),
    ARCHIVED("Archived"),
    CANCELLED("Cancelled"),
    ERROR("Error"),
    SYNCHRONIZING("Synchronizing");

    private String caption;

    private ExecutionState(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }
}

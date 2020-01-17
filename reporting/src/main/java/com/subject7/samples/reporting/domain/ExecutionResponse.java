package com.subject7.samples.reporting.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExecutionResponse {
    @JsonProperty("id")
    private Long id = null;
    @JsonProperty("executionSetId")
    private Long executionSetId = null;
    @JsonProperty("executionSetName")
    private String executionSetName = null;
    @JsonProperty("requested")
    private OffsetDateTime requested = null;
    @JsonProperty("executionState")
    private ExecutionState executionState = null;
    @JsonProperty("pool")
    private String pool = null;
    @JsonProperty("executionType")
    private ExecutionType executionType = null;
    @JsonProperty("executionStatus")
    private ExecutionStatus executionStatus = null;
    @JsonProperty("result")
    private String result = null;
    @JsonProperty("browserTypes")
    private List<BrowserType> browserTypes = null;
    @JsonProperty("build")
    private String build = null;
    @JsonProperty("requester")
    private String requester = null;
    @JsonProperty("speed")
    private Integer speed = null;

    public ExecutionResponse() {
    }

    public ExecutionResponse id(Long id) {
        this.id = id;
        return this;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExecutionResponse executionSetId(Long executionSetId) {
        this.executionSetId = executionSetId;
        return this;
    }

    public Long getExecutionSetId() {
        return this.executionSetId;
    }

    public void setExecutionSetId(Long executionSetId) {
        this.executionSetId = executionSetId;
    }

    public ExecutionResponse executionSetName(String executionSetName) {
        this.executionSetName = executionSetName;
        return this;
    }

    public String getExecutionSetName() {
        return this.executionSetName;
    }

    public void setExecutionSetName(String executionSetName) {
        this.executionSetName = executionSetName;
    }

    public ExecutionResponse requested(OffsetDateTime requested) {
        this.requested = requested;
        return this;
    }

    public OffsetDateTime getRequested() {
        return this.requested;
    }

    public void setRequested(OffsetDateTime requested) {
        this.requested = requested;
    }

    public ExecutionResponse executionState(ExecutionState executionState) {
        this.executionState = executionState;
        return this;
    }

    public ExecutionState getExecutionState() {
        return this.executionState;
    }

    public void setExecutionState(ExecutionState executionState) {
        this.executionState = executionState;
    }

    public ExecutionResponse pool(String pool) {
        this.pool = pool;
        return this;
    }

    public String getPool() {
        return this.pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public ExecutionResponse executionType(ExecutionType executionType) {
        this.executionType = executionType;
        return this;
    }

    public ExecutionType getExecutionType() {
        return this.executionType;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }

    public ExecutionResponse executionStatus(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
        return this;
    }

    public ExecutionStatus getExecutionStatus() {
        return this.executionStatus;
    }

    public void setExecutionStatus(ExecutionStatus executionStatus) {
        this.executionStatus = executionStatus;
    }

    public ExecutionResponse result(String result) {
        this.result = result;
        return this;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ExecutionResponse browserTypes(List<BrowserType> browserTypes) {
        this.browserTypes = browserTypes;
        return this;
    }

    public ExecutionResponse addBrowserTypesItem(BrowserType browserTypesItem) {
        if (this.browserTypes == null) {
            this.browserTypes = new ArrayList();
        }

        this.browserTypes.add(browserTypesItem);
        return this;
    }

    public List<BrowserType> getBrowserTypes() {
        return this.browserTypes;
    }

    public void setBrowserTypes(List<BrowserType> browserTypes) {
        this.browserTypes = browserTypes;
    }

    public ExecutionResponse build(String build) {
        this.build = build;
        return this;
    }

    public String getBuild() {
        return this.build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public ExecutionResponse requester(String requester) {
        this.requester = requester;
        return this;
    }

    public String getRequester() {
        return this.requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public ExecutionResponse speed(Integer speed) {
        this.speed = speed;
        return this;
    }

    public Integer getSpeed() {
        return this.speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ExecutionResponse executionResponse = (ExecutionResponse)o;
            return Objects.equals(this.id, executionResponse.id) && Objects.equals(this.executionSetId, executionResponse.executionSetId) && Objects.equals(this.executionSetName, executionResponse.executionSetName) && Objects.equals(this.requested, executionResponse.requested) && Objects.equals(this.executionState, executionResponse.executionState) && Objects.equals(this.pool, executionResponse.pool) && Objects.equals(this.executionType, executionResponse.executionType) && Objects.equals(this.executionStatus, executionResponse.executionStatus) && Objects.equals(this.result, executionResponse.result) && Objects.equals(this.browserTypes, executionResponse.browserTypes) && Objects.equals(this.build, executionResponse.build) && Objects.equals(this.requester, executionResponse.requester) && Objects.equals(this.speed, executionResponse.speed);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.executionSetId, this.executionSetName, this.requested, this.executionState, this.pool, this.executionType, this.executionStatus, this.result, this.browserTypes, this.build, this.requester, this.speed});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ExecutionResponse {\n");
        sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
        sb.append("    executionSetId: ").append(this.toIndentedString(this.executionSetId)).append("\n");
        sb.append("    executionSetName: ").append(this.toIndentedString(this.executionSetName)).append("\n");
        sb.append("    requested: ").append(this.toIndentedString(this.requested)).append("\n");
        sb.append("    executionState: ").append(this.toIndentedString(this.executionState)).append("\n");
        sb.append("    pool: ").append(this.toIndentedString(this.pool)).append("\n");
        sb.append("    executionType: ").append(this.toIndentedString(this.executionType)).append("\n");
        sb.append("    executionStatus: ").append(this.toIndentedString(this.executionStatus)).append("\n");
        sb.append("    result: ").append(this.toIndentedString(this.result)).append("\n");
        sb.append("    browserTypes: ").append(this.toIndentedString(this.browserTypes)).append("\n");
        sb.append("    build: ").append(this.toIndentedString(this.build)).append("\n");
        sb.append("    requester: ").append(this.toIndentedString(this.requester)).append("\n");
        sb.append("    speed: ").append(this.toIndentedString(this.speed)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}


package com.daimler.heybeach.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private boolean success;
    private String errorMessage;
    private Long timestamp;
    private T data;
    private Integer status;

    public Response() {
        this.timestamp = new Date().getTime();
    }

    public Response(boolean success, Integer status) {
        this();
        this.success = success;
        this.status = status;
    }

    public Response(boolean success, String errorMessage, Integer status) {
        this();
        this.success = success;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public Response(boolean success, T data, Integer status) {
        this();
        this.success = success;
        this.data = data;
        this.status = status;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

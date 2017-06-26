package com.daimler.heybeach.backend.entities;

import com.daimler.heybeach.data.types.Entity;
import com.daimler.heybeach.data.types.Field;
import com.daimler.heybeach.data.types.Id;

@Entity(table = "payments")
public class Payment {
    @Id
    private Long id;
    @Id(fk = true)
    @Field(column = "order_id")
    private Long orderId;
    @Field(column = "payment_method")
    private String paymentMethod;
    private Boolean result;
    private String transaction;
    private Long timestamp;
    @Field(column = "error_reason")
    private String errorReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}

package com.daimler.heybeach.backend.entities;

import com.daimler.heybeach.data.types.Entity;
import com.daimler.heybeach.data.types.Field;
import com.daimler.heybeach.data.types.Id;

import java.math.BigDecimal;

@Entity(table = "orders")
public class Order {
    @Id
    private Long id;
    private Long timestamp;
    @Id(fk = true)
    @Field(column = "user_id")
    private Long userId;
    private BigDecimal amount;
    @Field(column = "billing_address")
    private String billingAddress;
    @Field(column = "shipping_address")
    private String shippingAddress;
    @Field(column = "full_name")
    private String fullName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

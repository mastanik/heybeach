package com.daimler.heybeach.backend.service;

public enum PaymentMethods {
    PAYPAL("PayPal"), VISA("Visa"), MASTERCARD("Mastercard");

    private String name;

    private PaymentMethods(String name) {
        this.name = name;
    }

    public String getPaymentName() {
        return this.name;
    }
}

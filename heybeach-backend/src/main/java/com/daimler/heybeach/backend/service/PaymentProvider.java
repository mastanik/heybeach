package com.daimler.heybeach.backend.service;

import com.daimler.heybeach.backend.entities.Order;

public interface PaymentProvider {
    public PaymentResult process(Order order);

    class PaymentResult {
        private String transactionId;
        private boolean success;
        private String errorReason;

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getErrorReason() {
            return errorReason;
        }

        public void setErrorReason(String errorReason) {
            this.errorReason = errorReason;
        }
    }
}

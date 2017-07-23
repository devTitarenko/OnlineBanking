package com.eisgroup.cb.model;

public interface PaymentMethod<T extends BaseObject> {
    String getLabel();

    Account getPaymentAccount();

    PaymentType getPaymentsType();

    public enum PaymentType {
        CARD("Card"), ACCOUNT("Account");

        private String name;

        PaymentType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

package com.example.bookstorebackend.Domain.Model.Order;

public enum OrderStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    OrderStatus(String value) {
    }

    public static OrderStatus fromString(String value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.toString().equalsIgnoreCase(value)) {
                return orderStatus;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus value: " + value);
    }

}

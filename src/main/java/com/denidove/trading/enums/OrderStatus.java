package com.denidove.trading.enums;

public enum OrderStatus {
    InWork ("в работе"),
    Delivered ("доставлено"),
    Archive ("в архиве");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

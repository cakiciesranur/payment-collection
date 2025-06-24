package com.eny.paymentcollection.enums;

public enum ResponseType {

    ERROR("ERROR"),
    REDIRECT("REDIRECT"),
    SUCCESS("SUCCESS")
    ;

    private final String text;

    ResponseType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
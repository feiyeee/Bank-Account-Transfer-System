package com.feiye.bank.exceptions;

public class MoneyNotEnoughException extends Exception {
    public MoneyNotEnoughException() {
    }

    public MoneyNotEnoughException(String message) {
        super(message);
    }
}

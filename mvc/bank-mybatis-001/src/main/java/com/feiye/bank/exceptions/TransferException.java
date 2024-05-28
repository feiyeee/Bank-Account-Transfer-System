package com.feiye.bank.exceptions;

public class TransferException extends Exception {
    public TransferException(String message) {
        super(message);
    }

    public TransferException() {
    }
}

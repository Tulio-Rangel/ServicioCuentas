package com.tulio.serviciocuentas.exception;

public class SaldoNoDisponibleException extends RuntimeException {
    public SaldoNoDisponibleException(String message) {
        super(message);
    }
}

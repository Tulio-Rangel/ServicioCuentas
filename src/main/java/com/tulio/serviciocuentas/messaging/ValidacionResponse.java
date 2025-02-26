package com.tulio.serviciocuentas.messaging;

public class ValidacionResponse {
    private boolean clienteValido;

    // Constructor, getters y setters
    public ValidacionResponse() {}

    public ValidacionResponse(boolean clienteValido) {
        this.clienteValido = clienteValido;
    }

    public boolean isClienteValido() {
        return clienteValido;
    }

    public void setClienteValido(boolean clienteValido) {
        this.clienteValido = clienteValido;
    }
}

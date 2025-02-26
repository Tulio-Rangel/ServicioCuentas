package com.tulio.serviciocuentas.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class CuentaProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    private CompletableFuture<Boolean> validacionFuture;

    public CompletableFuture<Boolean> validarCliente(Long clienteId) {
        // Crear un futuro para manejar la respuesta asincrónica
        validacionFuture = new CompletableFuture<>();

        // Enviar el mensaje a la cola de validación
        jmsTemplate.convertAndSend("validar-cliente", clienteId);

        return validacionFuture;
    }

    public void recibirRespuestaValidacion(Boolean respuesta) {
        if (validacionFuture != null) {
            validacionFuture.complete(respuesta);
        }
    }
}

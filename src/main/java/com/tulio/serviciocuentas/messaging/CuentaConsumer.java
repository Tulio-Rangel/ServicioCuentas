package com.tulio.serviciocuentas.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class CuentaConsumer {

    @Autowired
    private CuentaProducer cuentaProducer;

    @JmsListener(destination = "respuesta-validacion")
    public void recibirRespuestaValidacion(Boolean respuesta) {
        // Procesar la respuesta y completar el futuro
        cuentaProducer.recibirRespuestaValidacion(respuesta);
    }
}

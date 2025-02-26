package com.tulio.serviciocuentas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCuenta;
    private String tipoCuenta;  // Ahorro o Corriente.
    private double saldoInicial;
    private boolean estado;

    private Long clienteId;  // Relación con el cliente en el otro microservicio
}

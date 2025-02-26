package com.tulio.serviciocuentas.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReporteEstadoCuenta {

    private Long clienteId;
    private String nombreCliente;
    private Date fechaInicio;
    private Date fechaFin;
    private List<CuentaReporte> cuentas;

    @Getter
    @Setter
    public static class CuentaReporte {
        private Long id;
        private String numeroCuenta;
        private String tipoCuenta;
        private double saldoActual;
        private List<MovimientoReporte> movimientos;
    }

    @Getter
    @Setter
    public static class MovimientoReporte {
        private Long id;
        private Date fecha;
        private String tipoMovimiento;
        private double valor;
        private double saldo;
    }
}

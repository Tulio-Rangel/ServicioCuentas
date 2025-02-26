package com.tulio.serviciocuentas.service;

import com.tulio.serviciocuentas.model.Cuenta;
import com.tulio.serviciocuentas.model.Movimiento;
import com.tulio.serviciocuentas.model.ReporteEstadoCuenta;
import com.tulio.serviciocuentas.repository.CuentaRepository;
import com.tulio.serviciocuentas.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generarReporte_DebeRetornarReporteCompleto() {
        // Arrange
        Long clienteId = 1L;
        Date fechaInicio = new Date();
        Date fechaFin = new Date();

        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setClienteId(clienteId);
        cuenta.setNumeroCuenta("123456");
        cuenta.setTipoCuenta("AHORRO");
        cuenta.setSaldoInicial(1000.0);

        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(new Date());
        movimiento.setTipoMovimiento("CREDITO");
        movimiento.setValor(500.0);
        movimiento.setSaldo(1500.0);

        when(cuentaRepository.findByClienteId(clienteId)).thenReturn(Arrays.asList(cuenta));
        when(cuentaRepository.findById(clienteId)).thenReturn(java.util.Optional.of(cuenta));
        when(movimientoRepository.findByCuentaIdAndFechaBetween(cuenta.getId(), fechaInicio, fechaFin))
                .thenReturn(Arrays.asList(movimiento));

        // Act
        ReporteEstadoCuenta reporte = cuentaService.generarReporte(clienteId, fechaInicio, fechaFin);

        // Assert
        assertNotNull(reporte);
        assertEquals(clienteId, reporte.getClienteId());
        assertEquals(1, reporte.getCuentas().size());

        ReporteEstadoCuenta.CuentaReporte cuentaReporte = reporte.getCuentas().get(0);
        assertEquals(cuenta.getId(), cuentaReporte.getId());
        assertEquals(cuenta.getNumeroCuenta(), cuentaReporte.getNumeroCuenta());
        assertEquals(cuenta.getTipoCuenta(), cuentaReporte.getTipoCuenta());
        assertEquals(cuenta.getSaldoInicial(), cuentaReporte.getSaldoActual());

        assertEquals(1, cuentaReporte.getMovimientos().size());
        ReporteEstadoCuenta.MovimientoReporte movimientoReporte = cuentaReporte.getMovimientos().get(0);
        assertEquals(movimiento.getId(), movimientoReporte.getId());
        assertEquals(movimiento.getTipoMovimiento(), movimientoReporte.getTipoMovimiento());
        assertEquals(movimiento.getValor(), movimientoReporte.getValor());
        assertEquals(movimiento.getSaldo(), movimientoReporte.getSaldo());
    }
}
package com.tulio.serviciocuentas.controller;

import com.tulio.serviciocuentas.model.ReporteEstadoCuenta;
import com.tulio.serviciocuentas.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generarReporte_DebeRetornarReporteExitoso() {
        // Arrange
        Long clienteId = 1L;
        Date fechaInicio = new Date();
        Date fechaFin = new Date();

        ReporteEstadoCuenta reporteEsperado = new ReporteEstadoCuenta();
        reporteEsperado.setClienteId(clienteId);
        reporteEsperado.setFechaInicio(fechaInicio);
        reporteEsperado.setFechaFin(fechaFin);
        reporteEsperado.setCuentas(new ArrayList<>());

        when(cuentaService.generarReporte(clienteId, fechaInicio, fechaFin))
                .thenReturn(reporteEsperado);

        // Act
        ResponseEntity<ReporteEstadoCuenta> response = cuentaController.generarReporte(clienteId, fechaInicio, fechaFin);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reporteEsperado, response.getBody());
        verify(cuentaService, times(1)).generarReporte(clienteId, fechaInicio, fechaFin);
    }
}
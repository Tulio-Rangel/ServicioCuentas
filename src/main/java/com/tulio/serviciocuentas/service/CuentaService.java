package com.tulio.serviciocuentas.service;

import com.tulio.serviciocuentas.exception.ResourceNotFoundException;
import com.tulio.serviciocuentas.messaging.CuentaProducer;
import com.tulio.serviciocuentas.model.Cuenta;
import com.tulio.serviciocuentas.model.Movimiento;
import com.tulio.serviciocuentas.model.ReporteEstadoCuenta;
import com.tulio.serviciocuentas.repository.CuentaRepository;
import com.tulio.serviciocuentas.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaProducer cuentaProducer;

    public Cuenta crearCuenta(Cuenta cuenta) {
        // Validar el cliente de manera asincrónica
        CompletableFuture<Boolean> future = cuentaProducer.validarCliente(cuenta.getClienteId());

        // Esperar la respuesta y manejar el resultado
        future.thenAccept(clienteValido -> {
            if (!clienteValido) {
                throw new ResourceNotFoundException("Cliente no encontrado con id: " + cuenta.getClienteId());
            }
        }).join();  // Bloquear hasta que se complete el futuro

        return cuentaRepository.save(cuenta);
    }

    public Cuenta obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id: " + id));
    }

    public void eliminarCuenta(Long id) {
        if (!cuentaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cuenta no encontrada con id: " + id);
        }
        cuentaRepository.deleteById(id);
    }

    public ReporteEstadoCuenta generarReporte(Long clienteId, Date fechaInicio, Date fechaFin) {
        // Obtener las cuentas asociadas al cliente
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        // Construir el reporte
        ReporteEstadoCuenta reporte = new ReporteEstadoCuenta();
        reporte.setClienteId(clienteId);
        reporte.setNombreCliente(obtenerCuentaPorId(clienteId).getClienteId().toString());
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);

        // Obtener los movimientos de cada cuenta dentro del rango de fechas
        reporte.setCuentas(cuentas.stream().map(cuenta -> {
            ReporteEstadoCuenta.CuentaReporte cuentaReporte = new ReporteEstadoCuenta.CuentaReporte();
            cuentaReporte.setId(cuenta.getId());
            cuentaReporte.setNumeroCuenta(cuenta.getNumeroCuenta());
            cuentaReporte.setTipoCuenta(cuenta.getTipoCuenta());
            cuentaReporte.setSaldoActual(cuenta.getSaldoInicial());

            // Obtener movimientos de la cuenta dentro del rango de fechas
            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(
                    cuenta.getId(), fechaInicio, fechaFin);

            cuentaReporte.setMovimientos(movimientos.stream().map(movimiento -> {
                ReporteEstadoCuenta.MovimientoReporte movimientoReporte = new ReporteEstadoCuenta.MovimientoReporte();
                movimientoReporte.setId(movimiento.getId());
                movimientoReporte.setFecha(movimiento.getFecha());
                movimientoReporte.setTipoMovimiento(movimiento.getTipoMovimiento());
                movimientoReporte.setValor(movimiento.getValor());
                movimientoReporte.setSaldo(movimiento.getSaldo());
                return movimientoReporte;
            }).collect(Collectors.toList()));

            return cuentaReporte;
        }).collect(Collectors.toList()));

        return reporte;
    }
}

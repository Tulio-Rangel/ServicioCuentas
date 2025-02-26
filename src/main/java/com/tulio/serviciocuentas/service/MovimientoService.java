package com.tulio.serviciocuentas.service;

import com.tulio.serviciocuentas.exception.MovimientoIvalidoException;
import com.tulio.serviciocuentas.exception.SaldoNoDisponibleException;
import com.tulio.serviciocuentas.model.Cuenta;
import com.tulio.serviciocuentas.model.Movimiento;
import com.tulio.serviciocuentas.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaService cuentaService;

    public Movimiento crearMovimiento(Long cuentaId, Movimiento movimiento) {
        Cuenta cuenta = cuentaService.obtenerCuentaPorId(cuentaId);

        // Calcular el nuevo saldo según el tipo de movimiento
        double nuevoSaldo = cuenta.getSaldoInicial();

        if (movimiento.getTipoMovimiento().equalsIgnoreCase("Deposito")) {
            nuevoSaldo += movimiento.getValor();  // Sumar en caso de depósito
        } else if (movimiento.getTipoMovimiento().equalsIgnoreCase("Retiro")) {
            nuevoSaldo -= movimiento.getValor();  // Restar en caso de retiro
        } else {
            throw new MovimientoIvalidoException("Tipo de movimiento no válido. Use 'Deposito' o 'Retiro'.");
        }

        // Validar que el saldo no sea negativo después de un retiro
        if (nuevoSaldo < 0) {
            throw new SaldoNoDisponibleException("Saldo no disponible para realizar el retiro.");
        }

        // Actualizar el saldo de la cuenta
        cuenta.setSaldoInicial(nuevoSaldo);

        // Configurar los datos del movimiento
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(new Date());
        movimiento.setSaldo(nuevoSaldo);

        // Guardar el movimiento
        return movimientoRepository.save(movimiento);
    }

    public void eliminarMovimiento(Long id) {
        movimientoRepository.deleteById(id);
    }
}

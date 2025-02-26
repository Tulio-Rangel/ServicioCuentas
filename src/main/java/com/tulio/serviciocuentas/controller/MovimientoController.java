package com.tulio.serviciocuentas.controller;

import com.tulio.serviciocuentas.model.Movimiento;
import com.tulio.serviciocuentas.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/cuenta/{cuentaId}")
    public ResponseEntity<Movimiento> crearMovimiento(@PathVariable Long cuentaId, @RequestBody Movimiento movimiento) {
        Movimiento nuevoMovimiento = movimientoService.crearMovimiento(cuentaId, movimiento);
        return ResponseEntity.ok(nuevoMovimiento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }
}

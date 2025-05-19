package com.tcs.retomicroservices2.controller;

import com.tcs.retomicroservices2.entity.Motion;
import com.tcs.retomicroservices2.service.ServiceMotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/motion")
public class MotionController {

    @Autowired
    private ServiceMotion serviceMotion;

    @PostMapping
    public ResponseEntity<?> postMotion(@RequestBody Motion motion) {
        try {
            if (motion.getAccountId() == null) {
                return ResponseEntity.badRequest().body("Debe proporcionar una cuenta válida");
            }

            if (motion.getTypemotion() == null || motion.getValue() == null) {
                return ResponseEntity.badRequest().body("Debe proporcionar tipo de movimiento y valor");
            }

            Motion savedMotion = serviceMotion.postMotion(motion);
            return ResponseEntity.ok(savedMotion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el movimiento: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Motion> getMotions() {
        return serviceMotion.getMotions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMotionById(@PathVariable("id") long idMotion) {
        try {
            Motion motion = serviceMotion.getMotionById(idMotion);
            return ResponseEntity.ok(motion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Movimiento no encontrado");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putMotion(@PathVariable("id") long idMotion,
                                            @RequestBody Motion updatedMotion) {
        try {
            serviceMotion.putMotion(idMotion, updatedMotion);
            return ResponseEntity.ok("Se actualizó el registro del movimiento");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMotion(@PathVariable("id") long idMotion) {
        try {
            serviceMotion.deleteMotion(idMotion);
            return ResponseEntity.ok("Se eliminó el registro del movimiento");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error al eliminar: " + e.getMessage());
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getMotionsByAccountId(@PathVariable("accountId") Long accountId) {
        try {
            List<Motion> motions = serviceMotion.getMotionsByAccountId(accountId);
            return ResponseEntity.ok(motions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener movimientos: " + e.getMessage());
        }
    }

    @GetMapping("/account/{accountId}/balance")
    public ResponseEntity<?> getCurrentBalance(@PathVariable("accountId") Long accountId) {
        try {
            Double balance = serviceMotion.getCurrentBalance(accountId);
            return ResponseEntity.ok(Map.of("accountId", accountId, "currentBalance", balance));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener saldo: " + e.getMessage());
        }
    }
}
package com.tcs.retomicroservices2.controller;

import com.tcs.retomicroservices2.entity.Account;
import com.tcs.retomicroservices2.service.ServiceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private ServiceAccount serviceAccount;

    @PostMapping
    public ResponseEntity<String> postAccount(@RequestBody Account account) {
        // Verificar si el cliente existe
        if (account.getClient() == null || account.getClient().getIdclient() == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar un cliente válido.");
        }

        try {
            serviceAccount.postAccount(account);
            return ResponseEntity.ok("Cuenta agregada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la cuenta: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Account> getAccounts() {
        return serviceAccount.getAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable("id") long idAccount) {
        try {
            Account account = serviceAccount.getAccountById(idAccount);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Cuenta no encontrada.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putAccount(@PathVariable("id") long idAccount,
                                             @RequestBody Account updatedAccount) {
        try {
            serviceAccount.putAccount(idAccount, updatedAccount);
            return ResponseEntity.ok("Se actualizó el registro de la cuenta.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Cuenta no encontrada.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable("id") long idAccount) {
        try {
            serviceAccount.deleteAccount(idAccount);
            return ResponseEntity.ok("Se eliminó el registro de la cuenta.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Cuenta no encontrada.");
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> getAccountsByClientId(@PathVariable("clientId") Long clientId) {
        try {
            List<Account> accounts = serviceAccount.getAccountsByClientId(clientId);
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener cuentas del cliente: " + e.getMessage());
        }
    }
}
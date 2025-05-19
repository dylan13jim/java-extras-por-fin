package com.tcs.retomicroservices.controller;

import com.tcs.retomicroservices.entity.Client;
import com.tcs.retomicroservices.entity.Person;
import com.tcs.retomicroservices.service.ServiceClient;
import com.tcs.retomicroservices.service.ServicePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ServiceClient serviceClient;

    @Autowired
    private ServicePerson servicePerson;

    @PostMapping
    public ResponseEntity<String> postClient(@RequestBody Client client) {
        // Verificar si la persona existe
        if (client.getPerson() == null || client.getPerson().getIdperson() == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar una persona válida.");
        }

        try {
            // Verificar que la persona exista en la base de datos
            Person person = servicePerson.getPersonById(client.getPerson().getIdperson());

            // Verificar si la contraseña es válida
            if (client.getPassword() == null || client.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña no puede estar vacía.");
            }

            // Establecer el estado en true por defecto si no se proporciona
            if (client.getEstate() == null) {
                client.setEstate(true);
            }

            serviceClient.postClient(client);
            return ResponseEntity.ok("Cliente agregado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("La persona seleccionada no existe.");
        }
    }

    @GetMapping
    public List<Client> getClient() {
        return serviceClient.getClient();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable("id") long idClient) {
        try {
            serviceClient.deleteClient(idClient);
            return ResponseEntity.ok("Se eliminó el registro del cliente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Cliente no encontrado.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putClient(@PathVariable("id") long idClient,
                                            @RequestBody Client updatedClient) {
        try {
            // Verificar que el cliente exista
            Client existingClient = serviceClient.getClientById(idClient);

            // Verificar si la contraseña es válida
            if (updatedClient.getPassword() == null || updatedClient.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La contraseña no puede estar vacía.");
            }

            // Actualizar solo password y estate, manteniendo la relación con Person
            serviceClient.putClient(idClient, updatedClient);
            return ResponseEntity.ok("Se actualizó el registro del cliente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Cliente no encontrado.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable("id") long idClient) {
        try {
            Client client = serviceClient.getClientById(idClient);
            return ResponseEntity.ok(client);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Cliente no encontrado.");
        }
    }
}
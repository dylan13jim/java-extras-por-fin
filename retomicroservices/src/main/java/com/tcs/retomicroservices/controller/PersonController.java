package com.tcs.retomicroservices.controller;

import com.tcs.retomicroservices.entity.Person;
import com.tcs.retomicroservices.service.ServicePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private ServicePerson servicePerson;

    @PostMapping
    public ResponseEntity<String> postPerson(@RequestBody Person person) {
        // Validación de cédula duplicada
        if (servicePerson.isIdentificationExist(person.getIdentification())) {
            return ResponseEntity.badRequest().body("Cédula ya registrada ingrese otra.");
        }

        if (person.getIdentification().length() != 10) {
            return ResponseEntity.badRequest().body("La cedula debe tener 10 dígitos.");
        }

        // Validación de número de teléfono
        if (person.getPhoneperson().length() != 10) {
            return ResponseEntity.badRequest().body("Número de teléfono debe tener 10 dígitos.");
        }

        servicePerson.postPerson(person);
        return ResponseEntity.ok("Persona agregada correctamente.");
    }

    @GetMapping
    public List<Person> getPerson() {
        return servicePerson.getPerson();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") long idPerson) {
        servicePerson.deletePerson(idPerson);
        return ResponseEntity.ok("Se eliminó el registro de la persona.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> putPerson(@PathVariable("id") long idPerson,
                                            @RequestBody Person updatedPerson) {
        // Validación de cédula duplicada
        if (servicePerson.isIdentificationExist(updatedPerson.getIdentification()) &&
                !updatedPerson.getIdentification().equals(servicePerson.getPersonById(idPerson).getIdentification())) {
            return ResponseEntity.badRequest().body("Esta no es cedula ingrese su cedula.");
        }

        if (updatedPerson.getIdentification().length() != 10) {
            return ResponseEntity.badRequest().body("La cedula debe tener 10 dígitos.");
        }

        // Validación de número de teléfono
        if (updatedPerson.getPhoneperson().length() != 10) {
            return ResponseEntity.badRequest().body("Número de teléfono debe tener 10 dígitos.");
        }

        servicePerson.putPerson(idPerson, updatedPerson);
        return ResponseEntity.ok("Se actualizó el registro.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable("id") long idPerson) {
        Person person = servicePerson.getPersonById(idPerson);
        return ResponseEntity.ok(person);
    }
}

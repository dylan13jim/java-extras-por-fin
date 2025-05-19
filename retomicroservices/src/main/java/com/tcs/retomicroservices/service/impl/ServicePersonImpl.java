package com.tcs.retomicroservices.service.impl;

import com.tcs.retomicroservices.entity.Person;
import com.tcs.retomicroservices.repository.PersonRepository;
import com.tcs.retomicroservices.service.ServicePerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePersonImpl implements ServicePerson {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void postPerson(Person person) {
        personRepository.save(person);
    }

    @Override
    public List<Person> getPerson() {
        return personRepository.findAll();
    }

    @Override
    public void putPerson(long idPerson, Person updatedPerson) {
        Person existing = personRepository.findById(idPerson)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        existing.setNameperson(updatedPerson.getNameperson());
        existing.setGenderperson(updatedPerson.getGenderperson());
        existing.setAgeperson(updatedPerson.getAgeperson());
        existing.setIdentification(updatedPerson.getIdentification());
        existing.setAddressperson(updatedPerson.getAddressperson());
        existing.setPhoneperson(updatedPerson.getPhoneperson());

        personRepository.save(existing);
    }

    @Override
    public Person getPersonById(long idPerson) {
        return personRepository.findById(idPerson)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }

    @Override
    public void deletePerson(long id) {
        personRepository.deleteById(id);
    }

    // Verificar si la cédula ya existe
    public boolean isIdentificationExist(String identification) {
        return personRepository.findByIdentification(identification).isPresent();
    }
}

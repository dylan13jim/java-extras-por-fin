package com.tcs.retomicroservices.service;

import com.tcs.retomicroservices.entity.Person;

import java.util.List;

public interface ServicePerson {

    void postPerson(Person person);

    List<Person> getPerson();

    Person getPersonById(long idPerson);

    void putPerson(long idPerson, Person updatedPerson);

    void deletePerson(long idPerson);

    boolean isIdentificationExist(String identification);
}

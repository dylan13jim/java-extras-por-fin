package com.tcs.retomicroservices.service;

import com.tcs.retomicroservices.entity.Client;

import java.util.List;

public interface ServiceClient {

    void postClient(Client client);

    List<Client> getClient();

    Client getClientById(long idClient);

    void putClient(long idClient, Client updatedClient);

    void deleteClient(long idClient);
}

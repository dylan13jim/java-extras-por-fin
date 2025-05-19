package com.tcs.retomicroservices.service.impl;

import com.tcs.retomicroservices.entity.Client;
import com.tcs.retomicroservices.repository.ClientRepository;
import com.tcs.retomicroservices.service.ServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceClientImpl implements ServiceClient {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void postClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<Client> getClient() {
        return clientRepository.findAll();
    }

    @Override
    public void putClient(long idClient, Client updatedClient) {
        Client existing = clientRepository.findById(idClient)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        existing.setPassword(updatedClient.getPassword());
        existing.setEstate(updatedClient.getEstate());
        clientRepository.save(existing);
    }

    @Override
    public Client getClientById(long idClient) {
        return clientRepository.findById(idClient)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }
}

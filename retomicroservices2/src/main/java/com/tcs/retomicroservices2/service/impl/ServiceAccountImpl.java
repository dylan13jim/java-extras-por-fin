package com.tcs.retomicroservices2.service.impl;

import com.tcs.retomicroservices2.dto.ClientDto;
import com.tcs.retomicroservices2.entity.Account;
import com.tcs.retomicroservices2.repository.AccountRepository;
import com.tcs.retomicroservices2.service.ServiceAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceAccountImpl implements ServiceAccount {

    @Autowired
    private AccountRepository accountRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String clientServiceUrl = "http://localhost:8080";

    @Override
    public void postAccount(Account account) {
        // Si recibimos un DTO de cliente, extraemos el ID
        if (account.getClient() != null && account.getClient().getIdclient() != null) {
            account.setClientId(account.getClient().getIdclient());
        }

        // Por defecto, establecer estado como true si no se proporciona
        if (account.getEstateaccount() == null) {
            account.setEstateaccount(true);
        }

        accountRepository.save(account);
    }

    @Override
    public List<Account> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::populateClientDto)
                .collect(Collectors.toList());
    }

    @Override
    public Account getAccountById(long idAccount) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return populateClientDto(account);
    }

    @Override
    public void putAccount(long idAccount, Account updatedAccount) {
        Account existing = accountRepository.findById(idAccount)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // Actualizar campos
        existing.setNumaccount(updatedAccount.getNumaccount());
        existing.setTypeaccount(updatedAccount.getTypeaccount());
        existing.setInibalance(updatedAccount.getInibalance());
        existing.setEstateaccount(updatedAccount.getEstateaccount());

        // Actualizar clientId si se proporciona
        if (updatedAccount.getClient() != null && updatedAccount.getClient().getIdclient() != null) {
            existing.setClientId(updatedAccount.getClient().getIdclient());
        }

        // Guardar cambios
        accountRepository.save(existing);
    }

    @Override
    public void deleteAccount(long idAccount) {
        accountRepository.deleteById(idAccount);
    }

    @Override
    public List<Account> getAccountsByClientId(Long clientId) {
        List<Account> accounts = accountRepository.findByClientId(clientId);
        return accounts.stream()
                .map(this::populateClientDto)
                .collect(Collectors.toList());
    }

    /**
     * Método para obtener datos completos del cliente usando RestTemplate
     */
    private Account populateClientDto(Account account) {
        if (account.getClientId() != null) {
            try {
                // Usar RestTemplate para obtener datos completos del cliente
                ClientDto clientDto = restTemplate.getForObject(
                        clientServiceUrl + "/client/" + account.getClientId(),
                        ClientDto.class);

                account.setClient(clientDto);
            } catch (Exception e) {
                // En caso de error, crear DTO básico
                ClientDto basicClientDto = new ClientDto(account.getClientId());
                account.setClient(basicClientDto);
                System.out.println("Error al obtener cliente: " + e.getMessage());
            }
        }
        return account;
    }
}
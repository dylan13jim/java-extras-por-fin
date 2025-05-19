package com.tcs.retomicroservices2.service;

import com.tcs.retomicroservices2.entity.Account;
import java.util.List;

public interface ServiceAccount {
    void postAccount(Account account);
    List<Account> getAccounts();
    Account getAccountById(long idAccount);
    void putAccount(long idAccount, Account updatedAccount);
    void deleteAccount(long idAccount);
    List<Account> getAccountsByClientId(Long clientId);
}
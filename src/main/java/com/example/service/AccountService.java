package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /*
     * User registers for an account
     * @param newAccount, an Account object that contains the values of the new account being registered for
     */
    public void register(Account newAccount) {
        // Username can't be blank or null and if it is then throw an exception
        if (newAccount.getUsername() == null || newAccount.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username can not be blank");
        }

        // password can't be null or less than 4 and if it is throw an exception
        if (newAccount.getPassword() == null || newAccount.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 character long");
        }

        // to register the user the username can't aleady be used
        if (accountRepository.existsByUsername(newAccount.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        accountRepository.save(newAccount);
    }

    /*
     * User logs in with their credentials
     * @param username, the user's username
     * @param password, the user's password
     * @return account if the username and password is correct and if not then return null
     */
    public Account login(String username, String password) {
        Account account = accountRepository.findByUsername(username);

        if (account == null) {
            throw new IllegalArgumentException("Username is not valid");
        }

        if (!account.getPassword().equals(password)) {
            throw new IllegalArgumentException("Password is not valid");
        }

        if (account != null && account.getPassword().equals(password)) {
            return account;
        }

        return null;
    }
}

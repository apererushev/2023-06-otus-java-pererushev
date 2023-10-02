package ru.otus.apererushev.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.apererushev.entity.Account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AccountRepository {

    private final Map<Integer, Account> accountsDbEmulation;

    public AccountRepository() {
        accountsDbEmulation = new HashMap<>();
        accountsDbEmulation.put(1, new Account(1, new BigDecimal("100500.00")));
        accountsDbEmulation.put(2, new Account(2, new BigDecimal("0.00")));
        accountsDbEmulation.put(3, new Account(3, new BigDecimal("752.00")));
    }


    public @Nullable Account getById(@NotNull Integer id) {
        return accountsDbEmulation.get(id);
    }

    public void save(@NotNull Account account) {
        accountsDbEmulation.put(account.getId(), account);
    }
}

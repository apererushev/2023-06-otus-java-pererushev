package ru.otus.apererushev.repository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.apererushev.entity.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository {

    private final Map<Integer, Account> accounts = new HashMap<>();

    public @Nullable Account getById(@NotNull Integer id) {
        return accounts.get(id);
    }

    public void save(@NotNull Account account) {
        accounts.put(account.getId(), account);
    }
}

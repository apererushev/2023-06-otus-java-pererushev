package ru.otus.apererushev.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.entity.Account;
import ru.otus.apererushev.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Override
    public @NotNull Account getById(@NotNull Integer id) throws AccountNotFoundException {
        return Optional.ofNullable(repository.getById(id)).orElseThrow(AccountNotFoundException::new);
    }

    @Override
    public void changeRest(@NotNull Integer accountId, @NotNull BigDecimal amount) throws AccountNotFoundException, NotEnoughRestException {
        var account = getById(accountId);
        if (amount.compareTo(BigDecimal.ZERO) < 0 && account.getRest().compareTo(amount) < 0) {
            throw new NotEnoughRestException();
        }
        var updatedAccount = new Account(account.getId(), account.getRest().subtract(amount));
        repository.save(updatedAccount);
    }
}
package ru.otus.apererushev.atm;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.entity.Account;
import ru.otus.apererushev.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public class AtmImpl implements Atm {

    private final AccountRepository accountRepository;

    @Override
    public @NotNull BigDecimal getRest(@NotNull Integer accountId) {
        return Optional.ofNullable(accountRepository.getById(accountId)).map(Account::getRest).orElseThrow();
    }

    @Override
    public @NotNull Boolean withdraw(@NotNull Integer accountId, @NotNull BigDecimal amount) {
        return null;
    }

    @Override
    public void load(@NotNull Integer nominal, @NotNull BigDecimal amount) {

    }
}

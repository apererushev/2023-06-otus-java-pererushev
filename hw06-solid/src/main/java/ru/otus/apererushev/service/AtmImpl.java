package ru.otus.apererushev.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.entity.Account;
import ru.otus.apererushev.model.AtmCassette;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class AtmImpl implements Atm {

    private static final String ACCOUNT_NOT_FOUND_ERROR_TEMPLATE = "Account %s not found";

    private final AccountService accountService;

    private final AtmCassetteService atmCassetteService;

    @Override
    public @NotNull BigDecimal getRest(@NotNull final Integer accountId) {
        return Optional.of(accountService.getById(accountId))
                .map(Account::getRest)
                .orElseThrow(() -> new AccountNotFoundException(ACCOUNT_NOT_FOUND_ERROR_TEMPLATE.formatted(accountId)));
    }

    @Override
    public @NotNull Map<Integer, Integer> withdraw(@NotNull final Integer accountId, @NotNull final BigDecimal amount)
            throws AccountNotFoundException, NotEnoughMoneyException, NotEnoughRestException {
        accountService.changeRest(accountId, amount);
        var money = atmCassetteService.withdraw(amount);

        giveMoney(money);
        return money;

    }

    @Override
    public void load(@NotNull AtmCassette cassette) {
        atmCassetteService.load(cassette);
    }

    private void giveMoney(@NotNull final Map<Integer, Integer> money) {
        for (var entity : money.entrySet()) {
            System.out.printf("Give %s of %s%n", entity.getValue(), entity.getKey());
        }
    }
}

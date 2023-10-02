package ru.otus.apererushev.atm;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface Atm {

    @NotNull BigDecimal getRest(@NotNull Integer accountId);

    @NotNull Boolean withdraw(@NotNull Integer accountId, @NotNull BigDecimal amount);

    void load(@NotNull Integer nominal, @NotNull BigDecimal amount);
}

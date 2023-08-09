package ru.otus.apererushev.biseneslogic;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class SomeTestableClass {

    public @NotNull BigDecimal doSum(@NotNull final BigDecimal v1, @NotNull final BigDecimal v2) {
        return v1.add(v2);
    }

    public void doException() throws TestableException {
        throw new TestableException();
    }

    public void doNothing() {
        // do nothing
    }
}

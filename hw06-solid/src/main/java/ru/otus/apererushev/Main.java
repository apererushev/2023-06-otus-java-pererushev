package ru.otus.apererushev;

import ru.otus.apererushev.atm.Atm;
import ru.otus.apererushev.atm.AtmImpl;
import ru.otus.apererushev.repository.AccountRepository;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        Atm atm = new AtmImpl(new AccountRepository());
        atm.load(100, new BigDecimal("1000.00"));
        System.out.println(atm.getRest(1));
    }
}

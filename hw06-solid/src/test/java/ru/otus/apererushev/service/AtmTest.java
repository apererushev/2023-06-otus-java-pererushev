package ru.otus.apererushev.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import ru.otus.apererushev.entity.Account;
import ru.otus.apererushev.model.AtmCassette;
import ru.otus.apererushev.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AtmTest {

    @Test
    void getRest() {
        var ACC_ID = 1;
        var REST_100_00 = new BigDecimal("100.00");

        var testRepository = new AccountRepository();
        testRepository.save(new Account(ACC_ID, REST_100_00));

        var testAccountService = new AccountServiceImpl(testRepository);

        var testAtmCassetteService = new AtmCassetteServiceImpl();

        Atm testAtm = new AtmImpl(testAccountService, testAtmCassetteService);

        assertEquals(REST_100_00, testAtm.getRest(ACC_ID));
    }

    @Test
    @Description("Simple withdraw")
    void withdrawSimple() {
        var ACC_ID = 1;
        var REST = new BigDecimal("3350.00");

        var testRepository = new AccountRepository();
        testRepository.save(new Account(ACC_ID, REST));

        var testAccountService = new AccountServiceImpl(testRepository);

        var testAtmCassetteService = new AtmCassetteServiceImpl();

        Atm testAtm = new AtmImpl(testAccountService, testAtmCassetteService);
        testAtm.load(new AtmCassette(5000, 100));
        testAtm.load(new AtmCassette(1000, 100));
        testAtm.load(new AtmCassette(100, 100));
        testAtm.load(new AtmCassette(50, 100));

        var money = testAtm.withdraw(1, REST);
        assertThat(money).isEqualTo(Map.of(50, 1, 100, 3, 1000, 3));
    }

    @Test
    @Description("Withdraw from multiple cassettes")
    void withdrawMultipleCassettes() {
        var ACC_ID = 1;
        var REST = new BigDecimal("3350.00");

        var testRepository = new AccountRepository();
        testRepository.save(new Account(ACC_ID, REST));

        var testAccountService = new AccountServiceImpl(testRepository);

        var testAtmCassetteService = new AtmCassetteServiceImpl();

        Atm testAtm = new AtmImpl(testAccountService, testAtmCassetteService);
        testAtm.load(new AtmCassette(1000, 1));
        testAtm.load(new AtmCassette(1000, 2));
        testAtm.load(new AtmCassette(1000, 2));
        testAtm.load(new AtmCassette(100, 1));
        testAtm.load(new AtmCassette(100, 50));
        testAtm.load(new AtmCassette(50, 100));

        var money = testAtm.withdraw(1, REST);
        assertThat(money).isEqualTo(Map.of(50, 1, 100, 3, 1000, 3));
    }

    @Test
    @Description("Withdraw with only 50 is available")
    void withdrawOnly50() {
        var ACC_ID = 1;
        var REST = new BigDecimal("3350.00");

        var testRepository = new AccountRepository();
        testRepository.save(new Account(ACC_ID, REST));

        var testAccountService = new AccountServiceImpl(testRepository);

        var testAtmCassetteService = new AtmCassetteServiceImpl();

        Atm testAtm = new AtmImpl(testAccountService, testAtmCassetteService);
        testAtm.load(new AtmCassette(50, 100));

        var money = testAtm.withdraw(1, REST);
        assertThat(money).isEqualTo(Map.of(50, 67));
    }

    @Test
    void load() {
        var testAtmCassetteService = new AtmCassetteServiceImpl();

        Atm testAtm = new AtmImpl(new AccountServiceImpl(new AccountRepository()), testAtmCassetteService);
        testAtm.load(new AtmCassette(1000, 1));
        testAtm.load(new AtmCassette(1000, 2));
        testAtm.load(new AtmCassette(1000, 2));
        testAtm.load(new AtmCassette(100, 5));
        testAtm.load(new AtmCassette(50, 5));

        assertEquals(new BigDecimal("5750.00"), testAtmCassetteService.getTotalRest());
    }
}
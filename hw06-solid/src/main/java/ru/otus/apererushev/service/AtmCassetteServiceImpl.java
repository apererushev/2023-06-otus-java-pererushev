package ru.otus.apererushev.service;

import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.model.AtmCassette;

import java.math.BigDecimal;
import java.util.*;

public class AtmCassetteServiceImpl implements AtmCassetteService {

    private final List<AtmCassette> cassettes = new ArrayList<>();

    @Override
    public BigDecimal getTotalRest() {
        var rest = new BigDecimal("0.00");
        for (var cassette : cassettes) {
            rest = rest.add(getCassetteRest(cassette));
        }
        return rest;
    }

    @Override
    public void load(@NotNull AtmCassette cassette) {
        cassettes.add(cassette);
    }

    @Override
    public Map<Integer, Integer> withdraw(@NotNull BigDecimal amount) throws NotEnoughMoneyException {
        var money = new HashMap<Integer, Integer>();

        var sortedCassettes = cassettes.stream()
                .sorted(Comparator.comparingInt(AtmCassette::getNominal).reversed())
                .toList();

        var intAmount = amount.intValue();
        for (var cassette : sortedCassettes) {
            if (intAmount >= cassette.getNominal() && cassette.getCount() > 0) {
                var needCount = intAmount / cassette.getNominal();
                var haveCount = Math.min(needCount, cassette.getCount());
                cassette.poll(haveCount);
                if (money.containsKey(cassette.getNominal())) {
                    money.put(cassette.getNominal(), money.get(cassette.getNominal()) + haveCount);
                } else {
                    money.put(cassette.getNominal(), haveCount);
                }
                intAmount -= cassette.getNominal() * haveCount;
            }
        }

        if (intAmount == 0) {
            return money;
        } else {
            throw new NotEnoughMoneyException();
        }
    }

    private BigDecimal getCassetteRest(@NotNull final AtmCassette cassette) {
        return new BigDecimal(cassette.getNominal() * cassette.getCount());
    }
}

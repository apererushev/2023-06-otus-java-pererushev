package ru.otus.apererushev.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public class Account {

    private final Integer id;

    private final BigDecimal rest;
}

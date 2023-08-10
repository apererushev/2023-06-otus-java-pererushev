package ru.otus.apererushev;

import ru.otus.apererushev.test.SomeTestableClassTest;
import ru.otus.apererushev.testengine.TestEngine;

public class Main {
    public static void main(String[] args) {
        TestEngine.test(SomeTestableClassTest.class);
    }
}
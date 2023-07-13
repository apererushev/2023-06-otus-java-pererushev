package ru.otus.apererushev;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;


public class HelloOtus {
    public static void main(String... args) {
        List<Integer> example = new ArrayList<>();
        int min = 0;
        int max = 100;
        for (int i = min; i < max; i++) {
            example.add(i);
        }

        Lists.partition(example, 10).forEach(System.out::println);
    }
}

package ru.otus.apererushev;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomerReverseOrder {

    private final Queue<Customer> customerQueue = Collections.asLifoQueue(new LinkedList<>());

    /**
     * Метод добавляет клиента в коллекцию сервиса
     * @param customer клиент
     */
    public void add(@NotNull final Customer customer) {
        customerQueue.add(customer);
    }

    /**
     * Метод возвращает первого, по методике LIFO, клиента из коллекции сервиса или {@code null} если коллекция пуста
     * @return первый, по методике LIFO, клиента из коллекции сервиса или {@code null} если коллекция пуста
     */
    public @Nullable Customer take() {
        return customerQueue.poll();
    }
}

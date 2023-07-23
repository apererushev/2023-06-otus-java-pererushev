package ru.otus.apererushev;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CustomerService {

    private final TreeMap<Customer, String> customers = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    /**
     * Метод возвращает копию первой записи коллекции сервиса или {@code null} если коллекция пуста
     * @return копия первой записи коллекции сервиса или {@code null} если коллекция пуста
     */
    public @Nullable Map.Entry<Customer, String> getSmallest() {
        return copyEntry(customers.firstEntry());
    }

    /**
     * Метод ищет в коллекции сервиса клиента следующего по очкам ({@code Customer.Score}),<br/>
     * относительно переданного и возвращает копию записи найденного клиента
     * @param customer объект клиента относительно которого будет производиться поиск
     * @return копия записи найденного клиента
     */
    public @Nullable Map.Entry<Customer, String> getNext(@NotNull final Customer customer) {
        return copyEntry(customers.higherEntry(customer));
    }

    /**
     * Метод добавляет клиента и его доп. информацию в коллекцию сервиса <br/>
     * Если клиент уже содержится в коллекции сервиса - то его доп. информация будет обновлена
     *
     * @param customer клиент
     * @param data доп. информация о клиенте
     */
    public void add(@NotNull final Customer customer, @NotNull final String data) {
        customers.put(customer, data);
    }

    private @NotNull Customer copyCostumer(@NotNull final Customer customer) {
        return new Customer(customer.getId(), customer.getName(), customer.getScores());
    }

    private @Nullable Map.Entry<Customer, String> copyEntry(@Nullable final Map.Entry<Customer, String> entry){
        if (entry != null) {
            return new AbstractMap.SimpleEntry<>(copyCostumer(entry.getKey()), entry.getValue());
        } else {
            return null;
        }
    }
}

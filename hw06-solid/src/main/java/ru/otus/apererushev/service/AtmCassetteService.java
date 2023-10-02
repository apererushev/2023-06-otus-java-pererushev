package ru.otus.apererushev.service;

import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.model.AtmCassette;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Интерфейс представляющий методы работы с сервисом кассет банкомата
 */
public interface AtmCassetteService {

    /**
     * Метод возвращает суммарный остаток на всех кассетах банкомата
     *
     * @return суммарный остаток
     */
    BigDecimal getTotalRest();

    /**
     * Метод загрузки кассет с купюрами. </br>
     * Сервис предусматривает работу с несколькими кассетами с одинаковым номиналом
     *
     * @param cassette кассета с наличными
     */
    void load(@NotNull AtmCassette cassette);

    /**
     * Метод выполняет операцию снятия наличных с указанного счета. </br>
     * Метод должен уменьшать количество купюр в кассетах и
     * возвращать карту состоящую из пар номинал и количество купюр выданных банкоматом.
     *
     * @param amount сумма к снятию
     * @return Возвращает карту из пары номинал и количество купюр выданных банкоматом
     * @throws NotEnoughMoneyException выбрасывается при нехватке купюр в банкомате
     */
    Map<Integer, Integer> withdraw(@NotNull BigDecimal amount) throws NotEnoughMoneyException;
}

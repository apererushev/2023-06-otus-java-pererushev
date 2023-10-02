package ru.otus.apererushev.service;

import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.model.AtmCassette;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Интерфейс представляющий методы работы с банкоматом
 */
public interface Atm {

    /**
     * Метод возвращает остаток на указанном счете. </br>
     * В случае отсутствия информации об указанном счете должен выбрасывать исключение {@link AccountNotFoundException}.
     *
     * @param accountId идентификатор счета
     * @return Остаток нга указанном счете
     * @throws AccountNotFoundException выбрасывается при отсутствии указанного счета
     */
    @NotNull BigDecimal getRest(@NotNull Integer accountId) throws AccountNotFoundException;

    /**
     * Метод выполняет операцию снятия наличных с указанного счета. </br>
     * Метод должен уменьшать остаток на счете и количество купюр в кассетах и возвращать карту
     * состоящую из пар номинал и количество купюр выданных банкоматом.
     *
     * @param accountId идентификатор счета
     * @param amount сумма к снятию
     * @return Возвращает карту из пары номинал и количество купюр выданных банкоматом
     * @throws AccountNotFoundException выбрасывается при отсутствии указанного счета
     * @throws NotEnoughMoneyException выбрасывается при нехватке купюр в банкомате
     * @throws NotEnoughRestException выбрасывается при нехватке остатка на счете
     */
    @NotNull Map<Integer, Integer> withdraw(@NotNull Integer accountId, @NotNull BigDecimal amount) throws AccountNotFoundException, NotEnoughMoneyException, NotEnoughRestException;

    /**
     * Метод загрузки кассет с купюрами. </br>
     * Банкомат может содержать несколько кассет с одинаковым номиналом.
     *
     * @param cassette кассета с наличными
     */
    void load(@NotNull AtmCassette cassette);
}

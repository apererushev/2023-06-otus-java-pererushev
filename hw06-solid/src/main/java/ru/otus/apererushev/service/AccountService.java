package ru.otus.apererushev.service;

import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.entity.Account;

import java.math.BigDecimal;

/**
 * Интерфейс представляющий методы для работы с сервисом счетов
 */
public interface AccountService {

    /**
     * Метод возвращает счет по переданному id.
     *
     * @param accountId Идентификатор счета
     * @return Объект счета
     * @throws AccountNotFoundException выбрасывается при отсутствии информации по указанному счету
     */
    @NotNull Account getById(@NotNull Integer accountId) throws AccountNotFoundException;

    /**
     * Метод изменения остатка на указанном счете
     *
     * @param accountId Идентификатор счета
     * @param amount сумма на которую нужно изменить остаток
     */
    void changeRest(@NotNull Integer accountId, @NotNull BigDecimal amount);
}

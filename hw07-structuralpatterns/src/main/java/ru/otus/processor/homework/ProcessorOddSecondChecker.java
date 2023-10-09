package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * Процессор не меняет исходное сообщение, но вызывает исключение {@link EvenSecondException} каждую четную секунду
 *
 * @see Processor
 * @see EvenSecondException
 */
public class ProcessorOddSecondChecker implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorOddSecondChecker(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new EvenSecondException();
        }
        return message;
    }
}

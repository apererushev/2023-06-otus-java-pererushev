package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * Процессор меняет местами значения field11 и field12
 *
 * @see Processor
 */
public class ProcessorSwapFields implements Processor {
    @Override
    public Message process(Message message) {
        var field11 = message.getField11();
        var field12 = message.getField12();
        return message.toBuilder().field11(field12).field12(field11).build();
    }
}

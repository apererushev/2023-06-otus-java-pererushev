package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.ProcessorOddSecondChecker;
import ru.otus.processor.homework.ProcessorSwapFields;

import java.time.LocalDateTime;
import java.util.List;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */
    @SuppressWarnings({"java:S106", "java:S3457"})
    public static void main(String[] args) {
        var processors = List.of(
                new ProcessorSwapFields(),
                new ProcessorOddSecondChecker(LocalDateTime::now)
        );

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var f13 = new ObjectForMessage();
        f13.setData(List.of("13", "Thirty", "XIII"));

        var demoMessageId = 10;
        var message = new Message.Builder(demoMessageId)
                .field1("One")
                .field2("Two")
                .field3("Three")
                .field4("Four")
                .field5("Five")
                .field6("Six")
                .field7("Seven")
                .field8("Eight")
                .field9("Nine")
                .field10("Ten")
                .field11("Eleven")
                .field12("Twelve")
                .field13(f13)
                .build();

        var newMessage = complexProcessor.handle(message);
        System.out.printf("Original message is: %s\n", message);
        System.out.printf("New message is: %s\n", newMessage);
        System.out.printf("Message from history is: %s\n", historyListener.findMessageById(demoMessageId));

        complexProcessor.removeListener(historyListener);
    }
}

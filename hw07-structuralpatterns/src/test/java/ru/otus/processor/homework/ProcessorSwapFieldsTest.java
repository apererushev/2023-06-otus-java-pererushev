package ru.otus.processor.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorSwapFieldsTest {

    @Test
    void process() {
        var f13 = new ObjectForMessage();
        var message = new Message.Builder(1)
                .field1("1")
                .field2("2")
                .field3("3")
                .field4("4")
                .field5("5")
                .field6("6")
                .field7("7")
                .field8("8")
                .field9("9")
                .field10("10")
                .field11("11")
                .field12("12")
                .field13(f13)
                .build();

        var expectedMessage = new Message.Builder(1)
                .field1("1")
                .field2("2")
                .field3("3")
                .field4("4")
                .field5("5")
                .field6("6")
                .field7("7")
                .field8("8")
                .field9("9")
                .field10("10")
                .field11("12")
                .field12("11")
                .field13(f13)
                .build();

        var processor = new ProcessorSwapFields();
        var result = processor.process(message);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedMessage);
    }
}
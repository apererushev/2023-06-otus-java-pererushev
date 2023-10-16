package ru.otus.jdbc.mapper;

import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.otus.crm.annotation.Id;
import ru.otus.crm.annotation.TableName;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class EntityClassMetaDataImplTest {

    @Test
    void getName() {
        // given
        var testClass1MetaData = new EntityClassMetaDataImpl<>(TestClass1.class);
        var expectedTestClass1TableName = "test_class_one";

        var testClass2MetaData = new EntityClassMetaDataImpl<>(TestClass2.class);
        var expectedTestClass2TableName = "testclass2";

        // when
        var testClass1TableName = testClass1MetaData.getName();
        var testClass2TableName = testClass2MetaData.getName();

        // then
        assertThat(testClass1TableName)
                .isEqualTo(expectedTestClass1TableName);
        assertThat(testClass2TableName)
                .isEqualTo(expectedTestClass2TableName);
    }

    @Test
    @SneakyThrows
    void getConstructor() {
        // given
        var testClass1MetaData = new EntityClassMetaDataImpl<>(TestClass1.class);
        var expectedConstructor = TestClass1.class.getDeclaredConstructor();

        // when
        var constructor = testClass1MetaData.getConstructor();

        // then
        assertThat(constructor)
                .isEqualTo(expectedConstructor);
    }

    @Test
    void getIdField() {
        // given
        var testClass1MetaData = new EntityClassMetaDataImpl<>(TestClass1.class);
        var expectedClass1IdFieldName = "id";
        var testClass2MetaData = new EntityClassMetaDataImpl<>(TestClass2.class);
        var expectedClass2IdFieldName = "code";

        // when
        var class1IdField = testClass1MetaData.getIdField();
        var class2IdField = testClass2MetaData.getIdField();

        // then
        assertThat(class1IdField.getName())
                .isEqualTo(expectedClass1IdFieldName);
        assertThat(class2IdField.getName())
                .isEqualTo(expectedClass2IdFieldName);
    }

    @Test
    void getAllFields() {
        // given
        var testClass1MetaData = new EntityClassMetaDataImpl<>(TestClass1.class);
        var expectedClass1IdFieldNameList = List.of("id", "name");
        var testClass2MetaData = new EntityClassMetaDataImpl<>(TestClass2.class);
        var expectedClass2IdFieldNameList = List.of("code");

        // when
        var class1IdFields = testClass1MetaData.getAllFields();
        var class2IdFields = testClass2MetaData.getAllFields();

        // then
        assertThatCollection(
                class1IdFields.stream()
                        .map(Field::getName)
                        .toList()
        )
                .containsExactlyInAnyOrderElementsOf(expectedClass1IdFieldNameList);
        assertThatCollection(
                class2IdFields.stream()
                        .map(Field::getName)
                        .toList()
        )
                .containsExactlyInAnyOrderElementsOf(expectedClass2IdFieldNameList);
    }

    @Test
    void getFieldsWithoutId() {
        // given
        var testClass1MetaData = new EntityClassMetaDataImpl<>(TestClass1.class);
        var expectedClass1IdFieldNameList = List.of("name");
        var testClass2MetaData = new EntityClassMetaDataImpl<>(TestClass2.class);
        List<String> expectedClass2IdFieldNameList = List.of();

        // when
        var class1IdFields = testClass1MetaData.getFieldsWithoutId();
        var class2IdFields = testClass2MetaData.getFieldsWithoutId();

        // then
        assertThatCollection(
                class1IdFields.stream()
                        .map(Field::getName)
                        .toList()
        )
                .containsExactlyInAnyOrderElementsOf(expectedClass1IdFieldNameList);
        assertThatCollection(
                class2IdFields.stream()
                        .map(Field::getName)
                        .toList()
        )
                .containsExactlyInAnyOrderElementsOf(expectedClass2IdFieldNameList);
    }

    @TableName("test_class_one")
    @Getter
    private static class TestClass1 {

        @Id
        private int id;

        private String name;
    }

    @Getter
    private static class TestClass2 {

        @Id
        private String code;
    }
}
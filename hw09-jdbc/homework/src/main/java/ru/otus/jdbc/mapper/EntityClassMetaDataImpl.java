package ru.otus.jdbc.mapper;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import ru.otus.crm.annotation.Id;
import ru.otus.crm.annotation.TableName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    @Getter
    private final Constructor<T> constructor;
    @Getter
    private final Field idField;
    @Getter
    private final List<Field> allFields;
    @Getter
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(@NotNull final Class<T> clazz) {
        this.clazz = clazz;
        this.constructor = initConstructor();
        this.allFields = initAllFields();
        this.idField = initIdField();
        this.fieldsWithoutId = initFieldsWithoutId();
    }

    @Override
    public String getName() {
        var table = clazz.getAnnotation(TableName.class);
        if (Objects.nonNull(table)) {
            return table.value();
        }

        return clazz.getSimpleName().toLowerCase();
    }

    @SneakyThrows
    private Constructor<T> initConstructor() {
        return clazz.getDeclaredConstructor();
    }

    private List<Field> initAllFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .toList();
    }

    private List<Field> initFieldsWithoutId() {
        return getAllFields().stream()
                .filter(f -> Objects.isNull(f.getAnnotation(Id.class)))
                .toList();
    }

    private Field initIdField() {
        var idFields = getAllFields().stream()
                .filter(f -> Objects.nonNull(f.getAnnotation(Id.class)))
                .toList();
        if (idFields.size() != 1) {
            throw new NoIdFieldException();
        }
        return idFields.get(0);
    }
}

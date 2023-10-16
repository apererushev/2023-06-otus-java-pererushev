package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.crm.annotation.Id;
import ru.otus.crm.annotation.TableName;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    @Override
    public String getName() {
        var table = clazz.getAnnotation(TableName.class);
        if (Objects.nonNull(table)) {
            return table.value();
        }

        return clazz.getSimpleName().toLowerCase();
    }

    @Override
    @SneakyThrows
    public Constructor<T> getConstructor() {
        return clazz.getDeclaredConstructor();
    }

    @Override
    public Field getIdField() {
        var idFields = getAllFields().stream()
                .filter(f -> Objects.nonNull(f.getAnnotation(Id.class)))
                .toList();
        if (idFields.size() != 1) {
            throw new NoIdFieldException();
        }
        return idFields.get(0);
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .toList();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return getAllFields().stream()
                .filter(f -> Objects.isNull(f.getAnnotation(Id.class)))
                .toList();
    }
}

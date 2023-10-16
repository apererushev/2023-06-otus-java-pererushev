package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private static final String SELECT_ALL_QUERY_TEMPLATE = "select %s from %s %s";
    private static final String SELECT_BY_ID_QUERY_TEMPLATE = "%s where %s = ?";
    private static final String INSERT_QUERY_TEMPLATE = "insert into %s (%s) values(%s)";
    private static final String UPDATE_QUERY_TEMPLATE = "update %s set %s where %s = ?";

    private final EntityClassMetaData<T> metaData;

    @Override
    public String getSelectAllSql() {
        return SELECT_ALL_QUERY_TEMPLATE.formatted(
                generateFieldsString(metaData.getAllFields(), generateTableAlias(metaData.getName())),
                metaData.getName(),
                generateTableAlias(metaData.getName())
        );
    }

    @Override
    public String getSelectByIdSql() {
        return SELECT_BY_ID_QUERY_TEMPLATE.formatted(
                getSelectAllSql(),
                generateFieldsString(List.of(metaData.getIdField()), generateTableAlias(metaData.getName()))
        );
    }

    @Override
    public String getInsertSql() {
        return INSERT_QUERY_TEMPLATE.formatted(
                metaData.getName(),
                generateFieldsString(metaData.getFieldsWithoutId()),
                generateParametersSequence(metaData.getFieldsWithoutId())
        );
    }

    @Override
    public String getUpdateSql() {
        return UPDATE_QUERY_TEMPLATE.formatted(
                metaData.getName(),
                generateUpdatedFields(metaData.getFieldsWithoutId()),
                metaData.getIdField().getName()
        );
    }

    private @NotNull String generateUpdatedFields(@ NotNull final List<Field> fields) {
        return fields.stream()
                .map(f -> "%s = ?".formatted(f.getName()))
                .collect(Collectors.joining(","));
    }

    private @NotNull String generateParametersSequence(@NotNull final List<Field> fields) {
        return fields.stream()
                .map(f -> "?")
                .collect(Collectors.joining(","));
    }

    private @NotNull String generateFieldsString(@NotNull final List<Field> fields) {
        return fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(","));
    }

    private @NotNull String generateFieldsString(@NotNull final List<Field> fields, @NotNull final String tableAlias) {
        return fields.stream()
                .map(f -> "%s.%s".formatted(tableAlias, f.getName()))
                .collect(Collectors.joining(","));
    }

    private @NotNull String generateTableAlias(String name) {
        return name.substring(0, 1);
    }
}

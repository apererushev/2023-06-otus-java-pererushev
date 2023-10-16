package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Сохратяет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
@Slf4j
@RequiredArgsConstructor
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> metaData;

    @Override
    public Optional<T> findById(Connection connection, long id) {
        var sql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, sql, List.of(id), this::getEntity);
    }

    @Override
    public List<T> findAll(Connection connection) {
        var sql = entitySQLMetaData.getSelectAllSql();

        return dbExecutor.executeSelect(connection, sql, List.of(), this::getEntityList)
                .orElse(List.of());
    }

    @Override
    public long insert(Connection connection, T entity) {
        var sql = entitySQLMetaData.getInsertSql();

        return dbExecutor.executeStatement(connection, sql, getInsertParamsFromEntity(entity));
    }

    @Override
    @SneakyThrows//({InvocationTargetException.class, IllegalAccessException.class})
    public void update(Connection connection, T entity) {
        var id = getGetMethod(metaData.getIdField())
                .invoke(entity);

        if (Objects.isNull(id)) {
            throw new NullPointerException("Field marked as id (ru.otus.crm.annotation.@Id) must be set");
        }

        var sql = entitySQLMetaData.getUpdateSql();
        dbExecutor.executeStatement(connection, sql, getUpdateParamsFromEntity(entity));
    }

    @SneakyThrows
    private List<Object> getInsertParamsFromEntity(@NotNull final T entity) {
        var params = new ArrayList<>();
        for (var field : metaData.getFieldsWithoutId()) {
            params.add(getGetMethod(field).invoke(entity));
        }
        return params;
    }

    @SneakyThrows
    private List<Object> getUpdateParamsFromEntity(@NotNull final T entity) {
        var params = new ArrayList<>();
        params.addAll(getInsertParamsFromEntity(entity));
        params.add(getGetMethod(metaData.getIdField()).invoke(entity));
        return params;
    }

    @SneakyThrows
    private @NotNull Method getSetMethod(@NotNull final Field field) {
        var clazz = field.getDeclaringClass();
        return clazz.getMethod(generateAccessMethodName(field, AccessMethod.SET), field.getType());
    }

    @SneakyThrows
    private @NotNull Method getGetMethod(@NotNull final Field field) {
        var clazz = field.getDeclaringClass();
        return clazz.getMethod(generateAccessMethodName(field, AccessMethod.GET));
    }

    private @NotNull String generateAccessMethodName(@NotNull final Field field, @NotNull final AccessMethod accessMethod) {
        return accessMethod.name().toLowerCase()
                .concat(field.getName().substring(0, 1).toUpperCase())
                .concat(field.getName().substring(1));
    }

    private @Nullable T getEntity(@NotNull final ResultSet rs) {
        try {
            if (rs.next()) {
                return getEntityFromResultSet(rs);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private @NotNull List<T> getEntityList(@NotNull final ResultSet rs) {
        try {
            var entities = new ArrayList<T>();
            while (rs.next()) {
                entities.add(getEntityFromResultSet(rs));
            }
            return entities;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return List.of();
    }

    @SneakyThrows
    private @NotNull T getEntityFromResultSet(@NotNull final ResultSet rs) {
        var entity = metaData.getConstructor().newInstance();
        for (var field : metaData.getAllFields()) {
            getSetMethod(field).invoke(entity, rs.getObject(field.getName(), field.getType()));
        }
        return entity;
    }

    private enum AccessMethod {
        SET,
        GET
    }
}

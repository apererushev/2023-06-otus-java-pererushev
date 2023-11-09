package ru.otus.jdbc.mapper;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCollection;

@Testcontainers
class DataTemplateJdbcTest {

    @Container
    private final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("testDataBase")
            .withUsername("owner")
            .withPassword("secret")
            .withClasspathResourceMapping(
                    "00_initial_schema.sql",
                    "/docker-entrypoint-initdb.d/00_initial_schema.sql",
                    BindMode.READ_ONLY
            )
            .withClasspathResourceMapping(
                    "01_insertData.sql",
                    "/docker-entrypoint-initdb.d/01_insertData.sql",
                    BindMode.READ_ONLY
            );

    @ParameterizedTest
    @ArgumentsSource(FindByIdTestArgumentsSource.class)
    @SneakyThrows
    <T> void findById(Class<T> clazz, T exacted) {
        // given
        EntityClassMetaData<T> entityMetaData = new EntityClassMetaDataImpl<>(clazz);
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl<>(entityMetaData);
        var entityDataTemplateJdbc = new DataTemplateJdbc<>(new DbExecutorImpl(), sqlMetaData, entityMetaData);

        // when
        Optional<T> entity;
        try (var connection = makeConnection()) {
            entity = entityDataTemplateJdbc.findById(connection, 1);
        }

        // then
        assertThat(entity)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(exacted);
    }

    static class FindByIdTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(Client.class, new Client(1L, "Z")),
                    Arguments.of(Manager.class, new Manager(1L, "Zed", "Boss"))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(FindAllTestArgumentsSource.class)
    @SneakyThrows
    <T> void findAll(Class<T> clazz, T exacted) {
        // given
        EntityClassMetaData<T> entityMetaData = new EntityClassMetaDataImpl<>(clazz);
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl<>(entityMetaData);
        var entityDataTemplateJdbc = new DataTemplateJdbc<>(new DbExecutorImpl(), sqlMetaData, entityMetaData);

        // when
        List<T> clients = new ArrayList<>();
        try (var connection = makeConnection()) {
            clients.addAll(entityDataTemplateJdbc.findAll(connection));
        }

        // then
        assertThatCollection(clients)
                .usingRecursiveComparison()
                .isEqualTo(exacted);
    }

    static class FindAllTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(
                            Client.class,
                            List.of(
                                    new Client(1L, "Z"),
                                    new Client(2L, "J"),
                                    new Client(3L, "K")
                            )
                    ),
                    Arguments.of(Manager.class, List.of(new Manager(1L, "Zed", "Boss")))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(InsertTestArgumentsSource.class)
    @SneakyThrows
    <T> void insert(Class<T> clazz, T newEntity, T exacted) {
        // given
        EntityClassMetaData<T> entityMetaData = new EntityClassMetaDataImpl<>(clazz);
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl<>(entityMetaData);
        var entityDataTemplateJdbc = new DataTemplateJdbc<>(new DbExecutorImpl(), sqlMetaData, entityMetaData);

        // when
        Optional<T> insertedEntity;
        long newId;
        try (var connection = makeConnection()) {
            newId = entityDataTemplateJdbc.insert(connection, newEntity);

            insertedEntity = entityDataTemplateJdbc.findById(connection, newId);
        }

        // then
        assertThat(insertedEntity)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(exacted);
    }

    static class InsertTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(
                            Client.class,
                            new Client("D"),
                            new Client(4L, "D")
                    ),
                    Arguments.of(
                            Manager.class,
                            new Manager("J"),
                            new Manager(2L, "J", null)
                    )
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(UpdateTestArgumentsSource.class)
    @SneakyThrows
    <T> void update(Class<T> clazz, T exacted, long id) {
        // given
        EntityClassMetaData<T> entityMetaData = new EntityClassMetaDataImpl<>(clazz);
        EntitySQLMetaData sqlMetaData = new EntitySQLMetaDataImpl<>(entityMetaData);
        var entityDataTemplateJdbc = new DataTemplateJdbc<>(new DbExecutorImpl(), sqlMetaData, entityMetaData);

        // when
        Optional<T> updatedEntity;
        try (var connection = makeConnection()) {
            entityDataTemplateJdbc.update(connection, exacted);

            updatedEntity = entityDataTemplateJdbc.findById(connection, id);
        }

        // then
        assertThat(updatedEntity)
                .isNotEmpty()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(exacted);
    }

    static class UpdateTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(Client.class, new Client(2L, "L"), 2L),
                    Arguments.of(Manager.class, new Manager(1L, "J", "Big boss"), 1L)
            );
        }
    }

    @SneakyThrows
    private Connection makeConnection() {
        Connection connection =
                DriverManager.getConnection(postgresqlContainer.getJdbcUrl(), getConnectionProperties());
        connection.setAutoCommit(false);
        return connection;
    }

    private Properties getConnectionProperties() {
        Properties props = new Properties();
        props.setProperty("user", postgresqlContainer.getUsername());
        props.setProperty("password", postgresqlContainer.getPassword());
        props.setProperty("ssl", "false");
        return props;
    }
}
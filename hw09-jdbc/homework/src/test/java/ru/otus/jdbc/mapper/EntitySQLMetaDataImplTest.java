package ru.otus.jdbc.mapper;

import lombok.Getter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import ru.otus.crm.annotation.Id;
import ru.otus.crm.annotation.TableName;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EntitySQLMetaDataImplTest {

    @ParameterizedTest
    @ArgumentsSource(SelectTestArgumentsSource.class)
    <T> void getSelectAllSql(Class<T> clazz, String expectedSql) {
        // given
        var entitySQLMetaData = new EntitySQLMetaDataImpl<>(new EntityClassMetaDataImpl<>(clazz));

        // when
        var sql = entitySQLMetaData.getSelectAllSql();

        // then
        assertThat(sql)
                .isEqualTo(expectedSql);
    }

    static class SelectTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(TestClass1.class, "select s.id,s.name from super_test_class_one s"),
                    Arguments.of(TestClass2.class, "select t.code,t.foo,t.bar from testclass2 t")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(SelectByIdTestArgumentsSource.class)
    <T> void getSelectByIdSql(Class<T> clazz, String expectedSql) {
        // given
        var entitySQLMetaData = new EntitySQLMetaDataImpl<>(new EntityClassMetaDataImpl<>(clazz));

        // when
        var sql = entitySQLMetaData.getSelectByIdSql();

        // then
        assertThat(sql)
                .isEqualTo(expectedSql);
    }

    static class SelectByIdTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(TestClass1.class, "select s.id,s.name from super_test_class_one s where s.id = ?"),
                    Arguments.of(TestClass2.class, "select t.code,t.foo,t.bar from testclass2 t where t.code = ?")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(InsertTestArgumentsSource.class)
    <T> void getInsertSql(Class<T> clazz, String expectedSql) {
        // given
        var entitySQLMetaData = new EntitySQLMetaDataImpl<>(new EntityClassMetaDataImpl<>(clazz));

        // when
        var sql = entitySQLMetaData.getInsertSql();

        // then
        assertThat(sql)
                .isEqualTo(expectedSql);
    }

    static class InsertTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(TestClass1.class, "insert into super_test_class_one (name) values(?)"),
                    Arguments.of(TestClass2.class, "insert into testclass2 (foo,bar) values(?,?)")
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(UpdateTestArgumentsSource.class)
    <T> void getUpdateSql(Class<T> clazz, String expectedSql) {
        // given
        var entitySQLMetaData = new EntitySQLMetaDataImpl<>(new EntityClassMetaDataImpl<>(clazz));

        // when
        var sql = entitySQLMetaData.getUpdateSql();

        // then
        assertThat(sql)
                .isEqualTo(expectedSql);
    }

    static class UpdateTestArgumentsSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(TestClass1.class, "update super_test_class_one set name = ? where id = ?"),
                    Arguments.of(TestClass2.class, "update testclass2 set foo = ?,bar = ? where code = ?")
            );
        }
    }

    @TableName("super_test_class_one")
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

        private String foo;

        private String bar;
    }
}
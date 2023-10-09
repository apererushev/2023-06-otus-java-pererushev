package ru.otus.dataprocessor;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FileSerializerTest {

    @Test
    @SneakyThrows
    void serialize(@TempDir Path tempDir) {
        // given
        var outputFileName = "outputTest.json";
        var outputFullPath = "%s%s%s".formatted(tempDir, File.separator, outputFileName);
        var fileSerializer = new FileSerializer(outputFullPath);
        var dataToSave = Map.of(
                "val1", 3.0,
                "val2", 30.0,
                "val3", 33.0
        );
        var expected = "{\"val1\":3.0,\"val2\":30.0,\"val3\":33.0}";

        // when
        fileSerializer.serialize(dataToSave);

        // then
        var savedJson = Files.readString(Paths.get(outputFullPath));
        assertThat(savedJson).isEqualTo(expected);
    }
}
package ru.otus.dataprocessor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Measurement;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ResourcesFileLoaderTest {

    @Test
    void load() {
        // given
        var resourcesFileLoader = new ResourcesFileLoader("inputData.json");
        var expectedMeasurements = List.of(
                new Measurement("val1", 0.0),
                new Measurement("val1", 2.0),
                new Measurement("val2", 0.0),
                new Measurement("val2", 10.0),
                new Measurement("val2", 20.0),
                new Measurement("val1", 1.0),
                new Measurement("val3", 10.0),
                new Measurement("val3", 12.0),
                new Measurement("val3", 11.0)
        );

        // when
        var measurements = resourcesFileLoader.load();

        // then
        assertThatCollection(measurements)
                .containsExactlyInAnyOrderElementsOf(expectedMeasurements);
    }
}
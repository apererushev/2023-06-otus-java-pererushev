package ru.otus.dataprocessor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class ProcessorAggregatorTest {

    @Test
    void process() {
        // given
        var aggregator = new ProcessorAggregator();
        var expected = Map.of(
                "val1", 3.0,
                "val2", 30.0,
                "val3", 33.0
        );
        var measurements = List.of(
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
        var aggregatedData = aggregator.process(measurements);

        // then
        assertThat(aggregatedData)
                .containsExactlyInAnyOrderEntriesOf(expected);
    }
}
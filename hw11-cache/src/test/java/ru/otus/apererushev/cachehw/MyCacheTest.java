package ru.otus.apererushev.cachehw;

import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Проверка работы кеша")
class MyCacheTest {

    @Test
    void putAndGetTest() {
        // given
        HwCache<String, Integer> cache = new MyCache<>();

        // when
        cache.put("key_1", 1000);

        // then
        assertThat(cache.get("key_1"))
                .isEqualTo(1000);
    }

    @Test
    void putAndRemoveTest() {
        // given
        HwCache<String, Integer> cache = new MyCache<>();

        // when
        cache.put("key_1", 1000);
        assertThat(cache.get("key_1"))
                .isEqualTo(1000);
        cache.remove("key_1");

        // then
        assertThat(cache.get("key_1"))
                .isNull();
    }

    @Test
    void getNothingTest() {
        // given
        HwCache<String, Integer> cache = new MyCache<>();

        // when
        // do nothing

        // then
        assertThat(cache.get("key_200"))
                .isNull();
    }

    @Test
    void addListenerTest() {
        // given
        HwCache<String, Integer> cache = new MyCache<>();
        final var counter = new SimpleCounter();
        cache.addListener((key, value, action) -> counter.append());

        // when
        cache.put("key_1", 1000);
        cache.get("key_1");
        cache.remove("key_1");

        // then
        assertThat(counter.getCount())
                .isEqualTo(3);
    }

    @Test
    void removeListenerTest() {
        // given
        HwCache<String, Integer> cache = new MyCache<>();
        final var counter = new SimpleCounter();
        var listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                counter.append();
            }
        };
        cache.addListener(listener);

        // when
        cache.removeListener(listener);
        cache.put("key_1", 1000);
        cache.get("key_1");
        cache.remove("key_1");

        // then
        assertThat(counter.getCount())
                .isZero();
    }

    static class SimpleCounter {
        @Getter
        private int count = 0;

        public void append() {
            count++;
        }
    }
}
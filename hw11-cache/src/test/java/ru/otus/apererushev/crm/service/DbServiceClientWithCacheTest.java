package ru.otus.apererushev.crm.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.apererushev.base.AbstractHibernateTest;
import ru.otus.apererushev.crm.model.Address;
import ru.otus.apererushev.crm.model.Client;
import ru.otus.apererushev.crm.model.Phone;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DisplayName("Проверка работы сервиса по работе с клиентами с кешом")
class DbServiceClientWithCacheTest extends AbstractHibernateTest {

    @Test
    @DisplayName(" корректно сохранять, изменять и загружать клиента")
    void shouldCorrectSaveClient() {
        // given
        var client = new Client(
                null,
                "Vasya",
                new Address(null, "AnyStreet"),
                List.of(
                        new Phone(null, "13-555-22"),
                    new Phone(null, "14-666-333")
                )
        );

        // when
        var savedClient = dbServiceClientWithCache.saveClient(client);
        System.out.println(savedClient);

        // then
        var loadedSavedClient = dbServiceClientWithCache.getClient(savedClient.getId());
        assertThat(loadedSavedClient)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(savedClient);

        // when
        var savedClientUpdated = loadedSavedClient.get();
        savedClientUpdated.setName("updatedName");
        dbServiceClientWithCache.saveClient(savedClientUpdated);

        // then
        var loadedClient = dbServiceClientWithCache.getClient(savedClientUpdated.getId());
        assertThat(loadedClient).isPresent().get().usingRecursiveComparison().isEqualTo(savedClientUpdated);
        System.out.println(loadedClient);

        // when
        var clientList = dbServiceClientWithCache.findAll();

        // then
        assertThat(clientList).hasSize(1);
        assertThat(clientList.get(0)).usingRecursiveComparison().isEqualTo(loadedClient.get());
    }

    @Test
    @DisplayName(" проверяем, что с кешем быстрее")
    void checkCachePerformance() {
        // given
        // генерим в каждом сервисе по 1000 клиентов, потом читаем
        var clientsIdList = new ArrayList<Long>();
        for (var i = 0; i < 1000; i++) {
            var client = new Client();
            clientsIdList.add(dbServiceClientWithCache.saveClient(client).getId());
        }
        var dh = new DurationHelper();

        for (var id : clientsIdList) {
            dbServiceClientWithCache.getClient(id);
        }
        var getFromCacheDuration = dh.getNextNs();
        log.info("Get {} clients with cache by {} ns", clientsIdList.size(), getFromCacheDuration);

        // when
        for (var id : clientsIdList) {
            dbServiceClient.getClient(id);
        }
        var getFromDbDuration = dh.getNextNs();
        log.info("Get {} clients without cache by {} ns", clientsIdList.size(), getFromDbDuration);

        // then
        assertThat(getFromDbDuration)
                .isGreaterThan(getFromCacheDuration);
    }

    static class DurationHelper {

        private Long nanoTime;

        public DurationHelper() {
            this.nanoTime = System.nanoTime();
        }

        public long getNextNs() {
            var duration = (System.nanoTime() - nanoTime);
            nanoTime = System.nanoTime();
            return duration;
        }
    }
}
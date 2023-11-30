package ru.otus.apererushev.crm.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.otus.apererushev.cachehw.HwCache;
import ru.otus.apererushev.cachehw.MyCache;
import ru.otus.apererushev.core.repository.DataTemplate;
import ru.otus.apererushev.core.sessionmanager.TransactionManager;
import ru.otus.apererushev.crm.model.Client;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class DbServiceClientWithCacheImpl extends DbServiceClientImpl {

    private final HwCache<String, Client> clientCache = new MyCache<>();

    public DbServiceClientWithCacheImpl(@NonNull final TransactionManager transactionManager, @NonNull final DataTemplate<Client> clientDataTemplate) {
        super(transactionManager, clientDataTemplate);
    }

    @Override
    public Client saveClient(Client client) {
        var newClient = super.saveClient(client);
        putToCache(newClient);
        return newClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        var cachedClient = getFromCache(id);
        if (Objects.nonNull(cachedClient)) {
            log.info("Get client {} from cache", cachedClient);
            return Optional.of(cachedClient);
        }

        var client =  super.getClient(id);
        client.ifPresent(this::putToCache);
        log.info("Get client {} from db", client);
        return client;
    }

    @Override
    public List<Client> findAll() {
        var clients = super.findAll();
        clients.forEach(this::putToCache);
        return clients;
    }

    private void putToCache(@NonNull Client client) {
        clientCache.put(clientCacheKeyGenerator(client.getId()), client);
        log.info("Put client {} to cache", client);
    }

    private Client getFromCache(long id) {
        return clientCache.get(clientCacheKeyGenerator(id));
    }

    private @NonNull String clientCacheKeyGenerator(long id) {
        return "client_%s".formatted(id);
    }
}

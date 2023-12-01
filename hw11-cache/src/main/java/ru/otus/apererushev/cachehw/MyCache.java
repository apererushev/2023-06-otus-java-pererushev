package ru.otus.apererushev.cachehw;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {

    private final WeakHashMap<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();
    private final ReferenceQueue<HwListener<K, V>> referenceQueue = new ReferenceQueue<>();

    public MyCache() {
        final var referenceHandler = new ReferenceHandler<HwListener<K, V>>(referenceQueue, this::refRemover);
        referenceHandler.start();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        callListeners(key, value, Action.PUT);
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);
        callListeners(key, value, Action.REMOVE);
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        callListeners(key, value, Action.GET);
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener, referenceQueue));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        for (var wealListener : listeners) {
            var l = wealListener.get();
            if (Objects.nonNull(l) && l.equals(listener)) {
                listeners.remove(wealListener);
                break;
            }
        }
    }

    private void callListeners(K key, V value, Action action) {
        listeners.stream()
                .map(WeakReference::get)
                .filter(Objects::nonNull)
                .forEach(l -> callListener(l, key, value, action));
    }

    private void callListener(HwListener<K, V> listener, K key, V value, Action action) {
        try {
            listener.notify(key, value, action.name());
        } catch (Exception e) {
            log.error(e.getMessage());
            log.debug(e.getMessage(), e);
        }
    }

    @SuppressWarnings("All")
    private void refRemover(@NonNull final Reference<? extends HwListener<K, V>> reference) {
        listeners.remove(reference);
    }

    private enum Action {
        PUT,
        REMOVE,
        GET
    }
}

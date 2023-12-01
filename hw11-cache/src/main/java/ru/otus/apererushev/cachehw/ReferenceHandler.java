package ru.otus.apererushev.cachehw;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.function.Consumer;

@Slf4j
public class ReferenceHandler<V> extends Thread {
    private final ReferenceQueue<V> referenceQueue;

    @Setter
    @Getter
    private Consumer<Reference<? extends V>> onCollected;

    public ReferenceHandler(@NonNull final ReferenceQueue<V> referenceQueue, @NonNull final Consumer<Reference<? extends V>> onCollected) {
        this.referenceQueue = referenceQueue;
        this.onCollected = onCollected;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Reference<? extends V> reference = referenceQueue.remove();
                log.info("Collect: {}", reference);
                onCollected.accept(reference);
                log.info("Run: {}", onCollected);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                log.debug(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
}

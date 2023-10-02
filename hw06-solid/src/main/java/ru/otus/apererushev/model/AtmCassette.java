package ru.otus.apererushev.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class AtmCassette {

    private final Integer nominal;

    private Integer count;

    public Boolean poll(@NotNull final Integer count) {
        if (this.count.compareTo(count) >= 0) {
            this.count -= count;
            return true;
        } else {
            return false;
        }
    }
}

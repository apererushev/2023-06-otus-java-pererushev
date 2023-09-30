package ru.otus.apererushev.busneslogic;

import com.beust.jcommander.ParameterException;
import org.jetbrains.annotations.NotNull;
import ru.otus.apererushev.logger.Log;

import java.util.Arrays;

public class CheckByFortyTwo implements VeryImportantLogic {

    private static final Integer FORTY_TWO = 42;

    @Log // Это работать не будет, т.к. метод не описан в интерфейсе
    public @NotNull Boolean sum(@NotNull final Integer... numbers) {
        return FORTY_TWO.equals(doSum(numbers));
    }

    @Log // Это работать не будет, т.к. метод не описан в интерфейсе
    private @NotNull Integer doSum(@NotNull final Integer... numbers) {
        var sum = 0;
        for (var n : numbers) {
            sum += n;
        }

        return sum;
    }

    @Log
    @Override
    public @NotNull Boolean isItTrue(@NotNull final Object... params) {
        validateParameters(params);

        var intParams = Arrays.stream(params)
                .map(p -> (Integer)p)
                .toArray(Integer[]::new);

        return sum(intParams);
    }

    private void validateParameters(@NotNull final Object... params) {
        if (params.length == 0) {
            throw new ParameterException("You must pass at least one parameter.");
        }

        for (var param : params) {
            if (!(param instanceof Integer)) {
                throw new ParameterException("All parameters must be java.lang.Integer.");
            }
        }
    }
}

package ru.otus.apererushev.loggedlist;

import ru.otus.apererushev.logger.Log;

import java.util.ArrayList;

public class LoggedList<T> extends ArrayList<T> {

    @Log
    @Override
    public boolean add(T o) {
        return super.add(o);
    }

    @Log
    @Override
    public void add(int index, T element) {
        super.add(index, element);
    }

    @Log
    @Override
    public void clear() {
        super.clear();
    }
}

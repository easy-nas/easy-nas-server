package com.easynas.server.model;

import java.util.Map;

/**
 * 不可变的Pair
 *
 * @param <T> key
 * @param <U> value
 * @author liangyongrui
 */
public class Pair<T, U> implements Map.Entry<T, U> {

    private final T key;
    private final U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public T getKey() {
        return key;
    }

    @Override
    public U getValue() {
        return value;
    }

    @Override
    public U setValue(U value) {
        throw new UnsupportedOperationException("这是一个不可变的Pair");
    }
}

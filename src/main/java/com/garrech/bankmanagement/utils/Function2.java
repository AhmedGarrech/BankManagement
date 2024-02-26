package com.garrech.bankmanagement.utils;

@FunctionalInterface
public interface Function2<T, S, R> {

    R apply(T argument1, S argument2);
}

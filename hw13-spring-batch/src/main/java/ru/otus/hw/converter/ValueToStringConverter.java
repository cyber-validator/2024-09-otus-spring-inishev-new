package ru.otus.hw.converter;

public abstract class ValueToStringConverter {

    public String stringOf(Object value) {
        return String.valueOf(value);
    }

}

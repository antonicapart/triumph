package com.antonicapart.triumph.database.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

public abstract class Model implements Serializable {

    public Object get(final String name) {
        try {
            final Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(this);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean has(final String name) {
        return Arrays.stream(this.getClass().getDeclaredFields())
                .map(Field::getName)
                .filter(f -> f.equals(name))
                .count() == 1;
    }

    public boolean set(final String name, final Object value) {
        try {
            final Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(this, value);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}

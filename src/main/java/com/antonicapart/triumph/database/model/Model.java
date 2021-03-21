package com.antonicapart.triumph.database.model;

import com.antonicapart.triumph.database.creator.types.TypeTypes;
import com.antonicapart.triumph.database.model.annotations.Column;
import com.antonicapart.triumph.database.model.annotations.Key;
import com.antonicapart.triumph.database.model.annotations.Relation;
import com.antonicapart.triumph.errors.creator.AttributeNeededError;
import com.antonicapart.triumph.errors.creator.TypeNotMatchingError;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;

public abstract class Model implements Serializable
{
    private final Set<String> keys;
    private final Map<String, SimpleEntry<Type, Object>> fields;
    private final Map<String, SimpleEntry<TypeTypes, String>> fields2;
    private final Map<String, Class<? extends Model>> relations;

    public Model ()
    {
        this.keys = new LinkedHashSet<>();
        this.fields = new LinkedHashMap<>();
        this.fields2 = new LinkedHashMap<>();

        this.relations = new LinkedHashMap<>();

        // Setup
        final Field[] attributes = this.getClass().getDeclaredFields();

        for (Field attribute : attributes) {
            if (attribute.isAnnotationPresent(Key.class)) {
                this.keys.add(attribute.getName());
            }

            if (attribute.isAnnotationPresent(Column.class)) {
                this.fields.put(attribute.getName(), new SimpleEntry<>(attribute.getType(), null));

                Column column = attribute.getAnnotation(Column.class);

                TypeTypes typeTypes = TypeTypes.getTypeFromType(attribute.getType());

                if (typeTypes.equals(TypeTypes.NOT_DEFINE))
                    throw new TypeNotMatchingError(attribute, attribute.getType(), null);

                if (column.type().asArg() && column.args().isEmpty())
                    throw new AttributeNeededError(attribute, column.type());

                System.out.println(column.type() + " / " + column.type().asArg() + " : " + column.args());

                this.fields2.put(attribute.getName(), new SimpleEntry<>(typeTypes, typeTypes.asArg() ? column.args() : null));
            }

            if (attribute.isAnnotationPresent(Relation.class)) {
                this.relations.put(attribute.getName(), attribute.getAnnotation(Relation.class).model());
            }
        }
    }

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

    // Check if field is a key
    public boolean isKey (final String name)
    {
        return this.keys.contains(name);
    }

    public Set<String> getKeys() {
        return keys;
    }

    public Map<String, SimpleEntry<Type, Object>> getFields() {
        return fields;
    }

    public Map<String, SimpleEntry<TypeTypes, String>> getFields2() {
        return fields2;
    }

    public Map<String, Class<? extends Model>> getRelations() {
        return relations;
    }

    @Override
    public String toString()
    {
        final Collection<String> fields = new LinkedList<>();

        for (Map.Entry<String, SimpleEntry<Type, Object>> field : this.fields.entrySet()) {
            fields.add(new StringBuilder(field.getKey())
                    .append(" = ")
                    .append(field.getValue().getValue())
                    .toString()
            );
        }

        final StringBuilder instance = new StringBuilder(this.getClass().getName());
        instance.append(" { ");
        instance.append(String.join(", ", fields));
        instance.append(" } ");

        return instance.toString();
    }
}


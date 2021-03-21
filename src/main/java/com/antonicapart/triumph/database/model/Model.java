package com.antonicapart.triumph.database.model;

import com.antonicapart.triumph.database.creator.types.TypeTypes;
import com.antonicapart.triumph.database.model.annotations.Column;
import com.antonicapart.triumph.database.model.annotations.Key;
import com.antonicapart.triumph.database.model.annotations.Nullable;
import com.antonicapart.triumph.database.model.annotations.Relation;
import com.antonicapart.triumph.errors.creator.AttributeNeededError;
import com.antonicapart.triumph.errors.creator.RelationFieldsNotExist;
import com.antonicapart.triumph.errors.creator.RelationFieldsNotMatching;
import com.antonicapart.triumph.errors.creator.TypeNotMatchingError;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.stream.Collectors;

public abstract class Model implements Serializable
{
    private final Set<String> keys;
    private final Map<String, SimpleEntry<Type, Object>> fields;
    private final Map<String, SimpleEntry<TypeTypes, String>> fields2;
    private final Map<String, Boolean> nullableFields;
    private final Map<String, SimpleEntry<Class<? extends Model>, String>> relations;

    public Model ()
    {
        this.keys = new LinkedHashSet<>();
        this.fields = new LinkedHashMap<>();
        this.fields2 = new LinkedHashMap<>();
        this.nullableFields = new LinkedHashMap<>();

        this.relations = new LinkedHashMap<>();

        // Setup
        final Field[] attributes = this.getClass().getDeclaredFields();

        for (Field attribute : attributes) {
            if (attribute.isAnnotationPresent(Key.class)) {
                this.keys.add(attribute.getName());
                this.nullableFields.put(attribute.getName(), false);
            }

            if (attribute.isAnnotationPresent(Nullable.class)) {
                this.nullableFields.put(attribute.getName(), attribute.getAnnotation(Nullable.class).isNullable());
            }

            if (attribute.isAnnotationPresent(Column.class)) {
                this.fields.put(attribute.getName(), new SimpleEntry<>(attribute.getType(), null));

                Column column = attribute.getAnnotation(Column.class);

                TypeTypes currentType = column.type();
                if (currentType.equals(TypeTypes.NOT_DEFINE)) {
                    currentType = TypeTypes.getTypeFromType(attribute.getType());

                    if (currentType.equals(TypeTypes.NOT_DEFINE))
                        throw new TypeNotMatchingError(attribute, attribute.getType(), currentType);
                }
                else {
                    if (!currentType.equals(TypeTypes.getTypeFromType(attribute.getType())))
                        throw new TypeNotMatchingError(attribute, attribute.getType(), currentType);

                }

                if (currentType.asArg() && column.args().isEmpty())
                    throw new AttributeNeededError(attribute, currentType);

                this.fields2.put(attribute.getName(), new SimpleEntry<>(currentType, currentType.asArg() ? column.args() : null));
            }

            if (attribute.isAnnotationPresent(Relation.class)) {
                Relation relation =  attribute.getAnnotation(Relation.class);
                Class<? extends Model> relatedClass = relation.model();
                String relatedField = relation.relatedField();

                if (relatedField.isEmpty()) {
                    for (Field declaredField : relatedClass.getDeclaredFields()) {
                        if (declaredField.getName().contains(attribute.getDeclaringClass().getSimpleName()))
                            relatedField = declaredField.getName();
                    }
                    if (relatedField.isEmpty()) {
                        throw new RelationFieldsNotExist(attribute, relatedClass);
                    }
                }

                if (Arrays.stream(relatedClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList()).contains(relatedField)){
                    try {
                        if (!(Arrays.stream(((ParameterizedType) relatedClass.getDeclaredField(relatedField).getGenericType()).getActualTypeArguments()).findFirst().get()).equals(attribute.getDeclaringClass()))
                            throw new RelationFieldsNotMatching(attribute, relatedClass, relatedField);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                else throw new RelationFieldsNotExist(attribute, relatedClass);

                this.relations.put(attribute.getName(), new SimpleEntry<>(relatedClass, relatedField));
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

    public Map<String, Boolean> getNullableFields() {
        return nullableFields;
    }

    public Map<String, SimpleEntry<Class<? extends Model>, String>> getRelations() {
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


package com.antonicapart.triumph.database.creator;

import com.antonicapart.triumph.database.model.Model;
import com.antonicapart.triumph.database.model.annotations.Table;
import com.antonicapart.triumph.errors.creator.ModelNotExtendsError;
import com.antonicapart.triumph.database.creator.types.TableTypes;
import com.antonicapart.triumph.support.Str;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Creator {
    private static Creator instance;

    private static String packageName = "model";

    public static Creator getInstance() {
        if (instance ==  null)
           instance = new Creator();
        return instance;
    }

    private Creator() {
        this.mapTable = new LinkedHashMap<>();
    }

    private Map<Class<? extends Model>, String> mapTable;

    public Set<Class<? extends Model>> getModel() {
        Reflections reflections2 = new Reflections(
                packageName,  new SubTypesScanner(), new TypeAnnotationsScanner());
        Set<Class<?>> classExtendsModel =  reflections2.getTypesAnnotatedWith(Table.class);
        classExtendsModel.forEach(aClass -> {
            if (!aClass.getSuperclass().equals(Model.class)) {
                throw new ModelNotExtendsError(aClass);
            }
            String tableName =  aClass.getAnnotation(Table.class).name();
            mapTable.put((Class<? extends Model>) aClass, tableName.equals("") ? aClass.getSimpleName() : tableName);
        });

        TreeMap<Class<? extends Model>, String> buildingTree = new TreeMap<>();

        mapTable.forEach((k, v) -> {
            try {
                int h = 0;
                Class<? extends Model> parent = null;
                Model m = constructClass(k);
                m.getRelations().forEach((k2, v2) -> {
                    if (buildingTree.containsKey(k2));
                       // TODO Build, construction tree
                });
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        });

        mapTable.forEach((k,v) -> System.out.println(createTable(k,v)));
        return null;
    }

    private String createTable(Class<? extends Model> aClass, String tableName) {
        StringBuilder out = new StringBuilder();
        out.append(TableTypes.DELETE_TABLE.getValue()).append(" ")
                .append(TableTypes.IF_EXIST.getValue()).append(" ")
                .append(tableName).append(";");

        out.append("\n");

        out.append(TableTypes.CREATE_TABLE.getValue()).append(" ")
                .append(tableName).append(" (");

        try {
            Model model = constructClass(aClass);

            out.append("\n\t").append(model.getFields2().entrySet().stream().map((entry) -> {
                return Str.toSnake(entry.getKey()) + " " + entry.getValue().getKey() + (entry.getValue().getKey().asArg() ? " (" + entry.getValue().getValue() + ")" : "" + (model.getNullableFields().containsKey(entry.getKey()) ? " " + (model.getNullableFields().get(entry.getKey()) ? TableTypes.NULL.getValue() : TableTypes.NOT_NULL.getValue()) : ""));
            }).collect(Collectors.joining(",\n\t")));

            out.append(",\n\t").append(TableTypes.PRIMARY_KEY.getValue()).append("(").append(String.join(", ", model.getKeys())).append(")");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        out.append(" \n);");
        return out.toString();
    }

    private Model constructClass(Class<? extends Model> aClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return (Model) Arrays.stream(aClass.getDeclaredConstructors()).filter(constructor -> constructor.getParameterCount() == 0).findFirst().get().newInstance();
    }
}

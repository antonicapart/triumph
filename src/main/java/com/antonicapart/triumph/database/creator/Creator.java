package com.antonicapart.triumph.database.creator;

import com.antonicapart.triumph.database.model.Model;
import com.antonicapart.triumph.database.model.annotations.Table;
import com.antonicapart.triumph.errors.creator.ModelNotExtendsError;
import com.antonicapart.triumph.database.creator.types.TableTypes;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
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

        mapTable.forEach((k,v) -> System.out.println(createTable(k,v)));
        return null;
    }

    private String createTable(Class<? extends Model> aClass, String tableName) {
        StringBuilder out = new StringBuilder();
        out.append(TableTypes.DELETE_TABLE.getValue()).append(" ")
                .append(TableTypes.IF_EXIST.getValue()).append(" ")
                .append(tableName);

        out.append("\n");

        out.append(TableTypes.CREATE_TABLE.getValue()).append(" ")
                .append(tableName).append(" (");

        try {
            Model model = (Model) Arrays.stream(aClass.getDeclaredConstructors()).filter(constructor -> constructor.getParameterCount() == 0).findFirst().get().newInstance();

            out.append("\n\t").append(model.getFields2().entrySet().stream().map((entry) -> {
                return entry.getKey() + " " + entry.getValue().getKey() + (entry.getValue().getKey().asArg() ? " (" + entry.getValue().getValue() + ")" : "");
            }).collect(Collectors.joining(",\n\t")));

            out.append(",\n\t").append(TableTypes.PRIMARY_KEY).append("(").append(String.join(", ", model.getKeys())).append(")");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        out.append(" \n);");
        return out.toString();
    }
}

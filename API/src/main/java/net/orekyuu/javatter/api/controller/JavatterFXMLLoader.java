package net.orekyuu.javatter.api.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.util.lookup.Lookup;

import java.lang.reflect.Field;
import java.net.URL;

public class JavatterFXMLLoader extends FXMLLoader {

    private Stage ownerStage;

    public JavatterFXMLLoader() {
        setControllerFactory(this::createController);
    }

    public JavatterFXMLLoader(URL url) {
        super(url);
        setControllerFactory(this::createController);
    }

    public void setOwnerStage(Stage stage) {
        ownerStage = stage;
    }

    private Object createController(Class<?> aClass) {
        Object result = null;
        try {
            result = aClass.getConstructor().newInstance();
            Lookup.inject(result);
            injectOwnerStage(aClass, result);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void injectOwnerStage(Class<?> aClass, Object instance) throws IllegalAccessException {
        for (Field field : aClass.getDeclaredFields()) {
            if (field.getAnnotation(OwnerStage.class) == null) {
                continue;
            }
            if (!field.getType().isAssignableFrom(Stage.class)) {
                throw new ClassCastException(field.getName());
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(instance, ownerStage);
        }
    }
}

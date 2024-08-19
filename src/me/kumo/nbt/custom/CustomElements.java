package me.kumo.nbt.custom;

import com.github.weisj.darklaf.util.Pair;
import net.querz.nbt.io.NamedTag;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

public class CustomElements {
    private static final Logger logger = LoggerFactory.getLogger(CustomElements.class);

    public static ArrayList<Pair<String, Component>> find(String filename, String nodePath, NamedTag tag) {
        ArrayList<Pair<String, Component>> components = new ArrayList<>();
        Reflections reflections =
                new Reflections(new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage("me.kumo.nbt.custom"))
                        .setScanners(Scanners.MethodsAnnotated));
        Set<Method> methods = reflections.getMethodsAnnotatedWith(CustomTagElement.class);
        logger.debug("checking methods {}", methods.toString());
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.isAnnotationPresent(CustomTagElement.class)) {
                CustomTagElement annotation = method.getAnnotation(CustomTagElement.class);
                String filePattern = annotation.file();
                String pathPattern = annotation.path();
                if ((filename == null || Pattern.matches(filePattern, filename)) &&
                        Pattern.matches(pathPattern, nodePath)) {
                    logger.debug("calling method {}", method.getName());
                    try {
                        method.setAccessible(true);
                        Component result = (Component) method.invoke(null, tag); // Static method invocation with null instance
                        components.add(new Pair<>(annotation.title(), result));
                    } catch (Exception e) {
                        components.add(new Pair<>(annotation.title(), new JTextArea("Failed!\n" + e.getMessage()) {{
                            setEditable(false);
                        }}));
                    }
                }
            }
        }
        return components;
    }
}

package me.kumo.nbt.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomTagElement {
    String file() default ".*";  // Regex filter for file names

    String path() default ".*";  // Regex filter for tree paths

    String title() default "Node";
}
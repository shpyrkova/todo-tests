package com.todo.annotations;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO чтобы аннотация работала, надо поработать с порядком аннотаций. либо объединить их в одном Extension
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(MobileExecutionExtension.class)
public @interface Mobile {
}

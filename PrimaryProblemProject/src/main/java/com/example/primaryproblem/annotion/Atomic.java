package com.example.primaryproblem.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pzw
 * @data 2022/5/22
 * @apiNote 标注了该注解的方法都会有原子性
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Atomic {
    
}

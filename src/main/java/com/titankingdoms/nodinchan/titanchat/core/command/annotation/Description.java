package com.titankingdoms.nodinchan.titanchat.core.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
	
	String value() default "";
}

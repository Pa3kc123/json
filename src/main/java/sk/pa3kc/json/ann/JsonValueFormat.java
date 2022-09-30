package sk.pa3kc.json.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonValueFormat {
    String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
    String value() default DEFAULT_FORMAT;
}

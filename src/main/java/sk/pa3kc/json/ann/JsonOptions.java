package sk.pa3kc.json.ann;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonOptions {
    @Deprecated
    boolean ignoreMissing() default false;
    boolean strict() default false;
    boolean useFieldNameAsKey() default true;
}

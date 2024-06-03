package common.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Annotation for database operations.
 * For now only decorative. Useful to identify methods that interact with the database.
 * In the future, this annotation will be used to track response times of database operations.
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.METHOD)
public @interface DatabaseOperation {
}

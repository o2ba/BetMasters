package common.annotation;

/**
 * Annotation to mark a method as rate limited.
 * The rate limit is based on the IP address of the client.
 * To be implemented in the future.
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface RateLimited {
    /**
     * The maximum number of requests allowed in the time window.
     */
    int value() default 10;

    /**
     * The time window in seconds.
     */
    int timeWindow() default 60;
}

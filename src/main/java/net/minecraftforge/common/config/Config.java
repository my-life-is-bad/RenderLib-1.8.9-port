package net.minecraftforge.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Stub of the Forge 1.12+ @Config API for backward compatibility with 1.8.9.
 * Only includes minimal definitions used by tools like ConfigUtil.
 */
public @interface Config {
    String modid() default "";
    String category() default "general";
    String name() default "";
    boolean requireMcRestart() default false;
    boolean requireWorldRestart() default false;
    String comment() default "";

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        String value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Comment {
        String[] value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RangeInt {
        int min();
        int max();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RangeDouble {
        double min();
        double max();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ignore {
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface RequiresMcRestart {
    }
}

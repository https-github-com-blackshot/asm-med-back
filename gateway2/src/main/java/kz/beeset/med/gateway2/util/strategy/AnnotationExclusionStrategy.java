package kz.beeset.med.gateway2.util.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import kz.beeset.med.gateway2.util.annotation.Exclude;

public class AnnotationExclusionStrategy implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}

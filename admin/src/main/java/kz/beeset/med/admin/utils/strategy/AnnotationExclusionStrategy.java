package kz.beeset.med.admin.utils.strategy;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import kz.beeset.med.admin.utils.annotation.Exclude;
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

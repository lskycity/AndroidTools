package com.lskycity.androidtools.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author zhaofliu
 * @since 9/7/18.
 */
public class ClassUtils {
    public static void fetchClassInfo(StringBuilder sb, Class<?> build) {
        Field[] fields = build.getDeclaredFields();
        for(Field fd : fields) {
            int mode = fd.getModifiers();
            if(Modifier.isPublic(mode) && Modifier.isStatic(mode)) {
                sb.append(fd.getName());
                sb.append('=');
                try {
                    Object value = fd.get(null);
                    if(value instanceof Object[]) {
                        sb.append(Arrays.toString((Object[]) value));
                    } else {
                        sb.append(value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                sb.append('\n');
            }
        }
    }
}

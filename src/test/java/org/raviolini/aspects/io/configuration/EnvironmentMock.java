package org.raviolini.aspects.io.configuration;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public class EnvironmentMock {
    
    @SuppressWarnings("rawtypes")
    public static void setEnv(Map<String, String> newEnv) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class[] classes = Collections.class.getDeclaredClasses();
        Map<String, String> oldEnv = System.getenv();
        
        for (Class clazz : classes) {
            if ("java.util.Collections$UnmodifiableMap".equals(clazz.getName())) {
                replaceEnv(newEnv, oldEnv, clazz);
            }
        }
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void replaceEnv(Map<String, String> newEnv, Map<String, String> oldEnv, Class clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField("m");
        field.setAccessible(true);
        
        Object obj = field.get(oldEnv);
        
        Map<String, String> map = (Map<String, String>) obj;
        map.clear();
        map.putAll(newEnv);
    }
}
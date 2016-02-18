package org.raviolini.aspects.io.configuration;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EnvironmentMock {
    
    private Map<String, String> originalEnv;
    
    @SuppressWarnings("rawtypes")
    public void set(Map<String, String> newEnv) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class[] classes = Collections.class.getDeclaredClasses();
        Map<String, String> oldEnv = System.getenv();
        
        for (Class clazz : classes) {
            if ("java.util.Collections$UnmodifiableMap".equals(clazz.getName())) {
                replace(newEnv, oldEnv, clazz);
            }
        }
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void replace(Map<String, String> newEnv, Map<String, String> oldEnv, Class clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField("m");
        field.setAccessible(true);
        
        Object obj = field.get(oldEnv);
        
        Map<String, String> map = (Map<String, String>) obj;
        originalEnv = new HashMap<>(map);
        map.clear();
        map.putAll(newEnv);
    }
    
    public void reset() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        if (originalEnv != null) {
            set(originalEnv);
            originalEnv = null;
        }
    }
}
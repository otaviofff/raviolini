package org.raviolini.domain.dog;

import java.util.List;

import org.raviolini.api.exception.InternalServerException;

public class EntityService {

    //TODO: Implement caching.
    
    private static EntityRepository getRepository() {
        return new EntityRepository();
    }
    
    public static List<Dog> get() throws InternalServerException {
        return getRepository().select();
    }
    
    public static Dog get(Integer id) throws InternalServerException {
        return getRepository().select(id);
    }
    
    public static void post(Dog entity) throws InternalServerException {
        getRepository().insert(entity);
    }
    
    public static void put(Dog entity) throws InternalServerException {
        getRepository().update(entity);
    }
    
    public static void delete(Integer id) throws InternalServerException {
        getRepository().delete(id);
    }
}
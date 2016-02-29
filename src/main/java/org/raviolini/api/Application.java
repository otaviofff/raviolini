package org.raviolini.api;

import static spark.SparkBase.port;

import org.raviolini.domain.Entity;
import org.raviolini.facade.EntityService;

public class Application {

    public int listenToAssignedPort() {
        ProcessBuilder builder = new ProcessBuilder();
        String value = builder.environment().get("PORT");
        Integer port = 4567; //Default port set by Spark.
        
        if (value != null) {
            port = Integer.valueOf(value);
        }
        
        port(port);
        
        return port;
    }
    
    public <T extends Entity> void addRouter(Class<T> entityClass, EntityService<T> entityService) {
        RequestRouter<T> router = new RequestRouter<>();
        
        router.route(entityClass);
        
        if (entityService != null) {
            router.override(entityService);
        }
    }
    
    public <T extends Entity> void addRouter(Class<T> entityClass) {
        addRouter(entityClass, null);
    }
}
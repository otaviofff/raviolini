package org.muttie.api;

import org.muttie.domain.dog.Dog;
import org.raviolini.api.FrontRouter;

public class FrontController {

    public static void main(String[] args) {
        FrontRouter<Dog> router = new FrontRouter<>();
        
        router.route(Dog.class);
    }
}
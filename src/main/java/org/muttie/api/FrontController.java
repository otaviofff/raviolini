package org.muttie.api;

import org.muttie.domain.dog.Dog;
import org.muttie.domain.dog.Organization;
import org.raviolini.api.FrontRouter;

public class FrontController {

    public static void main(String[] args) {
        FrontRouter<Dog> router1 = new FrontRouter<>();
        FrontRouter<Organization> router2 = new FrontRouter<>();
        
        router1.route(Dog.class);
        router2.route(Organization.class);
    }
}
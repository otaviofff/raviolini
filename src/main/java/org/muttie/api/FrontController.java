package org.muttie.api;

import org.muttie.domain.Dog;
import org.muttie.domain.Organization;
import org.raviolini.api.RequestRouter;

public class FrontController {

    public static void main(String[] args) {
        RequestRouter<Dog> router1 = new RequestRouter<>();
        RequestRouter<Organization> router2 = new RequestRouter<>();
        
        router1.route(Dog.class);
        router2.route(Organization.class);
    }
}
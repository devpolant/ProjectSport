package com.polant.projectsport.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by Антон on 19.10.2015.
 */
public class BusProvider {

    private static final Bus bus = new Bus();

    public static Bus getInstance(){
        return bus;
    }

}

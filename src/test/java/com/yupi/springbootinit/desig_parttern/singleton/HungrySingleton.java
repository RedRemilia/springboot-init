package com.yupi.springbootinit.desig_parttern.singleton;

import lombok.Getter;

public class HungrySingleton {

    @Getter
    private static final HungrySingleton instance = new HungrySingleton();

    private HungrySingleton() {}

}

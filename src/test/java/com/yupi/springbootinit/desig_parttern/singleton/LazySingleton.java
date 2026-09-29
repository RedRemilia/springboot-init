package com.yupi.springbootinit.desig_parttern.singleton;

public class LazySingleton {

    private static volatile LazySingleton instance;

    private LazySingleton() {}

    public static synchronized LazySingleton getInstance(){
        if (instance == null){
            instance = new LazySingleton();
        }
        return instance;
    }
}

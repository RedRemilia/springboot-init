package com.yupi.springbootinit.desig_parttern.singleton;

public class DoubleCheckLockSingleton {

    private static volatile DoubleCheckLockSingleton instance = null;

    private DoubleCheckLockSingleton() {}

    public static DoubleCheckLockSingleton getInstance() {
        if (instance == null){
            synchronized (DoubleCheckLockSingleton.class) {
                if (instance == null){
                    instance = new DoubleCheckLockSingleton();
                }
            }
        }
        return instance;
    }


}

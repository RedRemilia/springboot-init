package com.yupi.springbootinit.desig_parttern.singleton;

public enum EnumSingleton {
    INSTANCE;

    public void doSomething() {
        System.out.println("enum singleton doSomething");
    }
}

package com.yupi.springbootinit.desig_parttern.observer;

public class ConcretObserver implements Observer {

    @Override
    public void update(String msg) {
        System.out.println("ConcretObserver.update");
    }
}

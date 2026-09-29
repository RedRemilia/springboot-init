package com.yupi.springbootinit.desig_parttern.observer;

import java.util.ArrayList;
import java.util.List;

public class ConcretSubject implements ISubject {

    private final List<Observer> observers = new ArrayList<>();

    public ConcretSubject() {}

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String msg) {
        observers.forEach(observer -> observer.update(msg));
    }
}

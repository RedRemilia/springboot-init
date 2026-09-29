package com.yupi.springbootinit.desig_parttern.observer;

public interface ISubject {

    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String msg);

}

package com.yupi.springbootinit.desig_parttern.proxy;

public class ProxyTest {

    public static void main(String[] args) {
        Purchase purchase = new Purchase();
        ISubject proxy = (ISubject) ProxyFactory.getDynamicProxy(purchase);
        proxy.doSomething();

        Speak speak = new Speak();
        ISubject proxy2 = (ISubject) ProxyFactory.getDynamicProxy(speak);
        proxy2.doSomething();
    }
}

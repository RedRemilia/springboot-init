package com.yupi.springbootinit.desig_parttern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    public static Object getDynamicProxy(Object target) {
        InvocationHandler invocationHandler = new DynamicProxy(target);
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                invocationHandler
        );
    }

}

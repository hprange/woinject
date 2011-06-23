package com.woinject;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class Interceptor {
    private static Injector injector;

    public static Injector injector() {
	if (injector == null) {
	    injector = Guice.createInjector(new WOInjectModule());
	}

	return injector;
    }

    // public static Object intercept(Class<?> clazz, Object[] parameters) {
    // System.out.println("\tClass: " + clazz + " Parameters: " +
    // java.util.Arrays.toString(parameters));
    //
    // if (WOSession.class.isAssignableFrom(clazz)) {
    // System.out.println("Creating an instace of " + clazz.getName());
    //
    // Object instance = injector().getInstance(clazz);
    //
    // return instance;
    // }
    //
    // if (EOEnterpriseObject.class.isAssignableFrom(clazz)) {
    // System.out.println("Creating an instace of " + clazz.getName());
    //
    // Object instance = injector().getInstance(clazz);
    //
    // return instance;
    // }
    //
    // return null;
    // }
}

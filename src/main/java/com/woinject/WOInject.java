/**
 * Copyright (C) 2010 hprange <hprange@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.woinject;

import java.lang.reflect.Method;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

/**
 * The <code>WOInject</code> class initializes the application intercepting core
 * methods used by WebObjects classes to create objects.
 * <p>
 * How to use:
 * 
 * <pre>
 * public static void main(String[] argv) {
 *     WOInject.init(&quot;com.company.app.Application&quot;, argv);
 * }
 * </pre>
 * <p>
 * <strong>Note</strong>: to ensure well functioning, do not obtain the
 * <code>Application</code> class name programmatically. No WebObjects classes
 * should be loaded before the WOInject initialization.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class WOInject {
    /**
     * Initialize the <code>WOApplication</code> with the WOInject capabilities.
     * 
     * @param applicationClass
     *            the name of the application class
     * @param args
     *            the application's command line arguments
     */
    public static void init(String applicationClass, String[] args) {
	final ClassPool pool = ClassPool.getDefault();

	try {
	    String classname = "com.webobjects.foundation._NSUtilities";

	    CtClass clazz = pool.get(classname);

	    CtMethod method = clazz.getDeclaredMethod("instantiateObject");

	    method.insertBefore("{ return com.webobjects.foundation.InstantiationInterceptor.instantiateObject($1, $2, $3, $4, $5); }");

	    method = clazz.getDeclaredMethod("instantiateObjectWithConstructor");

	    method.insertBefore("{ return com.webobjects.foundation.InstantiationInterceptor.instantiateObject($1, $2, $3, $4, $5); }");

	    ClassLoader loader = Thread.currentThread().getContextClassLoader();

	    loadClass(loader, classname, clazz.toBytecode());

	    Class<?> erxApp = loader.loadClass("er.extensions.appserver.ERXApplication");

	    Class<?> app = loader.loadClass(applicationClass);

	    Class<?> injectableApp = loader.loadClass("com.woinject.InjectableApplication");

	    if (!injectableApp.isAssignableFrom(app)) {
		throw new Error("Cannot initialize the injector. The Application class doesn't extend InjectableApplication.");
	    }

	    erxApp.getDeclaredMethod("main", String[].class, Class.class).invoke(null, args, app);
	} catch (Throwable exception) {
	    throw new Error("Cannot initialize the application to take advantage of WOInject features.", exception);
	}
    }

    private static Class<?> loadClass(ClassLoader loader, String classname, byte[] bytes) {
	Class<?> clazz = null;

	try {
	    Class<?> loaderClass = Class.forName("java.lang.ClassLoader");

	    Method method = loaderClass.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });

	    method.setAccessible(true);

	    try {
		Object[] args = new Object[] { classname, bytes, new Integer(0), new Integer(bytes.length) };

		clazz = (Class<?>) method.invoke(loader, args);
	    } finally {
		method.setAccessible(false);
	    }
	} catch (Exception exception) {
	    exception.printStackTrace();

	    System.exit(1);
	}

	return clazz;
    }
}

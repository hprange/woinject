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
package com.woinject.servlet;

import java.lang.reflect.Method;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * The <code>WOInjectServlet</code> initializes the WOInject framework in the
 * context of servlet applications.
 * <p>
 * Add the following snippet into the web.xml:
 * <p>
 *
 * <pre>
 * 	&lt;servlet&gt;
 * 		&lt;servlet-name&gt;WOInjectServlet&lt;/servlet-name&gt;
 * 		&lt;servlet-class&gt;com.woinject.servlet.WOInjectServlet&lt;/servlet-class&gt;
 * 		&lt;load-on-startup&gt;1&lt;/load-on-startup&gt;
 * 	&lt;/servlet&gt;
 * </pre>
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.1
 */
public class WOInjectServlet extends HttpServlet {
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
	    throw new Error("Error while defining class.", exception);
	}

	return clazz;
    }

    /**
     * Load the changed <code>_NSUtilities</code> class required by WOInject
     * during the container initialization.
     *
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException {
	super.init();

	ClassPool pool = ClassPool.getDefault();

	pool.insertClassPath(new ClassClassPath(WOInjectServlet.class));

	try {
	    String classname = "com.webobjects.foundation._NSUtilities";

	    CtClass clazz = pool.get(classname);

	    CtMethod method = clazz.getDeclaredMethod("instantiateObject");

	    method.insertBefore("{ return com.webobjects.foundation.InstantiationInterceptor.instantiateObject($1, $2, $3, $4, $5); }");

	    method = clazz.getDeclaredMethod("instantiateObjectWithConstructor");

	    method.insertBefore("{ return com.webobjects.foundation.InstantiationInterceptor.instantiateObject($1, $2, $3, $4, $5); }");

	    ClassLoader loader = Thread.currentThread().getContextClassLoader();

	    loadClass(loader, classname, clazz.toBytecode());
	} catch (Throwable exception) {
	    throw new Error("Cannot initialize the application to take advantage of WOInject features.", exception);
	}
    }
}

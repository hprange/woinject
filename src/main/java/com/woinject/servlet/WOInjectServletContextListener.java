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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.woinject.WOInject;

/**
 * <p>
 * The <code>WOInjectServletContextListener</code> initializes the WOInject
 * framework in the context of servlet applications. It's important to
 * initialize WOInject very early in the context of a servlet application.
 * Remember to add the listener in the first place on the web.xml descriptor.
 * </p>
 * <p>
 * Add the following snippet into the web.xml:
 * </p>
 *
 * <pre>
 * &lt;listener&gt;
 * 	&lt;listener-class&gt;com.woinject.servlet.WOInjectServletContextListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * </pre>
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.2
 */
public class WOInjectServletContextListener implements ServletContextListener {
    private static final String WOINJECT_MARKER_ATTRIBUTE = WOInject.class.getName();

    private static Class<?> loadClass(ClassLoader loader, String classname, byte[] bytes) {
        Class<?> clazz = null;

        try {
            Class<?> loaderClass = Class.forName("java.lang.ClassLoader");

            Method method = loaderClass.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });

            method.setAccessible(true);

            try {
                Object[] args = new Object[] { classname, bytes, 0, bytes.length };

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
     * Remove the WOInject marker associated with this context.
     *
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext servletContext = event.getServletContext();

        servletContext.removeAttribute(WOINJECT_MARKER_ATTRIBUTE);
    }

    /**
     * Load the changed <code>_NSUtilities</code> class required by WOInject
     * during the context initialization.
     *
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        if (context.getAttribute(WOINJECT_MARKER_ATTRIBUTE) != null) {
            // WOInject has already been initialized in this context
            return;
        }

        ClassPool pool = ClassPool.getDefault();

        pool.insertClassPath(new ClassClassPath(WOInjectServletContextListener.class));

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

        context.setAttribute(WOINJECT_MARKER_ATTRIBUTE, new Object());
    }
}

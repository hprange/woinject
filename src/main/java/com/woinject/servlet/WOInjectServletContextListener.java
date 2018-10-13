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

import static net.bytebuddy.matcher.ElementMatchers.named;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.woinject.WOInject;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.pool.TypePool;

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

    /**
     * Remove the WOInject marker associated with this context.
     *
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
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
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        if (context.getAttribute(WOINJECT_MARKER_ATTRIBUTE) != null) {
            // WOInject has already been initialized in this context
            return;
        }

        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            TypePool typePool = TypePool.Default.of(classloader);
            TypeDescription nsutilitiesClass = typePool.describe("com.webobjects.foundation._NSUtilities").resolve();
            TypeDescription instantiationInterceptorClass = typePool.describe("com.webobjects.foundation.InstantiationInterceptor").resolve();

            new ByteBuddy().redefine(nsutilitiesClass, ClassFileLocator.ForClassLoader.of(classloader))
                           .method(named("instantiateObject").or(named("instantiateObjectWithConstructor")))
                           .intercept(MethodDelegation.to(instantiationInterceptorClass))
                           .make()
                           .load(classloader, ClassLoadingStrategy.Default.INJECTION);
        } catch (Throwable exception) {
            throw new Error("Cannot initialize the application to take advantage of WOInject features.", exception);
        }

        context.setAttribute(WOINJECT_MARKER_ATTRIBUTE, new Object());
    }
}

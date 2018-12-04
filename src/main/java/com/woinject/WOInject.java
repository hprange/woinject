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

import static net.bytebuddy.matcher.ElementMatchers.named;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.pool.TypePool;

/**
 * The <code>WOInject</code> class initializes the application intercepting core methods used by WebObjects classes to
 * create objects. Since version 1.3, you have to create an application runner to start your application properly. An
 * application runner is a simple Java class containing only a <code>main</code> method.
 * <p>
 * As an illustration, an application runner should look like this:
 *
 * <pre>
 * public ApplicationRunner {
 *     public static void main(String[] argv) {
 *         WOInject.init(&quot;com.company.app.Application&quot;, argv);
 *     }
 * }
 * </pre>
 * <p>
 * <strong>Note</strong>: to ensure well functioning, do not obtain the <code>Application</code> class name
 * programmatically. No WebObjects classes must be loaded before the WOInject initialization.
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
        ClassLoader classloader = ClassLoader.getSystemClassLoader();
        TypePool typePool = TypePool.Default.of(classloader);
        TypeDescription nsutilitiesClass = typePool.describe("com.webobjects.foundation._NSUtilities").resolve();
        TypeDescription instantiationInterceptorClass = typePool.describe("com.webobjects.foundation.InstantiationInterceptor").resolve();

        new ByteBuddy().redefine(nsutilitiesClass, ClassFileLocator.ForClassLoader.of(classloader))
                       .method(named("instantiateObject").or(named("instantiateObjectWithConstructor")))
                       .intercept(MethodDelegation.to(instantiationInterceptorClass))
                       .make()
                       .load(classloader, ClassLoadingStrategy.Default.INJECTION);

        try {
            Class<?> erxApp = classloader.loadClass("er.extensions.appserver.ERXApplication");
            Class<?> injectableApp = classloader.loadClass("com.woinject.InjectableApplication");
            Class<?> app = classloader.loadClass(applicationClass);

            if (!injectableApp.isAssignableFrom(app)) {
                throw new Error("Cannot initialize the injector. The Application class doesn't extend InjectableApplication.");
            }

            erxApp.getDeclaredMethod("main", String[].class, Class.class).invoke(null, args, app);
        } catch (Throwable exception) {
            throw new Error("Cannot initialize the application to take advantage of WOInject features.", exception);
        }
    }
}

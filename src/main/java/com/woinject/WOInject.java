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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;

/**
 * Experiment to avoid inheritance of Application, Session, DirectAction and
 * EnterpriseObject classes.
 * <p>
 * How to use:
 * 
 * <pre>
 * public static void main(String[] argv) {
 *     WOInject.main(argv, &quot;com.company.app.Application&quot;);
 * }
 * </pre>
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class WOInject {
    public static void main(String[] args, String applicationClass) {
	ClassPool cp = ClassPool.getDefault();

	Loader cl = new Loader(cp);

	try {
	    CtClass cc = cp.get("com.webobjects.foundation._NSUtilities");
	    CtMethod m = cc.getDeclaredMethod("instantiateObject");

	    m.insertBefore("{ return com.webobjects.foundation.InstantiationInterceptor.instantiateObject($1, $2, $3, $4, $5); }");

	    m = cc.getDeclaredMethod("instantiateObjectWithConstructor");

	    m.insertBefore("{ return com.webobjects.foundation.InstantiationInterceptor.instantiateObject($1, $2, $3, $4, $5); }");

	    Thread.currentThread().setContextClassLoader(cl);

	    Class<?> erxApplication = cl.loadClass("er.extensions.appserver.ERXApplication");

	    Class<?> appClass = cl.loadClass(applicationClass);

	    Class<?> injectableAppClass = cl.loadClass("com.woinject.InjectableApplication");

	    if (!injectableAppClass.isAssignableFrom(appClass)) {
		throw new Error("Cannot initialize the injector. The Application class doesn't extend InjectableApplication.");
	    }

	    erxApplication.getDeclaredMethod("main", String[].class, Class.class).invoke(null, args, appClass);
	} catch (Throwable exception) {
	    throw new Error("Cannot initialize the application to take advantage of WOInject features.", exception);
	}
    }
}

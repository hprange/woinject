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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.webobjects.appserver.WOSession;
import com.webobjects.eocontrol.EOEnterpriseObject;

/**
 * Experiment to avoid inheritance of Application, Session, DirectAction and
 * EnterpriseObject classes.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public abstract class Runner {
    private static Injector injector;

    private static Injector injector() {
	if (injector == null) {
	    injector = Guice.createInjector(new WOInjectModule());
	}

	return injector;
    }

    public static Object intercept(Class<?> clazz, Object[] parameters) {
	System.out.println("\tClass: " + clazz + " Parameters: " + java.util.Arrays.toString(parameters));

	if (WOSession.class.isAssignableFrom(clazz)) {
	    System.out.println("Creating an instace of " + clazz.getName());

	    Object instance = injector().getInstance(clazz);

	    return instance;
	}

	if (EOEnterpriseObject.class.isAssignableFrom(clazz)) {
	    System.out.println("Creating an instace of " + clazz.getName());

	    Object instance = injector().getInstance(clazz);

	    return instance;
	}

	return null;
    }

    public static void main(String[] args) throws Throwable {
	ClassPool cp = ClassPool.getDefault();
	Loader cl = new Loader(cp);
	CtClass cc = cp.get("com.webobjects.foundation._NSUtilities");
	CtMethod m = cc.getDeclaredMethod("instantiateObject");

	m.insertBefore("{ Object result = com.woinject.Runner.intercept($1, $2); if(result!=null) return result; }");

	m = cc.getDeclaredMethod("instantiateObjectWithConstructor");

	m.insertBefore("{ Object result = com.woinject.Runner.intercept($2, $3); if(result!=null) return result; }");

	Thread.currentThread().setContextClassLoader(cl);

	cl.run("com.woinject.app.Application", args);
    }
}

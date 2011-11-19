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

import java.lang.reflect.Constructor;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.util.Providers;
import com.webobjects.appserver.WOAction;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WOSession;
import com.webobjects.eocontrol.EOEnterpriseObject;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class Interceptor {
    public static Object injectMembers(Object object) {
	Injector injector = InjectableApplication.application().injector();

	if (injector == null) {
	    return object;
	}

	injector.injectMembers(object);

	return object;
    }

    public static <T> T injectMembers2(final Class<T> type, final Class<?>[] parameterTypes, final Object[] parameters) {
	System.out.println("Calling instantiateObject");

	Injector injector = InjectableApplication.application().injector();

	if (injector == null || !(WOSession.class.isAssignableFrom(type) || WOComponent.class.isAssignableFrom(type) || EOEnterpriseObject.class.isAssignableFrom(type) || WODirectAction.class.isAssignableFrom(type))) {
	    System.out.println("--- Injector null for " + type);

	    try {
		return type.getConstructor(parameterTypes).newInstance(parameters);
	    } catch (Exception exception) {
		throw new RuntimeException(exception);
	    }
	}

	Module assistedModule = new AbstractModule() {
	    @SuppressWarnings({ "rawtypes", "unchecked" })
	    protected void configure() {
		Binder binder = binder().withSource(type);

		System.out.println("--- Type: " + type);

		int p = 0;

		if (parameterTypes != null) {
		    for (Class<?> paramKey : parameterTypes) {
			System.out.println("   --- Parameter Types: " + paramKey.getName());

			binder.bind(paramKey).toProvider((Provider) Providers.of(parameters[p++]));
		    }
		}

		Constructor<T> constructor;

		try {
		    constructor = type.getConstructor(parameterTypes);
		} catch (Exception exception) {
		    throw new RuntimeException(exception);
		}

		binder.bind(type).toConstructor(constructor).in(Scopes.NO_SCOPE);
	    }
	};

	Injector forCreate = injector.createChildInjector(assistedModule);

	Binding<T> binding = forCreate.getBinding(type);

	return binding.getProvider().get();
    }

    public static <T> T injectMembers3(final Constructor<T> constructor, final Class<T> type, final Object[] parameters) {
	System.out.println("Calling instantiateObjectWithConstructor");

	Injector injector = InjectableApplication.application().injector();

	if (injector == null) {
	    System.out.println("--- Injector null for " + type);

	    try {
		return constructor.newInstance(parameters);
	    } catch (Exception exception) {
		throw new RuntimeException(exception);
	    }
	}

	System.out.println("--- Type: " + type);

	Module assistedModule = new AbstractModule() {
	    @SuppressWarnings({ "rawtypes", "unchecked" })
	    protected void configure() {
		Binder binder = binder().withSource(type);

		for (Object parameter : parameters) {
		    System.out.println("   --- Parameter Types: " + parameter.getClass().getName());

		    binder.bind(parameter.getClass()).toProvider((Provider) Providers.of(parameter));
		}

		binder.bind(type).toConstructor(constructor).in(Scopes.NO_SCOPE);
	    }
	};

	Injector forCreate = injector.createChildInjector(assistedModule);

	Binding<T> binding = forCreate.getBinding(type);

	return binding.getProvider().get();
    }
}

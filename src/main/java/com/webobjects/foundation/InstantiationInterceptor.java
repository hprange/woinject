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
package com.webobjects.foundation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.BindingAnnotation;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.util.Providers;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WOSession;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.woinject.InjectableApplication;
import com.woinject.WOInjectException;

/**
 * The InstantiatorInterceptor class creates objects and injects their members
 * automatically. Special WebObjects types (components, enterprise objects,
 * direct actions and sessions) are instantiated using the Guice injector and
 * are subject to AOP interceptors. This class is private and is not intended to
 * be used directly by users.
 * 
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.0
 */
class InstantiationInterceptor {
    /**
     * Special binding annotation used by WOInject to avoid binding conflicts.
     * 
     * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
     * @since 1.0
     */
    @BindingAnnotation
    @Target({ FIELD, PARAMETER, METHOD })
    @Retention(RUNTIME)
    private @interface WOInjectBinding {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(InstantiationInterceptor.class);

    private static <T> Constructor<T> findConstructor(final Class<T> type, final Class<?>[] parameterTypes, boolean shouldThrow, boolean shouldLog) {
	try {
	    return type.getConstructor(parameterTypes);
	} catch (Exception exception) {
	    return handleException(type, exception, shouldThrow, shouldLog);
	}
    }

    private static <T, N> N handleException(Class<T> type, Exception exception, boolean shouldThrow, boolean shouldLog) {
	if (shouldLog) {
	    LOGGER.error("The instantiation of " + type.getName() + " class has failed.", exception);
	}

	if (!shouldThrow) {
	    return null;
	}

	throw new WOInjectException("The instantiation of " + type.getName() + " class has failed.", exception);
    }

    private static Injector injector() {
	InjectableApplication application = InjectableApplication.application();

	return application == null ? null : application.injector();
    }

    /**
     * Instantiate the object represented by the type parameter. The object is
     * created by the injector defined in the {@link InjectableApplication}
     * class if the type is assignable from <code>WOComponent</code>,
     * <code>EOEnterpriseObject</code>, <code>WOSession</code> or
     * <code>WODirectAction</code>.
     * <p>
     * Other types of objects are instantiated by reflection, and their members
     * are injected using the {@link Injector#injectMembers(Object)} method.
     * Those objects are not subject to AOP interceptors.
     * 
     * @see _NSUtilities#instantiateObject(Class, Class[], Object[], boolean,
     *      boolean)
     * @see Injector#injectMembers(Object)
     */
    public static <T> T instantiateObject(final Class<T> type, final Class<?>[] parameterTypes, final Object[] parameters, boolean shouldThrow, boolean shouldLog) {
	Constructor<T> constructor = findConstructor(type, parameterTypes, shouldThrow, shouldLog);

	if (constructor == null) {
	    return null;
	}

	return instantiateObject(constructor, type, parameters, shouldThrow, shouldLog);
    }

    /**
     * Instantiate the object represented by the type parameter using the
     * specified constructor. The object is created by the injector defined in
     * the {@link InjectableApplication} class if the type is assignable from
     * <code>WOComponent</code>, <code>EOEnterpriseObject</code>,
     * <code>WOSession</code> or <code>WODirectAction</code>.
     * <p>
     * Other types of objects are instantiated by reflection, and their members
     * are injected using the {@link Injector#injectMembers(Object)} method.
     * Those objects are not subject to AOP interceptors.
     * 
     * @see _NSUtilities#instantiateObject(Class, Class[], Object[], boolean,
     *      boolean)
     * @see Injector#injectMembers(Object)
     */
    public static <T> T instantiateObject(final Constructor<T> constructor, final Class<T> type, final Object[] parameters, boolean shouldThrow, boolean shouldLog) {
	Injector injector = injector();

	if (injector == null) {
	    return instantiateObjectByReflection(type, constructor, parameters, shouldThrow, shouldLog);
	}

	if (!(WOSession.class.isAssignableFrom(type) || WOComponent.class.isAssignableFrom(type) || EOEnterpriseObject.class.isAssignableFrom(type) || WODirectAction.class.isAssignableFrom(type))) {
	    T object = instantiateObjectByReflection(type, constructor, parameters, shouldThrow, shouldLog);

	    if (object != null) {
		injector.injectMembers(object);
	    }

	    return object;
	}

	Module woinjectModule = new AbstractModule() {
	    @Override
	    @SuppressWarnings({ "rawtypes", "unchecked" })
	    protected void configure() {
		Binder binder = binder().withSource(type);

		int i = 0;

		if (parameters != null) {
		    for (Class<?> parameterType : constructor.getParameterTypes()) {
			binder.bind(parameterType).toProvider((Provider) Providers.of(parameters[i++]));
		    }
		}

		binder.bind(Key.get(type, WOInjectBinding.class)).toConstructor(constructor).in(Scopes.NO_SCOPE);
	    }
	};

	try {
	    Injector forCreate = injector.createChildInjector(woinjectModule);

	    Binding<T> binding = forCreate.getBinding(Key.get(type, WOInjectBinding.class));

	    return binding.getProvider().get();
	} catch (Exception exception) {
	    return handleException(type, exception, shouldThrow, shouldLog);
	}
    }

    private static <T> T instantiateObjectByReflection(Class<T> type, Constructor<T> constructor, Object[] parameters, boolean shouldThrow, boolean shouldLog) {
	try {
	    return constructor.newInstance(parameters);
	} catch (Exception exception) {
	    return handleException(type, exception, shouldThrow, shouldLog);
	}
    }
}

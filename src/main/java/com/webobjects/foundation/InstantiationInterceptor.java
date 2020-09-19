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

import com.google.inject.Injector;
import com.woinject.InjectableApplication;
import com.woinject.WOInjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * The InstantiatorInterceptor class creates objects and injects their
 * members (methods and fields only) automatically. This class is private
 * and is not intended to be used directly by users.
 *
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.0
 */
class InstantiationInterceptor {
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
     * instantiated by reflection, and their members are injected using the
     * {@link Injector#injectMembers(Object)} method of the injector defined
     * in the {@link InjectableApplication} class.
     *
     * @see _NSUtilities#instantiateObject(Class, Class[], Object[], boolean, boolean)
     * @see Injector#injectMembers(Object)
     */
    public static <T> T instantiateObject(final Class<T> type, final Class<?>[] parameterTypes, final Object[] parameters, boolean shouldThrow, boolean shouldLog) {
        Constructor<T> constructor = findConstructor(type, parameterTypes, shouldThrow, shouldLog);

        if (constructor == null) {
            return null;
        }

        return instantiateObjectWithConstructor(constructor, type, parameters, shouldThrow, shouldLog);
    }

    /**
     * Instantiate the object represented by the type parameter using the
     * specified constructor. The object is instantiated by reflection, and
     * their members are injected using the {@link Injector#injectMembers(Object)}
     * method of the injector defined in the {@link InjectableApplication} class.
     *
     * @see _NSUtilities#instantiateObjectWithConstructor(Constructor, Class, Object[], boolean, boolean)
     * @see Injector#injectMembers(Object)
     */
    public static <T> T instantiateObjectWithConstructor(final Constructor<T> constructor, final Class<T> type, final Object[] parameters, boolean shouldThrow, boolean shouldLog) {
        T object = instantiateObjectByReflection(type, constructor, parameters, shouldThrow, shouldLog);

        if (object != null) {
            Injector injector = injector();

            if (injector != null) {
                injector.injectMembers(object);
            }
        }

        return object;
    }

    private static <T> T instantiateObjectByReflection(Class<T> type, Constructor<T> constructor, Object[] parameters, boolean shouldThrow, boolean shouldLog) {
        try {
            return constructor.newInstance(parameters);
        } catch (Exception exception) {
            return handleException(type, exception, shouldThrow, shouldLog);
        }
    }
}

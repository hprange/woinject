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

import static er.extensions.eof.ERXEOControlUtilities.localInstanceOfObject;

import com.google.inject.OutOfScopeException;
import com.google.inject.ProvisionException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;

/**
 * An object capable of providing instances of type {@code T} that extends
 * {@code EOEnterpriseObjects}. Providers are used in numerous ways by Guice.
 * See {@link com.google.inject.Provider} for more information.
 *
 * @param <T>
 *            the type of the {@code EOEnterpriseObject} this provides
 * @see com.google.inject.Provider
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 * @since 1.3
 */
@FunctionalInterface
public interface EOProvider<T extends EOEnterpriseObject> {
    /**
     * Returns a provider which always provides {@code instance}. This should
     * not be necessary to use in your application, but is helpful for several
     * types of unit tests.
     *
     * @param instance
     *            the instance that should always be provided. This is also
     *            permitted to be null, to enable aggressive testing, although
     *            in real life a Guice-supplied Provider will never return null.
     */
    public static <T extends EOEnterpriseObject> EOProvider<T> of(T instance) {
        return ec -> localInstanceOfObject(ec, instance);
    }

    /**
     * Provides an instance of {@code T} in the given editing context. Must
     * never return {@code null}.
     *
     * @param editingContext
     *            the editing context used to obtain the enterprise object.
     * 
     * @return Returns an instance of {@code T} in the specified editing
     *         context.
     *
     * @throws OutOfScopeException
     *             when an attempt is made to access a scoped object while the
     *             scope in question is not currently active
     * @throws ProvisionException
     *             if an instance cannot be provided. Such exceptions include
     *             messages and throwables to describe why provision failed.
     */
    public T get(EOEditingContext editingContext);
}

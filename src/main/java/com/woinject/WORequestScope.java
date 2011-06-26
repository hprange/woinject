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

import static com.webobjects.foundation.NSKeyValueCoding.NullValue;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.webobjects.appserver.WORequest;

import er.extensions.appserver.ERXWOContext;

/**
 * WORequest scope.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @author Sébastien Letélie
 * @since 1.0
 */
class WORequestScope implements Scope {
    WORequestScope() {
    }

    WORequest request() {
	return ERXWOContext.currentContext().request();
    }

    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
	final String name = key.toString();

	return new Provider<T>() {
	    public T get() {
		WORequest request = request();

		if (request == null) {
		    throw new OutOfScopeException("Cannot access scoped object. Either the request has not been dispatched yet, or its cycle has ended.");
		}

		synchronized (request) {
		    Object object = request.userInfoForKey(name);

		    if (object == NullValue) {
			return null;
		    }

		    @SuppressWarnings("unchecked")
		    T t = (T) object;

		    if (t == null) {
			t = creator.get();

			request.setUserInfoForKey(t == null ? NullValue : t, name);
		    }

		    return t;
		}
	    }
	};
    }
}

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

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.appserver.ERXSession;

/**
 * WOSession scope.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
class WOSessionScope implements Scope
{
	/*
	 * (non-Javadoc)
	 * @see com.google.inject.Scope#scope(com.google.inject.Key,
	 * com.google.inject.Provider)
	 */
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator)
	{
		final String name = key.toString();

		return new Provider<T>()
		{
			public T get()
			{
				WOSession session = session();

				if(session == null)
				{
					throw new OutOfScopeException("Cannot access scoped object. Either the Session has not been initialized yet or it has expired.");
				}

				synchronized(session)
				{
					Object object = session.objectForKey(name);

					if(object == NSKeyValueCoding.NullValue)
					{
						return null;
					}

					@SuppressWarnings("unchecked")
					T t = (T) object;

					if(t == null)
					{
						t = creator.get();

						session.setObjectForKey(t != null ? t : NSKeyValueCoding.NullValue, name);
					}

					return t;
				}
			}
		};
	}

	protected WOSession session()
	{
		return ERXSession.session();
	}
}

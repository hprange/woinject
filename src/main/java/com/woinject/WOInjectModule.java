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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.webobjects.appserver.WOContext;

import er.extensions.appserver.ERXSession;
import er.extensions.appserver.ERXWOContext;

/**
 * This class contains the basic <code>WOInject</code> configuration and
 * auxiliary providers.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class WOInjectModule extends AbstractModule {
    /**
     * Make the binding of WOScopes.
     * 
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
	bindScope(WORequestScoped.class, WOScopes.REQUEST);
	bindScope(WOSessionScoped.class, WOScopes.SESSION);
    }

    /**
     * Provide the current context object or throw an exception if unable to get
     * it.
     * 
     * @return the current context.
     */
    @Provides
    @Current
    public WOContext provideContext() {
	WOContext context = ERXWOContext.currentContext();

	if (context == null) {
	    throw new WOInjectException("Unable to provide the current context.");
	}

	return context;
    }

    /**
     * Provide the current session object or throw an exception if unable to get
     * it.
     * 
     * @return Return the current session.
     */
    @Provides
    @Current
    public ERXSession provideSession() {
	ERXSession session = ERXSession.session();

	if (session == null) {
	    throw new WOInjectException("Unable to provide the current session. Either the session has not been initialized yet, or it has expired.");
	}

	return session;
    }
}

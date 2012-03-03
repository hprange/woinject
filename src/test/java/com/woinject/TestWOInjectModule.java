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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.webobjects.appserver.WOContext;

import er.extensions.appserver.ERXSession;
import er.extensions.appserver.ERXWOContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestWOInjectModule {
    private Injector injector;

    private Map<Class<? extends Annotation>, Scope> scopes;

    @Test
    public void bindCurrentContextProvider() throws Exception {
	WOContext mockContext = mock(WOContext.class);

	ERXWOContext.setCurrentContext(mockContext);

	WOContext result = injector.getInstance(Key.get(WOContext.class, Current.class));

	assertThat(result, is(mockContext));
    }

    @Test
    public void bindCurrentSessionProvider() throws Exception {
	ERXSession mockSession = mock(ERXSession.class);

	ERXSession.setSession(mockSession);

	ERXSession result = injector.getInstance(Key.get(ERXSession.class, Current.class));

	assertThat(result, is(mockSession));
    }

    @Test
    public void bindWORequestScope() throws Exception {
	Scope scope = scopes.get(WORequestScoped.class);

	assertThat(scope == WOScopes.REQUEST, is(true));
    }

    @Test
    public void bindWOSessionScope() throws Exception {
	Scope scope = scopes.get(WOSessionScoped.class);

	assertThat(scope == WOScopes.SESSION, is(true));
    }

    @Test(expected = ProvisionException.class)
    public void exceptionIfProvidingNullContext() throws Exception {
	ERXWOContext.setCurrentContext(null);

	injector.getInstance(Key.get(WOContext.class, Current.class));
    }

    @Test(expected = ProvisionException.class)
    public void exceptionIfProvidingNullSession() throws Exception {
	ERXSession.setSession(null);

	injector.getInstance(Key.get(ERXSession.class, Current.class));
    }

    @Before
    public void setup() {
	injector = Guice.createInjector(new WOInjectModule());

	scopes = injector.getScopeBindings();
    }
}

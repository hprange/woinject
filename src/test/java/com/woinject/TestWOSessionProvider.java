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
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.woinject.stubs.StubApplication;

import er.extensions.appserver.ERXSession;
import er.extensions.appserver.ERXWOContext;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWOSessionProvider {
    @Mock
    private WOApplication application;

    @Mock
    private ERXSession session;

    @Mock
    private WOContext context;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @After
    public void tearDown() {
        ERXSession.setSession(null);
        ERXWOContext.setCurrentContext(null);
    }

    @Test
    public void returnSessionFromERXSession() throws Exception {
        ERXSession.setSession(session);

        WOSessionProvider<ERXSession> provider = new WOSessionProvider<ERXSession>();

        assertThat(provider.get(), is(session));
    }

    @Test
    public void returnSessionFromWOContext() throws Exception {
        ERXWOContext.setCurrentContext(context);

        when(context._session()).thenReturn(session);

        WOSessionProvider<ERXSession> provider = new WOSessionProvider<ERXSession>();

        assertThat(provider.get(), is(session));
    }

    @Test
    public void returnSessionFromSessionID() throws Exception {
        ERXWOContext.setCurrentContext(context);

        when(context._requestSessionID()).thenReturn("session-123");

        StubApplication.setApplication(application);

        when(application.restoreSessionWithID("session-123", context)).thenReturn(session);

        WOSessionProvider<ERXSession> provider = new WOSessionProvider<ERXSession>();

        assertThat(provider.get(), is(session));
    }

    @Test
    public void throwExceptionWhenNoSessionAvailable() throws Exception {
        WOSessionProvider<ERXSession> provider = new WOSessionProvider<ERXSession>();

        thrown.expect(WOInjectException.class);
        thrown.expectMessage(is("Unable to provide the current session. Either the session has not been initialized yet, or it has expired."));

        provider.get();
    }
}

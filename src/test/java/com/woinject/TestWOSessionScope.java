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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSKeyValueCoding;
import com.woinject.stubs.StubApplication;

import er.extensions.appserver.ERXSession;
import er.extensions.appserver.ERXWOContext;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWOSessionScope extends AbstractScopeTestCase<WOSessionScope> {
    @Mock
    private ERXSession mockSession;

    @Mock
    private WOContext mockContext;

    @Mock
    private WOApplication application;

    @Before
    public void setup() {
        scope = new WOSessionScope();

        ERXSession.setSession(mockSession);

        ERXWOContext.setCurrentContext(mockContext);

        StubApplication.setApplication(application);

        mockKey = Key.get(Object.class);

        when(mockSession.objectForKey(KEY_NAME)).thenReturn(null, mockObject);

        when(mockCreator.get()).thenReturn(mockObject);
    }

    @After
    public void tearDown() {
        ERXSession.setSession(null);
    }

    @Test
    public void createAndSaveObjectOnFirstCall() throws Exception {
        Provider<Object> result = scope.scope(mockKey, mockCreator);

        assertThat(result.get(), is(mockObject));

        Mockito.verify(mockCreator).get();
        Mockito.verify(mockSession).setObjectForKey(mockObject, KEY_NAME);
    }

    @Test
    public void exceptionIfSessionIsNotAvailable() throws Exception {
        ERXSession.setSession(null);

        Provider<Object> result = scope.scope(mockKey, mockCreator);

        thrown.expect(OutOfScopeException.class);
        thrown.expectMessage(is("Cannot access scoped object. Either the session has not been initialized yet, or it has expired."));

        result.get();
    }

    @Test
    public void retrieveTheSameObjectOnSubsequentCalls() throws Exception {
        for (int i = 0; i < 10; i++) {
            Provider<Object> result = scope.scope(mockKey, mockCreator);

            assertThat(result.get(), is(mockObject));
        }

        Mockito.verify(mockCreator, Mockito.times(1)).get();
        Mockito.verify(mockSession, Mockito.times(1)).setObjectForKey(mockObject, KEY_NAME);
    }

    @Test
    public void returnNullOnSubsequentCalls() throws Exception {
        Mockito.when(mockSession.objectForKey(KEY_NAME)).thenReturn(null, NSKeyValueCoding.NullValue);
        Mockito.when(mockCreator.get()).thenReturn(null);

        for (int i = 0; i < 10; i++) {
            Provider<Object> result = scope.scope(mockKey, mockCreator);

            assertThat(result.get(), nullValue());
        }

        Mockito.verify(mockCreator, Mockito.times(1)).get();
        Mockito.verify(mockSession, Mockito.times(1)).setObjectForKey(NSKeyValueCoding.NullValue, KEY_NAME);
    }

    @Test
    public void setNullValueIfProvidesReturnsNull() throws Exception {
        Mockito.when(mockCreator.get()).thenReturn(null);

        Provider<Object> result = scope.scope(mockKey, mockCreator);

        assertThat(result.get(), nullValue());

        Mockito.verify(mockSession).setObjectForKey(NSKeyValueCoding.NullValue, KEY_NAME);
    }

    @Test
    public void initializeSessionFromSessionID() throws Exception {
        ERXSession.setSession(null);

        when(mockContext._requestSessionID()).thenReturn("session-123");

        when(application.restoreSessionWithID("session-123", mockContext)).thenReturn(mockSession);

        Provider<Object> result = scope.scope(mockKey, mockCreator);

        assertThat(result.get(), is(mockObject));
    }
}

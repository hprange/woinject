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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSKeyValueCoding;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestWORequestScope extends AbstractScopeTestCase<WORequestScope> {
    @Mock
    protected WORequest mockRequest;

    @Test
    public void createAndSaveObjectOnFirstCall() throws Exception {
	Provider<Object> result = scope.scope(mockKey, mockCreator);

	assertThat(result.get(), is(mockObject));

	verify(mockCreator).get();
	verify(mockRequest).setUserInfoForKey(mockObject, KEY_NAME);
    }

    @Test
    public void exceptionIfSessionIsNotAvailable() throws Exception {
	doReturn(null).when(scope).request();

	Provider<Object> result = scope.scope(mockKey, mockCreator);

	thrown.expect(OutOfScopeException.class);
	thrown.expectMessage(is("Cannot access scoped object. Either the request has not been dispatched yet, or its cycle has ended."));

	result.get();
    }

    @Test
    public void retrieveTheSameObjectOnSubsequentCalls() throws Exception {
	for (int i = 0; i < 10; i++) {
	    Provider<Object> result = scope.scope(mockKey, mockCreator);

	    assertThat(result.get(), is(mockObject));
	}

	verify(mockCreator, times(1)).get();
	verify(mockRequest, times(1)).setUserInfoForKey(mockObject, KEY_NAME);
    }

    @Test
    public void returnNullOnSubsequentCalls() throws Exception {
	when(mockRequest.userInfoForKey(KEY_NAME)).thenReturn(null, NSKeyValueCoding.NullValue);
	when(mockCreator.get()).thenReturn(null);

	for (int i = 0; i < 10; i++) {
	    Provider<Object> result = scope.scope(mockKey, mockCreator);

	    assertThat(result.get(), nullValue());
	}

	verify(mockCreator, times(1)).get();
	verify(mockRequest, times(1)).setUserInfoForKey(NSKeyValueCoding.NullValue, KEY_NAME);
    }

    @Test
    public void setNullValueIfProvidesReturnsNull() throws Exception {
	when(mockCreator.get()).thenReturn(null);

	Provider<Object> result = scope.scope(mockKey, mockCreator);

	assertThat(result.get(), nullValue());

	verify(mockRequest).setUserInfoForKey(NSKeyValueCoding.NullValue, KEY_NAME);
    }

    @Before
    public void setup() {
	scope = spy(new WORequestScope());

	doReturn(mockRequest).when(scope).request();

	mockKey = Key.get(Object.class);

	when(mockRequest.userInfoForKey(KEY_NAME)).thenReturn(null, mockObject);

	when(mockCreator.get()).thenReturn(mockObject);

    }
}

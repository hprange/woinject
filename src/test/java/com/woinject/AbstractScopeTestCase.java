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

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractScopeTestCase<T extends Scope> {
    protected static final String KEY_NAME = "Key[type=java.lang.Object, annotation=[none]]";

    @Mock
    protected Provider<Object> mockCreator;

    protected Key<Object> mockKey;

    @Mock
    protected Object mockObject;

    protected T scope;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();
}

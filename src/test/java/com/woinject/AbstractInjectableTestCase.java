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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.webobjects.foundation.NSBundle;
import com.woinject.stubs.StubApplication;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public abstract class AbstractInjectableTestCase {
    /**
     * We have to cheat here in order to make the Application initialization
     * work.
     */
    @BeforeClass
    public static void initMainBundle() throws Exception {
	ClassPool pool = ClassPool.getDefault();
	CtClass cc = pool.get("com.webobjects.foundation._NSUtilities");
	CtMethod m = cc.getDeclaredMethod("instantiateObject");
	m.insertBefore("{ if(com.google.inject.Stage.class == $1) return com.woinject.InjectableApplication#application().injector().getInstance($1); }");
	cc.toClass();

	NSBundle mockBundle = mock(NSBundle.class);

	when(mockBundle.name()).thenReturn("woinject");
	when(mockBundle.bundlePathURL()).thenReturn(TestInjectableApplication.class.getClass().getResource("/"));

	NSBundle._setMainBundle(mockBundle);
    }

    protected StubApplication application;

    @Before
    public void setup() throws Exception {
	application = new StubApplication();
    }

    @After
    public void tearDown() {
	if (application != null) {
	    application.terminate();
	}

	application = null;
    }
}

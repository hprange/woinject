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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.After;
import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Stage;
import com.webobjects.foundation._NSUtilities;

import er.extensions.foundation.ERXProperties;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestInjectableApplication extends AbstractInjectableTestCase
{
	private static final String DEVELOPMENT_MODE_KEY = "er.extensions.ERXApplication.developmentMode";

	@Test
	public void aaaa() throws NotFoundException, CannotCompileException
	{
		Object result = _NSUtilities.instantiateObject(Stage.class, null, null, false, false);

		System.out.println(result);
	}

	@Test
	public void appendAndLoadStubModuleOnInjectorInitialization() throws Exception
	{
		assertThat(((StubApplication) application).stubModuleWasLoaded(), is(true));
	}

	@Test
	public void createAnInjectorOnApplicationInitialization() throws Exception
	{
		assertThat(application.injector(), notNullValue());
	}

	@Test
	public void injectApplicationFields() throws Exception
	{
		assertThat(((StubApplication) application).getInjectableField(), is("fieldInjected"));
	}

	@Test
	public void injectApplicationMethods() throws Exception
	{
		assertThat(((StubApplication) application).getInjectableMethod(), is("methodInjected"));
	}

	@Test
	public void loadWOInjectModuleAutomatically() throws Exception
	{
		Injector injector = application.injector();

		assertThat(injector.getScopeBindings().get(WOSessionScoped.class), notNullValue());
	}

	@Test
	public void setStageForDevelomentMode() throws Exception
	{
		ERXProperties.setStringForKey("true", DEVELOPMENT_MODE_KEY);

		application = new StubApplication();

		Stage stage = application.injector().getInstance(Stage.class);

		assertThat(stage, is(Stage.DEVELOPMENT));
	}

	@Test
	public void setStageForProductionMode() throws Exception
	{
		ERXProperties.setStringForKey("false", DEVELOPMENT_MODE_KEY);

		application = new StubApplication();

		Stage stage = application.injector().getInstance(Stage.class);

		assertThat(stage, is(Stage.PRODUCTION));
	}

	@Override
	@After
	public void tearDown()
	{
		super.tearDown();

		ERXProperties.removeKey(DEVELOPMENT_MODE_KEY);
	}
}

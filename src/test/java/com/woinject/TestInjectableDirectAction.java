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

import org.junit.Before;
import org.junit.Test;

import com.webobjects.appserver.WORequest;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestInjectableDirectAction extends AbstractInjectableTestCase
{
	private StubDirectAction directAction;

	@Test
	public void injectDirectActionFields() throws Exception
	{
		assertThat(directAction.getInjectableField(), is("fieldInjected"));
	}

	@Test
	public void injectDirectActionMethods() throws Exception
	{
		assertThat(directAction.getInjectableMethod(), is("methodInjected"));
	}

	@Override
	@Before
	public void setup()
	{
		super.setup();

		WORequest mockRequest = mock(WORequest.class);

		directAction = new StubDirectAction(mockRequest);
	}
}

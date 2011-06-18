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

import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class TestInjectableEnterpriseObject extends AbstractInjectableTestCase
{
	private StubEnterpriseObject eo;

	@Test
	public void injectEnterpriseObjectsFields() throws Exception
	{
		assertThat(eo.getInjectableField(), is("fieldInjected"));
	}

	@Test
	public void injectEnterpriseObjectsMethods() throws Exception
	{
		assertThat(eo.getInjectableMethod(), is("methodInjected"));
	}

	@Override
	@Before
	public void setup()
	{
		super.setup();

		eo = new StubEnterpriseObject();
	}
}

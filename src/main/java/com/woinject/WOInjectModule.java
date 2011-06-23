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
import com.google.inject.name.Names;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 */
public class WOInjectModule extends AbstractModule {

    @Override
    protected void configure() {
	bindScope(WOSessionScoped.class, new WOSessionScope());

	// TODO: Remove. It's here just to help debugging.
	bind(String.class).annotatedWith(Names.named("test")).toInstance("yes");
    }
}

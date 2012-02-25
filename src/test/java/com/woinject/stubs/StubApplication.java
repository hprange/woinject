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

package com.woinject.stubs;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.woinject.InjectableApplication;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
public class StubApplication extends InjectableApplication {
    private static class StubModule extends AbstractModule {
	private boolean moduleWasLoaded = false;

	@Override
	protected void configure() {
	    moduleWasLoaded = true;

	    bind(String.class).annotatedWith(Names.named("field")).toInstance("fieldInjected");
	    bind(String.class).annotatedWith(Names.named("method")).toInstance("methodInjected");
	}
    }

    @Inject
    @Named("field")
    private String injectableField;

    private String injectableMethod;

    private boolean nullInjector = false;

    private StubModule stubModule;

    public String getInjectableField() {
	return injectableField;
    }

    public String getInjectableMethod() {
	return injectableMethod;
    }

    @Inject
    public void initInjectableMethod(@Named("method") String value) {
	injectableMethod = value;
    }

    @Override
    public Injector injector() {
	if (nullInjector) {
	    return null;
	}

	return super.injector();
    }

    @Override
    protected Module[] modules() {
	stubModule = new StubModule();

	return new Module[] { stubModule };
    }

    public void setReturnNullInjector(boolean nullInjector) {
	this.nullInjector = nullInjector;
    }

    public boolean stubModuleWasLoaded() {
	return stubModule.moduleWasLoaded;
    }
}

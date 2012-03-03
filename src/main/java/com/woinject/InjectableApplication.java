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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOSession;

import er.extensions.appserver.ERXApplication;

/**
 * The <code>InjectableApplication</code> class is an extension of the
 * <code>ERXApplication</code> that adds support for dependency injection to
 * WebObjects applications. It automatically creates the Guice injector and
 * initializes it with the {@link WOInjectModule}. Additional modules can be
 * loaded by overriding the {@link InjectableApplication#modules()} method.
 * 
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 * @since 1.0
 * @see Injector
 * @see WOInjectModule
 */
public abstract class InjectableApplication extends ERXApplication {
    public static InjectableApplication application() {
	return (InjectableApplication) WOApplication.application();
    }

    private final Injector injector;

    /**
     * Creates an instance of an <code>InjectableApplication</code>. The methods
     * and fields annotated with the <code>@Inject</code> will be injected
     * during the application construction. Constructor injection is not
     * supported at the present time.
     * 
     * @see Inject
     */
    public InjectableApplication() {
	super();

	injector = createInjector();

	injector.injectMembers(this);
    }

    /**
     * Creates the injector to be used by the entire application.
     * <p>
     * The <code>WOInjectModule</code> is loaded by default in addition to all
     * the <code>Module</code>s returned by the
     * {@link InjectableApplication#modules()} method.
     * <p>
     * Override this method to create your own injector.
     * 
     * @return The main <code>Injector<code> for the entire application
     * 
     * @see Injector
     * @see WOInjectModule
     */
    protected Injector createInjector() {
	List<Module> modules = new ArrayList<Module>();

	@SuppressWarnings("unchecked")
	Class<? extends WOSession> sessionClass = _sessionClass();

	modules.add(new WOInjectModule(sessionClass));
	modules.addAll(Arrays.asList(modules()));

	return Guice.createInjector(stage(), modules);
    }

    /**
     * Obtain the injector created for this application.
     * 
     * @return The injector created during this application initialization
     */
    public Injector injector() {
	return injector;
    }

    /**
     * Override this method to inform what <code>Module</code>s should be loaded
     * by the injector during the initialization phase.
     * 
     * @return An array of <code>Module</code>s to be loaded when creating the
     *         injector
     * 
     * @see InjectableApplication#createInjector()
     * @see Module
     */
    protected Module[] modules() {
	return new Module[] {};
    }

    /**
     * Obtain the <code>Stage</code> in which the application is running. This
     * stage is to be used by the injector.
     * 
     * Override this method to inform
     * 
     * @return
     * @see Stage
     */
    protected Stage stage() {
	return isDevelopmentMode() ? Stage.DEVELOPMENT : Stage.PRODUCTION;
    }
}

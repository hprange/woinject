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
package com.webobjects.foundation;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.MockUtil;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.woinject.AbstractInjectableTestCase;
import com.woinject.WOInjectException;
import com.woinject.stubs.StubApplication;
import com.woinject.stubs.StubComponent;
import com.woinject.stubs.StubDirectAction;
import com.woinject.stubs.StubEnterpriseObject;
import com.woinject.stubs.StubObject;
import com.woinject.stubs.StubSession;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class TestInstantiationInterceptor extends AbstractInjectableTestCase {
    private static class StubInterceptor implements MethodInterceptor {
        int invocations = 0;

        public Object invoke(MethodInvocation invocation) throws Throwable {
            invocations++;

            return invocation.proceed();
        }

        public void reset() {
            invocations = 0;
        }
    }

    private static class StubModule extends AbstractModule {
        private final MethodInterceptor interceptor;

        public StubModule(MethodInterceptor interceptor) {
            this.interceptor = interceptor;
        }

        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Names.named("test")).toInstance("expectedText");
            bind(StubObjectForBinding.class).toInstance(mock(StubObjectForBinding.class));

            bindInterceptor(Matchers.any(), Matchers.any(), interceptor);
        }
    }

    static class StubObjectForBinding {
        public StubObjectForBinding() {
        }
    }

    private StubInterceptor interceptor;

    @Mock
    private WOContext mockContext;

    @Mock
    private WORequest mockRequest;

    private Object[][] parameters;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void bindInterceptorToSpecialObjectsWithConstructorUsingGuice() throws Throwable {
        for (Object[] parameter : parameters) {
            @SuppressWarnings("unchecked")
            Object result = InstantiationInterceptor.instantiateObjectWithConstructor((Constructor<Object>) parameter[3], (Class<Object>) parameter[0], (Object[]) parameter[2], true, false);

            interceptor.reset();

            result.toString();

            assertThat(interceptor.invocations, is(1));
        }
    }

    @Test
    public void bindInterceptorToSpecialObjectsWithGuice() throws Throwable {
        for (Object[] parameter : parameters) {
            Object result = InstantiationInterceptor.instantiateObject((Class<?>) parameter[0], (Class<?>[]) parameter[1], (Object[]) parameter[2], true, false);

            interceptor.reset();

            result.toString();

            assertThat(interceptor.invocations, is(1));
        }
    }

    @Test
    public void doNotThrowExceptionIfNotRequired() throws Exception {
        try {
            InstantiationInterceptor.instantiateObject(StubComponent.class, new Class<?>[] { WOContext.class }, new Object[] { mockRequest }, false, true);
            InstantiationInterceptor.instantiateObject(StubObject.class, new Class<?>[] { String.class, String.class }, null, false, true);
            InstantiationInterceptor.instantiateObject(StubObject.class, null, null, false, true);
        } catch (Exception exception) {
            exception.printStackTrace();

            fail("Should not throw an exception");
        }
    }

    @Test
    public void injectOrdinaryObjectMembersAfterConstructionWithReflection() throws Exception {
        Object result = InstantiationInterceptor.instantiateObject(StubObject.class, new Class<?>[] { String.class, String.class }, new Object[] { "teste", "teste2" }, true, false);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(StubObject.class));
        assertThat(result.toString(), is("expectedText"));
    }

    @Test
    public void injectOrdinaryObjectMembersAfterConstructionWithReflectionUsingConstructor() throws Exception {
        Constructor<StubObject> constructor = StubObject.class.getConstructor(new Class<?>[] { String.class, String.class });

        Object result = InstantiationInterceptor.instantiateObjectWithConstructor(constructor, StubObject.class, new Object[] { "teste", "teste2" }, true, false);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(StubObject.class));
        assertThat(result.toString(), is("expectedText"));
    }

    @Test
    public void injectSpecialObjectsWithConstructorUsingGuice() throws Exception {
        for (Object[] parameter : parameters) {
            @SuppressWarnings("unchecked")
            Object result = InstantiationInterceptor.instantiateObjectWithConstructor((Constructor<Object>) parameter[3], (Class<Object>) parameter[0], (Object[]) parameter[2], true, false);

            assertThat(result.toString(), is("expectedText"));
        }
    }

    @Test
    public void injectSpecialObjectsWithGuice() throws Exception {
        for (Object[] parameter : parameters) {
            Object result = InstantiationInterceptor.instantiateObject((Class<?>) parameter[0], (Class<?>[]) parameter[1], (Object[]) parameter[2], true, false);

            assertThat(result.toString(), is("expectedText"));
        }
    }

    @Test
    public void instantiateObjectWithContructorUsingReflectionIfInjectorIsNull() throws Exception {
        application.setReturnNullInjector(true);

        Constructor<StubEnterpriseObject> constructor = StubEnterpriseObject.class.getConstructor();

        StubEnterpriseObject result = InstantiationInterceptor.instantiateObjectWithConstructor(constructor, StubEnterpriseObject.class, null, true, false);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(StubEnterpriseObject.class));
    }

    @Test
    public void instantiateObjectWithReflectionIfApplicationIsNull() throws Exception {
        StubApplication.setApplication(null);

        StubEnterpriseObject result = InstantiationInterceptor.instantiateObject(StubEnterpriseObject.class, null, null, true, false);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(StubEnterpriseObject.class));
    }

    @Test
    public void instantiateObjectWithReflectionIfInjectorIsNull() throws Exception {
        application.setReturnNullInjector(true);

        StubEnterpriseObject result = InstantiationInterceptor.instantiateObject(StubEnterpriseObject.class, null, null, true, false);

        assertThat(result, notNullValue());
        assertThat(result, instanceOf(StubEnterpriseObject.class));
    }

    @Test
    public void instantiateSpecialObjectsWithConstructorUsingGuice() throws Exception {
        for (Object[] parameter : parameters) {
            @SuppressWarnings("unchecked")
            Object result = InstantiationInterceptor.instantiateObjectWithConstructor((Constructor<Object>) parameter[3], (Class<Object>) parameter[0], (Object[]) parameter[2], true, false);

            assertThat(result, notNullValue());
            assertThat(result, instanceOf((Class<?>) parameter[0]));
        }
    }

    @Test
    public void instantiateSpecialObjectsWithGuice() throws Exception {
        for (Object[] parameter : parameters) {
            Object result = InstantiationInterceptor.instantiateObject((Class<?>) parameter[0], (Class<?>[]) parameter[1], (Object[]) parameter[2], true, false);

            assertThat(result, notNullValue());
            assertThat(result, instanceOf((Class<?>) parameter[0]));
        }
    }

    @Override
    @Before
    public void setup() throws Exception {
        final StubInterceptor interceptor = new StubInterceptor();

        this.interceptor = interceptor;

        application = new StubApplication() {
            @Override
            protected Module[] modules() {
                List<Module> modules = new ArrayList<Module>(Arrays.asList(super.modules()));

                modules.add(new StubModule(interceptor));

                return modules.toArray(new Module[modules.size()]);
            }
        };

        parameters = new Object[][] { { StubEnterpriseObject.class, null, null, StubEnterpriseObject.class.getConstructor() }, { StubComponent.class, new Class<?>[] { WOContext.class }, new Object[] { mockContext }, StubComponent.class.getConstructor(new Class<?>[] { WOContext.class }) }, { StubSession.class, null, null, StubSession.class.getConstructor() }, { StubDirectAction.class, new Class<?>[] { WORequest.class }, new Object[] { mockRequest }, StubDirectAction.class.getConstructor(new Class<?>[] { WORequest.class }) } };
    }

    @Test
    public void throwExceptionIfGuiceUnableToCreateInstance() throws Exception {
        thrown.expect(WOInjectException.class);
        thrown.expectMessage(is("The instantiation of com.woinject.stubs.StubComponent class has failed."));

        WORequest illegalArgument = mockRequest;

        InstantiationInterceptor.instantiateObject(StubComponent.class, new Class<?>[] { WOContext.class }, new Object[] { illegalArgument }, true, false);
    }

    @Test
    public void throwExceptionIfIllegalArguments() throws Exception {
        thrown.expect(WOInjectException.class);
        thrown.expectMessage(is("The instantiation of com.woinject.stubs.StubObject class has failed."));

        InstantiationInterceptor.instantiateObject(StubObject.class, new Class<?>[] { String.class, String.class }, null, true, false);
    }

    @Test
    public void throwExceptionIfNoSuitableConstructorFound() throws Exception {
        thrown.expect(WOInjectException.class);
        thrown.expectMessage(is("The instantiation of com.woinject.stubs.StubObject class has failed."));

        InstantiationInterceptor.instantiateObject(StubObject.class, null, null, true, false);
    }

    @Test
    public void useSpecialKeyToAvoidBindingConflictsWithParentInjector() throws Exception {
        StubObjectForBinding result = InstantiationInterceptor.instantiateObject(StubObjectForBinding.class, null, null, false, false);

        assertThat(new MockUtil().isMock(result), is(false));
    }
}

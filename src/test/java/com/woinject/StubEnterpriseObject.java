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

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * @author <a href="mailto:hprange@gmail.com">Henrique Prange</a>
 */
@SuppressWarnings("serial")
public class StubEnterpriseObject extends InjectableGenericRecord {
    @Inject
    @Named("field")
    private String injectableField;

    private String injetableMethod;

    public String getInjectableField() {
	return injectableField;
    }

    public String getInjectableMethod() {
	return injetableMethod;
    }

    @Inject
    public void initInjectableMethod(@Named("method") String value) {
	injetableMethod = value;
    }
}

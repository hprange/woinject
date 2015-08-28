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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author <a href="mailto:hprange@gmail.com.br">Henrique Prange</a>
 */
public class StubComponent extends WOComponent {
    @Inject
    @Named("test")
    private String injectedText;

    public StubComponent(WOContext context) {
        super(context);
    }

    @Override
    public String toString() {
        return injectedText;
    }
}

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

import com.google.inject.Provider;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOSession;

import er.extensions.appserver.ERXSession;
import er.extensions.appserver.ERXWOContext;

/**
 * This class provides the current session object or throws an exception if
 * unable to get it.
 *
 * @param <T>
 *            The session class type.
 * @since 1.0
 */
class WOSessionProvider<T extends WOSession> implements Provider<T> {
    /*
     * (non-Javadoc)
     *
     * @see com.google.inject.Provider#get()
     */
    @Override
    public T get() {
        T session = getOrNull();

        if (session == null) {
            throw new WOInjectException("Unable to provide the current session. Either the session has not been initialized yet, or it has expired.");
        }

        return session;
    }

    @SuppressWarnings("unchecked")
    protected T getOrNull() {
        T session = (T) ERXSession.session();

        if (session == null) {
            // No session, really? Lets dig a little deeper.
            WOContext context = ERXWOContext.currentContext();

            if (context != null) {
                // Be careful. We don't want to create a new session
                // accidentally.
                session = (T) context._session();

                if (session == null) {
                    String sessionId = context._requestSessionID();

                    if (sessionId != null) {
                        session = (T) WOApplication.application().restoreSessionWithID(sessionId, context);
                    }
                }
            }
        }

        return session;
    }
}

/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.makotan.ninja.auth.pac4j.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.Context;
import com.makotan.ninja.auth.pac4j.NinjaWebContext;
import org.pac4j.core.profile.CommonProfile;

@Singleton
public class UserUtils {
    private final static String PAC4J_PROFILE = "pac4jProfile";
    private static final String CONTEXT_PROFILE_NAME = "pac4j_context_profile";

    @Inject
    ProfileAccess profileAccess;

    /**
     * Return if the user is authenticated.
     *
     * @param context
     * @return if the user is authenticated
     */
    public boolean isAuthenticated(final Context context) {
        return getProfile(context , CommonProfile.class) != null;
    }

    /**
     * Read the profile from the request.
     *
     * @param context
     * @return the user profile
     */
    public <T extends CommonProfile> T getProfile(final Context context , Class<T> tClass) {
        Object profile = context.getAttribute(CONTEXT_PROFILE_NAME);
        if (profile != null) {
            return (T)profile;
        }

        NinjaWebContext nw = new NinjaWebContext(context);
        String id = (String) nw.getSessionAttribute(PAC4J_PROFILE);
        if (id == null) {
            return null;
        }
        return profileAccess.read(id);
    }

    /**
     * Save the profile in session.
     *
     * @param context
     * @param profile
     */
    public <T extends CommonProfile> void setProfile(final Context context, final T profile) {
        String id = profileAccess.write(profile);
        NinjaWebContext nw = new NinjaWebContext(context);
        nw.setSessionAttribute(PAC4J_PROFILE, id);
        context.setAttribute(CONTEXT_PROFILE_NAME , profile);
    }

    /**
     * Logout the user.
     *
     * @param context
     */
    public void logout(final Context context) {
        setProfile(context, null);
        context.setAttribute(CONTEXT_PROFILE_NAME , null);
    }

}

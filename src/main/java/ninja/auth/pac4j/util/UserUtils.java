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
package ninja.auth.pac4j.util;

import ninja.Context;
import ninja.auth.pac4j.NinjaWebContext;
import org.pac4j.core.profile.CommonProfile;

public class UserUtils {
    private final static String PAC4J_PROFILE = "pac4jProfile";
    /**
     * Return if the user is authenticated.
     *
     * @param context
     * @return if the user is authenticated
     */
    public static boolean isAuthenticated(final Context context) {
        return getProfile(context) != null;
    }

    /**
     * Read the profile from the request.
     *
     * @param context
     * @return the user profile
     */
    public static CommonProfile getProfile(final Context context) {
        NinjaWebContext nw = new NinjaWebContext(context);
        Object profile = nw.getSessionAttribute(PAC4J_PROFILE);
        if (profile == null) {
            return null;
        }
        return (CommonProfile)profile;
    }

    /**
     * Save the profile in session.
     *
     * @param context
     * @param profile
     */
    public static void setProfile(final Context context, final CommonProfile profile) {
        NinjaWebContext nw = new NinjaWebContext(context);
        nw.setSessionAttribute(PAC4J_PROFILE, profile);
    }

    /**
     * Logout the user.
     *
     * @param context
     */
    public static void logout(final Context context) {
        setProfile(context, null);
    }

}

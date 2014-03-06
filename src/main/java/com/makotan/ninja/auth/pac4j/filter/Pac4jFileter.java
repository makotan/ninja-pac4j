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
package com.makotan.ninja.auth.pac4j.filter;


import com.google.inject.Inject;
import com.makotan.ninja.auth.pac4j.NinjaWebContext;
import ninja.*;
import com.makotan.ninja.auth.pac4j.configuration.ClientsFactory;
import com.makotan.ninja.auth.pac4j.util.UserUtils;
import ninja.servlet.ContextImpl;
import ninja.utils.NinjaProperties;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;


public class Pac4jFileter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(Pac4jFileter.class);

    public static final String ORIGINAL_REQUESTED_URL = "pac4jOriginalRequestedUrl";

    @Inject
    NinjaProperties properties;

    @Inject
    ClientsFactory clientsFactory;

    public Result filter(FilterChain filterChain, Context context) {
        CommonProfile profile = UserUtils.getProfile(context , CommonProfile.class);
        logger.debug("profile : {}", profile);

        if (profile != null) {
            return filterChain.next(context);
        } else {
            WebContext web = new NinjaWebContext(context);
            if (ContextImpl.class.isInstance(context)) {
                ContextImpl impl = (ContextImpl) context;
                HttpServletRequest request = impl.getHttpServletRequest();

                String requestedUrl = request.getRequestURL().toString();
                String queryString = request.getQueryString();
                if (CommonHelper.isNotBlank(queryString)) {
                    requestedUrl += "?" + queryString;
                }
                logger.debug("requestedUrl : {}", requestedUrl);
                web.setSessionAttribute(ORIGINAL_REQUESTED_URL, requestedUrl);
            }

            String redirectUrl = properties.get("pac4j.auth_error_redirect");
            if (redirectUrl == null) {
                Clients clients = clientsFactory.build();
                String clientName = web.getRequestParameter(clients.getClientNameParameter());
                if (clientName == null) {
                    clientName = properties.get("pac4j.client_name");
                }
                Client<Credentials, CommonProfile> client = clients.findClient(clientName);
                redirectUrl = client.getRedirectionUrl(web);

            }
            logger.debug("redirectUrl : {}", redirectUrl);
            return Results.redirect(redirectUrl);

        }
    }

}

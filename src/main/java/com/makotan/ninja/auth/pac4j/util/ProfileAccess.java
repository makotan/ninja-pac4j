package com.makotan.ninja.auth.pac4j.util;

import org.pac4j.core.profile.CommonProfile;

/**
 * User: kuroeda.makoto
 * Date: 14/03/06
 * Time: 17:52
 */
public interface ProfileAccess {
    <T extends CommonProfile> T read(String id);
    <T extends CommonProfile> String write(T profile);

}

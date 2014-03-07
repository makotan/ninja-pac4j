package com.makotan.ninja.auth.pac4j.util;

import com.google.inject.Singleton;
import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: kuroeda.makoto
 * Date: 14/03/06
 * Time: 17:57
 */
@Singleton
public class SampleProfileAccess implements ProfileAccess {
    private static final Logger logger = LoggerFactory.getLogger(SampleProfileAccess.class);
    Map<String , CommonProfile> profiles = new HashMap<>();

    @Override
    public <T extends CommonProfile> T read(String id) {
        return (T) profiles.get(id);
    }

    @Override
    public <T extends CommonProfile> String write(T profile) {
        profiles.put(profile.getId() , profile);
        return profile.getId();
    }
}

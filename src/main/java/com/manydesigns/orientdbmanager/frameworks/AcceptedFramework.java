package com.manydesigns.orientdbmanager.frameworks;

import java.text.ParseException;

/**
 * Author: Emanuele Collura
 * Date: 20/06/22
 * Time: 17:41
 */
public enum AcceptedFramework {
    SPRING_BOOT,
    JAX_RS;

    public static AcceptedFramework parse(String s) throws ParseException {
        for (var f :
                AcceptedFramework.values()) {
            if (f.name().equalsIgnoreCase(s))
                return f;
        }

        throw new ParseException(s + " Framework not supported", 0);
    }
}

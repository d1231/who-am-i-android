package com.danny.projectt.services;

import org.apache.commons.lang3.StringUtils;

public class NameService {

    public String normalizeName(final String name) {

        String normalizedName = StringUtils.stripAccents(name);

        return normalizedName.toUpperCase();

    }

}

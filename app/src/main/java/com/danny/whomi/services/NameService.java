package com.danny.whomi.services;

import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

public class NameService {

    public String normalizeName(final String name) {

        String normalizedName = StringUtils.stripAccents(name);

        normalizedName = normalizedName.toUpperCase();

        normalizedName = removeNonEnglishLetters(normalizedName);

        normalizedName = normalizedName.trim().replaceAll("\\s+", " ");


        return normalizedName;

    }

    @NonNull
    private String removeNonEnglishLetters(final String normalizedName) {

        return normalizedName.replaceAll("[^A-Z ]", "");

    }

    public static NameService instance() {
        return new NameService();
    }
}

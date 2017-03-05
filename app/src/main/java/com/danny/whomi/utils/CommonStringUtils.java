package com.danny.whomi.utils;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

public class CommonStringUtils {

    public static Set<Character> getLettersSetFromString(String string) {

        final HashSet<Character> characters = Sets.newHashSet();

        for (int i = 0; i < string.length(); i++) {
            final char c = string.charAt(i);
            if (Character.isLetter(c)) {
                characters.add(Character.toUpperCase(c));
            }
        }

        return characters;

    }

}

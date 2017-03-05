package com.danny.whomi.model.objects;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class GameOptions {

    private static final int DEFUALT_YEAR_VALUE = -1;

    private int minYear;

    private Set<String> nations;

    public GameOptions() {

        this(DEFUALT_YEAR_VALUE, Sets.newHashSet());
    }

    public GameOptions(int minYear, Set<String> nations) {

        this.minYear = minYear;
        this.nations = nations;
    }

    public void addNations(String nation) {

        nations.add(nation);
    }

    public void removeNation(String nation) {

        nations.remove(nation);

    }

    public void resetNations() {

        nations.clear();

    }

    public void setMinYear(int minYear) {

        this.minYear = minYear;
    }

    public Map<String, String> toQueryMap() {

        final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

        if (minYear > DEFUALT_YEAR_VALUE) {
            builder.put("minYear", Integer.toString(minYear));
        }

        if (!nations.isEmpty()) {
            final String nations = Joiner.on(",").join(this.nations);
            builder.put("nations", nations);
        }

        return builder.build();

    }
}

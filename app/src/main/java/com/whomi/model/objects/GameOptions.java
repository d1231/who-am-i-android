package com.whomi.model.objects;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

public class GameOptions {

    private static final int DEFUALT_YEAR_VALUE = -1;

    private int minYear;

    private List<String> nations;

    public GameOptions() {

        this(DEFUALT_YEAR_VALUE, Sets.newHashSet());
    }

    public GameOptions(int minYear, Set<String> nations) {

        this.minYear = minYear;
        this.nations = Lists.newArrayList(nations);
    }

    public List<String> getNations() {
        return nations;
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

}

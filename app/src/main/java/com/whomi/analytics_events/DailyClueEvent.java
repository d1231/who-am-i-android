package com.whomi.analytics_events;

import com.whomi.services.WhomiAnalyticsService;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class DailyClueEvent extends WhomiAnalyticsService.AnalyticEvent {

    private long previous;

    private DailyClueEvent(long previous) {
        this.previous = previous;
    }

    public static DailyClueEvent create(long previous) {
        return new DailyClueEvent(previous);
    }

    @Override
    public String name() {
        return "DailyClue";
    }

    @Override
    public Map<String, String> data() {

        return ImmutableMap.of("Previous clue", String.valueOf(previous));

    }
}

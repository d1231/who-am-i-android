package com.whomi.analytics_events;

import com.whomi.services.WhomiAnalyticsService;

public class GameScreenEnterEvent extends WhomiAnalyticsService.AnalyticEvent {

    public static GameScreenEnterEvent create() {
        return new GameScreenEnterEvent();
    }

    @Override
    protected String name() {
        return "GameScreenEnter";
    }

    private GameScreenEnterEvent() {
    }
}

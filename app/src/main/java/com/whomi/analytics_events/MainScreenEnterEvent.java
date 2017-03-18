package com.whomi.analytics_events;

import com.whomi.services.WhomiAnalyticsService;

public class MainScreenEnterEvent extends WhomiAnalyticsService.AnalyticEvent {

    public static MainScreenEnterEvent create() {
        return new MainScreenEnterEvent();
    }

    @Override
    protected String name() {
        return "MainScreenEnter";
    }

    private MainScreenEnterEvent() {
    }
}

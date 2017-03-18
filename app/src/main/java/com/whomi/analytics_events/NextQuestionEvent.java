package com.whomi.analytics_events;

import com.whomi.services.WhomiAnalyticsService;

public class NextQuestionEvent extends WhomiAnalyticsService.AnalyticEvent {

    public static NextQuestionEvent create() {
        return new NextQuestionEvent();
    }

    @Override
    protected String name() {
        return "NextQuestion";
    }

    private NextQuestionEvent() {
    }
}

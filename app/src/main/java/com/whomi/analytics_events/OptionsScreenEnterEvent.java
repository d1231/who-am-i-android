package com.whomi.analytics_events;

import com.whomi.services.WhomiAnalyticsService;

/**
 * Created by danny on 11-Mar-17.
 */

public class OptionsScreenEnterEvent extends WhomiAnalyticsService.AnalyticEvent {

    private OptionsScreenEnterEvent() {
    }

    public static OptionsScreenEnterEvent create() {
        return new OptionsScreenEnterEvent();
    }


    @Override
    public String name() {
        return "OptionsScreenEnter";
    }

}

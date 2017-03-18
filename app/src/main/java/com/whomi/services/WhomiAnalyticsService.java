package com.whomi.services;

import android.os.Bundle;

import com.google.common.collect.Maps;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

import javax.inject.Inject;

public class WhomiAnalyticsService {

    private FirebaseAnalytics analyticsService;

    @Inject
    public WhomiAnalyticsService(FirebaseAnalytics analyticsService) {

        this.analyticsService = analyticsService;
    }

    public void logEvent(AnalyticEvent analyticEvent) {

        Bundle bundle = new Bundle();

        Map<String, String> analyticData = analyticEvent.data();
        for (Map.Entry<String, String> entry : analyticData.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        analyticsService.logEvent(analyticEvent.name(), bundle);
    }

    public static abstract class AnalyticEvent {

        protected abstract String name();

        protected Map<String, String> data() {
            return Maps.newHashMap();
        }

    }

}

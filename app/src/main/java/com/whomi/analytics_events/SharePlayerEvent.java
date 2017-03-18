package com.whomi.analytics_events;

import com.whomi.model.objects.Player;
import com.whomi.services.WhomiAnalyticsService;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SharePlayerEvent extends WhomiAnalyticsService.AnalyticEvent {
    private Player player;

    public static SharePlayerEvent create(Player player) {
        return new SharePlayerEvent(player);
    }

    @Override
    protected String name() {
        return "SharePlayer";
    }

    private SharePlayerEvent(Player player) {
        this.player = player;
    }

    @Override
    protected Map<String, String> data() {
        return ImmutableMap.of("playerName", player.name(), "playerId", player.id());
    }
}

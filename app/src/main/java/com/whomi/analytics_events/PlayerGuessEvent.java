package com.whomi.analytics_events;

import com.whomi.model.objects.Player;
import com.whomi.services.WhomiAnalyticsService;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class PlayerGuessEvent extends WhomiAnalyticsService.AnalyticEvent {
    private Player player;
    private List<Character> game;

    public static PlayerGuessEvent create(Player player, List<Character> game) {
        return new PlayerGuessEvent(player, game);
    }

    @Override
    protected String name() {
        return "SkipPlayer";
    }

    private PlayerGuessEvent(Player player, List<Character> game) {
        this.player = player;
        this.game = game;
    }

    @Override
    protected Map<String, String> data() {
        return ImmutableMap.of("playerName", player.name(), "playerId", player.id(), "game", game.toString());
    }
}

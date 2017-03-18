package com.whomi.analytics_events;

import com.whomi.fragments.DialogResult;
import com.whomi.model.objects.Player;
import com.whomi.services.WhomiAnalyticsService;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class SkipPlayerEvent extends WhomiAnalyticsService.AnalyticEvent {
    private Player player;
    private DialogResult dialogResult;
    private List<Character> game;

    public static SkipPlayerEvent create(Player player, DialogResult dialogResult, List<Character> game) {
        return new SkipPlayerEvent(player, dialogResult, game);
    }

    @Override
    protected String name() {
        return "SkipPlayer";
    }

    private SkipPlayerEvent(Player player, DialogResult dialogResult, List<Character> game) {
        this.player = player;
        this.dialogResult = dialogResult;
        this.game = game;
    }

    @Override
    protected Map<String, String> data() {
        return ImmutableMap.of("playerName", player.name(), "playerId", player.id(), "dialogResult", dialogResult.toString(), "game", game.toString());
    }
}

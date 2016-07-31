package com.danny.projectt.utils;

import com.danny.projectt.model.objects.Player;
import com.danny.projectt.model.objects.Stats;
import com.danny.projectt.model.objects.TeamHistory;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class AutoValueTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        Class<? super T> rawType = type.getRawType();

        if (rawType.equals(Player.class)) {
            return (TypeAdapter<T>) Player.typeAdapter(gson);
        } else if (rawType.equals(TeamHistory.class)) {
            return (TypeAdapter<T>) TeamHistory.typeAdapter(gson);
        } else if (rawType.equals(Stats.class)) {
            return (TypeAdapter<T>) Stats.typeAdapter(gson);
        }

        return null;
    }
}

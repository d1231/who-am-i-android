package com.danny.whomi.model.objects;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Player implements Parcelable {

    public static Player create(String id, String name, String position, String placeOfBirth, String dateOfBirth, List<TeamHistory> teamHistory) {

        return new AutoValue_Player(id, name, position, placeOfBirth, dateOfBirth, teamHistory);
    }

    public static TypeAdapter<Player> typeAdapter(Gson gson) {

        return new AutoValue_Player.GsonTypeAdapter(gson);
    }

    @SerializedName("_id")
    public abstract String id();

    public abstract String name();

    public abstract String position();

    public abstract String placeOfBirth();

    public abstract String dateOfBirth();

    @SerializedName("teams")
    public abstract List<TeamHistory> teamHistory();

}
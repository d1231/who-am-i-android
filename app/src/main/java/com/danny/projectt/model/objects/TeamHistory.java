package com.danny.projectt.model.objects;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class TeamHistory implements Parcelable {

    public static TypeAdapter<TeamHistory> typeAdapter(Gson gson) {

        return new AutoValue_TeamHistory.GsonTypeAdapter(gson);
    }

    @SerializedName("team")
    public abstract String teamName();

    @SerializedName("start")
    public abstract int startYear();

    @SerializedName("end")
    public abstract int endYear();

    public abstract boolean aLoan();

    public abstract Stats leagueStats();

}

package com.danny.whomi.model.objects;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class TeamHistory implements Parcelable{

    public static TeamHistory create(String teamName, int startYear, int endYear, boolean aLoan, int apps, int goals) {

        return new AutoValue_TeamHistory(teamName, startYear, endYear, aLoan, Stats.create(apps, goals));
    }

    public static TypeAdapter<TeamHistory> typeAdapter(Gson gson) {

        return new AutoValue_TeamHistory.GsonTypeAdapter(gson);
    }

    @SerializedName("team")
    public abstract String teamName();

    @SerializedName("start")
    public abstract int startYear();

    @SerializedName("end")
    public abstract int endYear();

    @SerializedName("loan")
    public abstract boolean aLoan();

    public abstract Stats leagueStats();

}

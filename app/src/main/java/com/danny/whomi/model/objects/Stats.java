package com.danny.whomi.model.objects;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

@AutoValue
public abstract class Stats implements Parcelable {

    public static Stats create(int apps, int goals) {

        return new AutoValue_Stats(apps, goals);
    }


    public static TypeAdapter<Stats> typeAdapter(Gson gson) {

        return new AutoValue_Stats.GsonTypeAdapter(gson);
    }


    public abstract int apps();

    public abstract int goals();


}

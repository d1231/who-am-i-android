package com.danny.whomi.model.network;

import com.danny.whomi.model.objects.Player;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface BackendService {

    @GET("/players")
    Observable<List<Player>> getPlayer(@QueryMap Map<String, String> options);

    @GET("/nations")
    Observable<List<String>> getAvailableNations();
}

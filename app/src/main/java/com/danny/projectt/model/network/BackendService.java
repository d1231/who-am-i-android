package com.danny.projectt.model.network;

import com.danny.projectt.model.objects.Player;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface BackendService {

    @GET("/players")
    Observable<List<Player>> getPlayer(@QueryMap Map<String, String> options);

    @GET("/players")
    Observable<List<Player>> getPlayer();

    @GET("/nations")
    Observable<List<String>> getAvailableNations();
}

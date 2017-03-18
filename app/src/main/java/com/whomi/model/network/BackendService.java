package com.whomi.model.network;

import com.whomi.model.objects.GameOptions;
import com.whomi.model.objects.Player;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface BackendService {

    @POST("/players/sample")
    Observable<List<Player>> getPlayer(@Body GameOptions options);

    @GET("/nations")
    Observable<List<String>> getAvailableNations();
}

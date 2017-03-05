package com.danny.whomi.components;

import android.content.SharedPreferences;

import com.danny.whomi.model.network.BackendService;
import com.danny.whomi.model.objects.Player;
import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;

import org.mockito.Mock;
import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;

@Module
public class TestModule {

    @Provides
    public ObjectQueue<Player> providePlayerObjectQueue() {
        return new InMemoryObjectQueue<>();
    }

    @Provides
    public SharedPreferences provideSharedPreferences() {
        return new InMemorySharedPreferences();
    }

    @Provides
    public BackendService backendService() {
        return Mockito.mock(BackendService.class);
    }
}

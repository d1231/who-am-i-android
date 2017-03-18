package sharedTest;

import android.content.SharedPreferences;

import com.whomi.model.network.BackendService;
import com.whomi.model.objects.Player;
import com.whomi.services.ClueService;
import com.whomi.services.PlayerService;
import com.whomi.services.WhomiAnalyticsService;
import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;

import org.mockito.Mockito;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import sharedTest.InMemorySharedPreferences;

@Module
public class TestModule {

    @Provides
    @TestScope
    public ObjectQueue<Player> providePlayerObjectQueue() {
        return new InMemoryObjectQueue<>();
    }

    @Provides
    @TestScope
    public SharedPreferences provideSharedPreferences() {
        return new InMemorySharedPreferences();
    }

    @Provides
    public WhomiAnalyticsService provideWhomiAnalyticsService() {
        return Mockito.mock(WhomiAnalyticsService.class);
    }

    @Provides
    @TestScope
    public BackendService backendService() {
        return Mockito.mock(BackendService.class);
    }

    public PlayerService providePlayerService() {
        return Mockito.mock(PlayerService.class);
    }

}

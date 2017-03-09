package sharedTest;

import android.content.SharedPreferences;

import com.danny.whomi.model.network.BackendService;
import com.danny.whomi.model.objects.Player;
import com.danny.whomi.services.ClueService;
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
    @TestScope
    public BackendService backendService() {
        return Mockito.mock(BackendService.class);
    }

    @Provides
    @TestScope
    public ClueService provideMockClueService() {
        return Mockito.mock(ClueService.class);
    }
}

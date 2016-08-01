package com.danny.projectt.dagger;

import com.danny.projectt.dagger.application.ApplicationComponent;
import com.danny.projectt.dagger.application.ApplicationModule;
import com.danny.projectt.dagger.application.DataModule;
import com.danny.projectt.dagger.application.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MockApplicationModule.class,
        MockNetworkModule.class,
        MockDataModule.class
}
)
public interface TestComponent extends ApplicationComponent {

}

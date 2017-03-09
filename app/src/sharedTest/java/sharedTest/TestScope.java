package sharedTest;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by danny on 09-Mar-17.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface TestScope {
}

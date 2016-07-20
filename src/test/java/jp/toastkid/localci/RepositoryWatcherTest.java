package jp.toastkid.localci;

import java.io.IOException;
import java.time.Duration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import reactor.test.TestSubscriber;

/**
 * {@link RepositoryWatcherFactory}'s test.
 * @author Toast kid
 *
 */
public class RepositoryWatcherTest {

    /**
     * check success.
     * @throws IOException
     */
    @Test
    public void successCase() throws IOException {
        final RepositoryWatcherFactory watcher = new RepositoryWatcherFactory("./.git", "master");
        Whitebox.setInternalState(watcher, "latestHash", "dummy");
        TestSubscriber
            .subscribe(watcher.makeFlux())
            .configureValuesTimeout(Duration.ofSeconds(1L))
            .assertValuesWith(Assert::assertNotNull);
    }

    /**
     * check failure.
     * @throws IOException
     */
    @Test(expected=IllegalArgumentException.class)
    public void defunctCase() throws IOException {
        new RepositoryWatcherFactory("./.git", "notExists");
    }

}

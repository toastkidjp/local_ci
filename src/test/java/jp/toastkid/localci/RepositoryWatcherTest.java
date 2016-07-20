package jp.toastkid.localci;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;

public class RepositoryWatcherTest {

    @Test
    public void test() throws IOException {
        final RepositoryWatcher watcher = new RepositoryWatcher("./.git");
        assertNotNull(watcher.findLatestRevisionHash("master"));
        watcher.findLatestRevisionHash("not_exists");
    }

}

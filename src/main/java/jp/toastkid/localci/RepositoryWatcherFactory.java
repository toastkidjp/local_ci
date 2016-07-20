package jp.toastkid.localci;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.gradle.impldep.org.apache.commons.lang.StringUtils;

import reactor.core.publisher.Flux;

/**
 * Repository watcher with JGit.
 *
 * @author Toast kid
 *
 */
public class RepositoryWatcherFactory {

    /** git repository. */
    private final Repository repo;

    /** target repo's branch. */
    private final String branch;

    /** latest revision hash of target repo. */
    private String latestHash;

    /**
     * make instance.
     * If you passed defunct branch, it throws IllegalArgumentException this constructor.
     * @param path
     * @param branch
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public RepositoryWatcherFactory(final String path, final String branch) throws IOException {
        this.repo = new FileRepositoryBuilder()
                .setGitDir(new File(path))
                .build();
        this.branch = branch;
        latestHash = findLatestRevisionHash();
        if (StringUtils.isEmpty(latestHash)) {
            throw new IllegalArgumentException("Specified branch is not exists.");
        }
    }

    /**
     * find revision hash.
     * @return revision hash
     * @throws IOException
     */
    private String findLatestRevisionHash() throws IOException {
        final Ref ref = this.repo.findRef(branch);
        return ref != null
                ? ref.getObjectId().name()
                : "";
    }

    /**
     * make flux object.
     * @return Flux
     */
    public Flux<String> makeFlux() {
        return Flux.create(emitter -> {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> emitter.complete()));
            try {
                final String newHash = findLatestRevisionHash();
                if (!latestHash.equals(newHash)) {
                    emitter.next(newHash);
                    latestHash = findLatestRevisionHash();
                }
            } catch (final Exception e) {
                e.printStackTrace();
                emitter.fail(e);
            }
        });
    }
}

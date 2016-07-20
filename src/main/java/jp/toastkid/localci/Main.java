package jp.toastkid.localci;

import java.io.IOException;
import java.util.List;

import org.eclipse.collections.impl.factory.Lists;

/**
 * Main class of Local CI.
 *
 * @author Toast kid
 *
 */
public class Main {

    /** checking interval. */
    private static final long INTERVAL = 100L;

    /** path to repo. */
    private static String projectDir = "../Yobidashi/";

    /** target branch. */
    private static String branch = "master";

    /**
     * main.
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        System.out.println("Start Local CI!");

        initByArgs(args);
        watchAndBuild();
    }

    /**
     * do Watching and Building.
     * @throws IOException
     * @throws InterruptedException
     */
    private static void watchAndBuild() throws IOException, InterruptedException {
        final RepositoryWatcherFactory watcher
            = new RepositoryWatcherFactory(projectDir  + ".git", branch);
        final CiBuilder builder = new CommandsBuilder(makeCommands());
        watcher.makeFlux()
            .takeMillis(INTERVAL)
            .subscribe(str -> builder.build());
        while (true) {
            Thread.sleep(INTERVAL);
        }
    }

    /**
     * init params by args.
     * @param args
     */
    private static void initByArgs(final String[] args) {
        if (1 < args.length) {
            projectDir = args[0];
        }

        if (1 < args.length) {
            branch = args[1];
        }

        System.out.println("Target repository: " + projectDir);
        System.out.println("Target branch:     " + branch);
    }

    /**
     * make Commands.
     * @return command list.
     */
    private static List<String[]> makeCommands() {
        return Lists.mutable.of(
                new String[]{"gradle", "clean", "jar", "del"},
                new String[]{}
                );
    }

}

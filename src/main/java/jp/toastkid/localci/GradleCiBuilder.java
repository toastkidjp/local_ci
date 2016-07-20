package jp.toastkid.localci;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnectionException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.ResultHandler;
import org.gradle.util.GradleVersion;

/**
 *
 * @author Toast kid
 *
 */
public class GradleCiBuilder implements CiBuilder {

    private final String projectDir;
    private final GradleConnector connector;
    private final String[] tasks;

    /**
     *
     * @param projectDir
     */
    public GradleCiBuilder(
            final String projectDir, final String gradleVersion, final String... tasks) {
        this.projectDir    = projectDir;

        if (tasks == null || tasks.length == 0) {
            throw new IllegalStateException("You should pass tasks.");
        }

        this.tasks = tasks;
        this.connector = GradleConnector.newConnector();
        connector.useGradleVersion(
                Optional.ofNullable(gradleVersion).orElse(GradleVersion.current().getVersion()))
                    .forProjectDirectory(new File(projectDir));
    }

    @Override
    public void build() {
        final ProjectConnection connect  = connector.connect();
        final BuildLauncher     launcher = connect.newBuild();
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream();
             final ByteArrayOutputStream err = new ByteArrayOutputStream();) {
            launcher.setStandardError(err);
            launcher.setStandardOutput(out);
            launcher.forTasks(tasks);
            final CompletableFuture<GradleConnectionException> future = new CompletableFuture<>();
            launcher.run(new ResultHandler<Void>() {
                @Override
                public void onComplete(final Void result) {
                    System.out.println("gradle execution complete");
                    future.complete(null);
                }

                @Override
                public void onFailure(final GradleConnectionException failure) {
                    future.complete(failure);
                }
            });

            try {
                final GradleConnectionException gce = future.get();
                if (out.size() > 0) {
                    System.out.println(new String(out.toByteArray()));
                }
                if (err.size() > 0) {
                    System.err.println(new String(err.toByteArray()));;
                }
                gce.printStackTrace();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();;
            }
        } catch (final IOException e1) {
            e1.printStackTrace();
        }
    }

}

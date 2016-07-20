package jp.toastkid.localci;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.block.factory.Procedures;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

/**
 * Simple command builder.
 *
 * @author Toast kid
 *
 */
public class CommandsBuilder implements CiBuilder {

    /** Process builder. */
    private final MutableList<ProcessBuilder> processes;

    /**
     *
     * @param commands
     */
    public CommandsBuilder(final List<String[]> commands) {
        processes = Lists.immutable.ofAll(commands)
                .reject( c -> c == null || c.length == 0)
                .collect(c -> new ProcessBuilder(c)).toList();
    }

    /**
     *
     * @param pd
     */
    public CommandsBuilder(final ProcessBuilder... pd) {
        this.processes = ArrayAdapter.adapt(pd).toList();
    }

    @Override
    public void build() {
        this.processes.each(Procedures.throwing(pd -> {
            final Process process = pd.start();
            try (final BufferedReader reader
                    = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                reader.lines().forEach(System.out::println);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }));
    }

}

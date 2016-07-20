package jp.toastkid.localci;

import java.util.ArrayList;

import org.junit.Test;

/**
 * {@link CommandsBuilder}'s test case.
 *
 * @author Toast kid
 *
 */
public class CommandsBuilderTest {

    /**
     * check build.
     */
    @Test
    public void testBuild() {
        new CommandsBuilder(new ArrayList<String[]>(){{add(new String[]{"ls", "-l"});}}).build();;
    }

}

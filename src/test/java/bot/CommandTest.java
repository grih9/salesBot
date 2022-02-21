package bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandTest {

    @Test
    public void values() {
        Assertions.assertEquals(Command.values().length, 5);
    }
}

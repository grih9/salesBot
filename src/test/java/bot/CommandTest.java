package bot;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTest {

    @Test
    public void values() {
        Assert.assertEquals(Command.values().length, 5);
    }
}

package utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class UtilsTest {
    private final String USERNAME = "username";
    private final String FIRSTNAME = "Ivan";
    private final String LASTNAME = "Ivanov";
    private User userWithUsername;
    private User userWithoutUsername;
    private Utils utils;

    @Before
    public void setUser() {
        utils = new Utils();
        userWithUsername = new User();
        userWithUsername.setUserName(USERNAME);
        userWithUsername.setFirstName(FIRSTNAME);
        userWithUsername.setLastName(LASTNAME);

        userWithoutUsername = new User();
        userWithoutUsername.setFirstName(FIRSTNAME);
        userWithoutUsername.setLastName(LASTNAME);
    }

    @Test
    public void getUserName() {
        Message message = new Message();
        message.setFrom(userWithUsername);
        Assert.assertEquals(Utils.getUserName(message), USERNAME);

        message.setFrom(userWithoutUsername);
        Assert.assertEquals(Utils.getUserName(message), LASTNAME + " " + FIRSTNAME);
    }

    @Test
    public void testGetUserName() {
        Assert.assertEquals(Utils.getUserName(userWithUsername), USERNAME);

        StringBuilder sb = new StringBuilder();
        sb.append(LASTNAME).append(" ").append(FIRSTNAME);
        Assert.assertEquals(Utils.getUserName(userWithoutUsername), sb.toString());
    }
}

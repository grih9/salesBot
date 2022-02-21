package utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class UtilsTest {
    private static final String USERNAME = "username";
    private static final String FIRSTNAME = "Ivan";
    private static final String LASTNAME = "Ivanov";
    private static User userWithUsername;
    private static User userWithoutUsername;
    private static Utils utils;

    @BeforeAll
    static void setUser() {
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
        Assertions.assertEquals(Utils.getUserName(message), USERNAME);

        message.setFrom(userWithoutUsername);
        Assertions.assertEquals(Utils.getUserName(message), LASTNAME + " " + FIRSTNAME);
    }

    @Test
    public void testGetUserName() {
        Assertions.assertEquals(Utils.getUserName(userWithUsername), USERNAME);

        StringBuilder sb = new StringBuilder();
        sb.append(LASTNAME).append(" ").append(FIRSTNAME);
        Assertions.assertEquals(Utils.getUserName(userWithoutUsername), sb.toString());
    }
}

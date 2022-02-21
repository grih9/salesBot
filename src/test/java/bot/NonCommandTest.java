package bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NonCommandTest {
    private final String testStringWithoutSpaces = "1,2,30";
    private final String testStringWithSpaces = "1, 2, 30";
    private final String testStringWithDoubledCommasWithSpaces = ",1 , 2, 30,";

    private final String testStringWithDoubledCommasWithoutSpaces = "1,,2,30";
    private final String testStringWithDoubledCommasInBeginningWithSpace = ",,,1 ,, 2, 30,";
    private final String testStringWithDoubledCommasWithSpace = "1,,,, 2,, 30";

    private final String testStringWithDigitOnly = "1";
    private final String testStringWithCommaOnly = ",";
    private final String testStringWithDuplicatedDigits = "1,2,30,1";
    private final String testStringWithZero = "0";
    private final String testStringWithNegativeNumber = "-1";
    private final String testStringWithDotOnly = ".";

    private final int[] NUMBERS = {1, 2, 30};
    NonCommand nonCommand = new NonCommand();

    @Test
    public void checkValid() {
        Assertions.assertTrue(nonCommand.checkValid(testStringWithoutSpaces));
        Assertions.assertTrue(nonCommand.checkValid(testStringWithSpaces));
        Assertions.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasWithSpaces));
        Assertions.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasWithoutSpaces));
        Assertions.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasInBeginningWithSpace));
        Assertions.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasWithSpace));

        Assertions.assertTrue(nonCommand.checkValid(testStringWithDigitOnly));
        Assertions.assertTrue(nonCommand.checkValid(testStringWithDuplicatedDigits));
        Assertions.assertTrue(nonCommand.checkValid(testStringWithZero));
        Assertions.assertFalse(nonCommand.checkValid(testStringWithNegativeNumber));
        Assertions.assertFalse(nonCommand.checkValid(testStringWithDotOnly));
        Assertions.assertFalse(nonCommand.checkValid(testStringWithCommaOnly));
    }

    @Test
    public void getNumbers() {
        Assertions.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithoutSpaces)));
        Assertions.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithSpaces)));
        Assertions.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasWithSpaces)));
        Assertions.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasWithoutSpaces)));
        Assertions.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasInBeginningWithSpace)));
        Assertions.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasWithSpace)));

        Assertions.assertTrue(checkArraysAreEqual(new int[]{1}, nonCommand.getNumbers(testStringWithDigitOnly)));
        Assertions.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDuplicatedDigits)));
        Assertions.assertTrue(checkArraysAreEqual(new int[]{0}, nonCommand.getNumbers(testStringWithZero)));
        Assertions.assertTrue(checkArraysAreEqual(new int[]{-1}, nonCommand.getNumbers(testStringWithNegativeNumber)));
        Assertions.assertTrue(checkArraysAreEqual(new int[]{}, nonCommand.getNumbers(testStringWithCommaOnly)));
    }

    private boolean checkArraysAreEqual(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void removeDuplicatesCommas() {
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithoutSpaces), testStringWithoutSpaces);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithSpaces), testStringWithSpaces);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasWithSpaces), testStringWithDoubledCommasWithSpaces);

        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasWithoutSpaces), testStringWithoutSpaces);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasInBeginningWithSpace), testStringWithDoubledCommasWithSpaces);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasWithSpace), testStringWithSpaces);

        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDigitOnly),testStringWithDigitOnly);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDuplicatedDigits),testStringWithDuplicatedDigits);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithZero),testStringWithZero);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithNegativeNumber),testStringWithNegativeNumber);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithCommaOnly),testStringWithCommaOnly);
        Assertions.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDotOnly),testStringWithDotOnly);
    }
}

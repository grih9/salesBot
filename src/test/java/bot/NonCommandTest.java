package bot;

import org.junit.Assert;
import org.junit.Test;

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
        Assert.assertTrue(nonCommand.checkValid(testStringWithoutSpaces));
        Assert.assertTrue(nonCommand.checkValid(testStringWithSpaces));
        Assert.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasWithSpaces));
        Assert.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasWithoutSpaces));
        Assert.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasInBeginningWithSpace));
        Assert.assertTrue(nonCommand.checkValid(testStringWithDoubledCommasWithSpace));

        Assert.assertTrue(nonCommand.checkValid(testStringWithDigitOnly));
        Assert.assertTrue(nonCommand.checkValid(testStringWithDuplicatedDigits));
        Assert.assertTrue(nonCommand.checkValid(testStringWithZero));
        Assert.assertFalse(nonCommand.checkValid(testStringWithNegativeNumber));
        Assert.assertFalse(nonCommand.checkValid(testStringWithDotOnly));
        Assert.assertFalse(nonCommand.checkValid(testStringWithCommaOnly));
    }

    @Test
    public void getNumbers() {
        Assert.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithoutSpaces)));
        Assert.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithSpaces)));
        Assert.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasWithSpaces)));
        Assert.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasWithoutSpaces)));
        Assert.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasInBeginningWithSpace)));
        Assert.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDoubledCommasWithSpace)));

        Assert.assertTrue(checkArraysAreEqual(new int[]{1}, nonCommand.getNumbers(testStringWithDigitOnly)));
        Assert.assertTrue(checkArraysAreEqual(NUMBERS, nonCommand.getNumbers(testStringWithDuplicatedDigits)));
        Assert.assertTrue(checkArraysAreEqual(new int[]{0}, nonCommand.getNumbers(testStringWithZero)));
        Assert.assertTrue(checkArraysAreEqual(new int[]{-1}, nonCommand.getNumbers(testStringWithNegativeNumber)));
        Assert.assertTrue(checkArraysAreEqual(new int[]{}, nonCommand.getNumbers(testStringWithCommaOnly)));
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
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithoutSpaces), testStringWithoutSpaces);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithSpaces), testStringWithSpaces);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasWithSpaces), testStringWithDoubledCommasWithSpaces);

        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasWithoutSpaces), testStringWithoutSpaces);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasInBeginningWithSpace), testStringWithDoubledCommasWithSpaces);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDoubledCommasWithSpace), testStringWithSpaces);

        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDigitOnly),testStringWithDigitOnly);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDuplicatedDigits),testStringWithDuplicatedDigits);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithZero),testStringWithZero);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithNegativeNumber),testStringWithNegativeNumber);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithCommaOnly),testStringWithCommaOnly);
        Assert.assertEquals(nonCommand.removeDuplicatesCommas(testStringWithDotOnly),testStringWithDotOnly);
    }
}

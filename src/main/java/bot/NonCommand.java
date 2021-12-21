package bot;

import javax.ws.rs.core.Link;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public class NonCommand {
    public Boolean checkValid(String text) {
        text = text.replaceAll(" ", "");
        text = text.replaceAll("\\t", "");
        text = text.replaceAll("\\s", "");
        Pattern pattern = Pattern.compile("((,*[ ]*)*[0-9]+(,*[ ]*)*)+");
        //Pattern pattern = Pattern.compile("(,*[ ]*)*[0-9]+");
        System.out.println(pattern.matcher(text).pattern());

        Matcher m = pattern.matcher(text);
        if(m.find())
        {
            System.out.println("TAG " + m.group());
        }

        return pattern.matcher(text).matches();
    }

    public int[] getNumbers(String text){
        text = text.replaceAll(" ", "");
        text = text.replaceAll("\\t", "");
        text = text.replaceAll("\\s", "");
        String removeDuplicates = removeDuplicatesCommas(text);

        if (removeDuplicates.endsWith(",")) {
            removeDuplicates = removeDuplicates.substring(0, removeDuplicates.length() - 1);
        }

        if (removeDuplicates.indexOf(",") == 0) {
            removeDuplicates = removeDuplicates.substring(1, removeDuplicates.length());
        }
        String[] words = removeDuplicates.split(",");

        LinkedHashSet<Integer> numbers = new LinkedHashSet<>();
        for (String word : words) {
            numbers.add(Integer.valueOf(word));
        }

        return numbers.stream().mapToInt(Number::intValue).toArray();
    }

    public String removeDuplicatesCommas(String input){
        StringBuilder result = new StringBuilder(String.valueOf(input.charAt(0)));
        for (int i = 1; i < input.length(); i++) {
            if (!String.valueOf(input.charAt(i)).equals(String.valueOf(input.charAt(i - 1))) ||
                    (String.valueOf(input.charAt(i)).equals(String.valueOf(input.charAt(i - 1))) && !String.valueOf(input.charAt(i)).equals(","))) {
                result.append(String.valueOf(input.charAt(i)));
            }
        }
        return result.toString();
    }
}

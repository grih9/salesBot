package bot;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public class NonCommand {
    public int[] getNumbers(String text){
        String removeDuplicates = removeDuplicates(text);

        if (removeDuplicates.endsWith(",")) {
            removeDuplicates = removeDuplicates.substring(0, removeDuplicates.length() - 2);
        }

        if (removeDuplicates.indexOf(",") == 0) {
            removeDuplicates = removeDuplicates.substring(1, removeDuplicates.length() - 1);
        }
        String[] words = removeDuplicates.split(",");

        int[] numbers = new int[words.length];
        int i = 0;
        for (String word : words) {
            numbers[i] = Integer.parseInt(word);
            i++;
        }
        return numbers;
    }

    public String removeDuplicates(String input){
        String result = "";
        for (int i = 0; i < input.length(); i++) {
            if(!result.contains(String.valueOf(input.charAt(i)))) {
                result += String.valueOf(input.charAt(i));
            }
        }
        return result;
    }
}

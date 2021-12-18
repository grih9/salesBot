package bot;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 */
public class NonCommand {
    public int[] getNumbers(String text){
        String[] words = text.split(",");
        int[] numbers = new int[words.length];
        int i = 0;
        for (String word : words) {
            numbers[i] = Integer.parseInt(word);
            i++;
        }
        return numbers;
    }

}

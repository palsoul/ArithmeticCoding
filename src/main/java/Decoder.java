import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author: Brotherdos
 * @date: 17.10.2016.
 */
public class Decoder {
    public static void main(String[] args) {
        try {
            FileWorker.write("decoded.txt", decode(FileWorker.read("encoded.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String decode(String sCode) {
        StringBuilder text = new StringBuilder();
        double code = Double.parseDouble(sCode);
        String[] dictionary;
        try {
            dictionary = FileWorker.read("dictionary.txt").split("\n");
        } catch (FileNotFoundException e) {
            dictionary = new String[0];
            e.printStackTrace();
        }
        String[][] symbolRanges = buildSymbolRanges(dictionary);

        int length = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            length = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (length > 0) {
            for (String[] symbolRange : symbolRanges) {
                if ((Double.parseDouble(symbolRange[1]) < code) && (code < Double.parseDouble(symbolRange[2]))) {
                    text.append(symbolRange[0]);
                    double low = Double.parseDouble(symbolRange[1]);
                    double high = Double.parseDouble(symbolRange[2]);
                    code = (code - low) / (high - low);
                    System.out.println(code);
                    break;
                }
            }
            length--;
        }
        return text.toString();
    }

    private static String[][] buildSymbolRanges(String[] dictionary) {
        String[][] symbolRanges = new String[dictionary.length][3];
        for (int i = 0; i < dictionary.length; i++) {
            symbolRanges[i] = dictionary[i].split("-");
        }
        return symbolRanges;
    }
}

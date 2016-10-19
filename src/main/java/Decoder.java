import java.io.*;
import java.util.Properties;

/**
 * @author: Brotherdos
 * @date: 17.10.2016.
 */
public class Decoder {
    private static int border;
    private static int length;

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")){
            properties.load(fis);
            border = Integer.parseInt(properties.getProperty("border"));
            length = Integer.parseInt(properties.getProperty("length"));
            FileWorker.write("decoded.txt", decode(FileWorker.read("encoded.txt")));
        } catch (IOException e) {
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

        while (length > 0) {
            for (String[] symbolRange : symbolRanges) {
                if ((Double.parseDouble(symbolRange[1]) < code) && (code < Double.parseDouble(symbolRange[2]))) {
                    text.append(symbolRange[0]);
                    System.out.println(length + ": " + symbolRange[0] + ": " + code);
                    double low = Double.parseDouble(symbolRange[1]);
                    double high = Double.parseDouble(symbolRange[2]);
                    code = border * (code - low) / (high - low);
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

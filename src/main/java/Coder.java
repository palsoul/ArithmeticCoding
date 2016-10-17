import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Brotherdos
 * @date: 12.10.2016.
 */
public class Coder {
    public static void main(String[] args) {
        String text;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            text = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            text = "";
        }
        FileWorker.write("encoded.txt", String.valueOf(encode(text)));
    }

    private static Double encode(String text) {
        Map<Character, Double> symbolWeights = buildSymbolWeights(text);
        String[][] symbolRanges = buildSymbolRanges(symbolWeights);
        saveDictionary(symbolRanges);
        double lowOld = 0, highOld = 0;
        for (int i = 0; i < symbolRanges.length; i++) {
            if (text.charAt(0) == symbolRanges[i][0].charAt(0)) {
                lowOld = Double.parseDouble(symbolRanges[i][1]);
                highOld = Double.parseDouble(symbolRanges[i][2]);
                break;
            }
        }
        for (int i = 1; i < text.length(); i++) {
            double low = 0, high = 0;
            for (int j = 0; j < symbolRanges.length; j++) {
                if (text.charAt(i) == symbolRanges[j][0].charAt(0)) {
                    low = Double.parseDouble(symbolRanges[j][1]);
                    high = Double.parseDouble(symbolRanges[j][2]);
                    break;
                }
            }
            lowOld = lowOld + (highOld - lowOld) * low;
            highOld = lowOld + ((highOld - lowOld)) * high;
            System.out.println(lowOld + " " + highOld);
        }
        return lowOld;
    }

    private static Map<Character, Double> buildSymbolWeights(String text) {
        Map<Character, Integer> symbolFrequencies = buildSymbolFrequencies(text);
        Map<Character, Double> symbolWeights = new HashMap<>();
        for (Map.Entry<Character, Integer> frequency : symbolFrequencies.entrySet()) {
            Double weight = (double) frequency.getValue() / text.length();
            symbolWeights.put(frequency.getKey(), weight);
        }
        return symbolWeights;
    }

    private static Map<Character, Integer> buildSymbolFrequencies(String text) {
        Map<Character, Integer> symbolFrequencies = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            int count = 0;
            for (Map.Entry<Character, Integer> symbolFrequency : symbolFrequencies.entrySet()) {
                if (text.charAt(i) == symbolFrequency.getKey()) {
                    symbolFrequency.setValue(symbolFrequency.getValue() + 1);
                    break;
                }
                count++;
            }
            if (symbolFrequencies.size() == count) {
                symbolFrequencies.put(text.charAt(i), 1);
            }
        }
        return symbolFrequencies;
    }

    private static String[][] buildSymbolRanges(Map<Character, Double> symbolWeights) {
        String[][] symbolRanges = new String[symbolWeights.size()][3];
        int i = 0;
        double low = 0;
        while (symbolWeights.size() > 0) {
            char maxKey = ' ';
            double maxValue = 0;
            for (Map.Entry<Character, Double> weight : symbolWeights.entrySet()) {
                maxKey = weight.getKey();
                maxValue = weight.getValue();
                break;
            }
            for (Map.Entry<Character, Double> weight : symbolWeights.entrySet()) {
                if (weight.getValue() > maxValue) {
                    maxValue = weight.getValue();
                    maxKey = weight.getKey();
                }
            }
            symbolWeights.remove(maxKey);
            symbolRanges[i][0] = String.valueOf(maxKey);
            symbolRanges[i][1] = String.valueOf(low);
            symbolRanges[i][2] = String.valueOf(low + maxValue);

            low += maxValue;
            i++;
        }
        return symbolRanges;
    }

    private static void saveDictionary(String[][] symbolRanges) {
        StringBuilder dictionary = new StringBuilder();
        for (int i = 0; i < symbolRanges.length; i++) {
            dictionary.append(symbolRanges[i][0]).append("-").append(symbolRanges[i][1]).append("-").append(symbolRanges[i][2]).append("\n");
        }
        FileWorker.write("dictionary.txt", dictionary.toString().substring(0, dictionary.length() - 1));
    }
}
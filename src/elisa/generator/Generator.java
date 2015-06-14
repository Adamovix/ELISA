package elisa.generator;

import java.util.*;

/**
 * @author Adam Goscicki
 */
public class Generator extends Observable {
    private double lastProbability;
    private String lastUnknown;
    private Random random = new Random(System.nanoTime());

    public String generate(String starting, Analyzer analyzer) {
        lastProbability = 1;
        String text = "";

        String last = starting;
        int counter = 1;
        while (last != null && !last.matches(".*[!.?]$") && counter < 16) {
            last = generateSuffix(last, analyzer);
            if (last != null) {
                text += last + " ";
                counter++;
            }
        }

        setChanged();
        notifyObservers();
        return text;
    }

    public double getLastProbability() {
        return lastProbability;
    }

    public String getLastUnknown() {
        return lastUnknown;
    }

    private String generateSuffix(String prefix, Analyzer analyzer) {
        Map<String, Map<String, Integer>> map = analyzer.getGramsMap();

        List<Map.Entry<String, Integer>> entries;
        if (map.containsKey(prefix)) {
            entries = new ArrayList<>(map.get(prefix).entrySet());
        } else {
            lastUnknown = prefix;
            entries = new ArrayList<>(map.get(findSimilar(prefix, analyzer)).entrySet());
        }

        Map.Entry<String, Integer> entry = entries.get(random.nextInt(entries.size()));
        lastProbability *= 1;
        lastProbability /= entries.size();
        return entry.getKey();
    }

    private static String findSimilar(String prefix, Analyzer analyzer) {
        Map<String, Map<String, Integer>> map = analyzer.getGramsMap();

        String similarKey = null;
        double factor = Integer.MAX_VALUE;
        for (String key : map.keySet()) {
            double i = getSimilarityFactor(key, prefix);
            if (i < factor) {
                similarKey = key;
                factor = i;
            }
        }
        return similarKey;
    }

    private static double getSimilarityFactor(String a, String b) {
        int f = 0;
        char[] aArray = a.toLowerCase().toCharArray();
        char[] bArray = b.toLowerCase().toCharArray();
        int minLength = Math.min(a.length(), b.length());

        for (int i = 0; i < minLength; i++) {
            int diff = aArray[i] - bArray[i];
            f += diff * diff;
        }
        f += Math.max(aArray.length, b.length()) - minLength;
        return f + f * Math.cos(Math.random() * Math.PI);
    }
}

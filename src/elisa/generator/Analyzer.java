package elisa.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Adam Goscicki
 */
public class Analyzer extends Observable {
    private Map<String, Map<String, Integer>> gramsMap = new HashMap<>();
    private Map<String, Integer> wordsMap = new HashMap<>();

    public void parse(File file) throws FileNotFoundException {
        if (file == null) {
            throw new FileNotFoundException();
        }

        Scanner scanner = new Scanner(file);

        String last = scanner.next();
        addWord(last);

        String current;
        while (scanner.hasNext()) {
            current = scanner.next();

            addWord(current);
            addNGram(last, current);

            last = current;
        }
        scanner.close();
        notifyObservers();
    }

    private void addWord(String word) {
        int v = wordsMap.containsKey(word) ? wordsMap.get(word) + 1 : 1;
        wordsMap.put(word, v);
        setChanged();
    }

    private void addNGram(String prefix, String suffix) {
        if (gramsMap.containsKey(prefix)) {
            Map<String, Integer> suffixes = gramsMap.get(prefix);
            int v;
            if (suffixes.containsKey(suffix)) {
                v = suffixes.get(suffix) + 1;
            } else { // unique
                v = 1;
            }
            suffixes.put(suffix, v);
        } else {
            Map<String, Integer> suffixes = new HashMap<>();
            suffixes.put(suffix, 1);
            gramsMap.put(prefix, suffixes);
        }
        setChanged();
    }

    public Map.Entry<String, Integer> getWord(String word) {
        if (!wordsMap.containsKey(word)) {
            throw new IllegalArgumentException("word not found");
        }
        return new AbstractMap.SimpleImmutableEntry<>(word, wordsMap.get(word));
    }

    protected Map<String, Map<String, Integer>> getGramsMap() {
        return gramsMap;
    }

    public int getUniqueWordCount() {
        return wordsMap.size();
    }

    public boolean isEmpty() {
        return gramsMap.isEmpty();
    }
}

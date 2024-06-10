package edu.njit.cs114;

import java.io.IOException;
import java.util.*;

public class AnagramFinderListImpl extends AbstractAnagramFinder {

    private List<WordChArrPair> wordChArrPairList = new ArrayList<>();

    private class WordChArrPair implements Comparable<WordChArrPair> {
        public final String word;
        public final char [] charArr;

        public WordChArrPair(String word) {
            this.word = word;
            charArr = word.toCharArray();
            Arrays.sort(charArr);
        }

        public boolean isAnagram(WordChArrPair wordArrPair) {
            // Compare charArr already sorted using Arrays.equals() method
            return Arrays.equals(this.charArr, wordArrPair.charArr);
        }

        @Override
        public int compareTo(WordChArrPair wordArrPair) {
            return this.word.compareTo(wordArrPair.word);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof WordChArrPair)) {
                return false;
            }
            WordChArrPair other = (WordChArrPair) obj;
            return this.word.equals(other.word);
        }
    }

    @Override
    public void clear() {
        wordChArrPairList.clear();
    }

    @Override
    public void addWord(String word) {
        WordChArrPair newPair = new WordChArrPair(word);
        if (!wordChArrPairList.contains(newPair)) {
            wordChArrPairList.add(newPair);
        }
    }

    @Override
    public List<List<String>> getMostAnagrams() {
        List<WordChArrPair> wordArrPairList = new ArrayList<>(wordChArrPairList);
        Collections.sort(wordArrPairList);
        ArrayList<List<String>> mostAnagramsList = new ArrayList<>();

        int maxAnagramSize = 0;

        while (!wordArrPairList.isEmpty()) {
            List<String> anagramList = new ArrayList<>();
            Iterator<WordChArrPair> iter = wordArrPairList.iterator();
            WordChArrPair word = wordArrPairList.get(0); // Get the next word pair

            // Iterate through the remaining list to find anagrams of the current word pair
            while (iter.hasNext()) {
                WordChArrPair pair = iter.next();
                if (word.isAnagram(pair)) {
                    anagramList.add(pair.word);
                    iter.remove();
                }
            }

            // Update the maximum anagram size and adjust the mostAnagramsList
            if (anagramList.size() > maxAnagramSize) {
                maxAnagramSize = anagramList.size();
                mostAnagramsList.clear();
                mostAnagramsList.add(anagramList);
            } else if (anagramList.size() == maxAnagramSize) {
                mostAnagramsList.add(anagramList);
            }
        }

        return mostAnagramsList;
    }

    public static void main(String[] args) {
        AnagramFinderListImpl finder = new AnagramFinderListImpl();
        finder.clear();
        long startTime = System.nanoTime();
        int nWords = 0;
        try {
            nWords = finder.processDictionary("words.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<List<String>> anagramGroups = finder.getMostAnagrams();
        long estimatedTime = System.nanoTime() - startTime;
        double seconds = ((double) estimatedTime / 1000000000);
        System.out.println("Number of words : " + nWords);
        System.out.println("Number of groups of words with maximum anagrams : "
                + anagramGroups.size());
        if (!anagramGroups.isEmpty()) {
            System.out.println("Length of list of max anagrams : " + anagramGroups.get(0).size());
            for (List<String> anagramGroup : anagramGroups) {
                System.out.println("Anagram Group : " + anagramGroup);
            }
        }
        System.out.println("Time (seconds): " + seconds);
    }
}

package com.wordle.Wordle_App.Logic;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WordleHints {

    // Get list of 5 letter words
    private static final List<String> WORD_LIST = getWords();

    // Get map of letters and associated frequency
    private static final Map<String, Integer> LETTER_FREQUENCIES = getLetterFrequency();

    private static final String DICTIONARY_1 = "/wordle-allowed-guesses.txt";
    private static final String DICTIONARY_2 = "/wordle-answers-alphabetical.txt";

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public static final List<String> smoothInput(Map<String, String> guessResultMap) {
        // Get score of each word. This map will get smaller and smaller as words are eliminated.
        // TODO to be improved: first implementation is to simply add up raw frequencies
        Map<String, Integer> WORD_SCORES = getWordScores(LETTER_FREQUENCIES);
        List<String> blackTiles = new ArrayList<>();
        Map<Integer, List<String>> yellowTiles = new HashMap<>();
        Map<Integer, String> greenTiles = new HashMap<>();
        for (String key : guessResultMap.keySet()) {
            for (int i = 0; i < 5; i++) {
                String letter = String.valueOf(key.charAt(i));
                String color = String.valueOf(guessResultMap.get(key).charAt(i));
                if (color.equals("B")) {
                    blackTiles.add(letter);
                }
                if (color.equals("Y")) {
                    // check if there is already a list of yellow tiles at this index
                    if (yellowTiles.get(i) != null) {
                        List<String> currentYellows = yellowTiles.get(i);
                        currentYellows.add(letter);
                        yellowTiles.put(i, currentYellows);
                    } else {
                        List<String> newYellow = new ArrayList<>();
                        newYellow.add(letter);
                        yellowTiles.put(i, newYellow);
                    }
                }
                if (color.equals("G")) {
                    greenTiles.put(i, letter);
                }
            }
        }
        return getNextGuesses(WORD_SCORES, blackTiles, yellowTiles, greenTiles);
    }

    private static List<String> getWords() {
        List<String> wordList = new ArrayList<>();
        InputStream stream1 = WordleHints.class.getClassLoader().getResourceAsStream(DICTIONARY_1);
        String list1 = new BufferedReader(
                new InputStreamReader(stream1, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        List<String> dic1 = Arrays.asList(list1.split("\n"));

        InputStream stream2 = WordleHints.class.getClassLoader().getResourceAsStream(DICTIONARY_2);
        String list2 = new BufferedReader(
                new InputStreamReader(stream2, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        List<String> dic2 = Arrays.asList(list2.split("\n"));
        wordList.addAll(dic1);
        wordList.addAll(dic2);
        return wordList;
    }

    private static Map<String, Integer> getLetterFrequency() {
        Map<String, Integer> freqMap = new HashMap<>();
        for (String letter : ALPHABET.split("")) {
            Integer count = 0;
            for (String word : WORD_LIST) {
                for (String character : word.split("")) {
                    if (character.equals(letter)) {
                        count++;
                    }
                }
            }
            freqMap.put(letter, count);
        }
        return freqMap;
    }

    private static Map<String, Integer> getWordScores(Map<String, Integer> letterFreqs) {
        Map<String, Integer> wordScores = new HashMap<>();
        for (String word : WORD_LIST) {
            Integer score = 0;
            for (String letter : word.split("")) {
                Integer letterScore = letterFreqs.get(letter);
                score += letterScore;
            }
            wordScores.put(word, score);
        }
        return wordScores;
    }

    private static List<String> getBestWords(Map<String, Integer> WORD_SCORES) {
        Integer maxScore = getMaxScore(WORD_SCORES);
        List<String> bestWords = new ArrayList<>();
        for (String word : WORD_SCORES.keySet()) {
            if (WORD_SCORES.get(word) == maxScore) {
                bestWords.add(word);
            }
        }
        return bestWords;
    }

    private static Integer getMaxScore(Map<String, Integer> wordScoresMap) {
        Integer maxScore = 0;
        for (String word : wordScoresMap.keySet()) {
            Integer score = wordScoresMap.get(word);
            // filter out any guess with double letters
            List<String> letters = Arrays.asList(word.split(""));
            Set<String> wordAsSet = new HashSet<>(letters);
            if (score > maxScore && wordAsSet.size() == 5) {
                maxScore = score;
            }
        }
        // if nothing was found for no double letters, then we must allow them
        if (maxScore == 0) {
            for (String word : wordScoresMap.keySet()) {
                Integer score = wordScoresMap.get(word);
                if (score > maxScore) {
                    maxScore = score;
                }
            }
        }
        return maxScore;
    }

    static final List<String> getNextGuesses(Map<String, Integer> WORD_SCORES,
                                             List<String> blackLetters,
                                             Map<Integer, List<String>> yellowLetterToPositionMap,
                                             Map<Integer, String> greenLetterToPositionMap) {
        // for every letter in blackList, remove any word containing this letter from WORD_LIST
        List<String> wordsToRemove = new ArrayList<>();
        for (String letter : blackLetters) {
            for (String word : WORD_SCORES.keySet()) {
                if (word.contains(letter)) {
                    wordsToRemove.add(word);
                }
            }
        }
        // for every letter in yellowList, remove any word that contains this letter at the given position
        for (Integer index : yellowLetterToPositionMap.keySet()) {
            for (String word : WORD_SCORES.keySet()) {
                List<String> wordAsList = Arrays.asList(word.split(""));
                List<String> badLettersAtIndex = yellowLetterToPositionMap.get(index);
                for (String letter : badLettersAtIndex) {
                    // remove words that contain the yellow letter at the given index
                    if (wordAsList.get(index).equals(letter)) {
                        wordsToRemove.add(word);
                    }
                    // remove words that do not contain the yellow letter at all
                    if (!word.contains(letter)) {
                        wordsToRemove.add(word);
                    }
                }

            }
        }
        // for every letter in greenList, remove any word that does NOT contain this letter at the given position
        for (Integer index : greenLetterToPositionMap.keySet()) {
            for (String word : WORD_SCORES.keySet()) {
                List<String> wordAsList = Arrays.asList(word.split(""));
                if (!wordAsList.get(index).equals(greenLetterToPositionMap.get(index))) {
                    wordsToRemove.add(word);
                }
            }
        }
        for (String word : wordsToRemove) {
            WORD_SCORES.remove(word);
        }
        return getBestWords(WORD_SCORES);
    }
}
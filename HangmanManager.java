package Hangman;

/**
 * HangmanManager
 * This is built for managing the evil hangman game.
 *
 * @author Gaeun Kim
 * @version 16 March 2024
 * CS122 10AM
 */

import java.util.*;
public class HangmanManager {

    private int guessLeft;
    private Set<Character> guessedSet;
    private Set<String> wordSet;//only included with given length words


    /**
     * Constructor
     *
     * Initialize the set of words to contain only those from the dictionary
     * that match the given target word length.
     * Throw an IllegalArgumentException if the target word length is less than 1
     * or if the maximum number of wrong guesses is less than 0.
     *
     * @param Dictionary list of dictionary words
     * @param length     desired word length
     * @param max        max number of guess
     **/


    //Collection interface includes List, Queue, Set
    HangmanManager(Collection<String> Dictionary, int length, int max) {
        if (length < 1 || max < 0) throw new IllegalArgumentException();

        guessLeft = max;
        wordSet = new TreeSet<>();
        guessedSet = new TreeSet<>();

        //grab the word with given length in Dictionary, add in wordSet
        for (String dictionWord : Dictionary) {
            if (dictionWord.length() == length) wordSet.add(dictionWord);
        }
    }

    /**
     * words()
     *
     * @return the set of given length of words
     */

    public Set<String> words() {
        return wordSet;
    }

    /**
     * guessesLeft()
     *
     * @return the number of left chance of guessing
     */
    public int guessesLeft() {
        return guessLeft;
    }

    /**
     * guesses()
     *
     * @return the set of character has been guessed
     */
    public Set<Character> guesses() {
        return guessedSet;
    }

    /**
     * generatePattern()
     *
     * generate pattern of the words with guessed character
     *
     * @param word the word in the wordSet
     * @return the pattern with spacing
     */
    private String generatePattern(String word) {
        String pattern = "";
        for (int i=0; i < word.length(); i++) {
            //instead of using char parameter, using guessedSet, checked if it's match
            if (guessedSet.contains(word.charAt(i)))
                pattern += word.substring(i, i + 1);
            else
                pattern += "-";
        }

        //add space
        String newPattern = "";
        for (int i=0; i<pattern.length(); i++){
            newPattern += pattern.charAt(i) + " ";
        }
        return newPattern;
    }


    /**
     * pattern()
     *
     * throw IllegalArgumentException if wordSet is empty
     *
     * @return the pattern of the word with guessed character
     */
    public String pattern(){
        if(wordSet.isEmpty()) throw new IllegalArgumentException();
        return generatePattern(wordSet.iterator().next());
    }




    /**
     * record
     *
     * It should return the number of occurrences of the guessed letter in the new pattern.
     * It should appropriately update the number of guesses left.
     *
     * throw an IllegalStateException
     *  if the number of guesses left is not at least 1 OR if the list of words is empty.
     *  if the list of words is nonempty AND the character being guessed was guessed previously.
     *
     * @param guess user input of letter
     * @return the number of occurrences for the character in the new updated pattern
     */
    public int record(char guess) {
        int correctGuess = 0;

        //check if the number of guessLeft < 1 or wordSet is empty
        if (wordSet.isEmpty() || guessLeft < 1) throw new IllegalArgumentException();
        //check if param guess has been guessed
        else if (!wordSet.isEmpty() && guessedSet.contains(guess)) throw new IllegalArgumentException();
        guessedSet.add(guess); //add the guessed character in the set (both wrong and right)

        //create map to get the largest set of the mapValue (which is map Set)
        Map<String, Set<String>> map = new TreeMap<>();
        largestWordSet(map); //updated the largest set with the guessed pattern

        //check the guess if it's right or wrong

        /* (This is what I understand)
        Occurrences = the number of times for the right guess for every updated pattern.
                    = the number of correct Guess

        when we run the program, the largest set will be changed for every guess,
        which means, the current pattern will be changed depends on the size of set as well.

        Hence, we need to figure out if the user guess is right or wrong for every changed pattern.
        (we need pattern (in largest set), guess)

         */

        //grab the current pattern
        String pattern = this.pattern();

        for (int i=0; i<pattern.length(); i++){
            if (pattern.charAt(i) == guess) correctGuess++;
        }

        //if you guessed wrong, deduct by 1
        if (correctGuess == 0) guessLeft--;

        return correctGuess;
    }

    /**
     * largestWordSet()
     *
     * By creating the map, pick the largest size of set, and updated for wordSet
     * If the size is same, pick the one that occurs earlier in the Map.
     *
     * @param map this map is to organize the words with same pattern
     */
    private void largestWordSet (Map<String, Set<String>> map){
        //TODO: create map
        //grab a word in the wordSet
        for (String mapWord: wordSet){
            //checking if the map contains the pattern
            String pattern = generatePattern(mapWord);
            if (map.containsKey(pattern)){
                map.get(pattern).add(mapWord);
            } else {
                map.put(pattern, new HashSet<>());
                map.get(pattern).add(mapWord);
            }
        }

        //TODO: get the largest set -> wordSet updated
        int maxSize = 0; //In map, Set<String> size
        String mapKey = "";
        for (String pattern: map.keySet()){
            maxSize = Math.max(map.get(pattern).size(), maxSize);

            //grab the map key String
            if (map.get(pattern).size() == maxSize){
                mapKey = pattern;
                //grab the first key when there are same size of sets
                break;
            }
        }

        //change wordSet to the largest size of Set in the map Set
        wordSet = map.get(mapKey);
    }


}

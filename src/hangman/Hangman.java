
package hangman;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Program Description: A hangman game that uses random numbers to choose a word
 * from the dictionary.txt file
 *
 * @author David Heywood
 * Due Date: March 3, 2014
 *
 */
public class Hangman {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    
    /* Determines whether the player wants to play another game. If the answer
     * is yes then jump to the playGame method
    */
    public static void main(String[] args) throws FileNotFoundException {
        char answer;
        playGame();
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Do you want to play again (Y/N) ");
        answer = keyboard.next().charAt(0); //read a single char
        while (answer == 'Y') {
            playGame();
            System.out.print("Do you want to play again (Y/N) ");
            answer = keyboard.next().charAt(0); //read a single char
        }
    }

    public static void playGame() throws FileNotFoundException {
        String[] dictionary = new String[127142];
        char[] guessList = new char[50];
        String randomWord;
        boolean correctGuess, duplicate;
        boolean game = true;
        char guess;
        int numWrongGuesses = 0;
        int currentGuess = 0;
        readFile(dictionary);
        randomWord = getRandomWord(dictionary);
        StringBuilder hiddenWord = hideRandomWord(randomWord);
        
        // While loop checks game condition to see if game should continue        
        while (game == true) {
            guess = letterGuess(hiddenWord);
            duplicate = dupGuess(guessList, guess);
            correctGuess = verifyLetterGuess(guess, randomWord, hiddenWord);
            // This if places current guess into the guessList array
            if (duplicate == false) {
                guessList[currentGuess] = guess;
                currentGuess = currentGuess + 1;
                // This if places correct guess in the hiddenWord and checks if
                // you have won
                if (correctGuess == true) {
                    hiddenWord = replaceLetter(hiddenWord, randomWord, guess);                    
                    game = checkWon(hiddenWord, randomWord, numWrongGuesses);
                    // This else tracks wrong guesses and checks if you lost
                } else {
                    numWrongGuesses = wrongGuess(correctGuess, numWrongGuesses);
                    game = checkLost(numWrongGuesses, randomWord);
                }
            } else {
                System.out.println(guess + " has already been guessed");
            }
        }
    }

    // Reads in the dictionary.txt file to be used to pick words for the game
    public static void readFile(String[] m) throws FileNotFoundException {
        Scanner in = null;
        boolean everythingOK = true;
        try {
            in = new Scanner(new FileReader("dictionary.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
            everythingOK = false;
        }
        if (everythingOK) {
            for (int r = 0; r < m.length; r++) {
                m[r] = in.next();
            }
        } else {
            System.out.println("bye");
        }
    }

    // Picks a random word from the list of words to be used in the current game
    public static String getRandomWord(String[] list) {
        String randomWord;
        randomWord = list[(int) (Math.random() * list.length)];
        System.out.println(randomWord);
        return randomWord;
    }

    // Appends the random word with astericks to hide the word
    public static StringBuilder hideRandomWord(String randomWord) {
        StringBuilder hiddenWord = new StringBuilder(randomWord.length() + 1);
        for (int i = 0; i < randomWord.length(); i++) {
            hiddenWord.append('*');
        }
        return hiddenWord;
    }

    // Asks the player to guess a letter that my be in the hidden word
    public static char letterGuess(StringBuilder hiddenWord) {
        char guess;
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter a letter in the word " + hiddenWord + " > ");
        guess = keyboard.next().charAt(0); //read a single char
        return guess;
    }

    // Checks to see if the letter has already been guessed
    public static boolean dupGuess(char[] guessList, char guess) {
        boolean duplicate = false;
        for (int i = 0; i < guessList.length; i++) {
            if (guess == guessList[i]) {
                duplicate = true;
            }
        }
        return duplicate;
    }

    // Verifies that the letter guessed is in the random word
    public static boolean verifyLetterGuess(char guess, String randomWord,
            StringBuilder hiddenWord) {
        boolean letterGuess = false;
        for (int i = 0; i < randomWord.length(); i++) {
            if (randomWord.charAt(i) == guess) {
                letterGuess = true;
            }
        }
        return letterGuess;
    }

    // Increases the numWrongGuess count by one if the current guess is wrong 
    public static int wrongGuess(boolean letterGuess, int numWrongGuesses) {
        if (letterGuess == false) {
            numWrongGuesses++;
        }
        return numWrongGuesses;
    }

    // Replaces the asterics in the hiddenWord with the guessed letter if the 
    // guess is correct
    public static StringBuilder replaceLetter(StringBuilder hiddenWord,
            String randomWord, char guess) {
        for (int i = 0; i < randomWord.length(); i++) {
            if (randomWord.charAt(i) == guess) {
                hiddenWord.setCharAt(i, guess);
            }
        }
        return hiddenWord;
    }

    // Checks to see if you have won the game
    public static boolean checkWon(StringBuilder hiddenWord, String randomWord,
            int numWrongGuesses) {
        boolean game = false;
        for (int i = 0; i < hiddenWord.length(); i++) {
            if (hiddenWord.charAt(i) == '*') {
                game = true;
            }
        }
        if (game == false) {
            System.out.println("YOU WON!!");
            System.out.println("The word is " + randomWord);
            System.out.println("You missed " + numWrongGuesses + " times");
        }
        return game;
    }

    // Checks to see if you have lost the game
    public static boolean checkLost(int numWrongGuesses, String randomWord) {
        boolean game = true;
        if (numWrongGuesses == 7) {
            game = false;
            System.out.println("YOU LOST!!");
            System.out.println("The word is " + randomWord);
        }
        return game;
    }
}

package Management;

import API.DictionaryAPI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DictionaryGame {
    // Word need guessing
    private String wordToGuess;

    // Guessing times
    private int chances;
    private final int maxChances;

    // Input from keyboard
    private Scanner input;

    // Words from file Game.txt
    private List<String> words;

    // Random word from List above
    private Random random;

    // Constructor
    public DictionaryGame(String word) {
        this.maxChances = 10;
        this.input = new Scanner(System.in);
        this.words = new ArrayList<>();
        this.random = new Random();
        this.chances = 0;
        loadWords();
    }

    public void loadWords() {
        try {
            // Read from file Game.txt
            BufferedReader reader = new BufferedReader(new FileReader("Game.txt"));
            String line;
            while ((line = reader.readLine()) != null) {

                // Add words to word list, trim() method to ensure there is no blank space
                words.add(line.trim());
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void play() {
        // Play-again checking flag
        boolean playAgain = true;

        // Start playing
        while (playAgain) {

            // Generate randomly word index
            int index = random.nextInt(words.size());

            // Get word at word index found, then convert to UPPERCASE
            wordToGuess = words.get(index).toUpperCase();

            // HashSet to store characters previously guessed
            HashSet<String> prevGuesses = new HashSet<>();


            int length = wordToGuess.length();

            // Convert word to array
            char[] wordToGuessChars = wordToGuess.toCharArray();

            // Construct array censor with full of "_" having length of worth
            char[] censor = new char[length];
            Arrays.fill(censor, '_');


            while (!String.valueOf(censor).equals(wordToGuess) && chances < maxChances) {

                // Check if the guessed character is true or not
                boolean correct = false;

                // Check if the characters have been guessed more than one time
                boolean repeated = false;

                for (char c : censor) {
                    System.out.print(c);
                }

                System.out.println();

                System.out.print("Enter a character: ");

                // Handle input
                String currentGuess = input.next().toUpperCase().substring(0, 1);
                char currentGuessChar = currentGuess.charAt(0);

                // Check for repetition, the repeated character must be one of the characters in
                // the guessed word, or else it is considered to be incorrect
                if (prevGuesses.contains(currentGuessChar)) {
                    System.out.println("This character has been guessing. Guessed character(s): " + String.join(
                            ", ", prevGuesses));
                    repeated = true;
                }

                // NOTE: just only the characters consist of word can be added to HashSet
                for (int i = 0; i < length; i++) {
                    if (wordToGuessChars[i] == currentGuessChar) {
                        prevGuesses.add(currentGuess);
                    }
                }

                // If the character is not repeated, continue
                if (!repeated) {
                    int times = 0;
                    for (int i = 0; i < length; i++) {
                        if (wordToGuessChars[i] == currentGuessChar) {
                            censor[i] = currentGuessChar;
                            correct = true;
                            times++;
                        }
                    }

                    if (correct) {
                        System.out.println("Character " + currentGuessChar + " is available." +
                                " The word have " + times + " character(s) " + currentGuessChar);
                    } else {
                        System.out.println("The word does not contain this character!");
                        chances++;
                    }
                }
            }
            if (String.valueOf(censor).equals(wordToGuess)) {
                System.out.println("BINGO: " + wordToGuess.toUpperCase() + "\n");
                DictionaryAPI output = new DictionaryAPI();
                output.searchWord(wordToGuess);
            } else {
                System.out.println("YOU ARE HUNG! The word is " + wordToGuess + "\n");
                DictionaryAPI output = new DictionaryAPI();
                output.searchWord(wordToGuess);
            }

            // Play-again request
            System.out.print("Wanna try again? Enter Y, or else N for exit: ");
            String answer = input.next().toUpperCase();
            input.nextLine();

            if (answer.equals("N")) {
                playAgain = false;
            }
        }
    }

    public static String getRandomWordFromFile() {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Game.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\s+");
                if (parts.length > 0) {
                    words.add(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!words.isEmpty()) {
            int randomIndex = new Random().nextInt(words.size());
            return words.get(randomIndex);
        } else {
            return null;
        }
    }
}

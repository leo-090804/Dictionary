package CmdLine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameFromDictionary {
    private String secretWord;
    private int attempts;
    private int maxAttempts;
    private Set<String> pastGuesses;
    private Scanner userInput;
    private List<String> wordList;
    private Random rand;

    public GameFromDictionary(String inputWord) {
        this.maxAttempts = inputWord.length() + 10;
        this.userInput = new Scanner(System.in);
        this.wordList = new ArrayList<>();
        this.rand = new Random();
        loadWordList();
    }

    public void loadWordList() {
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("Game.txt"));
            String fileLine = fileReader.readLine();
            while (fileLine != null) {
                wordList.add(fileLine.trim());
                fileLine = fileReader.readLine();
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        boolean continueGame = true;
        while (continueGame) {
            int randomIndex = rand.nextInt(wordList.size());
            secretWord = wordList.get(randomIndex).toUpperCase();
            pastGuesses = new HashSet<>();
            attempts = 0;

            int wordLength = secretWord.length();
            char[] secretWordChars = secretWord.toCharArray();
            char[] hiddenWord = new char[wordLength];

            for (int i = 0; i < wordLength; i++) {
                hiddenWord[i] = '_';
            }

            while (!String.valueOf(hiddenWord).equals(secretWord) && attempts < maxAttempts) {
                boolean isCorrect = false;
                boolean isRepeated = false;

                for (char c : hiddenWord) {
                    System.out.print(c);
                }
                System.out.println();
                System.out.print("Nhập ký tự bạn đoán: ");
                String currentGuess = userInput.next().toUpperCase().substring(0, 1);
                char currentGuessChar = currentGuess.charAt(0);

                if (pastGuesses.contains(currentGuess)) {
                    System.out.println("Bạn đã đoán ký tự này rồi! Hãy thử lại. Các ký tự bạn đã đoán là: " + String.join(", ", pastGuesses));
                    isRepeated = true;
                }
                pastGuesses.add(currentGuess);

                if (!isRepeated) {
                    int count = 0;
                    for (int i = 0; i < wordLength; i++) {
                        if (secretWordChars[i] == currentGuessChar) {
                            hiddenWord[i] = currentGuessChar;
                            isCorrect = true;
                            count++;
                        }
                    }
                    if (isCorrect) {
                        System.out.println("Ký tự " + currentGuessChar + " có trong từ bí mật! Có " + count + " ký tự " + currentGuessChar + " trong từ. Hiển thị các ký tự đó: ");
                    } else {
                        System.out.println("Xin lỗi, ký tự này không có trong từ. Từ bí mật của bạn là:  ");
                    }
                    System.out.println();
                }
                attempts++;
            }

            if (String.valueOf(hiddenWord).equals(secretWord)) {
                System.out.println("Bạn đã đoán đúng toàn bộ từ " + secretWord.toUpperCase() + "! Bạn đã đoán được sau " + attempts + " lần!");
            } else {
                System.out.println("Bạn đã hết lượt chơi. Từ bí mật là: " + secretWord);
            }

            // Hỏi người chơi có muốn chơi lại hay không
            System.out.print("Bạn có muốn chơi lại không? Nhập Y để chơi lại, N để thoát: ");
            String userAnswer = userInput.next().toUpperCase();

            // "Tiêu thụ" Enter sau khi đọc "N"
            userInput.nextLine();

            if (userAnswer.equals("N")) {
                continueGame = false;
            }
        }
    }


    public static String getSecretWordFromFile() {
        List<String> wordListFromFile = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("Game.txt"))) {
            String fileLine;
            while ((fileLine = fileReader.readLine()) != null) {
                String[] lineParts = fileLine.split("\s+");
                if (lineParts.length > 0) {
                    wordListFromFile.add(lineParts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!wordListFromFile.isEmpty()) {
            int randomIndexFromFile = new Random().nextInt(wordListFromFile.size());
            return wordListFromFile.get(randomIndexFromFile);
        } else {
            return null;
        }
    }
}
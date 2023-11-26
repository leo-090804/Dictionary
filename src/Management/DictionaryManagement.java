package Management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DictionaryManagement {

    public void showAllWords(Dictionary dictionary) {
        System.out.println("No\t| English\t| Vietnamese");
        int no = 1;
        for (Word word : dictionary.getWords()) {
            System.out.println(no + "\t| " + word.getWord_target() + "\t| " + word.getWord_explain());
            no++;
        }
    }
//    public void insertFromCommandline(Dictionary dictionary) {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Nhập số lượng từ vựng: ");
//        int n = sc.nextInt();
//        sc.nextLine();
//        for (int i = 0; i < n; i++) {
//            System.out.println("Nhập từ tiếng Anh: ");
//            String word_target = sc.nextLine();
//            System.out.println("Nhập giải thích tiếng Việt: ");
//            String word_explain = sc.nextLine();
//            Word word = new Word(word_target, word_explain);
//            dictionary.getWords().add(word);
//        }
//        insertToFile(dictionary);
//    }
//public void insertFromFile(Dictionary dictionary) {
//    try {
//        File file = new File("dictionaries.txt");
//        if (file.exists() && file.length() > 0) {
//            Scanner sc = new Scanner(file);
//            while (sc.hasNextLine()) {
//                String line = sc.nextLine();
//                String[] parts = line.split("\t");
//                if (parts.length == 2) {
//                    String word_target = parts[0];
//                    String word_explain = parts[1];
//                    Word word = new Word(word_target, word_explain);
//                    dictionary.getWords().add(word);
//                }
//            }
//            sc.close();
//        } else {
//            System.out.println("File dictionaries.txt không tồn tại hoặc không có dữ liệu");
//        }
//    } catch (FileNotFoundException e) {
//        System.out.println("Không tìm thấy file dictionaries.txt");
//        e.printStackTrace();
//    }
//
//}

    public void insertToFile(Dictionary dictionary) {
        try {
            File file = new File("dictionaries.txt");
            FileWriter fw = new FileWriter(file, true); // Mở tệp trong chế độ nối (append mode), thêm true

            for (Word word : dictionary.getWords()) {
                String line = word.getWord_target() + " " + word.getWord_explain();
                fw.write(line + "\n");
            }

            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage() + ". Error when inserting to file dictionaries.txt");
            e.printStackTrace();
        }
    }

    public void insertFromFile(Dictionary dictionary) {
        File file = new File("dictionaries.txt");
        if (file.exists() && file.length() > 0) {
            try {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(" ");
                    if (parts.length == 2) {
                        String word_target = parts[0];
                        String word_explain = parts[1];
                        Word word = new Word(word_target, word_explain);
                        dictionary.getWords().add(word);
                    }
                }
                sc.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage() + ". File dictionaries.txt not found!");
                e.printStackTrace();
            }
        }
        showAllWords(dictionary);
    }

    public void dictionaryLookup(Dictionary dictionary) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        Collections.sort(dictionary.getWords(), Comparator.comparing(Word::getWord_target));

        while (!exit) {
            System.out.println("Enter a word (enter 'exit' for exit): ");
            String word_target = sc.nextLine();

            if (word_target.equals("exit")) {
                exit = true;
                break;
            }

            // Sử dụng binarySearch trên danh sách đã sắp xếp
            int index = Collections.binarySearch(dictionary.getWords(), new Word(word_target, ""), Comparator.comparing(Word::getWord_target));

            if (index >= 0) {
                System.out.println("Vietnamese meaning: " + dictionary.getWords().get(index).getWord_explain());
            } else {
                System.out.println("Word not found!");
            }

            // Do you want to add the word that you have just looked up to your own list word


        }
    }

    public void dictionarySearch(Dictionary dictionary) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Enter a word (enter 'exit' for exit): ");
            String wordTarget = sc.nextLine();

            if (wordTarget.equals("exit")) {
                exit = true;
                break;
            }

            // Kiểm tra xem từ cần tra cứu có rỗng không
            if (wordTarget.trim().isEmpty()) {
                System.out.println("Vui lòng nhập từ cần tra cứu.");
                continue;
            }

            // Thực hiện tìm kiếm thông thường
            boolean found = performRegularSearch(wordTarget, dictionary.getWords());

            if (!found) {
                // Nếu không tìm thấy, thực hiện tìm kiếm fuzzy
                performFuzzySearch(wordTarget, dictionary.getWords());
            }
        }
    }

    private boolean performRegularSearch(String wordTarget, List<Word> words) {
        boolean found = false;

        for (Word word : words) {
            if (word.getWord_target().startsWith(wordTarget)) {
                System.out.println("Word(s) start with '" + wordTarget + "': " + word.getWord_target());
                found = true;
                System.out.println("Vietnamese meaning: " + word.getWord_explain());
            }
        }

        if (!found) {
            System.out.println("Word not found!.");
        }

        return found;
    }

    // DOES NOT GET MUCH THESE SECTIONS BELOW
    private void performFuzzySearch(String wordTarget, List<Word> words) {
        // Sử dụng thuật toán tìm kiếm mờ để tìm các từ gần giống
        List<Word> similarWords = new ArrayList<>();

        for (Word word : words) {
            String candidate = word.getWord_target();
            double similarity = calculateSimilarity(wordTarget, candidate);

            // Tùy chọn ngưỡng tương đồng
            double threshold = 0.7;

            if (similarity < threshold) {
                similarWords.add(word);
            }
        }

        // Sắp xếp theo độ tương đồng giảm dần
        Collections.sort(similarWords, (a, b) -> Double.compare(calculateSimilarity(wordTarget, b.getWord_target()),
                calculateSimilarity(wordTarget, a.getWord_target())));

        if (!similarWords.isEmpty()) {
            System.out.println("Tìm thấy các từ gần giống:");
            for (Word similarWord : similarWords) {
                System.out.println("Từ: " + similarWord.getWord_target());
                System.out.println("Độ tương đồng: " + calculateSimilarity(wordTarget, similarWord.getWord_target()));
                System.out.println("Giải thích tiếng Việt: " + similarWord.getWord_explain());
                System.out.println();
            }
        } else {
            System.out.println("Không tìm thấy từ gần giống.");
        }
    }

    private double calculateSimilarity(String s1, String s2) {
        int distance = levenshteinDistance(s1, s2);
        return 1 - Math.pow(1 - (double) distance / Math.max(s1.length(), s2.length()), 2);
    }

    private int levenshteinDistance(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[n][m];
    }
}
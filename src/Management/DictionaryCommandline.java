package Management;

import java.util.Iterator;
import java.util.Scanner;

public class DictionaryCommandline {

    public void showAllWords(Dictionary dictionary) {
        System.out.println("No\t| English\t| Vietnamese");
        int no = 1;
        for (Word word : dictionary.getWords()) {
            System.out.println(no + "\t| " + word.getWord_target() + "\t| " + word.getWord_explain());
            no++;
        }
    }

    // Words adding method
    public void addWord(Dictionary dictionary) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Number of word you wanna add: ");
        int numberOfWordsToAdd = sc.nextInt();
        sc.nextLine();

        for (int i = 1; i <= numberOfWordsToAdd; i++) {
            System.out.println("Enter a word: ");
            String word_target = sc.nextLine();

            // Checking repetition
            boolean exist = dictionary.getWords().stream()
                    .anyMatch(word -> word.getWord_target().equals(word_target));

            if (exist) {
                System.out.println("This word has been added previously");

                // Phần này t thấy k cần lắm
                i--; // Nếu từ đã tồn tại, không tăng số từ thêm thành công
            } else {
                System.out.println("Enter Vietnamese meaning for this word: ");
                String word_explain = sc.nextLine();
                Word word = new Word(word_target, word_explain);
                dictionary.getWords().add(word);

                // Cái này cùng hơi thừa, bỏ phần thêm từ thứ i đi
                System.out.println("Đã thêm thành công. Đây là từ thêm thứ " + i);
            }
        }
        System.out.println("Đã thêm tất cả " + numberOfWordsToAdd + " từ");
    }
    // Phần addWord ở trên t thấy làm theo kiểu y/n hợp lí hơn trong nhiều trường hợp, cân nhắc

    public void editWord(Dictionary dictionary) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Enter a word (enter 'exit' for exit): ");
            String word_target = sc.nextLine();
            boolean found = false;

            if (word_target.equals("exit")) {
                exit = true;
                break;
            }
            for (Word word : dictionary.getWords()) {
                if (word.getWord_target().equals(word_target)) {

                    // Hỏi người dùng cách họ muốn sửa
                    System.out.println("Do you wanna edit word (enter 'w') or its meaning (enter " +
                            "'d'): ");
                    String choice = sc.nextLine().toLowerCase();

                    if (choice.equals("d")) {
                        System.out.println("Enter new meaning: ");
                        String word_explain = sc.nextLine();
                        word.setWord_explain(word_explain);
                        System.out.println("Edit word's meaning successfully!");
                    } else if (choice.equals("w")) {
                        System.out.println("Enter new word: ");
                        String word_explain = sc.nextLine();
                        word.setWord_target(word_explain);
                        System.out.println("Edit word successfully!");
                    } else {
                        System.out.println("Unavailable choice");
                        // Đoạn này thêm phần lựa chọn lại nữa
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Word not found!");
            }
        }
    }

    public void deleteWord(Dictionary dictionary) {
        Scanner sc = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Enter a word (enter 'exit' for exit): ");
            String word_target = sc.nextLine();
            Iterator<Word> iterator = dictionary.getWords().iterator();
            boolean found = false;

            if (word_target.equals("exit")) {
                exit = true;
                break;
            }
            while (iterator.hasNext()) {
                Word word = iterator.next();
                if (word.getWord_target().equals(word_target)) {
                    iterator.remove();
                    System.out.println("Delete word succesfully");
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("Word not found!");
            }
        }
    }
}
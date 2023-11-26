package CmdLine;
import java.util.Scanner;

public class Main {
    public static Dictionary dictionary = new Dictionary();

    public static void main(String[] args) {
        Main main = new Main(); // Tạo một đối tượng main thuộc lớp Start.Main
        DictionaryCommandline dictionaryCommandline = new DictionaryCommandline(); // Tạo đối tượng dictionaryCommandline thuộc lớp Management.DictionaryCommandline
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        Scanner sc = new Scanner(System.in); // Tạo đối tượng Scanner để nhập dữ liệu từ bàn phím
        boolean exit = false; // Tạo biến boolean để kiểm tra xem có thoát khỏi chương trình hay không
        int choice = 11; // Khởi tạo biến choice bằng 0
        while (choice != 0) { // Lặp lại cho đến khi biến choice bằng 0
            System.out.println("Welcome to My Application!");
            System.out.println("[0] Exit");
            System.out.println("[1] Add");
            System.out.println("[2] Remove");
            System.out.println("[3] Update");
            System.out.println("[4] Display");
            System.out.println("[5] Lookup");
            System.out.println("[6] Search");
            System.out.println("[7] Game");
            System.out.println("[8] Import from file");
            System.out.println("[9] Export to file");
            System.out.println("Your action: ");

            choice = sc.nextInt(); // Nhập một số từ bàn phím để lựa chọn chức năng
            sc.nextLine(); // Xóa bộ nhớ đệm
            switch (choice) { // Sử dụng switch-case để xử lý các trường hợp khác nhau
//                case 1:
//                    dictionaryCommandline.dictionaryBasic(); // Gọi hàm dictionaryBasic() của đối tượng dictionaryCommandline với tham số là biến dictionary của đối tượng main
//                    break;
                case 0:
                    exit = true; // Đặt biến exit bằng true để thoát khỏi vòng lặp
                    System.out.println("See you again!!!");
                    break;
                case 1:
                    dictionaryCommandline.addWord(dictionary); // Gọi hàm addWord() của đối tượng dictionaryCommandline với tham số là biến dictionary của đối tượng main
                    break;
                case 2:
                    dictionaryCommandline.deleteWord(dictionary); // Gọi hàm deleteWord() của đối tượng dictionaryCommandline với tham số là biến dictionary của đối tượng main
                    break;
                case 3:
                    dictionaryCommandline.editWord(dictionary); // Gọi hàm editWord() của đối tượng dictionaryCommandline với tham số là biến dictionary của đối tượng main
                    break;
                case 4:
                    dictionaryCommandline.showAllWords(dictionary);
                    break;
                case 5:
                    dictionaryManagement.dictionaryLookup(dictionary);
                    break;
                case 6:
                    dictionaryManagement.dictionarySearch(dictionary);
                    break;
                case 7:
                    while (choice != 2) {
                        System.out.println("Chọn chế độ chơi:");
                        System.out.println("1. Chơi với từ ngẫu nhiên từ file");
                        System.out.println("2. Thoát");
                        System.out.print("Nhập lựa chọn của bạn: ");
                        choice = sc.nextInt();
                        sc.nextLine(); // Đọc dòng thừa sau khi nhập số
                        switch (choice) {

                            case 1:
                                // Lấy ngẫu nhiên một từ từ file
                                String randomWord = GameFromDictionary.getSecretWordFromFile();
                                if (randomWord != null) {
                                    GameFromDictionary game2 = new GameFromDictionary(randomWord);
                                    game2.startGame();
                                } else {
                                    System.out.println("Không thể lấy từ từ file dictionaries.txt");
                                }
                                break;
                            case 2:
                                System.out.println("Cảm ơn bạn đã chơi!");
                                break;

                            default:
                                System.out.println("Lựa chọn không hợp lệ!");
                        }
                    }

                    break;
                case 8:
                    dictionaryManagement.insertFromFile(dictionary);
                    break;
                case 9:
                    dictionaryManagement.insertToFile(dictionary);
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ"); // Nếu số nhập vào không thuộc các trường hợp trên, thông báo cho người dùng biết
                    break;

            }
        }
        sc.close(); // Đóng Scanner sc
    }
}
package CmdLine;

public class Word {
    // Khai báo thuộc tính word_target (từ mới), word_explain (giải nghĩa)
    private String word_target;
    private String word_explain;

    // Khởi tạo mặc định
    public Word() {
        word_target = "";
        word_explain = "";
    }

    // Khởi tạo có tham số
    public Word(String word_target, String word_explain) {
        this.word_target = word_target;
        this.word_explain = word_explain;
    }

    // Phương thức getter và setter cho các thuộc tính
    public String getWord_target() {
        return word_target;
    }

    public void setWord_target(String word_target) {
        this.word_target = word_target;
    }

    public String getWord_explain() {
        return word_explain;
    }

    public void setWord_explain(String word_explain) {
        this.word_explain = word_explain;
    }
}

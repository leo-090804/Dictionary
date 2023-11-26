package DictionaryMethod;

public interface DictionaryConnection {
    public abstract String connectionType();

    public abstract String searchWord(String word);
}

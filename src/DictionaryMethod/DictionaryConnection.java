package DictionaryMethod;

import java.sql.Connection;

public interface DictionaryConnection {
    public abstract String connectionType();
    public abstract String searchWord(String word);
}

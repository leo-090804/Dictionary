package DictionaryMethod;

import java.sql.*;
import java.util.*;

public class Offline implements DictionaryConnection {
    @Override
    public String connectionType() {
        return "Offline";
    }

    public Connection connection() {
        String dbPath = "./Dictionary.db";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    private static String thisWord;
    private static String thisDefinition;

    @Override
    public String searchWord(String word) {
        thisWord = word;
        StringBuilder output = new StringBuilder(word).append("\n");
        String searchQuery = "SELECT definition, wordtype FROM entries WHERE word = ?";
        String phoneticsQuery = "SELECT DISTINCT pronunciation FROM entries WHERE word = ?";

        Map<String, String> definitions = new HashMap<>();
        String phonetic;

        try (Connection newConnection = this.connection(); PreparedStatement query = newConnection.prepareStatement(phoneticsQuery)) {
            query.setString(1, word);
            ResultSet outputString = query.executeQuery();
            phonetic = outputString.getString("pronunciation");
            output.append(phonetic).append("\n");

            try (PreparedStatement subquery = newConnection.prepareStatement(searchQuery)) {
                subquery.setString(1, word);

                try (ResultSet outputMap = subquery.executeQuery()) {
                    while (outputMap.next()) {
                        String wordtype = outputMap.getString("wordtype");
                        String definition = outputMap.getString("definition");
                        definitions.put(definition, wordtype);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        if (definitions.isEmpty()) {
            throw new NullPointerException("Word not available");
        }

        for (Map.Entry<String, String> entry : definitions.entrySet()) {
            String def = entry.getKey().replaceAll("\\n\\s+", " ").replaceAll("--", "");
            String wordtype = entry.getValue();
            output.append("(" + wordtype + ")" + "\n").append(def + "\n");
        }

        thisDefinition = output.toString();
        return output.toString();
    }

    public List<String> searchListWordDef(String word) {
        String sql = "SELECT definition FROM entries WHERE word = ?";

        List<String> definitions = new ArrayList<>();

        try (Connection conn = this.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, word);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String definition = rs.getString("definition");
                    definitions.add(definition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        if (definitions.size() == 0) {
            throw new NullPointerException("Word not available");
        }
        return definitions;
    }

    public List<String> exactSearch(String searchWord, Set<String> allWordsOnlineSet) {
        List<String> searchResults = new ArrayList<>();

        if (allWordsOnlineSet.contains(searchWord)) {
            // Use the offline search function
            try {
                List<String> offlineResults = searchListWordDef(searchWord);
                for (String definition : offlineResults) {
                    // Customize the format based on your needs
                    searchResults.add(definition);
                }
            } catch (NullPointerException e) {
                // Handle the case where the word is not available offline
                System.out.println("Word not available offline. Searching online...");
            }

        }
        return searchResults;
    }

    public void insertWord(String newWord, String newWordType, String newMeaing) {
        String insertQuery = "INSERT INTO addedword (word, wordtype, meaning) VALUES (?, ?, ?)";

        try (Connection newConnection = this.connection();
             PreparedStatement query = newConnection.prepareStatement(insertQuery)) {
            query.setString(1, newWord);
            query.setString(2, newWordType);
            query.setString(3, newMeaing);
            query.executeUpdate();
            System.out.println("Add new word successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteWord(String deleteWord) {
        String deleteQuery = "DELETE FROM addedword WHERE word = ?";

        try (Connection newConnection = this.connection();
             PreparedStatement query = newConnection.prepareStatement(deleteQuery)) {
            query.setString(1, deleteWord);

            int affected = query.executeUpdate();

            if (affected > 0) {
                System.out.println("Delete word successfully");
            } else {
                System.out.println("Word does not exist");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void editWord(String word, String newWord, String newWordType, String newMeaning) {
        String editQuery = "UPDATE addedword SET word = ?, wordtype = ?, meaning = ? WHERE " +
                "word = ?";

        try (Connection newConnection = this.connection();
             PreparedStatement query = newConnection.prepareStatement(editQuery)) {
            query.setString(1, newWord);
            query.setString(2, newWordType);
            query.setString(3, newMeaning);
            query.setString(4, word);

            int affected = query.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public Map<String, String> getWordTypeAndMeaning(String word) {

        String searchQuery = "SELECT meaning, wordtype FROM addedword WHERE word = ?";
        Map<String, String> output = new HashMap<>();

        try (Connection newConnection = this.connection(); PreparedStatement query = newConnection.prepareStatement(searchQuery)) {
            query.setString(1, word);

            try (ResultSet outputMap = query.executeQuery()) {
                while (outputMap.next()) {
                    String wordtype = outputMap.getString("wordtype");
                    String definition = outputMap.getString("meaning");
                    output.put(definition, wordtype);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        if (output.isEmpty()) {
            throw new NullPointerException("Word not available");
        }
        return output;
    }

    public List<String> showAddedWord() {
        String sql = "SELECT word FROM addedword";

        List<String> output = new ArrayList<>();

        try (Connection conn = this.connection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet words = pstmt.executeQuery()) {
                while (words.next()) {
                    String word = words.getString("word");
                    output.add(word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
//        if (output.size() == 0) {
//            throw new NullPointerException("Word not available");
//        }
        return output;
    }

    public String getWordType(Map<String, String> definitions) {
        StringBuilder wordTypes = new StringBuilder();
        for (Map.Entry<String, String> entry : definitions.entrySet()) {
            String wordtype = entry.getValue();
            wordTypes.append(wordtype + "\n");
        }
        return wordTypes.toString();
    }

    public String getMeaning(Map<String, String> definitions) {
        StringBuilder meanings = new StringBuilder();
        for (Map.Entry<String, String> entry : definitions.entrySet()) {
            String def = entry.getKey().replaceAll("\\n\\s+", " ").replaceAll("--", "");
            meanings.append(def + "\n");
        }
        thisDefinition = meanings.toString();
        return meanings.toString();
    }

    public List<String> getAllWordsOffline() {
        String sql = "SELECT DISTINCT word FROM entries";

        List<String> words = new ArrayList<>();

        try (Connection conn = this.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String word = rs.getString("word");
                words.add(word.toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return words;
    }

    public List<String> getAllPhrasesOffline() {
        String sql = "SELECT phrv FROM phrasalverb ORDER BY phrv";

        List<String> words = new ArrayList<>();

        try (Connection conn = this.connection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String word = rs.getString("phrv");
                words.add(word.toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return words;
    }

    public String searchPhrase(String word) {
        StringBuilder output = new StringBuilder(word).append("\n\n");
        String searchQuery = "SELECT derivative, meaning, examples, synonyms FROM phrasalverb " +
                "WHERE " +
                "phrv =" +
                " ?";

        try (Connection newConnection = this.connection(); PreparedStatement query =
                newConnection.prepareStatement(searchQuery)) {
            query.setString(1, word);
            ResultSet outputString = query.executeQuery();

            String derivative = outputString.getString("derivative");
            output.append("Derivative:\n").append(derivative + "\n\n");

            String meaning = outputString.getString("meaning");
            output.append("Meaning:\n").append(meaning + "\n");


            String examples = outputString.getString("examples");
            output.append("For example:\n").append(examples + "\n");


            String synonyms = outputString.getString("synonyms");
            output.append("Synonyms:\n").append(synonyms + "\n");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        thisDefinition = output.toString();
        return output.toString();
    }

    public static void main(String[] args) {
        Offline test = new Offline();
        String wordToSearch = "abacus"; // Thay thế "your_word_here" bằng từ bạn muốn tìm kiếm

        try {
            String result = test.searchWord(wordToSearch);
            System.out.println("Search result for word '" + wordToSearch + "':");
            System.out.println(result);
        } catch (NullPointerException e) {
            System.out.println("Word not available");
        }
    }
}
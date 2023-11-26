package DicFX.Search.Trie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {
    TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }


    public void insert(String word) {
        if (word == null || word.isEmpty()) {
            return; // Skip insertion for empty or null words
        }

        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node == null) {
                System.out.println("DEBUG: Node is null for word: " + word);
                return;
            }

            if (node.children == null) {
                node.children = new HashMap<>();
            }

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.word = word;
    }


    public List<String> searchPrefix(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) {
                return List.of();
            }
        }

        return findAllWords(node);
    }

    private List<String> findAllWords(TrieNode node) {
        if (node == null) {
            return new ArrayList<>();
        }

        List<String> words = new ArrayList<>();
        if (node.word != null) {
            words.add(node.word);
        }

        for (TrieNode child : node.children.values()) {
            words.addAll(findAllWords(child));
        }

        return words;
    }

    public List<String> getAllWords() {
        return findAllWords(root);
    }


}


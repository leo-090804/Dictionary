package DicFX.Search.Trie;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    Map<Character, TrieNode> children;
    String word;

    TrieNode() {
        this.children = new HashMap<>();
        this.word = null;
    }
}


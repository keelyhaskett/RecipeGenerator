package recipeInfo.tags;

import java.util.HashMap;
import java.util.HashSet;

public class TagTrieNode {
	private final char character;

	private HashMap<Character, TagTrieNode> children;
	private HashSet<String> tags;

	public TagTrieNode(char character) {
		this.character = character;
		children = new HashMap<>();
		tags = new HashSet<>();
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public void addChild(Character key, TagTrieNode value) {
		children.put(key, value);
	}

	public HashMap<Character, TagTrieNode> getChildren() {
		return children;
	}

	public HashSet<String> getTags() {
		return tags;
	}
}

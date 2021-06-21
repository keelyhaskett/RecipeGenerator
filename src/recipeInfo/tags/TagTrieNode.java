package recipeInfo.tags;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Node element for the Trie involved in autocompleting tags.
 * Has the character represented by the node, all nodes beneath, and all tags connected to this node.
 */
public class TagTrieNode {
	private final char character;

	private final HashMap<Character, TagTrieNode> children;
	private final HashSet<String> tags;

	public TagTrieNode(char character) {
		this.character = character;
		children = new HashMap<>();
		tags = new HashSet<>();
	}

	public void addTag(String tag) {
		this.tags.add(tag);
	}

	public void addChild(Character key, TagTrieNode value) { children.put(key, value); }

	public HashMap<Character, TagTrieNode> getChildren() {
		return children;
	}

	public HashSet<String> getTags() {
		return tags;
	}
}

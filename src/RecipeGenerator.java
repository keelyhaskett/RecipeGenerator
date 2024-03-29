import recipeInfo.Recipe;
import recipeInfo.tags.TagTrieNode;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Main class for the program.
 * Implements any abstract methods from GUI and contains the master RecipeBook and TagTrieNode.
 */
public class RecipeGenerator extends GUI {
    private final RecipeBook recipes =  new RecipeBook();
    private final TagTrieNode trie = new TagTrieNode('\u0000');

    public static void main(String[] args) {
        new RecipeGenerator();
    }

    @Override
    protected RecipeBook getRecipes() {
        return recipes;
    }

    @Override
    protected void saveRecipe(Recipe r) {
        recipes.addRecipe(r);
    }

    @Override
    protected void loadTags(ArrayList<String> tags) {
        for (String tag : tags) {
            if (!recipes.checkAndAddTag(tag)) { continue; }
            tag = tag.toUpperCase();
            //if tag is new, add to the trie
            TagTrieNode current = trie;
            for (int i = 0; i < tag.length(); i++) {
                if (current.getChildren().containsKey(tag.charAt(i))) {
                    current = current.getChildren().get(tag.charAt(i));
                }
                else {
                    TagTrieNode temp = new TagTrieNode(tag.charAt(i));
                    current.addChild(tag.charAt(i), temp);
                    current = temp;
                }
                current.addTag(tag);
            }
        }
    }

    @Override
    protected HashSet<String> getSuggestedTags(String tag) {
        return searchTrie(tag.toUpperCase(), 0, trie);
    }

    @Override
    protected HashSet<String> getAllTags() {
        return recipes.getTags();
    }

    /**
     * Recursively searches the trie for the tag given.
     * @param tag   Tag to search for
     * @param currentPos    Current position down the tree
     * @param currentNode   Currentl node in the tree
     * @return  a Hashset of matching tags, null if none were found
     */
    private HashSet<String> searchTrie(String tag, int currentPos, TagTrieNode currentNode) {
        char c = tag.charAt(currentPos);

        //if we've hit the end of the tag, and the final character is logged, return all tags at the node
        if (currentPos == tag.length() - 1 && currentNode.getChildren().containsKey(c)) {
            return currentNode.getChildren().get(c).getTags();
        }
        //else if the current key is logged, search again one node deeper
        if (currentNode.getChildren().containsKey(c)) {
            return searchTrie(tag, currentPos + 1, currentNode.getChildren().get(c));
        }
        //otherwise no similar tags are stored so no suggestions can be made
        return null;
    }
}

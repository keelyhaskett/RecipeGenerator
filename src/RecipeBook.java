import recipeInfo.Recipe;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Collection of recipes and tags, has helpful recipe related methods such as duplicate checking, and various getters.
 */
public class RecipeBook {
    private final ArrayList<Recipe> recipes;
    private final HashSet<String> knownTags;

    public RecipeBook(){
        recipes = new ArrayList<>();
        knownTags = new HashSet<>();
    }

    public void addRecipe(Recipe r) {
        recipes.add(r);
    }

    /**
     * Check whether recipe already exists in the book.
     * @param r Recipe to check
     * @return true if recipe exists, false if it's new
     */
    public Boolean checkForDuplicate(Recipe r) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(r.getName())) { return true; }
        }
        return false;
    }

    /**
     * Check if a tag has been recorded, and record if not.
     * @param tag   Tag to check
     * @return  true if tag was recorded, false if tag already exists
     */
    public boolean checkAndAddTag(String tag) {
        if (knownTags.contains(tag.toUpperCase())) { return false; }
        knownTags.add(tag.toUpperCase());
        return true;
    }

    /**
     * Get all the names of recipes and return as an array.
     * @return  String array of recipe names
     */
    public String[] namesToArray() {
        String[] names = new String[recipes.size()];
        int count = 0;
        for (Recipe r : recipes) {
            names[count] = r.getName();
            count++;
        }
        return names;
    }

    public HashSet<String> getTags() {
        return knownTags;
    }

    /**
     * Get the recipe at given index in the book.
     * @param index Index of recipe
     * @return  recipe at that index, null if index is out of bounds
     */
    public Recipe getRecipeAt(int index) {
        if (index >= recipes.size()) {
            return null;
        }
        return recipes.get(index);
    }

    /**
     * Get a recipe by its name
     * @param r Name of recipe to get
     * @return  recipe if can be found, null if doesn't exist
     */
    public Recipe getRecipeByName(String r) {
        for (Recipe recipe : recipes) {
            if (recipe.getName().equals(r)) { return recipe; }
        }
        return null;
    }

    /**
     * Gets a random recipe from a list of qualifying recipes.
     * @param numServes Number of serves of the recipe
     * @param maxTime   Max time allowed for the recipe to take
     * @param tags  Tags the recipe has to have
     * @return  Recipe which matches qualifiers
     */
    public String getRecipeWhere (int numServes, Duration maxTime, List<String> tags) {
        List<Recipe> recipeCandidates = getRecipesWhere(numServes, maxTime, tags);
        if (recipeCandidates.isEmpty()) { return null; }
        if (recipeCandidates.size() == 1) { return recipeCandidates.get(0).getName(); }
        return recipeCandidates.get((int)Math.round(Math.random() * (recipeCandidates.size() - 1))).getName();
    }


    /**
     * Get all recipes which meet qualifiers.
     @param numServes Number of serves of the recipe
      * @param maxTime   Max time allowed for the recipe to take
     * @param tags  Tags the recipe has to have
     * @return list of recipes which match qualifiers
     */
    public List<Recipe> getRecipesWhere(int numServes, Duration maxTime, List<String> tags) {
        List<Recipe> potentialRecipes = new ArrayList<>(recipes);
        if (numServes != 0) {
            potentialRecipes = potentialRecipes.stream().filter(s -> s.getInfo().getServes() == numServes).collect(Collectors.toList());
        }
        if (maxTime.toMinutes() >= 1) {
            potentialRecipes = potentialRecipes.stream().filter(s -> s.getTotTime().toMinutes() <= maxTime.toMinutes()).collect(Collectors.toList());
        }
        return potentialRecipes.stream().filter(s -> s.getTags().containsAll(tags)).collect(Collectors.toList());
    }
}

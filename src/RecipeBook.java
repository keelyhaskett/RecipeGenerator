import recipeInfo.Recipe;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeBook {
    private ArrayList<Recipe> recipes;
    private HashSet<String> knownTags;

    public RecipeBook(){
        recipes = new ArrayList<>();
        knownTags = new HashSet<>();
    }

    public RecipeBook(ArrayList<Recipe> r) {
        this.recipes = r;
    }

    public void addRecipe(Recipe r) {
        recipes.add(r);
    }

    public boolean checkAndAddTag(String tag) {
        if (knownTags.contains(tag.toUpperCase())) { return false; }
        knownTags.add(tag.toUpperCase());
        return true;
    }

    public boolean checkAndAddTag(Collection<String> tags) {
        boolean anyUnique = false;
        for (String t : tags) {
            if (knownTags.contains(t.toUpperCase())) {continue;}
            anyUnique = true;
            knownTags.add(t.toUpperCase());
        }
        return anyUnique;
    }

    public String[] namesToArray() {
        String[] names = new String[recipes.size()];
        int count = 0;
        for (Recipe r : recipes) {
            names[count] = r.getName();
            count++;
        }
        return names;
    }

    public Recipe getRecipeAt(int index) {
        if (index >= recipes.size()) {
            return null;
        }
        return recipes.get(index);
    }

    public String getRecipeWhere (int numServes, Duration maxTime, List<String> tags) {
        List<Recipe> recipeCandidates = getRecipesWhere(numServes, maxTime, tags);
        if (recipeCandidates.isEmpty()) { return null; }
        if (recipeCandidates.size() == 1) { return recipeCandidates.get(0).getName(); }
        return recipeCandidates.get((int)Math.round(Math.random() * recipeCandidates.size() - 1)).getName();
    }


    /**
     * @param numServes
     * @param maxTime
     * @param tags
     * @return
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

    public HashSet<String> getTags() {
        return knownTags;
    }
}

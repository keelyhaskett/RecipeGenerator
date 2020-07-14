import RecipeInfo.Recipe;
import RecipeInfo.RecipeCategories.RecipeClassifier;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("rawtypes")
public class RecipeBook {
    private HashSet<Recipe> recipes;

    public RecipeBook(){}

    public RecipeBook(HashSet<Recipe> r) {
        this.recipes = r;
    }

    public void addRecipe(Recipe r) {
        recipes.add(r);
    }


    /**
     * When passed an enum value, method will fetch all relevant recipes
     * from the book and return them as a list.
     * @param e     Enum value to find recipes that match
     * @return      Returns a list of matching recipes
     */
    public List<Recipe> getRecipesWhere(RecipeClassifier e) {

        //TODO: add functionality when descriptive enums are implemented (e.g type of meal, meat content, time etc)
        return null;
    }
}

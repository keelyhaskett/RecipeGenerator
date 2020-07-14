package RecipeInfo;

import RecipeInfo.RecipeContents.Ingredient;
import RecipeInfo.RecipeContents.Measurement;
import RecipeInfo.RecipeContents.Method;

import java.time.Duration;
import java.util.HashMap;
import java.util.Objects;

public class Recipe {

    private HashMap<Ingredient, Measurement> ingredients;
    private Method method;
    private String name;
    private Duration prepTime;
    private Duration cookTime;
    private int serves;

    public Recipe(HashMap<Ingredient, Measurement> i, Method m, String n, Duration prep, Duration cook, int s) {
        this.ingredients = new HashMap<>(i);
        this.method = m;
        this.name = n;
        this.prepTime = prep;
        this.cookTime = cook;
        this.serves = s;
    }

    @Override
    public String toString() {
        StringBuilder b =  new StringBuilder();
        b.append(name).append("\n");
        b.append("Serves: ").append(serves).append("\n");
        b.append("Prep Time: ").append(prepTime.toMinutes()).append("   Cook Time: ").append(cookTime.toMinutes()).append("   Total Time: ").append(prepTime.plus(cookTime).toMinutes()).append("\n");
        for (Ingredient i : ingredients.keySet()) {
            b.append(ingredients.get(i).toString());
            b.append(" ").append(i.toString());
            b.append("\n");
        }
        b.append(method.toString());


        return b.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return serves == recipe.serves &&
                ingredients.equals(recipe.ingredients) &&
                method.equals(recipe.method) &&
                name.equals(recipe.name) &&
                prepTime.equals(recipe.prepTime) &&
                cookTime.equals(recipe.cookTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredients, method, name, prepTime, cookTime, serves);
    }
}

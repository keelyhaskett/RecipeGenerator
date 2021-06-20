package recipeInfo;

import recipeInfo.recipeContents.Ingredient;
import recipeInfo.recipeContents.Measurement;
import recipeInfo.recipeContents.Method;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Recipe {

    private ArrayList<Ingredient> ingredients;
    private Method method;
    private String name;
    private Duration prepTime;
    private Duration cookTime;
    private int serves;

    public Recipe(ArrayList<Ingredient> i, Method m, String n, Duration prep, Duration cook, int s) {
        this.ingredients = new ArrayList<>(i);
        this.method = m;
        this.name = n;
        this.prepTime = prep;
        this.cookTime = cook;
        this.serves = s;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        StringBuilder b =  new StringBuilder();
        b.append(name).append("\n");
        b.append("Serves: ").append(serves).append("\n");
        b.append("Prep Time: ").append(prepTime.toMinutes()).append("   Cook Time: ").append(cookTime.toMinutes()).append("   Total Time: ").append(prepTime.plus(cookTime).toMinutes()).append("\n");
        for (Ingredient i : ingredients) {
           b.append(i.toString());
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

package recipeInfo;

import recipeInfo.recipeContents.InfoBlock;
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
    private InfoBlock info;

    public Recipe(ArrayList<Ingredient> i, Method m, String n, InfoBlock info) {
        this.ingredients = new ArrayList<>(i);
        this.method = m;
        this.name = n;
        this.info = info;
    }

    public String getName() { return name; }

    public String toFileFormat() {
        StringBuilder b =  new StringBuilder();
        b.append("( ").append(name).append(" ) \n");
        b.append(info.toFileFormat());
        b.append("<start> ");
        for (Ingredient i : ingredients) {
            b.append(i.toFileFormat());
        }
        b.append("<stop> ");
        b.append(method.toFileFormat());
        return b.toString();
    }

    @Override
    public String toString() {
        StringBuilder b =  new StringBuilder();
        b.append(name).append("\n");
        b.append("Serves: ").append(info.getServes()).append("\n");
        b.append("Prep Time: ").append(info.getPrepTime().toMinutes()).append("   Cook Time: ").append(info.getCookTime().toMinutes()).append("   Total Time: ").append(info.getPrepTime().plus(info.getCookTime()).toMinutes()).append("\n");
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
        return info.getServes() == recipe.info.getServes() &&
                ingredients.equals(recipe.ingredients) &&
                method.equals(recipe.method) &&
                name.equals(recipe.name) &&
                info.getPrepTime().equals(recipe.info.getPrepTime()) &&
                info.getCookTime().equals(recipe.info.getCookTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredients, method, name, info);
    }
}

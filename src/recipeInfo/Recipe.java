package recipeInfo;

import recipeInfo.recipeContents.InfoBlock;
import recipeInfo.recipeContents.Ingredient;
import recipeInfo.recipeContents.Method;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Recipe object, containing all elements of a recipe.
 * Getters for some fields, master file formatter for outward parsing.
 */
public class Recipe {

    private final ArrayList<Ingredient> ingredients;
    private final Method method;
    private final String name;
    private final InfoBlock info;
    private final ArrayList<String> tags;
    private final Duration totTime;

    public Recipe(ArrayList<Ingredient> i, Method m, String n, InfoBlock info, ArrayList<String> t) {
        this.ingredients = new ArrayList<>(i);
        this.method = m;
        this.name = n;
        this.info = info;
        this.tags = new ArrayList<>(t);
        totTime = info.getPrepTime().plus(info.getCookTime());
    }

    public ArrayList<String> getTags() { return tags; }

    public InfoBlock getInfo() { return info; }

    public Duration getTotTime() { return totTime; }

    public String getName() { return name; }

    public ArrayList<Ingredient> getIngredients() { return ingredients; }

    /**
     * Format the recipe into a parsable format.
     * @return formatted recipe string
     */
    public String toFileFormat() {
        StringBuilder b =  new StringBuilder();
        b.append("( ").append(name).append(" ) ");
        b.append(info.toFileFormat());
        b.append(" <start> ");
        for (Ingredient i : ingredients) { b.append(i.toFileFormat()); }
        b.append("<stop> ");
        b.append(method.toFileFormat());
        b.append(" <tagOpen> ");
        tags.forEach(s -> b.append(" ( ").append(s).append(" ) "));
        b.append(" <tagClose>");

        return b.toString();
    }

    @Override
    public String toString() {
        StringBuilder b =  new StringBuilder();
        b.append(name).append("\n");
        b.append("Serves: ").append(info.getServes()).append("\n");
        b.append("Prep Time: ").append(((int) info.getPrepTime().toMinutes()) / 60).append(":");
        long minutes = info.getPrepTime().toMinutes() % 60;
        if (minutes < 10) {b.append(0);}//if the number isn't 2 digits, add a second
        b.append(minutes);
        b.append("   Cook Time: ").append(((int) info.getCookTime().toMinutes()) / 60).append(":");
        minutes = info.getCookTime().toMinutes() % 60;
        if (minutes < 10) {b.append(0);}//if the number isn't 2 digits, add a second
        b.append(minutes);
        b.append("   Total Time: ").append(((int) totTime.toMinutes()) / 60).append(":");
        minutes = totTime.toMinutes() % 60;
        if (minutes < 10) {b.append(0);}//if the number isn't 2 digits, add a second
        b.append(minutes).append("\n\n");
        b.append("Ingredients: \n");
        ingredients.forEach(i -> b.append(i.toString()));
        b.append("\n").append(method.toString());
        b.append("\n").append("Tags: ");
        tags.forEach(s ->b.append(s).append(", "));//add each tag and a comma between each tag
        b.deleteCharAt(b.length()-2);//delete redundant extra comma


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

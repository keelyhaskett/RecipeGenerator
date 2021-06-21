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
    private ArrayList<String> tags;
    private Duration totTime;

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

    public String toFileFormat() {
        StringBuilder b =  new StringBuilder();
        b.append("( ").append(name).append(" ) ");
        b.append(info.toFileFormat());
        b.append(" <start> ");
        for (Ingredient i : ingredients) {
            b.append(i.toFileFormat());
        }
        b.append("<stop> ");
        b.append(method.toFileFormat());
        b.append("\n").append(" <tagOpen ");
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
        if (minutes < 10) {b.append(0);}
        b.append(minutes);
        b.append("   Cook Time: ").append(((int) info.getCookTime().toMinutes()) / 60).append(":");
        minutes = info.getCookTime().toMinutes() % 60;
        if (minutes < 10) {b.append(0);}
        b.append(minutes);
        b.append("   Total Time: ").append(((int) totTime.toMinutes()) / 60).append(":");
        minutes = totTime.toMinutes() % 60;
        if (minutes < 10) {b.append(0);}
        b.append(minutes).append("\n\n");
        b.append("Ingredients: \n");
        ingredients.forEach(i -> b.append(i.toString()));
        b.append("\n").append(method.toString());
        b.append("\n").append("Tags: ");
        tags.forEach(s ->b.append(s).append(", "));
        b.deleteCharAt(b.length()-2);


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

import recipeInfo.Recipe;

public class RecipeGenerator extends GUI {
    private RecipeBook recipes =  new RecipeBook();


    //TODO: Using GUI designer, create a GUI, maybe own class?
    //TODO: Write parser and file writer

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
}

import javax.swing.*;

public class RecipeGenerator {
    public RecipeBook recipes =  new RecipeBook();


    //TODO: Using GUI designer, create a GUI, maybe own class?
    //TODO: Write parser and file writer

    public static void main(String[] args) {
        JFrame frame = new GUI("Recipe Generator");
        frame.setVisible(true);

    }
}

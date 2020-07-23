import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public abstract class GUI {
    private JPanel mainPanel;
    private JFrame startWindow;
    private JFrame mainWindow;
    private JFrame generationWindow;
    private JFrame recipeFormWindow;

    public GUI() {
        build();
    }

    /**
     *
     */
    private void build() {
        JButton start = new JButton("Start");
        start.setPreferredSize(new Dimension(100, 60));
        start.setMaximumSize(new Dimension(300, 80));
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { openMainWindow(); } //opens the new window on click
        });

        JButton quit = new JButton("Quit");
        quit.setPreferredSize(new Dimension(100, 60));
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { System.exit(0); } //ends the program
        });


        JLabel title = new JLabel();
        title.setText("Recipe Generator");
        title.setFont(new Font("Helvetica", Font.ITALIC, 60));



        startWindow = new JFrame("Recipe Generator");

        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
        startWindow.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        //add the title to the frame
        constraints.ipady = 70;
        constraints.ipadx = 100;
        constraints.gridx = 0;
        constraints.gridy = 0; //set the element placing to a 0,0 pos (with above line)
        constraints.gridwidth = 3; //make the element take up 3 column positions
        constraints.weightx = 1.0;
        //TODO: title won't sit centered on the screen, how to fix????
        startWindow.add(title, constraints);

        //add the start button to the frame
        constraints.weightx = 0.0;
        constraints.ipady = 0;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 20, 0); //adds a gap between the buttons
        startWindow.add(start, constraints);

        //add the quit button to the frame
        constraints.gridx = 1;
        constraints.gridy = 2;
        startWindow.add(quit, constraints);


        //startWindow.setContentPane(mainPanel);
        startWindow.pack();
        startWindow.setLocationRelativeTo(null); //centers the window on the screen
        startWindow.setVisible(true); //so we can see the gui
    }

    /**
     *
     */
    private void openMainWindow() {
        mainWindow = new JFrame("Recipe Generator");
        startWindow.setVisible(false); //make the start window go away

        //TODO: create load, generate, and quit buttons

        //TODO: allow for 3 panels, button panel, recipes total, and something else

        //TODO: make 3rd window on generation showing the titles of the generated recipes.

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
        mainWindow.setContentPane(mainPanel);
        mainWindow.pack();
        mainWindow.setVisible(true); //so we can see the gui


    }

    /**
     *
     */
    private void generatedRecipesWindow() {
        generationWindow = new JFrame("Pick your generation options");

        //TODO: code for generated recipes here

        //TODO: eventually add shopping list here or make another window method

        generationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //TODO: setContentPane line, should probably make new panel, research it
        //BREAKTHROUGH! PANELS ARE WHAT MAKE FUNKY LAYOUTS!!!
        generationWindow.pack();
        generationWindow.setVisible(true);
    }

    /**
     *
     */
    private void recipeCreationWindow() {
        recipeFormWindow = new JFrame("Enter your recipe");

        //TODO: code for a form that lets user fill out a recipe and a file is created based on that

        recipeFormWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //ideally will have a cancel button that we prefer user to use
        //TODO: make panel for form
        recipeFormWindow.pack();
        recipeFormWindow.setVisible(true);
    }

}

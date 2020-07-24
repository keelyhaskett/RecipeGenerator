import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 */
public abstract class GUI {
    private static final int START_WINDOW_BUTTON_TEXT_SIZE = 30;
    private static final int START_WINDOW_TITLE_TEXT_SIZE = 60;

    private final Dimension startWindowButtonSize = new Dimension(100, 60);
    private final Dimension startWindowMinimumSize = new Dimension(800, 500);

    private final Color bgCol = new Color(170, 210, 240);
    private final Color primaryTextCol = new Color(68, 55, 66);
    private final Color primaryButCol = new Color(79, 180, 119);
    private final Color hoverTextCol = new Color(237, 233, 237);
    private final Color hoverButCol = new Color(39, 144, 81);

    private JFrame startWindow;
    private JFrame mainWindow;
    private JFrame generationWindow;
    private JFrame recipeFormWindow;



    public GUI() {
        buildStartWindow();
    }

    /**
     *  Builds the first window to open when the program is run.
     *  Contains a title label with the name of the program, a start button,
     *  which when clicked runs the method to open the main window (and close
     *  this start window), and a quit button, which closes the program.
     *  Window is built with a JFrame using GridBag layout
     */
    private void buildStartWindow() {
        //create and format the start button
        JButton start = new JButton("Start");
        start.setPreferredSize(startWindowButtonSize);
        start.setFont(new Font("Helvetica", Font.PLAIN, START_WINDOW_BUTTON_TEXT_SIZE));
        start.setForeground(primaryTextCol);
        start.setBackground(primaryButCol);
        start.setFocusPainted(false); //turns off grey border around text on press
        //TODO: remove white overlay on press, need to mess with icons?
        start.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buildMainWindow(); //opens the main window
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) {
                //adjust to hover colours on hover
                start.setForeground(hoverTextCol);
                start.setBackground(hoverButCol);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //adjust to non-hover colours off hover
                start.setForeground(primaryTextCol);
                start.setBackground(primaryButCol);
            }
        });

        //create and format the quit button
        JButton quit = new JButton("Quit");
        quit.setPreferredSize(startWindowButtonSize);
        quit.setFont(new Font("Helvetica", Font.PLAIN, START_WINDOW_BUTTON_TEXT_SIZE));
        quit.setForeground(primaryTextCol);
        quit.setBackground(primaryButCol);
        quit.setFocusPainted(false); //turns off grey border around text on press
        quit.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0); //ends the program
            }

            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) {
                //adjust to hover colours on hover
                quit.setForeground(hoverTextCol);
                quit.setBackground(hoverButCol);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //adjust to non-hover colours off hover
                quit.setForeground(primaryTextCol);
                quit.setBackground(primaryButCol);
            }
        });

        //create and format the title text
        JLabel title = new JLabel("Recipe Generator");
        title.setFont(new Font("Helvetica", Font.ITALIC, START_WINDOW_TITLE_TEXT_SIZE));
        title.setForeground(primaryTextCol);
        title.setHorizontalAlignment(SwingConstants.CENTER); // makes sure the title is always centered on the form


        startWindow = new JFrame("Recipe Generator");


        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
        startWindow.setLayout(new GridBagLayout());
        startWindow.getContentPane().setBackground(bgCol);
        startWindow.setMinimumSize(startWindowMinimumSize); //makes sure the frame stays at a nice viewable size.

        GridBagConstraints constraints = new GridBagConstraints();

        //add the title to the frame
        constraints.ipady = 70; //add extra height padding to accommodate for the text
        constraints.ipadx = 300; //add padding either side for a bigger form
        //set the element placing to a 0,0 pos
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3; //make the element take up 3 column positions
        constraints.weightx = 1.0; //forces the title to take up as much space as possible
        startWindow.add(title, constraints);

        //add the start button to the frame
        constraints.weightx = 0.0; //button should only take up needed room
        constraints.ipady = 0; //revert to no extra padding
        //set the element placing to a 1,1 pos
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 20, 0); //adds a gap between the buttons
        startWindow.add(start, constraints);

        //add the quit button to the frame
        //set the element placing to a 1,2 pos
        constraints.gridx = 1;
        constraints.gridy = 2;
        startWindow.add(quit, constraints);


        //startWindow.setContentPane(mainPanel);
        startWindow.pack(); //makes all frame contents at or above their preferred size
        startWindow.setLocationRelativeTo(null); //centers the window on the screen
        startWindow.setVisible(true); //so we can see the gui
    }

    /**
     *
     */
    private void buildMainWindow() {
        mainWindow = new JFrame("Recipe Generator");
        startWindow.setVisible(false); //make the start window go away

        //TODO: create load, generate, and quit buttons

        //TODO: allow for 3 panels, button panel, recipes total, and something else

        //TODO: make 3rd window on generation showing the titles of the generated recipes.

        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
        //mainWindow.setContentPane(mainPanel);
        mainWindow.pack();
        mainWindow.setVisible(true); //so we can see the gui


    }

    /**
     *
     */
    private void buildGeneratedRecipesWindow() {
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
    private void buildRecipeFormWindow() {
        recipeFormWindow = new JFrame("Enter your recipe");

        //TODO: code for a form that lets user fill out a recipe and a file is created based on that

        recipeFormWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //ideally will have a cancel button that we prefer user to use
        //TODO: make panel for form
        recipeFormWindow.pack();
        recipeFormWindow.setVisible(true);
    }

}

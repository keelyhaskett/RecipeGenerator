import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Keely Haskett
 */
public abstract class GUI {

    protected abstract RecipeBook getRecipes();



    private static final int START_WINDOW_BUTTON_TEXT_SIZE = 30;
    private static final int START_WINDOW_TITLE_TEXT_SIZE = 60;
    private static final int MAIN_WINDOW_BUTTON_TEXT_SIZE = 20;
    private static final int RECIPE_LIST_TEXT_SIZE = 15;
    private static final int RECIPE_FORM_TEXT_SIZE = 20;
    private static final int RECIPE_FORM_COMPONENT_TEXT_SIZE = 15;

    private final Dimension startWindowButtonSize = new Dimension(100, 60);
    private final Dimension startWindowMinimumSize = new Dimension(800, 500);
    private final Dimension mainWindowButtonSize = new Dimension(100, 60);
    private final Dimension recipeListPreferredSize = new Dimension(300, 600);
    private final Dimension recipeFormButtonSize = new Dimension(80, 50);
    private final Insets buttonPanelInsets = new Insets(15, 0, 15, 0);
    private final Insets buttonPanelQuitInsets = new Insets(recipeListPreferredSize.height -
                                                ((buttonPanelInsets.top + buttonPanelInsets.bottom) * 3 +
                                                 (mainWindowButtonSize.height * 4) - 30), 0, 30, 0);
    private final Font startWindowButtonFont = new Font("Helvetica", Font.PLAIN, START_WINDOW_BUTTON_TEXT_SIZE);
    private final Font mainWindowButtonFont = new Font("Helvetica", Font.PLAIN, MAIN_WINDOW_BUTTON_TEXT_SIZE);
    private final Font recipeFormWindowLabelFont = new Font("Helvetica", Font.PLAIN, RECIPE_FORM_TEXT_SIZE);
    private final Font recipeFormWindowComponentFont = new Font("Helvetica", Font.ITALIC, RECIPE_FORM_COMPONENT_TEXT_SIZE);

    private final Color bgCol = new Color(170, 210, 240);
    private final Color primaryTextCol = new Color(68, 55, 66);
    private final Color primaryButCol = new Color(79, 180, 119);
    private final Color hoverTextCol = new Color(237, 233, 237);
    private final Color hoverButCol = new Color(39, 144, 81);
    private final Color pressedButCol = new Color(5, 78, 34);

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
        Button start = new Button("Start");
        start.setPreferredSize(startWindowButtonSize);
        start.setFont(startWindowButtonFont);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { buildMainWindow(); }
        });

        //create and format the quit button
        Button quit = new Button("Quit");
        quit.setPreferredSize(startWindowButtonSize);
        quit.setFont(startWindowButtonFont);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { System.exit(0); }
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
     *  The main window hosts majority of the functionality of the program.
     *  It includes 2 panels, the first of which containing 4 buttons:
     *  A load button which allows the user to select a preformed recipe file, which the parser will process and
     *  create a recipe object based off, if the file is in proper format.
     *  A create button, which allows the user to build a recipe file with information in a prebuilt form, which
     *  will both create a recipe, and also produce a formatted recipe file for future use.
     *  A generate button, which will open a window to allow the user to select their recipe generation options, and
     *  then the program will give a set of recipes (and a shopping list eventually) based on the user's specifications.
     *  A quit button which exits the program.
     *
     *  The second panel features a list of recipes, and will eventually allow the user to remove any recipes from the
     *  recipe book that they do not want to include.
     */
    private void buildMainWindow() {
        mainWindow = new JFrame("Recipe Generator");
        startWindow.setVisible(false); //make the start window go away
        mainWindow.getContentPane().setBackground(bgCol);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgCol);

        //TODO: consider recipe edit button??

        Button load = new Button("Load");
        JFileChooser fileChooser = new JFileChooser();
        load.setPreferredSize(mainWindowButtonSize);
        load.setFont(mainWindowButtonFont);
        load.setToolTipText("Load a recipe file.");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileChooser.setCurrentDirectory(new File("."));
                fileChooser.setDialogTitle("Select recipe file.");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (fileChooser.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) { //if the user chose an
                                                                                            //acceptable file
                    File file = fileChooser.getSelectedFile();
                    //TODO: parse file here and return some sort of runtime exception if it doesn't parse properly

                }
            }
        });

        Button create = new Button("Create");
        create.setPreferredSize(mainWindowButtonSize);
        create.setFont(mainWindowButtonFont);
        create.setToolTipText("Create recipe file by filling in recipe form.");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { buildRecipeFormWindow(); } //open the form to create recipe file
        });

        Button generate = new Button("Generate");
        generate.setPreferredSize(mainWindowButtonSize);
        generate.setFont(mainWindowButtonFont);
        generate.setToolTipText("Select some options, and generate recipes.");
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { buildGeneratedRecipesWindow(); } //open generation window
        });

        Button quit = new Button("Quit");
        quit.setPreferredSize(mainWindowButtonSize);
        quit.setFont(mainWindowButtonFont);
        quit.setToolTipText("Close program.");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { System.exit(0); } //close program
        });


        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.ipadx = 30;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = buttonPanelInsets;
        buttonPanel.add(load, constraints);

        constraints.gridy = 1;
        constraints.insets = buttonPanelInsets;
        buttonPanel.add(create, constraints);

        constraints.gridy = 2;
        constraints.insets = buttonPanelInsets;
        buttonPanel.add(generate, constraints);

        constraints.gridy = 3;
        constraints.insets = buttonPanelQuitInsets;
        buttonPanel.add(quit, constraints);


        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BorderLayout());

        JList<String> recipes = new JList<>();
        recipes.setListData(getRecipes().namesToArray()); //add all of the name of recipes in recipebook to the list
        recipes.setLayoutOrientation(JList.VERTICAL);
        recipes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //TODO: change this to multiple selection when understanding
        recipes.setFont(new Font("Helvetica", Font.PLAIN, RECIPE_LIST_TEXT_SIZE));
        JScrollPane recipeScroll = new JScrollPane(recipes);
        recipeScroll.createVerticalScrollBar();
        recipeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); //only show scroll when necessary
        recipeScroll.setPreferredSize(recipeListPreferredSize);

        recipePanel.add(recipeScroll, BorderLayout.CENTER);


        //TODO: figure out purpose of 3rd panel


        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
        mainWindow.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0)); //TODO: maybe make this BorderLayout instead? look into it
        mainWindow.add(buttonPanel);
        mainWindow.add(recipePanel);

        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null); //centers the window on the screen
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
        recipeFormWindow.getContentPane().setBackground(bgCol);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(recipeFormWindowLabelFont);
        nameLabel.setForeground(primaryTextCol);

        JLabel servesLabel = new JLabel("Serves:");
        servesLabel.setFont(recipeFormWindowLabelFont);
        servesLabel.setForeground(primaryTextCol);

        JLabel prepLabel = new JLabel("Prep Time:");
        prepLabel.setFont(recipeFormWindowLabelFont);
        prepLabel.setForeground(primaryTextCol);

        JLabel cookLabel = new JLabel("Cook Time:");
        cookLabel.setFont(recipeFormWindowLabelFont);
        cookLabel.setForeground(primaryTextCol);

        JLabel ingredientsLabel = new JLabel("Ingredients");
        ingredientsLabel.setFont(recipeFormWindowLabelFont);
        ingredientsLabel.setForeground(primaryTextCol);

        JLabel methodLabel = new JLabel("Method");
        methodLabel.setFont(recipeFormWindowLabelFont);
        methodLabel.setForeground(primaryTextCol);

        JTextField nameField = new JTextField();
        nameField.setFont(recipeFormWindowComponentFont);
        nameField.setForeground(primaryTextCol);

        JTextField prepField = new JTextField();
        prepField.setFont(recipeFormWindowComponentFont);
        prepField.setForeground(primaryTextCol);

        JTextField cookField = new JTextField();
        cookField.setFont(recipeFormWindowComponentFont);
        cookField.setForeground(primaryTextCol);

        JTextField ingredientsField = new JTextField();
        ingredientsField.setFont(recipeFormWindowComponentFont);
        ingredientsField.setForeground(primaryTextCol);

        JTextField methodField = new JTextField();
        methodField.setFont(recipeFormWindowComponentFont);
        methodField.setForeground(primaryTextCol);

        JSlider servesSlider = new JSlider();
        servesSlider.setMinimum(1);
        servesSlider.setMaximum(20);
        servesSlider.setMinorTickSpacing(1);
        servesSlider.setFont(recipeFormWindowComponentFont);
        servesSlider.setForeground(primaryTextCol);

        JList<String> ingredientsList = new JList<>();
        ingredientsList.setFont(recipeFormWindowComponentFont);
        ArrayList<String> listOfIngredients = new ArrayList<>();

        JList<String> methodList = new JList<>();
        methodList.setFont(recipeFormWindowComponentFont);

        JScrollPane ingredientsScroll = new JScrollPane();
        ingredientsScroll.add(ingredientsList);
        ingredientsScroll.createVerticalScrollBar();
        ingredientsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //TODO: set preferred size

        JScrollPane methodScroll = new JScrollPane();
        methodScroll.add(methodList);
        methodScroll.createVerticalScrollBar();
        methodScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //TODO: set preferred size

        Button ingredientButton = new Button("OK!");
        //TODO: add fields for amount and measurement type to go before ingredient field
        ingredientButton.setFont(recipeFormWindowComponentFont);
        ingredientButton.setPreferredSize(recipeFormButtonSize);
        ingredientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!ingredientsField.getText().equals("")) {
                    //listOfIngredients.add(ingredientsField.getText())
                }
            }
        });

        Button methodButton = new Button("OK!");
        methodButton.setFont(recipeFormWindowComponentFont);
        methodButton.setPreferredSize(recipeFormButtonSize);
        methodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setFont(recipeFormWindowComponentFont);
        methodButton.setPreferredSize(recipeFormButtonSize);
        methodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        Button doneButton = new Button("Done");
        doneButton.setFont(recipeFormWindowComponentFont);
        doneButton.setPreferredSize(recipeFormButtonSize);
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //TODO: insert checkboxes and radio buttons for further specifications eg dietary and meat contents


        recipeFormWindow.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        recipeFormWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //ideally will have a cancel button that we prefer user to use
        //TODO: make panel for form
        recipeFormWindow.pack();
        recipeFormWindow.setVisible(true);
    }

    /**
     * Button innerclass allows more control over the appearance and actions of the buttons in the GUI,
     * but as it extends JButton, it is still recognizable as a swing component.
     */
    private class Button extends JButton {

        public Button (String text) {
            super(text);
            setContentAreaFilled(false); //stops the colour turning grey when pressed
            setOpaque(true); //ensures every pixel is painted

            setForeground(primaryTextCol);
            setBackground(primaryButCol);
            setFocusPainted(false); //turns off grey border around text on press
            setText(text);

            addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (getModel().isPressed()) {
                        //adjust to pressed colouration
                        setForeground(hoverTextCol);
                        setBackground(pressedButCol);
                    }
                    else if (getModel().isRollover()) {
                        //adjust to hover colouration
                        setForeground(hoverTextCol);
                        setBackground(hoverButCol);
                    }
                    else {
                        //adjust to regular colouration
                        setForeground(primaryTextCol);
                        setBackground(primaryButCol);
                    }
                }
            });
        }
    }
}

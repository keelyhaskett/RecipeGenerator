
import recipeInfo.Recipe;
import recipeInfo.recipeContents.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Build the GUI in Java Swing
 * @author Keely Haskett
 */
public abstract class GUI {

	protected abstract RecipeBook getRecipes();
	protected abstract void saveRecipe(Recipe r);
	protected abstract void loadTags(ArrayList<String> tags);
	protected abstract HashSet<String> getSuggestedTags(String tag);
	protected abstract HashSet<String> getAllTags();

	private static final int START_WINDOW_BUTTON_TEXT_SIZE = 30;
	private static final int START_WINDOW_TITLE_TEXT_SIZE = 60;
	private static final int MAIN_WINDOW_BUTTON_TEXT_SIZE = 16;
	private static final int RECIPE_LIST_TEXT_SIZE = 15;
	private static final int RECIPE_FORM_TEXT_SIZE = 15;
	private static final int RECIPE_FORM_COMPONENT_TEXT_SIZE = 15;

	private final Dimension startWindowButtonSize = new Dimension(100, 60);
	private final Dimension startWindowMinimumSize = new Dimension(800, 500);
	private final Dimension mainWindowButtonSize = new Dimension(100, 60);
	private final Dimension recipeListPreferredSize = new Dimension(300, 600);
	private final Dimension recipeFormButtonSize = new Dimension(80, 50);
	private final Insets buttonPanelInsets = new Insets(15, 0, 15, 0);
	private final Insets buttonPanelQuitInsets = new Insets(recipeListPreferredSize.height -
			((buttonPanelInsets.top + buttonPanelInsets.bottom) * 4 +
					(mainWindowButtonSize.height * 5) - 30), 0, 30, 0);
	private final Font startWindowButtonFont = new Font("Helvetica", Font.PLAIN, START_WINDOW_BUTTON_TEXT_SIZE);
	private final Font mainWindowButtonFont = new Font("Helvetica", Font.PLAIN, MAIN_WINDOW_BUTTON_TEXT_SIZE);
	private final Font recipeFormWindowLabelFont = new Font("Helvetica", Font.PLAIN, RECIPE_FORM_TEXT_SIZE);
	private final Font recipeFormWindowComponentFont = new Font("Helvetica", Font.PLAIN, RECIPE_FORM_COMPONENT_TEXT_SIZE);

	private final Color bgCol = new Color(170, 210, 240);
	private final Color primaryTextCol = new Color(68, 55, 66);
	private final Color primaryButCol = new Color(79, 180, 119);
	private final Color hoverTextCol = new Color(237, 233, 237);
	private final Color hoverButCol = new Color(39, 144, 81);
	private final Color pressedButCol = new Color(5, 78, 34);

	private JFrame startWindow;
	private JFrame mainWindow;
	private JFrame recipeFormWindow;

	volatile Step[] stepsCollection;
	volatile Ingredient[] ingredientsCollection;
	volatile String[] tagsCollection;
	volatile String[] generatedRecipesCollection;

	JList<String> recipes;

	public GUI() {
		buildStartWindow();
	}

	/**
	 * Builds the first window to open when the program is run.
	 * Contains a title label with the name of the program, a start button,
	 * which when clicked runs the method to open the main window (and close
	 * this start window), and a quit button, which closes the program.
	 * Window is built with a JFrame using GridBag layout
	 */
	private void buildStartWindow() {
		//create and format the start button
		Button start = new Button("Start");
		start.setPreferredSize(startWindowButtonSize);
		start.setFont(startWindowButtonFont);
		start.addActionListener(e -> buildMainWindow());

		//create and format the quit button
		Button quit = new Button("Quit");
		quit.setPreferredSize(startWindowButtonSize);
		quit.setFont(startWindowButtonFont);
		quit.addActionListener(e -> System.exit(0));

		//create and format the title text
		JLabel title = new JLabel("Recipe Generator");
		title.setFont(new Font("Helvetica", Font.ITALIC, START_WINDOW_TITLE_TEXT_SIZE));
		title.setForeground(primaryTextCol);
		title.setHorizontalAlignment(SwingConstants.CENTER); // makes sure the title is always centered on the form

		startWindow = new JFrame("Recipe Generator");

		startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
		startWindow.setLayout(new GridBagLayout());
		startWindow.getContentPane().setBackground(bgCol);
		startWindow.setPreferredSize(startWindowMinimumSize); //makes sure the frame stays at a nice viewable size.

		//TODO: overhaul with Box Layout
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

		startWindow.pack(); //makes all frame contents at or above their preferred size
		startWindow.setLocationRelativeTo(null); //centers the window on the screen
		startWindow.setVisible(true); //so we can see the gui
	}

	/**
	 * The main window hosts majority of the functionality of the program.
	 * It includes 2 panels, the first of which containing 6 buttons:
	 * A load button which allows the user to select a preformed recipe file, which the parser will process and
	 * create a recipe object based off, if the file is in proper format.
	 * A load folder button which does the above, but for every file in the 'Recipes' folder.
	 * A create button, which allows the user to build a recipe file with information in a prebuilt form, which
	 * will both create a recipe, and also produce a formatted recipe file for future use.
	 * A generate button, which will open a window to allow the user to select their recipe generation options, and
	 * then the program will give a set of recipes and a shopping list, based on the user's specifications.
	 * A quit button which exits the program.
	 * <p>
	 * The second panel features a list of recipes, and will eventually allow the user to remove any recipes from the
	 * recipe book that they do not want to include. The user can select a recipe, to open in a full view using the open
	 * recipe bottom below.
	 */
	private void buildMainWindow() {
		mainWindow = new JFrame("Recipe Generator");
		startWindow.setVisible(false); //make the start window go away
		mainWindow.getContentPane().setBackground(bgCol);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(bgCol);

		Button loadFile = new Button("Load File");
		JFileChooser fileChooser = new JFileChooser();
		loadFile.setPreferredSize(mainWindowButtonSize);
		loadFile.setFont(mainWindowButtonFont);
		loadFile.setToolTipText("Load a recipe file.");

		Button loadFolder = new Button("Load Folder");
		loadFolder.setPreferredSize(mainWindowButtonSize);
		loadFolder.setFont(mainWindowButtonFont);
		loadFolder.setToolTipText("Load the recipes folder.");

		Button create = new Button("Create");
		create.setPreferredSize(mainWindowButtonSize);
		create.setFont(mainWindowButtonFont);
		create.setToolTipText("Create recipe file by filling in recipe form.");

		Button generate = new Button("Generate");
		generate.setPreferredSize(mainWindowButtonSize);
		generate.setFont(mainWindowButtonFont);
		generate.setToolTipText("Select some options, and generate recipes.");

		Button quit = new Button("Quit");
		quit.setPreferredSize(mainWindowButtonSize);
		quit.setFont(mainWindowButtonFont);
		quit.setToolTipText("Close program.");

		//TODO: overhaul with Box Layout
		buttonPanel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.ipadx = 30;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = buttonPanelInsets;
		buttonPanel.add(loadFile, constraints);

		constraints.gridy = 1;
		constraints.insets = buttonPanelInsets;
		buttonPanel.add(loadFolder, constraints);

		constraints.gridy = 2;
		constraints.insets = buttonPanelInsets;
		buttonPanel.add(create, constraints);

		constraints.gridy = 3;
		constraints.insets = buttonPanelInsets;
		buttonPanel.add(generate, constraints);

		constraints.gridy = 4;
		constraints.insets = buttonPanelQuitInsets;
		buttonPanel.add(quit, constraints);

		JPanel recipePanel = new JPanel();
		recipePanel.setLayout(new BorderLayout());

		recipes = new JList<>();
		recipes.setListData(getRecipes().namesToArray()); //add all of the name of recipes in recipeBook to the list
		recipes.setLayoutOrientation(JList.VERTICAL);
		recipes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		recipes.setFont(new Font("Helvetica", Font.PLAIN, RECIPE_LIST_TEXT_SIZE));

		JScrollPane recipeScroll = new JScrollPane(recipes);
		recipeScroll.createVerticalScrollBar();
		recipeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); //only show scroll when necessary
		recipeScroll.setPreferredSize(recipeListPreferredSize);

		Button openRecipeButton = new Button("Open Recipe");
		setFormComponentDetails(openRecipeButton);

		recipePanel.add(recipeScroll, BorderLayout.CENTER);
		recipePanel.add(openRecipeButton, BorderLayout.SOUTH);

		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the instance finish when GUI is closed
		mainWindow.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));
		mainWindow.add(buttonPanel);
		mainWindow.add(recipePanel);

		mainWindow.pack();
		mainWindow.setLocationRelativeTo(null); //centers the window on the screen
		mainWindow.setVisible(true); //so we can see the gui

		//ACTION LISTENERS
		loadFile.addActionListener(e -> {
			fileChooser.setCurrentDirectory(new File("./Recipes/"));
			fileChooser.setDialogTitle("Select recipe file.");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			if (fileChooser.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) { //if the user chose an
				//acceptable file
				File file = fileChooser.getSelectedFile();
				loadRecipe(file);
			}
		});

		loadFolder.addActionListener(e -> {
			File folder =  new File("./Recipes/");
			File[] listOfFiles = folder.listFiles();

			if (listOfFiles == null) { return; }
			for (File f : listOfFiles) {
				loadRecipe(f);
			}
		});

		//open the form to create recipe file
		create.addActionListener(e -> buildRecipeFormWindow());

		//open generation window
		generate.addActionListener(e -> buildGeneratedRecipesWindow());

		//close program
		quit.addActionListener(e -> System.exit(0));

		openRecipeButton.addActionListener(e -> {
			if (recipes.getSelectedIndex() != -1) {
				buildRecipeDisplayWindow(getRecipes().getRecipeAt(recipes.getSelectedIndex()));
			}
		});

	}

	/**
	 * Build window to display a recipe.
	 * @param r Recipe to display.
	 */
	private void buildRecipeDisplayWindow(Recipe r) {
		JFrame recipeWindow = new JFrame("Recipe View");

		JTextArea recipe = new JTextArea();
		recipe.setEditable(false);
		setFormComponentDetails(recipe);
		recipe.append(r.toString());
		recipe.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JScrollPane recipeScroll = new JScrollPane(recipe);
		recipeScroll.setMaximumSize(new Dimension(1000, 800));
		recipeScroll.createVerticalScrollBar();
		recipeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		recipeScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		recipeScroll.setBorder(BorderFactory.createLineBorder(pressedButCol, 10));

		JPanel formattingPanel = new JPanel();
		formattingPanel.setBackground(bgCol);
		formattingPanel.setLayout(new BoxLayout(formattingPanel, BoxLayout.X_AXIS));
		formattingPanel.add(Box.createRigidArea(new Dimension(20, 10)));
		formattingPanel.add(recipeScroll);
		formattingPanel.add(Box.createRigidArea(new Dimension(20, 10)));

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(bgCol);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		mainPanel.add(formattingPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		recipeWindow.add(mainPanel);
		recipeWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		recipeWindow.setMaximumSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.9), (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.9)));
		recipeWindow.pack();
		recipeWindow.setLocationRelativeTo(null);
		recipeWindow.setVisible(true);
	}

	/**
	 *  Builds the window where user can randomly get recipes from the registered ones based on specifications, and
	 *  perform actions with those recipes.
	 */
	private void buildGeneratedRecipesWindow() {
		JFrame generationWindow = new JFrame("Pick your generation options");

		JPanel generationOptions = new JPanel();
		generationOptions.setLayout(new BoxLayout(generationOptions, BoxLayout.X_AXIS));

		JPanel specificOptions = new JPanel();
		specificOptions.setLayout(new BoxLayout(specificOptions, BoxLayout.Y_AXIS));
		JSpinner numServesSpinner = new JSpinner();
		numServesSpinner.setMaximumSize(new Dimension(50, 20));
		numServesSpinner.setModel(new SpinnerNumberModel(0, 0, 10, 1));
		setFormComponentDetails(numServesSpinner);
		JPanel servesPadding = new JPanel();
		servesPadding.setLayout(new BoxLayout(servesPadding, BoxLayout.X_AXIS));
		servesPadding.add(Box.createHorizontalGlue());
		servesPadding.add(numServesSpinner);
		servesPadding.add(Box.createHorizontalGlue());

		JPanel timeOptions = new JPanel();
		timeOptions.setLayout(new BoxLayout(timeOptions, BoxLayout.X_AXIS));
		JSpinner maxHoursSpinner = new JSpinner();
		maxHoursSpinner.setToolTipText("What's the max number of hours?");
		maxHoursSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		setFormComponentDetails(maxHoursSpinner);
		JSpinner maxMinutesSpinner = new JSpinner();
		maxMinutesSpinner.setToolTipText("What's the max number of minutes?");
		maxMinutesSpinner.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		setFormComponentDetails(maxMinutesSpinner);
		timeOptions.add(Box.createHorizontalGlue());
		timeOptions.add(maxHoursSpinner);
		timeOptions.add(Box.createRigidArea(new Dimension(5, 10)));
		timeOptions.add(maxMinutesSpinner);
		timeOptions.add(Box.createHorizontalGlue());

		Button generateButton = new Button("Generate a Recipe!");
		setFormComponentDetails(generateButton);

		specificOptions.add(setFormLabelDetails(new JLabel("Number of Serves (0 for any)")));
		specificOptions.add(Box.createRigidArea(new Dimension(5,15)));
		specificOptions.add(servesPadding);
		specificOptions.add(Box.createRigidArea(new Dimension(5,15)));
		specificOptions.add(setFormLabelDetails(new JLabel("Max Recipe Time (0:0 for any)")));
		specificOptions.add(Box.createRigidArea(new Dimension(5,15)));
		specificOptions.add(timeOptions);
		specificOptions.add(Box.createRigidArea(new Dimension(5,15)));
		specificOptions.add(generateButton);

		JPanel tagOptions = new JPanel();
		tagOptions.setLayout(new BoxLayout(tagOptions, BoxLayout.Y_AXIS));
		JList<String> tagsList = new JList<>();
		tagsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		if (getAllTags().size() > 0) {
			HashSet<String> tags = getAllTags();
			String[] tagsListCollection = new String[tags.size() + 1];
			tagsListCollection[0] = "None";
			AtomicInteger i = new AtomicInteger(1);
			tags.forEach(s -> tagsListCollection[i.getAndIncrement()] = s);
			tagsList.setListData(tagsListCollection);
		}
		JScrollPane tagsScroll = new JScrollPane(tagsList);

		tagOptions.add(setFormLabelDetails(new JLabel("Tags")));
		tagOptions.add(Box.createRigidArea(new Dimension(5,5)));
		tagOptions.add(tagsScroll);

		generationOptions.add(Box.createRigidArea(new Dimension(20, 5)));
		generationOptions.add(specificOptions);
		generationOptions.add(Box.createRigidArea(new Dimension(10, 5)));
		generationOptions.add(Box.createHorizontalGlue());
		generationOptions.add(tagOptions);
		generationOptions.add(Box.createRigidArea(new Dimension(20, 5)));

		JPanel generations = new JPanel();
		generations.setLayout(new BoxLayout(generations, BoxLayout.Y_AXIS));

		JPanel listPadding = new JPanel();
		listPadding.setLayout(new BoxLayout(listPadding, BoxLayout.X_AXIS));
		JList<String> generatedRecipesList = new JList<>();
		generatedRecipesCollection = new String[5];
		generatedRecipesList.setListData(generatedRecipesCollection);
		JScrollPane generatedRecipesScroll = new JScrollPane(generatedRecipesList);
		generatedRecipesScroll.setPreferredSize(new Dimension(100, 200));
		listPadding.add(Box.createRigidArea(new Dimension(20, 5)));
		listPadding.add(generatedRecipesScroll);
		listPadding.add(Box.createRigidArea(new Dimension(20, 5)));

		JPanel generationFunctionsButtons = new JPanel();
		generationFunctionsButtons.setLayout(new BoxLayout(generationFunctionsButtons, BoxLayout.X_AXIS));
		Button viewButton = new Button("View Recipe");
		setFormComponentDetails(viewButton);
		Button removeButton = new Button("Remove Recipe");
		setFormComponentDetails(removeButton);
		Button shoppingListButton = new Button("Shopping List");
		setFormComponentDetails(shoppingListButton);

		generationFunctionsButtons.add(Box.createRigidArea(new Dimension(20,5)));
		generationFunctionsButtons.add(viewButton);
		generationFunctionsButtons.add(Box.createHorizontalGlue());
		generationFunctionsButtons.add(removeButton);
		generationFunctionsButtons.add(Box.createHorizontalGlue());
		generationFunctionsButtons.add(shoppingListButton);
		generationFunctionsButtons.add(Box.createRigidArea(new Dimension(20,5)));

		JLabel recipesLabel = setFormLabelDetails(new JLabel("Recipes"));
		recipesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		generations.add(recipesLabel);
		generations.add(Box.createRigidArea(new Dimension(5,5)));
		generations.add(listPadding);
		generations.add(Box.createRigidArea(new Dimension(5,5)));
		generations.add(generationFunctionsButtons);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(Box.createRigidArea(new Dimension(5,15)));
		mainPanel.add(generationOptions);
		mainPanel.add(Box.createRigidArea(new Dimension(10,15)));
		mainPanel.add(generations);
		mainPanel.add(Box.createRigidArea(new Dimension(5,15)));

		generationWindow.add(mainPanel);
		generationWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		generationWindow.pack();
		generationWindow.setVisible(true);

		//Listeners
		tagsList.addListSelectionListener(e -> {
			if (tagsList.getSelectedIndex() == 0) { tagsList.clearSelection(); }
		});

		generateButton.addActionListener( e -> {
			Duration max = Duration.ofHours((int) maxHoursSpinner.getValue()).plus(Duration.ofMinutes((int) maxMinutesSpinner.getValue()));
			int numServes = (int)numServesSpinner.getValue();
			List<String> tags = tagsList.getSelectedValuesList();
			String r = getRecipes().getRecipeWhere(numServes, max, tags);
			if (r == null) {
				JOptionPane noRecipe = new JOptionPane();
				noRecipe.setOptionType(JOptionPane.DEFAULT_OPTION);
				noRecipe.setMessage("No recipe exists with those specifications..!");
				JDialog noRecipeDialog = noRecipe.createDialog("No Recipe Found!");
				noRecipeDialog.pack();
				noRecipeDialog.setVisible(true);
				int dialogChoice = (Integer) noRecipe.getValue();
				if (dialogChoice == JOptionPane.OK_OPTION) {
					noRecipeDialog.setVisible(false);
				}
			}
			 else {
				if (generatedRecipesCollection[generatedRecipesCollection.length - 1] != null) {
					//make list bigger
					String[] temp = new String[generatedRecipesCollection.length + 5];
					for (int i = 0; i < generatedRecipesCollection.length; i++) {
						temp[i] = generatedRecipesCollection[i];
					}
					generatedRecipesCollection = temp;
				}
				for (int i = 0; i < generatedRecipesCollection.length; i++) {
					if (generatedRecipesCollection[i] != null) {
						continue;
					}
					generatedRecipesCollection[i] = r;
					generatedRecipesList.setListData(generatedRecipesCollection);
					break;
				}
			}
		});

		viewButton.addActionListener(e -> {
			if (generatedRecipesList.getSelectedIndex() != -1) { buildRecipeDisplayWindow(getRecipes().getRecipeByName(generatedRecipesList.getSelectedValue())); }
		});

		removeButton.addActionListener(e -> {
			if (generatedRecipesList.getSelectedIndex() != -1) {
				for (int i = generatedRecipesList.getSelectedIndex(); i < generatedRecipesCollection.length - 1; i++) {
					generatedRecipesCollection[i] = generatedRecipesCollection[i+1];
				}
				generatedRecipesCollection[generatedRecipesCollection.length - 1] = null;
				generatedRecipesList.setListData(generatedRecipesCollection);
			}
		});
		shoppingListButton.addActionListener(e -> buildShoppingListWindow(Arrays.stream(generatedRecipesCollection).filter(Objects::nonNull).map(s -> GUI.this.getRecipes().getRecipeByName(s)).collect(Collectors.toList())));
	}


	/**
	 * Build window to display the shopping list.
	 */
	private void buildShoppingListWindow(List<Recipe> recipes) {
		HashMap<String, List<Measurement>> ingredients = new HashMap<>();

		for (Recipe r : recipes) {
			for (Ingredient i : r.getIngredients()) {
				if (ingredients.containsKey(i.getIngredient().toLowerCase())) {
					List<Measurement> measurements = ingredients.get(i.getIngredient().toLowerCase());
					boolean likeMeasurementFound = false;
					for (Measurement m : measurements) {
						if (m.getUnit() == i.getMeasurement().getUnit()) {
							m.addAmount(i.getMeasurement().getAmount());
							likeMeasurementFound = true;
							break;
						}
					}
					if (!likeMeasurementFound) {
						measurements.add(new Measurement(i.getMeasurement().getAmount(), i.getMeasurement().getUnit()));
					}
					ingredients.put(i.getIngredient().toLowerCase(), measurements);
				}
				else {
					List<Measurement> ms = new ArrayList<>();
					ms.add(new Measurement(i.getMeasurement().getAmount(), i.getMeasurement().getUnit()));
					ingredients.put(i.getIngredient().toLowerCase(), ms);
				}
			}
		}

		JFrame shoppingListWindow = new JFrame("Recipe View");

		JTextArea shoppingList = new JTextArea();
		shoppingList.setEditable(false);
		setFormComponentDetails(shoppingList);
		shoppingList.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		StringBuilder b = new StringBuilder();
		b.append("Ingredients needed for recipes: \n");
		for (String s : ingredients.keySet()) {
			b.append(s).append(" (");
			for (Measurement m : ingredients.get(s)) {
				if (b.charAt(b.length()-1) != '(') { b.append(", "); }
				b.append(m.toString());
			}
			b.append(")\n");
		}
		shoppingList.append(b.toString());

		JScrollPane shoppingListScroll = new JScrollPane(shoppingList);
		shoppingListScroll.setMaximumSize(new Dimension(1000, 800));
		shoppingListScroll.createVerticalScrollBar();
		shoppingListScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		shoppingListScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		shoppingListScroll.setBorder(BorderFactory.createLineBorder(pressedButCol, 10));

		JPanel formattingPanel = new JPanel();
		formattingPanel.setBackground(bgCol);
		formattingPanel.setLayout(new BoxLayout(formattingPanel, BoxLayout.X_AXIS));
		formattingPanel.add(Box.createRigidArea(new Dimension(20, 10)));
		formattingPanel.add(shoppingListScroll);
		formattingPanel.add(Box.createRigidArea(new Dimension(20, 10)));

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(bgCol);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		mainPanel.add(formattingPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		shoppingListWindow.add(mainPanel);
		shoppingListWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		shoppingListWindow.setMaximumSize(new Dimension((int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.9), (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.9)));
		shoppingListWindow.pack();
		shoppingListWindow.setLocationRelativeTo(null);
		shoppingListWindow.setVisible(true);
	}


	/**
	 * Builds the window where a user can build a recipe file.
	 */
	private void buildRecipeFormWindow() {
		JPanel name = new JPanel();
		name.setLayout(new BoxLayout(name, BoxLayout.X_AXIS));
		JTextField nameInput = new JTextField("");
		nameInput.setToolTipText("Enter the title of the recipe");
		setFormComponentDetails(nameInput);
		nameInput.setColumns(15);
		nameInput.setMaximumSize(new Dimension(60, 30));
		name.add(Box.createHorizontalGlue());
		name.add(setFormLabelDetails(new JLabel("Recipe Title")));
		name.add(Box.createRigidArea(new Dimension(15, 0)));
		name.add(nameInput);
		name.add(Box.createHorizontalGlue());

		JPanel recipeStats = new JPanel();
		JSlider servesInput = new JSlider(1, 10, 4);
		setFormComponentDetails(servesInput);
		servesInput.setToolTipText("Slide to indicate how many servings the recipe makes");
		servesInput.setMinorTickSpacing(1);
		servesInput.setMajorTickSpacing(2);
		servesInput.setPaintLabels(true);
		servesInput.setPaintTicks(true);
		servesInput.setSnapToTicks(true);

		JSpinner prepTimeHourInput = new JSpinner();
		prepTimeHourInput.setToolTipText("Showing the hours, indicate how long the recipe takes to prep");
		prepTimeHourInput.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		setFormComponentDetails(prepTimeHourInput);
		JSpinner prepTimeMinuteInput = new JSpinner();
		prepTimeMinuteInput.setToolTipText("Showing the minutes, indicate how long the recipe takes to prep");
		prepTimeMinuteInput.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		setFormComponentDetails(prepTimeMinuteInput);

		JSpinner cookTimeHourInput = new JSpinner();
		cookTimeHourInput.setToolTipText("Showing the hours, indicate how long the recipe takes to cook");
		cookTimeHourInput.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		setFormComponentDetails(cookTimeHourInput);
		JSpinner cookTimeMinuteInput = new JSpinner();
		cookTimeMinuteInput.setToolTipText("Showing the minutes, indicate how long the recipe takes to cook");
		cookTimeMinuteInput.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		setFormComponentDetails(cookTimeMinuteInput);

		recipeStats.add(Box.createHorizontalGlue());
		recipeStats.add(Box.createRigidArea(new Dimension(30, 0)));
		recipeStats.add(setFormLabelDetails(new JLabel("Serves")));
		recipeStats.add(Box.createRigidArea(new Dimension(15, 0)));
		recipeStats.add(servesInput);
		recipeStats.add(Box.createRigidArea(new Dimension(30, 0)));
		recipeStats.add(setFormLabelDetails(new JLabel("Prep Time")));
		recipeStats.add(Box.createRigidArea(new Dimension(15, 0)));
		recipeStats.add(prepTimeHourInput);
		recipeStats.add(prepTimeMinuteInput);
		recipeStats.add(Box.createRigidArea(new Dimension(20, 0)));
		recipeStats.add(setFormLabelDetails(new JLabel("Cook Time")));
		recipeStats.add(Box.createRigidArea(new Dimension(15, 0)));
		recipeStats.add(cookTimeHourInput);
		recipeStats.add(cookTimeMinuteInput);
		recipeStats.add(Box.createRigidArea(new Dimension(30, 0)));
		recipeStats.add(Box.createHorizontalGlue());

		JPanel ingredients = new JPanel();
		ingredients.setLayout(new BoxLayout(ingredients, BoxLayout.Y_AXIS));
		JPanel ingredientsInputRow = new JPanel();
		JTextField amountInput = new JTextField("");
		setFormComponentDetails(amountInput);
		amountInput.setToolTipText("Enter how many units are required");
		amountInput.setColumns(4);
		amountInput.setMaximumSize(new Dimension(20, 40));
		JComboBox<String> measurementInput = new JComboBox<>();
		setFormComponentDetails(measurementInput);
		measurementInput.setToolTipText("Choose the unit of measurement");
		measurementInput.setMaximumSize(new Dimension(40, 40));
		measurementInput.addItem(null);
		for (Measurement.UnitsOfMeasurement e : Measurement.UnitsOfMeasurement.values()) { measurementInput.addItem(Measurement.convertToString(e)); }
		JTextField ingredientInput = new JTextField("");
		setFormComponentDetails(ingredientInput);
		ingredientInput.setToolTipText("Name the ingredient");
		ingredientInput.setColumns(10);
		ingredientInput.setMaximumSize(new Dimension(150, 40));
		Button addIngredientButton = new Button("Add!");
		Button deleteIngredientButton = new Button("Delete");
		ingredientsInputRow.setLayout(new BoxLayout(ingredientsInputRow, BoxLayout.X_AXIS));
		ingredientsInputRow.add(Box.createHorizontalGlue());
		ingredientsInputRow.add(Box.createRigidArea(new Dimension(20, 0)));
		ingredientsInputRow.add(setFormComponentDetails(measurementInput));
		ingredientsInputRow.add(Box.createRigidArea(new Dimension(30, 0)));
		ingredientsInputRow.add(setFormComponentDetails(amountInput));
		ingredientsInputRow.add(Box.createRigidArea(new Dimension(20, 0)));
		ingredientsInputRow.add(setFormComponentDetails(ingredientInput));
		ingredientsInputRow.add(Box.createRigidArea(new Dimension(20, 0)));
		ingredientsInputRow.add(setFormComponentDetails(addIngredientButton));
		ingredientsInputRow.add(Box.createRigidArea(new Dimension(20, 0)));
		ingredientsInputRow.add(setFormComponentDetails(deleteIngredientButton));
		ingredientsInputRow.add(Box.createRigidArea(new Dimension(30, 0)));
		ingredientsInputRow.add(Box.createHorizontalGlue());
		JPanel scrollIncludeIngredients = new JPanel();
		scrollIncludeIngredients.setLayout(new BoxLayout(scrollIncludeIngredients, BoxLayout.X_AXIS));
		ingredientsCollection = new Ingredient[10];
		JList<Ingredient> ingredientsJList = new JList<>(ingredientsCollection);
		setFormComponentDetails(ingredientsJList);
		JScrollPane ingredientsScroll = new JScrollPane(ingredientsJList);
		setFormComponentDetails(ingredientsScroll);
		ingredientsScroll.createVerticalScrollBar();
		ingredientsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ingredientsScroll.setPreferredSize(new Dimension(500, 80));
		scrollIncludeIngredients.add(Box.createHorizontalGlue());
		scrollIncludeIngredients.add(ingredientsScroll);
		scrollIncludeIngredients.add(Box.createHorizontalGlue());
		JLabel ingredientsTitle = setFormLabelDetails(new JLabel("Ingredients"));
		ingredientsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		ingredientsTitle.setToolTipText("Fill out the boxes below and then click 'Add' for each ingredient, \nwhich will appear in the large box below");
		ingredients.add(new JPanel().add(ingredientsTitle));
		ingredients.add(Box.createRigidArea(new Dimension(15, 10)));
		ingredients.add(ingredientsInputRow);
		ingredients.add(Box.createRigidArea(new Dimension(15, 10)));
		ingredients.add(scrollIncludeIngredients);

		JPanel method = new JPanel();
		method.setLayout(new BoxLayout(method, BoxLayout.Y_AXIS));

		JTextField stepInput = new JTextField();
		setFormComponentDetails(stepInput);
		stepInput.setToolTipText("Fill out the instructions for a step, not including the step number \n(that will be included for you)");
		stepInput.setColumns(35);
		Button addStepButton = new Button("Add!");
		setFormComponentDetails(addStepButton);
		Button deleteStepButton = new Button("Delete");
		setFormComponentDetails(deleteStepButton);
		JPanel stepInputRow = new JPanel();
		stepInputRow.add(stepInput);
		stepInputRow.add(addStepButton);
		stepInputRow.add(Box.createRigidArea(new Dimension(20, 0)));
		stepInputRow.add(deleteStepButton);

		JPanel scrollIncludeMethod = new JPanel();
		scrollIncludeMethod.setLayout(new BoxLayout(scrollIncludeMethod, BoxLayout.X_AXIS));

		stepsCollection = new Step[10];
		JList<Step> stepsJList = new JList<>(stepsCollection);
		setFormComponentDetails(stepsJList);
		JScrollPane methodScroll = new JScrollPane(stepsJList);
		setFormComponentDetails(methodScroll);
		methodScroll.createVerticalScrollBar();
		methodScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		methodScroll.setPreferredSize(new Dimension(500, 80));
		scrollIncludeMethod.add(Box.createHorizontalGlue());
		scrollIncludeMethod.add(methodScroll);
		scrollIncludeMethod.add(Box.createHorizontalGlue());
		JLabel methodTitle = setFormLabelDetails(new JLabel("Method"));
		methodTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		methodTitle.setToolTipText("Fill out the box below for each recipe step, in order from first to last, \nclicking 'Add' for each.");
		method.add(new JPanel().add(methodTitle));
		method.add(Box.createRigidArea(new Dimension(15, 10)));
		method.add(stepInputRow);
		method.add(Box.createRigidArea(new Dimension(15, 10)));
		method.add(scrollIncludeMethod);

		JPanel tags = new JPanel();

		JPanel tagsCombo = new JPanel();
		tagsCombo.setLayout(new BoxLayout(tagsCombo, BoxLayout.Y_AXIS));
		JComboBox<String> tagsComboBox = new JComboBox<>();
		tagsComboBox.setPreferredSize(new Dimension(150, 30));
		tagsComboBox.setMaximumSize(new Dimension(150, 30));
		tagsComboBox.setEditable(true);
		setFormComponentDetails(tagsComboBox);
		JTextField tagEditor = (JTextField) tagsComboBox.getEditor().getEditorComponent();

		tagsCombo.add(setFormLabelDetails(new JLabel("Recipe Tags")));
		tagsCombo.add(Box.createRigidArea(new Dimension(5, 10)));
		tagsCombo.add(tagsComboBox);
		tagsCombo.add(Box.createVerticalGlue());

		Button tagButton = new Button("Add!");
		setFormComponentDetails(tagButton);

		JList<String> tagsJList = new JList<>();
		tagsCollection = new String[5];
		tagsJList.setListData(tagsCollection);
		setFormComponentDetails(tagsJList);
		JScrollPane tagsScroll = new JScrollPane(tagsJList);
		tagsScroll.setPreferredSize(new Dimension(30, 60));
		Button deleteTagButton = new Button("Delete");
		setFormComponentDetails(deleteTagButton);
		JPanel bottomTagsPanel = new JPanel();
		bottomTagsPanel.setLayout(new BoxLayout(bottomTagsPanel, BoxLayout.X_AXIS));
		bottomTagsPanel.add(tagButton);
		bottomTagsPanel.add(Box.createHorizontalGlue());
		bottomTagsPanel.add(deleteTagButton);
		JPanel rightTagsPanel = new JPanel();
		rightTagsPanel.setLayout(new BoxLayout(rightTagsPanel, BoxLayout.Y_AXIS));
		rightTagsPanel.add(Box.createRigidArea(new Dimension(20,10)));
		rightTagsPanel.add(tagsScroll);
		rightTagsPanel.add(Box.createRigidArea(new Dimension(20,10)));
		rightTagsPanel.add(bottomTagsPanel);

		tags.setLayout(new BoxLayout(tags, BoxLayout.X_AXIS));
		tags.add(Box.createHorizontalGlue());
		tags.add(tagsCombo);
		tags.add(Box.createRigidArea(new Dimension(20,10)));
		tags.add(rightTagsPanel);
		tags.add(Box.createRigidArea(new Dimension(20,10)));
		tags.add(Box.createHorizontalGlue());

		JPanel finalButtons = new JPanel();
		finalButtons.setLayout(new BoxLayout(finalButtons, BoxLayout.X_AXIS));
		Button cancelButton = new Button("Cancel");
		setFormComponentDetails(cancelButton);
		cancelButton.setMaximumSize(recipeFormButtonSize);
		cancelButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

		Button doneButton = new Button("Done");
		setFormComponentDetails(doneButton);
		doneButton.setMaximumSize(recipeFormButtonSize);
		doneButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
		finalButtons.add(Box.createHorizontalGlue());
		finalButtons.add(cancelButton);
		finalButtons.add(Box.createRigidArea(new Dimension(15, 10)));
		finalButtons.add(doneButton);
		finalButtons.add(Box.createRigidArea(new Dimension(20, 10)));

		recipeFormWindow = new JFrame("Enter your recipe");
		recipeFormWindow.getContentPane().setBackground(bgCol);
		JPanel mainPanel = new JPanel();
		BoxLayout layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(layout);

		mainPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		mainPanel.add(name);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		mainPanel.add(recipeStats);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 10)));
		mainPanel.add(ingredients);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		mainPanel.add(method);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		mainPanel.add(tags);
		mainPanel.add(Box.createRigidArea(new Dimension(15, 20)));
		mainPanel.add(finalButtons);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));

		recipeFormWindow.add(mainPanel);
		recipeFormWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //ideally will have a cancel button that we prefer user to use
		recipeFormWindow.pack();
		recipeFormWindow.setLocationRelativeTo(null);
		recipeFormWindow.setVisible(true);

		JOptionPane openInfo = new JOptionPane();
		openInfo.setOptionType(JOptionPane.DEFAULT_OPTION);
		openInfo.setMessage("Enter your recipe information..! \nHover over any element to see more information about it!");
		JDialog openInfoDialog = openInfo.createDialog("Helpful Information");
		openInfoDialog.pack();
		openInfoDialog.setVisible(true);
		int dialogChoice = (Integer) openInfo.getValue();
		if (dialogChoice == JOptionPane.OK_OPTION) {
			openInfoDialog.setVisible(false);
		}

		//Listeners

		addIngredientButton.addActionListener(e -> {
			if (!ingredientInput.getText().equals("") && !amountInput.getText().equals("") && amountInput.getText().matches("\\d+(\\.\\d+)*") && measurementInput.getSelectedItem() != null) {
				if (ingredientsCollection[ingredientsCollection.length - 1] != null) {
					//make list bigger
					Ingredient[] temp = new Ingredient[ingredientsCollection.length + 5];
					for (int i = 0; i < ingredientsCollection.length; i++) {
						temp[i] = ingredientsCollection[i];
					}
					ingredientsCollection = temp;
				}
				for (int i = 0; i < ingredientsCollection.length; i++) {
					if (ingredientsCollection[i] != null) {
						continue;
					}
					ingredientsCollection[i] = new Ingredient(Double.parseDouble(amountInput.getText()), measurementInput.getSelectedItem().toString(), ingredientInput.getText());
					amountInput.setText("");
					ingredientInput.setText("");
					measurementInput.setSelectedIndex(0);
					ingredientsJList.setListData(ingredientsCollection);
					break;
				}
			} else if (!amountInput.getText().matches("\\d+(\\.\\d+)*")) {
				JOptionPane option = new JOptionPane();
				option.setOptionType(JOptionPane.DEFAULT_OPTION);
				option.setMessage("The measurement amount may only be numeric. \nPlease use decimals for fractions, e.g 0.25 instead of 1/4");
				JDialog dialog = option.createDialog("Incorrect Input");
				dialog.pack();
				dialog.setVisible(true);
				int choice = (Integer) option.getValue();
				if (choice == JOptionPane.OK_OPTION) {
					dialog.setVisible(false);
				}
			} else {
				JOptionPane option = new JOptionPane();
				option.setOptionType(JOptionPane.DEFAULT_OPTION);
				option.setMessage("You haven't filled out all of the boxes! Please check and try again");
				JDialog dialog = option.createDialog("Missing Information");
				dialog.pack();
				dialog.setVisible(true);
				int choice = (Integer) option.getValue();
				if (choice == JOptionPane.OK_OPTION) {
					dialog.setVisible(false);
				}
			}
		});

		deleteIngredientButton.addActionListener(e -> {
			if (ingredientsJList.getSelectedIndex() != -1) {
				for (int i = ingredientsJList.getSelectedIndex(); i < ingredientsCollection.length - 1; i++) {
					ingredientsCollection[i] = ingredientsCollection[i+1];
				}
				ingredientsCollection[ingredientsCollection.length - 1] = null;
				ingredientsJList.setListData(ingredientsCollection);
			}
		});

		addStepButton.addActionListener(e -> {
			if (!stepInput.getText().equals("")) {
				if (!Character.isDigit(stepInput.getText().charAt(0))) {
					if (stepsCollection[stepsCollection.length - 1] != null) {
						//make list bigger
						Step[] temp = new Step[stepsCollection.length + 5];
						for (int i = 0; i < stepsCollection.length; i++) {
							temp[i] = stepsCollection[i];
						}
						stepsCollection = temp;
					}
					for (int i = 0; i < stepsCollection.length; i++) {
						if (stepsCollection[i] != null) {
							continue;
						}
						stepsCollection[i] = new Step(stepInput.getText(), i + 1);
						stepInput.setText("");
						stepsJList.setListData(stepsCollection);
						break;
					}
				} else {
					JOptionPane option = new JOptionPane();
					option.setOptionType(JOptionPane.DEFAULT_OPTION);
					option.setMessage("The first character of your method step cannot be numeric!");
					JDialog dialog = option.createDialog("Incorrect Format");
					dialog.pack();
					dialog.setVisible(true);
					int choice = (Integer) option.getValue();
					if (choice == JOptionPane.OK_OPTION) {
						dialog.setVisible(false);
					}
				}
			} else {
				JOptionPane option = new JOptionPane();
				option.setOptionType(JOptionPane.DEFAULT_OPTION);
				option.setMessage("You must have content to add.");
				JDialog dialog = option.createDialog("Incorrect Format");
				dialog.pack();
				dialog.setVisible(true);
				int choice = (Integer) option.getValue();
				if (choice == JOptionPane.OK_OPTION) {
					dialog.setVisible(false);
				}
			}
		});

		deleteStepButton.addActionListener(e -> {
			if (stepsJList.getSelectedIndex() != -1) {
				for (int i = stepsJList.getSelectedIndex(); i < stepsCollection.length - 1; i++) {
					stepsCollection[i] = stepsCollection[i+1];
					stepsCollection[i].setStepNumber(i + 1);
				}
				stepsCollection[stepsCollection.length - 1] = null;
				stepsJList.setListData(stepsCollection);
			}
		});

		tagEditor.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override public void keyPressed(KeyEvent e) { String text = tagEditor.getText();
			if (!text.equals("")) {
				DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
				HashSet<String> suggestions = getSuggestedTags(text);
				m.addElement(text);
				if (suggestions != null) { m.addAll(suggestions); }
				tagsComboBox.setModel(m);
				tagsComboBox.showPopup();}}
			@Override public void keyReleased(KeyEvent e) { }
		});

		tagButton.addActionListener(e -> {
			if (tagsCollection[tagsCollection.length - 1] != null) {
				//make list bigger
				String[] temp = new String[tagsCollection.length + 5];
				for (int i = 0; i < tagsCollection.length; i++) {
					temp[i] = tagsCollection[i];
				}
				tagsCollection = temp;
			}
			for (int i = 0; i < tagsCollection.length; i++) {
				if (tagsCollection[i] != null) {
					continue;
				}
				tagsCollection[i] = String.valueOf(tagsComboBox.getSelectedItem());
				DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
				tagsComboBox.setModel(m);
				tagsJList.setListData(tagsCollection);
				break;
			}
		});

		deleteTagButton.addActionListener(e -> {
			if (tagsJList.getSelectedIndex() != -1) {
				for (int i = tagsJList.getSelectedIndex(); i < tagsCollection.length - 1; i++) {
					tagsCollection[i] = tagsCollection[i+1];
				}
				tagsCollection[tagsCollection.length - 1] = null;
				tagsJList.setListData(tagsCollection);
			}
		});

		cancelButton.addActionListener(e -> {
			JOptionPane option = new JOptionPane();
			option.setOptionType(JOptionPane.OK_CANCEL_OPTION);
			option.setMessage("Are you sure you want to cancel? \n You will lose all progress.");
			JDialog dialog = option.createDialog("Are you sure?");
			dialog.pack();
			dialog.setVisible(true);
			int choice = (Integer) option.getValue();
			if (choice == JOptionPane.OK_OPTION) {
				recipeFormWindow.setVisible(false);
			} else if (choice == JOptionPane.CANCEL_OPTION) {
				dialog.setVisible(false);
			}

		});

		doneButton.addActionListener(e -> {
			if (((int) prepTimeHourInput.getValue() > 0 || (int) prepTimeMinuteInput.getValue() > 0) && ((int) cookTimeHourInput.getValue() > 0 || (int) cookTimeMinuteInput.getValue() > 0)
					&& ingredientsCollection[0] != null && stepsCollection[0] != null && !nameInput.getText().equals("")) {

				Recipe r = new Recipe(new ArrayList<>(Arrays.stream(ingredientsCollection).filter(Objects::nonNull).collect(Collectors.toList())),
						new Method(new ArrayList<>(Arrays.stream(stepsCollection).filter(Objects::nonNull).collect(Collectors.toList()))),
						nameInput.getText(),
						new InfoBlock(Duration.ofHours((int) prepTimeHourInput.getValue()).plus(Duration.ofMinutes((int) prepTimeMinuteInput.getValue())),
								Duration.ofHours((int) cookTimeHourInput.getValue()).plus(Duration.ofMinutes((int) cookTimeMinuteInput.getValue())),
								servesInput.getValue()),
						new ArrayList<>(Arrays.stream(tagsCollection).filter(Objects::nonNull).collect(Collectors.toList())));
				saveRecipe(r);
				JOptionPane option = new JOptionPane();
				option.setOptionType(JOptionPane.DEFAULT_OPTION);
				option.setMessage("Recipe has successfully been created! It will be saved for you.");
				JDialog dialog = option.createDialog("Success");
				dialog.pack();
				dialog.setVisible(true);
				int choice = (Integer) option.getValue();
				if (choice == JOptionPane.OK_OPTION) {

					File file = new File("./Recipes/" + r.getName().replace(" ", "_") + ".txt");
					try {
						new RecipeParser().parseRecipeToFile(r, file);
					} catch (FileNotFoundException fileNotFoundException) {
						System.out.println("first error");
						//TODO: add dialog to say communicate error
					} catch (Throwable t) {
						t.printStackTrace();
						//TODO: put dialog
					}
					refreshRecipeList();
					dialog.setVisible(false);
					recipeFormWindow.setVisible(false);
				}
			} else {
				JOptionPane option = new JOptionPane();
				option.setOptionType(JOptionPane.DEFAULT_OPTION);
				option.setMessage("A necessary field seems to be empty or invalid");
				JDialog dialog = option.createDialog("Missing Information!");
				dialog.pack();
				dialog.setVisible(true);
				int choice = (Integer) option.getValue();
				if (choice == JOptionPane.OK_OPTION) {
					dialog.setVisible(false);
				}
			}
		});
	}

	/**
	 * Add styling for form components.
	 * @param component     Component to style
	 * @return  Component passed in
	 */
	private JComponent setFormComponentDetails(JComponent component) {
		component.setFont(recipeFormWindowComponentFont);
		component.setForeground(primaryTextCol);
		return component;
	}

	/**
	 * Add styling for form labels.
	 * @param label     Label to style
	 * @return  Label passed in
	 */
	private JLabel setFormLabelDetails(JLabel label) {
		label.setFont(recipeFormWindowLabelFont);
		label.setForeground(primaryTextCol);
		return label;
	}

	/**
	 * Re-add the list data to recipes to refresh it
	 */
	private void refreshRecipeList() {
		recipes.setListData(getRecipes().namesToArray());
	}

	/**
	 * Helper Method to parse file and handle results.
	 * @param f File to parse from
	 */
	private void loadRecipe(File f) {
		try {
			Recipe r = new RecipeParser().parseRecipeFromFile(f);
			if (getRecipes().checkForDuplicate(r)) { return; }
			saveRecipe(r);
			loadTags(r.getTags());
			refreshRecipeList();
			recipes.setSelectedIndex(getRecipes().namesToArray().length - 1);
		} catch (FileNotFoundException fileNotFoundException) {
			JOptionPane fileNotFound = new JOptionPane();
			fileNotFound.setOptionType(JOptionPane.DEFAULT_OPTION);
			fileNotFound.setMessage("The file selected was not found. Please try again!");
			JDialog fileNotFoundDialog = fileNotFound.createDialog("File Not Found");
			fileNotFoundDialog.pack();
			fileNotFoundDialog.setVisible(true);
			int dialogChoice = (Integer) fileNotFound.getValue();
			if (dialogChoice == JOptionPane.OK_OPTION) {
				fileNotFoundDialog.setVisible(false);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			JOptionPane parseError = new JOptionPane();
			parseError.setOptionType(JOptionPane.DEFAULT_OPTION);
			parseError.setMessage("There was an error while processing your file..! \n" + t.getMessage());
			JDialog parseErrorDialog = parseError.createDialog("Recipe Parse Error");
			parseErrorDialog.pack();
			parseErrorDialog.setVisible(true);
			int dialogChoice = (Integer) parseError.getValue();
			if (dialogChoice == JOptionPane.OK_OPTION) {
				parseErrorDialog.setVisible(false);
			}
		}
	}

	/**
	 * Button innerclass allows more control over the appearance and actions of the buttons in the GUI,
	 * but as it extends JButton, it is still recognizable as a swing component.
	 */
	private class Button extends JButton {

		public Button(String text) {
			super(text);
			setContentAreaFilled(false); //stops the colour turning grey when pressed
			setOpaque(true); //ensures every pixel is painted

			setForeground(primaryTextCol);
			setBackground(primaryButCol);
			setFocusPainted(false); //turns off grey border around text on press
			setText(text);

			addChangeListener(e -> {
				if (getModel().isPressed()) {
					//adjust to pressed colouration
					setForeground(hoverTextCol);
					setBackground(pressedButCol);
				} else if (getModel().isRollover()) {
					//adjust to hover colouration
					setForeground(hoverTextCol);
					setBackground(hoverButCol);
				} else {
					//adjust to regular colouration
					setForeground(primaryTextCol);
					setBackground(primaryButCol);
				}
			});
		}
	}
}

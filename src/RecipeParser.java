import recipeInfo.Recipe;
import recipeInfo.recipeContents.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * In and out parser for recipe files.
 * Reads files in and creates Recipe objects from valid ones.
 * Utilizes the toFileFormat() method in Recipe to parse out to a file.
 */
public class RecipeParser {

	/**
	 * Given a file, parse the content and return a new Recipe object
	 * @param recipeFile    File to parse
	 * @return  a Recipe object of file
	 * @throws FileNotFoundException    If file is invalid
	 */
	public Recipe parseRecipeFromFile(File recipeFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(recipeFile);
		scanner.useDelimiter("\\s+");

		String title = parseLetterSeqIn(scanner);

		InfoBlock info = parseInfoBlock(scanner);

		ArrayList<Ingredient> ingredients = parseIngredientBlock(scanner);

		Method method = parseMethod(scanner);

		ArrayList<String> tags = parseTags(scanner);

		return new Recipe(ingredients, method, title, info, tags);
	}

	/**
	 * Parse tags from scanner.
	 * @param scanner   Scanner to read from
	 * @return  List of tags
	 */
	private ArrayList<String> parseTags(Scanner scanner) {
		assertExpected(scanner,  "<tagOpen>");
		ArrayList<String> tags = new ArrayList<>();
		while (true) {
			String next = scanner.next();
			if (next.equals("<tagClose>")) {
				break;
			} else if (next.equals("(")) {
				StringBuilder tag = new StringBuilder();
				while (true) {
					String token = scanner.next();
					if (token.equals(")")) {
						break;
					}
					if (!tag.toString().equals("")) {
						tag.append(" ");
					}
					tag.append(token);
				}
				tags.add(tag.toString().toUpperCase());
			} else {
				throw new IllegalArgumentException("Incorrect Format");
			}
		}
		return tags;
	}


	/**
	 * Parse the method from scanner.
	 * @param scanner   Scanner to read from
	 * @return  a Method object containing parsed steps
	 */
	private Method parseMethod(Scanner scanner) {
		assertExpected(scanner, "<start>");
		Method m = new Method(new ArrayList<>());
		while (true) {
			String next = scanner.next();
			if (next.equals("<stop>")) {
				break;
			} else if (next.equals("#")) {
				m.addStep(parseStep(scanner));
			} else {
				throw new IllegalArgumentException("Incorrect Format");
			}
		}
		return m;
	}

	/**
	 * Parses a step from scanner.
	 * @param scanner   Scanner to read from
	 * @return  A step object with parsed info
	 */
	private Step parseStep(Scanner scanner) {
		int stepNum = scanner.nextInt();
		StringBuilder step = new StringBuilder();
		while (true) {
			String token = scanner.next();
			if (token.equals("#")) { break; }
			if (!step.toString().equals("")) { step.append(" "); }
			step.append(token);
		}

		return new Step(step.toString(), stepNum);
	}

	/**
	 * Parses a block of ingredients from the scanner.
	 * @param scanner   Scanner to read from
	 * @return  List of ingredients
	 */
	private ArrayList<Ingredient> parseIngredientBlock(Scanner scanner) {
		assertExpected(scanner, "<start>");
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		while (true) {
			String next = scanner.next();
			if (next.equals("<stop>")) {
				break;
			} else if (next.equals("(")) {
				ingredients.add(parseIngredient(scanner));
			} else {
				throw new IllegalArgumentException("Incorrect Format");
			}
		}
		return ingredients;
	}

	/**
	 * Parse ingredient from scanner.
	 * @param scanner   Scanner to read from
	 * @return  a new Ingredient object with info
	 */
	private Ingredient parseIngredient(Scanner scanner) {
		double amount = scanner.nextDouble();
		String unit = scanner.next();
		if (Measurement.convertToUnit(unit) == null) { throw new IllegalArgumentException("Bad Format"); }
		StringBuilder ingredient = new StringBuilder();
		while (true) {
			String token = scanner.next();
			if (token.equals(")")) { break; }
			if (!ingredient.toString().equals("")) { ingredient.append(" "); }
			ingredient.append(token);
		}
		return new Ingredient(amount, unit, ingredient.toString());
	}

	/**
	 * Parse the info block from scanner
	 * @param scanner   Scanner to read from
	 * @return a new InfoBlock object
	 */
	private InfoBlock parseInfoBlock(Scanner scanner) {
		int serves = scanner.nextInt();
		assertExpected(scanner, ",");
		int prepHour = scanner.nextInt();
		assertExpected(scanner, ":");
		Duration prep = Duration.ofHours(prepHour).plus(Duration.ofMinutes(scanner.nextInt()));
		assertExpected(scanner, ",");
		int cookHour = scanner.nextInt();
		assertExpected(scanner, ":");
		Duration cook = Duration.ofHours(cookHour).plus(Duration.ofMinutes(scanner.nextInt()));
		return new InfoBlock(prep, cook, serves);
	}

	/**
	 * Parse a simple string sequence
	 * @param scanner   Scanner to read from
	 * @return  String which was parsed
	 */
	public String parseLetterSeqIn(Scanner scanner) {
		assertExpected(scanner, "(");
		StringBuilder letterSeq = new StringBuilder();
		while (true) {
			String token = scanner.next();
			if (token.equals(")")) {
				break;
			}
			if (!letterSeq.toString().equals("")) { letterSeq.append(" "); }
			letterSeq.append(token);
		}
		return letterSeq.toString();
	}

	/**
	 * Throws an error when token read wasn't expected one due to bad formatting
	 * @param scanner    Scanner to read from
	 * @param expected  Expected token
	 */
	public void assertExpected(Scanner scanner, String expected) {
		String token = scanner.next();
		if (!token.equals(expected)) { throw new IllegalArgumentException("Bad Format"); }
	}

	/**
	 * Parse and write a given Recipe object to a given file.
	 * @param recipe    Recipe to parse out
	 * @param file  File to write to
	 * @throws FileNotFoundException if file is invalid
	 */
	public void parseRecipeToFile(Recipe recipe, File file) throws FileNotFoundException {
		PrintWriter p = new PrintWriter(file);
		System.out.println(recipe.toFileFormat());
		p.write(recipe.toFileFormat());
		p.flush();
		p.close();
	}
}


import recipeInfo.Recipe;
import recipeInfo.recipeContents.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RecipeParser {

	private final Pattern LETTER_SEQUENCE = Pattern.compile("[^<>()\\n#]+");
	private final Pattern NUM = Pattern.compile("[0-9]+");
	private final Pattern QUANTITY = Pattern.compile("\\d+(\\.\\d+)*");
	private final Pattern INGREDIENT = Pattern.compile("(" + LETTER_SEQUENCE + ")");

	public Recipe parseRecipeFromFile(File recipeFile) throws FileNotFoundException {
		Scanner scanner = new Scanner(recipeFile);
		scanner.useDelimiter("\\s");

		String title = parseLetterSeqIn(scanner);

		assert scanner.next().equals("\n");

		InfoBlock info = parseInfoBlock(scanner);

		assert scanner.next().equals("\n");

		ArrayList<Ingredient> ingredients = parseIngredientBlock(scanner);

		Method method = parseMethod(scanner);

		return new Recipe(ingredients, method, title, info);
	}

	private Method parseMethod(Scanner scanner) {
		assert scanner.next().equals("<start>");
		Method m = new Method(new ArrayList<>());
		while (true) {
			String next = scanner.next();
			if (next.equals("<stop>")) {
				break;
			} else if (next.equals("#")) {
				m.addStep(parseStep(scanner.next(LETTER_SEQUENCE)));
				assert scanner.next().equals("#");
			} else {
				throw new IllegalArgumentException("Incorrect Format");
			}
		}
		return m;
	}

	private Instruction parseStep(String next) {
		Scanner scan = new Scanner(next);
		int stepNum = scan.nextInt();
		String step = scan.next(LETTER_SEQUENCE);
		return new Instruction(step, stepNum);
	}

	private ArrayList<Ingredient> parseIngredientBlock(Scanner scanner) {
		assert scanner.next().equals("<start>");
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		while (true) {
			String next = scanner.next();
			if (next.equals("<stop>")) {
				break;
			} else if (next.equals("(")) {
				ingredients.add(parseIngredient(scanner.next(LETTER_SEQUENCE)));
				assert scanner.next().equals(")");
			} else {
				throw new IllegalArgumentException("Incorrect Format");
			}
		}
		return ingredients;
	}

	private Ingredient parseIngredient(String next) {
		Scanner scan = new Scanner(next);
		double amount = scan.nextDouble();
		String unit = scan.next();
		assert Measurement.convertToUnit(unit) != null;
		StringBuilder ingredient = new StringBuilder();
		while (scan.hasNext()) { ingredient.append(scan.next()); }
		ingredient.deleteCharAt(ingredient.length()-1);
		scan.close();
		return new Ingredient(amount, unit, ingredient.toString());
	}

	private InfoBlock parseInfoBlock(Scanner scanner) {
		int serves = Integer.parseInt(scanner.next(NUM));
		Duration prep = Duration.ofHours(Integer.parseInt(scanner.next(NUM))).plus(Duration.ofMinutes(Integer.parseInt(scanner.next(NUM))));
		Duration cook = Duration.ofHours(Integer.parseInt(scanner.next(NUM))).plus(Duration.ofMinutes(Integer.parseInt(scanner.next(NUM))));
		return new InfoBlock(prep, cook, serves);
	}

	public String parseLetterSeqIn(Scanner sc) {
		assert sc.next().equals("(");
		String letterSeq = sc.next(LETTER_SEQUENCE);
		assert sc.next().equals(")");
		return letterSeq;
	}




	public File parseRecipeToFile(Recipe recipe, String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		PrintWriter p = new PrintWriter(file);
		p.write(recipe.toFileFormat());
		p.flush();
		p.close();
		return file;
	}

}


import recipeInfo.Recipe;
import recipeInfo.recipeContents.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;

public class RecipeParser {

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

	private Step parseStep(Scanner scan) {
		int stepNum = scan.nextInt();
		StringBuilder step = new StringBuilder();
		while (true) {
			String token = scan.next();
			if (token.equals("#")) { break; }
			if (!step.toString().equals("")) { step.append(" "); }
			step.append(token);
		}

		return new Step(step.toString(), stepNum);
	}

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

	public String parseLetterSeqIn(Scanner sc) {
		assertExpected(sc, "(");
		StringBuilder letterSeq = new StringBuilder();
		while (true) {
			String token = sc.next();
			if (token.equals(")")) {
				break;
			}
			if (!letterSeq.toString().equals("")) { letterSeq.append(" "); }
			letterSeq.append(token);
		}
		return letterSeq.toString();
	}

	public void assertExpected(Scanner sc, String expected) {
		String token = sc.next();
		if (!token.equals(expected)) { throw new IllegalArgumentException("Bad Format"); }
	}



	public void parseRecipeToFile(Recipe recipe, File file) throws FileNotFoundException {
		PrintWriter p = new PrintWriter(file);
		System.out.println(recipe.toFileFormat());
		p.write(recipe.toFileFormat());
		p.flush();
		p.close();
	}

}


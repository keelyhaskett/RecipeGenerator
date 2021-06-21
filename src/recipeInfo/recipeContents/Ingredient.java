package recipeInfo.recipeContents;

/**
 * Contains information about an ingredient within a recipe.
 * Includes fields for measurement and name of the ingredient.
 * Each Ingredient object is exclusive to the recipe object it originated from.
 * Contains getters for fields and String formatter for outward parsing.
 */
public class Ingredient {
	private final Measurement measurement;
	private final String ingredient;

	public Ingredient(double amount, String measurement, String ingredient) {
		this.measurement = new Measurement(amount, Measurement.convertToUnit(measurement));
		this.ingredient = ingredient;
	}

	public String getIngredient() { return ingredient; }

	public Measurement getMeasurement() { return measurement; }

	@Override
	public String toString() { return measurement.toString() + " " + ingredient + "\n"; }

	/**
	 * Format the ingredient to a parsable format.
	 * @return formatted ingredient String.
	 */
	public String toFileFormat() {
		return "( " + measurement.toFileFormat() + " " + ingredient + " ) ";
	}
}

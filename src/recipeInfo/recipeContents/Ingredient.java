package recipeInfo.recipeContents;

public class Ingredient {
	Measurement measurement;
	String ingredient;

	public Ingredient(String amount, String measurement, String ingredient) {
		this.measurement = new Measurement(amount, Measurement.convertToUnit(measurement));
		this.ingredient = ingredient;
	}

	@Override
	public String toString() {
		return measurement.toString() + " " + ingredient;
	}
//TODO: maybe make this a class??? or just a string field in the ingredients hashmap instead?
}

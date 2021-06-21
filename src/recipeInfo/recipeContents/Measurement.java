package recipeInfo.recipeContents;

/**
 * Information about the measurement of an ingredient, amount and unit of measurement.
 * Class contains getters for each field, the ability to add to an amount (for the shopping list), and all
 * information and helper methods related to UnitsOfMeasurement.
 * UnitsOfMeasurement define accepted measurement units, no unit can be other than these.
 * Methods to convert between enum style format and parsable string format are provided.
 */
public class Measurement {
    private double amount;
    private final UnitsOfMeasurement unit;

    public Measurement(double a, UnitsOfMeasurement u) {
        this.amount = a;
        this.unit = u;
    }

    public UnitsOfMeasurement getUnit() { return unit; }

    public double getAmount() { return amount; }

    public void addAmount(double toAdd) { amount += toAdd; }

    /**
     * Format the measurement into a parsable format.
     * @return a formatted measurement string
     */
    public String toFileFormat() { return amount + " " + convertToString(unit); }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(amount == Math.floor(amount) ? String.valueOf((int)amount) : String.valueOf(amount)); //if amount doesn't need decimal place, get rid of it
        switch (unit) {
            case GRAM -> s.append("g");
            case CUP -> {
                if (amount == 1) {
                    s.append(" cup");
                } else {
                    s.append(" cups");
                }
            }
            case LITRE -> s.append("L");
            case KILOGRAM -> s.append("kg");
            case TEASPOON -> s.append(" tsp");
            case MILLIGRAM -> s.append("mg");
            case MILLILITRE -> s.append("ml");
            case TABLESPOON -> s.append(" tbsp");
        }
        return s.toString();
    }

    /**
     *
     */
    public enum UnitsOfMeasurement {
        GRAM, KILOGRAM, MILLIGRAM, LITRE, MILLILITRE, CUP, TEASPOON, TABLESPOON, EACH
    }

    /**
     * Convert String unit to its Enum UnitsOfMeasurement value
     * @param s String to convert
     * @return  converted UnitsOfMeasurement
     */
    public static UnitsOfMeasurement convertToUnit(String s) {
        return switch (s) {
            case "gram" -> UnitsOfMeasurement.GRAM;
            case "cup" -> UnitsOfMeasurement.CUP;
            case "litre" -> UnitsOfMeasurement.LITRE;
            case "kilogram" -> UnitsOfMeasurement.KILOGRAM;
            case "teaspoon" -> UnitsOfMeasurement.TEASPOON;
            case "milligram" -> UnitsOfMeasurement.MILLIGRAM;
            case "millilitre" -> UnitsOfMeasurement.MILLILITRE;
            case "tablespoon" -> UnitsOfMeasurement.TABLESPOON;
            case "each" -> UnitsOfMeasurement.EACH;
            default -> null;
        };
    }

    /**
     * Convert UnitsOfMeasurement to its accepted String value
     * @param unit  UnitsOfMeasurement to convert
     * @return  an approved String alternative
     */
    public static String convertToString(UnitsOfMeasurement unit) {
        return switch (unit) {
            case GRAM -> "gram";
            case CUP -> "cup";
            case LITRE -> "litre";
            case KILOGRAM -> "kilogram";
            case TEASPOON -> "teaspoon";
            case MILLIGRAM -> "milligram";
            case MILLILITRE -> "millilitre";
            case TABLESPOON -> "tablespoon";
            case EACH -> "each";
        };
    }
}

package recipeInfo.recipeContents;

public class Measurement {
    private final double amount;
    private final UnitsOfMeasurement unit;

    public Measurement(double a, UnitsOfMeasurement u) {
        this.amount = a;
        this.unit = u;
    }

    public String toFileFormat() {
        return amount + " " + convertToString(unit);
    }

    public enum UnitsOfMeasurement {
        GRAM, KILOGRAM, MILLIGRAM, LITRE, MILLILITRE, CUP, TEASPOON, TABLESPOON, EACH
    }

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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(amount);
        switch (unit) {
            case GRAM -> s.append("g");
            case CUP -> s.append("c");
            case LITRE -> s.append("L");
            case KILOGRAM -> s.append("kg");
            case TEASPOON -> s.append("tsp");
            case MILLIGRAM -> s.append("mg");
            case MILLILITRE -> s.append("ml");
            case TABLESPOON -> s.append("tbsp");
        }
        return s.toString();
    }
}

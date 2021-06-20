package recipeInfo.recipeContents;

public class Measurement {
    private final String amount;
    private final UnitsOfMeasurement unit;

    public Measurement(String a, UnitsOfMeasurement u) {
        this.amount = a;
        this.unit = u;
    }

    public enum UnitsOfMeasurement {
        GRAM, KILOGRAM, MILLIGRAM, LITRE, MILLILITRE, CUP, TEASPOON, TABLESPOON, EACH
    }

    public static UnitsOfMeasurement convertToUnit(String s) {
        switch (s) {
            case "gram":
                return UnitsOfMeasurement.GRAM;
            case "cup":
                return UnitsOfMeasurement.CUP;

            case "litre":
                return UnitsOfMeasurement.LITRE;

            case "kilogram":
                return UnitsOfMeasurement.KILOGRAM;

            case "teaspoon":
                return UnitsOfMeasurement.TEASPOON;

            case "milligram":
                return UnitsOfMeasurement.MILLIGRAM;

            case "millilitre":
                return UnitsOfMeasurement.MILLILITRE;

            case "tablespoon":
                return UnitsOfMeasurement.TABLESPOON;

            case "each":
                return UnitsOfMeasurement.EACH;
        }
        return null;
    }

    public static String convertToString(UnitsOfMeasurement unit) {
        switch (unit) {
            case GRAM:
                return "gram";

            case CUP:
                return "cup";

            case LITRE:
                return "litre";

            case KILOGRAM:
                return "kilogram";

            case TEASPOON:
                return "teaspoon";

            case MILLIGRAM:
                return "milligram";

            case MILLILITRE:
                return "millilitre";

            case TABLESPOON:
                return "tablespoon";

            case EACH:
                return "each";

        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(amount);
        switch (unit) {
            case GRAM:
                s.append(" g");
                break;

            case CUP:
                s.append(" c");
                break;

            case LITRE:
                s.append(" l");
                break;

            case KILOGRAM:
                s.append(" kg");
                break;

            case TEASPOON:
                s.append(" tsp");
                break;

            case MILLIGRAM:
                s.append(" mg");
                break;

            case MILLILITRE:
                s.append(" ml");
                break;

            case TABLESPOON:
                s.append(" tbsp");
                break;

        }
        return s.toString();
    }
}

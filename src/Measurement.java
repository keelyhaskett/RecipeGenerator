public class Measurement {
    private final double amount;
    private final UnitsOfMeasurement unit;

    public Measurement(double a, UnitsOfMeasurement u) {
        this.amount = a;
        this.unit = u;
    }

    private enum UnitsOfMeasurement {
        GRAM, KILOGRAM, MILLIGRAM, LITRE, MILLILITRE, CUP, TEASPOON, TABLESPOON
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(amount);
        s.append(" ");
        switch (unit) {
            case GRAM:
                s.append("g");

            case CUP:
                s.append("c");

            case LITRE:
                s.append("l");

            case KILOGRAM:
                s.append("kg");

            case TEASPOON:
                s.append("tsp");

            case MILLIGRAM:
                s.append("mg");

            case MILLILITRE:
                s.append("ml");

            case TABLESPOON:
                s.append("tbsp");

        }
        return s.toString();
    }
}

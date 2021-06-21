package recipeInfo.recipeContents;

/**
 * Step contains method number and step description for a method step.
 * Ability to change step number (for creation mistakes) and format for outward parsing.
 */
public class Step {
    String step;
    int stepNumber;

    public Step(String step, int num) {
        this.step = step;
        this.stepNumber = num;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    @Override
    public String toString() {
        String tempStep = stepNumber + ". " + step;
        StringBuilder b = new StringBuilder();
        boolean needsNewLine = false;

        for (int i = 0; i < tempStep.length(); i++) {
            if (i%60 == 0 && i != 0) {
                needsNewLine = true;
            }
            if (tempStep.charAt(i) == (' ') && needsNewLine) {
                b.append('\n');
                needsNewLine = false;
            }
            b.append(tempStep.charAt(i));
        }
        return b.toString();
    }

    /**
     * Format step to a parsable format.
     * @return a formatted String
     */
    public String toFileFormat() {
        return "# " + stepNumber + " " + step + " #  ";
    }
}

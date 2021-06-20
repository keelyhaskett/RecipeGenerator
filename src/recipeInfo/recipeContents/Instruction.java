package recipeInfo.recipeContents;

public class Instruction {
    String step;
    int stepNumber;

    public Instruction(String step, int num) {
        this.step = step;
        this.stepNumber = num;
    }

    @Override
    public String toString() {
        String tempStep = stepNumber + ". " + step;
        StringBuilder b = new StringBuilder();
        boolean needsNewLine = false;

        for (int i = 0; i < tempStep.length(); i++) {
            if (i%30 == 0 && i != 0) {
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

    public String toFileFormat() {
        return "# " + stepNumber + " " + step + " # ";
    }
}

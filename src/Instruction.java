public class Instruction {
    String step;

    public Instruction(String i) {
        this.step = i;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        boolean needsNewLine = false;
        for (int i = 0; i < step.length(); i++) {
            if (i%30 == 0 && i != 0) {
                needsNewLine = true;
            }
            if (step.charAt(i) == (' ') && needsNewLine) {
                b.append('\n');
                needsNewLine = false;
            }
            b.append(step.charAt(i));
        }
        return b.toString();
    }
}

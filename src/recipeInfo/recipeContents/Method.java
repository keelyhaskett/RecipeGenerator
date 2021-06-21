package recipeInfo.recipeContents;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private ArrayList<Instruction> steps;

    public Method(List<Instruction> i) {
        steps = new ArrayList<>(i);
    }

    public void addStep(Instruction i) { steps.add(i); }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Steps: ").append("\n");
        steps.forEach(step -> b.append(step.toString()).append("\n"));
        return b.toString();
    }

    public String toFileFormat() {
        StringBuilder b = new StringBuilder();
        b.append("<start> ");
        for (Instruction i : steps) {
            b.append(i.toFileFormat());
        }
        b.append("<stop>");
        return b.toString();
    }
}

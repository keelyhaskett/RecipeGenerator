package RecipeInfo.RecipeContents;

import java.util.ArrayList;

public class Method {
    private ArrayList<Instruction> steps;

    public Method() {
        steps = new ArrayList<>();
    }

    public void addStep(Instruction i) { steps.add(i); }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Steps: ").append(steps.size()).append("\n");
        for (int i = 0; i < steps.size(); i++) {
            b.append(i).append(") ");
            b.append(steps.get(i).toString());
        }
        return b.toString();
    }
}

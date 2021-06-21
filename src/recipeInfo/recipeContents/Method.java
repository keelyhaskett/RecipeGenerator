package recipeInfo.recipeContents;

import java.util.ArrayList;
import java.util.List;

/**
 * Method has a list of steps, which can be added to.
 * Contains a method to convert method to file format for outward parsing.
 */
public class Method {
    private final ArrayList<Step> steps;

    public Method(List<Step> i) {
        steps = new ArrayList<>(i);
    }

    public void addStep(Step i) { steps.add(i); }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("Steps: ").append("\n");
        steps.forEach(step -> b.append(step.toString()).append("\n"));
        return b.toString();
    }

    /**
     * Format method to parsable format.
     * @return formatted method string
     */
    public String toFileFormat() {
        StringBuilder b = new StringBuilder();
        b.append("<start> ");
        for (Step i : steps) {
            b.append(i.toFileFormat());
        }
        b.append("<stop>");
        return b.toString();
    }
}

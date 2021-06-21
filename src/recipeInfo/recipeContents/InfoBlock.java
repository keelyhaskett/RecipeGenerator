package recipeInfo.recipeContents;

import java.time.Duration;

/**
 * Infoblock stores the numeric initial info of a recipe (times and serves), primarily for the purpose of parsing
 * ease.
 * Contains a getter for each field, and a fileFormat String method for parsing back to a file.
 */
public class InfoBlock {

	private final Duration prepTime;
	private final Duration cookTime;
	private final int serves;

	public InfoBlock(Duration prepTime, Duration cookTime, int serves) {
		this.prepTime = prepTime;
		this.cookTime = cookTime;
		this.serves = serves;
	}

	public Duration getPrepTime() {
		return prepTime;
	}

	public Duration getCookTime() {
		return cookTime;
	}

	public int getServes() {
		return serves;
	}

	/**
	 * Format information into a parsable state.
	 * @return a formatted string of info.
	 */
	public String toFileFormat() {
		return serves + " , " +
				((int) prepTime.toMinutes()) / 60 + " : " + prepTime.toMinutes() % 60 + " , " +
				((int) cookTime.toMinutes()) / 60 + " : " + cookTime.toMinutes() % 60;
	}
}

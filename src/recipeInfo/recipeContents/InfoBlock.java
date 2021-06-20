package recipeInfo.recipeContents;

import java.time.Duration;

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

	public String toFileFormat() {
		return serves + "," +
				((int) prepTime.toMinutes()) / 60 + ":" + prepTime.toMinutes() % 60 + "," +
				((int) cookTime.toMinutes()) / 60 + ":" + cookTime.toMinutes() % 60 +
				"\n";
	}
}

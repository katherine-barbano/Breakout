package text;

import javafx.scene.Group;

public class LivesText extends StatusText {

  public static final String LIVES_TITLE = "Lives: ";
  public static final int LIVES_XPOSITION = LevelText.LEVEL_XPOSITION;
  public static final int LIVES_YPOSITION = LevelText.LEVEL_YPOSITION+25;
  public static final String LIVES_ID = "#livesText";

  public LivesText(int numberOfLives, Group gameRootArg) {
    super(gameRootArg, numberOfLives, LIVES_TITLE, LIVES_XPOSITION, LIVES_YPOSITION, LIVES_ID);
  }
}

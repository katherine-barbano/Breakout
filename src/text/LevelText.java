package text;

import javafx.scene.Group;

public class LevelText extends StatusText {

  public static final String LEVEL_TITLE="Level: ";
  public static final int LEVEL_XPOSITION = 520;
  public static final int LEVEL_YPOSITION = 40;
  public static final String LEVEL_ID = "#levelText";

  public LevelText(int levelNumber, Group gameRootArg) {
    super(gameRootArg,levelNumber, LEVEL_TITLE,LEVEL_XPOSITION,LEVEL_YPOSITION,LEVEL_ID);
  }
}

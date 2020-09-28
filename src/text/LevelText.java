package text;

import javafx.scene.Group;

public class LevelText extends StatusText {

  public LevelText(int levelNumber, Group gameRootArg) {
    super(gameRootArg, levelNumber);
    setStatusTextProperties(getLevelTitle(), getLevelXPosition(), getLevelYPosition(), getLevelId());
  }
}

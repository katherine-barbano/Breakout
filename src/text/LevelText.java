package text;

import javafx.scene.Group;

public class LevelText extends StatusText {

  public LevelText(int levelNumber, Group gameRootArg) {
    super(gameRootArg, levelNumber);
  }

  @Override
  void setStatusTextProperties(){
    setTitle(getLevelTitle());
    setX(getLevelXPosition());
    setY(getLevelYPosition());
    setId(getLevelId());
  }
}

package text;

import javafx.scene.Group;

/***
 * Subclass to display level number status indicator on the InfoBar
 * at the specified position and with the "Level: " title.
 *
 * @author Katherine Barbano
 */
public class LevelText extends StatusText {

  public LevelText(int levelNumber, Group gameRootArg) {
    super(gameRootArg, levelNumber);
  }

  @Override
  void setStatusTextProperties() {
    setTitle(getLevelTitle());
    setX(getLevelXPosition());
    setY(getLevelYPosition());
    setId(getLevelId());
  }
}

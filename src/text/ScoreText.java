package text;

import javafx.scene.Group;

/***
 * Subclass to display score value status indicator on the InfoBar
 * at the specified position and with the "Score: " title.
 */
public class ScoreText extends StatusText {

  public ScoreText(int score, Group gameRootArg) {
    super(gameRootArg, score);
  }

  @Override
  void setStatusTextProperties(){
    setTitle(getScoreTitle());
    setX(getScoreXPosition());
    setY(getLevelYPosition());
    setId(getScoreId());
  }
}
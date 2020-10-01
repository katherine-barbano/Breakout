package text;

import javafx.scene.Group;

/***
 * Subclass to display score to win status indicator on the InfoBar
 * at the specified position and with the "Score needed to win level: " title.
 */
public class ScoreToWinText extends StatusText {

  public ScoreToWinText(int scoreTarget, Group gameRootArg) {
    super(gameRootArg, scoreTarget);
  }

  @Override
  void setStatusTextProperties(){
    setTitle(getScoreToWinTitle());
    setX(getScoreToWinXPosition());
    setY(getScoreToWinYPosition());
    setId(getScoreToWinId());
  }
}
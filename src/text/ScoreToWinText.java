package text;

import javafx.scene.Group;

public class ScoreToWinText extends StatusText {

  public ScoreToWinText(int scoreTarget, Group gameRootArg) {
    super(gameRootArg, scoreTarget);
    setStatusTextProperties(getScoreToWinTitle(), getScoreToWinXPosition(), getScoreToWinYPosition(), getScoreToWinId());
  }
}
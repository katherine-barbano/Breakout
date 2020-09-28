package text;

import javafx.scene.Group;

public class ScoreText extends StatusText {

  public ScoreText(int score, Group gameRootArg) {
    super(gameRootArg, score);
    setStatusTextProperties(getScoreTitle(), getScoreXPosition(), getLevelYPosition(), getScoreId());
  }
}
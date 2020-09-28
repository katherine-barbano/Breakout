package text;

import javafx.scene.Group;

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
package text;

import javafx.scene.Group;

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
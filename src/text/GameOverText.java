package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameOverText extends GameText {

  public GameOverText(Group gameRootArg) {
    super(gameRootArg);
    initializeText(getGameOverTitle());
    setFill(getGameOverColor());
  }

  @Override
  public void initializeText(String words) {
    initializeProperties(words, getGameOverXPosition(), getGameOverYPosition(), getGameOverId());
    addText();
  }

  @Override
  public void removeText() {
    setText("");
  }

  @Override
  void updateText(String words) {
    setText(words);
  }

  public void gameOverUpdate(boolean gameIsWon) {
    if(gameIsWon) {
      updateText(getGameWonTitle());
    }
    else {
      updateText(getGameOverTitle());
    }
  }
}

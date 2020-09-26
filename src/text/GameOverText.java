package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class GameOverText extends GameText {

  public static final String GAMEOVER_TITLE = "Game Over! Tap the space bar to restart from Level 1.";
  public static final String GAMEWON_TITLE = "You won! Tap the space bar to restart from Level 1.";
  public static final String GAMEOVER_ID = "gameOverText";

  public static final int GAMEOVER_XPOSITION = 60;
  public static final int GAMEOVER_YPOSITION = 250;

  public GameOverText(Group gameRootArg) {
    super(gameRootArg);
    initializeText(GAMEOVER_TITLE);
  }

  @Override
  public void initializeText(String words) {
    initializeProperties(words, GAMEOVER_XPOSITION, GAMEOVER_YPOSITION, GAMEOVER_ID);
    addText();
  }

  @Override
  public void removeText() {
    setText("");
  }

  public void gameOverUpdate(boolean gameIsWon) {
    if(gameIsWon) {
      setText(GAMEWON_TITLE);
    }
    else {
      setText(GAMEOVER_TITLE);
    }
  }
}

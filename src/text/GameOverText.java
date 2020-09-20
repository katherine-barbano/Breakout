package text;

import breakout.Game;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameOverText extends Text implements GameText {

  public static final String GAMEOVER_TITLE = "Game Over! Tap the space bar to restart from Level 1.";
  public static final String GAMEWON_TITLE = "You won! Tap the space bar to restart from Level 1.";
  public static final String TEXT_FONT = "ARIAL";
  public static final int TEXT_SIZE = 20;
  public static final Paint TEXT_COLOR = Color.MEDIUMVIOLETRED;
  public static final String GAMEOVER_ID = "gameOverText";

  public static final int GAMEOVER_XPOSITION = 60;
  public static final int GAMEOVER_YPOSITION = 250;

  private Group gameRoot;

  public GameOverText(Group gameRootArg) {
    gameRoot = gameRootArg;
    initializeText(GAMEOVER_TITLE);
  }

  @Override
  public void initializeText(String words) {
    setX(GAMEOVER_XPOSITION);
    setY(GAMEOVER_YPOSITION);
    setFont(new Font(TEXT_FONT, TEXT_SIZE));
    setFill(TEXT_COLOR);
    setId(GAMEOVER_ID);

    addText();
  }

  @Override
  public void removeText() {
    gameRoot.getChildren().remove(this);
  }

  @Override
  public void addText() {
    gameRoot.getChildren().add(this);
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

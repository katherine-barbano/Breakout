package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PauseText extends Text implements GameText {

  public static final String PAUSE_TITLE="Paused. Resume with space bar";
  public static final String START_TITLE = "Click the space bar to start!";
  public static final String TEXT_FONT = "ARIAL";
  public static final int TEXT_SIZE = 20;
  public static final Paint TEXT_COLOR = Color.MEDIUMVIOLETRED;

  public static final int PAUSE_XPOSITION = 20;
  public static final int PAUSE_YPOSITION = LivesText.LIVES_YPOSITION;

  private Group gameRoot;

  public PauseText(Group gameRootArg) {
    gameRoot = gameRootArg;
    startGame();
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    setX(PAUSE_XPOSITION);
    setY(PAUSE_YPOSITION);
    setFont(new Font(TEXT_FONT, TEXT_SIZE));
    setFill(TEXT_COLOR);
  }

  @Override
  public void removeText() {
    gameRoot.getChildren().remove(this);
  }

  @Override
  public void addText() {
    gameRoot.getChildren().add(this);
  }

  public void startGame() {
    /*if (gameRoot.getChildren().contains(gameOverText)) {
      gameOverText.removeText();
    }*/

    initializeText(START_TITLE);
    addText();
  }

  public void startPause() {
    removeText();
    initializeText(PAUSE_TITLE);
    addText();
  }

  public void endPause() {
    removeText();
  }

}

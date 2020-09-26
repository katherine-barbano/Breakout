package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class PauseText extends GameText {

  public static final String PAUSE_TITLE="Paused. Resume with space bar";
  public static final String START_TITLE = "Click the space bar to start!";

  public static final int PAUSE_XPOSITION = 20;
  public static final int PAUSE_YPOSITION = LivesText.LIVES_YPOSITION;
  public static final String PAUSE_ID = "#pauseText";

  public PauseText(Group gameRootArg) {
    super(gameRootArg);
    startGame();
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, PAUSE_XPOSITION, PAUSE_YPOSITION, PAUSE_ID);
  }

  public void startGame() {
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

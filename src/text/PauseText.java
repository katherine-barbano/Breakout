package text;

import javafx.scene.Group;

public class PauseText extends GameText {

  public static final String PAUSE_TITLE="Paused. Resume with space";
  public static final String START_TITLE = "Click the space bar to start!";

  public static final int PAUSE_XPOSITION = ScoreToWinText.SCORETOWIN_XPOSITION;
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

  @Override
  void updateText(String words) {
    initializeText(words);
    addText();
  }

  public void startGame() {
    updateText(START_TITLE);
  }

  public void startPause() {
    removeText();
    updateText(PAUSE_TITLE);
  }

  public void endPause() {
    removeText();
  }

}

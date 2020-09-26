package text;

import javafx.scene.Group;

public class ScoreText extends GameText {

  public static final String SCORE_TITLE = "Score: ";
  public static final int SCORE_XPOSITION = 375;
  public static final int SCORE_YPOSITION = 40;
  public static final String SCORE_ID = "#scoreText";

  public ScoreText(Group gameRootArg) {
    super(gameRootArg);
    updateScore(0);
    addText();
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, SCORE_XPOSITION, SCORE_YPOSITION, SCORE_ID);
  }

  public void updateScore(int newScore) {
    String words = SCORE_TITLE + newScore;
    initializeText(words);
  }
}

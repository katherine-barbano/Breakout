package text;

import javafx.scene.Group;

public class ScoreText extends StatusText {

  public static final String SCORE_TITLE = "Score: ";
  public static final int SCORE_XPOSITION = LivesText.LIVES_XPOSITION-120;
  public static final int SCORE_YPOSITION = LivesText.LIVES_YPOSITION;
  public static final String SCORE_ID = "#scoreText";

  public ScoreText(int score, Group gameRootArg) {
    super(gameRootArg,score, SCORE_TITLE,SCORE_XPOSITION,SCORE_YPOSITION,SCORE_ID);
  }
}

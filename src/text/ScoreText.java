package text;

import javafx.scene.Group;

public class ScoreText extends StatusText {

  public static final String SCORE_TITLE = "Score: ";
  public static final int SCORE_XPOSITION = 375;
  public static final int SCORE_YPOSITION = 40;
  public static final String SCORE_ID = "#scoreText";

  public ScoreText(Group gameRootArg) {
    super(gameRootArg,0, SCORE_TITLE,SCORE_XPOSITION,SCORE_YPOSITION,SCORE_ID);
  }
}

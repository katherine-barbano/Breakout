package text;

import javafx.scene.Group;

public class ScoreToWinText extends StatusText {

  public static final String SCORETOWIN_TITLE = "Score needed to win level: ";
  public static final int SCORETOWIN_XPOSITION = 320;
  public static final int SCORETOWIN_YPOSITION = LevelText.LEVEL_YPOSITION;
  public static final String SCORETOWIN_ID = "#scoreToWinText";

  public ScoreToWinText(int scoreTarget, Group gameRootArg) {
    super(gameRootArg, scoreTarget, SCORETOWIN_TITLE, SCORETOWIN_XPOSITION, SCORETOWIN_YPOSITION, SCORETOWIN_ID);
  }
}
package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ScoreText extends Text implements GameText {

  public static final String SCORE_TITLE = "Score: ";
  public static final int SCORE_XPOSITION = 375;
  public static final int SCORE_YPOSITION = 40;
  public static final String TEXT_FONT = "ARIAL";
  public static final int TEXT_SIZE = 20;
  public static final Paint TEXT_COLOR = Color.MEDIUMVIOLETRED;
  public static final String SCORE_ID = "#scoreText";

  private Group gameRoot;

  public ScoreText(Group gameRootArg) {
    gameRoot = gameRootArg;
    updateScore(0);
    addText();
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    setX(SCORE_XPOSITION);
    setY(SCORE_YPOSITION);
    setFont(new Font(TEXT_FONT, TEXT_SIZE));
    setFill(TEXT_COLOR);
    setId(SCORE_ID);
  }

  @Override
  public void removeText() {
    gameRoot.getChildren().remove(this);
  }

  @Override
  public void addText() {
    gameRoot.getChildren().add(this);
  }

  public void updateScore(int newScore) {
    String words = SCORE_TITLE + newScore;
    initializeText(words);
  }
}

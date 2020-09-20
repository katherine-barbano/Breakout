package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LivesText extends Text implements GameText {

  public static final String LIVES_TITLE = "Lives: ";
  public static final int LIVES_XPOSITION = 500;
  public static final int LIVES_YPOSITION = 40;
  public static final String TEXT_FONT = "ARIAL";
  public static final int TEXT_SIZE = 20;
  public static final Paint TEXT_COLOR = Color.MEDIUMVIOLETRED;

  private Group gameRoot;

  public LivesText(int numberOfLives, Group gameRootArg) {
    gameRoot = gameRootArg;
    updateLives(numberOfLives);
    addText();
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    setX(LIVES_XPOSITION);
    setY(LIVES_YPOSITION);
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

  public void updateLives(int numberOfLives) {
    String words = LIVES_TITLE + numberOfLives;
    initializeText(words);
  }
}

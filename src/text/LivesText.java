package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class LivesText extends GameText {

  public static final String LIVES_TITLE = "Lives: ";
  public static final int LIVES_XPOSITION = 500;
  public static final int LIVES_YPOSITION = 40;
  public static final String LIVES_ID = "#livesText";

  public LivesText(int numberOfLives, Group gameRootArg) {
    super(gameRootArg);
    updateLives(numberOfLives);
    addText();
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, LIVES_XPOSITION, LIVES_YPOSITION, LIVES_ID);
  }

  public void updateLives(int numberOfLives) {
    String words = LIVES_TITLE + numberOfLives;
    initializeText(words);
  }
}

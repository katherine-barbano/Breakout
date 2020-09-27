package text;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class GameText extends Text {

  public static final Paint TEXT_COLOR = Color.WHITE;
  public static final String TEXT_FONT = "ARIAL";
  public static final int TEXT_SIZE = 20;

  private Group gameRoot;
  public GameText(Group gameRootArg) {
    gameRoot = gameRootArg;
  }

  abstract void initializeText(String words);
  abstract void updateText(String text);

  void initializeProperties(String text, int x, int y, String id) {
    setText(text);
    setX(x);
    setY(y);
    setFont(new Font(TEXT_FONT, TEXT_SIZE));
    setFill(TEXT_COLOR);
    setId(id);
  }

  public void removeText() {
    gameRoot.getChildren().remove(this);
  }

  public void addText() {
    gameRoot.getChildren().add(this);
  }
}

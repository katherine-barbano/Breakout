package text;

import javafx.scene.Group;

public class StatusText extends GameText {

  private String title;
  private String id;
  private int xPosition;
  private int yPosition;

  public StatusText(Group gameRootArg, int statusValue, String titleArg, int xPositionArg, int yPositionArg, String idArg) {
    super(gameRootArg);
    setStatusTextProperties(titleArg, xPositionArg, yPositionArg, idArg);
    updateValue(statusValue);
  }

  public StatusText(Group gameRootArg, int statusValue) {
    super(gameRootArg);
    updateValue(statusValue);
  }

  void setStatusTextProperties(String titleArg, int xPositionArg, int yPositionArg, String idArg) {
    title=titleArg;
    id=idArg;
    xPosition=xPositionArg;
    yPosition=yPositionArg;
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, xPosition, yPosition, id);
  }

  @Override
  public void updateText(String newText) {
    String words = title + newText;
    initializeText(words);
  }

  public void updateValue(int integerNewText) {
    updateText(""+integerNewText);
  }
}

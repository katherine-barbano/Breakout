package text;

import javafx.scene.Group;

public abstract class StatusText extends GameText {

  private String title;

  public StatusText(Group gameRootArg, int statusValue) {
    super(gameRootArg);
    addText();
    setStatusTextProperties();
    updateValue(statusValue);
  }

  abstract void setStatusTextProperties();

  void setTitle(String titleArg) {
    title = titleArg;
  }

  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, (int)getX(), (int)getY(), getId());
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

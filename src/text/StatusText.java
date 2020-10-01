package text;

import javafx.scene.Group;

/***
 * Abstract superclass for text that is of the format "title: int value",
 * specifically for status indicators like Score: 10 or Lives: 3.
 * Contains abstraction for what to set the position, id, and title
 * variables as.
 */
public abstract class StatusText extends GameText {

  /***
   * If the text you want to display is "Score: 3", the title
   * would be "Score: ".
   */
  private String title;

  /***
   * Constructor adds the text to the scene and initializes it.
   * updates the status value initially to the provided argument.
   * @param gameRootArg Group
   * @param statusValue Integer
   */
  public StatusText(Group gameRootArg, int statusValue) {
    super(gameRootArg);
    addText();
    setStatusTextProperties();
    updateValue(statusValue);
  }

  /***
   * Sets the position, title, and id of the specific type of
   * subclass text.
   */
  abstract void setStatusTextProperties();

  /***
   * Sets the title of the text. If the text you want to display is "Score: 3", the title
   * would be "Score".
   * @param titleArg String
   */
  void setTitle(String titleArg) {
    title = titleArg;
  }

  /***
   * Initializes the properties for the first time.
   * @param words The text that should be displayed on the screen.
   */
  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, (int) getX(), (int) getY(), getId());
  }

  /***
   * Used as a helper for updateValue. Updates the text
   * to the given string and intializes it.
   * @param newText Text without a title
   */
  @Override
  public void updateText(String newText) {
    String words = title + newText;
    initializeText(words);
  }

  /***
   * This method should be called instead of updateText.
   * Makes it much easier to update StatusText instead of using updateText because it
   * takes an integer as an argument for what value to
   * set the statusText to.
   * @param integerNewText integer
   */
  public void updateValue(int integerNewText) {
    updateText("" + integerNewText);
  }
}

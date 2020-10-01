package text;

import javafx.scene.Group;

/***
 * Subclass to display the fact that the game has ended, and
 * whether the game resulted in a win or a loss.
 */
public class GameOverText extends GameText {

  /***
   * Initializes the text and sets the color of the
   * text so that it is visible.
   * @param gameRootArg
   */
  public GameOverText(Group gameRootArg) {
    super(gameRootArg);
    initializeText(getGameOverTitle());
    setFill(getGameOverColor());
  }

  /***
   * Adds the text to the scene, and sets
   * the position, id, and text.
   * @param words The text that should be displayed on the screen.
   */
  @Override
  public void initializeText(String words) {
    initializeProperties(words, getGameOverXPosition(), getGameOverYPosition(), getGameOverId());
    addText();
  }

  /***
   * Overrides GameText's definition of removeText to not actually
   * remove the text from the scene, but to set it as an empty string.
   * This allows GameText to always be present, so that updateText never
   * attempts to display "you lost" or "you won" for
   * a GameOverText that is not actually showing in the Scene.
   */
  @Override
  public void removeText() {
    setText("");
  }

  /***
   * Sets the text to the argument.
   * @param words new string of text
   */
  @Override
  void updateText(String words) {
    setText(words);
  }

  /***
   * updates the text to "you won" if gameIsWon is true.
   * updates text to "you lost" if gameIsWon is false.
   * @param gameIsWon true if the game is won.
   */
  public void gameOverUpdate(boolean gameIsWon) {
    if (gameIsWon) {
      updateText(getGameWonTitle());
    } else {
      updateText(getGameOverTitle());
    }
  }

}

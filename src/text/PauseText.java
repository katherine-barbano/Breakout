package text;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;

/***
 * Subclass to display the fact that the game is paused when pressing the space bar
 * cheat key, or "Press space to start" at the beginning of the game (because
 * the game always begins in the paused state).
 */
public class PauseText extends GameText {

  private Properties properties;

  /***
   * Sets the group and updates the text
   * to "Press space to start" because the game is beginning.
   * @param gameRootArg group
   */
  public PauseText(Group gameRootArg) {
    super(gameRootArg);
    initializePropertiesFile();
    startGame();
  }

  /***
   * Retrieves properties file to use constants.
   */
  void initializePropertiesFile() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    } catch (FileNotFoundException e) {
    } catch (IOException e) {
    }
  }

  /***
   * Adds the text to the scene, and sets
   * the position, id, and text.
   * @param words The text that should be displayed on the screen.
   */
  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, getPauseXPosition(), getPauseYPosition(), getPauseId());
  }

  /***
   * Initializes and adds the text for its specific properties.
   * @param words new string of text
   */
  @Override
  void updateText(String words) {
    initializeText(words);
    addText();
  }

  /***
   * Starts the game by updating the text to be "Press space to start"
   */
  public void startGame() {
    updateText(getStartTitle());
  }

  /***
   * Begins a pause by updating the text on the screen to say
   * that the game is currently paused.
   */
  public void startPause() {
    removeText();
    updateText(getPauseTitle());
  }

  /***
   * Ends a pause by removing the PauseText from the screen.
   */
  public void endPause() {
    removeText();
  }
}

package text;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;

public class PauseText extends GameText {

  private Properties properties;

  public PauseText(Group gameRootArg) {
    super(gameRootArg);
    initializePropertiesFile();
    startGame();
  }

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

  @Override
  public void initializeText(String words) {
    setText(words);
    initializeProperties(words, getPauseXPosition(), getPauseYPosition(), getPauseId());
  }

  @Override
  void updateText(String words) {
    initializeText(words);
    addText();
  }

  public void startGame() {
    updateText(getStartTitle());
  }

  public void startPause() {
    removeText();
    updateText(getPauseTitle());
  }

  public void endPause() {
    removeText();
  }
}

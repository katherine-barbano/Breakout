package text;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/***
 * Abstract superclass for all types of text in this game of Breakout.
 * Contains abstractions for how text is initialized and updated, and
 * concretely defines how to addText and removeText from the Scene.
 */
public abstract class GameText extends Text {

  private Properties properties;
  private Group gameRoot;

  /***
   * Constructor that sets the Group to add text elements to.
   * @param gameRootArg Group
   */
  public GameText(Group gameRootArg) {
    gameRoot = gameRootArg;
    getPropertiesList();
  }

  /***
   * Describes how text should be displayed on the screen for the
   * first time when instantiated.
   * @param words The text that should be displayed on the screen.
   */
  abstract void initializeText(String words);

  /***
   * Describes how text should be updated on the screen when
   * it needs to change.
   * @param text The text that should be displayed on the screen.
   */
  abstract void updateText(String text);

  /***
   * Helper method for subclasses to initialize the text,
   * position, and id.
   * @param text
   * @param x
   * @param y
   * @param id
   */
  void initializeProperties(String text, int x, int y, String id) {
    setText(text);
    setX(x);
    setY(y);
    setFont(new Font(getTextFont(), getTextSize()));
    setFill(getTextColor());
    setId(id);
  }

  /***
   * Used to retrieve constants from properties file.
   */
  void getPropertiesList() {
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
   * Remove this object from the scene.
   */
  public void removeText() {
    gameRoot.getChildren().remove(this);
  }

  /***
   * Add this object to the scene.
   */
  public void addText() {
    gameRoot.getChildren().add(this);
  }

  /***
   * Begin properties accessors
   */

  // getters for text properties
  Paint getTextColor() {
    return Paint.valueOf(properties.getProperty("text_color"));
  }

  String getTextFont() {
    return properties.getProperty("text_font");
  }

  int getTextSize() {
    return Integer.parseInt(properties.getProperty("text_size"));
  }

  // getters for livesText properties
  String getLivesTitle() {
    return properties.getProperty("lives_title");
  }

  String getLivesId() {
    return properties.getProperty("lives_id");
  }

  int getLivesXPosition() {
    return Integer.parseInt(properties.getProperty("lives_x_position"));
  }

  int getLivesYPosition() {
    return Integer.parseInt(properties.getProperty("lives_y_position"));
  }

  // getters for scoreText properties
  String getScoreTitle() {
    return properties.getProperty("score_title");
  }

  String getScoreId() {
    return properties.getProperty("score_id");
  }

  int getScoreXPosition() {
    return Integer.parseInt(properties.getProperty("score_x_position"));
  }

  // getters for LevelText properties
  int getLevelYPosition() {
    return Integer.parseInt(properties.getProperty("level_y_position"));
  }

  String getLevelTitle() {
    return properties.getProperty("level_title");
  }

  String getLevelId() {
    return properties.getProperty("level_id");
  }

  int getLevelXPosition() {
    return Integer.parseInt(properties.getProperty("level_x_position"));
  }

  // getters for ScoreToWinText properties
  String getScoreToWinTitle() {
    return properties.getProperty("score_to_win_title");
  }

  String getScoreToWinId() {
    return properties.getProperty("score_to_win_id");
  }

  int getScoreToWinXPosition() {
    return Integer.parseInt(properties.getProperty("score_to_win_x_position"));
  }

  int getScoreToWinYPosition() {
    return Integer.parseInt(properties.getProperty("score_to_win_y_position"));
  }

  // getters for PauseText properties
  String getPauseTitle() {
    return properties.getProperty("pause_title");
  }

  String getStartTitle() {
    return properties.getProperty("start_title");
  }

  String getPauseId() {
    return properties.getProperty("pause_id");
  }

  int getPauseXPosition() {
    return Integer.parseInt(properties.getProperty("pause_x_position"));
  }

  int getPauseYPosition() {
    return Integer.parseInt(properties.getProperty("pause_y_position"));
  }

  // getters for TimerText properties
  String getTimerTitle() {
    return properties.getProperty("timer_title");
  }

  String getTimerId() {
    return properties.getProperty("timer_id");
  }

  int getTimerXPosition() {
    return Integer.parseInt(properties.getProperty("timer_x_position"));
  }

  int getTimerYPosition() {
    return Integer.parseInt(properties.getProperty("timer_y_position"));
  }

  // getters for GameOverText properties
  String getGameOverTitle() {
    return properties.getProperty("game_over_title");
  }

  String getGameWonTitle() {
    return properties.getProperty("game_won_title");
  }

  String getGameOverId() {
    return properties.getProperty("game_over_id");
  }

  int getGameOverXPosition() {
    return Integer.parseInt(properties.getProperty("game_over_x_position"));
  }

  int getGameOverYPosition() {
    return Integer.parseInt(properties.getProperty("game_over_y_position"));
  }

  Paint getGameOverColor() {
    return Paint.valueOf(properties.getProperty("game_over_color"));
  }

  // getters for GameOverScoreText properties
  String getGameOverScoreId() {
    return properties.getProperty("game_over_score_id");
  }

  int getGameOverScoreAmountShown() {
    return Integer.parseInt(properties.getProperty("game_over_score_amount"));
  }

  int getGameOverScoreXPosition() {
    return Integer.parseInt(properties.getProperty("game_over_score_x_position"));
  }

  int getGameOverScoreYPosition() {
    return Integer.parseInt(properties.getProperty("game_over_score_y_position"));
  }

  Paint getGameOverScoreColor() {
    return Paint.valueOf(properties.getProperty("game_over_score_color"));
  }

  String getGameOverScoreTitle() {
    return properties.getProperty("game_over_score_title");
  }

  String getGameOverYourScoreTitle() {
    return properties.getProperty("game_over_your_score_title");
  }

  String getGameOverScoreSeparator() {
    return properties.getProperty("game_over_score_separator");
  }

  String getGameOverScorePrefix() {
    return properties.getProperty("game_over_score_prefix");
  }

  String getScoreBoardExitText() {
    return properties.getProperty("game_score_board_exit_text");
  }
}

package gameElements;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import text.GameText;
import text.LivesText;
import text.PauseText;
import text.StatusText;

/***
 * Handles displaying information on the top of the screen about
 * status indicators for the level and game, such as whether the game is paused,
 * the score, the number of lives left, the level number, the score necessary to win,
 * and the remaining time.
 */
public class InfoBar extends Rectangle {

  private Properties properties;
  private GameText pauseText;
  private GameText scoreText;
  private GameText livesText;
  private GameText levelText;
  private GameText scoreToWinText;
  private GameTimer gameTimer;
  private Group root;

  /***
   * Constructor with scoreText as argument. scoreText is argument
   * because InfoBar is instantiated in Game, and score is the only
   * status indicator that is cumulative BETWEEN multiple levels.
   * All other indicators and level-specific, so they are initialized
   * later when the Levels themselves are initialized.
   *
   * @param scoreText GameText object
   * @param root Group
   */
  public InfoBar(GameText scoreText, Group root) {
    initializeProperties();
    setPosition(0, 0, getSceneSize(), getInfoBarHeight());
    setFill(getInfoBarColor());
    this.root = root;
    this.scoreText = scoreText;
    this.root.getChildren().add(this);
  }

  private void setPosition(int x, int y, int width, int height) {
    setX(x);
    setY(y);
    setWidth(width);
    setHeight(height);
  }

  /***
   * For retrieving constants form properties file.
   */
  void initializeProperties() {
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
   * Called within Level to initialize level-specific status indicators.
   * @param pauseText GameText reference to pauseText object
   * @param livesText GameText reference to livesText object
   * @param levelText GameText reference to levelText object
   * @param scoreToWinText GameText reference to scoreToWinText object
   */
  public void initializeLevelSpecificText(GameText pauseText, GameText livesText,
      GameText levelText, GameText scoreToWinText) {
    this.levelText = levelText;
    this.scoreToWinText = scoreToWinText;
    this.pauseText = pauseText;
    this.livesText = livesText;
    scoreText.removeText();
    scoreText.addText();
  }

  /***
   * Removes the gameTimerText from the scene.
   */
  public void removeGameTimerText() {
    gameTimer.removeTimerText();
  }

  /***
   * Creates a new GameTimer to keep track of and display
   * the remaining time in the level.
   * @param timeLimit int
   */
  public void setTimeLimit(int timeLimit) {
    this.gameTimer = new GameTimer(root, timeLimit);
  }

  /***
   * Returns time remaining in level
   * @return int
   */
  public int getTimeRemaining() {
    return gameTimer.getTimeRemaining();
  }

  /***
   * Updates PauseText object to display the fact that the
   * game is currently paused.
   */
  public void initiatePauseInText() {
    PauseText subclassPauseText = (PauseText) pauseText;
    subclassPauseText.startPause();
    pauseText = subclassPauseText;
    gameTimer.pauseTimer();
  }

  /***
   * Updates PauseText object to display the fact that the
   * game is currently unpaused.
   */
  public void initiateUnpauseInText() {
    PauseText subclassPauseText = (PauseText) pauseText;
    subclassPauseText.endPause();
    pauseText = subclassPauseText;
    gameTimer.unpauseTimer();
  }

  /***
   * Resets the pause text for a new level, allowing
   * the pause text to be displayed as "click space to start"
   * @param gameRoot Group
   */
  public void resetPauseText(Group gameRoot) {
    pauseText.removeText();
    pauseText = new PauseText(gameRoot);
    gameTimer.pauseTimer();
  }

  /***
   * Removes all level-specific status
   * indicator text from the Scene.
   */
  public void removeAllLevelSpecificText() {
    livesText.removeText();
    pauseText.removeText();
    levelText.removeText();
    scoreToWinText.removeText();
    gameTimer.removeTimerText();
  }

  /***
   * Casts GameText to a livesText subclass to update
   * the integer value of lives.
   * @param lives int
   */
  public void updateLivesText(int lives) {
    LivesText subclassLivesText = (LivesText) livesText;
    subclassLivesText.updateValue(lives);
    livesText = subclassLivesText;
  }

  /***
   * Casts GameText to a scoreText subclass to update
   * the integer value of score.
   * @param newScore int
   */
  public void updateScoreText(int newScore) {
    StatusText subclassUpdateValueText = (StatusText) scoreText;
    subclassUpdateValueText.updateValue(newScore);
    scoreText = subclassUpdateValueText;
  }

  /***
   * removes score text from scene
   */
  public void removeScoreText() {
    scoreText.removeText();
  }

  /***
   * Returns the gameLivesText object for unit testing.
   * @return LivesText object for the Level
   */
  public GameText getLivesText() {
    return livesText;
  }

  /***
   * Returns levelText object for infobar.
   * @return levelText
   */
  public GameText getLevelText() {
    return levelText;
  }

  /***
   * Returns the gamePauseText object.
   * @return PauseText object for the Level
   */
  public GameText getPauseText() {
    return pauseText;
  }

  /***
   * Returns scoreText object.
   * @return ScoreText object for Level.
   */
  public GameText getScoreText() {
    return scoreText;
  }

  /***
   * returns whether gameTimer has reached 0
   * @return true if there is no remaining time left
   */
  public boolean timeIsUp() {
    return gameTimer.timeIsUp();
  }

  /***
   * Returns gameTimer for the level
   * @return gameTimer
   */
  public GameTimer getGameTimer() {
    return gameTimer;
  }

  /***
   * Casts GameText to a scoreToWinText subclass to update the integer
   * value of scoreToWin.
   * @param newScore int
   */
  public void setScoreToWinText(int newScore) {
    StatusText subclassUpdateValueText = (StatusText) scoreToWinText;
    subclassUpdateValueText.updateValue(newScore);
    scoreToWinText = subclassUpdateValueText;
  }

  // properties accessors//
  private int getInfoBarHeight() {
    return Integer.parseInt(properties.getProperty("info_bar_height"));
  }

  private Paint getInfoBarColor() {
    return Paint.valueOf(properties.getProperty("info_bar_color"));
  }

  private int getSceneSize() {
    return Integer.parseInt(properties.getProperty("scene_size"));
  }
}

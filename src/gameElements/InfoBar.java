package gameElements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import breakout.Game;
import text.GameOverText;
import text.GameText;
import text.LevelText;
import text.LivesText;
import text.PauseText;
import text.ScoreText;
import text.StatusText;

public class InfoBar extends Rectangle {

  private Properties properties;
  private GameText pauseText;
  private GameText scoreText;
  private GameText livesText;
  private GameText levelText;
  private GameText scoreToWinText;
  private GameTimer gameTimer;
  private Group root;

  public InfoBar(GameText scoreText,Group root) {
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

  void initializeProperties() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }
  // FIXME
  public void initializeLevelSpecificText(GameText pauseText, GameText livesText, GameText levelText, GameText scoreToWinText) {
    this.levelText = levelText;
    this.scoreToWinText = scoreToWinText;
    this.pauseText = pauseText;
    this.livesText = livesText;
    scoreText.removeText();
    scoreText.addText();
  }

  public void removeGameTimerText() {
    gameTimer.removeTimerText();
  }

  public void setTimeLimit(int timeLimit) {
    this.gameTimer = new GameTimer(root, timeLimit);
  }

  public int getTimeRemaining() {
    return gameTimer.getTimeRemaining();
  }

  public void initiatePauseInText() {
    PauseText subclassPauseText = (PauseText) pauseText;
    subclassPauseText.startPause();
    pauseText = subclassPauseText;
    gameTimer.pauseTimer();
    // setIsPaused = true
  }

  public void initiateUnpauseInText() {
    PauseText subclassPauseText = (PauseText) pauseText;
    subclassPauseText.endPause();
    pauseText = subclassPauseText;
    gameTimer.unpauseTimer();
  }

  public void resetPauseText(Group gameRoot) {
    pauseText.removeText();
    pauseText = new PauseText(gameRoot);
    gameTimer.pauseTimer();
    // setIsPaused = true
  }

  public void removeAllLevelSpecificText() {
    livesText.removeText();
    pauseText.removeText();
    levelText.removeText();
    scoreToWinText.removeText();
    gameTimer.removeTimerText();
  }

  public void updateLivesText(int lives) {
    LivesText subclassLivesText = (LivesText) livesText;
    subclassLivesText.updateValue(lives);
    livesText = subclassLivesText;
  }

  public void updateScoreText(int newScore) {
    StatusText subclassUpdateValueText = (StatusText) scoreText;
    subclassUpdateValueText.updateValue(newScore);
    scoreText = subclassUpdateValueText;
  }

  public void removeScoreText() {
    scoreText.removeText();
  }

  /***
   * Returns the gameLivesText object for unit testing.
   * @return LivesText object for the Level
   */
  public GameText getLivesText() { return livesText; }

  public GameText getLevelText() {
    return levelText;
  }

  /***
   * Returns the gamePauseText object for unit testing.
   * @return PauseText object for the Level
   */
  public GameText getPauseText() { return pauseText; }

  public GameText getScoreText() {
    return scoreText;
  }

  public boolean timeIsUp() {
    return gameTimer.timeIsUp();
  }
  private int getInfoBarHeight() { return Integer.parseInt(properties.getProperty("info_bar_height")); }
  private Paint getInfoBarColor() { return Paint.valueOf(properties.getProperty("info_bar_color")); }
  private int getSceneSize() { return Integer.parseInt(properties.getProperty("scene_size")); }

  public GameTimer getGameTimer() {
    return gameTimer;
  }

  public GameText getScoreToWinText() {
    return scoreToWinText;
  }

  public void setScoreToWinText(int newScore) {
    StatusText subclassUpdateValueText = (StatusText) scoreToWinText;
    subclassUpdateValueText.updateValue(newScore);
    scoreToWinText = subclassUpdateValueText;
  }

  public void removeInfoBar() {
    root.getChildren().remove(this);
  }
}

package gameElements;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import breakout.Game;
import text.GameText;
import text.LivesText;
import text.PauseText;
import text.StatusText;

public class InfoBar extends Rectangle {

  public static final int INFO_BAR_HEIGHT = Game.SCENE_SIZE-Game.PLAYABLE_AREA_SIZE;
  public static final Color INFO_BAR_COLOR = Color.ORCHID;

  private GameText pauseText;
  private GameText scoreText;
  private GameText livesText;
  private GameText levelText;
  private Group root;
  public InfoBar(GameText scoreText,Group root) {
    super(0,0,Game.SCENE_SIZE,INFO_BAR_HEIGHT);
    setFill(INFO_BAR_COLOR);
    root.getChildren().add(this);
    this.scoreText = scoreText;
    this.root = root;
  }

  public void initializeLevelSpecificText(GameText pauseText, GameText livesText, GameText levelText) {
    this.pauseText = pauseText;
    this.livesText = livesText;
    this.levelText = levelText;
  }

  public void initiatePauseInText() {
    PauseText subclassPauseText = (PauseText) pauseText;
    subclassPauseText.startPause();
    pauseText = subclassPauseText;
  }

  public void initiateUnpauseInText() {
    PauseText subclassPauseText = (PauseText) pauseText;
    subclassPauseText.endPause();
    pauseText = subclassPauseText;
  }

  public void resetPauseText(Group gameRoot) {
    pauseText.removeText();
    pauseText = new PauseText(gameRoot);
  }

  public void removeAllLevelSpecificText() {
    livesText.removeText();
    pauseText.removeText();
    levelText.removeText();
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
}

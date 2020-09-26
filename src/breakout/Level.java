package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.BlockConfiguration;
import gameElements.Paddle;
import gameElements.PowerUp;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import text.GameText;
import text.LivesText;
import text.PauseText;
import text.ScoreText;
import text.StatusText;

/***
 * Maintains gameElements for a single level of the game. Handles removal and addition of gameElements
 * to the Group. Handles pausing/unpausing the game, maintains the number of lives left in the level,
 * maintains an instance of Ball and Paddle, and maintains blocks on the screen from a BlockConfiguration.
 */
public class Level {

  public static final int INITIAL_NUMBER_LIVES = 3;

  private int levelLives;
  private int levelNumber;
  private int prevBallScore;
  private BlockConfiguration levelConfiguration;
  private boolean gameIsPaused;
  private GameText gamePauseText;
  private GameText gameLivesText;
  private Group gameRoot;
  private Ball gameBall; // TODO extension: List<Ball> myBalls, to accomodate multi-gameBall powerups
  private Paddle gamePaddle;

  /***
   * Constructor for giving a filename directly for the Level.
   * Takes Group as an argument since this class directly handles
   * adding and removing nodes from the Scene, making it easier to add
   * new features to Game without having to worry about how they will affect
   * removal or addition of nodes in Game.
   * @param gameRootArg Group root for a scene
   * @param gameName String of the directory of the Game being run within the data folder
   * @param fileName String of the filename of the Level to instantiate
   */
  public Level(Group gameRootArg, String gameName, String fileName) {
    this.levelConfiguration = new BlockConfiguration(gameName, fileName, this);
    this.levelNumber = 0;
    this.prevBallScore = 0;

    initializeLevelProperties(gameRootArg);
  }

  /***
   * Constructor for giving a level number directly for the Level.
   * Takes Group as an argument since this class directly handles
   * adding and removing nodes from the Scene, making it easier to add
   * new features to Game without having to worry about how they will affect
   * removal or addition of nodes in Game.
   *
   * Assumes the first level has the number "1" in its file name, and that no other levels have
   * the number 1 in the file name.
   * @param gameRootArg Group root for a scene
   * @param levelNumber Number of the level to instantiate
   */
  public Level(Group gameRootArg, String gameName, int levelNumber) {
    this.levelConfiguration = new BlockConfiguration();
    this.levelConfiguration.setLevel(this);
    this.levelNumber = levelNumber;
    this.gameRoot = gameRootArg;

    generateLevelConfiguration(gameName, levelNumber);
    initializeLevelProperties(gameRootArg);
  }

  private void initializeLevelProperties(Group gameRootArg) {
    this.gameIsPaused = true;
    this.levelConfiguration.updateConfiguration(Game.SCENE_SIZE, Game.SCENE_SIZE);
    System.out.println("Level has " + levelConfiguration.getNumberOfBlocksRemaining() + " blocks");
  }

  private void generateLevelConfiguration(String gameName, int levelNumber) {
    String fileName = "level_" + levelNumber; // TODO
    BlockConfiguration configuration = new BlockConfiguration(gameName, fileName, this);
    this.levelConfiguration = configuration;
  }

  /***
   * Display the current level on the screen
   */
  public void showLevel() {
    this.gameLivesText = new LivesText(getLives(),gameRoot);
    this.gamePauseText = new PauseText(gameRoot);

    setLives(INITIAL_NUMBER_LIVES);
    initializeNewBallAndPaddle();
    addBlocksToRoot();
  }

  public void addBlocksToRoot() {
    List<Block> allBlocks = levelConfiguration.getBlocksAsList();
    for (Block block : allBlocks) {
      if (!gameRoot.getChildren().contains(block)) gameRoot.getChildren().add(block);
    }
  }

  /***
   * Handles updating lives, ball and paddle position, and text when the Level
   * needs to be reset.
   *
   * Call this when the ball touches the ground and there are still lives left, or
   * when pressing the "r" cheat key.
   */
  void resetCurrentLevel() {
    prevBallScore += gameBall.getScore();
    decreaseLivesByOne();
    resetPosition();
  }

  /***
   * Returns whether the ball's position is not touching the ground, meaning
   * it is in a position that allows the game to continue. Also updates
   * the coordinates of the ball.
   *
   * Should be called by step in Game.
   * @param elapsedTime time elapsed in a single step
   * @return true if ball is valid and Level should continue
   */
  boolean isBallValid(double elapsedTime) {
    return gameBall.updateCoordinatesAndContinue(elapsedTime, gameIsPaused);
  }

  /***
   * Handles key input relating to cheat keys and paddle control.
   * @param code KeyCode input by player
   */
  void handleKeyInput(KeyCode code) {
    gamePaddle.handleKeyInput(code, gameIsPaused);
    if(code == KeyCode.SPACE) {
      handleSpaceBarInput();
    }
    else if(code == KeyCode.R) {
      resetPosition();
    }
    else if(code == KeyCode.L) {
      setLives(levelLives+1);
    }
  }

  private void handleSpaceBarInput() {
    if(gameIsPaused) {
      unpauseGame();
    }
    else {
      pauseGame();
    }
  }

  private void pauseGame() {
    PauseText subclassPauseText = (PauseText) gamePauseText;
    subclassPauseText.startPause();
    gamePauseText = subclassPauseText;

    gameIsPaused = true;
  }

  private void unpauseGame() {
    PauseText subclassPauseText = (PauseText) gamePauseText;
    subclassPauseText.endPause();
    gamePauseText = subclassPauseText;

    gameIsPaused = false;
  }

  private void resetPosition() {
    gameIsPaused = true;
    gamePauseText.removeText();
    gamePauseText = new PauseText(gameRoot);

    resetBallAndPaddle();
  }

  private void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(gameRoot);
    gameBall = new Ball (gameRoot, gamePaddle, this);
  }

  private void resetBallAndPaddle() {
    gamePaddle.removePaddle();
    gameBall.removeBall();
    initializeNewBallAndPaddle();
  }

  void dropFoundPowerUps(double elapsedTime) {
    List<PowerUp> powerUps = levelConfiguration.getVisiblePowerUps();
    for (PowerUp fallingPowerUp: powerUps) {
      fallingPowerUp.updateLocation(elapsedTime, gameIsPaused);
      if (gamePaddle.isTouchingPaddleTop(fallingPowerUp)){
        fallingPowerUp.setPaddle(gamePaddle);
        fallingPowerUp.setGameBall(gameBall);
        fallingPowerUp.givePowerUp();
      }
    }
  }

  void updatePositionMovingBlocks(double elapsedTime) {
    List<Block> movingBlocks = levelConfiguration.getMovingBlocks();
    for (Block movingBlock : movingBlocks) {
      movingBlock.updateLocationAndVelocity(elapsedTime, gameIsPaused);
    }
  }

  /***
   * Called by Game class to remove all nodes from the current Level
   * from the root. This effectively removes the entire Level from
   * the Scene.
   */
  void removeLevel() {
    gameLivesText.removeText();
    gamePauseText.removeText();
    gamePaddle.removePaddle();
    gameBall.removeBall();
    levelConfiguration.removeAllBlocks();
  }

  /***
   * Number of lives left in the Level
   * @return number of lives
   */
  int getLives() {
    return levelLives;
  }

  private void setLives(int lives) {
    levelLives = lives;

    LivesText subclassLivesText = (LivesText) gameLivesText;
    subclassLivesText.updateValue(lives);
    gameLivesText = subclassLivesText;
  }

  // made public for unit testing
  public void decreaseLivesByOne() {
    setLives(levelLives-1);
  }

  /***
   * Returns the gameLivesText object for unit testing.
   * @return LivesText object for the Level
   */
  public GameText getLivesText() { return gameLivesText; }

  /***
   * Returns the gamePauseText object for unit testing.
   * @return PauseText object for the Level
   */
  public GameText getPauseText() { return gamePauseText; }

  /***
   * Returns whether there are 0 blocks left
   * @return true if game is won
   */
  boolean levelIsWon() {
    return (levelConfiguration.getNumberOfBlocksRemaining() == 0);
  }

  /***
   * Returns whether there are 0 lives left
   * @return true if the game is lost
   */
  boolean gameIsLost() {
    return (levelLives == 0);
  }

  /***
   * Returns the level's block configuration
   * @return BlockConfiguration levelConfiguration
   */
  public BlockConfiguration getLevelConfiguration() {
    return levelConfiguration;
  }

  public void setLevelConfiguration(BlockConfiguration blockConfiguration) { this.levelConfiguration = blockConfiguration; }

  /***
   * Returns which level is currently being run
   * @return int level number
   */
  int getLevelNumber() {
    return levelNumber;
  }

  /***
   * Sets the level number that is currently being run
   * @param levelNumber int level number
   */
  void setLevelNumber(int levelNumber) {
    this.levelNumber = levelNumber;
  }

  int getScore() { return prevBallScore + gameBall.getScore(); }
  void increaseBallScore(int points) {
    gameBall.increaseScoreBy(points);
  }

  public Group getGameRoot() { return gameRoot; }
  public void setGameRoot(Group gameRoot) { this.gameRoot = gameRoot; }

  public Paddle getGamePaddle() { return gamePaddle; }
  public void setGamePaddle(Paddle gamePaddle) { this.gamePaddle = gamePaddle; }

  public Ball getGameBall() { return this.gameBall; }
  public void setGameBall(Ball ball) { this.gameBall = ball; }
}

package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.BlockConfiguration;
import gameElements.FilledBlockRow;
import gameElements.GameTimer;
import gameElements.InfoBar;
import gameElements.MovingBlockRow;
import gameElements.Paddle;
import gameElements.PowerUp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import text.GameText;
import text.LevelText;
import text.LivesText;
import text.PauseText;
import text.ScoreToWinText;

/***
 * Maintains gameElements for a single level of the game. Handles removal and addition of gameElements
 * to the Group. Handles pausing/unpausing the game, maintains the number of lives left in the level,
 * maintains an instance of Ball and Paddle, and maintains blocks on the screen from a BlockConfiguration.
 */
public class Level {

  public static final int INITIAL_NUMBER_LIVES = 3;
  public static final String FILE_SOURCE_PATH = "data/";

  private int levelLives;
  private int levelNumber;
  private BlockConfiguration levelConfiguration;
  private boolean gameIsPaused;
  private int scoreToWinLevel;
  private int levelTimeLimit;
  private InfoBar infoBar;
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
  public Level(Group gameRootArg, String gameName, String fileName, InfoBar infoBar) {
    this.levelConfiguration = new BlockConfiguration(gameName, fileName, this);
    this.levelNumber = 0;
    this.gameRoot = gameRootArg;
    this.infoBar = infoBar;

    initializeLevelProperties(gameName);
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
  public Level(Group gameRootArg, String gameName, int levelNumber, InfoBar infoBar) {
    this.levelConfiguration = new BlockConfiguration();
    this.levelConfiguration.setLevel(this);
    this.levelNumber = levelNumber;
    this.gameRoot = gameRootArg;
    this.infoBar = infoBar;

    generateLevelConfiguration(gameName, levelNumber);
    initializeLevelProperties(gameName);
  }

  private void initializeLevelProperties(String gameName) {
    this.gameIsPaused = true;
    this.levelConfiguration.updateConfiguration(Game.PLAYABLE_AREA_SIZE, Game.SCENE_SIZE);
    readLevelScoresAndTimeFile(getFileNameOfScoreToWinAndLivesFile(gameName));
    System.out.println("Level has " + levelConfiguration.getNumberOfBlocksRemaining() + " blocks");
  }

  public String generateFilePathForFile(String gameName, String fileName) {
    return FILE_SOURCE_PATH + gameName + "/" + fileName + ".txt";
  }

  private String getFileNameOfScoreToWinAndLivesFile(String gameName) {
    try {
      //following line to list files in directory from http://zetcode.com/java/listdirectory/
      Stream filesInGame = Files.list(new File(FILE_SOURCE_PATH + gameName).toPath());

      Object[] filesInGameArray = filesInGame.toArray();

      for(Object filePath:filesInGameArray) {
        if (!stringContainsNumeric(filePath.toString())) {
          return filePath.toString();
        }
      }
      throw new IOException();
    }
    catch(IOException e) {
      throw new IllegalArgumentException("Invalid argument given for gameName or filenames.");
    }
  }

  private boolean stringContainsNumeric(String s) {
    for(int index=0;index<Game.NUMERICS.length;index++) {
      if(s.contains(Game.NUMERICS[index])) {
        return true;
      }
    }
    return false;
  }

  private void generateLevelConfiguration(String gameName, int levelNumber) {
    String fileName = "level_" + levelNumber; // TODO
    BlockConfiguration configuration = new BlockConfiguration(gameName, fileName, this);
    this.levelConfiguration = configuration;
  }

  private void readLevelScoresAndTimeFile(String filePath) {
    try {
      File scoresAndTimeFile = new File(filePath);
      Scanner fileReader = new Scanner(scoresAndTimeFile);
      String fileLine="";
      for (int i = 0; i < levelNumber; i++) {
        fileLine = fileReader.nextLine();
      }
      String[] scoresAndTime = fileLine.split(" ");
      scoreToWinLevel=Integer.parseInt(scoresAndTime[0]);
      levelTimeLimit=Integer.parseInt(scoresAndTime[1]);
    }
    catch (FileNotFoundException e) {
      throw new IllegalArgumentException("No file provided with scores to win level and time limits.");
    }
    catch (PatternSyntaxException e) {
      throw new IllegalArgumentException("Invalid number of levels provided in file.");
    }
    catch(NoSuchElementException e) {
      throw new IllegalArgumentException("Invalid number of levels provided in file.");
    }
  }

  /***
   * Display the current level on the screen
   */
  public void showLevel() {
    GameText gameLivesText = new LivesText(getLives(),gameRoot);
    GameText gamePauseText = new PauseText(gameRoot);
    GameText gameLevelText = new LevelText(getLevelNumber(),gameRoot);
    GameText gameScoreToWinText = new ScoreToWinText(scoreToWinLevel,gameRoot);
    infoBar.initializeLevelSpecificText(gamePauseText,gameLivesText,gameLevelText,gameScoreToWinText);
    infoBar.setTimeLimit(levelTimeLimit);

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
  void handleKeyInputOnEndScreen(KeyCode code) {

  }

  void handleKeyInputDuringGame(KeyCode code) {
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
    else if(code == KeyCode.D) {
      removeFirstBlock();
    }
    else if(code == KeyCode.P) {
      dropFirstPowerUp();
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
    infoBar.initiatePauseInText();
    gameIsPaused = true;
  }

  private void unpauseGame() {
    infoBar.initiateUnpauseInText();
    gameIsPaused = false;
  }

  private void resetPosition() {
    gameIsPaused = true;
    infoBar.resetPauseText(gameRoot);
    resetBallAndPaddle();
  }

  private void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(gameRoot);
    gameBall = new Ball (gameRoot, gamePaddle, this);
  }

  private void resetBallAndPaddle() {
    int oldScore = gameBall.getScore();
    gamePaddle.removePaddle();
    gameBall.removeBall();
    initializeNewBallAndPaddle();
    gameBall.setScore(oldScore);
  }

  //define first block as the block that exists farthest to the top left. This block should have a
  //hardness greater than 0, meaning it still exists on the screen.
  private void removeFirstBlock() {
    int indexFirstHardnessZeroBlock = -1;
    Block firstHardnessZeroBlock = new Block();
    while(firstHardnessZeroBlock.getBlockHardness() == 0) {
      indexFirstHardnessZeroBlock++;
      List<Block> currentBlockList = levelConfiguration.getBlocksAsList();
      firstHardnessZeroBlock = currentBlockList.get(indexFirstHardnessZeroBlock);
    }
    levelConfiguration.removeBlockFromConfiguration(firstHardnessZeroBlock);
    firstHardnessZeroBlock.setBlockHardness(0);
  }

  private void dropFirstPowerUp() {
    Block firstPowerUpBlock = getFirstBlockWithPowerUp();
    if(firstPowerUpBlock!=null) {
      firstPowerUpBlock.releasePowerUp();
      firstPowerUpBlock.setHasReleasedPowerUp(true);
    }
  }

  //define first block as the block that exists farthest to the top left.
  private Block getFirstBlockWithPowerUp() {
    int indexFirstPowerUpBlock = -1;
    Block firstPowerUpBlock = new Block();
    boolean indexInRange = indexFirstPowerUpBlock<levelConfiguration.getNumberOfBlocksRemaining()-1;
    while(!firstPowerUpBlock.hasPowerUp() && indexInRange) {
      indexFirstPowerUpBlock++;
      List<Block> currentBlockList = levelConfiguration.getBlocksAsList();
      firstPowerUpBlock = currentBlockList.get(indexFirstPowerUpBlock);
      indexInRange = indexFirstPowerUpBlock<levelConfiguration.getNumberOfBlocksRemaining()-1;
    }
    if(firstPowerUpBlock.hasPowerUp()) {
      return firstPowerUpBlock;
    }
    return null;
  }

  private void dropSinglePowerUp(PowerUp fallingPowerUp, double elapsedTime) {
    fallingPowerUp.updateLocation(elapsedTime, gameIsPaused);
    if (gamePaddle.isTouchingPaddleTop(fallingPowerUp)) {
      fallingPowerUp.setPaddle(gamePaddle);
      fallingPowerUp.setGameBall(gameBall);
      fallingPowerUp.givePowerUp();
    }
  }

  void dropFoundPowerUps(double elapsedTime) {
    List<PowerUp> powerUps = levelConfiguration.getVisiblePowerUps();
    for (PowerUp fallingPowerUp: powerUps) {
      dropSinglePowerUp(fallingPowerUp,elapsedTime);
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
    infoBar.removeAllLevelSpecificText();
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
    infoBar.updateLivesText(lives);
  }

  // made public for unit testing
  public void decreaseLivesByOne() {
    setLives(levelLives-1);
  }

  /***
   * Returns whether there are 0 blocks left
   * @return true if game is won
   */
  boolean allBlocksBrokenInLevel() {
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

  int getScore() { return gameBall.getScore(); }
  void increaseBallScore(int points) {
    gameBall.increaseScoreBy(points);
  }

  public Group getGameRoot() { return gameRoot; }
  public void setGameRoot(Group gameRoot) { this.gameRoot = gameRoot; }

  public Paddle getGamePaddle() { return gamePaddle; }
  public void setGamePaddle(Paddle gamePaddle) { this.gamePaddle = gamePaddle; }

  public Ball getGameBall() { return this.gameBall; }
  public void setGameBall(Ball ball) { this.gameBall = ball; }

  public InfoBar getInfoBar() {
    return infoBar;
  }

  public int getScoreToWinLevel() {return scoreToWinLevel;}

  public int getLevelTimeLimit() {
    return levelTimeLimit;
  }

  public void setLevelTimeLimit(int newTimeLimit) {
    levelTimeLimit = newTimeLimit;
  }

}

package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.BlockConfiguration;
import gameElements.InfoBar;
import gameElements.Paddle;
import gameElements.PowerUp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
 * Handles level-specific cheat keys and keeps track of lives, pausing, time limit, and score to win the level.
 *
 * @author Katherine Barbano
 * @author features added by Anna Diemel
 */
public class Level {

  public static final int INITIAL_NUMBER_LIVES = 3;
  public static final String FILE_SOURCE_PATH = "data/";
  public static final int ADDITIONAL_SECONDS = 10;
  public static final int DECREMENT_POINTS = 10;

  private int levelLives;
  private int levelNumber;
  private BlockConfiguration levelConfiguration;
  private boolean gameIsPaused;
  private int scoreToWinLevel;
  private int levelTimeLimit;
  private InfoBar infoBar;
  private Group gameRoot;
  private Ball gameBall;
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
    this.levelConfiguration.updateConfiguration(levelConfiguration.getPlayableArea(),
        levelConfiguration.getSceneSize());
    readLevelScoresAndTimeFile(getFileNameOfScoreToWinAndLivesFile(gameName));
  }

  /***
   * Given a gameName and filename, returns a String corresponding
   * to the path.
   * @param gameName name of directory storing the game levels, for example, sample_game
   * @param fileName name of the file, for example, level_1.txt
   * @return file path string
   */
  public String generateFilePathForFile(String gameName, String fileName) {
    return FILE_SOURCE_PATH + gameName + "/" + fileName + ".txt";
  }

  private String getFileNameOfScoreToWinAndLivesFile(String gameName) {
    try {
      //following line to list files in directory from http://zetcode.com/java/listdirectory/
      Stream filesInGame = Files.list(new File(FILE_SOURCE_PATH + gameName).toPath());

      Object[] filesInGameArray = filesInGame.toArray();

      for (Object filePath : filesInGameArray) {
        if (!stringContainsNumeric(filePath.toString())) {
          return filePath.toString();
        }
      }
      throw new IOException();
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid argument given for gameName or filenames.");
    }
  }

  private boolean stringContainsNumeric(String s) {
    for (int index = 0; index < Game.NUMERICS.length; index++) {
      if (s.contains(Game.NUMERICS[index])) {
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
      String fileLine = "";
      for (int i = 0; i < levelNumber; i++) {
        fileLine = fileReader.nextLine();
      }
      String[] scoresAndTime = fileLine.split(" ");
      scoreToWinLevel = Integer.parseInt(scoresAndTime[0]);
      levelTimeLimit = Integer.parseInt(scoresAndTime[1]);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(
          "No file provided with scores to win level and time limits.");
    } catch (PatternSyntaxException e) {
      throw new IllegalArgumentException("Invalid number of levels provided in file.");
    } catch (NoSuchElementException e) {
      throw new IllegalArgumentException("Invalid number of levels provided in file.");
    }
  }

  /***
   * Display the current level on the screen. Initialize
   * gameElement objects like ball, paddle, blocks, and level-specific
   * GameText.
   */
  public void showLevel() {
    GameText gameLivesText = new LivesText(getLives(), gameRoot);
    GameText gamePauseText = new PauseText(gameRoot);
    GameText gameLevelText = new LevelText(getLevelNumber(), gameRoot);
    GameText gameScoreToWinText = new ScoreToWinText(scoreToWinLevel, gameRoot);
    infoBar.initializeLevelSpecificText(gamePauseText, gameLivesText, gameLevelText,
        gameScoreToWinText);
    infoBar.setTimeLimit(levelTimeLimit);

    setLives(INITIAL_NUMBER_LIVES);
    initializeNewBallAndPaddle();
    addBlocksToRoot();
  }

  /***
   * Add all blocks from level configuration to the Scene.
   */
  public void addBlocksToRoot() {
    List<Block> allBlocks = levelConfiguration.getBlocksAsList();
    for (Block block : allBlocks) {
      if (!gameRoot.getChildren().contains(block)) {
        gameRoot.getChildren().add(block);
      }
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
   * Handles the stepping driven by Game for the current game level.
   * Updates the ball's, the blocks', and the power ups' positions
   * and velocity as dictated by gameplay.
   * @param elapsedTime describes the amount of time represented by this
   *                    step and determines distance traveled.
   */
  void step(double elapsedTime) {
    Block touchedBlock = gameBall.getBlockBallIsTouching();
    boolean ballIsValid = gameBall.updateCoordinatesAndContinue(elapsedTime, gameIsPaused);
    if (!ballIsValid) {
      resetCurrentLevel();
    }
    levelConfiguration.updateBlocks(elapsedTime, touchedBlock, gameIsPaused);
    levelConfiguration.updateVisiblePowerUps(elapsedTime, gameIsPaused);
  }

  /***
   * Handles behavior when a level-specific cheat key is pressed.
   * @param code KeyCode corresponding to the cheat key that is pressed
   */
  void handleKeyInputDuringGame(KeyCode code) {
    gamePaddle.handleKeyInput(code, gameIsPaused);
    if (code == KeyCode.SPACE) {
      handleSpaceBarInput();
    } else if (code == KeyCode.R) {
      resetPosition();
    } else if (code == KeyCode.L) {
      setLives(levelLives + 1);
    } else if (code == KeyCode.D) {
      removeFirstBlock();
    } else if (code == KeyCode.P) {
      dropFirstPowerUp();
    } else if (code == KeyCode.K) {
      dropAllPowerUps();
    } else if (code == KeyCode.T) {
      addExtraTime();
    } else if (code == KeyCode.S) {
      decreaseScoreToWin();
    }
  }

  void handleKeyInputOnEndScreen(KeyCode keyCode) {

  }

  private void handleSpaceBarInput() {
    if (gameIsPaused) {
      unpauseGame();
    } else {
      pauseGame();
    }
  }

  private void pauseGame() {
    infoBar.initiatePauseInText();
    setGameIsPaused(true);
  }

  private void unpauseGame() {
    infoBar.initiateUnpauseInText();
    setGameIsPaused(false);
  }

  private void resetPosition() {
    setGameIsPaused(true);
    infoBar.resetPauseText(gameRoot);
    resetBallAndPaddle();
  }

  private void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(gameRoot);
    gameBall = new Ball(gameRoot, gamePaddle, this);
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
    while (firstHardnessZeroBlock.getBlockHardness() == 0) {
      indexFirstHardnessZeroBlock++;
      List<Block> currentBlockList = levelConfiguration.getBlocksAsList();
      firstHardnessZeroBlock = currentBlockList.get(indexFirstHardnessZeroBlock);
    }
    levelConfiguration.removeBlockFromConfiguration(firstHardnessZeroBlock);
    firstHardnessZeroBlock.setBlockHardness(0);
  }

  private void dropFirstPowerUp() {
    Block firstPowerUpBlock = getFirstBlockWithPowerUp();
    if (firstPowerUpBlock != null) {
      levelConfiguration.releasePowerUpInBlock(firstPowerUpBlock);
    }
  }

  /***
   * Returns the first block in BlockConfiguration, that is, the one
   * farthest to the upper left, that has a power up stored inside.
   * @return Block
   */
  public Block getFirstBlockWithPowerUp() {
    for (Block block : levelConfiguration.getBlocksAsList()) {
      if (block.hasPowerUp()) {
        return block;
      }
    }
    return null;
  }

  private void dropAllPowerUps() {
    for (Block block : levelConfiguration.getBlocksAsList()) {
      if (block.hasPowerUp()) {
        levelConfiguration.releasePowerUpInBlock(block);
      }
    }
  }

  private void addExtraTime() {
    int timeLeft = infoBar.getTimeRemaining();
    infoBar.removeGameTimerText();
    infoBar.setTimeLimit(timeLeft + ADDITIONAL_SECONDS);
    if (!gameIsPaused) {
      infoBar.initiateUnpauseInText();
    }
  }

  private void decreaseScoreToWin() {
    if (scoreToWinLevel > 0) {
      scoreToWinLevel -= DECREMENT_POINTS;
    }
    infoBar.setScoreToWinText(scoreToWinLevel);
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
   * Gets number of lives left in the Level
   * @return number of lives
   */
  int getLives() {
    return levelLives;
  }

  private void setLives(int lives) {
    levelLives = lives;
    infoBar.updateLivesText(lives);
  }

  /***
   * Decreases the number of lives stored in level and
   * displayed by 1
   */
  public void decreaseLivesByOne() {
    setLives(levelLives - 1);
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

  /***
   * Returns which level is currently being run
   * @return int level number
   */
  int getLevelNumber() {
    return levelNumber;
  }

  /***
   * Gets the score of the current ball in the level
   * @return score
   */
  int getScore() {
    return gameBall.getScore();
  }

  /***
   * Increases score of ball by number of points
   * @param points int
   */
  void increaseBallScore(int points) {
    gameBall.increaseScoreBy(points);
  }

  /***
   * returns the group associated with Level
   * @return group
   */
  public Group getGameRoot() {
    return gameRoot;
  }

  /***
   * sets the group associated with level
   * @param gameRoot group
   */
  public void setGameRoot(Group gameRoot) {
    this.gameRoot = gameRoot;
  }

  /***
   * gets the paddle object for the level
   * @return Paddle
   */
  public Paddle getGamePaddle() {
    return gamePaddle;
  }

  /***
   * Gets the ball object for the level
   * @return Ball
   */
  public Ball getGameBall() {
    return this.gameBall;
  }

  /***
   * Gets the InfoBar for the level
   * @return InfoBar
   */
  public InfoBar getInfoBar() {
    return infoBar;
  }

  /***
   * Gets the score necessary to win this level
   * and proceed to the next level by the time the
   * timer runs out.
   * @return int
   */
  public int getScoreToWinLevel() {
    return scoreToWinLevel;
  }

  /***
   * Gets the original time limit of the level
   * before it starts counting down.
   * @return int
   */
  public int getLevelTimeLimit() {
    return levelTimeLimit;
  }

  /***
   * Sets the stored time limit in level
   * and displays time limit on the InfoBar.
   * @param newTimeLimit Int
   */
  public void setLevelTimeLimit(int newTimeLimit) {
    levelTimeLimit = newTimeLimit;
    infoBar.setTimeLimit(newTimeLimit);
  }

  /***
   * Sets the fact that the game is paused.
   * @param isPaused boolean
   */
  public void setGameIsPaused(boolean isPaused) {
    this.gameIsPaused = isPaused;
    if (isPaused == true) {
      for (PowerUp powerUp : getLevelConfiguration().getVisiblePowerUps()) {
        powerUp.removeFromScene();
      }
    }
  }
}

package breakout;

import gameElements.InfoBar;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import text.GameOverText;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import text.GameText;
import text.ScoreText;

/***
 * Handles the game flow over multiple Levels. Creates and initializes the Scene, Group, and
 * Nodes, and handles key input as well as losing and winning. Creates and runs the game through all Levels
 * within a directory in the data folder.
 */
public class Game {

  public static final String PROPERTY_FILE = "src/config.properties";
  public static final String[] NUMERICS = {"1", "2", "3", "4", "5","6","7","8","9","0"};

  private final Scene gameScene;
  private Properties properties;
  private Group gameRoot;

  private List<Level> gameLevels;
  private int totalScore;
  private int currentGameLevelIndex;
  private InfoBar infoBar;
  private GameText gameOverText;

  /***
   * Constructor initializes gameScene and gameRoot, including key inputs,
   * and shows the Scene on the screen.
   * @param stage Stage to set the JavaFX game on
   */
  public Game(Stage stage) {
    getPropertiesList();
    gameScene = setupScene();
    stage.setScene(gameScene);
    stage.setTitle(getTitle());
    stage.show();
  }

  void getPropertiesList() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }

  /***
   * Used in Main.java to start running the Game in an infinite loop.
   * Should not be used for JUnit tests.
   */
  void beginInfiniteLoop() {
    KeyFrame frame = new KeyFrame(Duration.seconds(getSecondDelay()), e -> step(getSecondDelay()));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  /***
   * Handles the actions of the game for a single time step for a given second delay.
   * Handles resetting the game if the player loses the entire game, and handles resetting the
   * current level if the ball touches the ground but there are still lives left.
   * @param elapsedTime second delay
   */
  public void step (double elapsedTime) {
    Level currentLevel = getCurrentGameLevel();
    if (gameIsEnding(currentLevel)) {
      gameOver();
    }
    else if (gameIsContinuingToNextLevel(currentLevel)) {
      resetGameToLevel(currentGameLevelIndex+1);
    }
    else {
      currentLevel.dropFoundPowerUps(elapsedTime);
      currentLevel.updatePositionMovingBlocks(elapsedTime);
      boolean ballIsValid = currentLevel.isBallValid(elapsedTime);
      if (!ballIsValid) {
        currentLevel.resetCurrentLevel();
      }
      currentLevel.monitorBlocks();
    }
    updateGameScore(currentLevel);
  }

  private boolean gameIsEnding(Level currentLevel) {
    boolean scoreTooLowToContinue = infoBar.timeIsUp() && !scoreSurpassedThresholdToContinue(currentLevel);
    return currentLevel.gameIsLost() || gameIsWon() || scoreTooLowToContinue;
  }

  private boolean scoreSurpassedThresholdToContinue(Level level) {
    return totalScore>=level.getScoreToWinLevel();
  }

  private boolean gameIsContinuingToNextLevel(Level currentLevel) {
    boolean scoreHighEnoughToContinue = infoBar.timeIsUp() && scoreSurpassedThresholdToContinue(currentLevel);
    return currentLevel.allBlocksBrokenInLevel() || scoreHighEnoughToContinue;
  }


  public void updateGameScore(Level level) {
    totalScore = level.getScore();
    infoBar.updateScoreText(totalScore);
  }

  /***
   * Sets up the gameRoot, starts the game at the first Level, and sets up event handler for key and mouse inputs
   * @return Scene to be set as the gameScene
   */
  public Scene setupScene() {
    totalScore = 0;
    gameRoot = new Group();
    gameOverText = new GameOverText(gameRoot);
    infoBar = new InfoBar(new ScoreText(totalScore,gameRoot),gameRoot);

    initializeGameLevels();
    resetGameToLevelFirstTime(getLevelOneIndex());

    Scene scene = new Scene(gameRoot, getPlayableAreaSize(), getSceneSize(), getBackgroundColor());
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    scene.setOnMouseClicked(e -> handleMouseInput(e.getButton()));
    return scene;
  }

  private void handleKeyInput(KeyCode code) {
    Level currentLevel = getCurrentGameLevel();
    if(code == KeyCode.SPACE && currentLevel.gameIsLost()) {
      resetGameToLevel(getLevelOneIndex());
    }
    else {
      currentLevel.handleKeyInput(code);
    }
  }

  private void handleMouseInput(MouseButton button) {
    //left mouse click
    if(button == MouseButton.PRIMARY && currentGameLevelIndex!=getLevelOneIndex()) {
      resetGameToLevel(currentGameLevelIndex-1);
    }
    //right mouse click
    else if(button == MouseButton.SECONDARY && currentGameLevelIndex!=gameLevels.size()-1) {
      resetGameToLevel(currentGameLevelIndex+1);
    }
  }

  /***
   * Removes the current Level from the screen because the player lost all their lives or won the game.
   * Shows the "game over" or "you won" screen.
   */
  void gameOver() {
    Level currentLevel = getCurrentGameLevel();
    infoBar.removeScoreText();

    GameOverText subclassGameOverText = (GameOverText) gameOverText;
    subclassGameOverText.gameOverUpdate(gameIsWon());
    gameOverText = subclassGameOverText;

    currentLevel.removeLevel();
  }

  /***
   * Initializes the List of Levels in the game by accessing all level files in the given directory.
   * Assumes the level that comes first has the number "1" in its file name, and that no other levels have
   * the number 1 in the file name, and so on for level 2, 3, etc.
   */
  private void initializeGameLevels() {
    try {
      //following line to list files in directory from http://zetcode.com/java/listdirectory/
      Stream filesInGame = Files.list(new File(Level.FILE_SOURCE_PATH + getGameName()).toPath());

      Object[] filesInGameArray = filesInGame.toArray();
      gameLevels = new ArrayList<>();

      for(Object filePath:filesInGameArray) {
        int levelNumber = getLevelNumberFromFileName(filePath.toString());
        if(levelNumber != -1) {
          gameLevels.add(new Level(gameRoot,getGameName(),levelNumber,infoBar));
        }
      }
    }
    catch(IOException e) {
      throw new IllegalArgumentException("Given game name does not exist.");
    }
  }

  //returns -1 if no int existed in filename
  private int getLevelNumberFromFileName(String fileName) {
    for(int index = 0; index < fileName.length(); index++) {
      String letter = fileName.substring(index,index+1);
      if(Arrays.asList(NUMERICS).contains(letter)) {
        return Integer.parseInt(letter);
      }
    }
    return -1;
  }

  private void showCurrentLevel() {
    Level currentLevel = gameLevels.get(currentGameLevelIndex);
    currentLevel.setGameRoot(gameRoot);
    currentLevel.showLevel();
  }

  private boolean indexIsOutOfBounds(int index) {
    return (index < getLevelOneIndex()) || (index > getFinalLevelIndex());
  }

  private boolean gameIsWon() {
    return getCurrentGameLevel().allBlocksBrokenInLevel() && currentGameLevelIndex == gameLevels.size()-1;
  }

  // Getters and setters:
  /***
   * Gets the Level object currently running.
   * @return Level object currently running
   */
  public Level getCurrentGameLevel() {
    if (indexIsOutOfBounds(currentGameLevelIndex)) return gameLevels.get(getLevelOneIndex());
    return gameLevels.get(currentGameLevelIndex);
  }

  /***
   * Resets and shows the level number given in the argument.
   * LevelIndex should be start indexed at 0.
   * @param levelIndex Index in Levels to show
   */
  public void resetGameToLevelFirstTime(int levelIndex) {
    if (indexIsOutOfBounds(levelIndex)) {
      return;
    }
    initializeGameLevels();
    gameOverText.removeText();
    setLevelNumber(levelIndex);
    showCurrentLevel();
    Level newLevel = gameLevels.get(levelIndex);
    newLevel.increaseBallScore(totalScore);
  }

  public void resetGameToLevel(int levelIndex) {
    getCurrentGameLevel().removeLevel();
    resetGameToLevelFirstTime(levelIndex);
  }

  /**
   * Sets the Level object currently running to levelArg.
   * @param levelArg level to be displayed
   * Unit testing purposes only.
   */
  public void setCurrentGameLevel(Level levelArg) {
    gameOverText.removeText();
    levelArg.setGameRoot(gameRoot);
    levelArg.increaseBallScore(totalScore);
    levelArg.showLevel();
  }

  /***
   * Gets the Scene currently running the Game.
   * @return Scene object
   */
  public Scene getScene() { return gameScene; }

  public GameText getGameOverText() {
    return gameOverText;
  }

  public InfoBar getInfoBar() {
    return infoBar;
  }

  /***
   * Gets the Group object currently running the Game.
   * @return Group root object
   */
  public Group getRoot() {
    return gameRoot;
  }
  public void setRoot(Group root) { this.gameRoot = root; }

  /***
   * Get the List maintaining all the Levels in the current Game.
   * @return List of levels in game
   */
  public List<Level> getGameLevelsList() {
    return gameLevels;
  }
  public void setGameLevels(List<Level> levels) { this.gameLevels = levels; }

  /***
   * Sets the index of the level currently running to a new levelNumber.
   * @param levelNumber int of the level number to run
   */
  public void setLevelNumber(int levelNumber) {
    currentGameLevelIndex = levelNumber;
  }
  public int getLevelNumber() { return currentGameLevelIndex; }
  String getTitle() { return properties.getProperty("title"); }
  int getSceneSize() { return Integer.parseInt(properties.getProperty("scene_size"));}
  int getPlayableAreaSize() { return Integer.parseInt(properties.getProperty("playable_area_size"));}
  int getFramesPerSecond() { return Integer.parseInt(properties.getProperty("frames_per_second"));}
  public double getSecondDelay() { return 1.0 / getFramesPerSecond(); }
  Paint getBackgroundColor() { return Paint.valueOf(properties.getProperty("background_color"));}
  String getGameName() { return properties.getProperty("game_name");}
  int getLevelOneIndex() { return Integer.parseInt(properties.getProperty("level_one_index"));}
  int getFinalLevelIndex() { return Integer.parseInt(properties.getProperty("final_level_index"));}
}

package breakout;

import gameElements.BlockConfiguration;
import gameElements.InfoBar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import text.StatusText;

/***
 * Handles the game flow over multiple Levels. Creates and initializes the Scene, Group, and
 * Nodes, and handles key input as well as losing and winning. Creates and runs the game through all Levels
 * within a directory in the data folder.
 */
public class Game {

  public static final String TITLE = "Breakout";
  public static final int SCENE_SIZE = 670;
  public static final int PLAYABLE_AREA_SIZE = 600;
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  public static final Paint BACKGROUND = Color.AZURE;
  //public static final String GAME_NAME = "sample_game_no_powerups";
  public static final String GAME_NAME = "sample_game";
  //public static final String GAME_NAME = "game_for_testing_main";
  public static final String[] NUMERICS = {"1", "2", "3", "4", "5","6","7","8","9","0"};
  public static final int LEVEL_ONE_INDEX = 0;
  public static final int FINAL_LEVEL_INDEX = 2;

  private final Scene gameScene;
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
    gameScene = setupScene();
    stage.setScene(gameScene);
    stage.setTitle(TITLE);
    stage.show();
  }

  /***
   * Used in Main.java to start running the Game in an infinite loop.
   * Should not be used for JUnit tests.
   */
  void beginInfiniteLoop() {
    KeyFrame frame = new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY));
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
    if (currentLevel.gameIsLost() || gameIsWon()) {
      gameOver();
    }
    else if (currentLevel.levelIsWon()) {
      resetGameToLevel(currentGameLevelIndex+1);
    }
    else {
      currentLevel.dropFoundPowerUps(elapsedTime);
      currentLevel.updatePositionMovingBlocks(elapsedTime);
      boolean ballIsValid = currentLevel.isBallValid(elapsedTime);
      if (!ballIsValid) {
        currentLevel.resetCurrentLevel();
      }
    }
    updateGameScore(currentLevel);
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
    resetGameToLevelFirstTime(LEVEL_ONE_INDEX);

    Scene scene = new Scene(gameRoot, PLAYABLE_AREA_SIZE, SCENE_SIZE, BACKGROUND);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    scene.setOnMouseClicked(e -> handleMouseInput(e.getButton()));
    return scene;
  }

  private void handleKeyInput(KeyCode code) {
    Level currentLevel = getCurrentGameLevel();
    if(code == KeyCode.SPACE && currentLevel.gameIsLost()) {
      resetGameToLevel(LEVEL_ONE_INDEX);
    }
    else {
      currentLevel.handleKeyInput(code);
    }
  }

  private void handleMouseInput(MouseButton button) {
    //left mouse click
    if(button == MouseButton.PRIMARY && currentGameLevelIndex!=LEVEL_ONE_INDEX) {
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
      Stream filesInGame = Files.list(new File(BlockConfiguration.FILE_SOURCE_PATH + GAME_NAME).toPath());

      Object[] filesInGameArray = filesInGame.toArray();
      gameLevels = new ArrayList<>();

      for(Object filePath:filesInGameArray) {
        int levelNumber = getLevelNumberFromFileName(filePath.toString());
        gameLevels.add(new Level(gameRoot,GAME_NAME,levelNumber,infoBar));
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
    return (index < LEVEL_ONE_INDEX) || (index > FINAL_LEVEL_INDEX);
  }

  private boolean gameIsWon() {
    return getCurrentGameLevel().levelIsWon() && currentGameLevelIndex == gameLevels.size()-1;
  }

  // Getters and setters:
  /***
   * Gets the Level object currently running.
   * @return Level object currently running
   */
  public Level getCurrentGameLevel() {
    if (indexIsOutOfBounds(currentGameLevelIndex)) return gameLevels.get(LEVEL_ONE_INDEX);
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
}

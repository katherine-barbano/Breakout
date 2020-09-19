package breakout;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import text.GameOverText;
import text.LivesText;
import text.PauseText;

/***
 * Purpose: Contains private field for a BlockConfiguration. Maintains
 * number of lives left. Starts a game if the user hits the space bar. Maintains
 * cheat keys. Ends game if lives run out, or ends the current Level
 * if BlockConfiguration does not contain any more Blocks.
 *
 * Method: endGame, finishLevel, setLives, decreaseLivesByOne
 *         gameIsLost, levelIsWon
 */
public class Level {

  public static final int INITIAL_NUMBER_LIVES = 3;

  private int levelLives;
  private int levelNumber;
  private BlockConfiguration levelConfiguration;
  private final ArrayList<Block> blocksInLevel;
  private boolean gameIsPaused;
  private PauseText gamePauseText;
  private LivesText gameLivesText;
  private Group gameRoot;
  private Ball gameBall; // TODO extension: List<Ball> myBalls, to accomodate multi-gameBall powerups
  private Paddle gamePaddle;

  public Level(Group gameRootArg, String fileName) {
    this.levelLives = INITIAL_NUMBER_LIVES;
    this.levelNumber = 0;
    this.levelConfiguration = new BlockConfiguration(fileName);
    this.blocksInLevel = new ArrayList<>();
    this.gameIsPaused = true;

    gameRoot = gameRootArg;
    gamePauseText = new PauseText(gameRoot);
    gameLivesText = new LivesText(getLives(),gameRoot);

    initializeNewBallAndPaddle();
  }

  public Level(Group gameRootArg, int levelNumber) {
    this.levelLives = INITIAL_NUMBER_LIVES;
    this.levelNumber = levelNumber;
    this.levelConfiguration = new BlockConfiguration();
    this.blocksInLevel = new ArrayList<>();
    generateLevelConfiguration(levelNumber);
    this.gameIsPaused = true;

    gameRoot = gameRootArg;
    gamePauseText = new PauseText(gameRoot);
    gameLivesText = new LivesText(getLives(),gameRoot);

    initializeNewBallAndPaddle();
  }

  private void generateLevelConfiguration(int levelNumber) {
    String fileName = "level_" + levelNumber; // TODO
    BlockConfiguration configuration = new BlockConfiguration(fileName);
    this.levelConfiguration = configuration;
  }

  // FIXME: Do you think this should be in BlockConfiguration instead?
  ArrayList<Block> getAllBlocks(int sceneWidth, int sceneHeight) {
    int blockWidth = sceneWidth / Block.BLOCKS_PER_ROW;
    int blockHeight =
        (sceneHeight - Paddle.VERTICAL_PADDLE_OFFSET_FROM_BOTTOM - Paddle.PADDLE_HEIGHT) / (
            Block.NUMBER_OF_BLOCK_ROWS + 1);
    for (int y = 0; y < Block.NUMBER_OF_BLOCK_ROWS; y++) { // each BlockRow
      for (int x = 0; x < Block.BLOCKS_PER_ROW; x++) { // element in block row
        BlockRow row = levelConfiguration.getBlockRows()[y];
        Block block = row.getRowOfBlocks()[x];
        if (block.getBlockHardness() != 0) {
          block.updatePosition(blockWidth, blockHeight, x * blockWidth, y * blockHeight);
          block.updateBlockColor();
          blocksInLevel.add(block);
        }

      }
    }
    return this.blocksInLevel;
  }

  void resetCurrentLevel() {
    decreaseLivesByOne();
    gameLivesText.updateLives(getLives());
    resetPosition();
  }

  boolean isBallValid(double elapsedTime) {
    return gameBall.updateCoordinatesAndContinue(elapsedTime, gameIsPaused);
  }

  void handleKeyInput(KeyCode code) {
    gamePaddle.handleKeyInput(code, gameIsPaused);
    if(code == KeyCode.SPACE) {
      handleSpaceBarInput();
    }
    else if(code == KeyCode.R) {
      resetPosition();
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

  void resetPosition() {
    gameIsPaused = true;
    gamePauseText.removeText();
    gamePauseText = new PauseText(gameRoot);

    resetBallAndPaddle();
  }

  void pauseGame() {
    gamePauseText.startPause();
    gameIsPaused = true;
  }

  void unpauseGame() {
    gamePauseText.endPause();
    gameIsPaused = false;
  }

  void initializeNewBallAndPaddle() {
    gamePaddle = new Paddle(gameRoot);
    gameBall = new Ball (gameRoot, gamePaddle, getLevelConfiguration());
  }

  void resetBallAndPaddle() {
    gamePaddle.removePaddle();
    gameBall.removeBall();
    initializeNewBallAndPaddle();
  }

  void finishLevel() {
  } // TODO

  void decreaseLivesByOne() {
    levelLives--;
  }

  int getLives() {
    return levelLives;
  }

  void setLives(int lives) {
    levelLives = lives;
  }

  boolean gameIsLost() {
    return (levelLives == 0);
  }

  void updateBlocks(int width, int height) {
    levelConfiguration.updateConfiguration(width, height);
  }

  void updateBlocks(double width, double height) {
    updateBlocks((int) width, (int) height);
  }

  boolean levelIsWon() {
    return (levelConfiguration.getNumberOfBlocksRemaining() == 0);
  }

  BlockConfiguration getLevelConfiguration() {
    return levelConfiguration;
  }

  int getLevelNumber() {
    return levelNumber;
  }

  void setLevelNumber(int levelNumber) {
    this.levelNumber = levelNumber;
  }

  void removeLevel() {
    gameLivesText.removeText();
    gamePauseText.removeText();
    gamePaddle.removePaddle();
    gameBall.removeBall();
    removeBlocks();
  }

  void addBlocks() {
    ArrayList<Block> allBlocks = getAllBlocks(Game.SCENE_SIZE, Game.SCENE_SIZE);
    gameRoot.getChildren().addAll(allBlocks);
  }

  //TODO:add to BlockConfiguration
  private void removeBlocks() {
    ArrayList<Block> allBlocks = getAllBlocks(Game.SCENE_SIZE, Game.SCENE_SIZE);
    for (Block block : allBlocks) gameRoot.getChildren().remove(block);
  }
}

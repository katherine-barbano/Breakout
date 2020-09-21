package breakout;

import gameElements.Ball;
import gameElements.Block;
import gameElements.BlockConfiguration;
import gameElements.BlockRow;
import gameElements.Paddle;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import text.LivesText;
import text.PauseText;

/***
 * Maintains gameElements for a single level of the game. Handles removal and addition of gameElements
 * to the Group. Handles pausing/unpausing the game, maintains the number of lives left in the level,
 * maintains an instance of Ball and Paddle, and maintains blocks on the screen from a BlockConfiguration.
 */
public class Level {

  public static final int INITIAL_NUMBER_LIVES = 3;

  private int levelLives;
  private int levelNumber;
  private BlockConfiguration levelConfiguration;
  private final ArrayList<Block> blocksInLevel;//TODO: is it possible that we can put blocksInLevel into BlockConfiguration?
  private boolean gameIsPaused;
  private PauseText gamePauseText;
  private LivesText gameLivesText;
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
    this.levelConfiguration = new BlockConfiguration(gameName, fileName);
    this.blocksInLevel = new ArrayList<>();
    this.levelNumber = 0;

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
    this.blocksInLevel = new ArrayList<>();
    this.levelNumber = levelNumber;

    generateLevelConfiguration(gameName, levelNumber);
    initializeLevelProperties(gameRootArg);
  }

  private void initializeLevelProperties(Group gameRootArg) {
    this.gameIsPaused = true;
    this.gameRoot = gameRootArg;
    updateBlocks(Game.SCENE_SIZE, Game.SCENE_SIZE);
    addAllBlocksToList(Game.SCENE_SIZE, Game.SCENE_SIZE);
  }

  /***
   * Display the current level on the screen
   */
  void showLevel() {
    this.gameLivesText = new LivesText(getLives(),gameRoot);
    this.gamePauseText = new PauseText(gameRoot);

    setLives(INITIAL_NUMBER_LIVES);
    initializeNewBallAndPaddle();
    addBlocksToRoot();
  }

  private void addBlocksToRoot() {
    List<Block> allBlocks = getAllBlocks();
    gameRoot.getChildren().addAll(allBlocks);
  }

  private void updateBlocks(int width, int height) {
    levelConfiguration.updateConfiguration(width, height);
  }

  private void updateBlocks(double width, double height) {
    updateBlocks((int) width, (int) height);
  }

  private void generateLevelConfiguration(String gameName, int levelNumber) {
    String fileName = "level_" + levelNumber; // TODO
    BlockConfiguration configuration = new BlockConfiguration(gameName, fileName);
    this.levelConfiguration = configuration;
  }

  // FIXME: Do you think this should be in BlockConfiguration instead?
  // yes I think we should put this in BlockConfiguration, and probably also split this into some helper methods
  void addAllBlocksToList(int sceneWidth, int sceneHeight) {
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
  }

  /***
   * Gets a List of all the blocks that have been added
   * to the level from block configuration. Does not
   * add this list to the root yet.
   * @return List of blocks in the level
   */
  List<Block> getAllBlocks() {
    return this.blocksInLevel;
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
    gamePauseText.startPause();
    gameIsPaused = true;
  }

  private void unpauseGame() {
    gamePauseText.endPause();
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
    gameBall = new Ball (gameRoot, gamePaddle, getLevelConfiguration());
  }

  private void resetBallAndPaddle() {
    gamePaddle.removePaddle();
    gameBall.removeBall();
    initializeNewBallAndPaddle();
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
    removeBlocks();
  }

  //TODO: add to BlockConfiguration @Anna
  private void removeBlocks() {
    List<Block> allBlocks = getAllBlocks();
    for (Block block : allBlocks) gameRoot.getChildren().remove(block);
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
    gameLivesText.updateLives(lives);
  }

  private void decreaseLivesByOne() {
    setLives(levelLives-1);
  }

  /***
   * Returns the gameLivesText object for unit testing.
   * @return LivesText object for the Level
   */
  public LivesText getLivesText() { return gameLivesText; }

  /***
   * Returns the gamePauseText object for unit testing.
   * @return PauseText object for the Level
   */
  public PauseText getPauseText() { return gamePauseText; }

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
   * Sets the level number that is currently being run
   * @param levelNumber int level number
   */
  void setLevelNumber(int levelNumber) {
    this.levelNumber = levelNumber;
  }
}

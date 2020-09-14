package breakout;

import java.util.ArrayList;

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
  private ArrayList<Block> blocksInLevel;

  public Level (String fileName) {
    this.levelLives = INITIAL_NUMBER_LIVES;
    this.levelNumber = 0;
    this.levelConfiguration = new BlockConfiguration(fileName);
    this.blocksInLevel = new ArrayList<>();
  }
  public Level(int levelNumber) {
    this.levelLives = INITIAL_NUMBER_LIVES;
    this.levelNumber = levelNumber;
    this.levelConfiguration = new BlockConfiguration();
    this.blocksInLevel = new ArrayList<>();
    generateLevelConfiguration(levelNumber);
  }

  private void generateLevelConfiguration(int levelNumber) {
    String fileName = "level_" + levelNumber; // TODO
    BlockConfiguration configuration = new BlockConfiguration(fileName);
    this.levelConfiguration = configuration;
  }

  // FIXME: Do you think this should be in BlockConfiguration instead?
  ArrayList<Block> getAllBlocks(int sceneWidth, int sceneHeight) {
    int blockWidth = sceneWidth / Block.BLOCKS_PER_ROW;
    int blockHeight = (sceneHeight - Paddle.VERTICAL_PADDLE_OFFSET_FROM_BOTTOM - Paddle.PADDLE_HEIGHT) / (Block.NUMBER_OF_BLOCK_ROWS + 1);
    for (int y = 0; y < Block.NUMBER_OF_BLOCK_ROWS; y++) { // each BlockRow
      for (int x = 0; x < Block.BLOCKS_PER_ROW; x++) { // element in block row
        BlockRow row = levelConfiguration.getBlockRows()[y];
        Block block = row.getRowOfBlocks()[x];
        if (block.getBlockHardness() != 0) {
          block.updatePosition(blockWidth, blockHeight, x*blockWidth, y*blockHeight);
          blocksInLevel.add(block);
        }

      }
    }
    return this.blocksInLevel;
  }

  void endGame() {

  }

  void finishLevel() {

  }

  void setLives(int lives) { levelLives = lives; }
  int getLives() { return levelLives; }
  void decreaseLivesByOne() { levelLives--; }
  boolean gameIsLost() { return (levelLives == 0); }
  boolean levelIsWon() { return (levelConfiguration.getNumberOfBlocksRemaining() == 0); }
  int getLevelNumber() { return levelNumber; }
  void setLevelNumber(int levelNumber) { this.levelNumber = levelNumber; }
  BlockConfiguration getLevelConfiguration() { return levelConfiguration; }
}

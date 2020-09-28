package gameElements;

import breakout.Game;
import breakout.Level;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javafx.scene.Group;

/***
 *      Purpose: Reads in a text file into an array (size 8) of BlockRows
 *      Throws exception if any text file does not provide a valid level.
 *
 *      Methods: readTextFile, setBlockRows
 */
public class BlockConfiguration {

  private Properties properties;
  private Level myLevel;
  private File configFile;
  private BlockRow[] configRows;
  private int numberOfBlocksRemaining;
  private int numberOfPowerUps;

  public BlockConfiguration () { new BlockConfiguration("",""); }
  public BlockConfiguration(String gameName, String fileName) { new BlockConfiguration(gameName, fileName, null); }
  public BlockConfiguration(String gameName, String fileName, Level level) {
    initializeProperties();
    setLevel(level);
    this.configRows = new BlockRow[getNumberOfBlockRows()];
    initializeFromFile(gameName, fileName);
  }

  private void initializeFromFile(String gameName, String fileName) {
    if (!fileName.equals("")) {
      String filePath = myLevel.generateFilePathForFile(gameName, fileName);
      generateBlockRowsFromFile(filePath); // Blocks are dimensionless
    }
  }

  private void initializeProperties() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    }
    catch (FileNotFoundException e) { return;}
    catch (IOException e) {return;}
  }

  void generateBlockRowsFromFile(String filePath) {
    Scanner fileReader;
    try {
      configFile = new File(filePath);
      fileReader = new Scanner(configFile);
    } catch (FileNotFoundException e) { return; }
    for (int i = 0; i < getNumberOfBlockRows(); i++) {
      if (fileReader.hasNextLine()) {
        String fileLine = fileReader.nextLine();
        configRows[i] = generateBlockRowFromFileLine(fileLine);
      }
    }
  }

  private BlockRow generateBlockRowFromFileLine(String fileLine) {
    String[] hardnessArray = fileLine.split(" ");
    Block[] blockArray = new Block[getBlocksPerRow()];
    assert (blockArray.length == hardnessArray.length);

    BlockRow blockRow = new FilledBlockRow();
    for (int i = 0; i < hardnessArray.length; i++) {
      String hardness = hardnessArray[i];
      if (hardness.equals("M")) {
        MovingBlock onlyBlockThisRow = new MovingBlock();
        onlyBlockThisRow.setRandomHardness();
        blockArray[i] = onlyBlockThisRow;
        blockRow = new MovingBlockRow();
      } else {
        hardness = hardnessArray[i];
        Block newBlock = buildBlockWithHardness(hardness);
        blockArray[i] = newBlock;
      }
    }
    blockRow.setRowOfBlocks(blockArray);
    return blockRow;
  }

  Block buildBlockWithHardness(String hardness) {
    Block createdBlock = null;
    char hardnessChar = hardness.charAt(0);
    if (Character.isDigit(hardnessChar)) {
      int hardValue = Integer.parseInt(hardness);
      if (hardValue > 0) {
        createdBlock  = new Block(hardValue);
        numberOfBlocksRemaining++;
      }
    } else {
      createdBlock = makeSpecialBlock(hardnessChar);
    }
    return createdBlock;
  }

  private Block makeSpecialBlock(char hardnessChar) {
    Block createdBlock;
    if (hardnessChar == 'V') {
      createdBlock = new VaryingBlock();
    }
    else {
      PowerUp createdPowerUp = makePowerUp(hardnessChar, myLevel);
      createdBlock = new Block(createdPowerUp);
      numberOfPowerUps++;
    }
    numberOfBlocksRemaining++;
    return createdBlock;
  }

  private PowerUp makePowerUp(char hardnessChar, Level level) {
    Group gameRoot = level.getGameRoot();
    Paddle gamePaddle = level.getGamePaddle();
    PowerUp powerUp;
    switch (hardnessChar) {
      case 'S':
        powerUp = new SlowBallPowerUp(gameRoot, gamePaddle);
        break;
      case 'P':
        powerUp = new PaddlePowerUp(gameRoot, gamePaddle);
        break;
      case 'B':
        powerUp = new BreakerBallPowerUp(gameRoot, gamePaddle);
        break;
      case 'M':
        powerUp = new MovingBlockPowerUp(gameRoot, gamePaddle);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + hardnessChar);
    }
    return powerUp;
  }

  public void findAndDecrementBlock(Block block, Ball ball) {
    Block foundBlock = findBlock(block);
    if (foundBlock == null) return;
    if (ball.isBreakerBall()) {
      removeBlockFromConfiguration(foundBlock);
    }
    else decrementBlock(foundBlock);
  }

  private Block findBlock(Block block) {
    List<Block> blocks = getBlocksAsList();
    for (Block currentBlock : blocks) {
      if (currentBlock.equals(block)) return currentBlock;
    }
    return null;
  }

  public void removeBlockFromConfiguration(Block block) {
    block.removeFromScene();
    decreaseNumberOfBlocksByOne();
  }

  private void decrementBlock(Block block) {
    block.decreaseHardnessByOne();
    if (block.getBlockHardness() == 0) block.removeFromScene();
    block.updateBlockColor();
  }

  /**
   * Used by Level to update its blockConfiguration.
   * @param sceneWidth
   * @param sceneHeight
   */
  public void updateConfiguration(int sceneWidth, int sceneHeight) {
    for (int i = 0; i < configRows.length; i++) {
      BlockRow blockRow = configRows[i];
      if (blockRow == null) continue;
      Block[] blocks = blockRow.getRowOfBlocks();
      for (int j = 0; j < blocks.length; j++) {
        if (blocks[j] == null) continue;
        if (sceneWidth != 0 && sceneHeight != 0) {
          orientBlock(blocks[j], sceneWidth, sceneHeight, i, j);
        }
        blocks[j].updateBlockColor();
      }
    }
  }

  private void orientBlock(Block block, int sceneWidth, int sceneHeight, int i, int j) {
    block.setDimensions(sceneWidth,sceneHeight);
    block.setX(block.getBlockWidth(sceneWidth)*j);
    block.setY(block.getBlockHeight(sceneHeight)*i + getInfoBarHeight());
    block.setDimensionsPowerUp();
  }

  Block getBlock(int row, int col) {
    BlockRow blockRow = getBlockRows()[row];
    Block block = blockRow.getRowOfBlocks()[col];
    return block;
  }

  public List<Block> getBlocksAsList() {
    List<Block> blockList = new ArrayList<>();
    for (BlockRow blockRow : configRows) {
      if (blockRow == null || blockRow.getRowOfBlocks() == null) {
        continue;
      }
      for (Block block : blockRow.getRowOfBlocks()) {
        if (block == null) {
          continue;
        }
        blockList.add(block);
      }
    }
    return blockList;
  }

  public List<PowerUp> getVisiblePowerUps() {
    List<PowerUp> powerUpList = new ArrayList<>();
    List<Block> blockList = getBlocksAsList();
    for (Block block : blockList) {
      if (block != null && block.hasReleasedPowerUp()) {
        powerUpList.add(block.getPowerUp());
      }
    }
    return powerUpList;
  }

  // FIXME: currently unused, might not need
  public List<Block> getMovingBlocks() {
    List<Block> movingBlockList = new ArrayList<>();
    for (Block block : getBlocksAsList()) {
      if (block != null && block instanceof MovingBlock)
        movingBlockList.add(block);
    }
    return movingBlockList;
  }

  public List<Block> getVaryingBlocks() {
    List<Block> varyingBlockList = new ArrayList<>();
    for (Block block : getBlocksAsList()) {
      if (block != null && block instanceof VaryingBlock)
        varyingBlockList.add(block);
    }
    return varyingBlockList;
  }

  // FIXME: also currently unused.
  boolean containsMovingBlock(String fileLine) {
    for (char key : fileLine.toCharArray()) {
      if (key == 'M') return true;
    }
    return false;
  }

  public void removeAllBlocks() {
    List<Block> blockList = getBlocksAsList();
    for (Block block : blockList) {
      block.removeFromScene();
    }
    setNumberOfBlocksRemaining(0);
  }

  public Level getLevel() {
    return myLevel;
  }
  public void setLevel(Level myLevel) { this.myLevel = myLevel; }

  public BlockRow[] getBlockRows() { return configRows; }

  void decreaseNumberOfBlocksByOne() {
    numberOfBlocksRemaining--;
    myLevel.getGameBall().increaseScoreBy(getScoreIncrement()); // FIXME horrid
  }
  void setNumberOfBlocksRemaining(int numberOfBlocksRemaining) { this.numberOfBlocksRemaining = numberOfBlocksRemaining; }
  public int getNumberOfBlocksRemaining() { return numberOfBlocksRemaining; }
  boolean isEmpty() { return (numberOfBlocksRemaining == 0);}
  public int getNumberOfPowerUps() { return numberOfPowerUps; }
  public void setNumberOfPowerUps(int powerUps) { this.numberOfPowerUps = powerUps; }

  private int getInfoBarHeight() { return Integer
      .parseInt(properties.getProperty("info_bar_height")); }
  private int getBlocksPerRow() { return Integer.parseInt(properties.getProperty("blocks_per_row")); }
  private int getNumberOfBlockRows() { return Integer.parseInt(properties.getProperty("number_of_block_rows")); }
  private int getBlockConfigurationOffsetFromPaddle() { return Integer.parseInt(properties.getProperty("block_configuration_offset_from_paddle"));}
  private int getScoreIncrement() { return Integer.parseInt(properties.getProperty("score_increment")); }
  public int getSceneSize() { return Integer.parseInt(properties.getProperty("scene_size"));}
  public int getPlayableArea() { return Integer.parseInt(properties.getProperty("playable_area_size"));}

  public void updateBlockAttributes(double elapsedTime, boolean gameIsPaused) {
    for (Block block : getBlocksAsList()) {
      if (block instanceof VaryingBlock) ((VaryingBlock) block).waitToChangeHardness(elapsedTime, gameIsPaused);
      if (block instanceof MovingBlock) ((MovingBlock) block).updateVelocityAndPosition(elapsedTime, gameIsPaused);
    }
  }
}

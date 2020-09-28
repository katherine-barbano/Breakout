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
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }

  void generateBlockRowsFromFile(String filePath) {
    try {
      configFile = new File(filePath);
      Scanner fileReader = new Scanner(configFile);
      for (int i = 0; i < getNumberOfBlockRows(); i++) {
        if (fileReader.hasNextLine()) {
          String fileLine = fileReader.nextLine();
          if (containsMovingBlock(fileLine)) {
            MovingBlockRow movingBlockRow = convertFileLineToMovingBlockRow(fileLine);
            configRows[i] = movingBlockRow;
          }
          else {
            FilledBlockRow rowForFileLine = convertFileLineToBlockRow(fileLine);
            configRows[i] = rowForFileLine;
          }
        }
      }
    } catch (FileNotFoundException e) {}
  }

  private MovingBlockRow convertFileLineToMovingBlockRow(String fileLine) {
    String[] hardnessArray = fileLine.split(" ");
    Block[] blockArray = new Block[getBlocksPerRow()];
    assert (blockArray.length == hardnessArray.length);

    for (int i = 0; i < hardnessArray.length; i++) {
      String hardness = hardnessArray[i];
      if (hardness.equals("M")) {
        MovingBlock onlyBlockThisRow = new MovingBlock();
        onlyBlockThisRow.setRandomHardness();
        blockArray[i] = onlyBlockThisRow;
      } else {}
      // TODO: this ^ should never happen
    }

    //TODO: can we use polymorphism here by doing BlockRow movingBlockRow = new MovingBlockRow? And cast to subclass to use methods
    MovingBlockRow movingBlockRow = new MovingBlockRow();
    movingBlockRow.setRowOfBlocks(blockArray);
    return movingBlockRow;
  }

  // Assumes that fileRow is of format "X X X ".."X X X"
  private FilledBlockRow convertFileLineToBlockRow(String fileRow) {
    String[] hardnessArray = fileRow.split(" ");
    Block[] blockArray = new Block[getBlocksPerRow()];
    assert (blockArray.length == hardnessArray.length);

    for (int i = 0; i < hardnessArray.length; i++) {
      String hardness = hardnessArray[i];
      Block newBlock = buildBlockWithHardness(hardness);
      blockArray[i] = newBlock;
    }
    FilledBlockRow blockRow = new FilledBlockRow();
    blockRow.setRowOfBlocks(blockArray);
    return blockRow;
  }

  Block buildBlockWithHardness(String hardness) {
    Block createdBlock = null;
    char hardnessChar = hardness.charAt(0);
    if (Character.isDigit(hardnessChar)) {
      int hardValue = Integer.parseInt(hardness);
      if (hardValue > 0) {
        createdBlock  = new Block();
        createdBlock.setBlockHardness(hardValue);
        numberOfBlocksRemaining++;
      }
    } else {
      createdBlock = makeBlockWithPowerUp(hardnessChar);
    }
    return createdBlock;
  }

  private Block makeBlockWithPowerUp(char hardnessChar) {
    Block createdBlock = new Block();
    createdBlock.setRandomHardness();
    PowerUp createdPowerUp = makePowerUp(hardnessChar, myLevel, createdBlock);
    createdBlock.setPowerUp(createdPowerUp);
    createdBlock.setHasPowerUp(true);
    numberOfPowerUps++;
    numberOfBlocksRemaining++;
    return createdBlock;
  }

  private PowerUp makePowerUp(char hardnessChar, Level level, Block createdBlock) {
    Group gameRoot = level.getGameRoot();
    Paddle gamePaddle = level.getGamePaddle();
    PowerUp powerUp;
    switch (hardnessChar) {
      case 'S':
        powerUp = new SlowBallPowerUp(gameRoot, gamePaddle, createdBlock);
        break;
      case 'P':
        powerUp = new PaddlePowerUp(gameRoot, gamePaddle, createdBlock);
        break;
      case 'B':
        powerUp = new BreakerBallPowerUp(gameRoot, gamePaddle, createdBlock);
        break;
      case 'M':
        powerUp = new MovingBlockPowerUp(gameRoot, gamePaddle, createdBlock);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + hardnessChar);
    }
    return powerUp;
  }

  void findAndDecrementBlock(Block block, Ball ball) {
    Block foundBlock = findBlock(block);
    if (foundBlock == null) return;
    if (foundBlock.getBlockHardness() == 1 || ball.isBreakerBall()) {
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

  private void removeBlockFromConfiguration(Block block) {
    block.removeFromScene();
    decreaseNumberOfBlocksByOne();
  }

  private void decrementBlock(Block block) {
    block.decreaseHardnessByOne();
    block.updateBlockColor();
  }

  /**
   * Used by Level to update its blockConfiguration.
   * @param sceneWidth
   * @param sceneHeight
   */
  public void updateConfiguration(int sceneWidth, int sceneHeight) {
    int blockWidth = sceneWidth / (getBlocksPerRow());
    int blockHeight = (sceneHeight-getInfoBarHeight() - getBlockConfigurationOffsetFromPaddle())
        / getNumberOfBlockRows();

    // TODO: utilize getBlocksAsList()
    // make delta i,j matrices to assign X and Y?
    for (int i = 0; i < configRows.length; i++) {
      BlockRow blockRow = configRows[i];
      if (blockRow == null) continue;
      Block[] blocks = blockRow.getRowOfBlocks();
      for (int j = 0; j < blocks.length; j++) {
        if (blocks[j] == null) continue;
        if (sceneWidth != 0 && sceneHeight != 0) {
          blocks[j].setDimensions(sceneWidth,sceneHeight);
          blocks[j].setX(blocks[j].getBlockWidth(sceneWidth)*j);
          blocks[j].setY(blocks[j].getBlockHeight(sceneHeight)*i + getInfoBarHeight());
          blocks[j].setDimensionsPowerUp();
        }
        blocks[j].updateBlockColor();
      }
    }
  }

  Block getBlock(int row, int col) {
    BlockRow blockRow = getBlockRows()[row];
    Block block = blockRow.getRowOfBlocks()[col];
    return block;
  }

  public List<Block> getBlocksAsList() {
    List<Block> blockList = new ArrayList<>();
    for (BlockRow blockRow : configRows) {
      if (blockRow != null && blockRow.getRowOfBlocks() != null) {
        for (Block block : blockRow.getRowOfBlocks()) {
          if (block != null) blockList.add(block);
        }
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

  public List<Block> getMovingBlocks() {
    List<Block> movingBlockList = new ArrayList<>();
    List<Block> blockList = getBlocksAsList();
    for (Block block : blockList) {
      if (block != null && block instanceof MovingBlock)
        movingBlockList.add(block);
    }
    return movingBlockList;
  }

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

  void setConfigFile(File configFile) { this.configFile = configFile; }

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
}

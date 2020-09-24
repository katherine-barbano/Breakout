package gameElements;

import breakout.Level;
import gameElements.PowerUp.PowerUpType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.Group;

/***
 *      Purpose: Reads in a text file into an array (size 8) of BlockRows
 *      Throws exception if any text file does not provide a valid level.
 *
 *      Methods: readTextFile, setBlockRows
 */
public class BlockConfiguration {
  public static final String FILE_SOURCE_PATH = "data/";
  public static final int NUMBER_OF_BLOCK_ROWS = Block.NUMBER_OF_BLOCK_ROWS;

  private Level myLevel;
  private File configFile;
  private BlockRow[] configRows;
  private int numberOfBlocksRemaining;
  private int numberOfPowerUps;

  public BlockConfiguration () { new BlockConfiguration("",""); }
  public BlockConfiguration(String gameName, String fileName) { new BlockConfiguration(gameName, fileName, null); }
  public BlockConfiguration(String gameName, String fileName, Level level) {
    setLevel(level);
    this.configRows = new BlockRow[NUMBER_OF_BLOCK_ROWS];
    if (!fileName.equals("")) {
      String filePath = generateFilePathForFile(gameName, fileName);
      generateBlockRowsFromFile(filePath); // Blocks are dimensionless
    }
  }


  void generateBlockRowsFromFile(String filePath) {
    try {
      configFile = new File(filePath);
      Scanner fileReader = new Scanner(configFile);
      for (int i = 0; i < NUMBER_OF_BLOCK_ROWS; i++) {
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
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  private MovingBlockRow convertFileLineToMovingBlockRow(String fileLine) {
    String[] hardnessArray = fileLine.split(" ");
    Block[] blockArray = new Block[Block.BLOCKS_PER_ROW];
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

    MovingBlockRow movingBlockRow = new MovingBlockRow();
    movingBlockRow.setRowOfBlocks(blockArray);
    return movingBlockRow;
  }

  // Assumes that fileRow is of format "X X X ".."X X X"
  private FilledBlockRow convertFileLineToBlockRow(String fileRow) {
    String[] hardnessArray = fileRow.split(" ");
    Block[] blockArray = new Block[Block.BLOCKS_PER_ROW];
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
        powerUp.setPowerUpType(PowerUpType.SLOW_BALL);
        break;
      case 'P':
        powerUp = new PaddlePowerUp(gameRoot, gamePaddle, createdBlock);
        powerUp.setPowerUpType(PowerUpType.PADDLE);
        break;
      case 'B':
        powerUp = new BreakerBallPowerUp(gameRoot, gamePaddle, createdBlock);
        powerUp.setPowerUpType(PowerUpType.BREAKER_BALL);
        break;
      case 'M':
        powerUp = new MovingBlockPowerUp(gameRoot, gamePaddle, createdBlock);
        powerUp.setPowerUpType(PowerUpType.MOVING_BLOCK);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + hardnessChar);
    }
    //gameRoot.getChildren().add(powerUp);
    return powerUp;
  }

  void decrementBlock(Block block, Ball ball) {
    for (int i = 0; i < configRows.length; i++) {
      BlockRow row = configRows[i];
      for (int j = 0; j < row.getRowOfBlocks().length; j++) {
        Block currentBlock = row.getRowOfBlocks()[j];
        if (currentBlock != null && currentBlock.equals(block)) {
          if (currentBlock.getBlockHardness() == 1 || ball.isBreakerBall()) {
            currentBlock.removeFromScene();
            decreaseNumberOfBlocksByOne();
          } else {
            currentBlock.decreaseHardnessByOne();
            currentBlock.updateBlockColor();
          }
        }
      }
    }
  }

  /**
   * Used by Level to update its blockConfiguration.
   * @param sceneWidth
   * @param sceneHeight
   */
  public void updateConfiguration(int sceneWidth, int sceneHeight) {
    int blockWidth = sceneWidth / Block.BLOCKS_PER_ROW;
    int blockHeight = sceneHeight / (Block.NUMBER_OF_BLOCK_ROWS + 1);

    for (int i = 0; i < configRows.length; i++) {
      BlockRow blockRow = configRows[i];
      if (blockRow == null) continue;
      Block[] blocks = blockRow.getRowOfBlocks();
      for (int j = 0; j < blocks.length; j++) {
        if (blocks[j] == null) continue;
        if (sceneWidth != 0 && sceneHeight != 0) {
          blocks[j].setDimensions(sceneWidth,sceneHeight);
          blocks[j].setX(blockWidth*j);
          blocks[j].setY(blockHeight*i);
          blocks[j].setDimensionsPowerUp();
        }
        blocks[j].updateBlockColor();
      }
    }
  }

  String generateFilePathForFile(String gameName, String fileName) { return FILE_SOURCE_PATH + gameName + "/" + fileName + ".txt"; }

  public BlockRow[] getBlockRows() { return configRows; }

  void setConfigFile(File configFile) { this.configFile = configFile; }

  void decreaseNumberOfBlocksByOne() { numberOfBlocksRemaining--; }
  void setNumberOfBlocksRemaining(int numberOfBlocksRemaining) { this.numberOfBlocksRemaining = numberOfBlocksRemaining; }
  public int getNumberOfBlocksRemaining() { return numberOfBlocksRemaining; }
  boolean isEmpty() { return (numberOfBlocksRemaining == 0);}

  Block getBlock(int row, int col) {
    BlockRow blockRow = getBlockRows()[row];
    Block block = blockRow.getRowOfBlocks()[col];
    return block;
  }

  int getBlockHeight(int sceneHeight) {
    return (sceneHeight - Paddle.VERTICAL_PADDLE_OFFSET_FROM_BOTTOM - Paddle.PADDLE_HEIGHT) /
        (Block.NUMBER_OF_BLOCK_ROWS + 1);
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
    for (BlockRow blockRow : configRows) {
      if (blockRow != null && blockRow.getRowOfBlocks() != null) {
        for (Block block : blockRow.getRowOfBlocks()) {
          if (block != null && block.hasReleasedPowerUp()) {

            powerUpList.add(block.getPowerUp());
          }
        }
      }
    }
    return powerUpList;
  }

  public List<Block> getMovingBlocks() {
    List<Block> movingBlockList = new ArrayList<>();
    for (BlockRow blockRow : configRows) {
      if (blockRow != null && blockRow instanceof MovingBlockRow) {
        Block[] movingBlocks = blockRow.getRowOfBlocks();
        for (Block block : movingBlocks) {
          if (block != null && block instanceof MovingBlock)
            movingBlockList.add((MovingBlock) block); // FIXME: cast is redundant
        }
      }
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

  public int getNumberOfPowerUps() { return numberOfPowerUps; }
  public void setNumberOfPowerUps(int powerUps) { this.numberOfPowerUps = powerUps; }
}

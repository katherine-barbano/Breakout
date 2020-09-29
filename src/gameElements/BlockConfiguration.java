package gameElements;

import breakout.Game;
import breakout.Level;
import gameElements.PowerUp.PowerUpType;
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

  public BlockConfiguration() {
    new BlockConfiguration("", "");
  }

  public BlockConfiguration(String gameName, String fileName) {
    new BlockConfiguration(gameName, fileName, null);
  }

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
    } catch (FileNotFoundException e) {
      return;
    } catch (IOException e) {
      return;
    }
  }

  void generateBlockRowsFromFile(String filePath) {
    Scanner fileReader;
    try {
      configFile = new File(filePath);
      fileReader = new Scanner(configFile);
    } catch (FileNotFoundException e) {
      return;
    }
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
        createdBlock = new Block(hardValue);
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
    } else {
      PowerUp createdPowerUp = makePowerUp(hardnessChar, myLevel);
      createdBlock = new Block(createdPowerUp);
    }
    numberOfBlocksRemaining++;
    return createdBlock;
  }

  private PowerUp makePowerUp(char hardnessChar, Level level) {
    Group gameRoot = level.getGameRoot();
    Paddle gamePaddle = level.getGamePaddle();
    PowerUp powerUp;
    numberOfPowerUps++;
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
        numberOfPowerUps--;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + hardnessChar);
    }
    return powerUp;
  }

  public void removeBlockFromConfiguration(Block block) {
    block.removeFromScene();
    decreaseNumberOfBlocksByOne();
  }

  public void decrementBlock(Block block) {
    block.decreaseHardnessByOne();
    boolean isBreakerBall = myLevel.getGameBall().isBreakerBall();
    if (block.getBlockHardness() == 0 || isBreakerBall) {
      addScoreBasedOnProperties(block);
      decreaseNumberOfBlocksByOne();
      block.removeFromScene();
    }
    block.updateBlockColor();
  }

  private void addScoreBasedOnProperties(Block block) {
    if (block instanceof VaryingBlock) {
      increaseScoreBy(getScoreIncrementVaryingBlock());
    } else if (block instanceof MovingBlock) {
      increaseScoreBy(getScoreIncrementMovingBlock());
    } else {
      increaseScoreBy(getScoreIncrement());
    }
  }

  /**
   * Used by Level to update its blockConfiguration.
   *
   * @param sceneWidth
   * @param sceneHeight
   */
  public void updateConfiguration(int sceneWidth, int sceneHeight) {
    for (int i = 0; i < configRows.length; i++) {
      BlockRow blockRow = configRows[i];
      if (blockRow == null) {
        continue;
      }
      Block[] blocks = blockRow.getRowOfBlocks();
      for (int j = 0; j < blocks.length; j++) {
        if (blocks[j] == null) {
          continue;
        }
        if (sceneWidth != 0 && sceneHeight != 0) {
          orientBlock(blocks[j], sceneWidth, sceneHeight, i, j);
        }
        blocks[j].updateBlockColor();
      }
    }
  }

  private void orientBlock(Block block, int sceneWidth, int sceneHeight, int i, int j) {
    block.setDimensions(sceneWidth, sceneHeight);
    block.setX(block.getBlockWidth(sceneWidth) * j);
    block.setY(block.getBlockHeight(sceneHeight) * i + getInfoBarHeight());
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

  public List<Block> getVaryingBlocks() {
    List<Block> varyingBlockList = new ArrayList<>();
    for (Block block : getBlocksAsList()) {
      if (block != null && block instanceof VaryingBlock) {
        varyingBlockList.add(block);
      }
    }
    return varyingBlockList;
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

  public void setLevel(Level myLevel) {
    this.myLevel = myLevel;
  }

  public BlockRow[] getBlockRows() {
    return configRows;
  }

  void decreaseNumberOfBlocksByOne() {
    numberOfBlocksRemaining--;
  }

  void increaseScoreBy(int value) {
    myLevel.getGameBall().increaseScoreBy(value);
  }

  public int getNumberOfBlocksRemaining() {
    return numberOfBlocksRemaining;
  }

  void setNumberOfBlocksRemaining(int numberOfBlocksRemaining) {
    this.numberOfBlocksRemaining = numberOfBlocksRemaining;
  }

  public int getNumberOfPowerUps() {
    return numberOfPowerUps;
  }

  private int getInfoBarHeight() {
    return Integer
        .parseInt(properties.getProperty("info_bar_height"));
  }

  private int getBlocksPerRow() {
    return Integer.parseInt(properties.getProperty("blocks_per_row"));
  }

  private int getNumberOfBlockRows() {
    return Integer.parseInt(properties.getProperty("number_of_block_rows"));
  }

  private int getScoreIncrementVaryingBlock() {
    return Integer.parseInt(properties.getProperty("score_increment_varying_block"));
  }

  private int getScoreIncrementMovingBlock() {
    return Integer.parseInt(properties.getProperty("score_increment_moving_block"));
  }

  private int getScoreIncrement() {
    return Integer.parseInt(properties.getProperty("score_increment"));
  }

  public int getSceneSize() {
    return Integer.parseInt(properties.getProperty("scene_size"));
  }

  public int getPlayableArea() {
    return Integer.parseInt(properties.getProperty("playable_area_size"));
  }

  public void updateBlocks(double elapsedTime, Block touchedBlock, boolean gameIsPaused) {
    updateBlockAttributes(elapsedTime, gameIsPaused);
    if (touchedBlock == null) {
      return;
    } else {
      handleTouchedBlock(touchedBlock);
    }
  }

  public void updateBlockAttributes(double elapsedTime, boolean gameIsPaused) {
    for (Block block : getBlocksAsList()) {
      if (block instanceof VaryingBlock) {
        ((VaryingBlock) block).waitToChangeHardness(elapsedTime, gameIsPaused);
      }
      if (block instanceof MovingBlock) {
        ((MovingBlock) block).updateVelocityAndPosition(elapsedTime, gameIsPaused);
      }
    }
  }

  public void handleTouchedBlock(Block touchedBlock) {
    if (touchedBlock.hasPowerUp() && touchedBlock.getBlockHardness() == touchedBlock
        .getMinimumHardness()) {
      releasePowerUpInBlock(touchedBlock);
    }
    decrementBlock(touchedBlock);
  }

  public void releasePowerUpInBlock(Block block) {
    PowerUpType powerUpType = block.getPowerUp().getPowerUpType();
    switch (powerUpType) {
      case SLOW_BALL:
      case BREAKER_BALL: {
        block.getPowerUp().setGameBall(myLevel.getGameBall());
        break;
      }
      case PADDLE: {
        block.getPowerUp().setPaddle(myLevel.getGamePaddle());
        break;
      }
      case MOVING_BLOCK: {
        myLevel.getGameBall().increaseScoreBy(myLevel.getGameBall().getMovingBlockScoreValue());
        break;
      }
      default: {
        throw new IllegalStateException("This shouldn't be reached");
      }
    }
    if (powerUpType != PowerUpType.MOVING_BLOCK) {
      block.releasePowerUp();
      block.setHasReleasedPowerUp(true);
    }
  }

  public void updateVisiblePowerUps(double elapsedTime, boolean gameIsPaused) {
    for (PowerUp fallingPowerUp : getVisiblePowerUps()) {
      if (gameIsPaused) {
        fallingPowerUp.removeFromScene();
      } else {
        fallingPowerUp.updateLocation(elapsedTime, gameIsPaused);
        if (myLevel.getGamePaddle().isTouchingPaddleTop(fallingPowerUp)) {
          fallingPowerUp.givePowerUp(getLevel().getGameBall(), getLevel().getGamePaddle());
          fallingPowerUp.removeFromScene();
          numberOfPowerUps--;
        }
      }
    }
  }
}

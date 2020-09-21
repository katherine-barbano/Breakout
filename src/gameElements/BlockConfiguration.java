package gameElements;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/***
 *      Purpose: Reads in a text file into an array (size 8) of BlockRows
 *      Throws exception if any text file does not provide a valid level.
 *
 *      Methods: readTextFile, setBlockRows
 */
public class BlockConfiguration {
  public static final String FILE_SOURCE_PATH = "data/";
  public static final int NUMBER_OF_BLOCK_ROWS = Block.NUMBER_OF_BLOCK_ROWS;

  private File configFile;
  private BlockRow[] configRows;
  private int numberOfBlocksRemaining; // TODO keep track of breakable blocks remaining

  public BlockConfiguration () { new BlockConfiguration("",""); }
  public BlockConfiguration(String gameName, String fileName) {
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
          FilledBlockRow rowForFileLine = convertFileLineToBlockRow(fileLine);
          configRows[i] = rowForFileLine;
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  // Assumes that fileRow is of format "X X X ".."X X X"
  private FilledBlockRow convertFileLineToBlockRow(String fileRow) {
    String[] hardnessArray = fileRow.split(" ");
    Block[] blockArray = new Block[Block.BLOCKS_PER_ROW];
    assert (blockArray.length == hardnessArray.length);

    for (int i = 0; i < hardnessArray.length; i++) {
      String hardness = hardnessArray[i];
      int hardValue = Integer.parseInt(hardness);
      if (hardValue > 0) {
        blockArray[i] = new Block();
        blockArray[i].setBlockHardness(hardValue);
        numberOfBlocksRemaining++;
      }
    }

    FilledBlockRow blockRow = new FilledBlockRow();
    blockRow.setBlocks(blockArray);
    return blockRow;
  }

  void decrementBlock(Block block) {
    for (int i = 0; i < configRows.length; i++) {
      BlockRow row = configRows[i];
      for (int j = 0; j < row.getRowOfBlocks().length; j++) {
        Block currentBlock = row.getRowOfBlocks()[j];
        if (currentBlock != null && currentBlock.equals(block)) {
          if (currentBlock.getBlockHardness() > 1) {
            currentBlock.decreaseHardnessByOne();
            currentBlock.updateBlockColor();
          }
          else {
            currentBlock.removeFromScene();
            decreaseNumberOfBlocksByOne();
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

  public void removeAllBlocks() {
    List<Block> blockList = getBlocksAsList();
    for (Block block : blockList) {
      block.removeFromScene();
    }
    setNumberOfBlocksRemaining(0);
  }
}

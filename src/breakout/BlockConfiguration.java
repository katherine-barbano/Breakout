package breakout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/***
 *      Purpose: Reads in a text file into an array (size 8) of BlockRows
 *      Throws exception if any text file does not provide a valid level.
 *
 *      Methods: readTextFile, setBlockRows
 */
public class BlockConfiguration {
  public static final String FILE_SOURCE_PATH = "data/sample_game/";
  public static final int NUMBER_OF_BLOCK_ROWS = Block.NUMBER_OF_BLOCK_ROWS;

  private String fileName;
  private File configFile;
  private BlockRow[] configRows;
  private int numberOfBlocksRemaining; // TODO keep track of breakable blocks remaining

  public BlockConfiguration () { new BlockConfiguration(""); }
  public BlockConfiguration(String fileName) {
    this.fileName = fileName;
    this.configRows = new BlockRow[NUMBER_OF_BLOCK_ROWS];
    String filePath = generateFilePathForFile(fileName);
    if (!fileName.equals("")) generateBlockRowsFromFile(filePath); // Blocks are dimensionless
  }

  void generateBlockRowsFromFile(String filePath) {
    try {
      configFile = new File(filePath);
      Scanner fileReader = new Scanner(configFile);
      int arrPointer = 0;
      while (fileReader != null && fileReader.hasNextLine()) {
        if (arrPointer >= NUMBER_OF_BLOCK_ROWS) {
          throw new Exception();
        }
        String fileLine = fileReader.nextLine();
        FilledBlockRow rowForFileLine = convertFileLineToBlockRow(fileLine);
        configRows[arrPointer] = rowForFileLine;
        arrPointer++;
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      // TODO
      e.printStackTrace();
    }
  }

  // Assumes that fileRow is of format "X X X ".."X X X"
  private FilledBlockRow convertFileLineToBlockRow(String fileRow) {
    String[] hardnessArray = fileRow.split(" ");
    Block[] blocks = convertIntToBlocks(hardnessArray);
    FilledBlockRow blockRow = new FilledBlockRow();
    blockRow.setBlocks(blocks);
    return blockRow;
  }

  private Block[] convertIntToBlocks(String[] hardnessArray) {
    Block[] blocks = new Block[hardnessArray.length];
    for (int i = 0; i < blocks.length; i++) {
      int hardness = Integer.parseInt(hardnessArray[i]);
      if (hardness > 0) numberOfBlocksRemaining++;
      blocks[i] = new Block();
      blocks[i].setBlockHardness(hardness);
    }
    return blocks;
  }

  // e.g. input "level_1" generates full file source for level_1.txt
  private String generateFilePathForFile(String fileName) { return FILE_SOURCE_PATH + fileName + ".txt"; }

  BlockRow[] getBlockRows() { return configRows; }
  void setConfigFile(File configFile) { this.configFile = configFile; }
  void setFileName(String fileName) { this.fileName = fileName; }
  int getNumberOfBlocksRemaining() { return numberOfBlocksRemaining; }
  void decreaseNumberOfBlocksByOne() { numberOfBlocksRemaining--; }
  void setNumberOfBlocksRemaining(int numberOfBlocksRemaining) { this.numberOfBlocksRemaining = numberOfBlocksRemaining; }
  boolean isEmpty() { return (numberOfBlocksRemaining == 0);}
}

package gameElements;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/***
 *      Interface for a row of blocks in the block configuration. Its subclasses,
 *      MovingBlock and FilledBlockRow, should be instantiated.
 *
 *      Method: displayBlocks
 */
public abstract class BlockRow {

  private Block[] rowOfBlocks;
  private Properties properties;

  public BlockRow() {
    initializeProperties();
    rowOfBlocks = new Block[getBlocksPerRow()];
  }

  void initializeProperties() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }

  public Block[] getRowOfBlocks() { return rowOfBlocks; }
  public void setRowOfBlocks(Block[] rowOfBlocks) { this.rowOfBlocks = rowOfBlocks; }
  private int getBlocksPerRow() { return Integer.parseInt(properties.getProperty("blocks_per_row")); }
}

package gameElements;
import static gameElements.Block.BLOCKS_PER_ROW;
/***
 *      Interface for a row of blocks in the block configuration. Its subclasses,
 *      MovingBlock and FilledBlockRow, should be instantiated.
 *
 *      Method: displayBlocks
 */
public abstract class BlockRow {

  private Block[] rowOfBlocks;

  public BlockRow() {
    rowOfBlocks = new Block[BLOCKS_PER_ROW];
  }

  public Block[] getRowOfBlocks() { return rowOfBlocks; }
  public void setRowOfBlocks(Block[] rowOfBlocks) { this.rowOfBlocks = rowOfBlocks; }

}

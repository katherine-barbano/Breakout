package breakout;

import static breakout.Block.BLOCKS_PER_ROW;

/***
 *      Interface for a row of blocks in the block configuration. Its subclasses,
 *      MovingBlock and FilledBlockRow, should be instantiated.
 *
 *      Method: displayBlocks
 */
public interface BlockRow {

  abstract Block[] getRowOfBlocks();
}

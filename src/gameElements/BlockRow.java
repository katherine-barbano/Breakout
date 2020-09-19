package gameElements;

/***
 *      Interface for a row of blocks in the block configuration. Its subclasses,
 *      MovingBlock and FilledBlockRow, should be instantiated.
 *
 *      Method: displayBlocks
 */
public interface BlockRow {

  abstract Block[] getRowOfBlocks();
}

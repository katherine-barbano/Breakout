package gameElements;

import static gameElements.Block.BLOCKS_PER_ROW;

/***
 *   Contains private array (size 12) of multiple Block objects. Array
 *   might contain empty values if no blocks are present in that space from the text file.
 *
 *   This is a subclass of BlockRow. It only contains Block or PowerUpBlock. It cannot
 *   contain MovingBlock, since a single MovingBlock will take up the entire row.
 *
 *   Method: setBlocks, displayBlocks
 */
public class FilledBlockRow extends BlockRow {

  public FilledBlockRow () {
    super();
  }

}

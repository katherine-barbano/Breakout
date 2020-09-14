package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/***
 *    Purpose: A rectangle that is removed from obstructing the Ball's movement
 *    if the hardness becomes 0. Contains a hardness field from 0 to 3. The Block's
 *    color corresponds to the current hardness, which is updated if it is hit by a
 *    Ball.
 *
 *    Method: updateHardness
 */
public class Block extends Rectangle {

  public static final Paint BLOCK_COLOR_ONE = Color.PALETURQUOISE;
  public static final Paint BLOCK_COLOR_TWO = Color.PALEVIOLETRED;
  public static final Paint BLOCK_COLOR_THREE = Color.PURPLE;
  public static final int BLOCKS_PER_ROW = 12;
  public static final int NUMBER_OF_BLOCK_ROWS = 8;

  private int blockHardness;
  private Paint blockColor;

  public Block(int sceneWidth, int sceneHeight, int blockHardness) {
    setDimensions(sceneWidth, sceneHeight);
    this.blockHardness = blockHardness;
    updateBlockColor(blockHardness);
    Paint blockColor = getBlockColor(blockHardness);

    setFill(blockColor);
    setId("block");
  }

  public Block () { }

  private void updateBlockColor(int blockHardness) {
    Paint newBlockColor = getBlockColor(blockHardness);
    this.blockColor = newBlockColor;
  }

  private void setDimensions(int sceneWidth, int sceneHeight) {
    int blockWidth = sceneWidth / BLOCKS_PER_ROW;
    int blockHeight = getBlockHeight(sceneHeight);
    setWidth(blockWidth);
    setHeight(blockHeight);
  }

  // multiplied by factor of 0.8 because we aren't filling entire scene with rows
  // TODO: optimize this scaling factor
  private int getBlockHeight(int sceneHeight) {
    return (sceneHeight * 8) / 10 * NUMBER_OF_BLOCK_ROWS;
  }

  Paint getBlockColor(int blockHardness) {
    if (blockHardness == 1) {
      return BLOCK_COLOR_ONE;
    }
    else if (blockHardness == 2) {
      return BLOCK_COLOR_TWO;
    }
    else if (blockHardness == 3) {
      return BLOCK_COLOR_THREE;
    }
    else {
      return Color.BLACK; // Error color, should never appear
    }
  }

  int getBlockHardness() { return blockHardness; }
  void setBlockHardness(int blockHardness) {
    this.blockHardness = blockHardness;
    updateBlockColor(blockHardness);
  }
  void decreaseHardnessByOne() {
    blockHardness--;
  }
  Paint getBlockColor() { return blockColor; }
  void setBlockColor(Paint blockColor) { this.blockColor = blockColor; }

  public void updatePosition(int blockWidth, int blockHeight, int x, int y) {
    setWidth(blockWidth);
    setHeight(blockHeight);
    setX(x);
    setY(y);
  }
}

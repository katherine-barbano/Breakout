package gameElements;

public class VaryingBlock extends Block {

  private double pseudoClockCounter;

  public VaryingBlock() {
    initializeProperties();
    varyHardness();
  }

  void waitToChangeHardness(double elapsedTime, boolean isPaused) {
    if (! isPaused) {
      pseudoClockCounter += elapsedTime;
      if (pseudoClockCounter > getVaryingBlockDownTime()) {
        varyHardness();
      }
    }
  }

  void varyHardness() {
    pseudoClockCounter = 0.0;
    setRandomHardness();
    colorBlock();
  }
}

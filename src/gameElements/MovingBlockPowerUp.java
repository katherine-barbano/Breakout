package gameElements;

import javafx.scene.Group;

public class MovingBlockPowerUp extends PowerUp{

  public MovingBlockPowerUp(Group gameRoot, Paddle gamePaddle) {
    super(gameRoot, gamePaddle);
    setPowerUpType(PowerUpType.MOVING_BLOCK);
  }

  @Override
  public void givePowerUp() {
    int bonusScore = getMovingBlockScoreValue();
    if (getGameBall() != null) {
      getGameBall().increaseScoreBy(bonusScore);
    }
  }

  // this power up shouldn't show in the scene, so this is overridden to do nothing.
  @Override
  public void showInScene() { }

  @Override
  void setProperties() {
    super.setProperties();
    setId("Moving block power up");
  }

}

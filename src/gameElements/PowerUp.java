package gameElements;

import breakout.Game;
import breakout.Level;
import javafx.scene.paint.Paint;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

public abstract class PowerUp extends Circle {

  private Group gameRoot;
  private Properties properties;
  protected Paddle gamePaddle;
  private Ball gameBall;
  private Block ownerBlock;
  private PowerUpType powerUpType;
  private int velocityY;
  private boolean isReleased;

  public enum PowerUpType {
    BREAKER_BALL,
    PADDLE,
    MOVING_BLOCK,
    SLOW_BALL,
    P_LAST // shouldn't be reached
  }

  public PowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    gameRoot = gameRootArg;
    gamePaddle = paddleArg;
    ownerBlock = blockArg;
    initializePropertyList();
    setProperties();
  }

  public PowerUp(Group gameRootArg, Paddle paddleArg) {
    gameRoot = gameRootArg;
    gamePaddle = paddleArg;
    initializePropertyList();
  }

  public PowerUp(Level level, Block blockArg) {
    gameRoot = level.getGameRoot();
    gamePaddle = level.getGamePaddle();
    ownerBlock = blockArg;
    initializePropertyList();
    setProperties();
  }

  public abstract void givePowerUp(Ball gameBall, Paddle gamePaddle);

  void assignToBlock(Block block) {
    ownerBlock = block;
    setProperties();
  }

  private void initializePropertyList() {
    properties = new Properties();
    FileInputStream ip = null;
    try {
      ip = new FileInputStream(Game.PROPERTY_FILE);
      properties.load(ip);
    }
    catch (FileNotFoundException e) {}
    catch (IOException e) {}
  }

  void setProperties() {
    double centerX = GetXCenterOfBlock(getOwnerBlock());
    double centerY = GetYCenterOfBlock(getOwnerBlock());
    setCenterX(centerX);
    setCenterY(centerY);
    setIsReleased(false);
    setRadius(getBallRadius());
  }

  public void showInScene() {
    velocityY = getPowerUpDropSpeed();
    setIsReleased(true);
    addToScene();
  }

  public void updateLocation(double elapsedTime, boolean isPaused) {
    if (isPaused) {
      removeFromScene();
      return;
    }
    updatePositionY(elapsedTime);
  }

  private double GetXCenterOfBlock(Block block) {
    double x = block.getX();
    double width = block.getWidth();
    return x + ((width) / 2);
  }

  private double GetYCenterOfBlock(Block block) {
    double y = block.getY();
    double height = block.getHeight();
    return y + ((height) / 2);
  }

  private void addToScene() {
    assert (gameRoot != null);
    assert (gameRoot.getChildren() != null);
    if (! gameRoot.getChildren().contains(this)) {
      gameRoot.getChildren().add(this);
    }
  }
  public void removeFromScene() {
    gameRoot.getChildren().remove(this);
  }

  private void updatePositionY(double elapsedTime) { setCenterY(getCenterY() + velocityY * elapsedTime); }

  public Block getOwnerBlock() { return ownerBlock; }
  public void setOwnerBlock(Block ownerBlock) { this.ownerBlock = ownerBlock; }

  public void setIsReleased(boolean released) { isReleased = released; }

  public Group getGameRoot() { return gameRoot; }
  public void setGameRoot(Group gameRoot) { this.gameRoot = gameRoot; }

  public void setPaddle(Paddle gamePaddle) { this.gamePaddle = gamePaddle; }

  public void setPowerUpType(PowerUpType type) { this.powerUpType = type; }
  public PowerUpType getPowerUpType() { return this.powerUpType; }

  public Ball getGameBall() { return this.gameBall; }
  public void setGameBall(Ball ball) { this.gameBall = ball; }

  int getMovingBlockScoreValue() { return Integer.parseInt(properties.getProperty("moving_block_score_value"));}
  int getBallRadius() { return Integer.parseInt(properties.getProperty("ball_radius"));}
  int getPowerUpDropSpeed() { return Integer.parseInt(properties.getProperty("power_up_drop_speed"));}
  Paint getPaddlePowerUpColor() { return Paint.valueOf(properties.getProperty("paddle_power_up_color"));}
  Paint getSlowBallPowerUpColor() { return Paint.valueOf(properties.getProperty("slow_ball_power_up_color"));}
  Paint getBreakerBallPowerUpColor() { return Paint.valueOf(properties.getProperty("breaker_ball_color"));}
  int getSlowBallSpeed() {
    int slowBallSpeed = Integer.parseInt(properties.getProperty("slow_ball_speed"));
    if (gameBall.getVelocityY() < 0) slowBallSpeed *= -1;
    return slowBallSpeed;
  }
}

package gameElements;

import breakout.Game;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.scene.Group;
import javafx.scene.paint.Paint;

public class BreakerBallPowerUp extends PowerUp {

  private Properties properties;

  public BreakerBallPowerUp(Group gameRootArg, Paddle paddleArg, Block blockArg) {
    super(gameRootArg, paddleArg, blockArg);
    setPowerUpType(PowerUpType.BREAKER_BALL);
    initializeProperties();
  }

  public BreakerBallPowerUp(Group gameRoot, Paddle gamePaddle) {
    super(gameRoot, gamePaddle);
    setPowerUpType(PowerUpType.BREAKER_BALL);
    initializeProperties();
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

  public void givePowerUp() {
    if (getGameBall() != null) {
      getGameBall().setIsBreakerBall(true);
    }
  }

  @Override
  void setProperties() {
    super.setProperties();
    if (properties == null) initializeProperties();
    setFill(getBreakerBallColor());
    setId("Breaker ball power up");
  }

  private Paint getBreakerBallColor() { return Paint.valueOf(properties.getProperty("breaker_ball_color"));}
}

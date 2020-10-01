package text;

import javafx.scene.Group;

/***
 * Subclass to display remaining time status indicator on the InfoBar
 * at the specified position and with the "Time: " title.
 */
public class TimerText extends StatusText {

  public TimerText(int startTime, Group gameRootArg) {
    super(gameRootArg, startTime);
  }

  @Override
  void setStatusTextProperties(){
    setTitle(getTimerTitle());
    setX(getTimerXPosition());
    setY(getTimerYPosition());
    setId(getTimerId());
  }
}
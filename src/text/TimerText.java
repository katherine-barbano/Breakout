package text;

import javafx.scene.Group;

public class TimerText extends StatusText {

  public static final String TIMER_TITLE = "Time: ";
  public static final int TIMER_XPOSITION = LivesText.LIVES_XPOSITION+100;
  public static final int TIMER_YPOSITION = LivesText.LIVES_YPOSITION;
  public static final String TIMER_ID = "#timerText";

  public TimerText(int startTime, Group gameRootArg) {
    super(gameRootArg, startTime, TIMER_TITLE, TIMER_XPOSITION, TIMER_YPOSITION, TIMER_ID);
  }
}
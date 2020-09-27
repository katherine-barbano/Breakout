package gameElements;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.util.Duration;
import text.GameOverText;
import text.GameText;
import text.TimerText;

public class GameTimer {
  public static final int START_TIME_SECONDS=60;

  private Timeline timeline;
  private int timeSeconds = START_TIME_SECONDS;
  private GameText timerText;
  private Group root;

  //referenced https://asgteach.com/2011/10/javafx-animation-and-binding-simple-countdown-timer-2/ for how to set up Timeline with javafx
  public GameTimer(Group gameRoot) {
    root = gameRoot;
    timerText = new TimerText(timeSeconds,root);
    initializeTimeline();
  }

  private void initializeTimeline() {
    timeline = new Timeline();
    timeline.setCycleCount(START_TIME_SECONDS);
    EventHandler timerUpdate = new EventHandler() {
      public void handle(Event event) {
        timeSeconds--;
        System.out.println(timeSeconds);

        TimerText subclassTimerText = (TimerText) timerText;
        subclassTimerText.updateValue(timeSeconds);
        timerText = subclassTimerText;

        if(timeSeconds <=0) {
          timeline.stop();
        }
      }
    };
    KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), timerUpdate);
    timeline.getKeyFrames().add(keyFrame);

    timeline.playFromStart();
  }
}

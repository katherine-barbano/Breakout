package text;

import javafx.scene.Group;

/***
 * Subclass to display number of lives status indicator on the InfoBar
 * at the specified position and with the "Lives: " title.
 */
public class LivesText extends StatusText {

  public LivesText(int numberOfLives, Group gameRootArg) {
    super(gameRootArg, numberOfLives);
  }

  @Override
  void setStatusTextProperties(){
    setTitle(getLivesTitle());
    setX(getLivesXPosition());
    setY(getLivesYPosition());
    setId(getLivesId());
  }
}

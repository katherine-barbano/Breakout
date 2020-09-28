package text;

import javafx.scene.Group;

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

package text;

import javafx.scene.Group;

public class GameOverText extends GameText {

  public static final String GAMEOVER_TITLE = "Game Over! Tap the space bar to restart from Level 1.";
  public static final String GAMEWON_TITLE = "You won! Tap the space bar to restart from Level 1.";
  public static final String GAMEOVER_ID = "gameOverText";

  public static final int GAMEOVER_XPOSITION = 60;
  public static final int GAMEOVER_YPOSITION = 250;

  public GameOverText(Group gameRootArg) {
    super(gameRootArg);
    initializeText(GAMEOVER_TITLE);
  }

  @Override
  public void initializeText(String words) {
    initializeProperties(words, GAMEOVER_XPOSITION, GAMEOVER_YPOSITION, GAMEOVER_ID);
    addText();
  }

  @Override
  public void removeText() {
    setText("");
  }

  @Override
  void updateText(String words) {
    setText(words);
  }

  public void gameOverUpdate(boolean gameIsWon) {
    if(gameIsWon) {
      updateText(GAMEWON_TITLE);
    }
    else {
      updateText(GAMEOVER_TITLE);
    }
  }
}

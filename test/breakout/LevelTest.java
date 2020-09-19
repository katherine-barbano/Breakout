package breakout;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import util.DukeApplicationTest;

public class LevelTest extends DukeApplicationTest {
  private Level level;
  private Game game;

  @Override
  public void start (Stage stage) {
    game = new Game(stage);
  }

  @Test
  void testAllBlocks() {
    Group testGroup = new Group();
    level = new Level(testGroup,"testOneInput");
    int width = (int) game.getScene().getWidth();
    int height = (int) game.getScene().getHeight();
    ArrayList<Block> oneBlock = level.getAllBlocks(width, height);
    assertTrue(oneBlock.size() == 1);
    Block block = oneBlock.get(0);
    assertEquals(block.getBlockColor(), Color.PALETURQUOISE);
    assertEquals(block.getX(), block.getWidth());
  }

}

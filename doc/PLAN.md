# Game Plan
## NAMEs
Anna Diemel (ad356) and Katherine Barbano (kab134)

### Breakout Variation Ideas

### Interesting Existing Game Variations

 * Game 1: Fairy Treasure - This game used gems as a "positive" object and fire as a "negative" object.
 Similarly, we can have "good objects" in our game that drop down from a broken block to help the user
 if the paddle collects it.
 
 * Game 2: Super Breakout - This game had different color blocks have different corresponding levels of
 number of hits needed to break the block. Similarly, we can have blocks of varying hardness in our game
 that require a different number of hits to be broken.


#### Block Ideas

 * Block 1: Blocks with varying strengths (that is, some require 1 hit to be broken, some require
 2 to be broken, some require 3). When a level 3 hardness block is hit once, it turns into a level
 2 hardness block. Blocks are different colors corresponding to the number of hits left required
 to break the block.

 * Block 2: Blocks that drop power-ups. Once these blocks break, a power up falls down the screen
 and if it hits the paddle, the user receives the effects of the power-up. These blocks have a special
 color, and always require only one hit to break.

 * Block 3: Blocks that move from side to side horizontally. These blocks are bounded by the right and left
  sides of the screen, and bounce off the walls. A row containing a moving block should only contain that
  single moving block, in order to avoid confusing scenarios with other blocks being in the same row.
  If a power-up is dropping and the block is in the way, the power-up should simply drop through the 
  moving block and continue down. The starting position of the block should be specified by the text
  file reading in the level, and the block should begin moving when the ball is released.
  

#### Power Up Ideas

 * Power Up 1: Extend the size of the paddle so that it takes up more space, making it more difficult
 to miss the ball. This expires after 20 seconds.

 * Power Up 2: The ball slows down, making it easier for the user to navigate the paddle to the correct
 position and avoid missing the ball. This expires after 20 seconds.

 * Power Up 3: Ball becomes strong, meaning it can break through any block with a single hit regardless
 of the block's hardness. This power up is only available on levels with blocks of varying hardness. This
 expires after 20 seconds.


#### Cheat Key Ideas

 * Cheat Key 1: Pressing the ' ' space bar will pause and un-pause the game. Nothing will move when the game
   is paused.

 * Cheat Key 2: Pressing the 'r' button immediately resets the ball and paddle to their starting positions.
 Normally, the game will start in the "paused" state with the ball stationary on the paddle, and it begins moving vertically
 upward when the user clicks the space bar to unpause/start the game. Pressing 'r' will bring the ball back into
 this paused state with the ball ontop of the paddle, where it is not moving until the user clicks the space bar again.

 * Cheat Key 3: Left clicking the mouse skips the user to the previous level if it exists. Right clicking the mouse
 skips the user to the next level if it exists. If there are no levels left and the user right clicks, it brings the
 user to the "You Won" screen.

 * Cheat Key 4: Pressing 'p' automatically breaks all blocks in the level that contain power ups. The released power ups
 move down to the bottom of the screen, possibly moving through other blocks if other blocks are blocking their way to the bottome. 
 It is possible in this game to have multiple power-ups activated by the user simultaneously.


#### Level Descriptions

 * Level 1
   * Block Configuration

   * Variation features

 * Level 2
   * Block Configuration

   * Variation features

 * Level 3
   * Block Configuration

   * Variation features


### Possible Classes

 * Class 1
   * Purpose

   * Method

 * Class 2
   * Purpose

   * Method

 * Class 3
   * Purpose

   * Method

 * Class 4
   * Purpose

   * Method

 * Class 5
   * Purpose

   * Method

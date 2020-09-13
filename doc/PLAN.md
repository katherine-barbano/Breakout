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

In these block configurations, numbers represent how many hits are required to break
the block. For power up blocks, blocks labeled "P" represent blocks that drop power-ups
that increase the size of the paddle. Blocks labeled "S" drop power ups that slow
down the ball. Blocks labeled "B" drop power ups that allow the ball to break any
hardness of block with one hit. Next, blocks labeled "M" represent blocks moving in a row, with the position of the block in the diagram
specifying the block's starting position.

Block configurations must be exactly 12 columns and 8 rows. Text
files that specify block configurations must have a "0" to represent a space where
no block is present.

 * Level 1
   * Block Configuration
   
   0 0 0 0 0 0 0 0 0 0 0 0
   
   3 3 3 1 2 2 2 1 3 3 3 3
   
   1 1 3 1 2 1 2 1 1 1 1 3
   
   3 3 3 1 2 1 2 1 1 1 3 1
   
   1 1 3 1 2 1 2 1 1 3 1 1
   
   3 3 3 1 2 2 2 1 3 1 1 1
   
   0 0 0 0 0 0 0 0 0 0 0 0
   
   0 0 0 0 0 0 0 0 0 0 0 0

   * Variation features: This level contains blocks of varying hardness. Because
   the blocks are different colors based on their hardness, the colors will spell
   out the numbers "307". There are no power ups available in this level.
   

 * Level 2
   * Block Configuration
   
   0 0 0 0 0 1 1 0 0 0 0 0
   
   0 0 0 0 1 0 0 1 0 0 0 0
   
   0 0 0 1 0 2 2 0 1 0 0 0
   
   0 0 1 0 2 S B 2 0 1 0 0
   
   0 0 0 1 0 2 2 0 1 0 0 0
   
   0 0 0 0 1 0 0 1 0 0 0 0
   
   3 S 3 0 0 1 1 0 0 3 B 3
   
   0 0 0 0 0 0 0 0 0 0 0 0
   

   * Variation features: The configuration of blocks is shaped differently
   from a basic rectangle. Power-ups to make the ball strong or to slow the ball
   down are available in some blocks. These power-ups drop down once the blocks
   are broken, and are activated if they touch the paddle. This level also
   contains blocks of varying hardness again.

 * Level 3
   * Block Configuration
   
   M 0 0 0 0 0 0 0 0 0 0 0
   
   1 2 B 2 1 1 1 1 2 S 2 1
   
   0 0 0 0 0 0 0 0 0 0 0 M
   
   1 1 1 0 2 3 3 2 0 1 1 1
   
   M 0 0 0 0 0 0 0 0 0 0 0
   
   0 P 0 0 3 1 1 3 0 0 P 0
   
   0 0 0 0 0 0 0 0 0 0 0 M
   
   0 0 0 0 0 0 0 0 0 0 0 0

   * Variation features: This level contains moving blocks. It also contains
   blocks that drop all three types of power-ups, including the power up
   that extends the size of the paddle. It also still contains blocks of varying
   hardness.


### Possible Classes

 * Class 1: PowerUp.java (interface)
   * Purpose: This object is released from breaking a BlockContainingPowerUp. The
   powerUp object moves down the screen, potentially through other blocks obstructing
   its path, towards the bottom of the screen. If paddle touches a PowerUp, the PowerUp's
   positive effect is activated.
   
   Implemented by subclasses of PowerUp.

   * Method: activateAbility, deactivateAbility, isTouchingPaddle, fallToBottom

 * Class 2: ExtendedPaddlePowerUp.java (implements PowerUp)
   * Purpose: A yellow circle that moves down the screen. If it touches the paddle,
   the paddle object's size is set to a larger size for 20 seconds

   * Method: all methods in PowerUp, extendPaddleSize

 * Class 3: SlowBallPowerUp.java (implements PowerUp)
   * Purpose: A purple circle that moves down the screen. If it touches the paddle,
   the speed of the Ball object is set to a slower speed.

   * Method: all methods in PowerUp, setBallSpeedToSlow

 * Class 4: StrongBallPowerUp.java (implements PowerUp)
   * Purpose: A pink circle that moves down the screen. If it touches the paddle,
   the ball's color changes to pink (so the user knows a change happened), and
   it sets the strength of the ball to 3 for 20 seconds.

   * Method: all methods in PowerUp, setBallColor, setBallStrength

 * Class 5: Block.java
   * Purpose: A rectangle that is removed from obstructing the Ball's movement
   if the hardness becomes 0. Contains a hardness field from 0 to 3. The Block's 
   color corresponds to the current hardness, which is updated if it is hit by a 
   Ball.

   * Method: updateHardness
   
 * Class 6: PowerUpBlock.java (extends Block)
    * Purpose: Has a hardness of 1 until it is hit, and turns to 0. Drops PowerUp
    when hit.
 
    * Method: dropPowerUp
 
 * Class 7: MovingBlock.java (extends Block implements BlockRow)
    * Purpose: Moves horizontally across the row, in which it is the only block.
    Bounces off left and right sides of screen, and obstructs movement of
    the Ball. Has a hardness of 1 until it is hit, and turns to 0.
 
    * Method: move
    
 * Class 8: BlockConfiguration.java
     * Purpose: Reads in a text file into an array (size 8) of BlockRows
     Throws exception if any text file does not provide a valid level.
  
     * Methods: readTextFile, setBlockRows
     
 * Class 9: BlockRow.java (interface)
     * Purpose: Interface for a row of blocks in the block configuration. Its subclasses,
     MovingBlock and FilledBlockRow, should be instantiated.
  
     * Method: displayBlocks
     
* Class 10: FilledBlockRow.java (extends BlockRow)
  * Purpose: Contains private array (size 12) of multiple Block objects. Array
  might contain empty values if no blocks are present in that space from the text file.
  
  This is a subclass of BlockRow. It only contains Block or PowerUpBlock. It cannot
  contain MovingBlock, since a single MovingBlock will take up the entire row.

  * Method: setBlocks, displayBlocks

 * Class 11: Paddle.java
    * Purpose: A small rectangle at the bottom of the screen that a Ball
    can bounce off of.
 
    * Method: collision, setSize
    
* Class 12: Ball.java
    * Purpose: A circle that breaks Blocks, and ends the game if it crosses the 
    bottom of the screen. Bounces off all other walls of the screen, as well as 
    the Paddle and other Blocks.
    
    * Method: isOnScreen, breakBlock, isTouchingBlock, isTouchingPaddle

 * Class 13: Level.java
    * Purpose: Contains private field for a BlockConfiguration. Maintains
    number of lives left. Starts a game if the user hits the space bar. Maintains
    cheat keys. Ends game if lives run out, or ends the current Level
    if BlockConfiguration does not contain any more Blocks.
 
    * Method: endGame, finishLevel, updateLives
 
 * Class 14: Game.java
    * Purpose: Creates a new BlockConfiguration instance for each text file. Create a new
    Level for each BlockConfiguration. Iterates through all Levels (or
    at least until a Game Over) and displays a "You Won" screen at the end.
 
    * Method: start, createBlockConfiguration, createLevels, runLevels, gameOverDisplay, youWonDisplay
 
 * Class 15: Main.java
    * Purpose: Calls launch(args) to start the program.
 
    * Method: main
 
 * Class 16: MainTest.java
    * Purpose: Contains JUnit tests for the program.
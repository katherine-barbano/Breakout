game
====

This project implements the game of Breakout.

Name: Katherine Barbano (kab134) and Anna Diemel (ad356)

### Timeline

Start Date: 9/12/2020

Finish Date: 9/28/2020

Hours Spent: Katherine - Around 100
             Anna - 50-60

### Resources Used
- Used a few lines of code from the following source that gives a proof
for how to calculate whether a point overlaps between a circle and rectangle
(used in Ball and Paddle classes):
https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
- Included DukeApplicationTest, written by Robert Duvall,
 in test/util to allow test classes to extend this class. In GameTest,
 a line was taken from DukeApplicationTest with a few arguments changed,
 since the original class could not handle testing right clicks.
- Used a line of code from the following source that explains how to list
all the files within a given directory in Java:
http://zetcode.com/java/listdirectory/
- Referenced the following to generate random integers: // used https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java


### Running the Program

Main class: Run Main.java to interactively play the game yourself.
All tests are contained in the test directory. To run all tests at
the same time, right click the test folder, then click "Run 'All Tests'".

Data files needed: The "data" folder should be marked as a Resources
root, and the "test" folder should be marked as a Test Sources root.

Key/Mouse inputs:
- Moving the paddle: Use the right and left arrow keys to move
the paddle horizontally across the screen.
- Begin the game: Tap the space bar to begin the movement of the ball
and paddle. At the end of the game, tap the space bar to restart the
game from level 1 again.

Cheat keys:
- "R": Resets the position of the Ball and Paddle to the original
starting position within the current level. This also pauses the game.
- "SPACE": Pauses or unpauses the game. If the game has not been
started yet, it begins the movement of the Ball. At the end of the game, tap the space bar to restart the
game from level 1 again.
- "L": Adds one extra life to the player for the current level.
- Right mouse click: Skips to the next level. If the current level is the last level,
this click does nothing.
- Left mouse click: Skips to the previous level. If the current level is
the first level, this mouse click does nothing.
- Numeric keys (e.g. "1", "2", or "3"): Jumps to the level associated with that value. If that level
does not exist, no action is taken.
- "D": Deletes the first block in the level and adds 5 to score. First block is defined as the block that exists farthest to the top left
of the screen.
- "P": Drops the first power up in the level. First power up is defined as the one in the block that exists farthest to the top left of the screen.
- "T": Adds 10 seconds to the time for the current level.
- "K": Drops all power ups in the level at one time.
- "S": Decreases the score needed to win the current level by 10 points.

Known Bugs:
- If the level changes when there are still power ups that haven't been accepted by the paddle or
fallen off the screen, they remain on the screen the rest of the game.
- If a power up is dropped using P or K when the game is paused,
it will disappear instead of drop down. The P and K cheat keys also only work if the game is not
paused (otherwise they just destroy the block with power ups and nothing falls down).
- If you win/lose once, then restart and go to the last level, then
repeatedly decrease the score needed to win with the S cheat key, it will not decrease
all the way to 0.
- If you exceed minimum score, the paddle freezes. Something about totalScore/getLevelScore
is prompting level gameIsPaused = true.

Extra credit: Added you won/you lost screen at end. Implemented a game timer to countdown. Must surpass
a score by the end of the timer to move onto the next level. Because
blocks only add 5 points if they are completely broken (given that blocks
are varying hardnesses), this feature
gives a different playing experience for every level. The player has to
adjust their playing technique for each level by deciding which hardness
blocks are better to go for in that level based on their current score,
which power ups would actually hinder their ability to gather sufficient points,
etc. so each level is functionally different for the user, since the game
goals are changing each level.
Implemented a scoreboard to accompany the you won/you lost screen. As the Game is played in a given
window, the Game compiles a list of the highest scores achieved so far and displays them if the
player's game terminates. If not enough games have been played to populat the entire score board,
the unpopulated high scores say TBD.


### Notes/Assumptions
- To change which levels/games are being played, you need to go into
the config.properties file and change game_name variable to which folder
in the data directory you want to play.
- Each directory within the "data" folder represents a game that can
be played through. Each file within this directory represents a single
level. The filename of each level should contain a positive integer
that corresponds to which level it represents in the game. There should
be no other numerics present in the level filename. For example,
"level_1.txt" would be a valid filename.
- Each txt file for a level should contain 8 rows and 12 columns of
blocks, where each character represents a block. Each character in the
file should be separated by a space from the next character in that row.
Block rows are separated by a new line.
    - "0" represents no block being present in that position.
    - "1" represents a block of hardness 1 (needing only 1 hit to break it), color pale turquoise.
    - "2" represents a block of hardness 2 (needing 2 hits to break it), color pale violet red.
    - "3" represents a block of hardness 3 (needing 3 hits to break it), color purple.
    - "V" represents a stationary block that randomly changes hardness and can only be broken
    when its hardness is 1.
    - "P" represents blocks that drop power-ups that increase the size of the paddle, color light green.
    - "S" represents blocks that drop power ups that slow down the ball, color light blue.
    - "B" represents blocks that drop power ups that allow the ball to break any
      hardness of block with one hit, color orange.
    - "M" represents blocks moving in a row, with the position of the block in the diagram
      specifying the block's starting position. They are set to a random hardness value.
- Moving blocks should only be created in a row with no other blocks present.
This means that if a level file contains an "M", the rest of the row with
the "M" in it should be all "0"s.
-In addition to the level files within a game, each game must also
have a file without any numeric value in the filename. Each row on this
file represents a single level. Each row should have two numbers separated
by a space: first, a number specifying the score needed to win that level,
and second a number specifying the time limit of the level. See data/sample_game/level_scores_to_win_and_time
as an example.

### Impressions
Learned about the importance of inheritance and open-closed principle.
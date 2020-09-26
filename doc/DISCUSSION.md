## Lab Discussion
### Team 25
### Names Anna Diemel (ad356) and Katherine Barbano (kab134)


### Issues in Current Code
Bugs exist relating to level scrolling, score display during level scrolling, decrementing blocks.

#### Method or Class
 * Design issues
 Refactor GameText to be an abstract class instead of an interface to reduce duplicated code in subclasses.

 * Design issue
 addText and removeText are very similar between GameText subclasses, so code is duplicated.

#### Method or Class
 * Design issues
 Refactor Ball and Block collisions into several methods.

 * Design issue
The method handling Ball and Block collisions right now does many functions within one method, so
we are going to separate it out into multiple methods.

### Refactoring Plan

 * What are the code's biggest issues?
 Refactoring current methods, as above, to each complete a single function. Some methods that are too
 complex include: Ball updateVelocityY, handleFoundPowerUpInBlock, makePowerUp, BlockConfiguration 
 updateConfiguration, getBlocksAsList, etc.

 * Which issues are easy to fix and which are hard?
 Adding additional Exception handling, getting rid of printStackTrace will be easy to implement.
 It will be harder to refactor the methods to complete a single function.

 * What are good ways to implement the changes "in place"?
 Using IntelliJ's feature to refactor and extract method will be very useful. Also using
 TDD to make the changes will be helpful not only to help refactor, but also to expand
 the test cases of the current code.


### Refactoring Work

 * Issue chosen: Fix and Alternatives
Make GameText an abstract class instead of interface, with concrete methods to reduce duplicated code.

 * Issue chosen: Fix and Alternatives
Add a properties file to store all of the constant values globally instead of instantiating them
within the classes.
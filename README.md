# rkanoid-like-Game
## Objectives

This assignment requires you to implement a simple 3D game using the jMonkeyEngine 3.0 library.

## Game Description

The objective of this assignment is to implement a game inspired by the classic arcade
game Arkanoid, see, for example, https://en.wikipedia.org/wiki/Arkanoid and https:
//youtu.be/--pBWsS867s. The game is played on a rectangular field with solid top and
sides and an open bottom. In the middle of the field, game objects are positioned. A player
controls a moving paddle at the bottom of the field, which prevents the game ball from
falling from the field. The game ball bounces off the paddle, the boundaries and the game
objects on the field. When the game ball strikes a game object on the field, the object is
removed. When all objects are gone, the player wins the round.
In the classic game, the game objects are rectangular. In this assignment, we will use
balls as obstacles.

## Explanation of the extra features:

### 1.Textures: 
I download several 3D Blender resources from google. The stationary balls are billiard ball with different colors. The game ball is a football. The playing field is a billiards table. The background is the wooded board. The paddle will change its color every time the game ball collides with the it. 

### 2.Sound effects:
The game has four different sound effects. The first one is the collision sound when the game ball hits the table or the paddle. The second one is the collision sound when the game ball bounces off the stationary balls. The third one is the applause audio when the user finishes all three levels of the game. The last one is the background music of the game. It starts playing when the game starts. 

### 3.Stationary balls forming interesting shapes
The stationary balls form a triangle at the first level. At the second level they form a square. At the last level, they form a pentagon.

### 4.More realistic physics 
The game ball has shadow with it when moving inside the playing field. This makes the physics more realistic. 

### 5.Levels of difficulty
At the first level, there are only three stationary balls and the game ball moves slowly. This can help the user get used to the game. When the user proceeds to the second level, there are one more stationary ball and the game ball is accelerated. The last level is the most difficult level. There are 5 stationary balls and the game ball will move very fast. After finishing all three levels, the user will win and the winning interface will be displayed. 

### 6.Pause and restart the game
Once the user presses KEY A, the game ball will be paused. In order to restart the game, the user has to press KEY D.

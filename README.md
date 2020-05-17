# Raycaster
## For the TI-99/4A home computer

April 2020, Rasmus Moustgaard <rasmus.moustgaard@gmail.com>

## Building

Build on Windows by running make.bat.

Building requires the xdt99 cross-development tools (https://github.com/endlos99/xdt99) to be available on the path.

To build the cartridge, Java must be available on the path.

## Running

Running requires a TI-99/4A or emulator with 32K memory expansion.

* From disk run E/A#5: DSK1.RAYCASTER
* From cartridge load either raycaster8.bin or raycaster.rpk

## Algorithm

The world is divided into 128 directions, and for each direction a unit vector (cos(a), sin(a)) is pre-calculated in Java and stored as two 8.8 fixed point numbers, i.e. multiply floating point number by 256, round, and store in a 16-bit word. So the output from the Java program is a list of 128 x values and 128 y values as TI-99/4A assembly DATA statements.

The player's position is also stored as two 8.8 fixed point numbers (x and y) and the player's angle is stored as one of the 128 directions.

For each frame we cast 32 rays from the player's position, each in a different angle, with the player's current angle at the center, so from angle - 16 to angle + 15.

The casting algorithm is using elements from two different tutorials:

* https://lodev.org/cgtutor/raycasting.html
* http://www.permadi.com/tutorial/raycast/rayc1.html

The DDA algorithm for finding the next intersection point is based on the former, while the idea of using polar coordinates instead of vectors is taken from the latter. 

After finding the distance, the perpendicular distance is found by multiplying by cos(a).

The screen drawing routine is using pre-drawn column in order to send the data to the VDP as fast as possible, using an unrolled loop from scratch pad RAM. The screen is double buffered using two name tables for a flicker free experience.

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

The algorithm is a simple raycaster where reality is sacrificed for speed.

The world is divided into 128 directions, and for each direction I pre-calculate a unit vector (cos(a), sin(a)) in Java and store it as two 8.8 fixed point numbers, i.e. multiply floating point number by 256, round, and store in a 16-bit word. So the output from the Java program is a list of 128 x values and 128 y values as TI-99/4A assembly DATA statements.

The player's position is also stored as two 8.8 fixed point numbers (x and y) and the player's direction is stored as one of the 128 directions.

For each frame we cast 32 rays from the player's position, each in a different direction, with the player's current direction at the center, so from direction - 16 to direction + 15.

Each ray start with the player's position, and we keep adding the unit vector in the given direction until we hit a wall or give up. So for each iteration we need to check what the map contains at the given position. To do this as quickly as possible we make the map width 256 so the map address can be calculated as 256 * y + x + base, which it's very fast to do in assembly.

So the central loop of the raycaster looks like this, where (r0,r1) contains the current (x,y) position, (r3,r4) contains directional unit vector, and r8 is a counter for max iterations:      

```
cast_ray_1:
       a    r3,r0                      ; x += xdir
       a    r4,r1                      ; y += ydir
       movb r1,r6                      ; y -> r6 msb
       movb r0,*r5                     ; x -> r6 lsb (for a bit of speed, r5 contains the address of r6 lsb)
       movb @map(r6),r7                ; Get map entry
       jne  cast_ray_2                 ; Not zero is a hit
       dec  r8                         ; Distance count down
       jne  cast_ray_1                 ; Loop until we give up
```

The value of r8 after the loop determines the distance we have traveled, and the value of r7 determines what we have hit. From the distance we can calculate the height of the wall at the given screen column. The formula is something like max_height / distance, but I use a look-up table.

Calculating distances from a single point is resulting in a fish-eye view of the world, as you can see in the demo. A more sophisticated algorithm would calculate the perpendicular distance to the view plane instead. I think it should be possible to fix this without sacrificing speed by having an additional correction look-up table.

We now have a height for the walls for each of the 32 screen columns. For each column we want to draw a strip of sky, then a strip of wall, and finally a strip of floor. To do this most efficiently I have pre-drawn the columns at different wall heights in Magellan. That also makes it easy to add the primitive 'textures' you see on the demo. 

Drawing columns one by one is bad for performance because you have to set up the VDP write address for each row, so instead we set up the write address once and then draw one byte from each column in turn. To fetch the bytes to draw we set up a pointer for each column that point to the right column data. The drawing loop looks like this:

```
upload_screen_loop:
       li   r1,column_ptrs
       li   r2,screen_width
upload_screen_loop_1:
       mov  *r1,r0                     ; Get column pointer
       movb *r0+,*r15                  ; Write byte to VDP (r15 contains VDPWD)
       mov  r0,*r1+                    ; Write pointer back
       dec  r2
       jne  upload_screen_loop_1       ; Next column
       dec  r3
       jne  upload_screen_loop         ; Next row
       rt
```

Finally, to push the last bit of performance out of the TI-99/4A, I run the two central loops from scratch pad RAM.    
 
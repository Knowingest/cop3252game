Game cannot be "won" until the cube has been scrambled.

Scrambling and resetting the cube is possible via the menu bar.

Each button on the UI corresponds to a move, using the standardized speedcubing notation

I put a couple reference images in this directory, as well as a .pdf of a popular beginners guide from
a man that goes by "badmephisto", who made some of the most popular intro guides to cubing.

Speedcubing notation is also easily found online.

After scrambling, the program will congratulate you upon completion.




//NOTES

/*
The program is relatively robust I think.
The average user probably won't get to see the "Congratulations!" dialog box, but I can 
confirm that it works.

the game starts when the cube is scrambled, and it can detect when it is solved.
I can send in a video or something upon request, as someone who doesn't regularly solve will
have a hard time testing this.

The UI includes all of the basic moves that a regular solver would need

it is missing the lower case face moves, but those are rarely used, and easily
emulated using the other moves.  (f -> F + S for example)

someone who solves regularly can use this program without much difficulty.
 
At first it can be a bit confusing to look at,
but I was still able to solve it within ~2 min or so without any practice
*/

/*
    Things I would improve?

    * add the lower case face moves

    * add reference materials WITHIN the program, 
        instead of just packaging them in the directory

        (though this program is mostly for people who can already solve a cube anyway)

    * make UI prettier, use something other than the default JButton graphics, that sort of thing

    * if this was something I was being paid to make, I might try and optimize and automate more code.
        (IE, hardcode less things)
        but all in all I think the codebase is ok.  It cuts out blatant repitition when possible.
            maybe split the program in to more files to make it easier to read?


    there's also a small writeup in the Rubik.java sourcecode that explains why the files are so long
*/
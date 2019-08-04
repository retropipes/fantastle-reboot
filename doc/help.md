**Fantastle Manual**

Version 5.0.0-dev5 - Last Updated January 24, 2010

***Game Objective***

The overall goal of the game is to find and enter the finish for the
particular maze being solved. Since you can make your own mazes, the
exact way this is accomplished differs from maze to maze.

***Menu Overview***

**File**

*New*: Creates a new Maze. You can specify its dimensions. Rows and
Columns cannot be less than 2 squares nor greater than 64 squares, and
Floors and Levels not less than 1 nor greater than 8. A new Maze always
starts with 1 Level - more can be added later.

*Open*: Opens an existing Maze.

*Close*: Closes the current Mode Window (Game or Editor, not the current
Maze).

*Save*: Saves the current Maze.

*Save As*: Saves the current Maze with a new name.

*Exit* (non-Mac OS X platforms only): Quits the Program.

**Edit**

*Undo*: Undoes the most recent action. This command is available only in
the Editor.

*Redo*: Redoes the most recent action. This command is available only in
the Editor.

*Cut Level*: Copies the active Level to the internal clipboard, then
removes the Level. Not available if the active Maze has only one Level
in it. This command is available only in the Editor. Note that Levels
cannot be cut, copied or pasted outside Fantastle, but Levels can be
moved or copied from one Maze to another.

*Copy Level*: Copies the active Level to the internal clipboard. This
command is available only in the Editor. Note that Levels cannot be cut,
copied or pasted outside Fantastle, but Levels can be moved or copied
from one Maze to another.

*Paste Level*: Pastes the clipboard contents into the active Level,
overwriting whatever is there, and then redraws the Editor. This command
is available only in the Editor. Note that Levels cannot be cut, copied
or pasted outside Fantastle, but Levels can be moved or copied from one
Maze to another.

*Insert Level From Clipboard*: Pastes the clipboard contents into a new
Level, appending it to the end of the Maze. This command is available
only in the Editor. Note that Levels cannot be cut, copied or pasted
outside Fantastle, but Levels can be moved or copied from one Maze to
another.

*Go To...*: Prompts for a location, then moves the Editor view to be
centered on that location.

*Preferences*: Allows configuration of various Game and Editor Settings.

*Clear History*: Clears the Undo/Redo History. This command is available
only in the Editor.

*Up One Floor*, *Down One Floor*, *Up One Level*, *Down One Level, Add a
Level\..., Remove a Level\...*: All these commands work only in the
Editor, and are self-explanatory.

*Resize Current Level*: Changes the size of the active level. This
command is available only in the Editor.

*Toggle Layer*: Switches between Ground Layer and Object Layer views.
This command is available only in the Editor.

*Maze Preferences\...*: Opens the Maze Preferences dialog, where
settings specific to the current Maze can be set. This command is
available only in the Editor.

**Play**

*Play*: Allows solving the current Maze.

*Edit*: Allows modifying the current Maze. Note that invoking the Editor
after opening a saved game will cause the saved state to be lost.

**Game**

*Show Equipment*: Shows everything you are wearing, along with the slot
it occupies and its power. If nothing is equipped in a particular slot,
this will be indicated. This command is only available while solving a
Maze.

*Show Inventory*: Shows all Items you are carrying. This command is only
available while solving a Maze.

*Use an Item\...*: Allows using anything you're carrying that can be
explicitly used. This command is only available while solving a Maze.

*Reset Current Level*: Resets the current Level of the Maze you're
solving to its unsolved state. This is handy if you get stuck. This
command is only available while solving a Maze.

*Show Current Score*: Displays how many Steps you've taken so far to
solve this Maze. This command is only available while solving a Maze.

*Show Score Table*: Displays the 10 best Scores for this Maze. If no
Scores have been recorded yet, all the Names and Scores will have
default values. This command is only available while solving a Maze.

**Debug**

*View Image Cache*:* *Peek at the contents of the Image Cache.

*View Monster Cache*:* *Peek at the contents of the Monster Cache.

*Reset Preferences*: Resets all game preferences to their defaults.

**Help**

*About Fantastle\....*: Displays the Game's About Box.

*Fantastle Help*: Displays this manual.

*Fantastle Object Help*: Displays a window listing all the Objects and a
short description of what each one does.

***Preferences Overview***

The Preferences are divided into four tabs: *Editor*, *Sound, Music,
*and *Misc*. *Editor* and *Sound* are for Editor-related and
sound-related settings, respectively. The *Music *tab controls whether
or not the background music plays in various parts of the game. The
*Misc.* tab has five settings: whether or not to check for updated
versions when the Game is started, whether or not to check for updated
beta versions at startup (if using a release version, this setting is
hidden), whether or not to operate in One Move at a Time mode, whether
or not to enable Mobile Mode (limits window size to 320 by 320 and uses
half-size graphics), and how frequently the automatic update check
should be performed (once per day is the default).

***Controls in the Game***

**Creating Your Character**

When you choose Play from the Game menu, you\'ll be presented with 5
dialogs, for selecting your Race, Caste, Faith, Gender, and Personality.
Each of these attributes affects you in different ways. There are
1,000,000 possible combinations, so feel free to experiment.

**Moving Around and Identifying Objects**

To move, use the arrow keys - Up, Down, Left, and Right. You can also
use the Numeric Keypad, or the so-called \"WAXD\" configuration: W moves
up, A moves left, D moves right, and X moves down. Additionally,
diagonal movement is supported with WAXD: Q moves up and left, E moves
up and right, Z moves down and left, C moves down and right. If you wish
to move diagonally, the Numeric Keypad or the WAXD layout are the only
ways to do this. Shift+Click an object to identify it. Its name will be
displayed in the Message Area. If, for some reason, you need to make a
\"pass move\" - that is, a do-nothing move, pressing 5 on the Numeric
Keypad or the S key achieves this.

**Shooting Arrows**

To shoot, hold down Alt/Option then use the arrow keys - Up, Down, Left,
and Right, use the Numeric Keypad, or the WAXD layout. If you wish to
shoot diagonally, the Numeric Keypad or WAXD are the only ways to do
this. Various Objects react to getting hit by arrows. You have an
unlimited supply of regular arrows.

**Shooting Enchanted Arrows**

First, activate the enchanted bow you wish to use by choosing *Use an
Item\...* from the Game menu. The next arrow you shoot will come from
the bow you just activated. After that, if you wish to shoot another
enchanted arrow, you must activate the bow again. Enchanted bows only
come with a limited arrow supply, unlike your regular bow.

**Pushing and Pulling**

To push an Object, just move into it. It will also move, if it can. To
pull an Object, hold Shift and move AWAY from it. It will follow you, if
it can be pulled.

**Seeing what you are carrying**

To see what Items (if any) you have in your possession, choose *Show
Inventory* from the *Game* menu. A dialog box will appear, listing
everything you can carry and what quantity you have.

**Using Items you are carrying**

To use something you're carrying that can be explicitly used (such as a
Wand), choose *Use an Item\...* from the *Game *Menu. This will bring up
a dialog listing what usable Items you have, as well as their quantities
and uses - choose which one to use and click the *OK* button. Unless you
used an enchanted bow, a prompt will appear in the Message Area at the
bottom: "*Click to set target*". Click the square in the Maze where you
wish the Item's effect to occur, and it will be activated. If you
realize you didn't want to use something after all, press *Escape*, and
the action will be cancelled. If you try to use an Item but fail (for
example, because the requirements for the target square were not met), a
message will appear in the Message Area indicating what went wrong.

Not all Items can be explicitly used in this way. Items that show up in
the Inventory Dialog but not in the Use Dialog are used automatically
when needed.

**Getting Help**

To find out what all the Objects do, choose *Fantastle Help* from the
*Help* menu. This will bring up a window that you can leave open while
playing the game, listing each object and a short description of how it
works.

***Controls in the Editor***

**Moving Around and Identifying Objects**

To move the view around, drag the scroll bars on the left. To see more
Objects to place, drag the scroll bars on the right. To change Floors
and/or Levels, use the *Up One Floor*, *Up One Level*, *Down One Floor*,
and *Down One Level* items in the *Edit* menu.

Shift+Click an object in the maze to identify it. Its name, along with
any additional properties it has and the values of those properties,
will be displayed in the message area.

**Placing Objects**

To place an object, first select one in the right pane. Then, click the
location in the maze where you want the object to go. If you need to
change layers in order to place objects, this can be done via the
*Toggle Layer* option in the Edit menu.

**Modifying The Maze Dimensions**

Mazes can gain or lose Levels as they are edited. To add a new Level to
a Maze, choose *Add a Level\...* from the *Edit* menu. You will be
prompted for the dimensions of the new Level. Likewise, removing levels
can be accomplished via the *Remove a Level\...* item in the Edit menu.
You'll be prompted for the Level number to remove. Note that if you
remove the current Level, you'll be sent to Level 1. You cannot give a
Maze more than 8 Levels.

**Setting Object Properties**

To set the properties of an Object, if it has any, hold the Option (or
Alt) key and click the Object in the Maze Pane on the left. If it has
properties you can set, a dialog or a message will appear. If it has no
properties, the message "*This object has no properties*" will appear in
the Message Area. Most Teleports will generate a message that says
"*Click to set destination*" - this is exactly like using an Item. Click
the spot where you want the destination to be. If you'd like the
destination to be on a Floor other than the current one, you can change
Floors before clicking. Random Teleports will generate a dialog asking
for the Random Row and Column radii. Pressing *Escape* will cancel
setting properties.

**The meanings of terms used in the Editor**

Rows and Columns refer to the horizontal and vertical lines of squares
making up the Maze Grid. Floors are stacks of Row/Column Grids. Levels
are stacks of Floors. Finally, every Maze has 2 Layers - the Ground
Layer (where Grass, Tile, and other types of ground belong) and the
Object Layer (where everything else belongs).

***Controls in Battle***

**Fighting Monsters**

To attack the enemy, either click the *Attack* button, or press A on the
keyboard. If you wish to attempt to flee, instead of attacking, click
the *Flee* button, or press F on the keyboard. Note that if your flee
attempt fails, you'll automatically attack instead.

**Special Actions**

Sometimes, when you attack (and also when the enemy attacks you),
different things may occur. One of these is fumbling - if this occurs a
random amount of damage will be dealt to the fumbler, based on the power
of the weapon held. Attacks can hit with varying amounts of force, from
pathetic to brutal. Attacks will sometimes pierce the armor of the
defender too\... so beware. Additionally, it is now possible for you
(and the enemy) to act multiple times in the same round... but only for
regular attacks, not any other actions.

**Casting Spells**

To cast a spell, assuming you have learned at least one, click the *Cast
Spell* button, or press C on the keyboard. The spell selection screen
will appear. Select the spell you wish to cast, and click OK (or simply
press Return on the keyboard). If you don't know any spells, or don't
have enough MP to cast the spell you're trying to cast, a message will
be displayed indicating this.

**Stealing Money**

To attempt to steal from the enemy, click the *Steal Money* button, or
press S on the keyboard. If the attempt succeeds, you\'ll be told how
much money you stole. Beware, though - regardless of whether the steal
attempt succeeds or fails, the enemy gets a free chance to hit you.

**Using Items**

To use an item, click the *Use an Item* button, or press I on the
keyboard. Items do various things when used in battle, from healing you
to damaging or incapacitating the enemy.

**Draining the Enemy**

To attempt to drain the enemy\'s MP, click the *Drain Enemy* button, or
press D on the keyboard. If the attempt succeeds, the enemy loses some
MP, and you gain that lost MP. Beware, though - regardless of whether
the drain attempt succeeds or fails, the enemy gets a free chance to hit
you.

**Victory! / Defeat!**

The battle ends when either you or the enemy has run out of HP. If you
win, and do not lose any HP, you get a perfect fight bonus in the form
of extra gold. If you lose, and fail to hurt the enemy, you get a
different message compared to losing normally. If both you and the enemy
run out of HP at the same time, the battle is declared a draw, and you
get fully healed. If you gain enough XP to level up, a message will be
displayed indicating this.

***Controls while Shopping***

**Buying Stuff**

First select what you wish to buy, and then confirm that you want to buy
it when the shopkeeper tells you how much Gold the item or service
costs. If you lack the Gold to purchase whatever it was you tried to
buy, you'll get a message indicating this. The Weapons and Armor shops
give some additional options for selecting exactly what you wish to buy
(and, for one-handed weapons, where to equip the purchased item), before
presenting a list of items to look through.

**Using the Bank**

The Bank, unlike other shops, stores money for later retrieval. It will
tell you how much Gold you are carrying, as well as how much you
currently have stored.

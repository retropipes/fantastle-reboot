**Fantastle Version History**

V5.00-dev5 (unreleased): Fixed a bug that made effects modifying Maximum
HP or Maximum MP not work correctly. Added combat-usable items to the
game.

V5.00-dev4 (10/31/2009): Fixed another crashing bug in the battle
engine. Added a new Extras menu, with options to start earlier major
versions of Fantastle, Dungeon Diver, and Maze Runner. All are embedded
into the program, and can be invoked at any time. The embedded versions
have had a few things changed, such as the removal of the update
checker, and the removal of the GUI for any associated preferences for
the update checker. All of the embedded programs have version numbers
newer than the last release for that particular series - this is
deliberate. Note that once one of the embedded programs has been
invoked, the only way to return to Fantastle 5, or switch to a different
embedded program, is to quit and re-launch the game. Removed the
preference for hiding the Debug menu - it is now always visible. Added 2
new objects: Controllable Teleports and One-Shot Controllable Teleports.

V5.00-dev3 (10/24/2009): Emergency fix for a crashing bug that made
V5.00-dev2 unplayable.

V5.00-dev2 (10/23/2009): Saved game format changed again, due to other
additions. Revamped the item system - got rid of the old weapons and
armor, and replaced them with 2 caste-specific weapon choices - one
1-handed, the other 2-handed - and 12 different kinds of armor, that are
the same for all castes. It is now possible to equip two one-handed
weapons at once, as well, but note: doing so forfeits your ability to
equip a shield. Added music to the game, and a new set of preferences
for controlling it. Changed the way monster names are generated: Now,
any monster can appear at any level. Dramatically increased the level at
which The Boss is encountered, from 13 to 60. Boosted the boss\'s bonus
level relative to yours. Monsters now have equipment of their own
(making the monsters a little tougher than they used to be). Added an
equipment viewer to the Game menu. Unified the Show Inventory dialog so
that it shows both objects and items in inventory, and made it
consistent in style with the Use an Item... dialog. The contribution to
defense rating by equipping armor is one-tenth of what it used to be;
this is of course offset by the multiple varieties of armor now
available. Adjusted the distribution of monster spells per level to
reflect these changes. Changed the Inventory Dialog to show Regular
Boots instead of Boots: None, when you aren\'t wearing any special
boots.

V5.00-dev1 (10/16/2009): Changed the maze and saved game formats. Older
mazes can be opened without conversion; older saved games cannot be
opened or converted. Removed the entire Message section of the
Preferences, and all the associated settings. All creatures (the player,
monsters, and the boss) now have 6 new statistics. Added a bunch of
support code to use these new statistics. Improved the battle engine: It
is now possible to steal Gold from the enemy, or drain the enemy's MP.
It is also possible for you and the enemy to act more than once per
round. Redid the player attributes system: Got rid of the old Classes
and replaced these with Races, Castes, Faiths, Genders, and
Personalities. There are now 1,000,000 possible starting characters.
Added a new Mobile Mode preference, which, when enabled, restricts
window sizes to 320x480 and uses half-size graphics, intended to
simulate use of Fantastle on a mobile device (iPod touch, smartphone,
PDA, etc.). Added a new loading screen to address the long time for the
game to start initially.

V4.40 (10/10/2009): Added support for cutting levels, inserting levels
from the clipboard, and going to a specific location in the editor.
Refactored the code internally.

V4.30 (10/04/2009): Modernized the maze loader. Added support for
copying and pasting levels within the editor.

V4.20 (08/31/2009): Fixed several compatibility problems with Fantastle
and Snow Leopard (Mac OS X 10.6). Added the ability to resize levels, in
addition to the existing add/remove level functionality. Added a
meaningful error message for when an attempt to open a file fails
because it\'s not a maze file.

V4.10 (08/28/2009): Renamed many objects. The letter keys and locks are
now plugs and ports, and Rock is now Dirt, thanks to a user suggestion.
Many objects also got graphical upgrades or tweaks, thanks again to a
user suggestion. Rearranged the objects so that similar objects are
together. Added support for the \"WAXD\" control scheme as an
alternative to the numeric keypad, for users with keyboards that do not
have a numeric keypad. See the manual for details on how the \"WAXD\"
scheme works.

V4.00 (08/19/2009): Improved the user interface on all platforms. Fixed
an old editor undo/redo bug that could cause objects to end up in places
they shouldn\'t be. Fixed a battle bug that caused damage to be doubled
unexpectedly. Added 16 new objects. Once more, the maze and saved game
formats have changed. Just like the last time this happened, older mazes
can be opened without conversion, but older saved games cannot. Cracked
Walls and Bombs have been repurposed. All older mazes containing these
objects will still work - Cracked Walls become Brick Walls, and Bombs
become Hammers. Their functionality remains the same as before, despite
the appearance and name changes. Additionally, the scores and
preferences file formats changed in a backward-incompatible way. The
game now remembers the last used folder for opening and saving. Removed
some old bits of code that are no longer used. Fixed an old redraw bug
and an effect scaling bug. Fixed 2 old editor bugs.

V3.21 (06/11/2009): Fixed 5 bugs accidentally introduced in V3.20. Oops!

V3.20 (06/06/2009): Restructured the code internally. Added weaker than
normal hits to battles, as well as stronger than normal hits. Changed
the message displayed when one of these hits occurs.

V3.10 (05/21/2009): Fixed bugs. Added player and monster fumbles to
battles. Made the updater a lot smarter.

V3.00 (05/17/2009): Added 45 new objects. Changed the maze and saved
game formats again. Older mazes can be opened without conversion. Older
saved games cannot be opened or converted. Merged Dungeon Diver\'s code
into Fantastle, and enhanced it.

V2.10 (03/17/2009): Added the ability to add and remove levels while
editing a maze.

V2.01 (03/12/2009): Fixed 5 bugs.

V2.00 (03/07/2009): Added 119 new objects. Changed the maze and saved
game formats. A converter is now supplied with the game to convert older
mazes, but saved games cannot be converted. Removed the dynamic limits
introduced in V0.80 beta and made them static instead.

V1.11 (01/10/2009): Fixed bugs.

V1.10 (01/01/2009): Added 3 new options to the Game menu - Reset Current
Level, Show Current Score, and Show Score Table. Fixed even more bugs.
Added score tracking support - the game now tracks the number of steps
taken to solve a maze, and displays the 10 lowest step scores.
Reorganized the code internally. Changed the saved game format in such a
way that the new format is 100% compatible with older versions of the
game, and the old format 100% compatible with new versions.

V1.01 (12/27/2008): Fixed a few more bugs.

V1.00 (12/18/2008): Added an About Dialog to the game. Fixed the last
few remaining bugs. Created an installer for Windows users.

V0.80 beta (12/15/2008): Fixed an issue with large maze creation times.
Imposed new limits on maze size to fix an issue that caused mazes near
the memory limit to not work properly. The limits work like this: There
is an absolute upper limit on all dimensions of 64. Additionally, floors
and levels are subject to a more restrictive upper limit that is
dependent on the rows and columns chosen. This limit shrinks with
greater row/column dimensions. Upgraded a few graphics - the appearance
of cracked walls, water, and sunken blocks has changed, hopefully for
the better.

V0.71 beta (12/07/2008): Fixed many bugs. Made random teleports a lot
less frustrating than they used to be - now they\'ll always send you
somewhere the first try.

V0.70 beta (12/04/2008): Fixed bugs and reorganized the code internally.
Added a new sound for when push attempts fail.

V0.60 beta (11/09/2008): Second beta release. Sound support added.
Preferences interface revamped - now divided into four tabs. Many new
preferences added for turning sound effects on and off. Many bugs fixed.

Note to Mac OS X users: Due to a Mac OS X-specific bug in V0.50 beta,
the extension of the preferences file has changed for Mac OS X users
only, from plist to prefs. Your old preferences from V0.50 will NOT be
automatically imported, because this bug caused the program to crash.
However, you can export your preferences from V0.50 then import them
into V0.60 to work around this bug.

V0.50 beta (11/01/2008): First beta release. All known bugs fixed. The
program now comes with a Maze Runner maze converter that updates Maze
Runner mazes to the new format.

V0.40 alpha: Fixed more bugs, added export/import support to
preferences.

V0.30 alpha: Fixed bugs.

V0.20 alpha: Fixed bugs, added updater feature.

V0.10 alpha: Took the Maze Runner V2.21 code base and completely
overhauled it. This new code base is still fairly buggy.

**Maze Runner Version History**

V2.21 (08/26/2007): Fixed a bug in the editor that caused placing
objects not to work properly.

V2.20 (08/15/2007): Added undo and redo support to the editor. Added an
option for clearing the undo/redo history to the Edit menu. Added a
close command to the File menu. Removed the restriction preventing
replaying a maze without reloading it - this also allows going into the
editor and editing a maze right after playing it, as well as playing a
maze as soon as it\'s been created in the editor. Somehow, I managed to
break two-way teleporters in the editor in V2.10 - they\'re fixed now.
Cleaned up the version history a bit. Made some internal changes that
should minimize the chances of certain types of bugs rearing their ugly
heads.

V2.10 (08/09/2007): Added menus to all screens. Removed the requirement
to reload the maze after the graphics set or size is changed. Added the
ability to change the graphics set and size, or any other preference,
while playing or editing the maze, and made the program instantly
reflect the new settings. Added a new menu - the Game menu - and two new
menu items: Show Inventory and Use an Item. Added accelerator keys to
all menu items except Exit. Removed the alternate key bindings
introduced in V2.01 since they aren\'t needed anymore, and were
responsible for several bugs.

V2.01 (07/28/2007): Added alternative key bindings for Inventory, Save,
and Use to the game and editor. Fixed an issue with the Mac OS X release
where the menus weren\'t showing up in the Mac OS menu bar as they were
supposed to.

V2.00 (06/30/2007): Added 18 new objects: Pits (dump the player, a
pushable block, or a pushable/pullable block down one floor when walked
on or a block is pushed into it - impassable on floor 1 but can be
filled by pushing a block into one), Tile (a variety of ground that
permits objects to be pushed and pulled over it), Pushable Blocks,
Pullable Blocks, Pushable/Pullable Blocks, Ice (a frictionless object -
anything that touches ice keeps moving until it hits something that
isn\'t ice), Finish To\... (a special exit that takes the player to a
level other than the next one in the maze), Sunken Blocks (created by
pushing blocks into water - can be walked on and have objects pushed and
pulled over them), Water (normally impassable, but can be walked on if
the Water-Walking Boots are in your inventory), Water-Walking Boots
(permit walking on water), Annihilation Wand (destroys anything except
the Void), Finish-Making Wand, Wall-Making Wand, Energy Sphere (permits
walking on force fields), Force Field (normally impassable unless an
Energy Sphere is in your inventory), Teleport Wand (acts like a
teleporter when used, but you can control the destination), Void
(surrounds the maze now, instead of walls, and is completely
indestructible - it is immune to all wand effects; additionally, appears
in the game as a Sealing Wall if next to anything other than other Void
squares), and Exploding Walls (explode when touched, causing other
exploding walls next to it to also explode). Completely overhauled the
graphics - there are two new sets now, instead of the \"Default\" set
that older versions had, called Modern and Classic. Added multi-level
support - now mazes with multiple levels can act as several distinct
mazes, each with its own start and finish. If a Finish To\... is used to
send the player to a level they have already completed, that level will
be restored to a pristine state. Made a major change to the saved game
format, completely breaking backward compatibility with older saved
games. Older mazes, though, still work. Fixed many, many bugs.

V1.40 (06/13/2007): Added 1 new object - two-way teleporter (behaves in
pairs, just

like stairs) - and fixed several bugs. Added a status bar at the bottom
for displaying in-

game and in-editor messages. Added color graphics (this was present in
V1.30 also,

but never documented). Turned the \"Can\'t go that way\" message on by
default again.

V1.30 (06/09/2007): Added 6 new objects: stairs up, stairs down (stairs
behave in

pairs), and 4 kinds of one-way wall. Fixed a bug in the editor that
caused the

coordinates to place an object to not be computed properly.

V1.20 (05/11/2007): Added 6 new kinds of teleporter -- random, random
invisible, one-

shot, invisible one-shot, random one-shot, and random invisible
one-shot; teleporter

destinations are now set by clicking on the location for the teleporter
to teleport to;

added the ability to go back to the menu after solving a maze or editing
a maze;

changed message displayed when the player attempts to move outside the
maze to

"Can't go that way" and turned that message off by default; added a
check for the

current maze or game needing to be saved, and a prompt for the user to
save or not;

fixed numerous bugs.

V1.11 (05/07/2007): Fixed a bug in the editor.

V1.10 (05/04/2007): Improved user interface. Added preferences dialog.

V1.00 (05/02/2007): Initial Release.

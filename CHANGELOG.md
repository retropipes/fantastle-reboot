# **Change Log**

## v0.6.0-beta (unreleased)

### Balance Adjustments
* Boosted monster base stats.
* Leveling up takes more battles than before.
* Flattened the game difficulty curve.
* The final boss might now attack and/or cast spells more than once per turn, depending on the game difficulty setting.

### Other Notes
* Removed unused sound effects.
* The character file version is now 2. Version 1 characters will be auto-upgraded with no loss of fidelity.

## v0.5.1-beta (2019-12-09)
### Bug Fixes
* Fixed a division by zero bug making battles impossible to complete normally.
* Corrected an accidental omission in v0.5.0 of displaying the damage multiplier on attacks.

### Other Notes
* The auto-updater will find this version if you have at least v0.5.0-beta, and it is enabled.

## v0.5.0-beta (2019-12-08)
### New Features
* Added a Twisted Hallways with Rooms world generator.
* Added customization settings for the new generator.
* Added an update checking mechanism (automatic checks are off by default and must be enabled in Preferences manually).
* Added Closed Door and Open Door objects.

### Bug Fixes
* Fixed all the remaining known bugs.

### Other Notes 
* Binary builds are now possible to create.
* Removed unused code and resources.

## v0.4.0-alpha (2019-11-29)
### New Features
* Added Stairs Down, Stairs Up, Pit. Spring, Super Pit and Super Spring objects to the game.
* The party can now have up to 10 members, as opposed to just 1.
* All creatures (party members, monsters, and bosses) now have a faith, job and race.
* Party members now come with default names.
* A party can now be automatically assembled, if you'd like.
* Battles feature more than just 1 monster now.
* Monsters on different teams will attack each other, in addition to attacking you.
* There's new settings for the world generator.
* World- and Level-specific settings have been added to the editor.

### Bug Fixes
* Loading / saving games is now separate from that for worlds.
* Battles are less broken now.
* The editor doesn't try to draw the player, fail, and crash if it's not set.
* Checks to see if there's enough action points in battle are now all done the same way.

### Other Notes
* New music!
* Renamed Castes to Jobs.
* Renamed Mazes to Worlds.
* Simplified party creation: Personalities and genders only added confusion; they're gone now.
* All file versions and file extensions were reset. Files from older versions are not compatible with this version, and vice versa.
* Cleaned up old text referencing previous games.

## 0.3.1-alpha (2019-11-16)
### Other Notes
* Everything is drawn in one window only.

## 0.3.0-alpha (2019-11-29)
### New Features
* Monsters are now always hidden in the dungeon.

### Bug Fixes
* The editor works... sort of.
* Lots more buggy behavior was made no longer buggy.

## 0.2.0-alpha (2019-10-27)
### New Features
* The game now has music. 
* The game is semi-playable.

### Bug Fixes
* Lots of buggy behavior was made no longer buggy.

## 0.1.0-alpha (2019-10-23)
### New Features
* First release (very broken).

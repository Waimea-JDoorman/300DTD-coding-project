# Test Plan and Evidence / Results of Testing

## Game Description

The project involves the programming of a game.

The game is a single-player, adventure puzzle game where the player is trapped in a house.
The player must explore the house, interacting with the rooms in order find the necessary items to escape.
The player is timed by a gas leakage (100 seconds) and therefore must manage their time wisely while exploring the map.
The player wins if they are able to escape the house before the time runs out and loses if the time runs out before their escape.

### Game Features and Rules

The game has the following features and/or rules:

- The player can move around the map in 4 directions: Up, Down, Left, Right
- The player cannot move through locked obstacles null locations
- The player can interact with the environment to search for items
- The player will have an inventory where they can view their current collected items
- The player can use their items on corresponding obstacles
- If the gas pollution reaches 100, the player loses
- The player will be able to view the gas pollution number throughout the game
- If the player escapes before the gas reaches 100, they win

---

## Test Plan

The following game features / functionality and player actions will need to be tested:

- Player Movement
- Inventory and Items
- Losing
- Winning

The following tests will be run against the completed game. The tests should result in the expected outcomes shown.


### Player Movement

The game should only allow the player to make valid moves. The player should not be able to travel through locked or null locations.

#### Test Data / Actions to Use

Valid moves: Attempt to go through every possible valid location
Invalid moves: Attempt to go through every possible null or locked location, test with different items too (part of inventory and items).
Edge Case Valid Move: Attempt to go into a location that has been unlocked, leave, and attempt to re-enter

#### Expected Outcome

The player should be able to go into every valid location whether it be going back to previously visited locations or unlocked locations.
The player should not be able to attempt entering any null locations or locked locations before unlocking them as the button should be disabled.
The player should be able to go back into a location that has already been unlocked.

### Inventory and Items

The game should allow the player to interact with the environment to pick up items and see them in their inventory at any time.

#### Test Data / Actions to Use

Attempt to interact and take an item in its designated spot. Attempt to interact and take an item that is locked behind prerequisites that are not fulfilled, attempt to grab items in a room without items, attempt to grab an item multiple times, attempt to use items on things unrelated to itself.

#### Expected Outcome

The player should be able to interact and take items from their designated rooms when they fulfill the prerequisite to take it. The player should then see the item in their inventory.
The player should fail when trying to take items without the prerequisites fulfilled, should also fail when attempting to use items on locks or objects they are not made for, should be told there are no items when interacting in empty locations, and the item should be removed from the location when taken the first time.

### Losing

#### Test Data / Actions to Use

Attempt to get gas pollution to 100 during normal gameplay


#### Expected Outcome

The player should be presented with a loss screen after gas pollution hits 100 during normal gameplay, and the inputs should be disabled.


### Winning

#### Test Data / Actions to Use

Attempt to win and wait 100 seconds to test if the gas pollution gets paused or hits 100 and presents a loss.
Edge Case: Attempt to get the gas pollution close to 100 and see if the number goes up after the player has escaped.


#### Expected Outcome

The player should be able to win normally when escaping before gas pollution hits 100, gas pollution should then disappear and not continue rising.
The number should not have a delay and rise after the player has escaped.


---


## Evidence / Results of Testing

### Player Movement

ACTUAL RESULTS OF TESTING SHOWN HERE

https://youtu.be/yK-Y6rcWqCY

All the valid moves were continually tested and worked as intended.
All the invalid moves were continually tested and the player was prevented from making them.
The edge case of unlocking and entering a location, leaving, then re-entering worked as intended and the player was able to re-enter the previously locked location


### Inventory and Items

ACTUAL RESULTS OF TESTING SHOWN HERE

https://youtu.be/6iJXBsu-1hM

Interacting with every room was tested and everything worked as intended.
The rooms without items displayed that there were no items.
The rooms with items that the player met the prerequisites for allowed the player to take the item
After the player took the item, the item was removed from the room and the room then displayed that there were no more items in it.
The player was unable to take items they did not meet the prerequisites for.
The player was unable to unlock or interact with locks and objects they did not have the items for.
The player was able to interact with every lock and object they had the correct item for.


### Losing

ACTUAL RESULTS OF TESTING SHOWN HERE

https://youtu.be/RqsC8mkChVY

Losing worked as intended.
The player was presented with the losing screen when gas pollution hit 100 before they escaped.
The player was unable to continue as they could not submit any inputs as they were all disabled.
The items from the inventory disappeared


### Winning

ACTUAL RESULTS OF TESTING SHOWN HERE

https://youtu.be/5rtVoKAgHF8

The player was presented with the winning screen after they escaped before gas pollution hit 100.
The full 100 seconds for gas pollution to hit 100 was waited for and the losing screen did not overlap the winning screen.
The edge case of gas pollution having still counting up slightly after a successful escape was checked as the number did not rise after escaping.


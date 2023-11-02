# ChoHan
Chō-Han Bakuchi or simply Chō-Han (丁半) is a traditional Japanese gambling choHanGame using dice.

The choHanGame uses two standard six-sided dice, which are shaken in a bamboo cup or bowl by a dealer. The cup is then overturned onto the floor. Players then place their wagers on whether the sum total of numbers showing on the two dice will be "Chō" (even) or "Han" (odd). The dealer then removes the cup, displaying the dice. The winners collect their money.

Depending on the situation, the dealer will sometimes act as the house, collecting all losing bets. More often, the players will bet against each other (this requires an equal number of players betting on odd and even) and the house will collect a set percentage of winning bets.

[Message from author]\
To my follower(s), this project is currently on hold. I just learned how to use Unity, and have not been able to put it down since. Making sprites for a game is time-consuming, but fun! I will be back as soon as I finish creating one game with Unity.\
It could be awhile before I return. Thank you for your patient!

[To self]\
To ensure that the betting pools and the round number do not reset when the player chooses to go back, you'll need to maintain the state of the game and only reset specific parts of it when required.

Here's a step-by-step guide to solve the issue:

- Maintain State: Keep the game's state (round number, betting pools, etc.) stored in a structure (like a class or dictionary) so that you can easily return to it.

- Decision Function: Create a function that takes the player's decision. If they choose to go back (input "0"), you'll want to return to the previous state without updating the round number or the betting pools.

- Update Only When Necessary: The round number and betting pools should only be updated when the player actually places a bet or progresses to the next round. If they simply view their options and then choose to go back, you shouldn't update these values.


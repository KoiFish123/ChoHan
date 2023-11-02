import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

    /*
    ORDER OF THE GAME:
    1. THE DICE ROLLED. ODD OR EVEN IS DETERMINED.
        1.1 THE NPCs/BOTs PLACED THEIR BETS AND CONTRIBUTE TO THE POOL
    2. YOU ARE ASKED ON WHAT TO DO (ODD, EVEN, GUESS A NUMBER, ETC.). YOU CAN INPUT CHEAT CODE
        2.1. YOU CAN SEE BOTH POOL BEFORE MAKING YOUR CHOICE.
    3. YOU PLACE YOUR BET
        3.2. IF EVEN OR ODD, ADD THAT TO THE BET POOL.
    4. RESULT
    5. PLAY AGAIN?
     */

class GameUtilsImpl implements GameUtils {

    static Scanner choicePicked = new Scanner(System.in);

    @Override
    public int rollDice() {
        // DON'T TOUCH. IT IS FINE AS IS.
        int die = (int) (Math.random() * 6) + 1;
        return die;
    }

    @Override
    public String evenOrOdd(int die1, int die2) {
        // DON'T TOUCH. IT IS FINE AS IS.
        if ((die1 + die2) % 2 == 1) {
            return ("ODD");
        }
        return ("EVEN");
    }

    @Override
    public Ranking getRanking(int point) {
        // DON'T TOUCH. IT IS FINE AS IS.
        for (Ranking rank : Ranking.values()) {
            if (point >= rank.lowerBound && point <= rank.upperBound) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid rank: " + point);
    }

    @Override
    public void displayNPCBets(NPC[] npcs, int betPoolOdd, int betPoolEven) {
        List<NPC> oddNPCs = new ArrayList<>();
        List<NPC> evenNPCs = new ArrayList<>();

        // Categorize NPCs based on their bets
        for (NPC npc : npcs) {
            if (npc.getBet()[0] == 1) {
                oddNPCs.add(npc);
            } else {
                evenNPCs.add(npc);
            }
        }

        // Determine the maximum number of NPCs to display side-by-side
        int maxSize = Math.max(oddNPCs.size(), evenNPCs.size());
        System.out.println("\n\t\t\t\t\tCurrent pools");
        System.out.println("ODD: " + betPoolOdd + "\t\t\t\t\t\tEVEN: " + betPoolEven);

        // Print NPCs in a two-column format
        for (int i = 0; i < maxSize; i++) {
            String oddNPCName = (i < oddNPCs.size()) ? oddNPCs.get(i).getName() + " bet " + oddNPCs.get(i).getBet()[1] : "";
            String evenNPCName = (i < evenNPCs.size()) ? evenNPCs.get(i).getName() + " bet " + evenNPCs.get(i).getBet()[1] : "";

            System.out.printf("%-25s\t\t%-25s%n", oddNPCName, evenNPCName);
        }
    }

    @Override
    public void createGameChoices(int rounds) {
        String gameChoices;
        gameChoices = "What will it be?";
        gameChoices += "\n1. Odd";
        gameChoices += "\n2. Even";
        if (rounds > 5) {
            gameChoices += "\n3. Guess the number on one die";
        }
        if (rounds > 10) {
            gameChoices += "\n4. Guess the number on both dice";
        }
        if (rounds > 20) {
            gameChoices += "\n5. Guess the difference between both dice";
        }
        showChoice(gameChoices);
    }

    @Override
    public int oneDieGuessChoices() {
        int choice = 0;
        while (true) {
            showChoice("Pick a number that will appear on one of the dice (between 1-6).");
            showChoice("Or type '0', to go back");
            try {
                choice = choicePicked.nextInt();
                choicePicked.nextLine(); // This will consume the leftover newline "\n"
                if (choice <= 6 && choice >= 0) return choice;
            } catch (java.util.InputMismatchException e) {
                choicePicked.nextLine(); // Clear the invalid input
            }
            showErrorMessage("Please enter a valid integer.");
        }
    }

    @Override
    public Integer bet(Player player) {

        player.adjustBetMax();

        showMessage("\nCurrent points: " + player.getPoints());
        showChoice("Set bet amount (amount cannot be more than " + player.getBetMax() + "):");
        showChoice("Or type input \"0\" to go back");
        Integer ante = 0;
        while (true) {
            try {
                ante = choicePicked.nextInt();
                choicePicked.nextLine(); // This will consume the leftover newline "\n"

                if (ante == 0) {
                    return null;  // return null if user chooses to go back
                }

                if (ante <= player.getBetMax() && ante <= player.getPoints() && ante > 0) {
                    player.subtractPoints(ante);
                    break;
                }
                if (ante > player.getBetMax())
                    // Put down more than betting maximum(betMax) allowed
                    showErrorMessage("Amount cannot be more than " + player.getBetMax() + " Try again.\n");

                if (ante < 0)
                    // less than 0 bet
                    showErrorMessage("Amount cannot be less than 0. Try again.\n");

                if (ante > player.getPoints())
                    // Betting more points than you have
                    showErrorMessage("Cannot ante more points than you have. Try again.\n");

            } catch (java.util.InputMismatchException e) {
                showErrorMessage("Please enter a valid integer for your bet.\n");
                choicePicked.nextLine(); // Clear the invalid input
            }
        }
        return ante;
    }

    @Override
    public void displayDiceRoll(int die1, int die2, String evenOrOdd) {
        try {
            System.out.print("The result is");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.println("\n" + die1 + " " + die2);
            Thread.sleep(1000);

            if (die1 == 1 && die2 == 1)
                System.out.print("SNAKE EYES! ");

            System.out.println(evenOrOdd + "!\n");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void youWon(Player player, int ante, int multiplier) {
        // DON'T TOUCH. IT IS FINE AS IS.
        System.out.println("Nicely done!");

        System.out.println("Current Points: " + player.getPoints());

        int winning = ante * multiplier;

        player.addPoints(winning);

        player.incrementWins();

        player.incrementWinStreak();

        if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());

        if (player.getRounds() >= 15) {

            showMessage("Current Winning: " + winning);

            winning = betAgainstTheHouse(player, winning);

        }

        showMessage("You gain " + winning + " points.");


        showMessage("\nCurrent points: " + player.getPoints());
    }

    @Override
    public void youWonEvenOdd(Player player, int betPool, int winningPlayers) {
        showMessage("Nicely done.");

        showMessage("Current Points: " + player.getPoints());

        int winning = betPool / winningPlayers;

        player.incrementWins();

        player.incrementWinStreak();

        if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());


        /*
        INTERMEDIATE LEVEL: Guess a number on one of the dice. Available after 5 wins
         */
        if (player.getRounds() >= 15) {

            showMessage("Current Winning: " + winning);

            winning = betAgainstTheHouse(player, winning);

        }

        System.out.println("You gain " + winning + " points.");

        player.addPoints(winning);

        System.out.println("\nCurrent points: " + player.getPoints());
    }


    public int betAgainstTheHouse(Player player, int winning) {
        int currentWinning = winning;
        int roundsPlayed = 0;

        System.out.println("Would you like to play against the house? [Y/N]");
        while (roundsPlayed < 5) {
            String response = getUserInput();

            if (response.equals("n") || response.equals("no")) {
                break; // Exit the loop
            } else if (response.equals("y") || response.equals("yes")) {
                int die1 = rollDice();
                int die2 = rollDice();
                String evenOrOdd = evenOrOdd(die1, die2);

                System.out.println("[Cue the intense music]");
                System.out.println("Playing against the house...");
                createGameChoices(0);
                response = getUserInput();

                while (!response.equals("1") && !response.equals("2")) {
                    System.out.println("Invalid answer. Try again.");
                    response = getUserInput();
                }

                String guess = response.equals("1") ? "odd" : "even";
                displayDiceRoll(die1, die2, evenOrOdd);

                if (guess.equals(evenOrOdd.toLowerCase())) {
                    // CORRECT
                    currentWinning *= 2;
                    System.out.println("Nicely done! Your winnings have doubled to: " + currentWinning);

                    player.incrementWins();

                    player.incrementWinStreak();

                    if (player.getWinStreak() > player.getHighestWinStreak())
                        player.setHighestWinStreak(player.getWinStreak());

                } else {
                    // WRONG
                    currentWinning = 0;

                    player.setWinStreak(0);

                    player.incrementLoses();

                    System.out.println("Aww... Better luck next time.");
                    break; // Exit the loop
                }
            } else {
                System.out.println("Invalid answer. Try again.");
            }

            checkAndDisplayAnnouncement(player);

            roundsPlayed++;
            if (roundsPlayed < 5)
                System.out.println("Want to keep going, just you and me? [Y/N]");

            if (roundsPlayed == 5)
                System.out.println("Ugh, enough! You're so persistent");
        }
        // Return currentWinning for youWon() or youWonEvenOdd() to handle
        return currentWinning;
    }

    @Override
    public void youLost(Player player) {
        // DON'T TOUCH. IT IS FINE AS IS.
        System.out.println("Incorrect. It is what it is.");

        // Gain nothing if you lose

        // Lose your Win Streak
        player.setWinStreak(0);

        player.incrementLoses();

        System.out.println("\nCurrent points: " + player.getPoints());
    }

    @Override
    public boolean playAgain(Player player) {
        while (true) {
            System.out.println("Would you like to keep playing? [Y/N]");
            String again = getUserInput();
            if (again.equals("y") || again.equals("yes")) {

                System.out.println();
                System.out.println("Game Started\n");
                return true;
            }
            if (again.equals("n") || again.equals("no")) {
                System.out.println("Too bad. Come again.");

                // Player's info
                System.out.println("\nFinal points: " + player.getPoints());
                System.out.println("Highest Win Streak: " + player.getHighestWinStreak());
                System.out.println("Title: " + getRanking(player.getPoints()));

                return false;
            } else System.out.println("Invalid answer. Try again.");
        }
    }

    @Override
    public String getUserInput() {
        return choicePicked.nextLine().toLowerCase();
    }

    @Override
    public void shutdown() {
        System.exit(0);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showChoice(String choice) {
        System.out.println(choice);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }

    public void displayAnnouncement(String announcement) {
        System.out.println("\n+-----------------------ANNOUNCEMENT-----------------------+\n");
        System.out.println(announcement);
        System.out.println("\n+----------------------------------------------------------+\n");
    }

    @Override
    public void checkAndDisplayAnnouncement(Player player) {
        // IF THE PLAYER WIN STREAK IS 5, 10, 15, ETC. TELL THEM
        if (player.getWinStreak() % 5 == 0 && player.getWinStreak() != 0)
            displayAnnouncement("Nice going! Your win streak is currently at " + player.getWinStreak());

        // IF YOU ARE OUT OF POINTS, THE GAME KICK YOU OUT

        if (player.getPoints() == 0) {
            displayAnnouncement("You ran out of points. Better luck next time.");
            shutdown();
        }

        if (player.getRounds() == 5 || player.getRounds() == 10 || player.getRounds() == 15 || player.getRounds() == 20) {
            String newModeAnnouncement = "                New mode has been unlocked!                \n";
            switch (player.getRounds()) {
                case 5:
                    // INTERMEDIATE LEVEL EXPLANATION
                    newModeAnnouncement +=
                            "You unlock the 'Bet on one die' option, where you can " +
                                    "\npredict a number you think one of the two dice will have. " +
                                    "\nYou will get a good amount of tags if you win this."
                    ;
                    break;
                case 10:
                    newModeAnnouncement +=
                            "You unlock the 'Bet on two dice' option, where you " +
                                    "\nhave to predict both numbers of the dice. You have to " +
                                    "\nante at least 20% of your current points, but get " +
                                    "\nthe whole pot if you win this."
                    ;
                    break;
                case 15:
                    newModeAnnouncement +=
                            "\nYou can choose to play against the house " +
                                    "\nwhen winning a game (or refuse). The dealer will roll the dice again, and " +
                                    "\nyou gotta predict if it's even or odd. If you are correct, it'll double your " +
                                    "\nwinnings and you can choose to play against the house again (up to 5 times) " +
                                    "\nor cash out."
                    ;
                    break;
                case 20:
                    newModeAnnouncement+=
                            "\nNew mode has been unlocked. You unlock the 'Bet on the difference' option, " +
                                    "\nwhere you predict the difference between the two dice. The payout depends " +
                                    "\non your chosen number (the higher the difference the higher the payout) " +
                                    "\nand can go up to absurd heights."
                    ;
                    break;
                default: break;
            }
            displayAnnouncement(newModeAnnouncement);
        }

        if (player.getWins() == 10 || player.getWins() == 20 || player.getWins() == 30) {
            switch (player.getWins()) {
                case 10:
                    displayAnnouncement("You can now bet up to 500 points");
                    break;
                case 20:
                    displayAnnouncement("You can now bet up to 800 points");

                    break;
                case 30:
                    displayAnnouncement("You can now bet up to 1000 points");
                    break;
                default: break;
            }
        }
    }
}

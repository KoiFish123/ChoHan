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
    public void createGameChoices(int wins) {
        String gameChoices;
        gameChoices = "What will it be?";
        gameChoices += "\n1. Odd";
        gameChoices += "\n2. Even";
        if (wins >= 5) {
            gameChoices += "\n3. Guess the number on one die";
        }
        if (wins >= 10) {
            gameChoices += "\n4. Guess the number on both dice";
        }
        if (wins >= 20) {
            gameChoices += "\n5. Guess the difference between both dice";
        }
        System.out.println(gameChoices);
    }

    @Override
    public int oneDieGuessChoices() {
        int choice = 0;
        while (true) {
            System.out.println("Pick a number that will appear on one of the dice (between 1-6).");
            System.out.println("Or type '0', to go back");
            try {
                choice = choicePicked.nextInt();
                choicePicked.nextLine(); // This will consume the leftover newline "\n"
                if (choice <= 6 && choice >= 0) return choice;
            } catch (java.util.InputMismatchException e) {
                choicePicked.nextLine(); // Clear the invalid input
            }
            System.out.print("Please enter a valid integer. ");
        }
    }

    @Override
    public Integer bet(Player player) {
        System.out.println("\nCurrent points: " + player.getPoints());
        System.out.println("Set bet amount (amount cannot be more than " + player.getBetMax() + "):");
        Integer ante = 0;
        while (true) {
            try {
                ante = choicePicked.nextInt();
                choicePicked.nextLine(); // This will consume the leftover newline "\n"

                // After certain amount of rounds, your betMax will increase
                // For every 10 rounds, increase betMax by 100, to max of 500
                if (player.getBetMax() < 500) {
                    int increaseAmount = (player.getRounds() / 10) * 100;
                    player.setBetMax(Math.min(player.getBetMax() + increaseAmount, 500));
                }

                if (ante <= player.getBetMax() && ante <= player.getPoints() && ante > 0) {
                    player.subtractPoints(ante);
                    break;
                }
                if (ante > player.getBetMax())
                    // Put down more than betting maximum(betMax) allowed
                    System.out.print("Amount cannot be more than " + player.getBetMax() + " Try again.\n");

                if (ante <= 0)
                    // less than or equal to 0 bet
                    System.out.print("Amount cannot be less than or equal to 0. Try again.\n");

                if (ante > player.getPoints())
                    // Betting more points than you have
                    System.out.println("Cannot ante more points than you have. Try again.\n");

            } catch (java.util.InputMismatchException e) {
                System.out.print("Please enter a valid integer for your bet.");
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

            System.out.println(evenOrOdd);
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void youWon(Player player, int ante, int multiplier) {
        // DON'T TOUCH. IT IS FINE AS IS.
        System.out.println("Correct! Not bad.");

        System.out.println("Current Points: " + player.getPoints());

        int winning = ante * multiplier;

        player.addPoints(winning);

        player.incrementWins();

        player.incrementWinStreak();

        if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());

        // IF THE PLAYER WIN STREAK IS 5, 10, 15, ETC. TELL THEM
        if (player.getWinStreak() % 5 == 0 && player.getWinStreak() != 0)
            System.out.println("Nice going! Your win streak is currently at " + player.getWinStreak());

        System.out.println("Current Winning: " + winning);

        winning = betAgainstTheHouse(player, winning);

        System.out.println("You gain " + winning + " points.");


        System.out.println("\nCurrent points: " + player.getPoints());
    }

    @Override
    public void youWonEvenOdd(Player player, int betPool, int winningPlayers) {
        System.out.println("Correct! Not bad.");

        System.out.println("Current Points: " + player.getPoints());

        int winning = betPool / winningPlayers;

        player.incrementWins();

        player.incrementWinStreak();

        if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());

        // IF THE PLAYER WIN STREAK IS 5, 10, 15, ETC. TELL THEM
        if (player.getWinStreak() % 5 == 0 && player.getWinStreak() != 0)
            System.out.println("Nice going! Your win streak is currently at " + player.getWinStreak());

        System.out.println("Current Winning: " + winning);

        winning = betAgainstTheHouse(player, winning);

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
                    System.out.println("Correct! Your winnings have doubled to: " + currentWinning);

                    player.incrementWins();

                    player.incrementWinStreak();

                    if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());

                    // IF THE PLAYER WIN STREAK IS 5, 10, 15, ETC. TELL THEM
                    if (player.getWinStreak() % 5 == 0 && player.getWinStreak() != 0)
                        System.out.println("Nice going! Your win streak is currently at " + player.getWinStreak());

                } else {
                    // WRONG
                    currentWinning = 0;

                    player.setWinStreak(0);

                    player.incrementLoses();

                    System.out.println("Incorrect. You lost everything.");
                    break; // Exit the loop
                }
            } else {
                System.out.println("Invalid answer. Try again.");
            }

            roundsPlayed++;
            if (roundsPlayed < 5)
                System.out.println("Would you like to keep playing against the house? [Y/N]");

            if (roundsPlayed == 5)
                System.out.println("That's 5 rounds done! You are quite the gambler, huh?\n Unfortunately, this is as far as you can go.");
        }
        // Return currentWinning for youWon() or youWonEvenOdd() to handle
        return currentWinning;
    }

    @Override
    public void youLost(Player player) {
        // DON'T TOUCH. IT IS FINE AS IS.
        System.out.println("Incorrect. it is what it is.");

        // Gain nothing if you lose

        // Lose your Win Streak
        player.setWinStreak(0);

        player.incrementLoses();

        System.out.println("\nCurrent points: " + player.getPoints());
    }

    @Override
    public void isOutOfPoints(Player player) {
        if (player.getPoints() == 0) {
            System.out.print("Uh oh! You ran out of points");
            try {
                Thread.sleep(500);
                System.out.print(".");
                Thread.sleep(600);
                System.out.print(".");
                Thread.sleep(700);
                System.out.println(".");
                Thread.sleep(800);
                System.out.print("Avoid dark alleys.");
                shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean playAgain(Player player) {
        while (true) {
            System.out.println("Again? [Y/N]");
            String again = getUserInput();
            if (again.equals("y") || again.equals("yes")) {

                System.out.println();
                System.out.println("Game Started\n");
                return true;
            }
            if (again.equals("n") || again.equals("no")) {
                System.out.println("Too bad. Come again.");

                // Player's info
                System.out.println("Final points: " + player.getPoints());
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
}

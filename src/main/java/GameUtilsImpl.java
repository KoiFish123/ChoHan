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

    // Roll your dice
    @Override
    public int rollDice() {
        // DON'T TOUCH. IT IS FINE AS IS.
        int die = (int) (Math.random() * 6) + 1;
        return die;
    }

    // Is it even or odd?
    @Override
    public String EvenOrOdd(int die1, int die2) {
        // DON'T TOUCH. IT IS FINE AS IS.
        if ((die1 + die2) % 2 == 1) {
            return ("ODD");
        }
        return ("EVEN");
    }

    // Return ranking
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

        System.out.println("You gain " + (ante * multiplier) + " points.");

        player.addPoints(ante * multiplier);

        player.incrementWins();

        player.incrementWinStreak();

        if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());

        // IF THE PLAYER WIN STREAK IS 5, 10, 15, ETC. TELL THEM
        if (player.getWinStreak() % 5 == 0 && player.getWinStreak() != 0)
            System.out.println("Nice going! Your win streak is currently at " + player.getWinStreak());

        System.out.println("\nCurrent points: " + player.getPoints());
    }

    @Override
    public void youWonEvenOdd(Player player, int betPool, int winningPlayers) {
        System.out.println("Correct! Not bad.");

        System.out.println("You gain " + (betPool / winningPlayers) + " points.");

        player.addPoints(betPool / winningPlayers);

        player.incrementWins();

        player.incrementWinStreak();

        if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());

        // IF THE PLAYER WIN STREAK IS 5, 10, 15, ETC. TELL THEM
        if (player.getWinStreak() % 5 == 0 && player.getWinStreak() != 0)
            System.out.println("Nice going! Your win streak is currently at " + player.getWinStreak());

        System.out.println("\nCurrent points: " + player.getPoints());
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
}

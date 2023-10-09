import java.util.Scanner;

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
}

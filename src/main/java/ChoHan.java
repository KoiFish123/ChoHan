import java.lang.Math;
import java.util.Scanner;

public class ChoHan {
    static int point = 100;
    static int betMax = 200;
    static int round = 0;
    static int win = 0;
    static int winStreak = 0;
    static int highestWinstreak = 0;
    static int loses = 0;               // I haven't decided what to do with this yet. Maybe for the achievements?
    static int even = 0;
    static int odd = 0;

    static Scanner choicePicked = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Type 1 or Start to start the game, or 2 or Quit to quit");

        while (true) {
            String start = choicePicked.nextLine().toLowerCase();

            if (start.equals("start") || start.equals("1")) {
                System.out.println();
                System.out.println("Game Started\n");
                startGame();
                break;
            }
            if (start.equals("quit") || start.equals("2")) {
                System.out.println("Ending the game");
                System.exit(0);
            } else {
                System.out.println("Failed to start. Try again.");
            }
        }
    }

    public static void startGame() {
        round += 1;
        System.out.println("Round " + round);
        System.out.println("Current points: " + point);

        int die1 = rollDice();
        int die2 = rollDice();
        String evenOrOdd = EvenOrOdd(die1, die2);

        System.out.println(createGameChoices());

        while (true) {
            String answers = choicePicked.nextLine().toLowerCase();
            Integer ante;

            // 1. BASIC LEVEL: ODD OR EVEN
            if (answers.equals("1")) {
                answers = "odd";
            }

            if (answers.equals("2")) {
                answers = "even";
            }

            /*
            TODO:
             - INTERMEDIATE LEVEL: Guess a number on one of the dice
             - Available after 5 wins
             - Chose between 1 and 6.
             - The reward is difference depends on whether your choice is odd or even. (After implementing NPC)
             */

            /*
            TODO:
             - ADVANCE LEVEL: Guess numbers on both of the dice
             - Available after 10 wins
             - The player must put down 20% of their current points
             */

            /*
            TODO:
             - GOD-TIER: Bet against the house.
             - Available after 15 wins.
             - After winning a round, you will be asked if you want to play the house.
             - If you answered yes, you will take the point you earned in you previous win
               as ante to play against the house.
             - If you win against the house, you will be asked if you want to play against
               the house again,taking the point you just won as ante.
             - This will last at most 5 rounds. If you loses even once, you lose all.
             */

            /*
            TODO:
             - ASCENDED: Guess the difference between two dice.
             - Available after 20 wins.
             - Choice from 0 to 5.
             - The reward is difference depends on your choice.
             */


            // Cheat Code here:
            if (answers.equals("123231")) {
                System.out.println("Cheat code activated: +1000 points");
                point += 1000;
                break;
            }

            // Set betting amount AFTER making your choices
            while (true) {
                System.out.println("Current points: " + point);
                System.out.println("Set bet amount (amount cannot be more than " + betMax + "):");
                ante = choicePicked.nextInt();
                choicePicked.nextLine(); // This will consume the leftover newline "\n"

                if (ante <= betMax && ante <= point) {
                    point -= ante;
                    break;
                }
                if (ante > betMax)
                    // Put down more than betting maximum(betMax) allowed
                    System.out.println("Amount cannot be more than " + betMax + " Try again.\n");

                if (ante > point)
                    // Betting more points than you have
                    System.out.println("Cannot ante more points than you have. Try again.\n");
            }

            // 1. BASIC LEVEL: ODD OR EVEN
            if (answers.equals("odd") || answers.equals("even")) {
                System.out.println(die1 + " " + die2);

                if (answers.equals(evenOrOdd.toLowerCase())) {
                    System.out.println("Correct! Not bad.");
                    /*
                    TODO: Find alternative for way to gain point
                        - In the future, there will be NPC that contribute to the betting pool
                          which will affect the points the player get back.
                        - But for now, tripling the ante will suffice.
                     */
                    youWon(ante, 3);

                } else {
                    youLose();
                }

                break;
            } else System.out.println("Invalid answer. Try again.");
        }

        if (point == 0) {
            System.out.print("Uh oh! You ran out of points");
            try {
                Thread.sleep(100);
                System.out.print(".");
                Thread.sleep(100);
                System.out.print(".");
                Thread.sleep(100);
                System.out.println(".");
                Thread.sleep(100);
                System.out.print("Avoid dark alleys.");
                Thread.sleep(100);
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (winStreak % 5 == 0) System.out.println("Nice going! Your win streak is currently at " + winStreak);
        while (true) {
            System.out.println("Again? [Y/N]");
            String again = choicePicked.nextLine().toLowerCase();
            if (again.equals("y") || again.equals("yes")) {
                System.out.println();
                System.out.println("Game Started\n");
                startGame();
                break;
            }
            if (again.equals("n") || again.equals("no")) {
                System.out.println("Too bad. Come again.");
                System.out.println("Final points: " + point);
                System.out.println("Highest Win Streak: " + highestWinstreak);
                System.out.println("Title: " + getRanking(point));
                System.exit(0);
            } else System.out.println("Invalid answer. Try again.");
        }
    }

    // Handle point after you won
    public static void youWon(int ante, int multiplier) {
        System.out.println("You gain " + (ante * multiplier) + " points.");

        point += (ante * multiplier);

        win++;
        winStreak++;

        if (winStreak > highestWinstreak) highestWinstreak = winStreak;

        System.out.println("Current points: " + point);
    }

    public static void youLose() {
        System.out.println("Incorrect. it is what it is.");

        // Gain nothing if you lose

        // Lose your Win Streak
        winStreak = 0;

        System.out.println("Current points: " + point);
    }

    public static int rollDice() {
        // DON'T TOUCH
        int die = (int) (Math.random() * 6) + 1;
        return die;
    }

    public static String EvenOrOdd(int die1, int die2) {
        // DON'T TOUCH
        if ((die1 + die2) % 2 == 1) {
            odd++;
            return ("ODD");
        }
        even++;
        return ("EVEN");
    }

    public static Ranking getRanking(int point) {
        for (Ranking rank : Ranking.values()) {
            if (point >= rank.lowerBound && point <= rank.upperBound) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid rank: " + point);
    }

    public static String createGameChoices() {
        String gameChoices;
        gameChoices = "What will it be?\n";
        gameChoices += "1. Odd\n";
        gameChoices += "2. Even";
        /*
        if (win >= 5) {
            gameChoices += "3. Guess a number on one of the dice\n";
        }
         */

        return gameChoices;
    }
}
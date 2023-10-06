import java.util.Scanner;


public class ChoHan {
    static final int INITIAL_POINTS = 100;
    static final int MAX_BET = 200;
    static int even = 0;        // For later
    static int odd = 0;         // For later

    static Scanner choicePicked = new Scanner(System.in);
    private static GameUtils gameUtils = new GameUtilsImpl();

    static Player player = new Player();


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

    /*
    ORDER OF THE GAME:
    1. THE DICE ROLLED. ODD OR EVEN IS DETERMINED.
    2. YOU ARE ASKED ON WHAT TO DO (ODD, EVEN, GUESS A NUMBER, ETC.). YOU CAN INPUT CHEAT CODE
    3. YOU PLACE YOUR BET
    4. RESULT
    5. PLAY AGAIN?
     */

    public static void startGame() {
        Integer ante = 0;
        String answers;

        player.incrementRound();
        System.out.println("Round " + player.getRounds());
        System.out.println("Current points: " + player.getPoints());

        // 1. THE DICE ROLLED
        int die1 = gameUtils.rollDice();
        int die2 = gameUtils.rollDice();
        String evenOrOdd = gameUtils.EvenOrOdd(die1, die2);

        // 2. MAKE CHOICE
        while (true) {
            System.out.println(createGameChoices());

            // BASIC LEVEL: ODD OR EVEN
            if (getUserInput().equals("1")) {
                answers = "odd";
                break;
            }

            if (getUserInput().equals("2")) {
                answers = "even";
                break;
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


            // Cheat Code here (mainly uses for testing):
            if (getUserInput().equals("123231")) {
                System.out.println("Cheat code activated: +1000 points");
                System.out.println("F I L T H Y");
                player.addPoints(1000);
            }

            if (getUserInput().equals("i want to win")) {
                answers = null;
                System.out.println("Cheat code activated: You win!");
                System.out.println("I'm not giving you anything though.");
                youWon(0, 0);
                break;
            }

            if (getUserInput().equals("i want to lose")) {
                answers = null;
                System.out.println("Cheat code activated: You lose?");
                System.out.println("Unorthodox display of hubris but very well.");
                youLose();
                break;
            } else System.out.println("Invalid answer. Try again.");
        }

        // 3. BET
        // Set betting amount AFTER making your choices
        while (true) {
            if (answers == null) break;
            System.out.println("Current points: " + player.getPoints());
            System.out.println("Set bet amount (amount cannot be more than " + player.getBetMax() + "):");
            ante = choicePicked.nextInt();
            choicePicked.nextLine(); // This will consume the leftover newline "\n"

            // After certain amount of rounds, your betMax will increase
            // For every 10 rounds, increase betMax by 100, to max of 500
            if (player.getBetMax() < 500) {
                int increaseAmount = (player.getRounds() / 10) * 100;
                player.setBetMax(Math.min(player.getBetMax() + increaseAmount, 500));
            }

            if (ante <= player.getBetMax() && ante <= player.getPoints()) {
                player.subtractPoints(ante);
                break;
            }
            if (ante > player.getBetMax())
                // Put down more than betting maximum(betMax) allowed
                System.out.println("Amount cannot be more than " + player.getBetMax() + " Try again.\n");

            if (ante > player.getPoints())
                // Betting more points than you have
                System.out.println("Cannot ante more points than you have. Try again.\n");

            else System.out.println("Invalid answer. Try again.");
        }

        // 4. RESULT
        // ODD OR EVEN RESULT
        if (answers != null) {
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
            }
        }

        // IF YOU ARE OUT OF POINTS, THE GAME KICK YOU OUT
        if (player.getPoints() == 0) {
            System.out.print("Uh oh! You ran out of points");
            try {
                Thread.sleep(200);
                System.out.print(".");
                Thread.sleep(200);
                System.out.print(".");
                Thread.sleep(200);
                System.out.println(".");
                Thread.sleep(200);
                System.out.print("Avoid dark alleys.");
                Thread.sleep(200);
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // IF THE PLAYER WIN STREAK IS 5, 10, 15, ETC. TELL THEM
        if (player.getWinStreak() % 5 == 0 && player.getWinStreak() != 0)
            System.out.println("Nice going! Your win streak is currently at " + player.getWinStreak());

        // PLAY AGAIN?
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
                System.out.println("Final points: " + player.getPoints());
                System.out.println("Highest Win Streak: " + player.getHighestWinStreak());
                System.out.println("Title: " + gameUtils.getRanking(player.getPoints()));
                System.exit(0);
            } else System.out.println("Invalid answer. Try again.");
        }
    }

    // Handle point after you won
    public static void youWon(int ante, int multiplier) {
        // DON'T TOUCH. IT IS FINE AS IS.
        System.out.println("You gain " + (ante * multiplier) + " points.");

        player.addPoints(ante * multiplier);

        player.incrementWins();

        player.incrementWinStreak();

        if (player.getWinStreak() > player.getHighestWinStreak()) player.setHighestWinStreak(player.getWinStreak());

        System.out.println("Current points: " + player.getPoints());
    }

    public static void youLose() {
        // DON'T TOUCH. IT IS FINE AS IS.
        System.out.println("Incorrect. it is what it is.");

        // Gain nothing if you lose

        // Lose your Win Streak
        player.setWinStreak(0);

        player.incrementLoses();

        System.out.println("Current points: " + player.getPoints());
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

    public static String getUserInput() {
        return choicePicked.nextLine().toLowerCase();
    }
}
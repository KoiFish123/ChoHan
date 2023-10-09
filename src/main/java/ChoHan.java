import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public class ChoHan {
    static final int INITIAL_POINTS = 100;
    static final int MAX_BET = 200;
    static int even = 0;        // For later
    static int odd = 0;         // For later
    static int betPoolEven;
    static int betPoolOdd;
    static Scanner choicePicked = new Scanner(System.in);
    private static GameUtils gameUtils = new GameUtilsImpl();
    static Player player = new Player();
    static Map<String, Consumer<Player>> cheatCodes = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("Type 1 or Start to start the game, or 2 or Quit to quit");

        while (true) {
            String start = choicePicked.nextLine().toLowerCase();

            if (start.equals("start") || start.equals("1")) {
                System.out.println();
                System.out.println("Game Started\n");
                startGame();
            } else if (start.equals("quit") || start.equals("2")) {
                System.out.println("Ending the game");
                System.exit(0);
            }
        }
    }

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

    public static void startGame() {
        Integer ante = 0;
        String answers = null;
        boolean continuePlaying = true;
        AtomicBoolean cheatUsedToSkip = new AtomicBoolean(false);


        // Cheat Code here (mainly uses for testing):

        cheatCodes.put("123231", player -> {
            System.out.println("Cheat code activated: +1000 points.");
            System.out.println("F I L T H Y");
            player.addPoints(1000);
        });

        cheatCodes.put("i want to win", player -> {
            System.out.println("Cheat code activated: You win!");
            System.out.println("I'm not giving you anything though.");
            gameUtils.youWon(player, 0, 0);
            cheatUsedToSkip.set(true);
        });

        cheatCodes.put("i want to lose", player -> {
            System.out.println("Cheat code activated: You lost?");
            System.out.println("Unorthodox display of hubris but very well.");
            gameUtils.youLost(player);
            cheatUsedToSkip.set(true);
        });

        cheatCodes.put("donate to charity", player -> {
            System.out.println("Cheat code activated: removing all points.");
            System.out.print("The orphanage thank you for the " + player.getPoints() + " points. Bless your soul(s).");
            try {
                Thread.sleep(1000);
                System.out.print("\n.");
                Thread.sleep(1000);
                System.out.print(".");
                Thread.sleep(1000);
                System.out.print(".");
                Thread.sleep(1000);
                System.out.print("\nWait. What ARE they going to do with it?");
                Thread.sleep(1000);
                System.out.println();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            player.setPoints(0);
            cheatUsedToSkip.set(true);
        });

        while (continuePlaying) {
            cheatUsedToSkip.set(false);
            player.incrementRound();
            System.out.println("Round " + player.getRounds());
            System.out.println("Current points: " + player.getPoints());

            // 1. THE DICE ROLLED
            int die1 = gameUtils.rollDice();
            int die2 = gameUtils.rollDice();
            String evenOrOdd = gameUtils.EvenOrOdd(die1, die2);

            // TODO:
            //  1.1 THE NPCs/BOTs PLACED THEIR BETS AND CONTRIBUTE TO THE POOL

            // 2. MAKE CHOICE
            String userInput;

            while (true) {

                System.out.println("\nCurrent pools:");
                System.out.println("EVEN: " + betPoolEven);
                System.out.println("ODD: " + betPoolOdd);

                gameUtils.createGameChoices(player.getWins());

                userInput = getUserInput();

                if (cheatCodes.containsKey(userInput)) {
                    cheatCodes.get(userInput).accept(player);
                    if (cheatUsedToSkip.get()) {
                        break; // Exit the current game round loop to proceed to "Again? [Y/N]"
                    }
                    userInput = null;
                    continue; // If a cheat code was activated, move on to the next loop iteration.
                }

                // BASIC LEVEL: ODD OR EVEN
                if (userInput.equals("1")) {
                    answers = "odd";
                    break;
                }

                if (userInput.equals("2")) {
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
                if (userInput.equals("3") && (player.getWins() >= 5)) {
                    answers = null;
                    System.out.println("This mode has not been developed. Sorry for the inconvenience.");
                    break;
                }

            /*
            TODO:
             - ADVANCE LEVEL: Guess numbers on both of the dice
             - Available after 10 wins
             - The player must put down 20% of their current points
             */

                if (userInput.equals("4") && (player.getWins() >= 10)) {
                    answers = null;
                    System.out.println("This mode has not been developed. Sorry for the inconvenience.");
                    break;
                }

            /*
            TODO:
             - GOD-TIER: Guess the difference between two dice.
             - Available after 20 wins.
             - Choice from 0 to 5.
             - The reward is difference depends on your choice.
             */
                if (userInput.equals("5") && (player.getWins() >= 20)) {
                    answers = null;
                    System.out.println("This mode has not been developed. Sorry for the inconvenience.");
                    break;
                }
            /*
            TODO:
             - ASCENDED: Bet against the house.
             - Available after 15 wins.
             - After winning a round, you will be asked if you want to play the house.
             - If you answered yes, you will take the point you earned in you previous win
               as ante to play against the house.
             - If you win against the house, you will be asked if you want to play against
               the house again,taking the point you just won as ante.
             - This will last at most 5 rounds. If you loses even once, you lose all.
             - May need a separated function/method.
             */

                else System.out.println("Invalid answer. Try again.");
            }

            // 3. BET
            // Set betting amount AFTER making your choices
            if (answers != null) ante = gameUtils.bet(player);

            // 4. RESULT
            // ODD OR EVEN RESULT
            if (answers != null) {
                if (answers.equals("odd") || answers.equals("even")) {
                    if (answers.equals("odd")) betPoolOdd += ante;
                    if (answers.equals("even")) betPoolEven += ante;

                    gameUtils.displayDiceRoll(die1, die2, evenOrOdd);

                    if (answers.equals(evenOrOdd.toLowerCase())) {
                    /*
                    TODO: Find alternative for way to gain point
                        - In the future, there will be NPC that contribute to the betting pool
                          which will affect the points the player get back.
                        - But for now, tripling the ante will suffice.
                     */

                        gameUtils.youWon(player, ante, 3);        // For testing without bot
                        // youWonEvenOdd(betPoolEven+betPoolOdd, 1);

                    } else {
                        gameUtils.youLost(player);
                    }
                }
            }

            // IF YOU ARE OUT OF POINTS, THE GAME KICK YOU OUT
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
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // PLAY AGAIN?
            while (true) {
                System.out.println("Again? [Y/N]");
                String again = choicePicked.nextLine().toLowerCase();
                if (again.equals("y") || again.equals("yes")) {
                    // Reset the betting pool
                    betPoolEven = 0;
                    betPoolOdd = 0;

                    System.out.println();
                    System.out.println("Game Started\n");
                    break;  // Breaks out of the "Again? [Y/N]" loop and continues with the main game loop
                }
                if (again.equals("n") || again.equals("no")) {
                    System.out.println("Too bad. Come again.");

                    // Player's info
                    System.out.println("Final points: " + player.getPoints());
                    System.out.println("Highest Win Streak: " + player.getHighestWinStreak());
                    System.out.println("Title: " + gameUtils.getRanking(player.getPoints()));

                    continuePlaying = false;  // Ends the main game loop
                    break;  // Breaks out of the "Again? [Y/N]" loop
                } else System.out.println("Invalid answer. Try again.");
            }
        }
        System.exit(0);
    }


    public static String getUserInput() {
        return choicePicked.nextLine().toLowerCase();
    }
}
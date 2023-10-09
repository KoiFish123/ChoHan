public interface GameUtils {
    int rollDice();

    String EvenOrOdd(int die1, int die2);

    Ranking getRanking(int point);

    void createGameChoices(int wins);

    Integer bet(Player player);

    void displayDiceRoll(int die1, int die2, String evenOrOdd);


    void youWon(Player player, int ante, int multiplier);

    void youWonEvenOdd(Player player, int betPool, int winner);

    void youLost(Player player);

    void isOutOfPoints(Player player);

    boolean playAgain(Player player);

    String getUserInput();

    void shutdown();
}

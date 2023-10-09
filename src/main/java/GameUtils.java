public interface GameUtils {
    int rollDice();

    String EvenOrOdd(int die1, int die2);

    Ranking getRanking(int point);

    void createGameChoices(int wins);

    Integer bet(Player player);
}

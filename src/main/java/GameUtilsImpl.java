class GameUtilsImpl implements GameUtils {

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
}

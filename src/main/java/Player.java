class Player {
    private int points;
    private int betMax = 0;
    private int rounds;
    private int win;
    private int winStreak;
    private int highestWinStreak;
    private int loses;               // I haven't decided what to do with this yet. Maybe for the achievements?

    public Player() {
        this.points = Game.INITIAL_POINTS + 10000;  // Set high for testing. Todo: Change back
        adjustBetMax();
        this.rounds = 9;                            // Set high for testing. Todo: Change back
        this.win = 100;                             // Set high for testing. Todo: Change back
        this.winStreak = 0;
        this.highestWinStreak = 0;
        this.loses = 0;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public void addPoints(int points) {
        this.points += points;
    }
    public void subtractPoints(int points) {
        this.points -= points;
    }

    public int getBetMax() {
        return betMax;
    }

    public void adjustBetMax(){
        // After certain amount of rounds, your betMax will increase
        // For every 10 rounds, increase betMax by 100, to max of 500
        if (getBetMax() < 500) {
            setBetMax(Math.min((getRounds() / 10) * 100 +100, 500));
        }
    }
    public void setBetMax(int betMax) {
        this.betMax = betMax;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void incrementRound() {
        this.rounds += 1;
    }

    public int getWins() {
        return win;
    }

    public void setWins(int win) {
        this.win = win;
    }

    public void incrementWins() {
        this.win += 1;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }

    public void incrementWinStreak() {
        this.winStreak += 1;
    }

    public int getHighestWinStreak() {
        return highestWinStreak;
    }

    public void setHighestWinStreak(int highestWinStreak) {
        this.highestWinStreak = highestWinStreak;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int win) {
        this.loses = win;
    }

    public void incrementLoses() {
        this.loses += 1;
    }
}

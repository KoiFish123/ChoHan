public enum Ranking {
    // Need revise
    Cho_Han_God(100000, 99999999),
    Cho_Han_King(10000, 99999),
    Veteran_Gambler (600, 9999),
    Avid_Gambler (300, 599),
    Beginner (100, 299),
    Fool_and_Miscreant(0, 99);

    final int lowerBound;
    final int upperBound;

    Ranking(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

}

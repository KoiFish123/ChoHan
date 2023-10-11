public enum Ranking {
    // Need revise
    Cho_Han_God(10000, Integer.MAX_VALUE),                 // A high limit for the top rank.
    Cho_Han_Emperor(5000, 9999),                // Introduced a new rank for a high distinction.
    Cho_Han_King(2500, 4999),                   // Adjusted range based on game dynamics.
    Cho_Han_Prince(1500, 2499),                 // Suitable for those who've played against the house successfully.
    Veteran_Gambler(1000, 1499),                // For players who consistently do well.
    Skilled_Gambler(500, 999),                  // Adjusted the name for clarity.
    Avid_Gambler(300, 499),                     // Adjusted range.
    Novice_Gambler(201, 299),                   // Players who are just above the starting amount.
    Fresh_Start(100, 200),                      // For new players
    Fool_and_Miscreant(0, 99);                  // Kept as is for comedic value.

    final int lowerBound;
    final int upperBound;

    Ranking(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

}

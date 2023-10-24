public class ChoHan {
    private static GameUtils gameUtils = new GameUtilsImpl();

    public static void main(String[] args) {

        System.out.println(
                "Chō-Han Bakuchi or simply Chō-Han (丁半) is a traditional Japanese gambling \ngame using dice.\n" +
                        "The game uses two standard six-sided dice, which are shaken in a bamboo \n" +
                        "cup or bowl by a dealer. The cup is then overturned onto the floor. Players then \n" +
                        "place their wagers on whether the sum total of numbers showing on the two \n" +
                        "dice will be \"Chō\" (even) or \"Han\" (odd). The dealer then removes the cup, \n" +
                        "displaying the dice. The winners collect their money.\n");

        System.out.println("Type 1 or Start to start the game, or 2 or Quit to quit");

        while (true) {
            String start = gameUtils.getUserInput();

            if (start.equals("start") || start.equals("1")) {
                System.out.println();
                System.out.println("Game Started\n");
                startGame();
            } else if (start.equals("quit") || start.equals("2")) {
                System.out.println("Ending the game");
                gameUtils.shutdown();
            }
        }
    }
    public static void startGame() {
        Game game = new Game();
        gameUtils.shutdown();
    }
}
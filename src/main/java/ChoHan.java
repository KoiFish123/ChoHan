public class ChoHan {
    private static GameUtils gameUtils = new GameUtilsImpl();

    public static void main(String[] args) {

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
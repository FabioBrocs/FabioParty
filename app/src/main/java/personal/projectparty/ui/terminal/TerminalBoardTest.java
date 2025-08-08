/*
 * MIT License
 *
 * Copyright (c) 2025 Fabio Broccati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package personal.projectparty.ui.terminal;

import personal.projectparty.engine.GameEngine;
import personal.projectparty.model.board.Board;
import personal.projectparty.model.board.Space;
import personal.projectparty.model.item.GameItem;
import personal.projectparty.model.player.*;

import java.util.*;

public class TerminalBoardTest {
    /*
    private static final int MAX_ROUNDS = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<AbstractPlayer> players = createPlayers();
        Board board = Board.createDefaultBoard();
        GameEngine engine = new GameEngine(board, players);

        for (int round = 1; round <= MAX_ROUNDS; round++) {
            startRound(round);

            for (Player player : engine.getPlayers()) {
                System.out.println("\nTurno di " + player.getName() + " (" + player.getType() + ")");
                chooseItem(player, scanner, engine);
                waitForInput(player, scanner);

                // Usa il nuovo metodo che gestisce effetti e movimento
                engine.handlePlayerTurn(player);

                // Mostra posizione e tipo casella dopo movimento
                Space space = engine.getCurrentSpace(player);
                System.out.println(player.getName() + " ora è sulla casella " + player.getPosition() + " (" + space.getType() + ")");
                sleep(1000);
            }

            printBoard(engine.getBoard(), engine.getPlayers());

            if (round < MAX_ROUNDS) {
                System.out.println("\nClassifica provvisoria:");
                printRanking(engine.getRanking());
                System.out.println("\nPremi INVIO per iniziare il prossimo round...");
                scanner.nextLine();
            } else {
                System.out.println("\n=== CLASSIFICA FINALE ===");
                printRanking(engine.getRanking());
                engine.getWinner().ifPresent(TerminalBoardTest::printWinner);
            }
        }
    }

    private static List<AbstractPlayer> createPlayers() {
        return List.of(
                new Player("Giocatore 1", PlayerType.HUMAN),
                new Player("CPU 1", PlayerType.CPU)
        );
    }

    private static void startRound(int round) {
        System.out.println("\n".repeat(30));
        System.out.println("=== ROUND " + round + " ===");
    }

    private static void waitForInput(Player player, Scanner scanner) {
        if (player.getType() == PlayerType.HUMAN) {
            System.out.println("Premi INVIO per tirare i dadi.");
            scanner.nextLine();
        } else {
            System.out.println(player.getName() + " (CPU) sta tirando i dadi...");
            sleep(1000);
        }
    }

    private static void printBoard(Board board, List<Player> players) {
        for (int i = 0; i < board.size(); i++) {
            StringBuilder cell = new StringBuilder();
            cell.append(i == 0 ? "[START]" : "[" + i + " " + board.getSpace(i).getType() + "]");
            for (Player player : players) {
                if (player.getPosition() == i) cell.append(" ").append(player.getName());
            }
            System.out.println(cell);
        }
    }

    private static void printRanking(List<Player> ranking) {
        ranking.forEach(p ->
                System.out.printf("%s - Stelle: %d | Monete: %d%n",
                        p.getName(), p.getStars(), p.getCoins()));
    }

    private static void printWinner(Player winner) {
        System.out.println("\nIl vincitore è " + winner.getName() +
                " con " + winner.getStars() + " stelle e " + winner.getCoins() + " monete!");
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    private static void chooseItem(Player player, Scanner scanner, GameEngine engine) {
        List<GameItem> items = player.getInventory();
        if (items.isEmpty()) return;

        if (player.getType() == PlayerType.HUMAN) {
            System.out.println("Oggetti disponibili:");
            for (int i = 0; i < items.size(); i++) {
                System.out.printf("%d. %s - %s%n", i + 1, items.get(i).getName(), items.get(i).getDescription());
            }
            System.out.print("Vuoi usare un oggetto? (numero o 0 per saltare): ");
            int scelta = 0;
            try {
                scelta = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException ignored) {}
            if (scelta > 0 && scelta <= items.size()) {
                GameItem item = items.get(scelta - 1);
                item.use(player, engine);
                player.removeItem(item);
                System.out.println("Hai usato " + item.getName());
            }
        } else {
            // Logica semplice per CPU: 50% chance di usare un oggetto se presente
            if (!items.isEmpty() && new Random().nextBoolean()) {
                GameItem item = items.get(0);
                item.use(player, engine);
                player.removeItem(item);
                System.out.println(player.getName() + " ha usato " + item.getName());
            }
        }
    }*/
}

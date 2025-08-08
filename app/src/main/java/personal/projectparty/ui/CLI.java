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

package personal.projectparty.ui;

import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;
import personal.projectparty.model.board.Board;
import personal.projectparty.model.board.Space;
import personal.projectparty.model.player.AbstractPlayer;

import static java.lang.Thread.sleep;

public class CLI implements UserInterface{

    private final Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void askStart(String message) {
        showMessage(message);
        scanner.nextLine();
    }

    @Override
    public String getInput(){
        return scanner.nextLine();
    }

    @Override
    public void onRollDice(Runnable action) {
        showMessage("Press ENTER to roll the dice");
        scanner.nextLine();
        action.run();
    }

    @Override
    public String askString(String message) {
        showMessage(message);
        return scanner.nextLine();
    }

    @Override
    public Integer askNumber(Integer min, Integer max) {
        showMessage("Please enter a number between " + min + " and " + max);
        String input = scanner.nextLine();
        try {
             int number = Integer.parseInt(input);
             if (number < min || number > max) {
                 showMessage("Invalid number");
                 return askNumber(min, max);
             }
             return number;
        } catch (NumberFormatException e) {return askNumber(min, max);}
    }

    @Override
    public boolean askYesNo(String message) {
        showMessage(message + " (Y/N)");
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("YES")) {return true;}
        else if (input.equalsIgnoreCase("N") || input.equalsIgnoreCase("NO")) {return false;}
        else {
            showMessage("Invalid input");
            return askYesNo(message);
        }
    }

    @Override
    public Integer rollDiceHuman(Integer diceNumber) {
        int sum=0;
        for (int i = 0; i < diceNumber; i++) {
            showMessage("Press ENTER to roll the dice");
            scanner.nextLine();
            sum+=random.nextInt(6) + 1;
        }
        return sum;
    }

    @Override
    public Integer rollDiceCPU(Integer diceNumber) {
        int sum=0;
        for (int i = 0; i < diceNumber; i++) {
            sum+=random.nextInt(6) + 1;
        }
        waitSeconds(1);
        return sum;
    }

    @Override
    public void waitSeconds(Integer milliseconds) {
        try {
            sleep(milliseconds*1000);
        } catch (InterruptedException ignored) {}
    }

    @Override
    public void displayBoard(Board board, List<AbstractPlayer> players) {
        int size = board.size();

        StringBuilder separationLine = new StringBuilder("+");
        StringBuilder spaceLine = new StringBuilder(" ");
        StringBuilder playerLine = new StringBuilder(" ");

        for (int i = 0; i < size; i++) {
            Space space = board.getSpace(i);
            spaceLine.append(String.format("("+space.getSymbol()+")-"));
            separationLine.append("----");
            int finalI = i;
            Optional<AbstractPlayer> playerOnSpace = players.stream()
                    .filter(p -> p.getPosition() == finalI).findFirst();

            if (playerOnSpace.isPresent()) {
                String initials = "";
                AbstractPlayer player = playerOnSpace.get();
                if(!player.isHuman()) { initials = String.valueOf(playerOnSpace.get().getName().charAt(0))
                        .concat(String.valueOf(playerOnSpace.get().getName().charAt(3)));}
                else { initials = (player.getName().length()<2) ? player.getName() : player.getName().substring(0, 2);}
                playerLine.append(String.format("%2s", initials));
                if(players.stream().filter(p -> p.getPosition() == finalI).count() > 1) {playerLine.append("+ ");}
                else {playerLine.append("  ");}

            } else {
                playerLine.append("    ");
            }


        }
        separationLine.append("+");
        System.out.println(separationLine);
        System.out.println(playerLine);
        System.out.println(spaceLine);
        System.out.println(separationLine);

    }

    @Override
    public void displayLeaderboard(List<AbstractPlayer> players) {
        System.out.println("\n--- LEADERBOARD ---");
        for(int i = 0; i < players.size(); i++) {
            AbstractPlayer player = players.get(i);
            System.out.printf((i+1) + ") %-10s | Coins: %2d | Stars: %d\n",
                    player.getName(), player.getCoins(), player.getStars());
        }
    }

    @Override
    public void displayFinalLeaderboard(List<AbstractPlayer> players) {
        System.out.println("\n--- FINAL LEADERBOARD ---");
        for(int i = 0; i < players.size(); i++) {
            AbstractPlayer player = players.get(i);
            System.out.printf((i+1) + ") %-10s | Coins: %2d | Stars: %d\n",
                    player.getName(), player.getCoins(), player.getStars());
            waitSeconds(1);
        }
        System.out.println("Congratulations " + players.getFirst().getName() + " for winning the game!");
    }

    public void displayPlayerInfo(AbstractPlayer player) {
        System.out.println("Coins: " + player.getCoins());
        System.out.println("Stars: " + player.getStars());
        if(!player.getInventory().isEmpty()) {
        System.out.print("Inventory: ");
        player.getInventory().forEach(item -> System.out.print(item.getName() + ","));
        }
        System.out.println();
    }

    public void cleanDisplay() {
        for (int i = 0; i < 100; i++) {
            System.out.println("\n");
        }
        System.out.flush();
    }
}

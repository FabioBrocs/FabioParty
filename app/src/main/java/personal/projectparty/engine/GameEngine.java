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

package personal.projectparty.engine;

import personal.projectparty.model.board.Board;
import personal.projectparty.effects.boardeffects.*;
import personal.projectparty.model.player.*;
import personal.projectparty.ui.UserInterface;
import personal.projectparty.model.player.difficulty.Difficulty;
import personal.projectparty.effects.playereffects.*;
import personal.projectparty.model.board.boardspaces.SpaceEffectFactory;
import personal.projectparty.model.item.GameItem;
import personal.projectparty.model.minigame.MinigameFactory;
import personal.projectparty.model.minigame.AbstractMinigame;
import java.util.*;

public class GameEngine {
    private Board board;
    private List<AbstractPlayer> players;
    private Integer turns;
    private Integer currentTurn=1;
    private final Random random = new Random();
    private final UserInterface ui;
    private boolean skipDiceRoll = false;
    private Difficulty gameDifficulty;

    public GameEngine(UserInterface ui) {
        this.ui = ui;
    }

    public void start() {
        ui.cleanDisplay();

        ui.showMessage("- Welcome to Fabio Party! -");
        do {
            players = createPlayers();
            gameDifficulty = selectDifficulty();
            setCPUDifficulty(gameDifficulty);
            board = createBoard();
            turns = selectTurnNumber();

            ui.showMessage(">Game Settings:");
            ui.showMessage("Players: ");
            for (AbstractPlayer player : players) {
                ui.showMessage(" - " + player.getName());}
            ui.showMessage("Board size: " + board.size());
            ui.showMessage("Turns: " + turns);
            ui.showMessage("Difficulty: " + gameDifficulty.toString());
        } while (!ui.askYesNo(">Are these settings correct?"));

        startGame();
    }


    public Board getBoard() {return board;}
    public UserInterface getUI() {return ui;}
    public Difficulty getGameDifficulty() {return gameDifficulty;}

    public List<AbstractPlayer> getPlayers() {return players;}
    public List<AbstractPlayer> getRanking() {
        return players.stream()
                .sorted(Comparator.comparingInt(AbstractPlayer::getStars)
                        .thenComparingInt(AbstractPlayer::getCoins).reversed())
                .toList();
    }
    public Optional<AbstractPlayer> getWinner() {return getRanking().stream().findFirst();}

    public boolean useItem(AbstractPlayer player, Integer itemIndex) {
        itemIndex--;
        if(itemIndex<0 || itemIndex>player.getInventory().size()) {return false;}
        player.getInventory().get(itemIndex).use(player, this);
        player.getInventory().remove(player.getInventory().get(itemIndex));
        return true;
    }

    public boolean useItem(AbstractPlayer player, GameItem item) {
        if(item==null) return false;
        if(player.getInventory().contains(item)) {
            return useItem(player, player.getInventory().indexOf(item)+1);
        } else {
            ui.showMessage(">You don't have this item!");
            return false;
        }
    }

    public boolean useItemHandler(HumanPlayer player) {
        ui.showMessage(">Choose an item to use:");
        ui.showMessage("0. Exit");
        for (int i = 0; i < player.getInventory().size(); i++) {
            ui.showMessage((i + 1) + ". " + player.getInventory().get(i).getName());
        }
        Integer itemIndex = ui.askNumber(0, player.getInventory().size());
        if(itemIndex==0) {return false;}
        if (useItem(player, itemIndex)) {
            ui.showMessage("Item used!");
            return true;
        } else {
            ui.showMessage("Invalid item index!");
            return false;
        }
    }

    private List<AbstractPlayer> createPlayers() {
        ui.showMessage(">How many players would like to play?");
        Integer numberOfPlayers = ui.askNumber(2, 4);
        List<AbstractPlayer> players = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            boolean isHuman = ui.askYesNo(">Is player " + i + " a human?");
            if (isHuman) {
                players.add(new HumanPlayer(ui.askString(">Insert player " + i + "'s name:")));
            } else {
                players.add(new ComputerPlayer("CPU" + i, Difficulty.MEDIUM));
            }
        }
        return players;
    }

    private void setCPUDifficulty(Difficulty difficulty) {
        players.forEach(player -> {
            if(!player.isHuman()) {
                ComputerPlayer cpuPlayer = (ComputerPlayer) player;
                cpuPlayer.setDifficulty(difficulty);
            }
        });
    }

    private Board createBoard() {
        ui.showMessage(">Which board do you want to play on? (1-3)?");
        ui.showMessage("1. Small (20 Spaces)");
        ui.showMessage("2. Medium (30 Spaces)");
        ui.showMessage("3. Big (40 Spaces)");
        Integer boardIndex = ui.askNumber(1, 3);
        return switch (boardIndex) {
            case 1 -> Board.createSmallBoard();
            case 2 -> Board.createMediumBoard();
            case 3 -> Board.createBigBoard();
            default -> Board.createDefaultBoard();
        };
    }

    private Integer selectTurnNumber() {
        ui.showMessage(">How many turns would you like to play? (5-15)");
        return ui.askNumber(5, 15);
    }

    private Difficulty selectDifficulty() {
        ui.showMessage(">Select the difficulty of the CPUs and minigames (Default: Medium):");
        ui.showMessage("1. Easy");
        ui.showMessage("2. Medium");
        ui.showMessage("3. Hard");
        ui.showMessage("4. Crazy");
        Integer difficultyIndex = ui.askNumber(1, 4);
        switch (difficultyIndex) {
            case 1: return Difficulty.EASY;
            case 2: return Difficulty.MEDIUM;
            case 3: return Difficulty.HARD;
            case 4: return Difficulty.CRAZY;
            default: return Difficulty.MEDIUM;
        }
    }

    private void startGame() {
        decidePlayerOrder();
        ui.waitSeconds(3);
        turnHandler();
    }

    private void decidePlayerOrder() {
        ui.showMessage(">Choose one of the following options:");
        ui.showMessage("1. Randomize player order automatically");
        ui.showMessage("2. Randomize player order by throwing die");
        ui.showMessage("3. Don't randomize player order");
        switch(ui.askNumber(1,3))
        {
            case 1 -> randomizePlayerOrder();
            case 2 -> throwDieToDecideOrder();
            case 3 -> ui.showMessage("Player order will remain the same");
            default -> ui.showMessage("Invalid option. Player order will remain the same");
        }
    }

    private void randomizePlayerOrder() {
        Collections.shuffle(players);
        ui.showMessage(">Player order decided:");
        for (int i = 0; i < players.size(); i++) {
            ui.showMessage((i + 1) + ". " + players.get(i).getName());
        }
    }

    private void throwDieToDecideOrder() {
        ui.showMessage(">Throw die to decide the order of the players:");

        Map<AbstractPlayer, Integer> rolls = new HashMap<>();

        for (AbstractPlayer player : players) {
            ui.showMessage(">It's your turn " + player.getName());
            int roll = player.isHuman() ? ui.rollDiceHuman(1) : ui.rollDiceCPU(1);
            ui.showMessage(">"+ player.getName() + " rolled a " + roll + "\n");
            rolls.put(player, roll);
        }

        players.sort((p1, p2) -> rolls.get(p2) - rolls.get(p1));

        ui.showMessage(">Player order decided:");
        for (int i = 0; i < players.size(); i++) {
            ui.showMessage((i + 1) + ". " + players.get(i).getName());
        }
    }

    private void turnHandler() {
        currentTurn=1;
        do{
            ui.cleanDisplay();
            ui.waitSeconds(1);
            ui.showMessage("-- TURN " + currentTurn + "/" + turns + " --");
            ui.displayBoard(board, players);
            ui.displayLeaderboard(getRanking());
            checkBoardEffects();
            ui.waitSeconds(2);
            for (AbstractPlayer player : players) {
                ui.waitSeconds(1);
                ui.showMessage("\n");
                ui.showMessage(">It's your turn " + player.getName());
                ui.waitSeconds(1);
                ui.displayPlayerInfo(player);
                if (player.isHuman()) {
                    handleHumanTurn((HumanPlayer) player);
                } else {
                    handleComputerTurn((ComputerPlayer) player);
                }
            }
            ui.waitSeconds(1);
            ui.displayBoard(board, players);
            ui.waitSeconds(1);
            ui.displayLeaderboard(getRanking());
            ui.waitSeconds(5);
            ui.cleanDisplay();
            minigameHandler();
            ui.waitSeconds(5);
            currentTurn++;
        }while (currentTurn<=turns);

        ui.cleanDisplay();
        ui.waitSeconds(1);
        ui.displayFinalLeaderboard(getRanking());
        ui.waitSeconds(3);

        if(ui.askYesNo(">Would you like to play again?")) start();
    }

    private void handleHumanTurn(HumanPlayer player) {
        int choice;
        ui.displayBoard(board, players);
        if (!player.getInventory().isEmpty()) {
            do{
                ui.showMessage(">Choose an action:");
                ui.showMessage("1. Throw dice");
                ui.showMessage("2. Use item");

                choice = ui.askNumber(1, 2);

                if (choice == 2) {
                    if(useItemHandler(player)) choice = 1;
                }
            } while (choice!=1);
        }

        triggerBeforeDiceRoll(player);
        int diceCount = player.getDiceToRoll();
        int roll = ui.rollDiceHuman(diceCount);
        if(roll!=0) ui.showMessage(">"+ player.getName() + " rolled " + roll);
        ui.waitSeconds(1);
        triggerAfterDiceRoll(player, roll);
        List<AbstractPlayer> passedPlayers = board.getPlayersPassed(player, players, roll);
        for (AbstractPlayer passed : passedPlayers) {
            triggerPassPlayer(player, passed);}
        movePlayer(player, roll);
        triggerTurnEnd(player);
        player.cleanupPlayerEffects();
        ui.waitSeconds(2);
    }

    private void handleComputerTurn(ComputerPlayer player) {
        ui.displayBoard(board, players);
        if(!player.getInventory().isEmpty()) {
            useItem(player, player.useItemStrategy(this));
        }
        triggerBeforeDiceRoll(player);
        int diceCount = player.getDiceToRoll();
        int roll = ui.rollDiceCPU(diceCount);
        if(roll!=0) {
            ui.showMessage(">"+ player.getName() + " is rolling the dice...");
            ui.waitSeconds(1);
            ui.showMessage(">"+ player.getName() + " rolled a " + roll);
            ui.waitSeconds(1);
        }
        triggerAfterDiceRoll(player, roll);
        List<AbstractPlayer> passedPlayers = board.getPlayersPassed(player, players, roll);
        for (AbstractPlayer passed : passedPlayers) {
            triggerPassPlayer(player, passed);}
        movePlayer(player, roll);
        triggerTurnEnd(player);
        player.cleanupPlayerEffects();
        ui.waitSeconds(2);
    }

    public void movePlayer(AbstractPlayer player, int spaces) {
        if(spaces==0) return;
        int startingPosition = player.getPosition();
        int newPosition = board.calculateNextPosition(player.getPosition(), spaces);
        player.setPosition(newPosition);

        if(board.starSpacePassed(startingPosition, newPosition)){
            if(player.getCoins()>=20){
                if(player.isHuman()) {
                    if(ui.askYesNo(">Would you like to buy a star for 20 coins?")){
                        player.addStars(1);
                        player.removeCoins(20);
                        ui.showMessage(">"+ player.getName() + " bought a star!");
                        board.moveStarSpace();
                        ui.showMessage(">The Star has moved!");
                        ui.waitSeconds(1);
                    }
                    else {ui.showMessage(player.getName() + " chose not to buy a star...");}
                } else {
                    player.addStars(1);
                    player.removeCoins(20);
                    ui.showMessage(">"+ player.getName() + " bought a star!");
                    board.moveStarSpace();
                    ui.showMessage(">The Star has moved!");
                    ui.waitSeconds(1);
                }
            }else {ui.showMessage(player.getName() + " doesn't have enough coins to buy a star..."); ui.waitSeconds(1);}
        }

        ui.showMessage(">"+ player.getName() + " moved to: " + board.getSpaceTypeFromIndex(newPosition) + " Space.");
      SpaceEffectFactory.getEffect(board.getSpaceTypeFromIndex(player.getPosition())).apply(player, this, ui);
    }

    private void checkBoardEffects() {
        if(board.getBoardEffects().isEmpty()) return;
        List<BoardEffect> toRemove = new ArrayList<>();
        for (BoardEffect effect : board.getBoardEffects()) {
            if (effect.getRemainingDuration() == 0) {
                effect.disable(board);
                toRemove.add(effect);
            }
        }
        for (BoardEffect effect : toRemove) {
            board.removeBoardEffect(effect);
        }
        for (BoardEffect effect : board.getBoardEffects()) {
            effect.applyEffect(board);
            ui.showMessage(">The " + effect.getName() + " effect is active");
            ui.showMessage(">Effect: "+ effect.getDescription());
            ui.showMessage(">It will last for " + effect.getRemainingDuration() + " turn(s)");
            effect.decreaseRemainingDuration();
        }
    }

    private void triggerBeforeDiceRoll(AbstractPlayer player) {
        for (PlayerEffect effect : player.getPlayerEffects()) {
            effect.onBeforeDiceRoll(player, this);
        }
    }
    private void triggerAfterDiceRoll(AbstractPlayer player, int roll) {
        for (PlayerEffect effect : player.getPlayerEffects()) {
            effect.onAfterDiceRoll(player, this, roll);
        }
    }
    private void triggerPassPlayer(AbstractPlayer player, AbstractPlayer passed) {
        for (PlayerEffect effect : player.getPlayerEffects()) {
            effect.onPassPlayer(player, passed, this);
        }
    }
    private void triggerTurnEnd(AbstractPlayer player) {
        for (PlayerEffect effect : player.getPlayerEffects()) {
            effect.onTurnEnd(player, this);
        }
    }

    private void minigameHandler() {
        ui.showMessage(">Time for a Minigame! The winners get 5 coins!");
        ui.waitSeconds(2);
        AbstractMinigame minigame = MinigameFactory.getRandomMinigame();
        ui.showMessage(">The Minigame is: " + minigame.getName());
        ui.waitSeconds(1);
        ui.showMessage(minigame.getDescription());
        ui.waitSeconds(2);
        for(AbstractPlayer player : players) {
            ui.showMessage("\n>It's your turn " + player.getName()+"\n");
            ui.waitSeconds(1);
            if(player.isHuman()) {
                HumanPlayer humanPlayer = (HumanPlayer) player;
                if(minigame.playHuman(humanPlayer, this)) {
                    ui.showMessage(">"+ player.getName() + " won the Minigame! You get 5 coins!");
                    player.addCoins(5);}
                else {ui.showMessage(">"+ player.getName() + " too bad, you lost the minigame...");}
            }
            else {
                ComputerPlayer computerPlayer = (ComputerPlayer) player;
                if(minigame.playComputer(computerPlayer)){
                    ui.showMessage(">"+ player.getName() + " won the Minigame! You get 5 coins!");
                    player.addCoins(5);}
                else {ui.showMessage(">"+ player.getName() + " too bad, you lost the minigame...");}
                }
            ui.waitSeconds(1);
        }
    }
}


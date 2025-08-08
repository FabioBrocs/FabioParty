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

package personal.projectparty.model.board;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.IntStream;

import personal.projectparty.effects.boardeffects.*;
import personal.projectparty.model.player.AbstractPlayer;


public class Board {
    private List<Space> spaces;
    private final List<BoardEffect> boardEffects;
    private int multiplier;

    public Board(List<Space> spaces) {
        this.spaces = spaces;
        this.multiplier = 1;
        this.boardEffects = new ArrayList<>();
    }

    public Space getSpace(int position) {
        int index = position % spaces.size();
        return spaces.get(index);
    }

    public int size() {
        return spaces.size();
    }

    public List<Space> getSpaces() {return spaces;}

    public List<BoardEffect> getBoardEffects() {return boardEffects;}

    public int getMultiplier() {return multiplier;}
    public void setMultiplier(int multiplier) {this.multiplier = multiplier;}

    public static Board createDefaultBoard() {
        List<Space> spaces = List.of(
                new Space(0, SpaceType.START),
                new Space(1, SpaceType.BLUE),
                new Space(2, SpaceType.RED),
                new Space(3, SpaceType.EVENT),
                new Space(4, SpaceType.BLUE),
                new Space(5, SpaceType.ITEM),
                new Space(6, SpaceType.RED),
                new Space(7, SpaceType.STAR)
        );
        return new Board(spaces);
    }

    public static Board createSmallBoard() {
        List<Space> spaces = List.of(
                new Space(0, SpaceType.START),
                new Space(1, SpaceType.BLUE),
                new Space(2, SpaceType.RED),
                new Space(3, SpaceType.EVENT),
                new Space(4, SpaceType.BLUE),
                new Space(5, SpaceType.ITEM),
                new Space(6, SpaceType.RED),
                new Space(7, SpaceType.RED),
                new Space(8, SpaceType.BLUE),
                new Space(9, SpaceType.BLUE),
                new Space(10, SpaceType.RED),
                new Space(11, SpaceType.BLUE),
                new Space(12, SpaceType.STAR),
                new Space(13, SpaceType.RED),
                new Space(14, SpaceType.BLUE),
                new Space(15, SpaceType.ITEM),
                new Space(16, SpaceType.BLUE),
                new Space(17, SpaceType.EVENT),
                new Space(18, SpaceType.RED),
                new Space(19, SpaceType.BLUE)
        );
        return new Board(spaces);
    }

    public static Board createMediumBoard() {
        List<Space> spaces = List.of(
                new Space(0, SpaceType.START),
                new Space(1, SpaceType.BLUE),
                new Space(2, SpaceType.RED),
                new Space(3, SpaceType.RED),
                new Space(4, SpaceType.BLUE),
                new Space(5, SpaceType.EVENT),
                new Space(6, SpaceType.RED),
                new Space(7, SpaceType.ITEM),
                new Space(8, SpaceType.BLUE),
                new Space(9, SpaceType.BLUE),
                new Space(10, SpaceType.RED),
                new Space(11, SpaceType.BLUE),
                new Space(12, SpaceType.RED),
                new Space(13, SpaceType.RED),
                new Space(14, SpaceType.BLUE),
                new Space(15, SpaceType.RED),
                new Space(16, SpaceType.BLUE),
                new Space(17, SpaceType.ITEM),
                new Space(18, SpaceType.STAR),
                new Space(19, SpaceType.BLUE),
                new Space(20, SpaceType.RED),
                new Space(21, SpaceType.BLUE),
                new Space(22, SpaceType.RED),
                new Space(23, SpaceType.RED),
                new Space(24, SpaceType.BLUE),
                new Space(25, SpaceType.EVENT),
                new Space(26, SpaceType.BLUE),
                new Space(27, SpaceType.ITEM),
                new Space(28, SpaceType.RED),
                new Space(29, SpaceType.BLUE)
        );
        return new Board(spaces);
    }

    public static Board createBigBoard() {
        List<Space> spaces = List.of(
                new Space(0, SpaceType.START),
                new Space(1, SpaceType.BLUE),
                new Space(2, SpaceType.RED),
                new Space(3, SpaceType.RED),
                new Space(4, SpaceType.BLUE),
                new Space(5, SpaceType.BLUE),
                new Space(6, SpaceType.EVENT),
                new Space(7, SpaceType.RED),
                new Space(8, SpaceType.ITEM),
                new Space(9, SpaceType.BLUE),
                new Space(10, SpaceType.RED),
                new Space(11, SpaceType.BLUE),
                new Space(12, SpaceType.RED),
                new Space(13, SpaceType.RED),
                new Space(14, SpaceType.BLUE),
                new Space(15, SpaceType.RED),
                new Space(16, SpaceType.BLUE),
                new Space(17, SpaceType.RED),
                new Space(18, SpaceType.ITEM),
                new Space(19, SpaceType.BLUE),
                new Space(20, SpaceType.RED),
                new Space(21, SpaceType.BLUE),
                new Space(22, SpaceType.RED),
                new Space(23, SpaceType.RED),
                new Space(24, SpaceType.STAR),
                new Space(25, SpaceType.RED),
                new Space(26, SpaceType.BLUE),
                new Space(27, SpaceType.RED),
                new Space(28, SpaceType.ITEM),
                new Space(29, SpaceType.BLUE),
                new Space(30, SpaceType.RED),
                new Space(31, SpaceType.BLUE),
                new Space(32, SpaceType.RED),
                new Space(33, SpaceType.RED),
                new Space(34, SpaceType.EVENT),
                new Space(35, SpaceType.BLUE),
                new Space(36, SpaceType.RED),
                new Space(37, SpaceType.RED),
                new Space(38, SpaceType.ITEM),
                new Space(39, SpaceType.BLUE)
        );
        return new Board(spaces);
    }

    public int findFirstSpaceOfType(SpaceType type) {
        return IntStream.range(0, spaces.size())
                .filter(i -> spaces.get(i).getType() == type)
                .findFirst()
                .orElse(-1);
    }

    public Space getSpaceFromIndex(Integer index) {return spaces.get(index);}

    public int calculateNextPosition(int initialSpace, int spaces) {
        int boardSize = size();
        return (initialSpace + spaces) % boardSize;
    }

    public int calculateStepsToNextSpace(int initialSpace, int endingSpace) {
        int boardSize = size();
        int current = (initialSpace + 1) % boardSize;
        int steps = 0;
        while (current != (endingSpace + 1) % boardSize) {
            steps++;
            current = (current + 1) % boardSize;
        }
        return steps;
    }

    public int getStarSpaceIndex() {return findFirstSpaceOfType(SpaceType.STAR);}

    public void moveStarSpace() {
        switch(spaces.size()){
            case 20:
                if(getStarSpaceIndex()==12){
                    spaces.get(12).changeType(SpaceType.BLUE);
                    spaces.get(8).changeType(SpaceType.STAR);
                }else{
                    spaces.get(12).changeType(SpaceType.STAR);
                    spaces.get(8).changeType(SpaceType.BLUE);
                }
                break;
            case 30:
                if(getStarSpaceIndex()==18){
                    spaces.set(18, new Space(18, SpaceType.STAR));
                    spaces.set(12, new Space(12, SpaceType.BLUE));
                }else{
                    spaces.set(18, new Space(18, SpaceType.BLUE));
                    spaces.set(12, new Space(12, SpaceType.STAR));
                }
                break;
            case 40:
                if(getStarSpaceIndex()==24){
                    spaces.set(24, new Space(24, SpaceType.STAR));
                    spaces.set(16, new Space(16, SpaceType.BLUE));
                }else{
                    spaces.set(24, new Space(24, SpaceType.BLUE));
                    spaces.set(16, new Space(16, SpaceType.STAR));
                }
                break;
            default: break;

        }
    }

    public boolean starSpacePassed(int startingSpace, int endingSpace) {
        int boardSize = size();

        int current = (startingSpace + 1) % boardSize;
        while (current != (endingSpace + 1) % boardSize) {
            if (getSpace(current).getType() == SpaceType.STAR) {
                return true;
            }
            current = (current + 1) % boardSize;
        }
        return false;
    }

    public SpaceType getSpaceTypeFromIndex(Integer index) {return getSpaceFromIndex(index).getType();}

    public boolean addBoardEffect(BoardEffect boardEffect) {
        if(boardEffects.contains(boardEffect)) {return false;}
        boardEffects.add(boardEffect);
        return true;
    }

    public void removeBoardEffect(BoardEffect boardEffect) {
        if(!boardEffects.contains(boardEffect)) {return;}
        boardEffects.remove(boardEffect);}

    public List<AbstractPlayer> getPlayersPassed(AbstractPlayer mover, List<AbstractPlayer> players, int rolledValue) {
        List<AbstractPlayer> passedPlayers = new ArrayList<>();
        int from = mover.getPosition();
        int to = calculateNextPosition(from, rolledValue);

        for (AbstractPlayer other : players) {
            if (other == mover) continue;

            int pos = other.getPosition();
            if (from < to) {
                if (pos > from && pos <= to) {
                    passedPlayers.add(other);
                }
            } else {
                if (pos > from || pos <= to) {
                    passedPlayers.add(other);
                }
            }
        }
        return passedPlayers;
    }
}


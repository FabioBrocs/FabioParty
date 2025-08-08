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

package personal.projectparty.model.player;

import personal.projectparty.model.player.strategy.*;
import personal.projectparty.model.player.difficulty.Difficulty;
import personal.projectparty.model.item.GameItem;
import personal.projectparty.engine.GameEngine;
import java.util.Optional;

public class ComputerPlayer extends AbstractPlayer{

    ItemStrategy itemStrategy;
    Difficulty difficulty;

    public ComputerPlayer(String name, Difficulty difficulty) {
        super(name);
        this.difficulty = difficulty;
        this.itemStrategy = setStrategyBasedOnDifficulty();
        isHuman = false;

    }

    private ItemStrategy setStrategyBasedOnDifficulty(){
        switch(difficulty){
            case EASY: return new BasicItemStrategy();
            case MEDIUM: return new NoWasteItemStrategy();
            case HARD, CRAZY: return new SmartUseStrategy();
            default: return new RandomItemStrategy();
        }
    }

    public void setDifficulty(Difficulty difficulty){
        this.difficulty = difficulty;
        this.itemStrategy = setStrategyBasedOnDifficulty();
    }

    public Difficulty getDifficulty(){return difficulty;}

    public GameItem useItemStrategy(GameEngine engine) {
        Optional<GameItem> item = itemStrategy.chooseItem(inventory, engine, this);
        return item.orElse(null);
    }
}

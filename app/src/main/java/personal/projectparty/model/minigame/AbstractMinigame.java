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

package personal.projectparty.model.minigame;

import personal.projectparty.model.player.*;
import personal.projectparty.engine.GameEngine;

public class AbstractMinigame implements MinigameInterface{
    private String name = "Default Name";
    private String description = "Default Description";

    @Override
    public boolean playHuman(HumanPlayer player, GameEngine engine) {
        return false;
    }

    @Override
    public boolean playComputer(ComputerPlayer player) {
        switch (player.getDifficulty()) {
            case EASY: return ( Math.random() > 0.5 );
            case MEDIUM: return ( Math.random() > 0.3 );
            case HARD: return ( Math.random() > 0.2 );
            case CRAZY: return ( Math.random() > 0.1 );
            default: return ( Math.random() > 0.3 );
        }
    }

    @Override
    public String getName() {return name;}

    @Override
    public String getDescription() {return description;}
}

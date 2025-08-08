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

package personal.projectparty.effects.playereffects;
import personal.projectparty.model.player.AbstractPlayer;
import personal.projectparty.engine.GameEngine;

public class CometStarEffect implements PlayerEffect {
    @Override
    public void onBeforeDiceRoll(AbstractPlayer player, GameEngine engine) {
        if(player.isHuman()) {
            if (engine.getUI().askYesNo(">Buy a Star for 20 coins?")) {
                engine.movePlayer(player,
                        engine.getBoard().calculateStepsToNextSpace(player.getPosition(), engine.getBoard().getStarSpaceIndex()));
                engine.getUI().showMessage(">"+ player.getName() + " bought a Star!");
            } else {
                player.setPosition(engine.getBoard().getStarSpaceIndex());
            }
        }
        else {
            if(player.getCoins()<20) {
                player.setPosition(engine.getBoard().getStarSpaceIndex());
            }
            else{
                engine.movePlayer(player,
                        engine.getBoard().calculateStepsToNextSpace(player.getPosition(), engine.getBoard().getStarSpaceIndex()));
                engine.getUI().showMessage(">"+ player.getName() + " bought a Star!");
            }
        }
    }

    @Override
    public void onAfterDiceRoll(AbstractPlayer player, GameEngine engine, int rolledValue) {}

    @Override
    public void onPassPlayer(AbstractPlayer owner, AbstractPlayer passed, GameEngine engine) {}

    @Override
    public void onTurnEnd(AbstractPlayer player, GameEngine engine) {
        player.setDiceToRoll(1);
    }

    @Override
    public boolean isExpired() {
        return true;
    }
}

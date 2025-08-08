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

package personal.projectparty.model.player.strategy;

import personal.projectparty.model.item.GameItem;
import personal.projectparty.engine.GameEngine;
import personal.projectparty.model.player.*;
import personal.projectparty.model.item.*;
import java.util.List;
import java.util.Optional;

public class SmartUseStrategy implements ItemStrategy {
    @Override
    public Optional<GameItem> chooseItem(List<GameItem> inventory, GameEngine engine, ComputerPlayer player) {
        int playerCoins = player.getCoins();
        int playerPosition = player.getPosition();
        int starPosition = engine.getBoard().getStarSpaceIndex();
        int distanceToStar = engine.getBoard().calculateStepsToNextSpace(playerPosition, starPosition);

        // Filter items by type so we only deal with actual instances in the inventory
        GameItem cometStar = player.findItemOfType(CometStarItem.class);
        GameItem thiefGlove = player.findItemOfType(ThiefGloveItem.class);
        GameItem doubleDice = player.findItemOfType(DoubleDiceItem.class);

        // Use CometStar only if coins >= 20
        if (cometStar != null && playerCoins >= 20) {
            return Optional.of(cometStar);
        }

        // Use ThiefGlove if players are within 4 spaces
        List<AbstractPlayer> nearbyPlayers = engine.getBoard()
                .getPlayersPassed(player, engine.getPlayers(), 4);
        if (thiefGlove != null && !nearbyPlayers.isEmpty()) {
            return Optional.of(thiefGlove);
        }

        // Use DoubleDice if star is far and we have enough coins
        if (doubleDice != null && distanceToStar > 6 && playerCoins >= 20) {
            return Optional.of(doubleDice);
        }

        // Fallback: if inventory is full, use DoubleDice to clear space
        if (doubleDice != null && inventory.size() == 3) {
            return Optional.of(doubleDice);
        }

        return Optional.empty();
    }
}



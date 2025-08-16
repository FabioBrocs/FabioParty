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
import personal.projectparty.ui.UserInterface;
import java.util.concurrent.atomic.AtomicBoolean;

public class StringMinigame extends AbstractMinigame{
    @Override
    public String getName() {
        return "String Minigame";
    }

    @Override
    public String getDescription() {
        return "You have a few seconds to copy the given string.";
    }

    @Override
    public boolean playHuman(HumanPlayer player, GameEngine engine) {
        switch (engine.getGameDifficulty()) {
            case EASY:
                return playMinigame(player, engine.getUI(), 5, 4);
            case MEDIUM:
                return playMinigame(player, engine.getUI(), 4, 5);
            case HARD:
                return playMinigame(player, engine.getUI(), 3, 6);
            case CRAZY:
                return playMinigame(player, engine.getUI(), 2,5);
            default:
                return playMinigame(player, engine.getUI(), 4, 5);
        }
    }

    private boolean playMinigame(HumanPlayer player, UserInterface ui, int durationSeconds, int stringSize) {

        AtomicBoolean success = new AtomicBoolean(false);
        AtomicBoolean timerRunning = new AtomicBoolean(true);

        Thread timerThread = new Thread(() -> {
            try {
                for (int i = 0; i < durationSeconds; i++) {
                    if (!timerRunning.get()) return;
                    Thread.sleep(1000);
                }
                timerRunning.set(false); // Time's up
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });



        StringBuilder stringToCopy = new StringBuilder();
        char c;
        for (int i = 0; i < stringSize; i++) {
            c = (char) (Math.random() * 25 + 65);
            stringToCopy.append(c);
        }

        ui.showMessage(">You have " + durationSeconds + " seconds to write:");
        ui.askStart("Press ENTER to start...");
        timerThread.start();
        ui.showMessage(stringToCopy.toString());


        while (timerRunning.get()) {
            String input = ui.askString("");

            if (!timerRunning.get()) break;

            if (input.equalsIgnoreCase(stringToCopy.toString())) {
                success.set(true);
                timerRunning.set(false);
                break;
            } else {
                ui.showMessage("Incorrect! Try again:");
            }
        }

        try {
            timerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (success.get()) {
            ui.showMessage("You won the minigame!");
        } else {
            ui.showMessage("Time's up! You lost the minigame.");
        }
        return success.get();
    }
}

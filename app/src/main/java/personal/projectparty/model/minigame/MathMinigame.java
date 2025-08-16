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


public class MathMinigame extends AbstractMinigame {
    @Override
    public String getName() {
        return "Math Minigame";
    }

    @Override
    public String getDescription() {
        return "You have a few seconds to guess the answer.";
    }

    public boolean playHuman(HumanPlayer player, GameEngine engine) {
        switch (engine.getGameDifficulty()) {
            case EASY:
                return playMinigame(player, engine.getUI(), 5, 10);
            case MEDIUM:
                return playMinigame(player, engine.getUI(), 4, 15);
            case HARD:
                return playMinigame(player, engine.getUI(), 3, 20);
            case CRAZY:
                return playMinigame(player, engine.getUI(), 3, 30);
            default:
                return playMinigame(player, engine.getUI(), 4, 15);
        }
    }

    private boolean playMinigame(HumanPlayer player, UserInterface ui, int durationSeconds, int numberRange) {

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

        int a = Math.toIntExact(Math.round(Math.random() * numberRange));
        int b = Math.toIntExact(Math.round(Math.random() * numberRange));
        int result = 0;
        double operatorChooser;
        StringBuilder equation = new StringBuilder();
        equation.append(a + " ");
        operatorChooser = Math.random();
        if(operatorChooser < 0.5) {
            equation.append("+ ");
            result = a+b;
        } else {
            equation.append("- ");
            result = a-b;
        }
        equation.append(b).append(" =  ?");

        ui.showMessage(">Solve the equation to win! You have " + durationSeconds + " seconds.");
        ui.askStart("Press ENTER to start...");
        timerThread.start();

        ui.showMessage(equation.toString());

        while (timerRunning.get()) {
            String input = ui.askString("");

            if (!timerRunning.get()) break;

            if (input.equalsIgnoreCase(String.valueOf(result))) {
                success.set(true);
                timerRunning.set(false);
                break;
            } else {
                ui.showMessage(">Incorrect! Try again:");
            }
        }

        try {
            timerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (success.get()) {
            ui.showMessage(">You won the minigame!");
        } else {
            ui.showMessage(">Time's up! You lost the minigame.");
        }
        return success.get();
    }

}

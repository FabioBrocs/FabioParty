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

import personal.projectparty.model.item.GameItem;
import personal.projectparty.effects.playereffects.*;
import java.util.List;
import java.util.ArrayList;

public class AbstractPlayer implements PlayerInterface {

    String name;
    Integer position;
    Integer coins;
    Integer stars;
    List<GameItem> inventory;
    List<PlayerEffect> playerEffects;
    boolean isHuman;
    Integer diceToRoll;

    public AbstractPlayer(String name) {
        this.name = name;
        this.position = 0;
        this.coins = 10;
        this.stars = 0;
        this.inventory=new ArrayList<>();
        this.playerEffects=new ArrayList<>();
        this.diceToRoll=1;
    }

    @Override
    public String getName() {return name;}


    @Override
    public Integer getCoins() {return coins;}
    @Override
    public void addCoins(Integer coins) {this.coins+=coins;}
    @Override
    public void removeCoins(Integer coins) {
        this.coins-=coins;
        if(this.coins<0) this.coins=0;
    }
    public Integer stealCoins(Integer coins){
        int stolenCoins=Math.min(coins, this.coins);
        this.coins-=stolenCoins;
        return stolenCoins;
    }


    @Override
    public Integer getStars() {return stars;}
    @Override
    public void addStars(Integer stars) {this.stars+=stars;}
    @Override
    public void removeStars(Integer stars) {
        this.stars-=stars;
        if(this.stars<0) this.stars=0;
    }


    @Override
    public List<GameItem> getInventory() {
        return inventory;
    }
    @Override
    public boolean addItem(GameItem item) {
        if(inventory.size()>=3) return false;
        inventory.add(item);
        return true;
    }
    @Override
    public void removeItem(GameItem item) {inventory.remove(item);}


    @Override
    public Integer getPosition() {return position;}
    @Override
    public void setPosition(Integer position) {this.position=position;}

    @Override
    public boolean isHuman() {
        return isHuman;
    }

    @Override
    public List<PlayerEffect> getPlayerEffects() {return playerEffects;}
    @Override
    public void addPlayerEffect(PlayerEffect playerEffect) {playerEffects.add(playerEffect);}
    @Override
    public void removePlayerEffect(PlayerEffect playerEffect) {playerEffects.remove(playerEffect);}
    public void cleanupPlayerEffects() {playerEffects.removeIf(PlayerEffect::isExpired);}

    public void setDiceToRoll(Integer diceToRoll) {this.diceToRoll=diceToRoll;}
    public Integer getDiceToRoll() {return this.diceToRoll;}

    public GameItem findItemOfType(Class<? extends GameItem> itemType){
        for(GameItem item : inventory){
            if(item.getClass().equals(itemType)){
                return item;
            }
        }
        return null;
    }
}

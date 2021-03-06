/*******************************************************************************
 * HudPixel Reloaded (github.com/palechip/HudPixel), an unofficial Minecraft Mod for the Hypixel Network
 *
 * Copyright (c) 2014-2015 palechip (twitter.com/palechip) and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.palechip.hudpixelmod.api.interaction.representations;

import com.palechip.hudpixelmod.games.GameConfiguration;
import com.palechip.hudpixelmod.games.GameManager;
import com.palechip.hudpixelmod.util.GameType;
import com.palechip.hudpixelmod.util.UuidHelper;

public class Booster {
    public static final int TIPPING_COOLDOWN = 1800000; // = 30 min
    
    // these values are filled by the API
    // purchaser is only filled for old boosters
    private int amount;
    private long dateActivated;
    private int gameType;
    private long length;
    private long originalLength;
    private String purchaserUuid;
    
    // properties used by the mod
    private long tippingTime;
    private String owner;
    private int gameID; // saves the ID the mod uses for the game and not the database ID.
    
    public Booster(){}
    
    public int getCoinAmount() {
        return amount;
    }
    
    public long getActivationDateAndTime() {
        return dateActivated;
    }
    
    /**
     * Returns the mod ID for the game which will can be used to get the game configuration
     * @return
     */
    public int getGameID() {
        if(this.gameID == 0) {
            // go through all configurations
            for(GameConfiguration config : GameManager.getGameManager().getConfigurations()) {
                // if we find one with a matching database id
                if(config.getDatabaseID() == this.gameType) {
                    // save it
                    this.gameID = config.getModID();
                }
            }
        }
        return this.gameID;
    }
    
    public long getRemainingTime() {
        return length;
    }
    
    public long getTotalLength() {
        return originalLength;
    }
    
    public String getOwner() {
        if(this.owner == null || this.owner.equals("!ERROR!")) {
            this.owner = UuidHelper.getUsernameFormUUID(purchaserUuid);
        }
        // did getting the name fail?
        if(this.owner == null) {
            this.owner = "!ERROR!";
        }
        return owner;
    }
    
    /**
     * This saves the time when the booster was tipped.
     */
    public void tip() {
        this.tippingTime = System.currentTimeMillis();
    }
    
    public boolean canTip() {
        // this expression will be true if the booster is active and wasn't tipped or can be tipped again
        return (this.getRemainingTime() < this.getTotalLength() && (this.tippingTime == 0l || System.currentTimeMillis() > this.tippingTime + TIPPING_COOLDOWN ));
    }
    
    public void setTippingTime(long time) {
        this.tippingTime = time;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Booster) {
            Booster b = (Booster)obj;
            return this.dateActivated == b.dateActivated && this.owner.equals(b.owner) && this.gameType == b.gameType;
        }
        return super.equals(obj);
    }
}

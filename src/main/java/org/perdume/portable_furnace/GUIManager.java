package org.perdume.portable_furnace;

import org.bukkit.entity.Player;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class GUIManager {

    private Portable_furnace main = Portable_furnace.getPlugin(Portable_furnace.class);
    HashMap<Player, FuranceGUI> FurGUI =  new HashMap<>();

    public FuranceGUI getFuranceGUI(Player p){
        return new FuranceGUI(p);
    }
}

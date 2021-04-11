package com.flora.bank.event;

import com.flora.bank.gui.InventoryBank;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventInteractNPC implements Listener
{
    @EventHandler
    private void onInteractNPCEvent(NPCRightClickEvent event) {
        Player player = event.getClicker();

        if (event.getNPC().getName().equalsIgnoreCase("DellunaBank123")) {
            player.openInventory(new InventoryBank(player).getInventory());
        }
    }
}

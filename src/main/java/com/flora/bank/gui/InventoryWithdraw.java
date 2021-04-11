package com.flora.bank.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

public class InventoryWithdraw implements Listener
{
    Player player;

    public InventoryWithdraw(Player player) {
        this.player = player;
    }

    public Inventory getInventory() {
        Inventory inv = Bukkit.getServer().createInventory(null, 54, "§b 델루나 은행§dㆍ 출금");

        inv.setItem(11, InventoryIcon.iconGoldButton());
        inv.setItem(20, InventoryIcon.iconInteractWithdraw(1));

        inv.setItem(13, InventoryIcon.iconSilverButton());
        inv.setItem(22, InventoryIcon.iconInteractWithdraw(2));

        inv.setItem(15, InventoryIcon.iconCopperButton());
        inv.setItem(24, InventoryIcon.iconInteractWithdraw(3));


        // 경계선 윗부분 모두 바탕블록으로 채우기
        for (int i = 0; i < 36; i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, InventoryIcon.iconNull());
        }

        // 흰색 경계선 블록 채우기
        for (int i = 36; i < 45; i++) {
            inv.setItem(i,InventoryIcon.iconNull2());
        }

        inv.setItem(45, InventoryIcon.iconMoney(player.getUniqueId()));
        inv.setItem(49, InventoryIcon.iconAccess());
        inv.setItem(53, InventoryIcon.iconBack());

        return inv;
    }
}

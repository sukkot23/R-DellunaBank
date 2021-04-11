package com.flora.bank.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryBank
{
    // 플레이어 전역변수 선언
    Player player;

    public InventoryBank(Player player) {
        // 받아온 플레이어 객체를 전역변수에 넣음
        this.player = player;
    }

    // 인벤토리 정보를 반환
    public Inventory getInventory() {
        // 서버 내 새로운 인벤토리 생성
        Inventory inv = Bukkit.getServer().createInventory(null, 27, "§b 델루나 은행");

        // 입금 아이콘 추가
        inv.setItem(11, InventoryIcon.iconDeposit());
        // 출금 아이콘 추가
        inv.setItem(13, InventoryIcon.iconWithdraw());
        // 소지 재화 아이콘 추가
        inv.setItem(15, InventoryIcon.iconMoney(player.getUniqueId()));


        // 남은 칸 모두 바탕블록으로 채우기
        for (int i = 0; i < inv.getSize(); i++) {
            if (inv.getItem(i) == null)
                inv.setItem(i, InventoryIcon.iconNull());
        }

        return inv;
    }
}
package com.flora.bank.gui;

import com.flora.bank.Reference;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryIcon
{
    // 입금 아이콘
    public static ItemStack iconDeposit() {
        List<String> lore = new ArrayList<>();
        lore.add("§7 우클릭 시§a 입금페이지§7로 이동합니다");

        return Reference.iconDefault(Material.CHEST, "§a입금", lore);
    }
    // 입금 상호작용 버튼
    public static ItemStack iconInteractDeposit(int model) {
        List<String> lore = new ArrayList<>();
        lore.add("§7 좌클릭: +1");
        lore.add("§7 우클릭: -1");
        lore.add("§7 쉬프트+좌클릭: +10");
        lore.add("§7 쉬프트+우클릭: -10");

        return Reference.iconDefault(Material.LIME_STAINED_GLASS_PANE, "◆", lore, model);
    }
    // 출금 아이콘
    public static ItemStack iconWithdraw() {
        List<String> lore = new ArrayList<>();
        lore.add("§7 우클릭 시§d 출금페이지§7로 이동합니다");

        return Reference.iconDefault(Material.FURNACE, "§d출금", lore);
    }
    // 출금 상호작용 버튼
    public static ItemStack iconInteractWithdraw(int model) {
        List<String> lore = new ArrayList<>();
        lore.add("§7 좌클릭: +1");
        lore.add("§7 우클릭: -1");
        lore.add("§7 쉬프트+좌클릭: +10");
        lore.add("§7 쉬프트+우클릭: -10");

        return Reference.iconDefault(Material.PINK_STAINED_GLASS_PANE, "◆", lore, model);
    }

    // 재화 아이콘
    public static ItemStack iconMoney(UUID uuid) {
        List<String> lore = new ArrayList<>();
        lore.add("§x§F§F§D§7§0§0 금화 : " + Reference.getMoneyGold(uuid, 0));
        lore.add("§x§C§0§C§0§C§0 은화 : " + Reference.getMoneyGold(uuid, 1));
        lore.add("§x§C§D§7§F§3§2 동화 : " + Reference.getMoneyGold(uuid, 2));

        return Reference.iconDefault(Material.EMERALD, "§f현재 소지금액", lore);
    }

    //금화
    public static ItemStack iconGold() {
        List<String> lore = new ArrayList<>();
        lore.add("§a델루나 코인 §f: 델루나 서버 화폐이다");

        return Reference.iconDefault(Material.RED_DYE, "§x§f§f§d§7§0§0델루나 금화", lore, 1000);
    }

    //은화
    public static ItemStack iconSilver() {
        List<String> lore = new ArrayList<>();
        lore.add("§a델루나 코인 §f: 델루나 서버 화폐이다");

        return Reference.iconDefault(Material.BLUE_DYE, "§x§c§b§c§b§c§b델루나 은화", lore, 1000);
    }

    //동화
    public static ItemStack iconCopper() {
        List<String> lore = new ArrayList<>();
        lore.add("§a델루나 코인 §f: 델루나 서버 화폐이다");

        return Reference.iconDefault(Material.GREEN_DYE, "§x§b§f§8§0§6§9델루나 동화", lore, 1000);
    }

    //금화 - 아이콘
    public static ItemStack iconGoldButton() {
        return Reference.iconDefault(Material.RED_DYE, "§x§f§f§d§7§0§0델루나 금화", 1000);
    }

    //은화 - 아이콘
    public static ItemStack iconSilverButton() {
        return Reference.iconDefault(Material.BLUE_DYE, "§x§c§b§c§b§c§b델루나 은화", 1000);
    }

    //동화 - 아이콘
    public static ItemStack iconCopperButton() {
        return Reference.iconDefault(Material.GREEN_DYE, "§x§b§f§8§0§6§9델루나 동화", 1000);
    }

    // 확인 버튼
    public static ItemStack iconAccess() {
        return Reference.iconDefault(Material.NETHER_STAR, "§e확인");
    }
    // 뒤로가기 버튼
    public static ItemStack iconBack() { return Reference.iconDefault(Material.BARRIER, "§c뒤로가기"); }

    // 검정 바탕 아이콘
    public static ItemStack iconNull() {
        return Reference.iconDefault(Material.BLACK_STAINED_GLASS_PANE, " ");
    }
    // 흰색 바탕 아이콘
    public static ItemStack iconNull2() {
        return Reference.iconDefault(Material.WHITE_STAINED_GLASS_PANE, " ");
    }
}

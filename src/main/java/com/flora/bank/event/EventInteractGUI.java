package com.flora.bank.event;

import com.flora.bank.Reference;
import com.flora.bank.gui.InventoryBank;
import com.flora.bank.gui.InventoryDeposit;
import com.flora.bank.gui.InventoryIcon;
import com.flora.bank.gui.InventoryWithdraw;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventInteractGUI implements Listener
{
    // 인벤토리 클릭시 호출
    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // 은행 GUI 인지 확인
        if (isBankInventory(event)) {
            // 이벤트 캔슬 시 모든 상호작용 불가능
            event.setCancelled(true);
            onBankInteractEvent(event, player);
        }
    }

    // 인벤토리 가짜 아이템 복사 방지
    @EventHandler
    private void onFakeItemCancel(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getView().getTitle().contains("§b 델루나 은행"))
            player.getInventory().setContents(player.getInventory().getContents());
    }

    private void onBankInteractEvent(InventoryClickEvent event, Player player) {
        // 하단 인벤토리 상호작용 금지 (플레이어 인벤토리)
        if (event.getClickedInventory() == event.getView().getBottomInventory()) { return; }

        switch (Objects.requireNonNull(event.getCurrentItem()).getType()) {
            //입금 아이콘 클릭 시
            case CHEST:
                player.openInventory(new InventoryDeposit(player).getInventory());
                break;

            // 출금 아이콘 클릭 시
            case FURNACE:
                player.openInventory(new InventoryWithdraw(player).getInventory());
                break;

            // 뒤로가기 아이콘 클릭 시
            case BARRIER:
                player.openInventory(new InventoryBank(player).getInventory());
                break;

            // 입금 상호작용 버튼 클릭 시
            case LIME_STAINED_GLASS_PANE:
                ItemStack g1 = Objects.requireNonNull(event.getClickedInventory()).getItem(event.getSlot() - 9);
                ItemStack e1 = Objects.requireNonNull(event.getClickedInventory()).getItem(49);
                assert g1 != null;
                assert e1 != null;

                onClickedButton(event, g1, e1, event.getSlot(), true);
                break;

            // 출금 상호작용 버튼 클릭 시
            case PINK_STAINED_GLASS_PANE:
                ItemStack g2 = Objects.requireNonNull(event.getClickedInventory()).getItem(event.getSlot() - 9);
                ItemStack e2 = Objects.requireNonNull(event.getClickedInventory()).getItem(49);
                assert g2 != null;
                assert e2 != null;

                onClickedButton(event, g2, e2, event.getSlot(), false);
                break;

            // 확인 버튼 클릭 시
            case NETHER_STAR:
                onBackProcess(player, event.getCurrentItem());
                break;
        }
    }

    private void onClickedButton(InventoryClickEvent event, ItemStack stack, ItemStack endPoint, int slot, boolean isDW) {
        ItemMeta meta1 = stack.getItemMeta();
        assert meta1 != null;
        List<String> stackLore = meta1.getLore();

        ItemMeta meta2 = endPoint.getItemMeta();
        assert meta2 != null;
        List<String> endLore = meta2.getLore();


        switch (event.getClick()) {
            case LEFT:
                onClickButton(event, stack, meta1, stackLore, slot,1, isDW);
                break;

            case RIGHT:
                onClickButton(event, stack, meta1, stackLore, slot,2, isDW);
                break;

            case SHIFT_LEFT:
                onClickButton(event, stack, meta1, stackLore, slot,3, isDW);
                break;

            case SHIFT_RIGHT:
                onClickButton(event, stack, meta1, stackLore, slot,4, isDW);
                break;
        }

        setAccessIconData(event, endPoint, meta2, endLore, isDW);
    }

    // 돈 상호작용 이벤트
    private void onClickButton(InventoryClickEvent event, ItemStack item, ItemMeta meta, List<String> lore, int slot, int calMod, boolean isDW) {
        Player player = (Player) event.getWhoClicked();

        // 입금 상호작용
        if (isDW) {
            int hasGold = 0;
            int hasSilver = 0;
            int hasCopper = 0;

            // 소지 재화 확인
            for (ItemStack i : player.getInventory().getStorageContents()) {
                if (i != null) {
                    if (Objects.equals(i.getItemMeta(), InventoryIcon.iconGold().clone().getItemMeta()))
                        hasGold += i.getAmount();
                    if (Objects.equals(i.getItemMeta(), InventoryIcon.iconSilver().clone().getItemMeta()))
                        hasSilver += i.getAmount();
                    if (Objects.equals(i.getItemMeta(), InventoryIcon.iconCopper().clone().getItemMeta()))
                        hasCopper += i.getAmount();
                }
            }

            // 금화
            if (slot == 20)
                setMoneyIconData(event, item, meta, lore, slot, calMod, hasGold);
            // 은화
            else if (slot == 22)
                setMoneyIconData(event, item, meta, lore, slot, calMod, hasSilver);
            // 동화
            else if (slot == 24)
                setMoneyIconData(event, item, meta, lore, slot, calMod, hasCopper);
        }


        // 출금 상호작용
        else {
            int bankGold = Reference.getMoneyGold(player.getUniqueId(), 0);
            int bankSilver = Reference.getMoneyGold(player.getUniqueId(), 1);
            int bankCopper = Reference.getMoneyGold(player.getUniqueId(), 2);

            if (bankGold > 64)
                bankGold = 64;

            if (bankSilver > 64)
                bankSilver = 64;

            if (bankCopper > 64)
                bankCopper = 64;

            // 금화
            if (slot == 20)
                setMoneyIconData(event, item, meta, lore, slot, calMod, bankGold);
                // 은화
            else if (slot == 22)
                setMoneyIconData(event, item, meta, lore, slot, calMod, bankSilver);
                // 동화
            else if (slot == 24)
                setMoneyIconData(event, item, meta, lore, slot, calMod, bankCopper);
        }
    }

    // 돈 아이콘 Lore 데이터 변경
    private void setMoneyIconData(InventoryClickEvent event, ItemStack item, ItemMeta meta, List<String> lore, int buttonSlot, int calMod, int max) {
        // 아이콘 Lore 변경
        if (lore != null) {
            int count1 = Reference.getIntCalculation(calMod, Integer.parseInt(lore.get(0).substring(2).trim()), max);

            lore.set(0, "§f " + count1 + " ");
            meta.setLore(lore);
        } else {
            List<String> l1 = new ArrayList<>();
            l1.add("§f " + Reference.getIntCalculation(calMod, 0, max) + " ");

            meta.setLore(l1);
        }

        item.setItemMeta(meta);
        Objects.requireNonNull(event.getClickedInventory()).setItem(buttonSlot - 9, item);
    }

    // 확인 아이콘 Lore 데이터 변경
    private void setAccessIconData(InventoryClickEvent event, ItemStack item, ItemMeta meta, List<String> lore, boolean isDW) {
        // 확인 Lore 변경
        // 현재 입력한 금액 구하기
        List<Integer> bb = new ArrayList<>();

        for (int i = 20; i < 25; i += 2) {
            List<String> lo = Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getClickedInventory()).getItem(i - 9)).getItemMeta()).getLore();

            if (lo == null)
                bb.add(0);
            else
                bb.add(Integer.parseInt(lo.get(0).substring(2).trim()));
        }

        int gold = bb.get(0);
        int silver = bb.get(1);
        int copper = bb.get(2);
        
        if (lore != null) {
            lore.set(0, "§x§F§F§D§7§0§0금화: " + gold + "  §x§C§0§C§0§C§0은화: " + silver + "  §x§C§D§7§F§3§2동화: " + copper);

            meta.setLore(lore);

        } else {
            List<String> l2 = new ArrayList<>();
            l2.add("§x§F§F§D§7§0§0금화: " + gold + "  §x§C§0§C§0§C§0은화: " + silver + "  §x§C§D§7§F§3§2동화: " + copper);

            if (isDW)
                l2.add("§e 입금하시겠습니까? ");
            else
                l2.add("§e 출금하시겠습니까? ");

            meta.setLore(l2);
        }

        item.setItemMeta(meta);
        Objects.requireNonNull(event.getClickedInventory()).setItem(49, item);
    }

    // Lore 돈 데이터 (정규표현식)
    private int getLoreMoney(String moneyLore, int mod) {
        Pattern pattern = Pattern.compile(".{17}([0-9]*).{17}([0-9]*).{17}([0-9]*)");
        Matcher matcher = pattern.matcher(moneyLore.replaceAll(" ", ""));
        if (!(matcher.find())) return 0;

        switch (mod) {
            case 0:
                return Integer.parseInt(matcher.group(1));

            case 1:
                return Integer.parseInt(matcher.group(2));

            case 2:
                return Integer.parseInt(matcher.group(3));
        }

        return 0;
    }


    private void onBackProcess(Player player, ItemStack endPoint) {
        UUID uuid = player.getUniqueId();
        List<String> lore = Objects.requireNonNull(endPoint.getItemMeta()).getLore();
        if (lore != null) {
            int has_gold = Reference.getMoneyGold(uuid, 0);
            int has_silver = Reference.getMoneyGold(uuid, 1);
            int has_copper = Reference.getMoneyGold(uuid, 2);

            int result_gold = getLoreMoney(lore.get(0), 0);
            int result_silver = getLoreMoney(lore.get(0), 1);
            int result_copper = getLoreMoney(lore.get(0), 2);

            if (result_gold + result_silver + result_copper == 0) { return; }

            // 입금 시스템
            if (lore.get(1).contains("입금"))
            {
                int G1 = 0, S1 = 0, C1 = 0;

                for (ItemStack item : player.getInventory().getStorageContents()) {
                    if (item != null) {
                        if (Objects.equals(item.getItemMeta(), InventoryIcon.iconGold().clone().getItemMeta())) {
                            G1 += item.getAmount();
                            item.setAmount(0);
                        } else if (Objects.equals(item.getItemMeta(), InventoryIcon.iconSilver().clone().getItemMeta())) {
                            S1 += item.getAmount();
                            item.setAmount(0);
                        } else if (item.getItemMeta().equals(InventoryIcon.iconCopper().clone().getItemMeta())) {
                            C1 += item.getAmount();
                            item.setAmount(0);
                        }
                    }
                }

                ItemStack c1 = InventoryIcon.iconGold().clone();
                ItemStack c2 = InventoryIcon.iconSilver().clone();
                ItemStack c3 = InventoryIcon.iconCopper().clone();

                c1.setAmount(G1 - result_gold);
                c2.setAmount(S1 - result_silver);
                c3.setAmount(C1 - result_copper);

                player.getInventory().addItem(c1);
                player.getInventory().addItem(c2);
                player.getInventory().addItem(c3);

                saveData(player, has_gold + result_gold, has_silver + result_silver, has_copper + result_copper);
                player.closeInventory();
            }
            // 출금 시스템
            else {
                if (isInventoryFull(player)) {
                    player.sendMessage("§c 인벤토리를 3칸 이상 비워주세요");
                }
                else {
                    ItemStack c1 = InventoryIcon.iconGold().clone();
                    ItemStack c2 = InventoryIcon.iconSilver().clone();
                    ItemStack c3 = InventoryIcon.iconCopper().clone();

                    c1.setAmount(result_gold);
                    c2.setAmount(result_silver);
                    c3.setAmount(result_copper);

                    player.getInventory().addItem(c1);
                    player.getInventory().addItem(c2);
                    player.getInventory().addItem(c3);

                    saveData(player, has_gold - result_gold, has_silver - result_silver, has_copper - result_copper);
                    player.closeInventory();
                }
            }
        }
    }

    // 입출금 사용 후 저장
    private void saveData(Player player, int gold, int silver, int copper) {
        Reference.playerBank.put(player.getUniqueId(), "G" + gold + "S" + silver + "C" + copper);

        FileConfiguration config = Reference.getDataConfig(player.getUniqueId().toString());
        config.set("gold", gold);
        config.set("silver", silver);
        config.set("copper", copper);
        Reference.saveDataFile(config, Reference.getDataFile(player.getUniqueId().toString()));
    }

    // 인벤토리가 은행 GUI 인지 아닌지 구분
    // 맞으면 true 틀리면 false
    private boolean isBankInventory(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return false;
        if (event.getCurrentItem() == null) return false;

        return event.getView().getTitle().contains("§b 델루나 은행");
    }

    // 인벤토리가 비어있는지 확인
    private boolean isInventoryFull(Player player) {
        int stack = 0;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item != null) { stack++; }
        }

        return stack > (player.getInventory().getStorageContents().length - 2);
    }
}

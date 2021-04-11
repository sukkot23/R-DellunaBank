package com.flora.bank;

import com.flora.bank.event.EventInteractGUI;
import com.flora.bank.event.EventInteractNPC;
import com.flora.bank.event.EventPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class Main extends JavaPlugin
{
    // 플러그인이 시작될 때
    @Override
    public void onEnable() {
        //플러그인이 정상적으로 실행되었는지 확인하는 로그 출력
        Reference.LOG.info("플러그인이 실행되었습니다");

        // 이벤트 등록 소스
        // registerEvents(Listener를 implements 해준 클래스, 메인 클래스)
        Bukkit.getServer().getPluginManager().registerEvents(new EventPlayerData(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventInteractGUI(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new EventInteractNPC(), this);

        // 모든 플레이어 은행 데이터 및 플레이어 리스트 가져오기
        for (File file : Reference.getDataFiles()) {
            String uuidString = file.getName().substring(0, file.getName().length()-4);
            Reference.setPlayerBankData(UUID.fromString(uuidString));
            Reference.playerList.add(Reference.getDataConfig(uuidString).getString("name"));
        }
    }

    // 플러그인이 종료될 때
    @Override
    public void onDisable() {
        //플러그인이 종료되었는지 확인하는 로그 출력
        Reference.LOG.info("플러그인이 종료되었습니다");
    }

    // 커맨드 이벤트
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(command.getName().equalsIgnoreCase("bank"))) return true;
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (player.isOp()) {
            if (args.length == 1) {
                switch (args[0]) {
                    case "rank":
                    case "랭킹":
                        onDisplayMoneyRank(player);
                        return true;

                    default:
                        return onDisplayPlayerMoney(player, args[0]);
                }
            }
        }

        // return true  : 해당 커맨드의 usage 텍스트를 반환하지 않음
        // return false : 해당 커맨드의 usage 텍스트를 반환
        return true;
    }

    // 자동완성 이벤트
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(command.getName().equalsIgnoreCase("bank"))) return null;
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;

        List<String> tabList = Reference.playerList;
        tabList.add("rank");

        if (args.length < 2)
            return Reference.tabCompleteSort(tabList, args[0]);
        else
            return new ArrayList<>();
    }

    // 랭킹 소스
    private void onDisplayMoneyRank(Player player) {
        Map<UUID, Integer> rankMap = new HashMap<>();

        for (UUID uuid : Reference.playerBank.keySet()) {
            rankMap.put(uuid, (Reference.getMoneyGold(uuid, 1) * 3) + Reference.getMoneyGold(uuid, 2));
        }

        List<UUID> rankList = new ArrayList<>(rankMap.keySet());
        rankList.sort((o1, o2) -> rankMap.get(o2).compareTo(rankMap.get(o1)));

        player.sendMessage("§f============ §6델루나 재화랭킹 §f==========");

        if (rankList.size() < 10) {
            for (int i = 0; i < rankList.size(); i++)
                onSendMessageRank(player, rankList, i);
        }
        else {
            for (int i = 0; i < 10; i++)
                onSendMessageRank(player, rankList, i);
        }
    }

    // 랭킹 메세지 보내기
    private void onSendMessageRank(Player player, List<UUID> rank, int i) {
        UUID uuid = rank.get(i);
        String name = Reference.getDataConfig(uuid.toString()).getString("name");
        String money = Reference.playerBank.get(rank.get(i));

        player.sendMessage((i + 1) + ". §c" + name
                + " §f은화: §x§C§0§C§0§C§0" + Reference.getMoneyGold(uuid, 1)
                + " §f동화: §x§C§D§7§F§3§2" + Reference.getMoneyGold(uuid, 2)
        );
    }

    // 재화 보유 상황 보는 소스
    private boolean onDisplayPlayerMoney(Player player, String arg) {
        if (Reference.playerList.contains(arg)) {
            FileConfiguration config = Reference.getDataConfigToName(arg);

            player.sendMessage("§c " + arg + "§f님은 현재 ["
                    + "§x§F§F§D§7§0§0 금화: " + config.getInt("gold")
                    + "§x§C§0§C§0§C§0 은화: " + config.getInt("silver")
                    + "§x§C§D§7§F§3§2 동화: " + config.getInt("copper")
            + "§f ] 을(를) 보유하고있습니다.");

            return true;
        }
        else {
            return false;
        }
    }
}

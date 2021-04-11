package com.flora.bank;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reference
{
    // 플러그인 변수 선언
    public static final Plugin PLUGIN = JavaPlugin.getPlugin(Main.class);
    // 로그 변수 선언
    public static Logger LOG = Logger.getLogger("Minecraft");

    // 플레이어 조회를 위한 해쉬맵 생성
    public static List<String> playerList = new ArrayList<>();

    /* Player, G(Int)S(Int)C(Int)*/
    // 플레이어 재화 관련 데이터를 가진 "해쉬맵" 생성
    public static Map<UUID, String> playerBank = new HashMap<>();

    // 해쉬맵 데이터 조회
    public static String getPlayerBank(UUID uuid) {
        return playerBank.get(uuid);
    }

    // 플레이어 재화 수량 조회 (정규표현식 사용)
    // mod 숫자에 따라 금화,은화,동화 수량 반환
    // 0: 금화, 1: 은화, 2: 동화
    public static int getMoneyGold(UUID uuid, int mod) {
        try {
            Pattern pattern = Pattern.compile("G([0-9]*)S([0-9]*)C([0-9]*)");
            Matcher matcher = pattern.matcher(getPlayerBank(uuid));
            if (!(matcher.find())) return 0;

            switch (mod) {
                case 0:
                    return Integer.parseInt(matcher.group(1));

                case 1:
                    return Integer.parseInt(matcher.group(2));

                case 2:
                    return Integer.parseInt(matcher.group(3));
            }
        }
        catch (NullPointerException e) {
            Reference.LOG.info("§c[Bank] 플레이어 해쉬데이터를 불러오지 못했습니다");
            e.printStackTrace();
        }
        catch (NumberFormatException e) {
            Reference.LOG.info("§c[Bank] 플레이어 재화데이터에 오류가 발생하였습니다");
            e.printStackTrace();
        }

        return 0;
    }

    // 은행데이터를 해쉬맵에 등록
    public static void setPlayerBankData(UUID uuid) {
        FileConfiguration config = Reference.getDataConfig(uuid.toString());
        int gold = config.getInt("gold");
        int silver = config.getInt("silver");
        int copper = config.getInt("copper");

        Reference.playerBank.put(uuid, "G" + gold + "S" + silver + "C" + copper);
    }



    /* Player Data File */
    // 플레이어 데이터 파일 불러오기
    public static File getDataFile(String uuid) {
        return new File(PLUGIN.getDataFolder() + "\\playerdata", uuid + ".dat");
    }

    // 플레이어 데이터 폴더 내 모든 데이터 불러오기
    public static File[] getDataFiles() {
        return new File(PLUGIN.getDataFolder() + "\\playerdata").listFiles();
    }

    // 플레이어 데이터 파일을 콘피그 파일로 불러오기
    public static FileConfiguration getDataConfig(String uuid) {
        return YamlConfiguration.loadConfiguration(getDataFile(uuid));
    }

    /* Save Data File */
    // 플레이어 데이터 저장
    public static void saveDataFile(FileConfiguration config, File file) {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("§cFile I/O Error!!");
        }
    }

    /* Get Player Data File to PlayerName */
    // 플레이어 데이터를 PlayerName으로 불러오기
    public static File getDataFileToName(String playerName) throws NullPointerException {
        for (File file : getDataFiles()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            if (Objects.requireNonNull(config.getString("name")).equalsIgnoreCase(playerName))
                return file;
        }
        return null;
    }

    /* Get Player Data Config to PlayerName */
    // 플레이어 데이터 콘피그를 PlayerName으로 불러오기
    public static FileConfiguration getDataConfigToName(String playerName) throws NullPointerException {
        return YamlConfiguration.loadConfiguration(Objects.requireNonNull(getDataFileToName(playerName)));
    }





    /* 간편 GUI 아이콘 등록소스 */
    // 아이템만 반환
    public static ItemStack iconDefault(Material material) {
        return new ItemStack(material);
    }
    // "이름"이(가) 부여된 아이템 반환
    public static ItemStack iconDefault(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);

        item.setItemMeta(meta);
        return item;
    }
    // "이름", "부가설명"이(가) 부여된 아이템 반환
    public static ItemStack iconDefault(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }
    // "이름", "커스텀모델데이터"이(가) 부여된 아이템 반환
    public static ItemStack iconDefault(Material material, String displayName, int customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);
        return item;
    }
    // "이름", "부가설명", "커스텀모델데이터"이(가) 부여된 아이템 반환
    public static ItemStack iconDefault(Material material, String displayName, List<String> lore, int customModelData) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setCustomModelData(customModelData);

        item.setItemMeta(meta);
        return item;
    }

    /*
     * 1 : value +1
     * 2 : value +10
     * 3 : value -1
     * 4 : value -10
     */
    public static int getIntCalculation(int mod, int value, int max) {
        int result = 0;

        switch (mod) {
            case 1:
                result = value + 1;
                break;

            case 2:
                result = value - 1;
                break;

            case 3:
                result = value + 10;
                break;

            case 4:
                result = value - 10;
                break;
        }

        if (result < 0) result = 0;
        if (result > max) result = max;

        return result;
    }

    public static List<String> tabCompleteSort(List<String> list, String args) {
        List<String> sortList = new ArrayList<>();
        for (String s : list) {
            if (args.isEmpty()) return list;

            if (s.toLowerCase().startsWith(args.toLowerCase()))
                sortList.add(s);
        }
        return sortList;
    }
}

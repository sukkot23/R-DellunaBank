package com.flora.bank.event;

import com.flora.bank.Reference;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventPlayerData implements Listener
{
    // 플레이어가 서버에 접속할 때 호출
    @EventHandler
    private void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();

        // 은행데이터에 플레이어 정보가 있는지 없는지 확인
        if (!(Reference.getDataFile(uuid).canRead()))
            onCreateNewData(player, uuid);

        setReloadPlayerName(player, uuid);
//        setPlayerBankData(player, uuid);
    }

    // 서버 리로드 시 데이터 재등록을 위한 이벤트
    /*
    @EventHandler
    private void onPlayerDataReloadEvent(ServerLoadEvent event) {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> setPlayerBankData(player, player.getUniqueId().toString()));
    }
     */

    // 닉네임 변경 후 데이터 꼬임방지를 위한 소스
    private void setReloadPlayerName(Player player, String uuid)
    {
        FileConfiguration config = Reference.getDataConfig(uuid);
        config.set("name", player.getName());
        Reference.saveDataFile(config, Reference.getDataFile(uuid));
    }

    // 새로운 플레이어 은행 데이터 생성
    private void onCreateNewData(Player player, String uuid) {
        FileConfiguration config = Reference.getDataConfig(uuid);

        config.set("name", player.getName());
        config.set("uuid", uuid);
        config.set("gold", 0);
        config.set("silver", 0);
        config.set("copper", 0);

        Reference.saveDataFile(config, Reference.getDataFile(uuid));

        Reference.setPlayerBankData(player.getUniqueId());
    }
}

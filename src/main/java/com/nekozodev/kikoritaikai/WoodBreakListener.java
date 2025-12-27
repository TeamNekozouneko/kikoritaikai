package com.nekozodev.kikoritaikai;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class WoodBreakListener implements Listener {

    private final KikoriTaikaiPlugin plugin;
    private final DataManager dataManager;
    private final RankingManager rankingManager;
    private final Messages messages;

    public WoodBreakListener(KikoriTaikaiPlugin plugin, DataManager dataManager, RankingManager rankingManager, Messages messages) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.rankingManager = rankingManager;
        this.messages = messages;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.isGameRunning()) {
            return;
        }

        Player player = event.getPlayer();
        Material blockType = event.getBlock().getType();

        // 対象の原木かチェック
        if (plugin.getLogBlocks().contains(blockType.name())) {
            // 樹皮を剥いだものは対象外（STRIPPED_で始まるものは除外）
            if (!blockType.name().startsWith("STRIPPED_")) {
                // 自分で置いたブロックはカウントしない
                Location loc = event.getBlock().getLocation();
                if (dataManager.getPlacedLogs().containsKey(loc) && dataManager.getPlacedLogs().get(loc).equals(player.getUniqueId())) {
                    // カウントしない
                    return;
                }
                // スコアを追加
                dataManager.addScore(player.getUniqueId(), player.getName(), 1);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material blockType = event.getBlock().getType();
        // 対象の原木なら記録
        if (plugin.getLogBlocks().contains(blockType.name())) {
            Location loc = event.getBlock().getLocation();
            dataManager.getPlacedLogs().put(loc, event.getPlayer().getUniqueId());
        }
    }
}
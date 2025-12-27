package com.nekozodev.kikoritaikai;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerJoinListener implements Listener {

    private final KikoriTaikaiPlugin plugin;
    private final DataManager dataManager;
    private final RankingManager rankingManager;
    private final Messages messages;

    public PlayerJoinListener(KikoriTaikaiPlugin plugin, DataManager dataManager, RankingManager rankingManager, Messages messages) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.rankingManager = rankingManager;
        this.messages = messages;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // 毎回参加ごとにアイテムを与える
        ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 2);
        player.getInventory().addItem(axe);

        // 骨粉を64個与える
        ItemStack boneMeal = new ItemStack(Material.BONE_MEAL, 64);
        player.getInventory().addItem(boneMeal);

        // オークの苗木を64個与える
        ItemStack oakSapling = new ItemStack(Material.OAK_SAPLING, 64);
        player.getInventory().addItem(oakSapling);
    }
}
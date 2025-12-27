package com.nekozodev.kikoritaikai;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.List;

public class KikoriTaikaiPlugin extends JavaPlugin {

    private boolean isGameRunning = false;
    private DataManager dataManager;
    private RankingManager rankingManager;
    private Messages messages;
    private BukkitTask updateTask;

    @Override
    public void onEnable() {
        // データマネージャーの初期化
        dataManager = new DataManager(this);
        dataManager.loadData();

        // Initialize messages
        messages = new Messages(this);

        // ランキングマネージャーの初期化
        rankingManager = new RankingManager(this, dataManager, messages);

        // コンフィグのロード
        saveDefaultConfig();
        reloadConfig();

        // イベントリスナーの登録
        getServer().getPluginManager().registerEvents(new WoodBreakListener(this, dataManager, rankingManager, messages), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, dataManager, rankingManager, messages), this);

        // コマンドの登録
        KikoriCommands commands = new KikoriCommands(this, dataManager, rankingManager, messages);
        getCommand("kstart").setExecutor(commands);
        getCommand("kstop").setExecutor(commands);
        getCommand("kclear").setExecutor(commands);
        getCommand("kreload").setExecutor(commands);
        getCommand("kconfirmclear").setExecutor(commands);
        getCommand("kcancelclear").setExecutor(commands);

        // ゲーム状態の復元
        if (dataManager.isGameRunning()) {
            startGame();
        }

        // アクションバー更新タスクを開始（常に）
        startUpdateTask();

        getLogger().info("KikoriTaikaiPlugin enabled!");
    }

    @Override
    public void onDisable() {
        // データの保存
        dataManager.saveData();

        // タスクのキャンセル
        if (updateTask != null) {
            updateTask.cancel();
        }

        getLogger().info("KikoriTaikaiPlugin disabled!");
    }

    public void startGame() {
        isGameRunning = true;
        dataManager.setGameRunning(true);

        // 全プレイヤーにダイヤモンドの斧を2個与える
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            org.bukkit.inventory.ItemStack axe = new org.bukkit.inventory.ItemStack(org.bukkit.Material.DIAMOND_AXE, 2);
            player.getInventory().addItem(axe);
        }

        // スコアボードを表示
        rankingManager.updateScoreboard();

        getLogger().info("Game started!");
    }

    public void stopGame() {
        isGameRunning = false;
        dataManager.setGameRunning(false);

        // スコアボードのクリア
        rankingManager.clearScoreboard();

        getLogger().info("Game stopped!");
    }

    private void startUpdateTask() {
        updateTask = Bukkit.getScheduler().runTaskTimer(this, () -> {
            rankingManager.updateActionBar();
            rankingManager.updateTabList();
            rankingManager.updateScoreboard();
        }, 0L, 60L); // 3秒 = 60 ticks
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public List<String> getLogBlocks() {
        return getConfig().getStringList("logs");
    }
}
package com.nekozodev.kikoritaikai;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {

    private final JavaPlugin plugin;
    private final File dataFile;
    private YamlConfiguration dataConfig;
    private Map<UUID, Integer> scores = new HashMap<>();
    private Map<UUID, String> playerNames = new HashMap<>();
    private Map<Location, UUID> placedLogs = new HashMap<>();
    private boolean gameRunning = false;

    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            try {
                dataFile.getParentFile().mkdirs();
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void loadData() {
        if (dataConfig.contains("scores")) {
            for (String key : dataConfig.getConfigurationSection("scores").getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                int score = dataConfig.getInt("scores." + key);
                scores.put(uuid, score);
            }
        }
        if (dataConfig.contains("names")) {
            for (String key : dataConfig.getConfigurationSection("names").getKeys(false)) {
                UUID uuid = UUID.fromString(key);
                String name = dataConfig.getString("names." + key);
                playerNames.put(uuid, name);
            }
        }
        gameRunning = dataConfig.getBoolean("gameRunning", false);
    }

    public void saveData() {
        for (Map.Entry<UUID, Integer> entry : scores.entrySet()) {
            dataConfig.set("scores." + entry.getKey().toString(), entry.getValue());
        }
        for (Map.Entry<UUID, String> entry : playerNames.entrySet()) {
            dataConfig.set("names." + entry.getKey().toString(), entry.getValue());
        }
        dataConfig.set("gameRunning", gameRunning);
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, Integer> getScores() {
        return scores;
    }

    public Map<UUID, String> getPlayerNames() {
        return playerNames;
    }

    public void addScore(UUID uuid, String name, int amount) {
        scores.put(uuid, scores.getOrDefault(uuid, 0) + amount);
        playerNames.put(uuid, name);
    }

    public void clearScores() {
        scores.clear();
        playerNames.clear();
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public Map<Location, UUID> getPlacedLogs() {
        return placedLogs;
    }
}
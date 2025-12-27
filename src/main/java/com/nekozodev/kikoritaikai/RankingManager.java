package com.nekozodev.kikoritaikai;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.stream.Collectors;

public class RankingManager {

    private final JavaPlugin plugin;
    private final DataManager dataManager;
    private final Messages messages;
    private Scoreboard scoreboard;
    private Objective objective;

    public RankingManager(JavaPlugin plugin, DataManager dataManager, Messages messages) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.messages = messages;
        setupScoreboard();
    }

    private void setupScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("kikoritaikai", Criteria.DUMMY, Component.text(messages.get("scoreboard_title"), NamedTextColor.GOLD).decorate(TextDecoration.BOLD));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void updateScoreboard() {
        // スコアのリセット
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        // トップ10を取得
        List<Map.Entry<UUID, Integer>> top10 = dataManager.getScores().entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : top10) {
            String name = dataManager.getPlayerNames().get(entry.getKey());
            NamedTextColor color = (rank == 1) ? NamedTextColor.YELLOW : (rank <= 3) ? NamedTextColor.GOLD : NamedTextColor.GREEN;
            String display = "§" + (rank == 1 ? "e" : rank <= 3 ? "6" : "a") + rank + ". " + name + ": " + entry.getValue();
            Score score = objective.getScore(display);
            score.setScore(11 - rank); // 上から表示
            rank++;
        }

        // 全プレイヤーにスコアボードを設定
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }

    public void updateActionBar() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateActionBarForPlayer(player);
        }
    }

    private void updateActionBarForPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Map<UUID, Integer> scores = dataManager.getScores();
        if (!scores.containsKey(uuid)) {
            player.sendActionBar(Component.text(messages.get("actionbar_not_participating"), NamedTextColor.GRAY));
            return;
        }

        int playerScore = scores.get(uuid);
        List<Map.Entry<UUID, Integer>> sorted = scores.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : sorted) {
            if (entry.getKey().equals(uuid)) {
                break;
            }
            rank++;
        }

        String rankStr = messages.get("actionbar_rank", "rank", String.valueOf(rank), "score", String.valueOf(playerScore));
        if (rank <= 10) {
            player.sendActionBar(Component.text(rankStr, NamedTextColor.GREEN));
        } else {
            player.sendActionBar(Component.text(rankStr, NamedTextColor.YELLOW));
        }
    }

    public void updateTabList() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTabListForPlayer(player);
        }
    }

    private void updateTabListForPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Map<UUID, Integer> scores = dataManager.getScores();
        if (!scores.containsKey(uuid)) {
            player.playerListName(Component.text(messages.get("player_list_not_participating", "name", player.getName()), NamedTextColor.GRAY));
            return;
        }

        int playerScore = scores.get(uuid);
        List<Map.Entry<UUID, Integer>> sorted = scores.entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : sorted) {
            if (entry.getKey().equals(uuid)) {
                break;
            }
            rank++;
        }

        String listName = messages.get("player_list_rank_format", "rank", String.valueOf(rank), "name", player.getName());
        if (rank <= 10) {
            player.playerListName(Component.text(listName, NamedTextColor.GREEN));
        } else {
            player.playerListName(Component.text(listName, NamedTextColor.YELLOW));
        }
    }

    public void clearScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }
    }

    public String getRankingString() {
        StringBuilder sb = new StringBuilder(messages.get("results_header") + "\n");
        List<Map.Entry<UUID, Integer>> sorted = dataManager.getScores().entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : sorted) {
            String name = dataManager.getPlayerNames().get(entry.getKey());
            sb.append(rank).append(". ").append(name).append(": ").append(entry.getValue()).append("\n");
            rank++;
        }
        return sb.toString();
    }
}
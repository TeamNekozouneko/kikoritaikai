package com.nekozodev.kikoritaikai;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KikoriCommands implements CommandExecutor {

    private final KikoriTaikaiPlugin plugin;
    private final DataManager dataManager;
    private final RankingManager rankingManager;
    private final Messages messages;

    public KikoriCommands(KikoriTaikaiPlugin plugin, DataManager dataManager, RankingManager rankingManager, Messages messages) {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.rankingManager = rankingManager;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.get("not_a_player"));
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("kikoritaikai.op")) {
            player.sendMessage(Component.text(messages.get("no_permission"), NamedTextColor.RED));
            return true;
        }

        switch (label.toLowerCase()) {
            case "kstart":
                if (plugin.isGameRunning()) {
                    player.sendMessage(Component.text(messages.get("game_already_running"), NamedTextColor.YELLOW));
                } else {
                    plugin.startGame();
                    player.sendMessage(Component.text(messages.get("game_started"), NamedTextColor.GREEN));
                }
                break;
            case "kstop":
                if (!plugin.isGameRunning()) {
                    player.sendMessage(Component.text(messages.get("game_not_running"), NamedTextColor.YELLOW));
                } else {
                    plugin.stopGame();
                    String ranking = rankingManager.getRankingString();
                    player.sendMessage(Component.text(ranking));
                }
                break;
            case "kclear":
                if (plugin.isGameRunning()) {
                    player.sendMessage(Component.text(messages.get("cannot_clear_while_running"), NamedTextColor.RED));
                    break;
                }
                // confirmation message
                Component confirmMessage = Component.text(messages.get("confirm_clear_prefix"), NamedTextColor.RED)
                        .append(Component.text(messages.get("confirm_clear_delete_label"), NamedTextColor.DARK_RED)
                                .clickEvent(ClickEvent.runCommand("/kconfirmclear"))
                                .hoverEvent(HoverEvent.showText(Component.text(messages.get("confirm_clear_delete_hover")))))
                        .append(Component.text(messages.get("confirm_clear_cancel_label"), NamedTextColor.GREEN)
                                .clickEvent(ClickEvent.runCommand("/kcancelclear"))
                                .hoverEvent(HoverEvent.showText(Component.text(messages.get("confirm_clear_cancel_hover")))));
                player.sendMessage(confirmMessage);
                break;
            case "kreload":
                plugin.reloadConfig();
                messages.reload();
                player.sendMessage(Component.text(messages.get("config_reloaded"), NamedTextColor.GREEN));
                break;
            case "kconfirmclear":
                dataManager.clearScores();
                dataManager.saveData();
                player.sendMessage(Component.text(messages.get("ranking_cleared"), NamedTextColor.GREEN));
                break;
            case "kcancelclear":
                player.sendMessage(Component.text(messages.get("cancel_cleared"), NamedTextColor.YELLOW));
                break;
        }
        return true;
    }
}
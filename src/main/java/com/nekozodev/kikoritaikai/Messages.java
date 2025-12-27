package com.nekozodev.kikoritaikai;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Messages {

    private final JavaPlugin plugin;
    private final File messagesFile;
    private YamlConfiguration messagesConfig;

    public Messages(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        // Ensure default locale files are present in plugin data folder so admins can edit them
        String[] defaultFiles = new String[] {"messages.yml", "messages_ja.yml", "messages_zh.yml", "messages_ru.yml", "messages_fr.yml"};
        for (String f : defaultFiles) {
            File out = new File(plugin.getDataFolder(), f);
            if (!out.exists()) {
                plugin.saveResource(f, false);
            }
        }
        this.messagesConfig = YamlConfiguration.loadConfiguration(this.messagesFile);
    }

    public String get(String key) {
        return messagesConfig.getString(key, "" + key);
    }

    public String get(String key, Object... args) {
        String s = get(key);
        for (int i = 0; i + 1 < args.length; i += 2) {
            String placeholder = String.valueOf(args[i]);
            String value = String.valueOf(args[i + 1]);
            s = s.replace("%" + placeholder + "%", value);
        }
        return s;
    }

    public void reload() {
        this.messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }
}

package me.vemacs.bungee;

import lombok.Getter;
import me.vemacs.bungee.command.ScCommand;
import me.vemacs.bungee.command.TeleportCommand;
import me.vemacs.bungee.command.TidbitCommand;
import me.vemacs.bungee.misc.ConfigUtils;
import me.vemacs.bungee.misc.providers.BungeeCordProvider;
import me.vemacs.bungee.misc.providers.RedisBungeeProvider;
import me.vemacs.bungee.misc.providers.ServerInfoProvider;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCommands extends Plugin {
    @Getter
    private static SimpleCommands plugin;
    @Getter
    private static ServerInfoProvider provider;
    @Getter
    private static Map<String, List<String>> blacklist;

    private static Set<Command> commands = new HashSet<>();
    private static Map<String, String> tidbits;
    private static Map<String, String> teleports;

    @Override
    public void onEnable() {
        plugin = this;
        if (getProxy().getPluginManager().getPlugin("RedisBungee") != null)
            provider = new RedisBungeeProvider();
        else
            provider = new BungeeCordProvider();
        getLogger().info("Using " + provider.getClass().getSimpleName() + " as provider.");
        getProxy().getPluginManager().registerCommand(this, new ScCommand("sc"));
        load();
    }

    public static void load() {
        loadConfigs();
        registerCommands();
    }

    private static void loadConfigs() {
        tidbits = new ConcurrentHashMap<>(8, 0.75f, 1);
        for (Map.Entry<?, ?> entry : ConfigUtils.loadMapFrom(ConfigUtils.loadResource(getPlugin(), "tidbits.yml")).entrySet()) {
            tidbits.put((String) entry.getKey(), (String) entry.getValue());
        }
        teleports = new ConcurrentHashMap<>(8, 0.75f, 1);
        for (Map.Entry<?, ?> entry : ConfigUtils.loadMapFrom(ConfigUtils.loadResource(getPlugin(), "teleports.yml")).entrySet()) {
            teleports.put((String) entry.getKey(), (String) entry.getValue());
        }
        blacklist = new ConcurrentHashMap<>(8, 0.75f, 1);
        for (Map.Entry<?, ?> entry : ConfigUtils.loadMapFrom(ConfigUtils.loadResource(getPlugin(), "blacklist.yml")).entrySet()) {
            blacklist.put((String) entry.getKey(), lowercase((List<String>) entry.getValue()));
        }
    }

    private static void registerCommands() {
        for (Command cmd : commands)
            getPlugin().getProxy().getPluginManager().unregisterCommand(cmd);
        commands.clear();
        for (Map.Entry<String, String> tidbit : tidbits.entrySet()) {
            String[] names = tidbit.getKey().split("\\|");
            for (String tidbitCommand : names)
                commands.add(new TidbitCommand(tidbitCommand, tidbit.getValue()));
        }
        for (Map.Entry<String, String> teleport : teleports.entrySet()) {
            String[] names = teleport.getKey().split("\\|");
            for (String teleportCommand : names)
                commands.add(new TeleportCommand(teleportCommand, teleport.getValue()));
        }
        for (Command cmd : commands)
            getPlugin().getProxy().getPluginManager().registerCommand(getPlugin(), cmd);
    }

    private static List<String> lowercase(List<String> list) {
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext())
            iterator.set(iterator.next().toLowerCase());
        return list;
    }
}

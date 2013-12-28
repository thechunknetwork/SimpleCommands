package me.vemacs.bungee;

import lombok.Getter;
import me.vemacs.bungee.command.TidbitCommand;
import me.vemacs.bungee.misc.ConfigUtils;
import me.vemacs.bungee.misc.providers.BungeeCordProvider;
import me.vemacs.bungee.misc.providers.RedisBungeeProvider;
import me.vemacs.bungee.misc.providers.ServerInfoProvider;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleCommands extends Plugin {
    @Getter
    private static SimpleCommands plugin;
    @Getter
    private static ServerInfoProvider provider;
    @Getter
    private static Set<Command> commands = new HashSet<>();
    @Getter
    private static Map<String, String> tidbits;

    @Override
    public void onEnable() {
        plugin = this;
        if (getProxy().getPluginManager().getPlugin("RedisBungee") != null)
            provider = new RedisBungeeProvider();
        else
            provider = new BungeeCordProvider();
        getLogger().info("Using " + provider.getClass().getSimpleName() + " as provider.");
        load();
    }

    public static void load() {
        loadConfigs();
        registerCommands();
    }

    private static void loadConfigs() {
        tidbits = new ConcurrentHashMap<>();
        for (Map.Entry<?, ?> entry : ConfigUtils.loadMapFrom(ConfigUtils.loadResource(getPlugin(), "tidbits.yml")).entrySet()) {
            tidbits.put((String) entry.getKey(), (String) entry.getValue());
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
        for (Command cmd : commands)
            getPlugin().getProxy().getPluginManager().registerCommand(getPlugin(), cmd);
    }
}

package me.vemacs.bungee.misc.providers;

import me.vemacs.bungee.SimpleCommands;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashSet;
import java.util.Set;

public class BungeeCordProvider implements ServerInfoProvider {
    private static ProxyServer bungeeCord = SimpleCommands.getPlugin().getProxy();

    @Override
    public int getTotalPlayerCount() {
        return bungeeCord.getOnlineCount();
    }

    @Override
    public Set<String> getPlayersOnServer(String server) {
        Set<String> players = new HashSet<>();
        for (ProxiedPlayer p : bungeeCord.getServerInfo(server).getPlayers())
            players.add(p.getName());
        return players;
    }
}
